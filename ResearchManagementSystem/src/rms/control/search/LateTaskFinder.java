package rms.control.search;

import rms.model.item.Item;
import rms.model.item.ItemThread;
import rms.model.item.TaskItem;

/**
 * Finds {@link TaskItem}s which are overdue, i.e. their deadline is in the past.
 * 
 * @author Timothy
 */
public class LateTaskFinder extends AbstractFinder {

    @Override
    protected boolean accept(Item i) {
        if(i instanceof TaskItem){
            TaskItem ti = (TaskItem)i;
            return ti.isOverdue();
        }
        return false;
    }
    
    @Override
    protected boolean accept(ItemThread t) {
        return false;
    }
}
