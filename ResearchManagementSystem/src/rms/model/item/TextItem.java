package rms.model.item;

import java.util.Objects;
import rms.util.Helpers;

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

    @Override
    public abstract String getItemTypeName();
}
