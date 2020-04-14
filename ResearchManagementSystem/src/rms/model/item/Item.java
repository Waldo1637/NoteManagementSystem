package rms.model.item;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import rms.control.Main;

/**
 *
 * @author Timothy
 */
public abstract class Item implements Serializable {

    private static final long serialVersionUID = 01L;

    protected static final Logger LOG = Logger.getLogger(Item.class.getName());

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

    public Item getPreviousItem() {
        int pIndex = parentThread.indexOf(this) - 1;
        return pIndex < 0 ? null : parentThread.get(pIndex);
    }

    public Item getNextItem() {
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

    @Override
    public String toString() {
        return String.format("{ID=%s,name=%s,type=%s,parentID=%s}", itemID, name, getItemTypeName(), parentThread.getID());
    }

    protected boolean appendToParentThread() {
        if (parentThread.indexOf(this) >= 0) {
            LOG.log(Level.WARNING, "Failed to add Item {0} to parent thread (already there!)", this);
            return false;
        } else {
            boolean result = parentThread.add(this);
            if (!result) {
                LOG.log(Level.WARNING, "Failed to add Item {0} to parent thread", this);
            }
            return result;
        }
    }

    public abstract String getItemTypeName();
}
