package rms.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Static utility methods for Date objects
 * @author Timothy
 */
public final class DateHelpers {
    
    /**
     * @return  {@link Date} object representing midnight of the current day
     */
    public static Date Today(){
        return removeTime(new Date());
    }
    
    /**
     * @return {@link Date} object representing the current date and time
     */
    public static Date Now(){
        Calendar inst = GregorianCalendar.getInstance();
        return inst.getTime();
    }
    
    /**
     * Sets the time on the given {@link Date} object to midnight
     * @param d
     * @return 
     */
    public static Date removeTime(Date d){
        Calendar inst = GregorianCalendar.getInstance();
        inst.setTime(d);
        inst.set(Calendar.HOUR_OF_DAY, 0);
        inst.set(Calendar.MINUTE, 0);
        inst.set(Calendar.SECOND, 0);
        inst.set(Calendar.MILLISECOND, 0);
        return inst.getTime();
    }
    
    /**
     * Determines the number of days between two dates
     * @param d1
     * @param d2
     * @return 
     */
    public static int dayDiff(Date d1, Date d2){
        Date d1a = removeTime(d1);
        Date d2a = removeTime(d2);
        
        long timeDiff = d1a.getTime() - d2a.getTime();

        // milliseconds in 1 day = 86400000
        return (int)(timeDiff / 86400000);
    }
    
    public static Date addDays(Date startDate, int numDays){
        Calendar inst = GregorianCalendar.getInstance();
        inst.setTime(startDate);
        inst.add(Calendar.DAY_OF_YEAR, numDays);
        return inst.getTime();
    }
    
    public static Date addWeeks(Date startDate, int numWeeks){
        Calendar inst = GregorianCalendar.getInstance();
        inst.setTime(startDate);
        inst.add(Calendar.WEEK_OF_YEAR, numWeeks);
        return inst.getTime();
    }
    
    public static Date addMonths(Date startDate, int numMonths){
        Calendar inst = GregorianCalendar.getInstance();
        inst.setTime(startDate);
        inst.add(Calendar.MONTH, numMonths);
        return inst.getTime();
    }
    
        /**
     * Tests if the first date is before or equal to the second date.
     *
     * @param one
     * @param two
     * @return  <code>true</code> if and only if the instant of time
     *            represented by the first <tt>Date</tt> object is equal to
     *            or earlier than the instant represented by the second;
     *          <code>false</code> otherwise.
     * @exception NullPointerException if either is null.
     */
    public static boolean beforeEqual(Date one, Date two) {
        return one.getTime() <= two.getTime();
    }

    /**
     * Tests if the first date is after or equal to the second date.
     *
     * @param one
     * @param two
     * @return  <code>true</code> if and only if the instant represented
     *          by the first <tt>Date</tt> object is equal to or later
     *          than the instant represented by the second;
     *          <code>false</code> otherwise.
     * @exception NullPointerException if either is null.
     */
    public static boolean afterEqual(Date one, Date two) {
        return one.getTime() >= two.getTime();
    }
}
