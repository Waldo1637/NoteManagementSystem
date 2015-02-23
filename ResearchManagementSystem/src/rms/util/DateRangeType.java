package rms.util;

/**
 *
 * @author Timothy
 */
public enum DateRangeType {
    CREATED("completed"), MODIFIED("modified"), DEADLINE("with deadline");
    
    private final String text;

    /**
     * @param text
     */
    private DateRangeType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
