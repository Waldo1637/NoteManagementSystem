package rms.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Static utility methods
 *
 * @author Timothy
 */
public final class Helpers {

    private static final Logger LOG = Logger.getLogger(Helpers.class.getName());

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
     * Closes the Closable resource if it is not null or already closed.
     *
     * @param c
     */
    public static void closeResource(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException ex) {
                LOG.log(Level.WARNING, "Error closing resource", ex);
            }
        }
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
