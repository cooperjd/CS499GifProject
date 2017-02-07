/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ButtonPanel.java
 *
 * Created on Apr 26, 2011, 7:14:33 PM
 */
package gif;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import database.Database;

/**
 *
 * @author User
 */
public class ButtonPanel extends javax.swing.JPanel {

    private Animator animator;

    /** Creates new form ButtonPanel */
    public ButtonPanel(Animator animator) {
        this.animator = animator;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        open = new javax.swing.JButton();
        save = new javax.swing.JButton();

        open.setText("Open...");
        open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openActionPerformed(evt);
            }
        });

        save.setText("Save As...");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(open)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 222, Short.MAX_VALUE)
                .addComponent(save)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(save)
                    .addComponent(open))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void openActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openActionPerformed
        JFileChooser fc = new JFileChooser();
        int o = fc.showOpenDialog(getParent());
        if (o == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                if (file.getName().endsWith("gif")) {
                    List<GifFrame> frames = Gif.read(file);
                    for (GifFrame frame : frames) {
                        animator.addGifFrame(frame);
                    }
                } else {
                    BufferedImage image = ImageIO.read(file);
                    animator.addGifFrame(new GifFrame(image, 500));
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex, "Exception",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
}//GEN-LAST:event_openActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("*.gif"));
        fc.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".gif");
            }

            @Override
            public String getDescription() {
                return "CompuServe GIF (*.gif)";
            }
        });
        int o = fc.showSaveDialog(getParent());
        if (o == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                Gif.write(animator.getGifFrames(), animator.loop(), file);
                Database db = new Database();
                db.setPath(file.getAbsolutePath(), animator.username);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex, "Exception",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
}//GEN-LAST:event_saveActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton open;
    private javax.swing.JButton save;
    // End of variables declaration//GEN-END:variables
}
