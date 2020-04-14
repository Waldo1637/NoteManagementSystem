package rms.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import rms.model.State;
import rms.util.Helpers;

/**
 * Facilities to load the State from file and store it to file
 *
 * @author Timothy
 */
public class Loader {

    private static final Logger LOG = Logger.getLogger(Loader.class.getName());

    private static final String currentVersionStateFileFullPath;

    static {
        // setup the currentVersionStateFileFullPath variable
        String baseDir = System.getProperty("user.dir");
        String fileName = String.format("index_%s.rms", Loader.class.getPackage().getSpecificationVersion());
        if (baseDir == null) {
            LOG.log(Level.SEVERE, "Unable to determine user directory.");
            currentVersionStateFileFullPath = null;
        } else {
            currentVersionStateFileFullPath = baseDir.concat("/").concat(fileName);
        }
    }

    /**
     * Attempts to deserialize the stored {@code State} from the default path
     * for the current version. Exception is thrown if the file does not exist.
     *
     * @return the {@code State} object stored in file or null if unable to
     *         deserialize
     *
     * @throws rms.control.Loader.SerializedStateOutdatedException
     * @see rms.model.State
     */
    public static synchronized State attemptLoadDefaultFromFile() throws SerializedStateOutdatedException {
        File stateFile = new File(currentVersionStateFileFullPath);
        if (currentVersionStateFileFullPath == null || !stateFile.exists()) {
            throw new SerializedStateOutdatedException();
        }

        return loadFromFile(stateFile);
    }

    /**
     * Deserializes the stored {@code State} from the given path migrating the
     * given State to the newest version if it is outdated.
     *
     * @param stateFile
     *
     * @return
     */
    public static synchronized State loadFromFile(File stateFile) {
        State retVal = null;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(stateFile));
            retVal = (State) in.readObject();
            LOG.log(Level.FINE, "State loaded from file {0}", stateFile.getAbsolutePath());
        } catch (Exception ex) {    //catch any exception
            LOG.log(Level.INFO, "Unable to load state from " + stateFile.getAbsolutePath() + ". Creating new.", ex);
            retVal = new State();
        } finally {
            Helpers.closeResource(in);
        }
        return retVal;
    }

    /**
     * Attempts to serialize the given {@code State} object to file
     *
     * @param state {@code State} object to serialize
     *
     * @return true iff serialization was successful
     *
     * @see rms.model.State
     */
    public static synchronized boolean storeToFile(State state) {
        boolean success = false;

        if (currentVersionStateFileFullPath != null) {
            ObjectOutputStream out = null;
            try {
                out = new ObjectOutputStream(new FileOutputStream(currentVersionStateFileFullPath));
                out.writeObject(state);
                LOG.log(Level.FINE, "State saved to file {0}", currentVersionStateFileFullPath);
                success = true;
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Unable to store state.", ex);
            } finally {
                Helpers.closeResource(out);
            }
        }
        return success;
    }

    public static class SerializedStateOutdatedException extends Exception {
    }

    private Loader() {
    }
}
