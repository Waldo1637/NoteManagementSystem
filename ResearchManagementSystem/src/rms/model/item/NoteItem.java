package rms.model.item;

import java.io.Serializable;

/**
 *
 * @author Timothy
 */
public class NoteItem extends TextItem implements Serializable {
    
    private static final long serialVersionUID = 01L;
    
    protected NoteItem(ItemThread parentThread){
        this(parentThread, null);
    }
    
    protected NoteItem(ItemThread parentThread, String text){
        super(parentThread, text);
    }

    @Override
    public String getItemTypeName() {
        return "Note";
    }
}
