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
public class TagFilter extends AbstractFilter {

    private final Set<Tag> tag;

    /**
     *
     * @param tag {@link Tag} to search {@link ItemThread}s for
     */
    public TagFilter(Set<Tag> tag) {
        this.tag = tag;
    }

    @Override
    protected boolean accept(Item i) {
        //An item is accepted if its owning thread is accepted
        return accept(i.getThread());
    }

    @Override
    protected boolean accept(ItemThread t) {
        return !Helpers.intersection(t.getTagsUnmodifible(), tag).isEmpty();
    }
}
