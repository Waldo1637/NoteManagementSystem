package rms.model.item;

/**
 *
 * @author Timothy
 */
public abstract class TextItem extends Item {

    private static final long serialVersionUID = 01L;

    private String text;

    public TextItem(ItemThread parentThread, String text) {
        super(parentThread);
        this.text = text;
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
