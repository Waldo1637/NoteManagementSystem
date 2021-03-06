package rms.view.util;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import rms.model.Tag;

/**
 * Generic JPanel for displaying a list of {@link Tag}s and allowing the user to
 * instantly filter the displayed list.
 *
 * @author Timothy
 */
public final class JPanelTagSelection extends javax.swing.JPanel {

    private final ArrayList<DoubleClickSelectionListener> listeners;
    private final Set<Tag> completeTagList;

    /**
     * Listener for the search box to provide filtering when text is changed.
     */
    private final DocumentListener searchTextListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            updateListItemsShown();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateListItemsShown();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateListItemsShown();
        }
    };

    /**
     * Create an empty TagSelectionPanel.
     */
    public JPanelTagSelection() {
        this(Collections.<Tag>emptySet());
    }

    /**
     * Create a TagSelectionPanel with the given set of {@link Tag}s.
     *
     * @param existingTags
     */
    public JPanelTagSelection(Set<Tag> existingTags) {
        this.completeTagList = existingTags;
        this.listeners = new ArrayList<>();
        initComponents();
        jTextFieldSearch.getDocument().addDocumentListener(searchTextListener);
        updateListItemsShown();
    }

    /**
     * Update the displayed {@link Tag} list to show all items that contain the
     * text currently in the search bar.
     */
    public final void updateListItemsShown() {
        String filterText = jTextFieldSearch.getText();
        jListTags.setModel(new SearchTagsListModel(completeTagList, filterText));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jListTags = new javax.swing.JList<Tag>();
        jTextFieldSearch = new javax.swing.JTextField();

        jListTags.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListTagsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jListTags);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextFieldSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTextFieldSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jListTagsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListTagsMouseClicked
        // Capture double-click event on a list item and notify all listeners.
        // Include all currently selected items.
        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
            SelectedTags selection = new SelectedTags(evt.getSource(), getSelectedTags());
            synchronized (this) {
                for (DoubleClickSelectionListener l : listeners) {
                    l.itemsSelected(selection); // fire the event to this listener
                }
            }
        }
    }//GEN-LAST:event_jListTagsMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<Tag> jListTags;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldSearch;
    // End of variables declaration//GEN-END:variables

    /**
     *
     * @return the Set of {@link Tag}s currently selected
     */
    public Set<Tag> getSelectedTags() {
        return new HashSet<>(jListTags.getSelectedValuesList());
    }

    /**
     * Registers the given DoubleClickSelectionListener to handle selection of
     * items via double-click on an item.
     *
     * @param hearer
     */
    public synchronized void addDoubleClickSelectionListener(DoubleClickSelectionListener hearer) {
        listeners.add(hearer);
    }

    /**
     * Unregisters the given DoubleClickSelectionListener.
     *
     * @param hearer
     */
    public synchronized void removeDoubleClickSelectionListener(DoubleClickSelectionListener hearer) {
        listeners.remove(hearer);
    }

    /**
     * An {@link EventObject} containing a Set of {@link Tag}s chosen.
     */
    public static class SelectedTags extends java.util.EventObject {

        private final Set<Tag> selection;

        private SelectedTags(Object source, Set<Tag> selection) {
            super(source);
            this.selection = selection;
        }

        public Set<Tag> getSelection() {
            return selection;
        }
    }

    /**
     * An {@link EventListener} that is fired when items from the list are
     * selected via a double-click on an item in the list.
     */
    public interface DoubleClickSelectionListener extends java.util.EventListener {

        void itemsSelected(SelectedTags m);
    }
}
