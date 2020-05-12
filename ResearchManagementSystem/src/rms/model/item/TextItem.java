package rms.model.item;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Objects;
import java.util.logging.Level;
import rms.util.Helpers;
import rms.util.TextConversion;

/**
 *
 * @author Timothy
 */
public abstract class TextItem extends Item {

    private static final long serialVersionUID = 01L;

    //NOTE: treat 'null' as the empty string
    private String text;

    protected TextItem(ItemThread parentThread, String text) {
        super(parentThread);
        this.text = text;
    }

    //copy constructor
    protected TextItem(ItemThread parentThreadForCopy, TextItem toCopy) {
        super(parentThreadForCopy, toCopy);
        this.text = toCopy.text;//String is immutable
    }

    public String getText() {
        return Helpers.convertNullToEmpty(text);
    }

    /**
     *
     * @param newText
     *
     * @return {@code true} iff the content was changed (i.e. the given
     *         {@link String} is not equal to the existing text {@link String}).
     */
    public boolean replaceText(String newText) {
        if (Objects.equals(text, newText)) {
            return false;//don't replace if equal
        } else if (Helpers.isNullOrEmpty(text) && Helpers.isNullOrEmpty(newText)) {
            return false;//don't replace if both are 'null' or empty
        } else {
            text = newText;
            touch();
            return true;//replaced
        }
    }

    /**
     * NOTE: The newline is appended even if the given {@link String} is null or
     * empty.
     *
     * @param newText
     *
     * @return {@code true}.
     */
    public boolean appendText(String newText) {
        text += System.lineSeparator() + Helpers.convertNullToEmpty(newText);
        touch();
        return true;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        //de-serialize
        in.defaultReadObject();

        //If the 'text' field is not in HTML format, convert it
        //NOTE: This step is necessary to not lose formatting since the
        //  EditableTextField started using HTML to diplay the text/links.
        final String originalText = this.text;
        if (originalText != null) {
            if (!TextConversion.HTML_CONTENT.PATTERN.matcher(originalText).matches()) {
                String converted = TextConversion.convertPlainTextToHTML(originalText);
                //Only replace if non-null. If null, the conversion failed (an error
                //  was logged by TextConversion) so just keep the existing text and
                //  the layout/formatting may be lost and any HTML tags appearing in
                //  the content will likely be lost after a couple of edit/save 
                //  cycles on the text pane that holds this item. But I don't know
                //  if there's much else that I could do about it.
                if (converted != null) {
                    this.text = converted;
                } else {
                    LOG.log(Level.WARNING, "Failed to convert item {0} to HTML: {1}", new Object[]{this.itemID, this.text});
                }
            }
        }
    }
}
