package rms.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Timothy
 */
public final class Tag implements Serializable, Comparable<Tag> {

    private static final long serialVersionUID = 01L;

    private final String name;

    /**
     * Creates a new Tag with the given name.
     *
     * NOTE: Use {@link State#newTag(java.lang.String)} to create a new Tag.
     *
     * @param name
     */
    protected Tag(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        } else {
            return Objects.equals(this.name, ((Tag) obj).name);
        }
    }

    @Override
    public int compareTo(Tag o) {
        return this.name.compareTo(o.name);
    }
}
