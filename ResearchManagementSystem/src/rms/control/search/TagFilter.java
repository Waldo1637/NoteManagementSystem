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
public final class TagFilter extends AbstractFilter {

    private final Set<Tag> tag;

    /**
     *
     * @param tags {@link Tag} to search {@link ItemThread}s for
     *
     * @return
     */
    public static TagFilter get(Set<Tag> tags) {
        return new TagFilter(tags);
    }

    /**
     *
     * @param tags {@link Tag} to search {@link ItemThread}s for
     */
    private TagFilter(Set<Tag> tags) {
        this.tag = tags;
    }

    @Override
    public boolean accept(Item i) {
        //An item is accepted if its owning thread is accepted
        return accept(i.getThread());
    }

    @Override
    public boolean accept(ItemThread t) {
        return !Helpers.intersection(t.getTagsUnmodifible(), tag).isEmpty();
    }
}
