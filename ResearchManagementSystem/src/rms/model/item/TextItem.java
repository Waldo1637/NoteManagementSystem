package rms.model.item;

/**
 *
 * @author Timothy
 */
public abstract class TextItem extends Item {

    private static final long serialVersionUID = 01L;

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
        return text;
    }

    public void replaceText(String newText) {
        touch();
        text = newText;
    }

    public void appendText(String newText) {
        touch();
        text = text.concat("\n").concat(newText);
    }

    @Override
    public abstract String getItemTypeName();
}
