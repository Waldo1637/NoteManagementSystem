package rms.model.item;

import java.io.Serializable;

/**
 *
 * @author Timothy
 */
public final class EmptyFileItem extends Item implements Serializable {

    private static final long serialVersionUID = 01L;

    protected EmptyFileItem(ItemThread parentThread) {
        super(parentThread);
    }

    //copy constructor
    protected EmptyFileItem(ItemThread parentThreadForCopy, EmptyFileItem toCopy) {
        super(parentThreadForCopy, toCopy);
    }

    @Override
    public EmptyFileItem duplicateInThread(ItemThread parentThreadForCopy, CopyOptions opts) {
        return new EmptyFileItem(parentThreadForCopy, this);
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
