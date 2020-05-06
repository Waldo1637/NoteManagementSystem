package rms.control.search;

import rms.model.item.Item;
import rms.model.item.TaskItem;

/**
 * Finds {@link TaskItem}s which have been marked completed.
 *
 * @author Timothy
 */
public class CompletedTaskFilter extends AbstractFilter {

    // thread-safe lazy singleton pattern
    private static class InstanceHolder {

        static final CompletedTaskFilter INSTANCE = new CompletedTaskFilter();

        private InstanceHolder() {
        }
    }

    /**
     *
     * @return
     */
    public static CompletedTaskFilter get() {
        return InstanceHolder.INSTANCE;
    }

    private CompletedTaskFilter() {
    }

    @Override
    public boolean accept(Item i) {
        return (i instanceof TaskItem) ? ((TaskItem) i).isComplete() : false;
    }
}
