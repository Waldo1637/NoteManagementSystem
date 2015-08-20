package rms.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import rms.model.State;
import rms.model.item.ItemFactory;
import rms.model.item.ItemThread;
import rms.view.MainFrame;

/**
 *
 * @author Timothy
 */
public class Main {
    
    private static final Logger thisLog = Logger.getLogger(Main.class.getName());
    
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
            thisLog.log(Level.SEVERE, "Error setting L&F", ex);
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
     * @return true iff loading was successful
     */
    public static boolean loadStateFromFile(){
        state = Loader.loadFromFile();
        return state != null;
    }
    
    /**
     * Store the current state to file
     * @return 
     */
    public static boolean storeStateToFile(){
        return Loader.storeToFile(state);
    }
    
    public static void deleteThread(){
        state.getThreads().remove(gui.getSelectedThread());
        gui.refreshThreadListAndDisplay();
        gui.clearSelectedThread();
    }
    
    /**
     * Create a new task on the current thread. If there is no current thread, 
     * create one.
     */
    public static void newTask() {                                                 
        ItemThread td = getSelectedThreadOrCreate();
        ItemFactory.newTaskItem(td);
        gui.refreshSelectedThread();
    }
    
    /**
     * Create a new file on the current thread. If there is no current thread, 
     * create one.
     */
    public static void newFile() {                                                 
        ItemThread td = getSelectedThreadOrCreate();
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        if(chooser.showOpenDialog(gui) == JFileChooser.APPROVE_OPTION) {
            for(File f : chooser.getSelectedFiles()){
                ItemFactory.newFileItem(td, f);
            }
        }
        gui.refreshSelectedThread();
    }
    
    /**
     * Create a new note on the current thread. If there is no current thread, 
     * create one.
     */
    public static void newNote() {                                                 
        ItemThread td = getSelectedThreadOrCreate();
        ItemFactory.newNoteItem(td);
        gui.refreshSelectedThread();
    }
    
    /**
     * 
     * @return the current {@link State} 
     */
    public static State getState(){
        return state;
    }
    
    /**
     * Gets the currently selected thread. If none is selected, then creates new.
     * @return 
     */
    private static ItemThread getSelectedThreadOrCreate(){
        ItemThread td = gui.getSelectedThread();
        if(td == null){
            td = gui.createAndShowNewThread();
        }
        return td;
    }
}
