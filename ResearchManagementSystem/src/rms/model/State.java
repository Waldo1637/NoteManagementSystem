package rms.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import rms.model.item.ItemThread;

/**
 * Stores references to all items
 * 
 * @author Timothy
 */
public class State implements Serializable {
    
    private static final long serialVersionUID = 01L;
    
    private int nextItemNumber;
    private int nextThreadNumber;
    private final Set<ItemThread> threads;
    private final Set<Tag> tags;
    
    public State(){
        nextItemNumber = 0;
        nextThreadNumber = 0;
        threads = new HashSet<>();
        tags = new HashSet<>();
    }
    
    public int getNextItemNumber(){
        return ++nextItemNumber;
    }
    
    public int getNextThreadNumber(){
        return ++nextThreadNumber;
    }
    
    public ItemThread createNewThread(){
        ItemThread t = new ItemThread();
        this.threads.add(t);
        return t;
    }
    
    public Set<ItemThread> getThreads(){
        return threads;
    }
    
    public Set<Tag> getTags(){
        return tags;
    }
    
    public Tag newTag(String name){
        Tag t = new Tag(name);
        tags.add(t);
        return t;
    }
    
    //TODO:
    // 1. keep a list of tags?
    // 2. keep a mapping of tag to item?
    // 3. keep a mapping of mod time to item?
}
