package rms.view.item;

import java.awt.Color;
import java.awt.Container;
import java.util.Date;
import java.util.logging.Logger;
import javax.swing.UIDefaults;
import rms.model.item.TextItem;
import rms.view.util.Prompts;
import rms.view.util.Prompts.PromptType;
import rms.view.util.TabSizeStyledEditorKit;
import rms.view.util.UndoRedoProvider;

/**
 * Generic panel for displaying a {@link TextItem}
 * 
 * @author Timothy
 */
public abstract class PanelAnyTextItem extends javax.swing.JPanel {
    
    private static final Logger thisLog = Logger.getLogger(PanelAnyTextItem.class.getName());

    public enum StatusIndicator {PENDING, COMPLETE, OVERDUE};
    
    private static final String SAVE = "Save";
    private static final String EDIT = "Edit";
    
    protected final TextItem item;
    
    /**
     * Creates new form PanelNoteItem
     * @param itm
     */
    public PanelAnyTextItem(TextItem itm) {
        initComponents();
        jTextPaneDesc.setEditorKit(new TabSizeStyledEditorKit(36));
        UndoRedoProvider.addTo(jTextPaneDesc);
        changeDescPaneColor(Color.LIGHT_GRAY);
        
        item = itm;
        
        reflectItemChanges();
    }
    
    private void reflectItemChanges(){
        jLabelType.setText(item.getItemTypeName());
        jLabelID.setText(String.format("%05d", item.getID()));
        jLabelDateCreated.setText(item.getCreationTime().toString());
        jLabelDateModified.setText(item.getModificationTime().toString());
        jTextPaneDesc.setText(item.getText());
        
        switch(getStatus()){
            case PENDING:
                jLabelStatus.setText("Pending");
                jLabelStatus.setForeground(Color.yellow);
                break;
            case COMPLETE:
                jLabelStatus.setText("Completed");
                jLabelStatus.setForeground(new Color(0,175,0));
                break;
            case OVERDUE:
                jLabelStatus.setText("Overdue");
                jLabelStatus.setForeground(Color.red);
                break;
            default:
                jLabelStatus.setText("unavailable");
                jLabelStatus.setForeground(Color.black);
        }
    }
    
    protected final void showStatusPanel(boolean b){
        jPanelStatus.setVisible(b);   
    }
    
    //NOTE: The 3 abstract methods here exist because the UI was created to 
    //      support one of two subtypes of TextItem. This is a misuse of inheritance.
    
    protected abstract StatusIndicator getStatus();
    
    protected abstract void togglePressed();
    
    protected abstract void deadlineUpdated(Date newDate);
    
    protected void setDeadline(Date newDate){
        jXDatePickerDeadline.setDate(newDate);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelHeader = new javax.swing.JPanel();
        jLabelType = new javax.swing.JLabel();
        jLabelID = new javax.swing.JLabel();
        jLabelCreated = new javax.swing.JLabel();
        jLabelDateCreated = new javax.swing.JLabel();
        jLabelModified = new javax.swing.JLabel();
        jLabelDateModified = new javax.swing.JLabel();
        jButtonEdit = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jLabelType1 = new javax.swing.JLabel();
        jPanelStatus = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jXDatePickerDeadline = new org.jdesktop.swingx.JXDatePicker();
        jLabelStatus = new javax.swing.JLabel();
        jButtonToggleStatus = new javax.swing.JButton();
        jPanelText = new javax.swing.JPanel();
        jTextPaneDesc = new javax.swing.JTextPane();

        setName(""); // NOI18N

        jLabelType.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelType.setText("Type");

        jLabelID.setText("00000");

        jLabelCreated.setText("Created:");

        jLabelDateCreated.setText("Date");

        jLabelModified.setText("Modified:");

        jLabelDateModified.setText("Date");

        jButtonEdit.setText("Edit");
        jButtonEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditActionPerformed(evt);
            }
        });

        jButtonDelete.setText("Delete");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jLabelType1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelType1.setText(":");

        javax.swing.GroupLayout jPanelHeaderLayout = new javax.swing.GroupLayout(jPanelHeader);
        jPanelHeader.setLayout(jPanelHeaderLayout);
        jPanelHeaderLayout.setHorizontalGroup(
            jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHeaderLayout.createSequentialGroup()
                .addGroup(jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelHeaderLayout.createSequentialGroup()
                        .addComponent(jLabelCreated)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelDateCreated)
                        .addGap(27, 27, 27)
                        .addComponent(jLabelModified)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelDateModified))
                    .addGroup(jPanelHeaderLayout.createSequentialGroup()
                        .addComponent(jLabelType)
                        .addGap(0, 0, 0)
                        .addComponent(jLabelType1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelID)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDelete))
        );
        jPanelHeaderLayout.setVerticalGroup(
            jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHeaderLayout.createSequentialGroup()
                .addGroup(jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelType)
                    .addComponent(jLabelID)
                    .addComponent(jLabelType1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCreated)
                    .addComponent(jLabelDateCreated)
                    .addComponent(jLabelModified)
                    .addComponent(jLabelDateModified)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButtonEdit)
                .addComponent(jButtonDelete))
        );

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
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

        jTextPaneDesc.setEditable(false);
        jTextPaneDesc.setName(""); // NOI18N
        jTextPaneDesc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextPaneDescFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanelTextLayout = new javax.swing.GroupLayout(jPanelText);
        jPanelText.setLayout(jPanelTextLayout);
        jPanelTextLayout.setHorizontalGroup(
            jPanelTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextPaneDesc)
        );
        jPanelTextLayout.setVerticalGroup(
            jPanelTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextPaneDesc)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditActionPerformed
        switch (jButtonEdit.getText()) {
            case SAVE:
                saveAction();
                break;
            case EDIT:
                editAction();
                break;
        }
    }//GEN-LAST:event_jButtonEditActionPerformed

    private void jTextPaneDescFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextPaneDescFocusLost
        if(evt.getOppositeComponent() != jButtonEdit){
            saveAction();
        }
    }//GEN-LAST:event_jTextPaneDescFocusLost

    private void jButtonToggleStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonToggleStatusActionPerformed
        togglePressed();
        reflectItemChanges();
    }//GEN-LAST:event_jButtonToggleStatusActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        if(Prompts.getUserApproval("Are you sure you want to delete this item?", PromptType.QUESTION)){
            deleteItem();
        }
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jXDatePickerDeadlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXDatePickerDeadlineActionPerformed
        deadlineUpdated(jXDatePickerDeadline.getDate());
        reflectItemChanges();
    }//GEN-LAST:event_jXDatePickerDeadlineActionPerformed

    private void deleteItem(){
        item.getThread().remove(item);
        //cheap way to update UI
        Container p = this.getParent();
        p.remove(this);
        p.revalidate();
        p.repaint();
    }
    
    private void editAction(){
        jTextPaneDesc.requestFocusInWindow();
        changeDescPaneColor(Color.WHITE);
        jButtonEdit.setText(SAVE);
        reflectItemChanges();
        jTextPaneDesc.setEditable(true);
    }
    
    private void saveAction(){
        jButtonEdit.setText(EDIT);
        changeDescPaneColor(Color.LIGHT_GRAY);

        String oldText = item.getText();
        String newText = jTextPaneDesc.getText();
        if(!newText.equals(oldText)){
            item.replaceText(newText);
        }

        reflectItemChanges();
        jTextPaneDesc.setEditable(false);
    }
    
    private void changeDescPaneColor(Color c){
        UIDefaults defaults = new UIDefaults();
        defaults.put("TextPane[Enabled].backgroundPainter", c);
        jTextPaneDesc.putClientProperty("Nimbus.Overrides", defaults);
        jTextPaneDesc.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
        jTextPaneDesc.setBackground(c);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JButton jButtonToggleStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelCreated;
    private javax.swing.JLabel jLabelDateCreated;
    private javax.swing.JLabel jLabelDateModified;
    private javax.swing.JLabel jLabelID;
    private javax.swing.JLabel jLabelModified;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JLabel jLabelType;
    private javax.swing.JLabel jLabelType1;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelStatus;
    private javax.swing.JPanel jPanelText;
    private javax.swing.JTextPane jTextPaneDesc;
    private org.jdesktop.swingx.JXDatePicker jXDatePickerDeadline;
    // End of variables declaration//GEN-END:variables
}
