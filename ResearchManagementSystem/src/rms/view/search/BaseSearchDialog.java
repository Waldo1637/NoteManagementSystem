package rms.view.search;

import java.awt.Container;
import java.awt.Frame;
import rms.control.search.AbstractFinder;

/**
 * Generic dialog box providing Ok and Cancel functionality
 *
 * @author Timothy
 */
public class BaseSearchDialog extends javax.swing.JDialog {

    private boolean approved;

    /**
     * No-arg constructor for Bean creation
     */
    public BaseSearchDialog() {
        this(null, true);
    }

    /**
     * Creates new form DialogDateRange
     *
     * @param parent
     * @param modal
     */
    public BaseSearchDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
        getRootPane().setDefaultButton(jButtonOk);
        approved = false;
    }

    /**
     * Display the dialog.
     */
    public void showDialog() {
        setVisible(true);
    }

    /**
     * Close the dialog.
     *
     * @param isCancelled denotes if the user has cancelled the action or ok'd
     * the action
     */
    protected void hideDialog(boolean isCancelled) {
        approved = !isCancelled;
        setVisible(false);
    }

    /**
     * Subclass implementations should create and return an
     * {@link AbstractFinder} based on what the user entered in the dialog.
     *
     * NOTE: You must override this method or {@link #getResult()} will always
     * return {@code null}.
     *
     * @return {@code null}
     */
    protected AbstractFinder createFinder() {
        return null;
    }

    /**
     *
     * @return the {@link AbstractFinder} created by this dialog or null if the
     * dialog was canceled.
     */
    public AbstractFinder getResult() {
        return approved ? createFinder() : null;
    }

    /**
     * For Bean implementation.
     *
     * @return the {@link Container} where content can be placed by subclasses.
     */
    public Container getInnerContentPanel() {
        return jPanelContent;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonCancel = new javax.swing.JButton();
        jButtonOk = new javax.swing.JButton();
        jPanelContent = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Search");

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonOk.setText("Ok");
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });

        jPanelContent.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 178, Short.MAX_VALUE)
                        .addComponent(jButtonOk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonCancel))
                    .addComponent(jPanelContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanelContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancel)
                    .addComponent(jButtonOk))
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        hideDialog(false);
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        hideDialog(true);
    }//GEN-LAST:event_jButtonCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JPanel jPanelContent;
    // End of variables declaration//GEN-END:variables
}