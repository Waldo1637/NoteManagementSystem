package rms.view.item;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.Timer;
import javax.swing.UIDefaults;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import rms.control.search.ItemNumberFilter;
import rms.model.item.TextItem;
import rms.util.TextConversion;
import rms.view.MainFrame;
import rms.view.util.CustomHTMLEditorKit;
import rms.view.util.CustomStyledEditorKit;
import rms.view.util.Prompts;
import rms.view.util.UndoRedoProvider;

/**
 *
 * @author Timothy
 */
public final class EditableTextField extends javax.swing.JPanel {

    private static final Logger LOG = Logger.getLogger(EditableTextField.class.getName());
    //
    private static final boolean DEBUG_CONVERSION = false;
    //
    private static final String SAVE = "Save";
    private static final String EDIT = "Edit";
    //
    private static final Pattern ITEM_REF_PATT = Pattern.compile("^#(\\d+)$");
    //
    private final JButton saveEditButton;

    /**
     * Should contain the HTML form of the text. The exception is the initial
     * value with may be 'null' (visually the same as empty) or non-HTML (but it
     * will be converted as soon as the field is edited).
     */
    private String lastSavedText;

    /**
     * No-arg constructor for Bean creation. NOTE: Do not use.
     */
    public EditableTextField() {
        saveEditButton = null;
        lastSavedText = null;
        initComponents();
    }

    /**
     * Creates new form EditableTextField
     *
     * @param text
     * @param saveEditButton
     */
    public EditableTextField(String text, JButton saveEditButton) {
        if (saveEditButton == null) {
            throw new IllegalArgumentException();
        }
        this.lastSavedText = text;
        this.saveEditButton = saveEditButton;
        initComponents();
        //Initialize text pane content
        saveAction(true);
        //Add button listener
        saveEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                final String txt = EditableTextField.this.saveEditButton.getText();
                switch (txt) {
                    case SAVE:
                        saveAction(false);
                        break;
                    case EDIT:
                        editAction();
                        break;
                    default:
                        throw new IllegalStateException("Invalid button text: " + txt);
                }
            }
        });
        jTextPaneDesc.addHyperlinkListener(new HyperlinkListener() {
            private String previousTooltip;

            @Override
            public void hyperlinkUpdate(HyperlinkEvent evt) {
                if (evt.getSource() == jTextPaneDesc) {
                    HyperlinkEvent.EventType evtType = evt.getEventType();
                    if (HyperlinkEvent.EventType.ACTIVATED == evtType) {
                        followLink(evt.getURL(), evt.getDescription());
                    } else if (HyperlinkEvent.EventType.ENTERED == evtType) {
                        Element elem = evt.getSourceElement();
                        if (elem != null) {
                            AttributeSet a = (AttributeSet) elem.getAttributes().getAttribute(HTML.Tag.A);
                            if (a != null) {
                                previousTooltip = jTextPaneDesc.getToolTipText();//backup
                                jTextPaneDesc.setToolTipText("Go to " + a.getAttribute(HTML.Attribute.TITLE));
                            }
                        }
                    } else if (HyperlinkEvent.EventType.EXITED == evtType) {
                        jTextPaneDesc.setToolTipText(previousTooltip);//restore
                    }
                } else {
                    LOG.log(Level.WARNING, "Unexpected hyperlink source: {0}", evt.getSource());
                }
            }

            private void followLink(URL url, String description) {
                if (url == null) {
                    Matcher matcher = ITEM_REF_PATT.matcher(description);
                    if (matcher.matches()) {
                        int id = Integer.parseInt(matcher.group(1));//parse will not fail because only digits matched
                        MainFrame.instance().refreshThreadListAndDisplay(ItemNumberFilter.get(id), null);
                        return;//nothing more to do
                    } else {
                        try {
                            //Just try to create a URL from the 'description'
                            url = new URL("http", description, "");
                        } catch (MalformedURLException ex) {
                            LOG.log(Level.INFO, "Unable to create URL from " + description, ex);
                            Prompts.informUser("Error!", "Unable to open URL:\n" + description, Prompts.PromptType.WARNING);
                        }
                    }
                }
                if (url != null) {
                    final URL url_final = url;
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Desktop.getDesktop().browse(url_final.toURI());
                            } catch (Exception ex) {
                                LOG.log(Level.INFO, "Unable to open URL " + url_final, ex);
                                Prompts.informUser("Error!", "Unable to open URL:\n" + url_final, Prompts.PromptType.WARNING);
                            }
                        }
                    });
                }
            }
        });
        UndoRedoProvider.addTo(jTextPaneDesc);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setPreferredSize(new java.awt.Dimension(6, 22));

        jTextPaneDesc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextPaneDescFocusLost(evt);
            }
        });
        jTextPaneDesc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextPaneDescKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextPaneDesc)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextPaneDesc)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextPaneDescFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextPaneDescFocusLost
        if (evt.getOppositeComponent() != saveEditButton) {//already handled in that case
            saveAction(false);
        }
    }//GEN-LAST:event_jTextPaneDescFocusLost

    private void jTextPaneDescKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextPaneDescKeyTyped
        preserveSize();
    }//GEN-LAST:event_jTextPaneDescKeyTyped

    private void editAction() {
        //Convert the 'jTextPaneDesc' content to non-HTML form for editing
        String converted = getAndConvertContent(true);
        //Create a new non-HTML EditorKit and set the converted text
        jTextPaneDesc.setEditorKit(new CustomStyledEditorKit(36));
        jTextPaneDesc.setText(converted);
        //Make the content area editable
        makeEditable(true);
        //Try requesting focus every 5 ms until successful to ensure that
        //  the focusLost(..) event can be reliably captured.
        final Timer focusTimer = new Timer(5, null);
        focusTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jTextPaneDesc.requestFocusInWindow()) {
                    focusTimer.stop();
                }
            }
        });
        focusTimer.start();
    }

    private void saveAction(boolean initial) {
        if (jTextPaneDesc.isEditable()) {//skip if pane was not in edit mode
            //Convert the 'jTextPaneDesc' content to HTML form for storage and to
            //  allow links to be clicked (unless this call is for initialization)
            final String converted = initial
                    ? this.lastSavedText
                    : getAndConvertContent(false);
            if (DEBUG_CONVERSION) {
                System.out.printf("[saveAction] converted=|%s| %n",
                        toStringDebug(converted, false));
            }
            //Create a new HTML EditorKit and set the converted text
            jTextPaneDesc.setEditorKit(addStyles(new CustomHTMLEditorKit(36)));
            jTextPaneDesc.setText(converted);
            //Make the content area non-editable
            makeEditable(false);

            //If the converted content has changed from the last time saveAction()
            //  was called, then fire an ItemTextUpdatedEvent.
            if (!Objects.equals(this.lastSavedText, converted)) {
                if (DEBUG_CONVERSION) {
                    System.out.printf("[saveAction] old=|%s| %n",
                            toStringDebug(this.lastSavedText, false));
                }
                this.lastSavedText = converted;
                fireItemTextUpdatedEvent(converted);
            }
        }
    }

    /**
     * If {@code jTextPaneDesc.getStyledDocument()} is {@link HTMLDocument},
     * convert the {@code <body>} content to plain text with links, otherwise
     * convert to HTML form.
     *
     * @param expectingHTML
     *
     * @return
     *
     * @throws IllegalStateException if {@code expectingHTML==true} and the
     *                               document is not an {@link HTMLDocument}
     */
    private String getAndConvertContent(boolean expectingHTML) {
        StyledDocument doc = jTextPaneDesc.getStyledDocument();
        String retVal = TextConversion.convertDocumentContent(doc, expectingHTML);
        if (DEBUG_CONVERSION) {
            System.out.printf("[getAndConvertContent] in=|%s| out=|%s| %n",
                    jTextPaneDesc.getText(), retVal);
        }
        return retVal;
    }

    private static HTMLEditorKit addStyles(HTMLEditorKit kit) {
        StyleSheet sty = kit.getStyleSheet();
        sty.addRule("p {margin:0; padding:0; }");//no space between <p>
        return kit;
    }

    private static String toStringDebug(String s, boolean asCharArray) {
        if (DEBUG_CONVERSION) {
            if (s == null) {
                return null;
            } else if (asCharArray) {
                char[] chars = s.toCharArray();
                int iMax = chars.length - 1;
                if (iMax == -1) {
                    return "[]";
                }

                StringBuilder b = new StringBuilder();
                b.append('[');
                for (int i = 0;; i++) {
                    char c = chars[i];
                    if (32 < c && c < 127) {
                        b.append(c);
                    } else {
                        b.append("(").append((int) c).append(")");
                    }
                    if (i == iMax) {
                        return b.append(']').toString();
                    }
                    b.append(" ");
                }
            }
        }
        return s;
    }

    private void makeEditable(boolean editable) {
        jTextPaneDesc.setEditable(editable);
        if (editable) {
            changeDescPaneColor(Color.WHITE);
            saveEditButton.setText(SAVE);
        } else {
            changeDescPaneColor(Color.LIGHT_GRAY);
            saveEditButton.setText(EDIT);
        }
        preserveSize();
        //NOTE: When converting the HTML to editable form, there is an extra
        //  line added to the end and sometimes attempting to type in that line
        //  causes strange text placement (ex: it appears on the next line 
        //  which isn't even visible). Furthermore, in non-edit mode, this will
        //  prevent the window from automatically scrolling down to the bottom
        //  of the text pane when the content is long.
        jTextPaneDesc.setCaretPosition(0);
    }

    private void changeDescPaneColor(Color c) {
        UIDefaults defaults = new UIDefaults();
        defaults.put("TextPane[Enabled].backgroundPainter", c);
        jTextPaneDesc.putClientProperty("Nimbus.Overrides", defaults);
        jTextPaneDesc.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
        jTextPaneDesc.setBackground(c);
    }

    /**
     * There is a bug that causes the {@link javax.swing.JTextPane} to be
     * redrawn at default size unless
     * {@link javax.swing.JTextPane#getPreferredSize()} is called immediately
     * after actions that cause a repaint (such as setting the text).
     */
    private void preserveSize() {
        jTextPaneDesc.getPreferredSize();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JTextPane jTextPaneDesc = new javax.swing.JTextPane();
    // End of variables declaration//GEN-END:variables

    /**
     * Event fired when the text field of the {@link TextItem} has been updated
     * to reflect the data entered in the UI by the user.
     *
     * @author Timothy
     */
    public static class ItemTextUpdatedEvent extends EventObject {

        private final String newText;

        public ItemTextUpdatedEvent(Object source, String newText) {
            super(source);
            this.newText = newText;
        }

        public String getNewText() {
            return newText;
        }
    }

    /**
     * Listener interface for ItemTextUpdatedEvent events
     */
    public static interface ItemTextUpdateListener {

        public void textUpdated(ItemTextUpdatedEvent event);
    }

    private final ArrayList<ItemTextUpdateListener> listeners = new ArrayList<>();

    public void addItemTextUpdateListener(ItemTextUpdateListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    public void removeItemTextUpdateListener(ItemTextUpdateListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    private void fireItemTextUpdatedEvent(String newText) {
        ItemTextUpdatedEvent event = new ItemTextUpdatedEvent(this, newText);
        synchronized (listeners) {
            for (ItemTextUpdateListener l : listeners) {
                l.textUpdated(event);
            }
        }
    }

    /**
     * This method should be called before storing the State to file to ensure
     * that any modifications in the current text field are stored to the State.
     */
    public static void ensureModificationsAreStored() {
        //NOTE: In most scenarios, it would be sufficient for the MainFrame to
        //  request focus or use KeyboardFocusManager.clearGlobalFocusOwner(). 
        //  However, doing that after the window closing action has fired
        //  doesn't seem to have any effect. Instead, we determine if some 
        //  instance of the 'jTextPaneDesc' owns the focus and directly call
        //  the saveAction() method on the parent.
        Component o = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        if (o != null) {
            Container parent = o.getParent();
            if (parent instanceof EditableTextField) {
                ((EditableTextField) parent).saveAction(false);
            }
        }
    }
}
