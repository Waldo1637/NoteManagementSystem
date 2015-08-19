package rms.control;

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

    private static final Logger thisLog = Logger.getLogger(Loader.class.getName());

    private static final String stateFileFullPath;

    static {
        String baseDir = System.getProperty("user.dir");
        String fileName = String.format("index_%s.rms", Loader.class.getPackage().getSpecificationVersion());
        if (baseDir == null) {
            thisLog.log(Level.SEVERE, "Unable to determine user directory.");
            stateFileFullPath = null;
        } else {
            stateFileFullPath = baseDir.concat("/").concat(fileName);
        }
    }

    /**
     * Attempts to deserialize the stored {@code State} from file
     *
     * @return the {@code State} object stored in file or null if unable to
     * deserialize
     *
     * @see rms.model.State
     */
    public static synchronized State loadFromFile() {
        if (stateFileFullPath == null) {
            return null;
        }

        State retVal;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(stateFileFullPath));
            retVal = (State) in.readObject();
            thisLog.log(Level.FINE, "State loaded from file {0}", stateFileFullPath);
        } catch (IOException | ClassNotFoundException ex) {
            thisLog.log(Level.INFO, "Unable to load state. Creating new.", ex);
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
        if (stateFileFullPath == null) {
            return false;
        }

        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(stateFileFullPath));
            out.writeObject(state);
        } catch (IOException ex) {
            thisLog.log(Level.SEVERE, "Unable to store state.", ex);
            return false;
        } finally {
            Helpers.closeResource(out);
        }

        thisLog.log(Level.FINE, "State saved to file {0}", stateFileFullPath);
        return true;
    }
}
