package rms.model.item;

import java.io.Serializable;
import java.util.Date;
import rms.util.DateHelpers;

/**
 *
 * @author Timothy
 */
public final class TaskItem extends TextItem implements Serializable {

    private static final long serialVersionUID = 01L;

    public static enum StatusIndicator {
        PENDING, COMPLETE, OVERDUE
    };

    private Date deadline;
    private boolean completed;

    protected TaskItem(ItemThread parentThread, String text) {
        super(parentThread, text);
        this.deadline = null;
        this.completed = false;
    }

    //copy constructor
    protected TaskItem(ItemThread parentThreadForCopy, TaskItem toCopy) {
        super(parentThreadForCopy, toCopy);
        this.deadline = DateHelpers.clone(toCopy.deadline);//Date is mutable
        this.completed = toCopy.completed;
    }

    @Override
    public TaskItem duplicateInThread(ItemThread parentThreadForCopy, CopyOptions opts) {
        return new TaskItem(parentThreadForCopy, this);
    }

    public void setDeadline(Date newDeadline) {
        this.deadline = newDeadline;
    }

    public Date getDeadline() {
        return deadline;
    }

    public boolean isComplete() {
        return completed;
    }

    public boolean isOverdue() {
        return !completed && deadline != null && deadline.before(DateHelpers.today());
    }

    public void markCompleted() {
        touch();
        completed = true;
    }

    public void markActive() {
        touch();
        completed = false;
    }

    @Override
    public String getItemTypeName() {
        return "Task";
    }

    public static TaskItem createAndAddTaskItem(ItemThread parentThread, String text) {
        TaskItem i = new TaskItem(parentThread, text);
        i.appendToParentThread();
        return i;
    }

    public static TaskItem createAndAddTaskItem(ItemThread parentThread) {
        return createAndAddTaskItem(parentThread, null);
    }
}
