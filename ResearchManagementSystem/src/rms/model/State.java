package rms.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import rms.model.item.ItemThread;

/**
 * Main data storage. Stores all {@link ItemThread} and {@link Tag} instances
 * and has methods for creating new ones and managing the relationships between
 * the two.
 *
 * @author Timothy
 */
public final class State implements Serializable {

    //Do not change
    private static final long serialVersionUID = 01L;

    /**
     * Version number for new or de-serialized instances.
     */
    private static final byte CURRENT_OBJ_SERIAL_VERSION = 2;

    /**
     * Denotes the serialization version of a serialized object instance.
     * Determines which read method must be used to de-serialize the object.
     */
    private byte classSerializationVersion;

    // State data
    private int nextItemNumber;
    private int nextThreadNumber;
    private ThreadTagMap threadsAndTags;

    public State() {
        this.nextItemNumber = 0;
        this.nextThreadNumber = 0;
        this.threadsAndTags = new ThreadTagMap();
        this.classSerializationVersion = CURRENT_OBJ_SERIAL_VERSION;
    }

    public int getNextItemNumber() {
        return ++nextItemNumber;
    }

    public int getNextThreadNumber() {
        return ++nextThreadNumber;
    }

    /**
     * Creates a new {@link ItemThread}, adds it to the State, and returns it.
     *
     * @return
     */
    public ItemThread createNewThread() {
        ItemThread t = new ItemThread();
        threadsAndTags.addNewThread(t);
        return t;
    }

    /**
     * Creates a new {@link ItemThread} as a copy of the one given, adds it to
     * the State, and returns it.
     *
     * @param toCopy
     * @param changeFileToPlaceholder
     *
     * @return
     */
    public ItemThread createNewThread(ItemThread toCopy, boolean changeFileToPlaceholder) {
        ItemThread t = new ItemThread(toCopy, changeFileToPlaceholder);
        threadsAndTags.addNewThread(t);
        return t;
    }

    /**
     * Permanently delete the given {@link ItemThread} from the State.
     *
     * @param threadToRemove
     */
    public void deleteThread(ItemThread threadToRemove) {
        threadsAndTags.deleteItemThread(threadToRemove);
    }

    /**
     * Creates a new {@link Tag} with the given name, adds it to the State, and
     * returns it.
     *
     * @param name
     *
     * @return
     */
    public Tag createNewTag(String name) {
        Tag t = new Tag(name);
        threadsAndTags.addNewTag(t);
        return t;
    }

    /**
     * Permanently delete the given {@link Tag}s from the State (including from
     * all {@link ItemThread}s which reference the Tags).
     *
     * @param tagsToDelete
     */
    public void deleteTags(Set<Tag> tagsToDelete) {
        for (Tag t : tagsToDelete) {
            threadsAndTags.deleteTag(t);
        }
    }

    public void addTagToThread(ItemThread thread, Tag tag) {
        threadsAndTags.addTagToThread(thread, tag);
    }

    public void removeTagFromThread(ItemThread thread, Tag tag) {
        threadsAndTags.removeTagFromThread(thread, tag);
    }

    public Set<Tag> getTagsForThread(ItemThread t) {
        return threadsAndTags.getTagsFor(t);
    }

    public Set<ItemThread> getThreadsWithTag(Tag t) {
        return threadsAndTags.getThreadsFor(t);
    }

    /**
     * Gets an unmodifiable view of the Set of all {@link ItemThread}s.
     *
     * @return
     */
    public Set<ItemThread> getThreadsUnmodifiable() {
        return threadsAndTags.getAllItemThreads();
    }

    /**
     * Gets an unmodifiable view of the Set of all {@link Tag}s.
     *
     * @return
     */
    public Set<Tag> getTagsUnmodifiable() {
        return threadsAndTags.getAllTags();
    }

    /**
     * Object de-serialization entry point.
     *
     * @param in
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        // Read the fields from the stream
        ObjectInputStream.GetField serializedFields = in.readFields();

        // Check for version number. If not present, version is 1 (the field did not exist).
        byte objectVersionNumber = serializedFields.get("classSerializationVersion", (byte) 1);

        // Take the appropriate action based on revision number
        switch (objectVersionNumber) {
            case 1:
                readFromVer1(serializedFields);
                break;
            case 2:
                readFromVer2(serializedFields);
                break;
            default:
                throw new UnsupportedOperationException("Version number " + objectVersionNumber + " is not supported.");
        }
    }

    /**
     * De-serialize the fields as a version 1 object and use them to initialize
     * a State object of the most recent version.
     *
     * @param serializedFields
     *
     * @throws IOException
     */
    private void readFromVer1(ObjectInputStream.GetField serializedFields) throws IOException {
        // Update fields of this State from the Stream where possible
        this.threadsAndTags = new ThreadTagMap();
        this.nextItemNumber = serializedFields.get("nextItemNumber", 0);
        this.nextThreadNumber = serializedFields.get("nextThreadNumber", 0);
        HashSet<Tag> tags = (HashSet<Tag>) serializedFields.get("tags", null);
        HashSet<ItemThread> threads = (HashSet<ItemThread>) serializedFields.get("threads", null);

        // Capture the old Tag set from the stream and add to ThreadTagMap
        for (Tag tag : tags) {
            this.threadsAndTags.addNewTag(tag);
        }

        // Capture the old ItemThread set and Tag mapping from the stream and add to ThreadTagMap
        for (ItemThread thread : threads) {
            this.threadsAndTags.addNewThread(thread);
            for (Tag tag : thread.getTagsForClassSerializationRevision0()) {
                this.threadsAndTags.addTagToThread(thread, tag);
            }
        }

        //Update the revision number so the serialized version of this will
        //  reflect that it has been migrated to the current version.
        this.classSerializationVersion = CURRENT_OBJ_SERIAL_VERSION;
    }

    /**
     * De-serialize the fields as a version 2 object and use them to initialize
     * a State object of the most recent version.
     *
     * @param serializedFields
     *
     * @throws IOException
     */
    private void readFromVer2(ObjectInputStream.GetField serializedFields) throws IOException {
        this.nextItemNumber = serializedFields.get("nextItemNumber", 0);
        this.nextThreadNumber = serializedFields.get("nextThreadNumber", 0);
        this.threadsAndTags = (ThreadTagMap) serializedFields.get("threadsAndTags", new ThreadTagMap());
        this.classSerializationVersion = CURRENT_OBJ_SERIAL_VERSION;
    }
}
