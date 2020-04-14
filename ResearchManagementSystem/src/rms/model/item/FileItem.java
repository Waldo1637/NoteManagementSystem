package rms.model.item;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import java.util.logging.Level;

/**
 *
 * @author Timothy
 */
public class FileItem extends Item implements Serializable {

    private static final long serialVersionUID = 01L;

    private final File localFile;

    /**
     * NOTE: use {@link #create(ItemThread, File)} instead (for error handling).
     *
     * @param parentThread
     * @param sourceFile
     */
    private FileItem(ItemThread parentThread, File localFile) {
        super(parentThread);
        this.localFile = localFile;
    }

    public File getFile() {
        return localFile;
    }

    @Override
    public String getItemTypeName() {
        return "File";
    }

    public static class CreateResult {

        public final FileItem item;
        public final String error;

        public CreateResult(FileItem item) {
            this.item = item;
            this.error = null;
        }

        public CreateResult(String error) {
            this.item = null;
            this.error = error;
        }

        public boolean success() {
            return item != null;
        }
    }

    public static CreateResult create(ItemThread parentThread, File sourceFile) {
        //Ensure the source File name is not empty (or else the FileItem would
        //  end up referencing the thread's data folder instead of a file).
        String fileName = sourceFile.getName();
        if (fileName.isEmpty()) {
            return new CreateResult(sourceFile + " (empty name)");
        }
        //Ensure the source file exists (this is a fast check but the
        //  definitive case is Files.copy(..) throwing a NoSuchFileException).
        if (!sourceFile.exists()) {
            return new CreateResult(sourceFile + " (does not exist)");
        }
        //Ensure that the thread directory does not already include a file
        //  with that name (again, a fast check for the exceptional case).
        File localFile = new File(parentThread.getDataFolder(), fileName);
        if (localFile.exists()) {
            return new CreateResult(sourceFile + " (already in thread)");
        }
        //Finally, attempt to copy the file
        try {
            Files.copy(sourceFile.toPath(), localFile.toPath(), COPY_ATTRIBUTES);
        } catch (FileAlreadyExistsException ex) {
            LOG.log(Level.SEVERE, "Unable to copy file", ex);
            return new CreateResult(sourceFile + " (already in thread)");
        } catch (NoSuchFileException ex) {
            LOG.log(Level.SEVERE, "Unable to copy file", ex);
            return new CreateResult(sourceFile + " (does not exist)");
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Unable to copy file", ex);
            return new CreateResult(sourceFile + " " + ex.getMessage());
        }
        //If it succeeds, create and return the new FileItem
        return new CreateResult(new FileItem(parentThread, localFile));
    }

    public static CreateResult createAndAddFileItem(ItemThread parentThread, File sourceFile) {
        CreateResult i = create(parentThread, sourceFile);
        if (i.success()) {
            i.item.appendToParentThread();
        }
        return i;
    }
}
