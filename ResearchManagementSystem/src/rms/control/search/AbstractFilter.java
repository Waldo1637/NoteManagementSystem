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

    /**
     * Searches the {@code input} Collection for {@link ItemThread}s which match
     * the criteria of the subclass implementation.
     *
     * @param input
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
     * Searches the {@code input} for {@link Item}s which match the criteria of
     * the subclass implementation.
     *
     * @param input
     * @return
     */
    public Set<Item> filterItems(Iterable<Item> input) {
        HashSet<Item> retVal = new HashSet<>();
        for (Item i : input) {
            if (accept(i)) {
                retVal.add(i);
            }
        }
        return retVal;
    }

    /**
     * Helper method to check if an ItemThread should be accepted based on the
     * Items contained within.
     *
     * @param t
     * @return
     */
    protected boolean isAnyItemAccepted(ItemThread t) {
        for (Item i : t) {
            if (accept(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param item
     * @return {@code true} iff the given {@link Item} should be returned by
     * this AbstractFilter
     */
    protected abstract boolean accept(Item item);

    /**
     *
     * @param thread
     * @return {@code true} iff the given {@link ItemThread} should be returned
     * by this AbstractFilter
     */
    protected abstract boolean accept(ItemThread thread);

}
