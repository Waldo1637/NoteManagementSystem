package rms.util;

import java.util.HashSet;
import java.util.Set;

/**
 * Static utility methods
 *
 * @author Timothy
 */
public final class Helpers {

    /**
     * Checks if String s1 contains string s2 in a case insensitive manner
     *
     * @param s1
     * @param s2
     *
     * @return
     */
    public static boolean containsIgnoreCase(String s1, String s2) {
        return s1.toLowerCase().contains(s2.toLowerCase());
    }

    /**
     * Return a new Set containing the set intersection of the two given
     *
     * @param <N>
     * @param s1
     * @param s2
     *
     * @return
     */
    public static <N> Set<N> intersection(Set<N> s1, Set<N> s2) {
        HashSet<N> retVal = new HashSet<>(s1);
        retVal.retainAll(s2);
        return retVal;
    }

    /**
     * Return a new Set containing the set difference {@code s1 - s2}
     *
     * @param <N>
     * @param s1
     * @param s2
     *
     * @return
     */
    public static <N> Set<N> difference(Set<N> s1, Set<N> s2) {
        HashSet<N> retVal = new HashSet<>(s1);
        retVal.removeAll(s2);
        return retVal;
    }

    private Helpers() {
    }
}
