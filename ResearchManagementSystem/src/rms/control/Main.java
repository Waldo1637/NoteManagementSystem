package rms.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import rms.model.State;
import rms.model.item.ItemFactory;
import rms.model.item.ItemThread;
import rms.view.MainFrame;
import rms.view.util.Prompts;

/**
 *
 * @author Timothy
 */
public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    private static MainFrame gui;
    private static State state;

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        setupLogging();

        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code ">
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            LOG.log(Level.SEVERE, "Error setting L&F", ex);
        }
        //</editor-fold>

        //create the view
        gui = MainFrame.instance();

        // display the UI
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.setVisible(true);
                gui.loadStateAndPopulate();
            }
        });
    }

    private static void setupLogging() {
        String logFileFromProperties = System.getProperty("java.util.logging.config.file");
        if (logFileFromProperties == null) {
            String logFileName = "logging.properties";
            InputStream configStream = Main.class.getClassLoader().getResourceAsStream(logFileName);
            try {
                if (configStream == null) {
                    configStream = new FileInputStream(logFileName);
                }
                LogManager.getLogManager().readConfiguration(configStream);
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Load the data from file to State
     *
     * @return true iff loading was successful
     */
    public static boolean loadStateFromFile() {
        try {
            state = Loader.attemptLoadDefaultFromFile();
        } catch (Loader.SerializedStateOutdatedException ex) {
            Prompts.informUser("Data file not found", "The data file cannot be found. This may be the result of a recent program update. Please select a data file to load.", Prompts.PromptType.WARNING);
            JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
            if (chooser.showOpenDialog(gui) == JFileChooser.APPROVE_OPTION) {
                state = Loader.loadFromFile(chooser.getSelectedFile());
            }
        }
        return state != null;
    }

    /**
     * Store the current state to file
     *
     * @return
     */
    public static boolean storeStateToFile() {
        return Loader.storeToFile(state);
    }

    public static void deleteThread() {
        state.deleteThread(gui.getSelectedThread());
        gui.refreshThreadListAndDisplay();
        gui.clearSelectedThread();
    }

    /**
     * Create a new task on the current thread. If there is no current thread,
     * create one.
     */
    public static void newTask() {
        ItemThread td = getSelectedThreadOrCreate();
        ItemFactory.createAndAddTaskItem(td);
        gui.refreshSelectedThread();
    }

    /**
     * Create a new file on the current thread. If there is no current thread,
     * create one (as long as the user actually selected a file).
     */
    public static void newFile() {
        List<File> selection = gui.promptFileSelection();
        if (!selection.isEmpty()) {
            ItemThread td = getSelectedThreadOrCreate();
            for (File f : selection) {
                ItemFactory.createAndAddFileItem(td, f);
            }
            gui.refreshSelectedThread();//no need to refresh if no selection
        }
    }

    /**
     * Create a new note on the current thread. If there is no current thread,
     * create one.
     */
    public static void newNote() {
        ItemThread td = getSelectedThreadOrCreate();
        ItemFactory.createAndAddNoteItem(td);
        gui.refreshSelectedThread();
    }

    /**
     *
     * @return the current {@link State}
     */
    public static State getState() {
        return state;
    }

    /**
     * Returns the currently selected thread. If none is selected, then creates
     * a new thread, sets it as the current thread, and returns it.
     *
     * @return
     */
    private static ItemThread getSelectedThreadOrCreate() {
        ItemThread td = gui.getSelectedThread();
        if (td == null) {
            td = gui.createAndShowNewThread();
        }
        return td;
    }
}
