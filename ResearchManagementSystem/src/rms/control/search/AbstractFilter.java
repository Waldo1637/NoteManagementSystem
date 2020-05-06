package rms.control.search;

import java.util.HashSet;
import java.util.Set;
import rms.model.item.Item;
import rms.model.item.ItemThread;

/**
 * An AbstractFilter provides basic functionality for filtering
 * {@link ItemThread}s and {@link Item}s where filter criteria is defined by
 * subclasses.
 *
 * @author Timothy
 */
public abstract class AbstractFilter {

    public static final AbstractFilter ALL = new AbstractFilter() {
        @Override
        public boolean accept(Item item) {
            return true;
        }
    };

    public static final AbstractFilter NONE = new AbstractFilter() {
        @Override
        public boolean accept(Item item) {
            return false;
        }
    };

    /**
     * Searches the {@code input} Collection for {@link ItemThread}s which match
     * the criteria of the subclass implementation.
     *
     * @param input
     *
     * @return Set of {@link ItemThread}s which match the criteria
     */
    public Set<ItemThread> filterThreads(Iterable<ItemThread> input) {
        HashSet<ItemThread> retVal = new HashSet<>();
        for (ItemThread t : input) {
            if (accept(t)) {
                retVal.add(t);
            }
        }
        return retVal;
    }

    /**
     *
     * @param thread
     *
     * @return {@code true} iff the given {@link ItemThread} should be returned
     *         by this AbstractFilter
     */
    public boolean accept(ItemThread thread) {
        //Default behavior: the entire thread is accepted if any item is accepted
        for (Item i : thread) {
            if (accept(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param item
     *
     * @return {@code true} iff the given {@link Item} should be returned by
     *         this {@link AbstractFilter}
     */
    public abstract boolean accept(Item item);

}
