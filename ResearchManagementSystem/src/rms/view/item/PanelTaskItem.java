package rms.view.item;

import java.util.Date;
import rms.model.item.TaskItem;
import rms.model.item.TextItem;
import static rms.view.item.PanelAnyTextItem.StatusIndicator.COMPLETE;
import static rms.view.item.PanelAnyTextItem.StatusIndicator.OVERDUE;
import static rms.view.item.PanelAnyTextItem.StatusIndicator.PENDING;

/**
 * Panel for displaying a {@link TaskItem}
 *
 * @author Timothy
 */
public class PanelTaskItem extends PanelAnyTextItem {

    /**
     * Creates new PanelTaskItem with the given {@link TextItem}
     *
     * @param itm
     */
    public PanelTaskItem(TaskItem itm) {
        super(itm);
        showStatusPanel(true);
        setDeadline(itm.getDeadline());
    }

    @Override
    protected StatusIndicator getStatus() {
        TaskItem ti = (TaskItem) item;
        return ti.isComplete() ? COMPLETE : (ti.isOverdue() ? OVERDUE : PENDING);
    }

    @Override
    protected void togglePressed() {
        TaskItem ti = (TaskItem) item;
        if (ti.isComplete()) {
            ti.markActive();
        } else {
            ti.markCompleted();
        }
    }

    @Override
    protected void deadlineUpdated(Date newDate) {
        ((TaskItem) item).setDeadline(newDate);
    }
}
