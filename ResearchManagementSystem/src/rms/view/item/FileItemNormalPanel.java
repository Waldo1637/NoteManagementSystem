package rms.view.item;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.io.File;
import java.util.logging.Level;
import rms.model.item.FileItem;
import rms.view.util.Prompts;

/**
 *
 * @author Timothy Hoffman
 */
public class FileItemNormalPanel extends FileItemBasePanel {

    public FileItemNormalPanel(FileItem item, boolean startCollapsed) {
        super(item, startCollapsed, item.getFile().getName());
    }

    protected File getFile() {
        return ((FileItem) displayedItem).getFile();
    }

    @Override
    protected void deleteButtonAction_WithApproval() {
        getFile().delete();
    }

    @Override
    protected void fileButtonAction() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Desktop.getDesktop().open(getFile());
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "Unable to open file.", ex);
                }
            }
        });
    }

    @Override
    protected void auxiliaryButtonAction() {
        String question = "Would you like to replace the existing file with a new one?";
        if (Prompts.getUserApproval(question, Prompts.PromptType.QUESTION)) {
            super.auxiliaryButtonAction();
        }
    }
}
