package rms.model.item;

import java.io.File;

/**
 *
 * @author Timothy
 */
public class ItemFactory {

    public static NoteItem newNoteItem(ItemThread parentThread) {
        NoteItem i = new NoteItem(parentThread);
        parentThread.add(i);
        return i;
    }

    public static NoteItem newNoteItem(ItemThread parentThread, String text) {
        NoteItem i = new NoteItem(parentThread, text);
        parentThread.add(i);
        return i;
    }

    public static TaskItem newTaskItem(ItemThread parentThread) {
        TaskItem i = new TaskItem(parentThread);
        parentThread.add(i);
        return i;
    }

    public static TaskItem newTaskItem(ItemThread parentThread, String text) {
        TaskItem i = new TaskItem(parentThread, text);
        parentThread.add(i);
        return i;
    }

    public static FileItem newFileItem(ItemThread parentThread, File srcFile) {
        FileItem i = new FileItem(parentThread, srcFile);
        parentThread.add(i);
        return i;
    }
}
