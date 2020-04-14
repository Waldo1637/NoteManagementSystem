package rms.model.item;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import rms.control.Main;
import rms.model.Tag;
import rms.util.DateHelpers;

/**
 *
 * @author Timothy
 */
public class ItemThread implements Iterable<Item>, Serializable {

    private static final long serialVersionUID = 01L;

    private final ArrayList<Item> data;
    private final int threadID;
    private File dataFolder;
    protected Date modified;
    protected String name;

    @Deprecated
    protected final Set<Tag> tags;

    public ItemThread() {
        this(null);
    }

    public ItemThread(String name) {
        this.data = new ArrayList<>();
        this.threadID = Main.getState().getNextThreadNumber();
        this.dataFolder = null;
        setName(name);
        tags = null;
        touch();
    }

    //copy constructor
    public ItemThread(ItemThread existing, boolean changeFileToPlaceholder) {
        this.data = new ArrayList<>(existing.data.size());//same size
        this.threadID = Main.getState().getNextThreadNumber();//new number
        this.dataFolder = null;//new folder will be generated on-demand
        setName(existing.name);//copy the name of the existing thread
        tags = existing.tags == null ? null : new HashSet<>(existing.tags);
        //After everything else is setup, copy all items into the new thread
        Item.CopyOptions opts = new Item.CopyOptions();
        FileItem.setUsePlaceholder(opts, changeFileToPlaceholder);
        for (Item i : existing.data) {
            this.data.add(i.duplicateInThread(this, opts));
        }
        //Add the tags from the original thread
        this.addTags(existing.getTagsUnmodifible());
        //And finally, set the modification time
        touch();
    }

    protected boolean add(Item i) {
        boolean added = data.add(i);
        if (added) {
            touch();
        }
        return added;
    }

    public boolean remove(Item i) {
        boolean removed = data.remove(i);
        if (removed) {
            touch();
        }
        return removed;
    }

    public boolean replace(Item oldItem, Item newItem) {
        int idx = data.indexOf(oldItem);
        if (idx < 0) {
            return false;
        } else {
            Item removed = data.set(idx, newItem);
            assert oldItem.equals(removed);//only concurrent modification could make this fail?
            touch();
            return true;
        }
    }

    public int indexOf(Item i) {
        return data.indexOf(i);
    }

    public Item get(int index) {
        return data.get(index);
    }

    public int size() {
        return data.size();
    }

    public File getDataFolder() {
        if (dataFolder == null) {
            File root = new File("files");
            dataFolder = new File(root, String.format("%09d", this.threadID));
            dataFolder.mkdirs();
        }
        return dataFolder;
    }

    public final void touch() {
        this.modified = DateHelpers.now();
    }

    public Date getModificationTime() {
        return modified;
    }

    public void addTags(Set<Tag> tags) {
        for (Tag t : tags) {
            Main.getState().addTagToThread(this, t);
        }
    }

    public void removeTags(Set<Tag> tags) {
        for (Tag t : tags) {
            Main.getState().removeTagFromThread(this, t);
        }
    }

    public Set<Tag> getTagsUnmodifible() {
        return Main.getState().getTagsForThread(this);
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return threadID;
    }

    public final void setName(String newName) {
        name = (newName == null || newName.isEmpty()) ? "<untitled>" : newName;
    }

    @Override
    public Iterator<Item> iterator() {
        return data.iterator();
    }

    @Override
    public String toString() {
        return String.format("%s [%2$tm/%2$te/%2$ty %2$tR]", name, modified);
    }

    @Deprecated
    public Set<Tag> getTagsForClassSerializationRevision0() {
        return tags;
    }
}
