package rms.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractListModel;
import rms.model.item.ItemThread;
import rms.control.search.AbstractFilter;
import rms.control.Main;

/**
 * List model that supports filtering and sorting
 *
 * @author Timothy
 */
public class SearchSortItemThreadListModel extends AbstractListModel {

    private final List<ItemThread> items;

    public SearchSortItemThreadListModel(boolean sorted, AbstractFilter filter) {
        Set<ItemThread> itemThreads = Main.getState().getThreadsUnmodifiable();

        //create the list of items, applying the filter if available
        items = new ArrayList<>(filter == null ? itemThreads : filter.filterThreads(itemThreads));

        //optionally, sort the list
        if (sorted) {
            Collections.sort(items);
        }
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public Object getElementAt(int i) {
        return items.get(i);
    }
}
