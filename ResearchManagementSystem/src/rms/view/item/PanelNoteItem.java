package rms.view.item;

import java.util.Date;
import rms.model.item.NoteItem;

/**
 * Panel for displaying a {@link NoteItem}
 * 
 * @author Timothy
 */
public class PanelNoteItem extends PanelAnyTextItem {

    public PanelNoteItem(NoteItem itm) {
        super(itm);
        showStatusPanel(false);
    }
    
    @Override
    protected StatusIndicator getStatus(){
        return StatusIndicator.COMPLETE;
    }
    
    @Override
    protected void togglePressed(){
        //NOTHING
    }

    @Override
    protected void deadlineUpdated(Date newDate) {
        //NOTHING
    }
}
