package rms.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractListModel;
import rms.model.item.ItemThread;
import rms.control.search.AbstractThreadFinder;
import rms.control.Main;

/**
 * List model that supports filtering and sorting
 *
 * @author Timothy
 */
public class SearchSortItemThreadListModel extends AbstractListModel {

    private final List<ItemThread> items;

    public SearchSortItemThreadListModel(boolean sorted, AbstractThreadFinder finder) {
        Set<ItemThread> itemThreads = Main.getState().getThreads();

        //create the list of items, applying the filter if available
        items = new ArrayList<>(finder == null ? itemThreads : finder.find(itemThreads));

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
