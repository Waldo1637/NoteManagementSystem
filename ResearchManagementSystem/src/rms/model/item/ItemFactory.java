package rms.model.item;

import java.io.File;

/**
 *
 * @author Timothy
 */
public class ItemFactory {

    public static NoteItem createAndAddNoteItem(ItemThread parentThread) {
        NoteItem i = new NoteItem(parentThread);
        parentThread.add(i);
        return i;
    }

    public static NoteItem createAndAddNoteItem(ItemThread parentThread, String text) {
        NoteItem i = new NoteItem(parentThread, text);
        parentThread.add(i);
        return i;
    }

    public static TaskItem createAndAddTaskItem(ItemThread parentThread) {
        TaskItem i = new TaskItem(parentThread);
        parentThread.add(i);
        return i;
    }

    public static TaskItem createAndAddTaskItem(ItemThread parentThread, String text) {
        TaskItem i = new TaskItem(parentThread, text);
        parentThread.add(i);
        return i;
    }

    public static FileItem createAndAddFileItem(ItemThread parentThread, File srcFile) {
        FileItem i = new FileItem(parentThread, srcFile);
        parentThread.add(i);
        return i;
    }

    private ItemFactory() {
    }
}
