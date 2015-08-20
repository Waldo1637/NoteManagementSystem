package rms.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import rms.model.item.ItemThread;

/**
 * Stores references to all items
 *
 * @author Timothy
 */
public class State implements Serializable {

    private static final long serialVersionUID = 01L;

    private int nextItemNumber;
    private int nextThreadNumber;
    private final Set<ItemThread> threads;
    private final Set<Tag> tags;

    public State() {
        nextItemNumber = 0;
        nextThreadNumber = 0;
        threads = new HashSet<>();
        tags = new HashSet<>();
    }

    public int getNextItemNumber() {
        return ++nextItemNumber;
    }

    public int getNextThreadNumber() {
        return ++nextThreadNumber;
    }

    public ItemThread createNewThread() {
        ItemThread t = new ItemThread();
        this.threads.add(t);
        return t;
    }

    public Set<ItemThread> getThreads() {
        return threads;
    }

    public Set<Tag> getTagsUnmodifiable() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Creates a new {@link Tag} with the given name, adds it to the State, and
     * returns the new Tag.
     *
     * @param name
     * @return
     */
    public Tag newTag(String name) {
        Tag t = new Tag(name);
        tags.add(t);
        return t;
    }

    /**
     * Permanantly delete the given {@link Tag}s from the State (including from
     * all {@link ItemThread}s which reference the Tags).
     *
     * @param tagsToDelete
     */
    public void deleteTags(Set<Tag> tagsToDelete) {
        tags.removeAll(tagsToDelete);
        for(ItemThread t : threads){
            t.getTags().removeAll(tagsToDelete);
        }
    }
}
