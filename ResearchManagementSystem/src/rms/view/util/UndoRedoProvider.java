package rms.view.util;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 * Utility class to add undo/redo functionality to a {@link JTextComponent}.
 *
 * If the {@link JTextComponent} is made uneditable, the undo/redo buffer is
 * cleared.
 *
 * @author Timothy
 */
public class UndoRedoProvider {

    private static final Logger thisLog = Logger.getLogger(UndoRedoProvider.class.getName());

    private final UndoManager manager;

    /**
     * Add undo/redo functionality to the given {@link JTextComponent}.
     *
     * @param component
     */
    public static void addTo(JTextComponent component) {
        new UndoRedoProvider(component);
    }

    private UndoRedoProvider(final JTextComponent component) {
        manager = new UndoManager();

        Document componentDoc = component.getDocument();

        // setup listener to store events to the manager
        componentDoc.addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent evt) {
                manager.addEdit(evt.getEdit());
            }
        });

        // setup undo action
        component.getActionMap().put("Undo", new AbstractAction("Undo") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    if (manager.canUndo() && component.isEditable()) {  //don't attempt undo if the component is not editable
                        manager.undo();
                    }
                } catch (CannotUndoException ignored) {
                }
            }
        });
        component.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");

        // setup redo action
        component.getActionMap().put("Redo", new AbstractAction("Redo") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    if (manager.canRedo() && component.isEditable()) {  //don't attempt redo if the component is not editable
                        manager.redo();
                    }
                } catch (CannotRedoException ignored) {
                }
            }
        });
        component.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");

        // whenever the editable status of the component is changed, clear the undo/redo history
        component.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("editable".equals(e.getPropertyName())) {
                    manager.discardAllEdits();
                }
            }
        });

        thisLog.log(Level.FINE, "undo/redo functionality added to {0}", component);
    }
}
