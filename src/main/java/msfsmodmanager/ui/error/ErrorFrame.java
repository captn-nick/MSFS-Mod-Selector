package msfsmodmanager.ui.error;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JFrame;

import msfsmodmanager.Main;
import msfsmodmanager.logic.ErrorHandler;
import msfsmodmanager.util.Browser;
import msfsmodmanager.util.SwingUtil;
import msfsmodmanager.logic.ModDeleter;
import msfsmodmanager.state.ModsDb;
import static msfsmodmanager.logic.ErrorHandler.ErrorType;


public class ErrorFrame extends javax.swing.JDialog {
    private static final Font LINK_FONT;

    private String message;
    private String details;
    private String stackTrace;
    private ErrorType errorType;
    private boolean forceShutdown;
    
    private final ErrorFrameHandler handler = new ErrorFrameHandler(this);
    
    static {
        Map<TextAttribute, Integer> fontAttributes = new HashMap<TextAttribute, Integer>();
        fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        LINK_FONT = new java.awt.Font("Tahoma", 1, 11).deriveFont(fontAttributes);
    }

    /**
     * Creates new form ErrorFrame
     */
    public ErrorFrame(String title, String message, String details, String stackTrace, ErrorType errorType, boolean forceShutdown) {
        super((JFrame)null, true);
        setTitle("MSFS ModSelector: " + title);
        this.message = message;
        this.details = details;
        this.stackTrace = stackTrace;
        this.errorType = errorType;
        this.forceShutdown = forceShutdown;
        
        initComponents();
        SwingUtil.initPanel(this, quitButton);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                if (forceShutdown) {
                    System.exit(1);
                }
                else {
                    SwingUtil.closeWindow(ErrorFrame.this);
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        stacktracePanel = new javax.swing.JPanel();
        if (stackTrace == null) {
            stacktracePanel.setVisible(false);
        }
        stacktraceInfoPanel = new javax.swing.JPanel();
        stacktraceInfoLabel1 = new javax.swing.JLabel();
        stacktraceInfoLabel2 = new javax.swing.JLabel();
        stacktraceInfoLabel3 = new javax.swing.JLabel();
        stacktraceInfoLabel4 = new javax.swing.JLabel();
        stacktraceInfoLabel5 = new javax.swing.JLabel();
        stacktraceScrollPane = new javax.swing.JScrollPane();
        stacktraceTextArea = new javax.swing.JTextArea();
        topPanel = new javax.swing.JPanel();
        messageLabel = new javax.swing.JLabel();
        detailsScrollPane = new javax.swing.JScrollPane();
        bottomPanel = new javax.swing.JPanel();
        duplicatesDeleteButton = new javax.swing.JButton();
        updateModDbButton = new javax.swing.JButton();
        autoAddModsButton = new javax.swing.JButton();
        quitButton = new javax.swing.JButton();

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(723, 587));
        getContentPane().setLayout(new java.awt.BorderLayout(0, 4));

        stacktracePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
        stacktracePanel.setLayout(new java.awt.BorderLayout());

        stacktraceInfoPanel.setPreferredSize(new java.awt.Dimension(681, 30));
        stacktraceInfoPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        stacktraceInfoLabel1.setText("If you believe this is a bug, please report it on ");
        stacktraceInfoPanel.add(stacktraceInfoLabel1);

        stacktraceInfoLabel2.setFont(LINK_FONT);
        stacktraceInfoLabel2.setForeground(new java.awt.Color(0, 0, 255));
        stacktraceInfoLabel2.setText("the project's GitHub page");
        stacktraceInfoLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        stacktraceInfoLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                stacktraceInfoLabel2MouseReleased(evt);
            }
        });
        stacktraceInfoPanel.add(stacktraceInfoLabel2);

        stacktraceInfoLabel3.setText(" with the following information. Make sure to read the ");
        stacktraceInfoPanel.add(stacktraceInfoLabel3);

        stacktraceInfoLabel4.setFont(LINK_FONT);
        stacktraceInfoLabel4.setForeground(new java.awt.Color(0, 0, 255));
        stacktraceInfoLabel4.setText("README");
        stacktraceInfoLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        stacktraceInfoLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                stacktraceInfoLabel4MouseReleased(evt);
            }
        });
        stacktraceInfoPanel.add(stacktraceInfoLabel4);

        stacktraceInfoLabel5.setText(" first.");
        stacktraceInfoPanel.add(stacktraceInfoLabel5);

        stacktracePanel.add(stacktraceInfoPanel, java.awt.BorderLayout.NORTH);

        stacktraceTextArea.setColumns(20);
        stacktraceTextArea.setRows(5);
        stacktraceTextArea.setText(stackTrace);
        stacktraceScrollPane.setViewportView(stacktraceTextArea);

        stacktracePanel.add(stacktraceScrollPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(stacktracePanel, java.awt.BorderLayout.CENTER);

        topPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
        topPanel.setLayout(new java.awt.BorderLayout(0, 2));

        messageLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        messageLabel.setText(message);
        topPanel.add(messageLabel, java.awt.BorderLayout.NORTH);

        detailsTextArea.setColumns(20);
        detailsTextArea.setRows(5);
        detailsTextArea.setText(details);
        detailsScrollPane.setViewportView(detailsTextArea);

        topPanel.add(detailsScrollPane, java.awt.BorderLayout.CENTER);
        detailsScrollPane.setVisible(details != null);

        getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);
        if (stackTrace == null) {
            getContentPane().remove(topPanel);
            getContentPane().add(topPanel, java.awt.BorderLayout.CENTER);
        }

        bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        duplicatesDeleteButton.setText("DELETE the duplicates in the Temp folder NOW");
        duplicatesDeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                duplicatesDeleteButtonActionPerformed(evt);
            }
        });
        bottomPanel.add(duplicatesDeleteButton);
        if (errorType != ErrorType.DUPLICATE_MODS) {
            duplicatesDeleteButton.setVisible(false);
        }

        updateModDbButton.setText("Search for new mod definitions online (Update mod DB)");
        updateModDbButton.setToolTipText(ModsDb.instance.isUpdatable() ? null : "Can be updated every 10 minutes only.");
        updateModDbButton.setEnabled(ModsDb.instance.isUpdatable());
        updateModDbButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateModDbButtonActionPerformed(evt);
            }
        });
        bottomPanel.add(updateModDbButton);
        if (errorType != ErrorType.UNREGISTERED_MODS) {
            updateModDbButton.setVisible(false);
        }

        autoAddModsButton.setText("Auto-add these mod definitions found online");
        autoAddModsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoAddModsButtonActionPerformed(evt);
            }
        });
        bottomPanel.add(autoAddModsButton);
        if (errorType != ErrorType.UNREGISTERED_MODS_IN_DB) {
            autoAddModsButton.setVisible(false);
        }

        quitButton.setText(forceShutdown ? "Quit" : "Close");
        quitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitButtonActionPerformed(evt);
            }
        });
        bottomPanel.add(quitButton);

        getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void quitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitButtonActionPerformed
        if (forceShutdown) {
            System.exit(1);
        }
        else {
            SwingUtil.closeWindow(this);
        }
    }//GEN-LAST:event_quitButtonActionPerformed

    private void stacktraceInfoLabel4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stacktraceInfoLabel4MouseReleased
        Browser.openWebpage("https://github.com/captn-nick/MSFS-Mod-Selector");
    }//GEN-LAST:event_stacktraceInfoLabel4MouseReleased

    private void stacktraceInfoLabel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stacktraceInfoLabel2MouseReleased
        Browser.openWebpage("https://github.com/captn-nick/MSFS-Mod-Selector/issues");
    }//GEN-LAST:event_stacktraceInfoLabel2MouseReleased

    private void duplicatesDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_duplicatesDeleteButtonActionPerformed
        ModDeleter.deleteInTempDirectory(Arrays.asList( details.split("\n")));
        SwingUtil.closeWindow(this);
        Main.restart();
    }//GEN-LAST:event_duplicatesDeleteButtonActionPerformed

    private void autoAddModsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoAddModsButtonActionPerformed
        handler.autoAddMods();
    }//GEN-LAST:event_autoAddModsButtonActionPerformed

    private void updateModDbButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateModDbButtonActionPerformed
        handler.updateModsDb();
    }//GEN-LAST:event_updateModDbButtonActionPerformed

    public static void show(String title, String message, String details, String stackTrace, ErrorType errorType, boolean forceShutdown) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ErrorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ErrorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ErrorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ErrorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ErrorFrame(title, message, details, stackTrace, errorType, forceShutdown).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton autoAddModsButton;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JScrollPane detailsScrollPane;
    public final javax.swing.JTextArea detailsTextArea = new javax.swing.JTextArea();
    private javax.swing.JButton duplicatesDeleteButton;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JButton quitButton;
    private javax.swing.JLabel stacktraceInfoLabel1;
    private javax.swing.JLabel stacktraceInfoLabel2;
    private javax.swing.JLabel stacktraceInfoLabel3;
    private javax.swing.JLabel stacktraceInfoLabel4;
    private javax.swing.JLabel stacktraceInfoLabel5;
    private javax.swing.JPanel stacktraceInfoPanel;
    private javax.swing.JPanel stacktracePanel;
    private javax.swing.JScrollPane stacktraceScrollPane;
    private javax.swing.JTextArea stacktraceTextArea;
    private javax.swing.JPanel topPanel;
    private javax.swing.JButton updateModDbButton;
    // End of variables declaration//GEN-END:variables
}
