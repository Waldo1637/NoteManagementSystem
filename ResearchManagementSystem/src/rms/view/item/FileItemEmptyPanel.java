package rms.view.item;

import rms.model.item.EmptyFileItem;

/**
 *
 * @author Timothy Hoffman
 */
public final class FileItemEmptyPanel extends FileItemBasePanel {

    public FileItemEmptyPanel(EmptyFileItem item, boolean startCollapsed) {
        super(item, startCollapsed, "*** Add File ***");
    }

    @Override
    protected void fileButtonAction() {
        auxiliaryButtonAction();
    }
}
