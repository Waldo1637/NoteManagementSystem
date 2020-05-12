package rms.control;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import rms.model.State;

/**
 * Facilities loading and storing of the {@link State} object from/to the file
 * system.
 *
 * @author Timothy
 */
public final class Loader {

    private static final Logger LOG = Logger.getLogger(Loader.class.getName());

    private static final String DATA_FILE_PATH;

    static {
        // setup the DATA_FILE_PATH variable
        String baseDir = System.getProperty("user.dir");
        if (baseDir == null) {
            LOG.log(Level.SEVERE, "Unable to determine user directory.");
            DATA_FILE_PATH = null;
        } else {
            DATA_FILE_PATH = String.format("%s%sindex_%s.rms",
                    baseDir, File.separator,
                    Loader.class.getPackage().getSpecificationVersion());
        }
    }

    /**
     * Attempts to de-serialize the stored {@link State} from the default path
     * for the current version. Exception is thrown if the file does not exist.
     *
     * @return the {@link State} object stored in file or {@code null} if unable
     *         to de-serialize
     *
     * @throws rms.control.Loader.SerializedStateOutdatedException
     */
    public static synchronized State attemptLoadDefaultFromFile() throws SerializedStateOutdatedException {
        File stateFile = new File(DATA_FILE_PATH);
        if (DATA_FILE_PATH == null || !stateFile.exists()) {
            throw new SerializedStateOutdatedException();
        }
        return loadFromFile(stateFile);
    }

    /**
     * De-serializes the stored {@link State} from the given path, migrating the
     * {@link State} to the newest version if it is outdated.
     *
     * @param stateFile
     *
     * @return the loaded {@code State} or {@code null} if not found or failed
     *         to load for some reason
     */
    public static synchronized State loadFromFile(File stateFile) {
        State retVal;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(stateFile))) {
            retVal = (State) in.readObject();
            LOG.log(Level.FINE, "State loaded from file {0}", stateFile.getAbsolutePath());
        } catch (Exception ex) {    //catch any exception
            LOG.log(Level.INFO, "Failed to load state from " + stateFile.getAbsolutePath(), ex);
            retVal = null;
        }
        return retVal;
    }

    /**
     * Attempts to serialize the given {@link State} object to file.
     *
     * @param state {@link State} object to serialize
     *
     * @return true iff serialization was successful
     */
    public static synchronized boolean storeToFile(State state) {
        boolean success = false;
        if (DATA_FILE_PATH != null) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE_PATH))) {
                out.writeObject(state);
                LOG.log(Level.FINE, "State saved to file {0}", DATA_FILE_PATH);
                success = true;
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Unable to store state.", ex);
            }
        }
        return success;
    }

    public static class SerializedStateOutdatedException extends Exception {
    }

    private Loader() {
    }
}
