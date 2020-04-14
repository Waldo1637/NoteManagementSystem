package rms.view;

import java.util.*;
import javax.swing.AbstractListModel;
import rms.control.Main;
import rms.control.search.AbstractFilter;
import rms.model.item.ItemThread;

/**
 * List model that supports filtering and sorting
 *
 * @author Timothy
 */
public class SearchSortItemThreadListModel extends AbstractListModel<ItemThread> {

    private final List<ItemThread> items;

    public SearchSortItemThreadListModel(boolean sorted, AbstractFilter filter) {
        Set<ItemThread> itemThreads = Main.getState().getThreadsUnmodifiable();

        //create the list of items, applying the filter if available
        items = new ArrayList<>(filter == null ? itemThreads : filter.filterThreads(itemThreads));

        //optionally, sort the list
        if (sorted) {
            final Comparator<ItemThread> comparator = new Comparator<ItemThread>() {
                @Override
                public int compare(ItemThread o1, ItemThread o2) {
                    return o2.getModificationTime().compareTo(o1.getModificationTime());
                }
            };
            Collections.sort(items, comparator);
        }
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public ItemThread getElementAt(int i) {
        return items.get(i);
    }
}
