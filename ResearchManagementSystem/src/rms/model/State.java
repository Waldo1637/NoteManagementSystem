package rms.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
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

    // Used for serialization backwards compatibility. Should be updated if 
    //  serialization incompatible changes are made. Also update the
    //  deserialization process outlined at the end of this file.
    private byte classSerializationVersion;

    // State data
    private int nextItemNumber;
    private int nextThreadNumber;
    private ThreadTagMap threadsAndTags;

    public State() {
        nextItemNumber = 0;
        nextThreadNumber = 0;
        threadsAndTags = new ThreadTagMap();
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
     * Object deserialization entry point.
     *
     * @param in
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        // Read the fields from the stream
        ObjectInputStream.GetField serializedFields = in.readFields();

        // Check for revision number. If no revision number, assume 1
        ObjectStreamField revisionField = serializedFields.getObjectStreamClass().getField("classSerializationVersion");
        byte serialVersionNumber = revisionField == null ? 1 : serializedFields.get("classSerializationVersion", (byte) 0);
        //NOTE: 1 -> field does not exist (i.e. version 1), 0 -> default value

        // Take the appropriate action based on revision number
        switch (serialVersionNumber) {
            case 1:
                migrateFromVer1(serializedFields);
                break;
            case 2:
                migrateFromVer2(serializedFields);
                break;
            default:
                throw new UnsupportedOperationException("Version number " + serialVersionNumber + " is not supported.");
        }
    }

    /**
     * Deserialize the fields from the version 1 object in the stream and use
     * them to initialize a State object of the version 2.
     *
     * @param serializedFields
     * @throws IOException
     */
    private void migrateFromVer1(ObjectInputStream.GetField serializedFields) throws IOException {
        // Update fields of this State from the Stream where possible
        threadsAndTags = new ThreadTagMap();
        nextItemNumber = serializedFields.get("nextItemNumber", 0);
        nextThreadNumber = serializedFields.get("nextThreadNumber", 0);
        HashSet<Tag> tags = (HashSet<Tag>) serializedFields.get("tags", null);
        HashSet<ItemThread> threads = (HashSet<ItemThread>) serializedFields.get("threads", null);

        // Capture the old Tag set from the stream and add to ThreadTagMap
        for (Tag tag : tags) {
            threadsAndTags.addNewTag(tag);
        }

        // Capture the old ItemThread set and Tag mapping from the stream and add to ThreadTagMap
        for (ItemThread thread : threads) {
            threadsAndTags.addNewThread(thread);
            for (Tag tag : thread.getTagsForClassSerializationRevision0()) {
                threadsAndTags.addTagToThread(thread, tag);
            }
        }

        //Update the revision number so the serialized version of this will
        //  reflect that it has been migrated to revision 2.
        classSerializationVersion = 2;
    }

    /**
     * Deserialize the fields from the version 1 object in the stream and use
     * them to initialize a State object of version 2.
     * 
     * @param serializedFields
     * @throws IOException 
     */
    private void migrateFromVer2(ObjectInputStream.GetField serializedFields) throws IOException {
        nextItemNumber = serializedFields.get("nextItemNumber", 0);
        nextThreadNumber = serializedFields.get("nextThreadNumber", 0);
        threadsAndTags = (ThreadTagMap) serializedFields.get("threadsAndTags", new ThreadTagMap());
        classSerializationVersion = 2;  //set the serialization version to 2
    }
}
