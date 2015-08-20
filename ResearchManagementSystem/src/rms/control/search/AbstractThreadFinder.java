package rms.control.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import rms.model.item.Item;
import rms.model.item.ItemThread;

/**
 * An AbstractThreadFinder takes a Collection of {@link ItemThread} and returns the
 * subset which matches the criteria defined for the specific implementation.
 *
 * @author Timothy
 */
public abstract class AbstractThreadFinder {

    /**
     * Searches the {@code input} Collection for {@link ItemThread}s which match
     * the criteria of the subclass implementation.
     *
     * @param input
     * @return Set of {@link ItemThread}s which match the criteria
     */
    public Set<ItemThread> find(Collection<ItemThread> input) {
        HashSet<ItemThread> retVal = new HashSet<>();
        for (ItemThread t : input) {
            if (accept(t)) {
                retVal.add(t);
                continue; //continue with the next thread
            }

            for (Item i : t) {
                if (accept(i)) {
                    retVal.add(t);
                    break; //skip remaining items in thread
                }
            }
        }
        return retVal;
    }

    /**
     *
     * @param item
     * @return {@code true} iff the given {@link Item} should be returned by
 this AbstractThreadFinder
     */
    protected abstract boolean accept(Item item);

    /**
     *
     * @param thread
     * @return {@code true} iff the given {@link ItemThread} should be returned
 by this AbstractThreadFinder
     */
    protected abstract boolean accept(ItemThread thread);

}
