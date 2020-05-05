package rms.control.search;

import java.util.Date;
import rms.model.item.Item;
import rms.model.item.TaskItem;
import rms.util.DateHelpers;
import rms.util.DateUnit;

/**
 * Finds tasks whose deadline is within the next X time units
 *
 * @author Timothy
 */
public class UpcomingTaskFilter extends AbstractFilter {

    private final int increment;
    private final DateUnit unit;

    /**
     * @param increment number of {@code DateUnit}s into the future to search
     * @param unit      unit type associated with the increment
     *
     * @return
     */
    public static UpcomingTaskFilter get(int increment, DateUnit unit) {
        return new UpcomingTaskFilter(increment, unit);
    }

    /**
     * @param increment number of {@code DateUnit}s into the future to search
     * @param unit      unit type associated with the increment
     */
    private UpcomingTaskFilter(int increment, DateUnit unit) {
        this.increment = increment;
        this.unit = unit;
    }

    /**
     *
     * @param i {@link Item} to test for acceptance
     *
     * @return {@code true} iff the deadline on {@code i} is within the range
     *         specified by this instance
     */
    @Override
    protected boolean accept(Item i) {
        if (i instanceof TaskItem) {
            Date deadline = ((TaskItem) i).getDeadline();

            if (deadline == null) {
                return false;
            }

            Date today = DateHelpers.today();
            if (deadline.before(today)) {
                return false;
            }

            Date finalDate = null;
            switch (unit) {
                case DAY:
                    finalDate = DateHelpers.addDays(today, increment);
                    break;
                case WEEK:
                    finalDate = DateHelpers.addWeeks(today, increment);
                    break;
                case MONTH:
                    finalDate = DateHelpers.addMonths(today, increment);
                    break;
                default:
                    throw new UnsupportedOperationException("Option " + unit + " is not yet supported.");
            }

            return (finalDate == null) ? false : DateHelpers.beforeEqual(deadline, finalDate);
        }
        return false;
    }
}
