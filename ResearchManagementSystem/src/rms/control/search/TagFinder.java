package rms.control.search;

import rms.model.Tag;
import rms.model.item.Item;
import rms.model.item.ItemThread;

/**
 * Finds {@link ItemThread}s with the given {@link Tag}
 * 
 * @author Timothy
 */
public class TagFinder extends AbstractFinder {
    
    private final Tag tag;
    
    /**
     * 
     * @param tag   {@link Tag} to search {@link ItemThread}s for
     */
    public TagFinder(Tag tag){
        this.tag = tag;
    }

    /**
     * There is no {@link Tag} associated with an {@link Item}
     * @param i
     * @return {@code false}
     */
    @Override
    protected boolean accept(Item i) {
        return false;
    }

    @Override
    protected boolean accept(ItemThread t) {
        return t.getTags().contains(tag);
    }
}
