package rms.model.item;

import java.io.Serializable;

/**
 *
 * @author Timothy
 */
public class EmptyFileItem extends Item implements Serializable {

    private static final long serialVersionUID = 01L;

    protected EmptyFileItem(ItemThread parentThread) {
        super(parentThread);
    }

    @Override
    public String getItemTypeName() {
        return "File Holder";
    }

    public static EmptyFileItem createAndAddEmptyFileItem(ItemThread parentThread) {
        EmptyFileItem i = new EmptyFileItem(parentThread);
        i.appendToParentThread();
        return i;
    }
}
