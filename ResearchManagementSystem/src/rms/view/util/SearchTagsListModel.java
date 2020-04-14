package rms.view.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractListModel;
import rms.model.Tag;
import rms.util.Helpers;

/**
 * AbstractListModel used for displaying a subset of a {@link Tag} set
 *
 * @author Timothy
 */
public class SearchTagsListModel extends AbstractListModel<Tag> {

    private final List<Tag> items;

    public SearchTagsListModel(Set<Tag> allTags, String searchString) {
        items = new ArrayList<>();

        for (Tag t : allTags) {
            if (Helpers.containsIgnoreCase(t.toString(), searchString)) {
                items.add(t);
            }
        }
        Collections.sort(items);
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public Tag getElementAt(int i) {
        return items.get(i);
    }
}
