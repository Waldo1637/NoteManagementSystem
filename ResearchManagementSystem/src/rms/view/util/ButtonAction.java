package rms.view.util;

import java.awt.event.InputEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

/**
 *
 * @author Timothy Hoffman
 */
public abstract class ButtonAction extends AbstractAction {

    public ButtonAction(String text, String tooltip, int mnemonicAndAccel) {
        init(text, tooltip, mnemonicAndAccel, true);
    }

    public ButtonAction(String text, String tooltip, int mnemonicAndAccel, boolean addAccel) {
        init(text, tooltip, mnemonicAndAccel, addAccel);
    }

    private void init(String text, String tooltip, int mnemonicAndAccel, boolean addAccel) {
        putValue(Action.NAME, text);//button text (action name)
        putValue(Action.SHORT_DESCRIPTION, tooltip);
        putValue(Action.MNEMONIC_KEY, mnemonicAndAccel);
        if (addAccel) {
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(mnemonicAndAccel, InputEvent.CTRL_MASK));
        }
    }
}
