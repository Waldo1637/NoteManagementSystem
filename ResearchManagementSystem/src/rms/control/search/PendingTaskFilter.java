package rms.control.search;

import rms.model.item.Item;
import rms.model.item.TaskItem;

/**
 * Finds {@link TaskItem}s which have NOT been marked completed.
 *
 * @author Timothy
 */
public final class PendingTaskFilter extends AbstractFilter {

    // thread-safe lazy singleton pattern
    private static class InstanceHolder {

        static final PendingTaskFilter INSTANCE = new PendingTaskFilter();

        private InstanceHolder() {
        }
    }

    /**
     *
     * @return
     */
    public static PendingTaskFilter get() {
        return InstanceHolder.INSTANCE;
    }

    private PendingTaskFilter() {
    }

    @Override
    public boolean accept(Item i) {
        return (i instanceof TaskItem) ? !((TaskItem) i).isComplete() : false;
    }
}
