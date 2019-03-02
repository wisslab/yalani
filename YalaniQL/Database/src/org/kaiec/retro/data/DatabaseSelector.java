/*
 * This file is part of Semtinel (http://www.semtinel.org).
 * Copyright (c) 2007-2010 Kai Eckert (http://www.kaiec.org).
 *
 * Semtinel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Semtinel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Semtinel.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

/*
 * DatabaseSelector.java
 *
 * Created on 19.02.2009, 11:04:55
 */

package org.kaiec.retro.data;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author kai
 */
public class DatabaseSelector extends javax.swing.JPanel {

    /** Creates new form DatabaseSelector */
    public DatabaseSelector() {
        initComponents();
		  statusLabel.setText("");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        newDatabaseLocation = new javax.swing.JTextField();
        locationBrowseButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();

        jLabel2.setText(org.openide.util.NbBundle.getMessage(DatabaseSelector.class, "DatabaseSelector.jLabel2.text")); // NOI18N

        newDatabaseLocation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newDatabaseLocationActionPerformed(evt);
            }
        });
        newDatabaseLocation.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                newDatabaseLocationFocusLost(evt);
            }
        });
        newDatabaseLocation.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                newDatabaseLocationPropertyChange(evt);
            }
        });
        newDatabaseLocation.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                newDatabaseLocationKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                newDatabaseLocationKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                newDatabaseLocationKeyTyped(evt);
            }
        });

        locationBrowseButton.setText(org.openide.util.NbBundle.getMessage(DatabaseSelector.class, "DatabaseSelector.locationBrowseButton.text")); // NOI18N
        locationBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationBrowseButtonActionPerformed(evt);
            }
        });

        statusLabel.setText(org.openide.util.NbBundle.getMessage(DatabaseSelector.class, "DatabaseSelector.statusLabel.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newDatabaseLocation, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(locationBrowseButton))
                    .addComponent(statusLabel))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(newDatabaseLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(locationBrowseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statusLabel)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	 private void locationBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locationBrowseButtonActionPerformed
		  String file = null;
          String dir = DirectoryManager.getInstance().getDirectory(getClass().getName());
		  JFileChooser chooser = new JFileChooser(dir);
		  chooser.setDialogTitle("Select Databse Location");
		  chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		  chooser.setFileFilter(new FileFilter() {

				@Override
				public boolean accept(File f) {
					 return f.isDirectory() || f.getName().endsWith(".data.db")|| f.getName().endsWith(".mv.db");
				}

				@Override
				public String getDescription() {
					 return "Directory or existing Semtinel DB File";
				}
		  });
		  int returnVal = chooser.showOpenDialog(this);
		  if(returnVal == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile().getAbsolutePath();
				if (chooser.getSelectedFile().isDirectory()) {
					 file += File.separatorChar + "semtineldb";
				} else if (chooser.getSelectedFile().getName().endsWith(".data.db")) {
					 file = file.substring(0,file.length()-".data.db".length());
				} else if (chooser.getSelectedFile().getName().endsWith(".mv.db")) {
					 file = file.substring(0,file.length()-".mv.db".length());
				}
                try {
                    DirectoryManager.getInstance().setDirectory(getClass().getName(), chooser.getCurrentDirectory().getCanonicalPath());
                } catch (IOException ioe){
                    throw new RuntimeException(ioe);
                }
          }
		  if (file!=null) newDatabaseLocation.setText(file);
		  updateStatus();
}//GEN-LAST:event_locationBrowseButtonActionPerformed

	 private void newDatabaseLocationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newDatabaseLocationKeyPressed
		 updateStatus();
	 }//GEN-LAST:event_newDatabaseLocationKeyPressed

	 private void newDatabaseLocationKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newDatabaseLocationKeyReleased
		  updateStatus();
	 }//GEN-LAST:event_newDatabaseLocationKeyReleased

	 private void newDatabaseLocationKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newDatabaseLocationKeyTyped
		  updateStatus();
	 }//GEN-LAST:event_newDatabaseLocationKeyTyped

	 private void newDatabaseLocationFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_newDatabaseLocationFocusLost
		  updateStatus();
	 }//GEN-LAST:event_newDatabaseLocationFocusLost

	 private void newDatabaseLocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newDatabaseLocationActionPerformed
		 updateStatus();
	 }//GEN-LAST:event_newDatabaseLocationActionPerformed

	 private void newDatabaseLocationPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_newDatabaseLocationPropertyChange
		  updateStatus();
	 }//GEN-LAST:event_newDatabaseLocationPropertyChange

	 private void updateStatus() {
		  String fileName = newDatabaseLocation.getText();
		  fileName+=".data.db";
		  String fileName2=fileName + ".mv.db";
		  File file = new File(fileName);
		  File file2 = new File(fileName2);
		  if (file.exists() || file2.exists()) {
				statusLabel.setText("Existing Database.");
		  }
		  else {
				statusLabel.setText("Database does not exist and will be created automatically.");
		  }
	 }

	 public String getDatabaseLocation() {
		  return newDatabaseLocation.getText();
	 }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton locationBrowseButton;
    private javax.swing.JTextField newDatabaseLocation;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables

}