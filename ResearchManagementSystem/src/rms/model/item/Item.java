package rms.model.item;

import java.io.Serializable;
import java.util.Date;
import rms.control.Main;

/**
 *
 * @author Timothy
 */
public abstract class Item implements Serializable {

    private static final long serialVersionUID = 01L;

    protected final int itemID;
    protected final ItemThread parentThread;
    protected final Date created;
    protected Date modified;
    protected String name;

    protected Item(ItemThread parentThread, String name) {
        if (parentThread == null) {
            throw new IllegalArgumentException("Parent thread cannot be null");
        }

        this.itemID = Main.getState().getNextItemNumber();
        this.parentThread = parentThread;
        this.created = new Date();
        this.modified = (Date) created.clone();
        this.name = name;
    }

    protected Item(ItemThread parentThread) {
        this(parentThread, "");
    }

    public int getID() {
        return itemID;
    }

    public ItemThread getThread() {
        return parentThread;
    }

    public Item getParent() {
        int pIndex = parentThread.indexOf(this) - 1;
        return pIndex < 0 ? null : parentThread.get(pIndex);
    }

    public Item getChild() {
        int cIndex = parentThread.indexOf(this) + 1;
        return cIndex >= parentThread.size() ? null : parentThread.get(cIndex);
    }

    public Date getCreationTime() {
        return created;
    }

    public Date getModificationTime() {
        return modified;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void touch() {
        this.modified = new Date();
        this.getThread().touch();
    }

    public abstract String getItemTypeName();
}
