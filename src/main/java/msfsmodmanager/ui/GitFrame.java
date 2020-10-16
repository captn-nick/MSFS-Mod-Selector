package msfsmodmanager.ui;

import groovy.lang.Closure;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import msfsmodmanager.util.SwingUtil;

public class GitFrame extends javax.swing.JDialog {
    boolean error;
    private String out;
    
    private Closure okButtonAction;

    /**
     * Creates new form GitFrame
     */
    public GitFrame(boolean error, String out, Closure okButtonAction) {
        super((JFrame)null, true);
        this.error = error;
        this.out = out;
        this.okButtonAction = okButtonAction;
        
        initComponents();
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(okButton);
        okButton.requestFocus();
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                closeWindow();
            }
        });
    }
    
    private void closeWindow() {
        SwingUtil.closeWindow(this);
        okButtonAction.call();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        label = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setTitle(error ? "Git update: Error" : "Git update");
        setPreferredSize(new java.awt.Dimension(885, 487));

        textArea.setColumns(20);
        textArea.setRows(5);
        textArea.setText(out);
        scrollPane.setViewportView(textArea);

        getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);

        label.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        label.setForeground(error ? new Color(255, 0, 0) : new Color(0, 0, 0));
        label.setText(error ? "Encountered the following error during a Git pull:" : "Git pull successful.");
        label.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        getContentPane().add(label, java.awt.BorderLayout.NORTH);

        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(okButton);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        closeWindow();
    }//GEN-LAST:event_okButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void show(boolean error, String out, Closure okButtonAction) {
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
            java.util.logging.Logger.getLogger(GitFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GitFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GitFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GitFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GitFrame(error, out, okButtonAction).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JLabel label;
    private javax.swing.JButton okButton;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextArea textArea;
    // End of variables declaration//GEN-END:variables
}
