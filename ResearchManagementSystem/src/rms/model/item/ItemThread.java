package rms.model.item;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import rms.control.Main;
import rms.model.Tag;

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
        this.modified = new Date();
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
