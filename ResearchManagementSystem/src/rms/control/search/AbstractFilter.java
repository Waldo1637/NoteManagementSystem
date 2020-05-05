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
     * @param item
     *
     * @return true iff the given item is accepted by the Filter
     */
    public boolean includesItem(Item item) {
        return accept(item);
    }

    /**
     *
     * @param item
     *
     * @return {@code true} iff the given {@link Item} should be returned by
     *         this AbstractFilter
     */
    protected abstract boolean accept(Item item);

    /**
     *
     * @param t
     *
     * @return {@code true} iff the given {@link ItemThread} should be returned
     *         by this AbstractFilter
     */
    protected boolean accept(ItemThread t) {
        //Default behavior: the entire thread is accepted if any item is accepted
        for (Item i : t) {
            if (accept(i)) {
                return true;
            }
        }
        return false;
    }
}
