package rms.control.search;

import rms.model.item.Item;
import rms.model.item.ItemThread;
import rms.model.item.TaskItem;

/**
 * Finds {@link TaskItem}s which have not been marked completed
 * 
 * @author Timothy
 */
public class PendingTaskFinder extends AbstractFinder {

    @Override
    protected boolean accept(Item i) {
        if(i instanceof TaskItem){
            TaskItem ti = (TaskItem)i;
            return !ti.isComplete();
        }
        return false;
    }
    
    @Override
    protected boolean accept(ItemThread t) {
        return false;
    }
}
