package rms.view.item;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import rms.control.search.AbstractFilter;
import rms.control.search.CompletedTaskFilter;
import rms.control.search.LateTaskFilter;
import rms.control.search.PendingTaskFilter;
import rms.model.item.Item;
import rms.view.util.ButtonAction;
import rms.view.util.Prompts;

/**
 *
 * @author Timothy
 */
public class BaseItemPanel extends javax.swing.JPanel {

    public static class ThreadCollapseAllHandler extends MouseAdapter {

        private final ArrayList<BaseItemPanel> panels = new ArrayList<>();

        public void register(BaseItemPanel registrant) {
            assert !panels.contains(registrant);
            panels.add(registrant);
            registrant.jButtonToggleCollapsed.addMouseListener(this);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                showPopup(e);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                showPopup(e);
            }
        }

        private class CollapseAction extends ButtonAction {

            private final AbstractFilter filter;

            public CollapseAction(String text, String tooltip, int mnemonicAndAccel, AbstractFilter filter) {
                super(text, tooltip, mnemonicAndAccel, false);
                this.filter = filter;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                for (BaseItemPanel p : panels) {
                    if (filter.accept(p.displayedItem)) {
                        p.collapse();
                    }
                }
            }
        }

        private class UncollapseAction extends ButtonAction {

            private final AbstractFilter filter;

            public UncollapseAction(String text, String tooltip, int mnemonicAndAccel, AbstractFilter filter) {
                super(text, tooltip, mnemonicAndAccel, false);
                this.filter = filter;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                for (BaseItemPanel p : panels) {
                    if (filter.accept(p.displayedItem)) {
                        p.uncollapse();
                    }
                }
            }
        }

        private void showPopup(MouseEvent e) {
            JPopupMenu menu = new JPopupMenu();
            {
                JMenu menuCollapse = new JMenu("Collapse");
                menuCollapse.setMnemonic(KeyEvent.VK_C);
                menu.add(menuCollapse);
                menuCollapse.add(new JMenuItem(new CollapseAction(
                        "All",
                        "Collapses all items in the current thread",
                        KeyEvent.VK_A,
                        AbstractFilter.ALL)));
                menuCollapse.addSeparator();
                menuCollapse.add(new JMenuItem(new CollapseAction(
                        "Completed",
                        "Collapses all Tasks in the current thread that are completed",
                        KeyEvent.VK_C,
                        CompletedTaskFilter.get())));
                menuCollapse.add(new JMenuItem(new CollapseAction(
                        "Pending",
                        "Collapses all Tasks in the current thread that are not completed",
                        KeyEvent.VK_P,
                        PendingTaskFilter.get())));
                menuCollapse.add(new JMenuItem(new CollapseAction(
                        "Overdue",
                        "Collapses all Tasks in the current thread that are not completed and due in the past",
                        KeyEvent.VK_O,
                        LateTaskFilter.get())));
            }
            {
                JMenu menuExpand = new JMenu("Expand");
                menuExpand.setMnemonic(KeyEvent.VK_E);
                menu.add(menuExpand);
                menuExpand.add(new JMenuItem(new UncollapseAction(
                        "All",
                        "Expands all items in the current thread",
                        KeyEvent.VK_A,
                        AbstractFilter.ALL)));
                menuExpand.addSeparator();
                menuExpand.add(new JMenuItem(new UncollapseAction(
                        "Completed",
                        "Expands all Tasks in the current thread that are completed",
                        KeyEvent.VK_C,
                        CompletedTaskFilter.get())));
                menuExpand.add(new JMenuItem(new UncollapseAction(
                        "Pending",
                        "Expands all Tasks in the current thread that are not completed",
                        KeyEvent.VK_P,
                        PendingTaskFilter.get())));
                menuExpand.add(new JMenuItem(new UncollapseAction(
                        "Overdue",
                        "Expands all Tasks in the current thread that are not completed and due in the past",
                        KeyEvent.VK_O,
                        LateTaskFilter.get())));
            }
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    protected final Item displayedItem;

    /**
     * No-arg constructor for Bean creation. NOTE: Do not use.
     */
    public BaseItemPanel() {
        this(null, false);
    }

    /**
     * Creates a new BaseItemPanel displaying the given {@link Item}
     *
     * @param item
     * @param startCollapsed
     */
    public BaseItemPanel(Item item, boolean startCollapsed) {
        displayedItem = item;
        initComponents();
        if (startCollapsed) {
            collapse();
        }

        // need to have this check to use the Bean in NetBeans GUI editor
        if (displayedItem != null) {
            reflectItemChangesInUI_Base();
            jLabelItemType.setText(displayedItem.getItemTypeName());
            jLabelItemID.setText(String.format("%05d", displayedItem.getID()));
            jLabelItemDateCreated.setText(displayedItem.getCreationTime().toString());
        }
    }

    /**
     * For Bean implementation.
     *
     * @return the {@link Container} where content can be placed by subclasses.
     */
    public final Container getInnerContentPanel() {
        return jPanelContent;
    }

    public final JButton getAuxButton() {
        return jButtonAuxiliary;
    }

    /**
     * Update the basic {@link Item} properties in the UI along with anything
     * defined in {@link #reflectItemChangesInUI_Additional()}.
     */
    public final void reflectItemChangesInUI() {
        reflectItemChangesInUI_Base();
        reflectItemChangesInUI_Additional();
    }

    /**
     * Update the base Item properties that may change
     */
    private void reflectItemChangesInUI_Base() {
        jLabelItemDateModified.setText(displayedItem.getModificationTime().toString());
    }

    private void collapse() {
        jXCollapsiblePaneContent.setCollapsed(true);
        jButtonToggleCollapsed.setText("∨");
    }

    private void uncollapse() {
        jXCollapsiblePaneContent.setCollapsed(false);
        jButtonToggleCollapsed.setText("∧");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jXCollapsiblePaneContent = new org.jdesktop.swingx.JXCollapsiblePane();
        jPanelContent = new javax.swing.JPanel();
        jPanelHeader = new javax.swing.JPanel();
        jLabelItemType = new javax.swing.JLabel();
        jLabelItemID = new javax.swing.JLabel();
        jLabelCreated = new javax.swing.JLabel();
        jLabelItemDateCreated = new javax.swing.JLabel();
        jLabelModified = new javax.swing.JLabel();
        jLabelItemDateModified = new javax.swing.JLabel();
        jButtonAuxiliary = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jLabelColon = new javax.swing.JLabel();
        jButtonToggleCollapsed = new javax.swing.JButton();

        jPanelContent.setLayout(new java.awt.BorderLayout());
        jXCollapsiblePaneContent.getContentPane().add(jPanelContent);

        jLabelItemType.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelItemType.setText("Type");

        jLabelItemID.setText("00000");

        jLabelCreated.setText("Created:");

        jLabelItemDateCreated.setText("date");

        jLabelModified.setText("Modified:");

        jLabelItemDateModified.setText("date");

        jButtonAuxiliary.setText("Edit");
        jButtonAuxiliary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAuxiliaryActionPerformed(evt);
            }
        });

        jButtonDelete.setText("Delete");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jLabelColon.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelColon.setText(":");

        jButtonToggleCollapsed.setFont(jButtonToggleCollapsed.getFont().deriveFont((float)12));
        jButtonToggleCollapsed.setText("∧");
        jButtonToggleCollapsed.setAlignmentX(0.5F);
        jButtonToggleCollapsed.setFocusPainted(false);
        jButtonToggleCollapsed.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonToggleCollapsed.setMargin(new java.awt.Insets(1, 1, 1, 1));
        jButtonToggleCollapsed.setRolloverEnabled(false);
        jButtonToggleCollapsed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonToggleCollapsedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelHeaderLayout = new javax.swing.GroupLayout(jPanelHeader);
        jPanelHeader.setLayout(jPanelHeaderLayout);
        jPanelHeaderLayout.setHorizontalGroup(
            jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHeaderLayout.createSequentialGroup()
                .addGroup(jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelHeaderLayout.createSequentialGroup()
                        .addComponent(jLabelCreated)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelItemDateCreated)
                        .addGap(27, 27, 27)
                        .addComponent(jLabelModified)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelItemDateModified))
                    .addGroup(jPanelHeaderLayout.createSequentialGroup()
                        .addComponent(jLabelItemType)
                        .addGap(0, 0, 0)
                        .addComponent(jLabelColon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelItemID)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 179, Short.MAX_VALUE)
                .addComponent(jButtonAuxiliary)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonToggleCollapsed)
                .addGap(1, 1, 1))
        );
        jPanelHeaderLayout.setVerticalGroup(
            jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHeaderLayout.createSequentialGroup()
                .addGroup(jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelItemType)
                    .addComponent(jLabelItemID)
                    .addComponent(jLabelColon))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCreated)
                    .addComponent(jLabelItemDateCreated)
                    .addComponent(jLabelModified)
                    .addComponent(jLabelItemDateModified)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButtonAuxiliary)
                .addComponent(jButtonDelete)
                .addComponent(jButtonToggleCollapsed, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jXCollapsiblePaneContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jXCollapsiblePaneContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAuxiliaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAuxiliaryActionPerformed
        uncollapse();
        auxiliaryButtonAction();
    }//GEN-LAST:event_jButtonAuxiliaryActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        deleteButtonAction_PreApproval();
        if (Prompts.getUserApproval("Are you sure you want to delete this item?", Prompts.PromptType.QUESTION)) {
            boolean result = displayedItem.getThread().remove(displayedItem);
            assert result;
            deleteButtonAction_WithApproval();

            //cheap way to update UI
            Container p = this.getParent();
            p.remove(this);
            p.revalidate();
            p.repaint();
        } else {
            deleteButtonAction_WithoutApproval();
        }
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonToggleCollapsedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonToggleCollapsedActionPerformed
        if (jXCollapsiblePaneContent.isCollapsed()) {
            uncollapse();
        } else {
            collapse();
        }
    }//GEN-LAST:event_jButtonToggleCollapsedActionPerformed

    /**
     * Executed after the user clicks the Delete button but before the user is
     * prompted for approval.
     */
    protected void deleteButtonAction_PreApproval() {
        //No action by default
    }

    /**
     * Executed after the user has given approval to delete the {@link Item} and
     * the basic deletion has taken place.
     */
    protected void deleteButtonAction_WithApproval() {
        //No action by default
    }

    /**
     * Executed if the user does not give approval for the item to be deleted.
     */
    protected void deleteButtonAction_WithoutApproval() {
        //No action by default
    }

    /**
     * Executed when the Auxiliary button is clicked.
     */
    protected void auxiliaryButtonAction() {
        //No action by default
    }

    /**
     * Executed as a result of reflectItemChangesInUI() after the basic
     * {@link Item} properties have been updated in the UI.
     */
    protected void reflectItemChangesInUI_Additional() {
        //No action by default
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAuxiliary;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonToggleCollapsed;
    private javax.swing.JLabel jLabelColon;
    private javax.swing.JLabel jLabelCreated;
    private javax.swing.JLabel jLabelItemDateCreated;
    private javax.swing.JLabel jLabelItemDateModified;
    private javax.swing.JLabel jLabelItemID;
    private javax.swing.JLabel jLabelItemType;
    private javax.swing.JLabel jLabelModified;
    private javax.swing.JPanel jPanelContent;
    private javax.swing.JPanel jPanelHeader;
    private org.jdesktop.swingx.JXCollapsiblePane jXCollapsiblePaneContent;
    // End of variables declaration//GEN-END:variables
}
