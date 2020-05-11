package rms.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import rms.model.item.ItemThread;
import rms.util.Helpers;
import rms.util.ManyToManyRelation;

/**
 *
 * @author Timothy
 */
public final class ThreadTagMap implements Serializable {

    private static final long serialVersionUID = 01L;

    //NOTE: will use null mappings in the map to denote the existance of items
    private final ManyToManyRelation<Tag, ItemThread> map;

    public ThreadTagMap() {
        map = new ManyToManyRelation<>();
    }

    /**
     * If the given {@link Tag} does not exist in the map, then add the
     * {@link Tag} to the map. If it already exists, then do nothing.
     *
     * @param t
     */
    public void addNewTag(Tag t) {
        if (t == null) {
            //ensures there is never a null->null mappping
            throw new IllegalArgumentException("Tag cannot be null.");
        }
        map.add(t, null);
    }

    /**
     * If the given {@link ItemThread} does not exist in the map, then add the
     * {@link ItemThread} to the map. If it already exists, then do nothing.
     *
     * @param t
     */
    public void addNewThread(ItemThread t) {
        if (t == null) {
            //ensures there is never a null->null mappping
            throw new IllegalArgumentException("ItemThread cannot be null.");
        }
        map.add(null, t);
    }

    /**
     * Adds a mapping for the given {@link ItemThread} to the given {@link Tag}.
     * NOTE: it is NOT required for the objects to be added individually first.
     *
     * @param thread
     * @param tag
     */
    public void addTagToThread(ItemThread thread, Tag tag) {
        if (thread == null || tag == null) {
            //ensures there is never a null->null mappping
            throw new IllegalArgumentException("ItemThread and Tag must not be null.");
        }
        //add the null mappings to denote existance (existance marker)
        map.add(null, thread);
        map.add(tag, null);
        //add the new mapping
        map.add(tag, thread);
    }

    public void removeTagFromThread(ItemThread thread, Tag tag) {
        if (thread == null || tag == null) {
            //ensures there is never a null->null mappping
            throw new IllegalArgumentException("ItemThread and Tag must not be null.");
        }
        map.remove(tag, thread);
    }

    /**
     * Gets an unmodifiable view of the Set of all {@link ItemThread}s.
     *
     * @return
     */
    public Set<ItemThread> getAllItemThreads() {
        Set<ItemThread> s = map.getBforA(null);
        return s == null ? Collections.<ItemThread>emptySet() : s;
    }

    /**
     * Gets an unmodifiable view of the Set of all {@link Tag}s.
     *
     * @return
     */
    public Set<Tag> getAllTags() {
        Set<Tag> s = map.getAforB(null);
        return s == null ? Collections.<Tag>emptySet() : s;
    }

    /**
     * Gets an unmodifiable view of the Set of {@link ItemThread}s containing
     * the given {@link Tag}.
     *
     * @param t
     *
     * @return all ItemsThreads containing the given Tag or null if there are
     *         none
     */
    public Set<ItemThread> getThreadsFor(Tag t) {
        if (t == null) {
            throw new IllegalArgumentException("Tag cannot be null.");
        }

        //return the set of ItemThreads minus the null (existance marker)
        return Helpers.difference(map.getBforA(t), Collections.<ItemThread>singleton(null));
        //NOTE: May throw NullPointerException is the Tag does not exist but
        //      this shouldn't happen if this class is written correctly.
    }

    /**
     * Gets an unmodifiable view of the Set of {@link Tags}s belonging to the
     * given {@link ItemThread}.
     *
     * @param t
     *
     * @return
     */
    public Set<Tag> getTagsFor(ItemThread t) {
        if (t == null) {
            throw new IllegalArgumentException("ItemThread cannot be null.");
        }

        //return the set of ItemThreads minus the null (existance) marker
        return Helpers.difference(map.getAforB(t), Collections.<Tag>singleton(null));
        //NOTE: May throw NullPointerException is the ItemThread does not exist
        //      but this shouldn't happen if this class is written correctly.
    }

    /**
     * Removes all mappings for the given {@link Tag}.
     *
     * @param t
     */
    public void deleteTag(Tag t) {
        if (t == null) {
            throw new IllegalArgumentException("Tag cannot be null.");
        }
        map.removeAllA(t);
    }

    /**
     * Removes all mappings for the given {@link ItemThread}.
     *
     * @param t
     */
    public void deleteItemThread(ItemThread t) {
        if (t == null) {
            throw new IllegalArgumentException("ItemThread cannot be null.");
        }
        map.removeAllB(t);
    }
}
