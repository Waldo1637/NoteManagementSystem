package rms.util;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

/**
 * Static utility methods
 *
 * @author Timothy
 */
public final class Helpers {

    /**
     *
     * @param input
     * @param words
     *
     * @return
     */
    public static boolean stringContainsAnyWord(String input, Iterable<String> words) {
        for (String item : words) {
            if (input.contains(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param input
     * @param words
     *
     * @return
     */
    public static boolean stringContainsAllWords(String input, Iterable<String> words) {
        for (String item : words) {
            if (!input.contains(item)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static String convertNullToEmpty(String s) {
        return s == null ? "" : s;
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

    /**
     *
     * @param comp
     * @param c
     */
    public static void addOutsideColorBorder(JComponent comp, Color c) {
        comp.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(c),
                comp.getBorder()));
    }

    private Helpers() {
    }
}
