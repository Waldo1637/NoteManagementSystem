package rms.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractListModel;
import rms.model.item.ItemThread;
import rms.control.search.AbstractFinder;
import rms.control.Main;

/**
 * List model that supports filtering and sorting
 * 
 * @author Timothy
 */
public class SearchSortItemThreadListModel extends AbstractListModel {
    private final List<ItemThread> items;

    public SearchSortItemThreadListModel(boolean sorted, AbstractFinder finder) {
        Set<ItemThread> itemThreads = Main.getState().getThreads();
        
        //optionally, apply a filter
        if(finder != null){
            itemThreads = finder.find(itemThreads);
        }
        
        //create the list of items
        items = new ArrayList<>(itemThreads);
        
        //optionally, sort the list
        if(sorted){
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
