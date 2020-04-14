package rms.util;

/**
 *
 * @author Timothy
 */
public enum DateUnit {
    DAY("day(s)"), WEEK("week(s)"), MONTH("month(s)");

    private final String text;

    /**
     * @param text
     */
    private DateUnit(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
