package rms.control.search;

import java.util.Set;
import rms.model.Tag;
import rms.model.item.Item;
import rms.model.item.ItemThread;
import rms.util.Helpers;

/**
 * Finds {@link ItemThread}s with the given {@link Tag}
 *
 * @author Timothy
 */
public class TagFinder extends AbstractThreadFinder {

    private final Set<Tag> tag;

    /**
     *
     * @param tag {@link Tag} to search {@link ItemThread}s for
     */
    public TagFinder(Set<Tag> tag) {
        this.tag = tag;
    }

    /**
     * This method always returns false because there is no {@link Tag}
     * associated with an {@link Item}.
     *
     * @param i
     * @return {@code false}
     */
    @Override
    protected boolean accept(Item i) {
        return false;
    }

    @Override
    protected boolean accept(ItemThread t) {
        return !Helpers.intersection(t.getTags(), tag).isEmpty();
    }
}
