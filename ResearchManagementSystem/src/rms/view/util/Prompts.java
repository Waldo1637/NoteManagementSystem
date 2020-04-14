package rms.view.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import rms.view.MainFrame;

/**
 * Utility class for obtaining information from the user via dialogs
 *
 * @author Timothy
 */
public class Prompts {

    private static final Logger LOG = Logger.getLogger(Prompts.class.getName());

    public static enum PromptType {
        PLAIN, INFO, ERROR, WARNING, QUESTION
    };

    /**
     *
     * @param prompt
     * @param type
     *
     * @return
     */
    public static boolean getUserApproval(String prompt, PromptType type) {
        String title = "Response needed";
        int intType = convert(type);
        int option = JOptionPane.showConfirmDialog(MainFrame.instance(), prompt, title, JOptionPane.YES_NO_OPTION, intType);
        return option == JOptionPane.YES_OPTION;
    }

    /**
     *
     * @param prompt
     * @param type
     *
     * @return user entered value or null if the user Cancelled
     */
    public static String getUserInput(String prompt, PromptType type) {
        String title = "Response needed";
        int intType = convert(type);
        return JOptionPane.showInputDialog(MainFrame.instance(), prompt, title, intType);
    }

    /**
     *
     * @param title
     * @param prompt
     * @param type
     */
    public static void informUser(String title, String prompt, PromptType type) {
        JOptionPane.showMessageDialog(MainFrame.instance(), prompt, title, convert(type));
    }

    private static int convert(PromptType type) {
        switch (type) {
            case PLAIN:
                return JOptionPane.PLAIN_MESSAGE;
            case INFO:
                return JOptionPane.INFORMATION_MESSAGE;
            case ERROR:
                return JOptionPane.ERROR_MESSAGE;
            case WARNING:
                return JOptionPane.WARNING_MESSAGE;
            case QUESTION:
                return JOptionPane.QUESTION_MESSAGE;
            default:
                LOG.log(Level.SEVERE, "Undefined enum value.");
                return JOptionPane.PLAIN_MESSAGE;
        }
    }

    private Prompts() {
    }
}
