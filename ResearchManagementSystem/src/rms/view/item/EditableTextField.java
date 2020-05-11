package rms.view.item;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.Timer;
import javax.swing.UIDefaults;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.*;
import javax.swing.text.html.*;
import rms.control.search.ItemNumberFilter;
import rms.model.item.TextItem;
import rms.view.MainFrame;
import rms.view.util.CustomHTMLEditorKit;
import rms.view.util.CustomStyledEditorKit;
import rms.view.util.Prompts;
import rms.view.util.UndoRedoProvider;

/**
 *
 * @author Timothy
 */
public final class EditableTextField extends javax.swing.JPanel {

    private static final Logger LOG = Logger.getLogger(EditableTextField.class.getName());
    //
    private static final boolean DEBUG_CONVERSION = false;
    //
    private static final String SAVE = "Save";
    private static final String EDIT = "Edit";
    //
    private static final Pattern ITEM_REF_PATT = Pattern.compile("^#(\\d+)$");
    //
    private static final String LINK_TAG = "link";
    private static final String LINK_TAG_OPEN = "<" + LINK_TAG + ">";
    private static final String LINK_TAG_CLOSE = "</" + LINK_TAG + ">";
    //Pattern matches any string of at least one character between the LINK_TAGs
    //  and uses the reluctant quantifier (i.e. ?) for the shortest match to
    //  ensure that each pair of link tags in a line forms its own link.
    private static final Pattern LINK_PATT = Pattern.compile(LINK_TAG_OPEN + "(.+?)" + LINK_TAG_CLOSE);
    //
    private static final Map<String, String> HTML_REP_MAP;
    //NOTE: groups 0 and 1 are equal since () wraps the entire thing
    private static final Pattern HTML_REPLACE_PATT;

    static {
        HashMap<String, String> tempHtmlRepMap = new HashMap<>();
        //Specify replacements necessary for HTML to display properly
        tempHtmlRepMap.put("<", "&#60;");
        tempHtmlRepMap.put(">", "&#62;");
        tempHtmlRepMap.put(" ", "&#160;");
        tempHtmlRepMap.put("\t", "&#09;");
        //Build the pattern to match all of the HTML replacements needed
        String s = '(' + LINK_PATT.pattern();//must come first, contains <>
        for (Map.Entry<String, String> e : tempHtmlRepMap.entrySet()) {
            s += '|' + e.getKey();
        }
        s += ')';
        HTML_REPLACE_PATT = Pattern.compile(s);
        HTML_REP_MAP = Collections.unmodifiableMap(tempHtmlRepMap);
    }

    private final JButton saveEditButton;

    /**
     * Should contain the HTML form of the text. The exception is the initial
     * value with may be 'null' (visually the same as empty) or non-HTML (but it
     * will be converted as soon as the field is edited).
     */
    private String lastSavedText;

    /**
     * No-arg constructor for Bean creation. NOTE: Do not use.
     */
    public EditableTextField() {
        saveEditButton = null;
        lastSavedText = null;
        initComponents();
    }

    /**
     * Creates new form EditableTextField
     *
     * @param text
     * @param saveEditButton
     */
    public EditableTextField(String text, JButton saveEditButton) {
        if (saveEditButton == null) {
            throw new IllegalArgumentException();
        }
        this.lastSavedText = text;
        this.saveEditButton = saveEditButton;
        initComponents();
        //Initialize text pane content
        saveAction(true);
        //Add button listener
        saveEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                final String txt = EditableTextField.this.saveEditButton.getText();
                switch (txt) {
                    case SAVE:
                        saveAction(false);
                        break;
                    case EDIT:
                        editAction();
                        break;
                    default:
                        throw new IllegalStateException("Invalid button text: " + txt);
                }
            }
        });
        jTextPaneDesc.addHyperlinkListener(new HyperlinkListener() {
            private String previousTooltip;

            @Override
            public void hyperlinkUpdate(HyperlinkEvent evt) {
                if (evt.getSource() == jTextPaneDesc) {
                    HyperlinkEvent.EventType evtType = evt.getEventType();
                    if (HyperlinkEvent.EventType.ACTIVATED == evtType) {
                        followLink(evt.getURL(), evt.getDescription());
                    } else if (HyperlinkEvent.EventType.ENTERED == evtType) {
                        Element elem = evt.getSourceElement();
                        if (elem != null) {
                            AttributeSet a = (AttributeSet) elem.getAttributes().getAttribute(HTML.Tag.A);
                            if (a != null) {
                                previousTooltip = jTextPaneDesc.getToolTipText();//backup
                                jTextPaneDesc.setToolTipText("Go to " + a.getAttribute(HTML.Attribute.TITLE));
                            }
                        }
                    } else if (HyperlinkEvent.EventType.EXITED == evtType) {
                        jTextPaneDesc.setToolTipText(previousTooltip);//restore
                    }
                } else {
                    LOG.log(Level.WARNING, "Unexpected hyperlink source: {0}", evt.getSource());
                }
            }

            private void followLink(URL url, String description) {
                if (url == null) {
                    Matcher matcher = ITEM_REF_PATT.matcher(description);
                    if (matcher.matches()) {
                        int id = Integer.parseInt(matcher.group(1));//parse will not fail because only digits matched
                        MainFrame.instance().refreshThreadListAndDisplay(ItemNumberFilter.get(id), null);
                        return;//nothing more to do
                    } else {
                        try {
                            //Just try to create a URL from the 'description'
                            url = new URL("http", description, "");
                        } catch (MalformedURLException ex) {
                            LOG.log(Level.INFO, "Unable to create URL from " + description, ex);
                            Prompts.informUser("Error!", "Unable to open URL:\n" + description, Prompts.PromptType.WARNING);
                        }
                    }
                }
                if (url != null) {
                    final URL url_final = url;
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Desktop.getDesktop().browse(url_final.toURI());
                            } catch (Exception ex) {
                                LOG.log(Level.INFO, "Unable to open URL " + url_final, ex);
                                Prompts.informUser("Error!", "Unable to open URL:\n" + url_final, Prompts.PromptType.WARNING);
                            }
                        }
                    });
                }
            }
        });
        UndoRedoProvider.addTo(jTextPaneDesc);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setPreferredSize(new java.awt.Dimension(6, 22));

        jTextPaneDesc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextPaneDescFocusLost(evt);
            }
        });
        jTextPaneDesc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextPaneDescKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextPaneDesc)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextPaneDesc)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextPaneDescFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextPaneDescFocusLost
        if (evt.getOppositeComponent() != saveEditButton) {//already handled in that case
            saveAction(false);
        }
    }//GEN-LAST:event_jTextPaneDescFocusLost

    private void jTextPaneDescKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextPaneDescKeyTyped
        preserveSize();
    }//GEN-LAST:event_jTextPaneDescKeyTyped

    private void editAction() {
        //Convert the 'jTextPaneDesc' content to non-HTML form for editing
        String converted = getAndConvertContent(true);
        //Create a new non-HTML EditorKit and set the converted text
        jTextPaneDesc.setEditorKit(new CustomStyledEditorKit(36));
        jTextPaneDesc.setText(converted);
        //Make the content area editable
        makeEditable(true);
        //Try requesting focus every 5 ms until successful to ensure that
        //  the focusLost(..) event can be reliably captured.
        final Timer focusTimer = new Timer(5, null);
        focusTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jTextPaneDesc.requestFocusInWindow()) {
                    focusTimer.stop();
                }
            }
        });
        focusTimer.start();
    }

    private void saveAction(boolean initial) {
        if (jTextPaneDesc.isEditable()) {//skip if pane was not in edit mode
            //Convert the 'jTextPaneDesc' content to HTML form for storage and to
            //  allow links to be clicked (unless this call is for initialization)
            final String converted = initial
                    ? this.lastSavedText
                    : getAndConvertContent(false);
            if (DEBUG_CONVERSION) {
                System.out.printf("[saveAction] converted=|%s| %n",
                        toStringDebug(converted, false));
            }
            //Create a new HTML EditorKit and set the converted text
            jTextPaneDesc.setEditorKit(addStyles(new CustomHTMLEditorKit(36)));
            jTextPaneDesc.setText(converted);
            //Make the content area non-editable
            makeEditable(false);

            //If the converted content has changed from the last time saveAction()
            //  was called, then fire an ItemTextUpdatedEvent.
            if (!Objects.equals(this.lastSavedText, converted)) {
                if (DEBUG_CONVERSION) {
                    System.out.printf("[saveAction] old=|%s| %n",
                            toStringDebug(this.lastSavedText, false));
                }
                this.lastSavedText = converted;
                fireItemTextUpdatedEvent(converted);
            }
        }
    }

    private static HTMLEditorKit addStyles(HTMLEditorKit kit) {
        StyleSheet sty = kit.getStyleSheet();
        sty.addRule("p {margin:0; padding:0; }");//no space between <p>
        return kit;
    }

    private static String toStringDebug(String s, boolean charMap) {
        if (DEBUG_CONVERSION) {
            if (s == null) {
                return null;
            } else if (charMap) {
                char[] chars = s.toCharArray();
                int iMax = chars.length - 1;
                if (iMax == -1) {
                    return "[]";
                }

                StringBuilder b = new StringBuilder();
                b.append('[');
                for (int i = 0;; i++) {
                    char c = chars[i];
                    if (32 < c && c < 127) {
                        b.append(c);
                    } else {
                        b.append("(").append((int) c).append(")");
                    }
                    if (i == iMax) {
                        return b.append(']').toString();
                    }
                    b.append(", ");
                }
            }
        }
        return s;
    }

    private void makeEditable(boolean editable) {
        jTextPaneDesc.setEditable(editable);
        if (editable) {
            changeDescPaneColor(Color.WHITE);
            saveEditButton.setText(SAVE);
        } else {
            changeDescPaneColor(Color.LIGHT_GRAY);
            saveEditButton.setText(EDIT);
        }
        preserveSize();
        //NOTE: When converting the HTML to editable form, there is an extra
        //  line added to the end and sometimes attempting to type in that line
        //  causes strange text placement (ex: it appears on the next line 
        //  which isn't even visible). Furthermore, in non-edit mode, this will
        //  prevent the window from automatically scrolling down to the bottom
        //  of the text pane when the content is long.
        jTextPaneDesc.setCaretPosition(0);
    }

    private void changeDescPaneColor(Color c) {
        UIDefaults defaults = new UIDefaults();
        defaults.put("TextPane[Enabled].backgroundPainter", c);
        jTextPaneDesc.putClientProperty("Nimbus.Overrides", defaults);
        jTextPaneDesc.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
        jTextPaneDesc.setBackground(c);
    }

    /**
     * There is a bug that causes the {@link javax.swing.JTextPane} to be
     * redrawn at default size unless
     * {@link javax.swing.JTextPane#getPreferredSize()} is called immediately
     * after actions that cause a repaint (such as setting the text).
     */
    private void preserveSize() {
        jTextPaneDesc.getPreferredSize();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JTextPane jTextPaneDesc = new javax.swing.JTextPane();
    // End of variables declaration//GEN-END:variables

    /**
     * Event fired when the text field of the {@link TextItem} has been updated
     * to reflect the data entered in the UI by the user.
     *
     * @author Timothy
     */
    public static class ItemTextUpdatedEvent extends EventObject {

        private final String newText;

        public ItemTextUpdatedEvent(Object source, String newText) {
            super(source);
            this.newText = newText;
        }

        public String getNewText() {
            return newText;
        }
    }

    /**
     * Listener interface for ItemTextUpdatedEvent events
     */
    public static interface ItemTextUpdateListener {

        public void textUpdated(ItemTextUpdatedEvent event);
    }

    private final ArrayList<ItemTextUpdateListener> listeners = new ArrayList<>();

    public void addItemTextUpdateListener(ItemTextUpdateListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    public void removeItemTextUpdateListener(ItemTextUpdateListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    private void fireItemTextUpdatedEvent(String newText) {
        ItemTextUpdatedEvent event = new ItemTextUpdatedEvent(this, newText);
        synchronized (listeners) {
            for (ItemTextUpdateListener l : listeners) {
                l.textUpdated(event);
            }
        }
    }

    /**
     * If {@code jTextPaneDesc.getStyledDocument()} is {@link HTMLDocument},
     * convert the {@code <body>} content to plain text with links, otherwise
     * convert to HTML form.
     *
     * @return
     */
    private String getAndConvertContent(boolean expectingHTML) {
        StyledDocument doc = jTextPaneDesc.getStyledDocument();
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
            if (DEBUG_CONVERSION) {
                System.out.printf("[getAndConvertContent] in=|%s| out=|%s| %n",
                        jTextPaneDesc.getText(), os.toString());
            }
            return os.toString();
        } catch (IOException | BadLocationException ex) {
            LOG.log(Level.SEVERE, "Conversion exception", ex);
            return null;
        }
    }

    private static void writeDocAsHTML(Writer out, StyledDocument plainDoc) throws IOException, BadLocationException {
        new MinimalHTMLWriter(out, plainDoc) {

            {
                //Disable max line length to condense HTML content
                setCanWrapLines(false);
            }

            @Override
            protected String getText(Element elem) throws BadLocationException {
                return sanitize(super.getText(elem), true);
            }

            /**
             * This is used to sanitize the entire text of a each "paragraph"
             * (i.e. {@code isTopLevel==true}) and also the text within a link
             * body (i.e. {@code isTopLevel==false}).
             *
             * @param text
             * @param isTopLevel
             *
             * @return
             */
            private String sanitize(String text, boolean isTopLevel) {
                StringBuffer sb = new StringBuffer();
                Matcher matcher = HTML_REPLACE_PATT.matcher(text);
                while (matcher.find()) {
                    String repWith = HTML_REP_MAP.get(matcher.group());
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
                        if (!LINK_PATT.matcher(matcher.group()).matches()) {
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
                .write();
        out.flush();
    }

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
                    String name = LINK_TAG_OPEN + link + LINK_TAG_CLOSE;
                    out.write(name);
                }
            }
        }
        out.flush();
    }

    /**
     * This method should be called before storing the State to file to ensure
     * that any modifications in the current text field are stored to the State.
     */
    public static void ensureModificationsAreStored() {
        //NOTE: In most scenarios, it would be sufficient for the MainFrame to
        //  request focus or use KeyboardFocusManager.clearGlobalFocusOwner(). 
        //  However, doing that after the window closing action has fired
        //  doesn't seem to have any effect. Instead, we determine if some 
        //  instance of the 'jTextPaneDesc' owns the focus and directly call
        //  the saveAction() method on the parent.
        Component o = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        if (o != null) {
            Container parent = o.getParent();
            if (parent instanceof EditableTextField) {
                ((EditableTextField) parent).saveAction(false);
            }
        }
    }
}
