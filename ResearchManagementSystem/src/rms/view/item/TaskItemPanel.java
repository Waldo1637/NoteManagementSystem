package rms.view.item;

import java.awt.Color;
import java.util.logging.Logger;
import rms.model.item.TaskItem;

/**
 *
 * @author Timothy
 */
public class TaskItemPanel extends BaseItemPanel {

    private static final Logger LOG = Logger.getLogger(TaskItemPanel.class.getName());

    /**
     * Creates new form TaskItemPanel
     *
     * @param item
     * @param startCollapsed
     */
    public TaskItemPanel(TaskItem item, boolean startCollapsed) {
        super(item, startCollapsed);
        initComponents();
        reflectItemChangesInUI_Additional();

        //register a listener to update the UI when the user changes text
        itemTextField.addItemTextUpdateListener(new EditableTextField.ItemTextUpdateListener() {
            @Override
            public void textUpdated(EditableTextField.ItemTextUpdatedEvent evt) {
                reflectItemChangesInUI();
            }
        });
    }

    private TaskItem getTaskItem() {
        return (TaskItem) displayedItem;
    }

    @Override
    protected void reflectItemChangesInUI_Additional() {
        jXDatePickerDeadline.setDate(getTaskItem().getDeadline());
        itemTextField.updateViewFromItem();
        updateStatusIndicator();
    }

    private void updateStatusIndicator() {
        TaskItem ti = getTaskItem();
        if (ti.isComplete()) {
            jLabelStatus.setText("Completed");
            jLabelStatus.setForeground(new Color(0, 175, 0));
        } else if (ti.isOverdue()) {
            jLabelStatus.setText("Overdue");
            jLabelStatus.setForeground(Color.red);
        } else {
            jLabelStatus.setText("Pending");
            jLabelStatus.setForeground(Color.yellow);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelStatus = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jXDatePickerDeadline = new org.jdesktop.swingx.JXDatePicker();
        jLabelStatus = new javax.swing.JLabel();
        jButtonToggleStatus = new javax.swing.JButton();
        itemTextField = new rms.view.item.EditableTextField(getTaskItem(), super.getAuxButton());

        jLabel1.setText("Deadline:");

        jXDatePickerDeadline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jXDatePickerDeadlineActionPerformed(evt);
            }
        });

        jLabelStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelStatus.setText("status");

        jButtonToggleStatus.setText("Toggle Status");
        jButtonToggleStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonToggleStatusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelStatusLayout = new javax.swing.GroupLayout(jPanelStatus);
        jPanelStatus.setLayout(jPanelStatusLayout);
        jPanelStatusLayout.setHorizontalGroup(
            jPanelStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelStatusLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jXDatePickerDeadline, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 215, Short.MAX_VALUE)
                .addComponent(jLabelStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonToggleStatus))
        );
        jPanelStatusLayout.setVerticalGroup(
            jPanelStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jXDatePickerDeadline, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel1)
                .addComponent(jLabelStatus)
                .addComponent(jButtonToggleStatus))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getInnerContentPanel());
        getInnerContentPanel().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(itemTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jXDatePickerDeadlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXDatePickerDeadlineActionPerformed
        getTaskItem().setDeadline(jXDatePickerDeadline.getDate());
        reflectItemChangesInUI();
    }//GEN-LAST:event_jXDatePickerDeadlineActionPerformed

    private void jButtonToggleStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonToggleStatusActionPerformed
        TaskItem ti = getTaskItem();
        if (ti.isComplete()) {
            ti.markActive();
        } else {
            ti.markCompleted();
        }

        reflectItemChangesInUI();
    }//GEN-LAST:event_jButtonToggleStatusActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rms.view.item.EditableTextField itemTextField;
    private javax.swing.JButton jButtonToggleStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JPanel jPanelStatus;
    private org.jdesktop.swingx.JXDatePicker jXDatePickerDeadline;
    // End of variables declaration//GEN-END:variables
}
