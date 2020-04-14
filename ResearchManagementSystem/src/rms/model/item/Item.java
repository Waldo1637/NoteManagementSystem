package rms.model.item;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import rms.control.Main;
import rms.util.DateHelpers;

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
        this.created = DateHelpers.now();
        this.modified = DateHelpers.clone(created);
        this.name = name;
    }

    protected Item(ItemThread parentThread) {
        this(parentThread, "");
    }

    //copy constructor
    protected Item(ItemThread parentThreadForCopy, Item toCopy) {
        this(parentThreadForCopy, toCopy.name);//it's assigned a new ID and new Dates
    }

    public static class CopyOptions {

        /**
         * NOTE: package access to internal structure to allow only subclasses
         * of Item (in the current package) to add/remove/check the contents.
         */
        /*package*/ HashMap<String, String> data = new HashMap<>();
    }

    /**
     *
     * @param parentThreadForCopy
     * @param opts                Map of options for copying (may be 'null')
     *
     * @return
     */
    public abstract Item duplicateInThread(ItemThread parentThreadForCopy, CopyOptions opts);

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
        this.modified = DateHelpers.now();
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
