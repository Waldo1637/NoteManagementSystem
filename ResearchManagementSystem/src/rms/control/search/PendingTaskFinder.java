package rms.control.search;

import rms.model.item.Item;
import rms.model.item.ItemThread;
import rms.model.item.TaskItem;

/**
 * Finds {@link TaskItem}s which have not been marked completed
 *
 * @author Timothy
 */
public class PendingTaskFinder extends AbstractThreadFinder {

    @Override
    protected boolean accept(Item i) {
        return (i instanceof TaskItem) ? !((TaskItem) i).isComplete() : false;
    }

    @Override
    protected boolean accept(ItemThread t) {
        return false;
    }
}
