package rms.control.search;

import rms.model.item.Item;
import rms.model.item.TaskItem;

/**
 * Finds {@link TaskItem}s which are overdue (i.e. deadline is in the past).
 *
 * @author Timothy
 */
public final class LateTaskFilter extends AbstractFilter {

    // thread-safe lazy singleton pattern
    private static class InstanceHolder {

        static final LateTaskFilter INSTANCE = new LateTaskFilter();

        private InstanceHolder() {
        }
    }

    /**
     *
     * @return
     */
    public static LateTaskFilter get() {
        return InstanceHolder.INSTANCE;
    }

    private LateTaskFilter() {
    }

    @Override
    public boolean accept(Item i) {
        return (i instanceof TaskItem) ? ((TaskItem) i).isOverdue() : false;
    }
}
