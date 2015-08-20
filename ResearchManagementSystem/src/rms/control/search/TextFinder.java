package rms.control.search;

import rms.model.item.Item;
import rms.model.item.ItemThread;
import rms.model.item.TextItem;
import rms.util.Helpers;

/**
 * Finds {@link TextItem}s with text containing the given string
 *
 * @author Timothy
 */
public class TextFinder extends AbstractThreadFinder {

    private final String searchText;

    public TextFinder(String searchText) {
        this.searchText = searchText;
    }

    /**
     *
     * @param i
     * @return {@code true} iff the name of the {@code Item} or its body text
     * contains (case insensitive) the search text
     */
    @Override
    protected boolean accept(Item i) {
        //check the item name for the text
        if (Helpers.containsIgnoreCase(i.getName(), searchText)) {
            return true;
        }

        //if the item is a TextItem, check the text
        if (i instanceof TextItem) {
            String text = ((TextItem) i).getText();
            return (text == null) ? false : Helpers.containsIgnoreCase(text, searchText);
        }
        return false;
    }

    /**
     *
     * @param t
     * @return {@code true} iff the name of the {@code ItemThread} contains
     * (case insensitive) the search text
     */
    @Override
    protected boolean accept(ItemThread t) {
        return Helpers.containsIgnoreCase(t.getName(), searchText);
    }
}
