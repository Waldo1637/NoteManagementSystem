package rms.control.search;

import java.util.Arrays;
import java.util.List;
import rms.model.item.FileItem;
import rms.model.item.Item;
import rms.model.item.ItemThread;
import rms.model.item.TextItem;
import rms.util.Helpers;

/**
 * Accepts {@link Item}s where the name or some other {@link String} content
 * contains (ignoring case) the given {@link String}.
 *
 * @author Timothy
 */
public class TextFilter extends AbstractFilter {

    public static enum MatchType {
        Any, All, Exact
    }

    private final List<String> searchWords;// lowercase via constructor
    private final boolean matchAll;

    /**
     *
     * @param searchText
     * @param type
     *
     * @return
     */
    public static TextFilter get(String searchText, MatchType type) {
        return new TextFilter(searchText, type);
    }

    private TextFilter(String searchText, MatchType type) {
        switch (type) {
            case Any:
                //For an Any match, split the String and set 'matchAll' to false
                this.searchWords = Arrays.asList(searchText.toLowerCase().split(" "));
                this.matchAll = false;
                break;
            case All:
                //For an All match, split the String and set 'matchAll' to true
                this.searchWords = Arrays.asList(searchText.toLowerCase().split(" "));
                this.matchAll = true;
                break;
            case Exact:
                //For an Exact match, don't split the String, 'matchAll' doesn't matter
                this.searchWords = Arrays.asList(searchText.toLowerCase());
                this.matchAll = true;
                break;
            default:
                throw new IllegalArgumentException("Unhandled type " + type);
        }
    }

    /**
     *
     * @param i
     *
     * @return {@code true} iff the name of the {@code Item} or its body text
     *         contains (case insensitive) the search text
     */
    @Override
    public boolean accept(Item i) {
        if (containsMatch(i.getName())) {
            //check the item name for the text
            return true;
        } else if (i instanceof TextItem) {
            //if the item is a TextItem, check the text
            return containsMatch(((TextItem) i).getText());
        } else if (i instanceof FileItem) {
            //if the item is a FileItem, check the name
            return containsMatch(((FileItem) i).getFile().getName());
        } else {
            return false;
        }
    }

    /**
     *
     * @param t
     *
     * @return {@code true} iff the name of the {@code ItemThread} contains the
     *         search text (case insensitive comparison) or any item is accepted
     */
    @Override
    public boolean accept(ItemThread t) {
        return containsMatch(t.getName()) || super.accept(t);
    }

    /**
     *
     * @param test
     *
     * @return
     */
    private boolean containsMatch(String test) {
        if (test != null) {
            test = test.toLowerCase();//make lowercase to ignore case
            return matchAll
                    ? Helpers.stringContainsAllWords(test, searchWords)
                    : Helpers.stringContainsAnyWord(test, searchWords);
        }
        return false;
    }
}
