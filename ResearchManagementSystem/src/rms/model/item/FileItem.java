package rms.model.item;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Timothy
 */
public class FileItem extends Item implements Serializable {

    private static final long serialVersionUID = 01L;
    private static final Logger thisLog = Logger.getLogger(FileItem.class.getName());

    private final File localFile;

    protected FileItem(ItemThread parentThread, File sourceFile) {
        super(parentThread);

        localFile = new File(parentThread.getDataFolder(), sourceFile.getName());

        try {
            Files.copy(sourceFile.toPath(), localFile.toPath(), COPY_ATTRIBUTES, REPLACE_EXISTING);
        } catch (IOException ex) {
            thisLog.log(Level.SEVERE, "Unable to copy file", ex);
        }

    }

    public File getFile() {
        return localFile;
    }

    @Override
    public String getItemTypeName() {
        return "File";
    }
}
