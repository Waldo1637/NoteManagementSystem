package rms.view.util;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 * Utility class to add undo/redo functionality to a {@link JTextComponent}.
 *
 * If the {@link JTextComponent} is made un-editable, the undo/redo buffer is
 * cleared.
 *
 * @author Timothy
 */
public final class UndoRedoProvider {

    private static final Logger LOG = Logger.getLogger(UndoRedoProvider.class.getName());

    /**
     * Add undo/redo functionality to the given {@link JTextComponent}.
     *
     * @param component
     */
    public static void addTo(final JTextComponent component) {
        final UndoManager manager = new UndoManager();

        // setup listener to store events to the manager
        component.getDocument().addUndoableEditListener(manager);
        // setup listener to remap the undoable listener if the document changes
        component.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName() == "document") {
                    ((Document) evt.getOldValue()).removeUndoableEditListener(manager);
                    ((Document) evt.getNewValue()).addUndoableEditListener(manager);
                }
            }
        });

        // add Undo and Redo actions to the component's ActionMap
        final ActionMap actionMap = component.getActionMap();
        actionMap.put("Undo", new AbstractAction("Undo") {
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
        actionMap.put("Redo", new AbstractAction("Redo") {
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

        // setup keyboard shortcuts
        final InputMap inputMap = component.getInputMap();
        inputMap.put(KeyStroke.getKeyStroke("control Z"), "Undo");
        inputMap.put(KeyStroke.getKeyStroke("control Y"), "Redo");

        // whenever the editable status of the component is changed, clear the undo/redo history
        component.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("editable".equals(e.getPropertyName())) {
                    manager.discardAllEdits();
                }
            }
        });

        LOG.log(Level.FINE, "undo/redo functionality added to {0}", component);
    }

    private UndoRedoProvider() {
    }
}
