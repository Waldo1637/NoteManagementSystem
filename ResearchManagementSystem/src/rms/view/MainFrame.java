package rms.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import rms.control.Main;
import rms.control.search.AbstractFilter;
import rms.control.search.LateTaskFilter;
import rms.control.search.PendingTaskFilter;
import rms.control.search.TagFilter;
import rms.model.Tag;
import rms.model.item.*;
import rms.view.item.BaseItemPanel.ThreadCollapseAllHandler;
import rms.view.item.*;
import rms.view.search.*;
import rms.view.util.Prompts.PromptType;
import rms.view.util.*;

/**
 * The main UI window (singleton)
 *
 * @author Timothy
 */
public class MainFrame extends NotificationFrame {

    private static final Logger THIS_LOG = Logger.getLogger(MainFrame.class.getName());
    private static final Logger WORKER_LOG = Logger.getLogger("workers");

    // thread-safe lazy singleton pattern
    private static class InstanceHolder {

        static final MainFrame INSTANCE = new MainFrame();

        private InstanceHolder() {
        }
    }

    // thread-safe lazy singleton pattern
    public static MainFrame instance() {
        return InstanceHolder.INSTANCE;
    }

    private AbstractFilter cachedFilter = null;
    private JFileChooser cachedFileChooser = null;

    /**
     * Creates new form MainFrame
     */
    private MainFrame() {
        initComponents();
        initComponentsMore();
    }

    private final Action actionNewThreadBlank = new ButtonAction("New Thread (blank)", null, KeyEvent.VK_N) {
        @Override
        public void actionPerformed(ActionEvent evt) {
            createAndShowNewThread();
        }
    };

    private final Action actionNewThreadDuplicate = new ButtonAction("New Thread (duplicate)", null, KeyEvent.VK_D) {
        @Override
        public void actionPerformed(ActionEvent evt) {
            ItemThread selectedThread = getSelectedThread();
            if (selectedThread != null) {
                Prompts.Response result = Prompts.getUserResponse("Would you like to copy any files in the thread?"
                        + System.lineSeparator()
                        + "If 'no' they will be replaced with file placeholders.", PromptType.QUESTION);
                switch (result) {
                    default://print warning and fall through to CANCEL
                        THIS_LOG.log(Level.WARNING, "Undefined user response: {0}", result);
                    case CANCEL:
                        break;//do nothing
                    case YES:
                        promptToNameThread(Main.getState().createNewThread(selectedThread, false), true);
                        break;
                    case NO:
                        promptToNameThread(Main.getState().createNewThread(selectedThread, true), true);
                        break;
                }
            }
        }
    };

    private final Action actionShowAll = new ButtonAction("Show All", "Clear the current search filter", KeyEvent.VK_A) {
        @Override
        public void actionPerformed(ActionEvent evt) {
            refreshThreadListAndDisplay();
        }
    };

    private final Action actionSave = new ButtonAction("Save", null, KeyEvent.VK_S) {
        @Override
        public void actionPerformed(ActionEvent evt) {
            new WorkerSaveData().execute();
        }
    };

    private final Action actionFindAll = new ButtonAction("By Full Text", null, KeyEvent.VK_F) {
        @Override
        public void actionPerformed(ActionEvent evt) {
            useSearchDialog(new DialogSearchText(MainFrame.instance()));
        }
    };

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelEmptyThread = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButtonAddTag = new javax.swing.JButton();
        jPopupMenuTag = new javax.swing.JPopupMenu();
        jMenuItemRemoveTag = new javax.swing.JMenuItem();
        jSplitPaneHoriz = new javax.swing.JSplitPane();
        jScrollPaneThreadList = new javax.swing.JScrollPane();
        jListThreads = new javax.swing.JList<>();
        jPanelThreadInfo = new javax.swing.JPanel();
        jSplitPaneVert = new javax.swing.JSplitPane();
        jScrollPaneTags = new javax.swing.JScrollPane();
        jPanelTags = new javax.swing.JPanel();
        jPanelThreadContent = new javax.swing.JPanel();
        jScrollPaneContent = new javax.swing.JScrollPane();
        jXPanelContent = new org.jdesktop.swingx.JXPanel();
        jPanelHeader = new javax.swing.JPanel();
        jButtonDeleteThread = new javax.swing.JButton();
        jButtoNewFile = new javax.swing.JButton();
        jButtonNewTask = new javax.swing.JButton();
        jButtonNewNote = new javax.swing.JButton();
        jLabelThreadName = new javax.swing.JLabel();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuData = new javax.swing.JMenu();
        jMenuItemNewThreadBlank = new javax.swing.JMenuItem();
        jMenuItemNewThreadDuplicate = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItemManageTags = new javax.swing.JMenuItem();
        jMenuFind = new javax.swing.JMenu();
        jMenuItemShowAll = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItemFindTag = new javax.swing.JMenuItem();
        jMenuItemFindDate = new javax.swing.JMenuItem();
        jMenuItemFindText = new javax.swing.JMenuItem();
        jMenuItemFindItemNum = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemFindDeadline = new javax.swing.JMenuItem();
        jMenuItemFindPending = new javax.swing.JMenuItem();
        jMenuItemFindLateTasks = new javax.swing.JMenuItem();
        jMenuDatabase = new javax.swing.JMenu();
        jMenuItemSave = new javax.swing.JMenuItem();
        jMenuItemReload = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuItemAbout = new javax.swing.JMenuItem();

        jPanelEmptyThread.setPreferredSize(new java.awt.Dimension(345, 95));

        jLabel1.setText("This thread is currently empty.");

        jLabel2.setText("You may add items using the New menu or the buttons above.");

        jLabel3.setText("You can also use the Delete button to delete this thread.");

        javax.swing.GroupLayout jPanelEmptyThreadLayout = new javax.swing.GroupLayout(jPanelEmptyThread);
        jPanelEmptyThread.setLayout(jPanelEmptyThreadLayout);
        jPanelEmptyThreadLayout.setHorizontalGroup(
            jPanelEmptyThreadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEmptyThreadLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelEmptyThreadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelEmptyThreadLayout.setVerticalGroup(
            jPanelEmptyThreadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEmptyThreadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addContainerGap(52, Short.MAX_VALUE))
        );

        jButtonAddTag.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        jButtonAddTag.setText("+");
        jButtonAddTag.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(3, 5, 3, 5)));
        jButtonAddTag.setBorderPainted(false);
        jButtonAddTag.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonAddTag.setMargin(new java.awt.Insets(2, 3, 2, 3));
        jButtonAddTag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddTagActionPerformed(evt);
            }
        });

        jMenuItemRemoveTag.setText("Remove");
        jMenuItemRemoveTag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRemoveTagActionPerformed(evt);
            }
        });
        jPopupMenuTag.add(jMenuItemRemoveTag);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Research Management System");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(500, 300));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jSplitPaneHoriz.setDividerLocation(200);
        jSplitPaneHoriz.setResizeWeight(0.25);
        jSplitPaneHoriz.setMinimumSize(new java.awt.Dimension(100, 200));

        jListThreads.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListThreads.setMinimumSize(new java.awt.Dimension(100, 200));
        jListThreads.setName(""); // NOI18N
        jListThreads.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListThreadsValueChanged(evt);
            }
        });
        jScrollPaneThreadList.setViewportView(jListThreads);

        jSplitPaneHoriz.setLeftComponent(jScrollPaneThreadList);

        jSplitPaneVert.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPaneVert.setResizeWeight(0.9);

        jScrollPaneTags.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanelTags.setLayout(null);
        jScrollPaneTags.setViewportView(jPanelTags);

        jSplitPaneVert.setRightComponent(jScrollPaneTags);

        jPanelThreadContent.setLayout(new java.awt.BorderLayout());

        jXPanelContent.setScrollableHeightHint(org.jdesktop.swingx.ScrollableSizeHint.NONE);
        jXPanelContent.setScrollableWidthHint(org.jdesktop.swingx.ScrollableSizeHint.FIT);
        jXPanelContent.setLayout(new javax.swing.BoxLayout(jXPanelContent, javax.swing.BoxLayout.Y_AXIS));
        jScrollPaneContent.setViewportView(jXPanelContent);

        jPanelThreadContent.add(jScrollPaneContent, java.awt.BorderLayout.CENTER);

        jButtonDeleteThread.setText("Delete");
        jButtonDeleteThread.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteThreadActionPerformed(evt);
            }
        });

        jButtoNewFile.setText("New File");
        jButtoNewFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtoNewFileActionPerformed(evt);
            }
        });

        jButtonNewTask.setText("New Task");
        jButtonNewTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewTaskActionPerformed(evt);
            }
        });

        jButtonNewNote.setText("New Note");
        jButtonNewNote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewNoteActionPerformed(evt);
            }
        });

        jLabelThreadName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelThreadName.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelThreadName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelThreadNameMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelHeaderLayout = new javax.swing.GroupLayout(jPanelHeader);
        jPanelHeader.setLayout(jPanelHeaderLayout);
        jPanelHeaderLayout.setHorizontalGroup(
            jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelThreadName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 363, Short.MAX_VALUE)
                .addComponent(jButtonNewNote)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonNewTask)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtoNewFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDeleteThread))
        );
        jPanelHeaderLayout.setVerticalGroup(
            jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButtonDeleteThread)
                .addComponent(jButtoNewFile)
                .addComponent(jButtonNewTask)
                .addComponent(jButtonNewNote)
                .addComponent(jLabelThreadName))
        );

        jPanelThreadContent.add(jPanelHeader, java.awt.BorderLayout.NORTH);

        jSplitPaneVert.setLeftComponent(jPanelThreadContent);

        javax.swing.GroupLayout jPanelThreadInfoLayout = new javax.swing.GroupLayout(jPanelThreadInfo);
        jPanelThreadInfo.setLayout(jPanelThreadInfoLayout);
        jPanelThreadInfoLayout.setHorizontalGroup(
            jPanelThreadInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPaneVert)
        );
        jPanelThreadInfoLayout.setVerticalGroup(
            jPanelThreadInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPaneVert, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
        );

        jSplitPaneHoriz.setRightComponent(jPanelThreadInfo);

        jMenuData.setText("Data");

        jMenuItemNewThreadBlank.setText("New Thread (blank)");
        jMenuItemNewThreadBlank.setToolTipText("");
        jMenuData.add(jMenuItemNewThreadBlank);

        jMenuItemNewThreadDuplicate.setText("New Thread (duplicate)");
        jMenuItemNewThreadDuplicate.setToolTipText("");
        jMenuData.add(jMenuItemNewThreadDuplicate);
        jMenuData.add(jSeparator4);

        jMenuItemManageTags.setText("Manage Tags");
        jMenuItemManageTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemManageTagsActionPerformed(evt);
            }
        });
        jMenuData.add(jMenuItemManageTags);

        jMenuBar.add(jMenuData);

        jMenuFind.setText("Find");

        jMenuItemShowAll.setText("Show All");
        jMenuFind.add(jMenuItemShowAll);
        jMenuFind.add(jSeparator2);

        jMenuItemFindTag.setMnemonic('T');
        jMenuItemFindTag.setText("By Tag");
        jMenuItemFindTag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFindTagActionPerformed(evt);
            }
        });
        jMenuFind.add(jMenuItemFindTag);

        jMenuItemFindDate.setText("By Date Range");
        jMenuItemFindDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFindDateActionPerformed(evt);
            }
        });
        jMenuFind.add(jMenuItemFindDate);

        jMenuItemFindText.setText("By Full Text");
        jMenuFind.add(jMenuItemFindText);

        jMenuItemFindItemNum.setMnemonic('N');
        jMenuItemFindItemNum.setText("By Item Number");
        jMenuItemFindItemNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFindItemNumActionPerformed(evt);
            }
        });
        jMenuFind.add(jMenuItemFindItemNum);
        jMenuFind.add(jSeparator1);

        jMenuItemFindDeadline.setText("Upcoming Deadlines");
        jMenuItemFindDeadline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFindDeadlineActionPerformed(evt);
            }
        });
        jMenuFind.add(jMenuItemFindDeadline);

        jMenuItemFindPending.setMnemonic('P');
        jMenuItemFindPending.setText("Pending Tasks");
        jMenuItemFindPending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFindPendingActionPerformed(evt);
            }
        });
        jMenuFind.add(jMenuItemFindPending);

        jMenuItemFindLateTasks.setMnemonic('O');
        jMenuItemFindLateTasks.setText("Overdue Tasks");
        jMenuItemFindLateTasks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFindLateTasksActionPerformed(evt);
            }
        });
        jMenuFind.add(jMenuItemFindLateTasks);

        jMenuBar.add(jMenuFind);

        jMenuDatabase.setText("Database");

        jMenuItemSave.setText("Save");
        jMenuDatabase.add(jMenuItemSave);

        jMenuItemReload.setText("Reload");
        jMenuItemReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReloadActionPerformed(evt);
            }
        });
        jMenuDatabase.add(jMenuItemReload);

        jMenuBar.add(jMenuDatabase);

        jMenuHelp.setText("Help");

        jMenuItemAbout.setText("About");
        jMenuItemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAboutActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemAbout);

        jMenuBar.add(jMenuHelp);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getInnerContentPanel());
        getInnerContentPanel().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPaneHoriz, javax.swing.GroupLayout.DEFAULT_SIZE, 930, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPaneHoriz, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsMore() {
        jPanelTags.setLayout(new WrapLayout(WrapLayout.LEFT, 1, 1));
        //set the loading panel as the glass pane
        setGlassPane(new LoadingPanel());

        //Map actions to menu items and congfigure global hotkeys
        configureMenuItemAndGlobalShortcut(jMenuItemNewThreadBlank, actionNewThreadBlank);
        configureMenuItemAndGlobalShortcut(jMenuItemNewThreadDuplicate, actionNewThreadDuplicate);
        configureMenuItemAndGlobalShortcut(jMenuItemSave, actionSave);
        configureMenuItemAndGlobalShortcut(jMenuItemFindText, actionFindAll);
        configureMenuItemAndGlobalShortcut(jMenuItemShowAll, actionShowAll);
        //must override for the thread list because it seems to capture ctrl+A already (but does nothing)
        jListThreads.getInputMap(JComponent.WHEN_FOCUSED).put(jMenuItemShowAll.getAccelerator(), actionShowAll.getValue(Action.NAME));
        //Select a better index for the mnemonic underline
        jMenuItemNewThreadDuplicate.setDisplayedMnemonicIndex(12);
    }

    private static void configureMenuItemAndGlobalShortcut(JMenuItem menuItem, Action action) {
        //Configure the MenuItem based on the Action
        menuItem.setAction(action);
        //After configuring, Text and Accelerator can be retrieved to setup global shortcut 
        String actionName = menuItem.getText();
        menuItem.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(menuItem.getAccelerator(), actionName);
        menuItem.getActionMap().put(actionName, action);
    }

    private void jButtonAddTagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddTagActionPerformed
        TagSelectionDialog dialog = new TagSelectionDialog(this);
        dialog.showDialog();
        Set<Tag> selectedTags = dialog.getResult();
        if (selectedTags != null) {
            getSelectedThread().addTags(selectedTags);
            new WorkerDisplayThreadTags(getSelectedThread()).execute();
        }
    }//GEN-LAST:event_jButtonAddTagActionPerformed

    private void jMenuItemRemoveTagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRemoveTagActionPerformed
        try {
            JMenuItem source = (JMenuItem) evt.getSource();
            JPopupMenu parent = (JPopupMenu) source.getParent();
            TagButton invoker = (TagButton) parent.getInvoker();
            getSelectedThread().removeTags(Collections.singleton(invoker.tag));
            new WorkerDisplayThreadTags(getSelectedThread()).execute();
        } catch (ClassCastException ignored) {
        }
    }//GEN-LAST:event_jMenuItemRemoveTagActionPerformed

    private void jMenuItemReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReloadActionPerformed
        String message = "Reloading will cuase you to lose any data added since the last time you saved.\nAre you sure you want to proceed?";
        if (Prompts.getUserApproval(message, PromptType.WARNING)) {
            loadStateAndPopulate();
        }
    }//GEN-LAST:event_jMenuItemReloadActionPerformed

    private void jMenuItemFindTagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemFindTagActionPerformed
        useSearchDialog(new DialogSearchTags(this));
    }//GEN-LAST:event_jMenuItemFindTagActionPerformed

    private void jMenuItemFindDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemFindDateActionPerformed
        useSearchDialog(new DialogDateRange(this));
    }//GEN-LAST:event_jMenuItemFindDateActionPerformed

    private void jMenuItemFindDeadlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemFindDeadlineActionPerformed
        useSearchDialog(new DialogDeadline(this));
    }//GEN-LAST:event_jMenuItemFindDeadlineActionPerformed

    private void jMenuItemFindItemNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemFindItemNumActionPerformed
        useSearchDialog(new DialogItemNumber(this));
    }//GEN-LAST:event_jMenuItemFindItemNumActionPerformed

    private void jMenuItemFindPendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemFindPendingActionPerformed
        refreshThreadListAndDisplay(PendingTaskFilter.get(), null);
    }//GEN-LAST:event_jMenuItemFindPendingActionPerformed

    private void jMenuItemFindLateTasksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemFindLateTasksActionPerformed
        refreshThreadListAndDisplay(LateTaskFilter.get(), null);
    }//GEN-LAST:event_jMenuItemFindLateTasksActionPerformed

    private void jMenuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAboutActionPerformed
        Package pack = getClass().getPackage();
        String title = pack.getSpecificationTitle();
        String author = pack.getSpecificationVendor();
        String version = pack.getSpecificationVersion();
        String date = pack.getImplementationVersion();

        version = version != null ? version.replace("_", ".") : version;
        String msg = String.format("%s%nby %s%n%nversion: %s%n%s%n", title, author, version, date);

        JOptionPane.showMessageDialog(this, msg, "About", JOptionPane.PLAIN_MESSAGE);
    }//GEN-LAST:event_jMenuItemAboutActionPerformed

    private void jListThreadsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListThreadsValueChanged
        if (!evt.getValueIsAdjusting()) {
            refreshSelectedThread();
        }
    }//GEN-LAST:event_jListThreadsValueChanged

    private void jButtonDeleteThreadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteThreadActionPerformed
        promptDelete();
    }//GEN-LAST:event_jButtonDeleteThreadActionPerformed

    private void jButtoNewFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtoNewFileActionPerformed
        Main.newFile();
    }//GEN-LAST:event_jButtoNewFileActionPerformed

    private void jButtonNewTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewTaskActionPerformed
        Main.newTask();
    }//GEN-LAST:event_jButtonNewTaskActionPerformed

    private void jButtonNewNoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewNoteActionPerformed
        Main.newNote();
    }//GEN-LAST:event_jButtonNewNoteActionPerformed

    private void jLabelThreadNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelThreadNameMouseClicked
        promptToNameThread(getSelectedThread(), false);
    }//GEN-LAST:event_jLabelThreadNameMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        //Cannot use 'actionSave' because I need reference to the worker in
        //  order to wait for the save to complete (and log any exceptions).
        WorkerSaveData worker = new WorkerSaveData();
        worker.execute();
        setVisible(false);
        try {
            System.out.println("Saving and exiting...");
            THIS_LOG.log(Level.FINE, "Saving and exiting.");
            worker.get();
        } catch (InterruptedException | ExecutionException ex) {
            THIS_LOG.log(Level.SEVERE, "Exception while saving", ex);
        }
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItemManageTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemManageTagsActionPerformed
        // Create new TagManagementDialog and show it
        new TagManagementDialog(instance()).showDialog();
        // Update the tags for the selected thread in case any were deleted
        new WorkerDisplayThreadTags(getSelectedThread()).execute();
    }//GEN-LAST:event_jMenuItemManageTagsActionPerformed

    public List<File> promptFileSelection(boolean allowMultiple) {
        JFileChooser chooser = this.cachedFileChooser;
        if (chooser == null) {
            this.cachedFileChooser = chooser = new JFileChooser();
        }
        chooser.setMultiSelectionEnabled(allowMultiple);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (allowMultiple) {
                File[] ret = chooser.getSelectedFiles();
                reset(chooser);
                return Collections.unmodifiableList(Arrays.asList(ret));
            } else {
                File ret = chooser.getSelectedFile();
                reset(chooser);
                return Collections.singletonList(ret);
            }
        } else {
            return Collections.emptyList();
        }
    }

    private static void reset(JFileChooser chooser) {
        final File f = new File("");
        chooser.setSelectedFile(f);//using 'null' is not effective
        File[] clear = {f};
        chooser.setSelectedFiles(clear);//using 'null'/empty is not effective
    }

    private void promptToNameThread(ItemThread sel, boolean isNewThread) {
        String prompt = isNewThread
                ? "Enter a name for the new thread"
                : "Enter new name for the thread (or cancel to keep name)";

        String resp = Prompts.getUserInput(prompt, PromptType.PLAIN);
        if (resp != null) {
            sel.setName(resp);
            refreshThreadListAndDisplay(null, sel);
        }
    }

    private void useSearchDialog(BaseSearchDialog dialog) {
        dialog.showDialog();
        AbstractFilter filter = dialog.getResult();
        if (filter != null) {
            refreshThreadListAndDisplay(filter, null);
        }
    }

    private void promptDelete() {
        String message = "Deleting this thread cannot be undone.\nAre you sure you want to proceed?";
        if (Prompts.getUserApproval(message, PromptType.WARNING)) {
            Main.deleteThread();
        }
    }

    private void enableThreadButtons(boolean b) {
        jButtonDeleteThread.setEnabled(b);
    }

    private BaseItemPanel createPanelForItem(Item i) {
        boolean collapse = cachedFilter == null ? false : !cachedFilter.accept(i);
        try {
            if (i instanceof NoteItem) {
                return new NoteItemPanel((NoteItem) i, collapse);
            } else if (i instanceof TaskItem) {
                return new TaskItemPanel((TaskItem) i, collapse);
            } else if (i instanceof FileItem) {
                return new FileItemNormalPanel((FileItem) i, collapse);
            } else if (i instanceof EmptyFileItem) {
                return new FileItemEmptyPanel((EmptyFileItem) i, collapse);
            }
            throw new UnsupportedOperationException("Not implemented: " + i);
        } catch (Exception ex) {
            THIS_LOG.log(Level.SEVERE, "Error creating panel for Item " + i, ex);
            throw ex;
        }
    }

    /**
     * JButton representing a {@link Tag}.
     */
    private class TagButton extends JButton {

        public final Tag tag;

        public TagButton(final Tag tag) {
            this.tag = tag;
            init();
        }

        private void init() {
            this.setBorderPainted(false);
            this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5), BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
            this.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            this.setText(tag.toString());
            this.setMinimumSize(new Dimension(40, 40));
            this.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    refreshThreadListAndDisplay(TagFilter.get(Collections.singleton(tag)), null);
                }
            });
            this.addMouseListener(tagPopupClickListener);
        }
    }

    /**
     * MouseAdapter for displaying the popup menu on tags
     */
    private final MouseAdapter tagPopupClickListener = new MouseAdapter() {

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

        private void showPopup(MouseEvent e) {
            jPopupMenuTag.show(e.getComponent(), e.getX(), e.getY());
        }
    };

    /**
     * Loads the given ItemThread into the GUI
     *
     * @param toLoad
     */
    private void displayThread(ItemThread toLoad) {
        if (toLoad != null) {
            //set name and enable buttons
            jLabelThreadName.setText(toLoad.getName());
            enableThreadButtons(true);

            new WorkerDisplayThreadItems(toLoad).execute();
            new WorkerDisplayThreadTags(toLoad).execute();
        } else {
            //clear existing content
            jXPanelContent.removeAll();
            jPanelTags.removeAll();
            //clear name and disable buttons
            jLabelThreadName.setText("");
            enableThreadButtons(false);
        }
        revalidate();
        repaint();
    }

    private int numLoaders = 0;

    private synchronized void showLoader() {
        numLoaders++;
        getGlassPane().setVisible(true);
    }

    private synchronized void hideLoader() {
        numLoaders--;
        if (numLoaders <= 0) {
            getGlassPane().setVisible(false);
        }
    }

    //<editor-fold defaultstate="collapsed" desc=" Workers ">
    private abstract class UIBlockingWorker extends SwingWorker<Void, Void> {

        @Override
        protected void done() {
            this.updateUI();
            hideLoader();
            WORKER_LOG.log(Level.FINE, "Completed {0}", this.getClass().getName());
        }

        @Override
        protected Void doInBackground() {
            WORKER_LOG.log(Level.FINE, "Starting {0}", this.getClass().getName());
            showLoader();
            try {
                this.doWork();
            } catch (Exception ex) {
                WORKER_LOG.log(Level.SEVERE, "Exception caught by " + this.getClass().getName(), ex);
            }
            return null;
        }

        protected abstract void updateUI();

        protected abstract void doWork() throws Exception;
    }

    /**
     * Task of loading State from file and populating the GUI with information
     */
    private class WorkerLoadData extends UIBlockingWorker {

        private boolean result;

        @Override
        protected void updateUI() {
            if (result) {
                refreshThreadListAndDisplay();
                displayNotification("Data loaded successfully");
            } else {
                Prompts.informUser("Error!", "Unrecoverable error: unable to load data.\nSee log file for more information.", PromptType.ERROR);
                System.exit(1);
            }
        }

        @Override
        protected void doWork() {
            result = Main.loadStateFromFile();
        }
    }

    /**
     * Task of saving data from State to file
     */
    private class WorkerSaveData extends UIBlockingWorker {

        private boolean result;

        @Override
        protected void updateUI() {
            if (result) {
                displayNotification("Data saved successfully");
            } else {
                Prompts.informUser("Error!", "Unable to save data.\nSee log file for more information.", PromptType.ERROR);
            }
        }

        @Override
        protected void doWork() {
            EditableTextField.ensureModificationsAreStored();
            result = Main.storeStateToFile();
        }
    }

    /**
     * Task of refreshing the list of threads in the GUI
     */
    private class WorkerRefreshThreadList extends UIBlockingWorker {

        private final AbstractFilter filter;
        private final ItemThread toDisplay;

        public WorkerRefreshThreadList(AbstractFilter filter, ItemThread toDisplay) {
            this.filter = filter;
            this.toDisplay = toDisplay;
        }

        @Override
        protected void updateUI() {
            clearDisplayedThread();
            setSelectedThread(toDisplay);
        }

        @Override
        protected void doWork() {
            cachedFilter = filter;
            jListThreads.setModel(new SearchSortItemThreadListModel(true, filter));
        }
    }

    /**
     * Loads the items in the given thread to the GUI
     */
    private class WorkerDisplayThreadItems extends UIBlockingWorker {

        private final ItemThread toLoad;

        public WorkerDisplayThreadItems(ItemThread toLoad) {
            this.toLoad = toLoad;
        }

        @Override
        protected void updateUI() {
            //relayout the UI
            jPanelThreadContent.updateUI();

            //update the scroll bar position
            JScrollBar sb = jScrollPaneContent.getVerticalScrollBar();
            sb.setValue(sb.getMaximum());
        }

        @Override
        protected void doWork() {
            //clear existing content
            jXPanelContent.removeAll();
            //load items
            if (toLoad.size() == 0) {
                jXPanelContent.add(jPanelEmptyThread);
            } else {
                ThreadCollapseAllHandler collapseHdlr = new ThreadCollapseAllHandler();
                for (Item i : toLoad) {
                    BaseItemPanel itemPanel = createPanelForItem(i);
                    collapseHdlr.register(itemPanel);
//                    rms.util.Helpers.addOutsideColorBorder(itemPanel, java.awt.Color.RED);
                    jXPanelContent.add(itemPanel);
                }
//                rms.util.Helpers.addOutsideColorBorder(jXPanelContent, java.awt.Color.BLUE);
            }
        }
    }

    /**
     * Loads the {@link Tag}s for the given thread to the GUI
     */
    private class WorkerDisplayThreadTags extends UIBlockingWorker {

        private final ItemThread toLoad;

        public WorkerDisplayThreadTags(ItemThread toLoad) {
            this.toLoad = toLoad;
        }

        @Override
        protected void updateUI() {
            //relayout the UI
            jScrollPaneTags.updateUI();
        }

        @Override
        protected void doWork() {
            //clear existing content
            jPanelTags.removeAll();
            //load tags
            for (Tag t : toLoad.getTagsUnmodifible()) {
                jPanelTags.add(new TagButton(t));
            }
            jPanelTags.add(jButtonAddTag);
        }
    }
    //</editor-fold>

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtoNewFile;
    private javax.swing.JButton jButtonAddTag;
    private javax.swing.JButton jButtonDeleteThread;
    private javax.swing.JButton jButtonNewNote;
    private javax.swing.JButton jButtonNewTask;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelThreadName;
    private javax.swing.JList<ItemThread> jListThreads;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuData;
    private javax.swing.JMenu jMenuDatabase;
    private javax.swing.JMenu jMenuFind;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuItemAbout;
    private javax.swing.JMenuItem jMenuItemFindDate;
    private javax.swing.JMenuItem jMenuItemFindDeadline;
    private javax.swing.JMenuItem jMenuItemFindItemNum;
    private javax.swing.JMenuItem jMenuItemFindLateTasks;
    private javax.swing.JMenuItem jMenuItemFindPending;
    private javax.swing.JMenuItem jMenuItemFindTag;
    private javax.swing.JMenuItem jMenuItemFindText;
    private javax.swing.JMenuItem jMenuItemManageTags;
    private javax.swing.JMenuItem jMenuItemNewThreadBlank;
    private javax.swing.JMenuItem jMenuItemNewThreadDuplicate;
    private javax.swing.JMenuItem jMenuItemReload;
    private javax.swing.JMenuItem jMenuItemRemoveTag;
    private javax.swing.JMenuItem jMenuItemSave;
    private javax.swing.JMenuItem jMenuItemShowAll;
    private javax.swing.JPanel jPanelEmptyThread;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelTags;
    private javax.swing.JPanel jPanelThreadContent;
    private javax.swing.JPanel jPanelThreadInfo;
    private javax.swing.JPopupMenu jPopupMenuTag;
    private javax.swing.JScrollPane jScrollPaneContent;
    private javax.swing.JScrollPane jScrollPaneTags;
    private javax.swing.JScrollPane jScrollPaneThreadList;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JSplitPane jSplitPaneHoriz;
    private javax.swing.JSplitPane jSplitPaneVert;
    private org.jdesktop.swingx.JXPanel jXPanelContent;
    // End of variables declaration//GEN-END:variables

    /**
     * Creates a new {@link ItemThread} and displays it in the UI
     *
     * @return
     */
    public ItemThread createAndShowNewThread() {
        ItemThread thread = Main.getState().createNewThread();
        promptToNameThread(thread, true);
        return thread;
    }

    /**
     *
     * @return the {@link ItemThread} currently selected in the thread list
     */
    public ItemThread getSelectedThread() {
        return (ItemThread) jListThreads.getSelectedValue();
    }

    /**
     * Select the given {@link ItemThread} in the thread list
     *
     * @param thread
     */
    public void setSelectedThread(ItemThread thread) {
        jListThreads.setSelectedValue(thread, true);
    }

    /**
     * Clear the selected thread in the thread list
     */
    public void clearSelectedThread() {
        jListThreads.clearSelection();
    }

    /**
     * Clear the thread currently
     */
    public void clearDisplayedThread() {
        displayThread(null);
    }

    /**
     * Reload the display for the currently selected thread
     */
    public void refreshSelectedThread() {
        displayThread(getSelectedThread());
    }

    /**
     * Refresh the thread list, showing all item threads, without selecting one.
     */
    public void refreshThreadListAndDisplay() {
        refreshThreadListAndDisplay(null, null);
    }

    /**
     * Refresh the thread list, showing only threads that match the given filter
     * and selecting the item given
     *
     * @param filter    the thread filter to apply
     * @param toDisplay the item to select after refreshing (may be null)
     */
    public void refreshThreadListAndDisplay(AbstractFilter filter, ItemThread toDisplay) {
        new WorkerRefreshThreadList(filter, toDisplay).execute();
    }

    /**
     * Load the state from file and display it in the GUI
     */
    public void loadStateAndPopulate() {
        new WorkerLoadData().execute();
    }
}
