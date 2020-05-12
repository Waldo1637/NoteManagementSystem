package rms.util;

import java.awt.EventQueue;
import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.MinimalHTMLWriter;
import rms.view.util.Prompts;

/**
 *
 * @author Timothy Hoffman
 */
public final class TextConversion {

    private static final Logger LOG = Logger.getLogger(TextConversion.class.getName());

    // thread-safe lazy implementation pattern
    public static class LINK {

        public static final String TAG_NAME = "link";
        public static final String OPEN_TAG = "<" + TAG_NAME + ">";
        public static final String CLOSE_TAG = "</" + TAG_NAME + ">";

        /**
         * Pattern matches any string of at least one character between
         * {@link #OPEN_TAG} and {@link #CLOSE_TAG} and uses the reluctant
         * quantifier (i.e. ?) for the shortest match to ensure that each
         * consecutive pair of link tags in a single line matches independently.
         */
        public static final Pattern PATTERN = Pattern.compile(OPEN_TAG + "(.+?)" + CLOSE_TAG);

        private LINK() {
        }
    }

    // thread-safe lazy implementation pattern
    public static class HTML_REPLACE {

        /**
         *
         */
        public static final Map<String, String> MAP;

        /**
         * NOTE: groups 0 and 1 are equal since () wraps the entire thing
         */
        public static final Pattern PATTERN;

        static {
            HashMap<String, String> tempHtmlRepMap = new HashMap<>();
            //Specify replacements necessary for HTML to display properly
            tempHtmlRepMap.put("<", "&#60;");
            tempHtmlRepMap.put(">", "&#62;");
            tempHtmlRepMap.put(" ", "&#160;");
            tempHtmlRepMap.put("\t", "&#09;");
            //Build the pattern to match all of the HTML replacements needed
            String s = '(' + LINK.PATTERN.pattern();//must come first, contains <>
            for (Map.Entry<String, String> e : tempHtmlRepMap.entrySet()) {
                s += '|' + e.getKey();
            }
            s += ')';
            PATTERN = Pattern.compile(s);
            MAP = Collections.unmodifiableMap(tempHtmlRepMap);
        }

        private HTML_REPLACE() {
        }
    }

    // thread-safe lazy implementation pattern
    public static class HTML_CONTENT {

        /**
         * Matches any HTML body (including newlines within the content).
         */
        public static final Pattern PATTERN = Pattern.compile("^<html>.*</html>$", Pattern.DOTALL);

        private HTML_CONTENT() {
        }
    }

    /**
     *
     * NOTE: if returning {@code null} an error will have been logged to the
     * logger for this class
     *
     * @param plainText
     *
     * @return
     */
    public static String convertPlainTextToHTML(String plainText) {
        DefaultStyledDocument doc = new DefaultStyledDocument();
        try {
            doc.insertString(0, plainText, null);
            return convertDocumentContent(doc, false);
        } catch (BadLocationException ex) {
            LOG.log(Level.SEVERE, "Conversion exception", ex);
            return null;
        }
    }

    /**
     * If the {@link StyledDocument} is an {@link HTMLDocument}, convert the
     * {@code <body>} content to plain text with links, otherwise convert to
     * HTML form.
     *
     * NOTE: if returning {@code null} an error will have been logged to the
     * logger for this class
     *
     * @param doc
     * @param expectingHTML
     *
     * @return
     *
     * @throws IllegalStateException if {@code expectingHTML==true} and the
     *                               document is not an {@link HTMLDocument}
     */
    public static String convertDocumentContent(StyledDocument doc, boolean expectingHTML) {
        if (expectingHTML != (doc instanceof HTMLDocument)) {
            throw new IllegalStateException("Expecting HTML? " + expectingHTML + ", found " + doc.getClass());
        }
        try (OutputStream os = new ByteArrayOutputStream();
                Writer w = new OutputStreamWriter(os)) {
            if (expectingHTML) {//i.e. (doc instanceof HTMLDocument)
                writeDocAsPlainTextWithLinks(w, (HTMLDocument) doc);
            } else {
                writeDocAsHTML(w, doc);
            }
            return os.toString();
        } catch (IOException | BadLocationException ex) {
            LOG.log(Level.SEVERE, "Conversion exception", ex);
            return null;
        }
    }

    private static class CustomHTMLWriter extends MinimalHTMLWriter {

        public CustomHTMLWriter(Writer out, StyledDocument plainDoc) {
            super(out, plainDoc);
            //Disable max line length to condense HTML content
            setCanWrapLines(false);
        }

        @Override
        protected String getText(Element elem) throws BadLocationException {
            return sanitize(super.getText(elem), true);
        }

        /**
         * This is used to sanitize the entire text of a each "paragraph" (i.e.
         * {@code isTopLevel==true}) and also the text within a link body (i.e.
         * {@code isTopLevel==false}).
         *
         * @param text
         * @param isTopLevel
         *
         * @return
         */
        private String sanitize(String text, boolean isTopLevel) {
            StringBuffer sb = new StringBuffer();
            Matcher matcher = HTML_REPLACE.PATTERN.matcher(text);
            while (matcher.find()) {
                String repWith = HTML_REPLACE.MAP.get(matcher.group());
                if (repWith == null) {
                    //The reluctant regular expression will process nested link
                    //  tags (ex: <link><link>hi</link></link>) by matching the
                    //  first <link> with the first </link>, leaving the other
                    //  pair separated with the opening tag inside and the
                    //  closing tag outside. Thus, this case can only happen
                    //  at the top level, not in the recursive call (and since
                    //  the recursive call only occurs in this case, it means
                    //  there will only ever be one level of recursion).
                    if (!isTopLevel) {
                        throw new IllegalStateException("Unknown nested match fragment: " + matcher.group());
                    }
                    //Sanity check: when the match is not in the Map, the
                    //  only other option is a <link>...</link>
                    if (!LINK.PATTERN.matcher(matcher.group()).matches()) {
                        throw new IllegalStateException("Unknown match fragment: " + matcher.group());
                    }
                    //NOTE: The link body will be group 2.
                    String group2 = matcher.group(2);
                    //Cannot directly use $2 in the appendReplacement(..)
                    //  because some sanitization must happen (for both the
                    //  href/title use and the tag content use.
                    //NOTE: 'title' is used for the tooltip.
                    matcher.appendReplacement(sb, "<a href=\"");
                    //The href/title uses must be sanitized to remove double quotes
                    if (group2.contains("\"")) {
                        group2 = group2.replaceAll("\"", "%22");
                        //Notify the user of the change but asynchronously
                        //  so that the content conversion can complete in
                        //  the background.
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                Prompts.informUser("Illegal character",
                                        "Link/URL cannot contain double quote (\")."
                                        + "\nIt will be replaced with %22.",
                                        Prompts.PromptType.INFO);
                            }
                        });
                    }
                    sb.append(group2).append("\" title=\"").append(group2).append("\">");
                    //Content should be further sanitized so that certain characters
                    //  are just displayed and not treated as HTML syntax.
                    sb.append(sanitize(group2, false)).append("</a>");
                } else {
                    matcher.appendReplacement(sb, repWith);
                }
            }
            matcher.appendTail(sb);

            return sb.toString();
        }

        @Override

        protected void writeHeader() throws IOException {
            //Write a condensed header
            write("<head></head>");
        }

        @Override
        protected void indent() throws IOException {
            //Skip all indentation to condense HTML content
        }

        @Override
        protected void write(char ch) throws IOException {
            //Skip direct write of NEWLINE to condense HTML content
            if (NEWLINE != ch) {
                super.write(ch);
            }
        }

        @Override
        protected void writeNonHTMLAttributes(AttributeSet attr) throws IOException {
            //Do not generate the <span> tag with styling for each paragraph
            //It was forcing links to be black and overall caused excess
            //  clutter in the HTML text. A better solution might be to
            //  figure out where the style attributes are even coming from
            //  and remove them at the source. However, I'm not sure how to
            //  do that at this time.
        }
    }

    /**
     *
     * @param out
     * @param plainDoc
     *
     * @throws IOException
     * @throws BadLocationException
     */
    private static void writeDocAsHTML(Writer out, StyledDocument plainDoc) throws IOException, BadLocationException {
        new CustomHTMLWriter(out, plainDoc).write();
        out.flush();
    }

    /**
     *
     * @param out
     * @param doc
     *
     * @throws IOException
     * @throws BadLocationException
     */
    private static void writeDocAsPlainTextWithLinks(Writer out, HTMLDocument doc) throws IOException, BadLocationException {
        Segment data = new Segment();
        //NOTE: I had previously tried "doc.getIterator(HTML.Tag.CONTENT)" but
        //  that also returned elements in the head, not just the body.
        final HTML.Tag cTag = HTML.Tag.CONTENT;
        Element body = doc.getElement(doc.getDefaultRootElement(), StyleConstants.NameAttribute, HTML.Tag.BODY);
        ElementIterator itr = new ElementIterator(body);//traverse the body
        for (Element elem = itr.next(); elem != null; elem = itr.next()) {
            AttributeSet a = elem.getAttributes();
            if (a.isDefined(cTag) || a.getAttribute(StyleConstants.NameAttribute) == cTag) {//content elements
                Object anchorTag = a.getAttribute(HTML.Tag.A);
                if (anchorTag == null) {
                    int start = elem.getStartOffset();
                    int end = elem.getEndOffset();
                    doc.getText(start, end - start, data);
                    out.write(data.array, data.offset, data.count);
                } else {
                    SimpleAttributeSet asAttrs = (SimpleAttributeSet) anchorTag;
                    Object link = asAttrs.getAttribute(HTML.Attribute.HREF);
                    String name = LINK.OPEN_TAG + link + LINK.CLOSE_TAG;
                    out.write(name);
                }
            }
        }
        out.flush();
    }

    private TextConversion() {
    }
}
