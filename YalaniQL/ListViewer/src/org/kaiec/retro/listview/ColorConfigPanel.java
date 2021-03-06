/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ColorConfigPanel.java
 *
 * Created on 09.12.2010, 10:24:09
 */

package org.kaiec.retro.listview;

import org.kaiec.retro.data.CloseListener;
import org.kaiec.retro.data.CloseEvent;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import org.kaiec.retro.data.PrefSerializer;
import org.kaiec.retro.data.RecordList;

/**
 *
 * @author kai
 */
public class ColorConfigPanel extends javax.swing.JPanel {
    List<ColorPattern> colors = new ArrayList<>();
    /** Creates new form ColorConfigPanel */
    public ColorConfigPanel() {
        initComponents();
        jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS));
        load();
        CloseEvent.getInstance().addCloseListener(new CloseListener() {

            public void closing() {
                save();
            }
        });
    }

    public void save() {
        PrefSerializer<ColorPattern> ps = new PrefSerializer<>();
        ps.saveList("Colors", colors);
    }

    public void load() {
        PrefSerializer<ColorPattern> ps = new PrefSerializer<>();
        colors.clear();
        colors.addAll( ps.loadList("Colors"));
        createColorPanel();
    }

     private void createColorPanel() {
        jPanel1.removeAll();
        for (ColorPattern color:colors) {
            jPanel1.add(new SingleColorPanel(this, color));
        }
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    public void removeColor(ColorPattern color) {
        colors.remove(color);
        createColorPanel();
    }

    public void up(ColorPattern color) {
        int pos = colors.indexOf(color);
        if (pos==0) return;
        colors.remove(color);
        colors.add(pos-1,color);
        createColorPanel();
    }

    public void down(ColorPattern color) {
        int pos = colors.indexOf(color);
        if (pos==colors.size()-1) return;
        colors.remove(color);
        colors.add(pos+1,color);
        createColorPanel();
    }

    public Color getColor(String rvk, Color def) {
        for (ColorPattern c:colors) {
            if (c.getText()==null) continue;
            if (rvk.toLowerCase().startsWith(c.getText().toLowerCase())) return c.getColor();
        }
        return def;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/kaiec/retro/listview/add.png"))); // NOI18N
        jButton1.setText(org.openide.util.NbBundle.getMessage(ColorConfigPanel.class, "ColorConfigPanel.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1, java.awt.BorderLayout.PAGE_START);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/kaiec/retro/listview/ok.png"))); // NOI18N
        jButton2.setText(org.openide.util.NbBundle.getMessage(ColorConfigPanel.class, "ColorConfigPanel.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2, java.awt.BorderLayout.PAGE_END);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 413, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 258, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel1);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        colors.add(new ColorPattern());
        createColorPanel();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        RecordList.getInstance().datachanged();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
