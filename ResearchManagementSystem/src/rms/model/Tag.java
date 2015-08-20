package rms.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Timothy
 */
public class Tag implements Serializable, Comparable<Tag> {
    
    private static final long serialVersionUID = 01L;
    
    private final String name;
    
    /**
     * Creates a new Tag with the given name.
     * 
     * NOTE: must use {@link State#newTag(java.lang.String)} to create a new Tag.
     * 
     * @param name 
     */
    protected Tag(String name){
        this.name = name;
    }
    
    @Override
    public String toString(){
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tag other = (Tag) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public int compareTo(Tag o) {
        return this.name.compareTo(o.name);
    }
}
