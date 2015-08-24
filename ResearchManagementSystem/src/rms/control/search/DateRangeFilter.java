package rms.control.search;

import java.util.Date;
import rms.model.item.Item;
import rms.model.item.ItemThread;
import rms.model.item.TaskItem;
import rms.util.DateHelpers;
import rms.util.DateRangeType;

/**
 * Finds tasks whose creation/modification time or deadline is within the given
 * date range.
 *
 * @author Timothy
 */
public class DateRangeFilter extends AbstractFilter {

    private final DateRangeType type;
    private final Date start;
    private final Date end;

    /**
     *
     * @param type the type of timestamp to search
     * @param start inclusive start date of the range
     * @param end inclusive end date of the range
     */
    public DateRangeFilter(DateRangeType type, Date start, Date end) {
        this.type = type;
        this.start = DateHelpers.removeTime(start);
        this.end = DateHelpers.removeTime(end);
    }

    @Override
    protected boolean accept(Item i) {
        Date itemDate = null;

        switch (type) {
            case CREATED:
                itemDate = i.getCreationTime();
                break;
            case MODIFIED:
                itemDate = i.getModificationTime();
                break;
            case DEADLINE:
                itemDate = (i instanceof TaskItem) ? ((TaskItem) i).getDeadline() : null;
                break;
            default:
                throw new UnsupportedOperationException("Option " + type + " is not yet supported.");
        }

        if (itemDate == null) {
            return false;
        }

        itemDate = DateHelpers.removeTime(itemDate);
        return DateHelpers.afterEqual(itemDate, start) && DateHelpers.beforeEqual(itemDate, end);
    }

    @Override
    protected boolean accept(ItemThread t) {
        //the entire thread is accepted if any item is accepted
        return isAnyItemAccepted(t);
    }
}
