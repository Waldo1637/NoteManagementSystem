package rms.view.item;

import java.util.Date;
import rms.model.item.TaskItem;

/**
 * Panel for displaying a {@link TaskItem}
 * 
 * @author Timothy
 */
public class PanelTaskItem extends PanelAnyTextItem {

    public PanelTaskItem(TaskItem itm) {
        super(itm);
        showStatusPanel(true);
        setDeadline(itm.getDeadline());
    }
    
    @Override
    protected StatusIndicator getStatus(){
        TaskItem ti = (TaskItem)item;

        if(ti.isComplete()){
            return StatusIndicator.COMPLETE;
        }else if(ti.isOverdue()){
            return StatusIndicator.OVERDUE;
        }else {
            return StatusIndicator.PENDING;
        }
    }
    
    @Override
    protected void togglePressed(){
        TaskItem ti = (TaskItem)item;
        
        if(ti.isComplete()){
            ti.markActive();
        }else{
            ti.markCompleted();
        }
    }

    @Override
    protected void deadlineUpdated(Date newDate) {
        TaskItem ti = (TaskItem)item;
        ti.setDeadline(newDate);
    }
}
