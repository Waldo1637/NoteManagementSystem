package rms.control.search;

import rms.model.Tag;
import rms.model.item.Item;
import rms.model.item.ItemThread;

/**
 * Finds {@link ItemThread}s with the given {@link Tag}
 *
 * @author Timothy
 */
public class ItemNumberFilter extends AbstractFilter {

    private final int itemID;

    /**
     *
     * @param itemID
     *
     * @return
     */
    public static ItemNumberFilter get(int itemID) {
        return new ItemNumberFilter(itemID);
    }

    /**
     *
     * @param itemID
     */
    private ItemNumberFilter(int itemID) {
        this.itemID = itemID;
    }

    @Override
    public boolean accept(Item item) {
        return item.getID() == itemID;
    }
}
