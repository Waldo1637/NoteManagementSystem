package rms.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author Timothy
 * @param <A>
 * @param <B>
 */
public final class ManyToManyRelation<A, B> implements Serializable {

    private static final long serialVersionUID = 01L;

    private final HashMap<A, HashSet<B>> forward;
    private final HashMap<B, HashSet<A>> reverse;

    /**
     *
     */
    public ManyToManyRelation() {
        forward = new HashMap<>();
        reverse = new HashMap<>();
    }

    /**
     * Adds a new mapping from A to B to the relation.
     *
     * @param a
     * @param b
     */
    public void add(A a, B b) {
        getOrCreate(forward, a).add(b);
        getOrCreate(reverse, b).add(a);
    }

    /**
     * Removes the mapping from A to B if one exists.
     *
     * @param a
     * @param b
     */
    public void remove(A a, B b) {
        HashSet<B> mappingsOfA = forward.get(a);
        if (mappingsOfA != null) {
            mappingsOfA.remove(b);
        }
        HashSet<A> mappingsOfB = reverse.get(b);
        if (mappingsOfB != null) {
            mappingsOfB.remove(a);
        }
    }

    /**
     * Removes all mappings containing A.
     *
     * @param a
     */
    public void removeAllA(A a) {
        //remove the forward mapping for a
        HashSet<B> mappingsOfA = forward.remove(a);

        //remove A from the reverse mappings of all items in 'mappingOfA'
        if (mappingsOfA != null) {
            for (B b : mappingsOfA) {
                reverse.get(b).remove(a);
            }
        }
    }

    /**
     * Removes all mappings containing B.
     *
     * @param b
     */
    public void removeAllB(B b) {
        //remove the reverse mapping for b
        HashSet<A> mappingsOfB = reverse.remove(b);

        //remove b from the reverse mappings of all items in 'mappingOfB'
        if (mappingsOfB != null) {
            for (A a : mappingsOfB) {
                forward.get(a).remove(b);
            }
        }
    }

    /**
     * Gets an unmodifiable view of all A contained in the relation.
     *
     * @return
     */
    public Set<A> getAllA() {
        return Collections.unmodifiableSet(forward.keySet());
    }

    /**
     * Gets an unmodifiable view of all B contained in the relation.
     *
     * @return
     */
    public Set<B> getAllB() {
        return Collections.unmodifiableSet(reverse.keySet());
    }

    /**
     * Gets an unmodifiable view of the mappings for A.
     *
     * @param a
     *
     * @return all items of type B mapped to A or null if A is not in the map
     */
    public Set<B> getBforA(A a) {
        HashSet<B> mappingsOfA = forward.get(a);
        return mappingsOfA == null ? null : Collections.unmodifiableSet(mappingsOfA);
    }

    /**
     * Gets an unmodifiable view of the mappings for B.
     *
     * @param b
     *
     * @return all items of type A mapped to B or null if B is not in the map
     */
    public Set<A> getAforB(B b) {
        HashSet<A> mappingsOfB = reverse.get(b);
        return mappingsOfB == null ? null : Collections.unmodifiableSet(mappingsOfB);
    }

    /**
     *
     * @param <K>
     * @param <V>
     * @param map
     * @param key
     *
     * @return
     */
    private static <K, V> HashSet<V> getOrCreate(HashMap<K, HashSet<V>> map, K key) {
        HashSet<V> get = map.get(key);
        if (get == null) {
            get = new HashSet<>();
            map.put(key, get);
        }
        return get;
    }

    @Override
    public String toString() {
        StringBuilder retVal = new StringBuilder();
        for (Entry<A, HashSet<B>> entry : forward.entrySet()) {
            A one = entry.getKey();
            for (B two : entry.getValue()) {
                retVal.append(one).append(" - ").append(two).append("\n");
            }
        }
        return retVal.toString();
    }
}
