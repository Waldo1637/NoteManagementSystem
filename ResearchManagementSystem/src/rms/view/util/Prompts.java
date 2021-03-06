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
public final class Prompts {

    private static final Logger LOG = Logger.getLogger(Prompts.class.getName());

    public static enum PromptType {
        PLAIN, INFO, ERROR, WARNING, QUESTION
    };

    public static enum Response {
        YES, NO, CANCEL
    };

    /**
     * Prompts the user with a yes/no dialog.
     *
     * @param prompt
     * @param type
     *
     * @return {@code true} only if the user selected "yes"
     */
    public static boolean getUserApproval(String prompt, PromptType type) {
        String title = "Response needed";
        int optionType = JOptionPane.YES_NO_OPTION;
        int messageType = convert(type);
        int option = JOptionPane.showConfirmDialog(MainFrame.instance(), prompt, title, optionType, messageType);
        return option == JOptionPane.YES_OPTION;
    }

    /**
     * Prompts the user with a yes/no/cancel dialog.
     *
     * @param prompt
     * @param type
     *
     * @return the specific {@link Response} from the user
     */
    public static Response getUserResponse(String prompt, PromptType type) {
        String title = "Response needed";
        int optionType = JOptionPane.YES_NO_CANCEL_OPTION;
        int messageType = convert(type);
        int option = JOptionPane.showConfirmDialog(MainFrame.instance(), prompt, title, optionType, messageType);
        return convertToResponse(option);
    }

    /**
     * Prompts the user with a text entry dialog.
     *
     * @param prompt
     * @param type
     *
     * @return user entered value or null if the user canceled
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
                LOG.log(Level.SEVERE, "Undefined enum value: {0}", type);
                return JOptionPane.PLAIN_MESSAGE;
        }
    }

    private static Response convertToResponse(int confirmDialogResult) {
        switch (confirmDialogResult) {
            case JOptionPane.YES_OPTION:
                return Response.YES;
            case JOptionPane.NO_OPTION:
                return Response.NO;
            case JOptionPane.CANCEL_OPTION:
            case JOptionPane.CLOSED_OPTION:
                return Response.CANCEL;
            default:
                LOG.log(Level.SEVERE, "Undefined dialog result value: {0}", confirmDialogResult);
                return Response.CANCEL;
        }
    }

    private Prompts() {
    }
}
