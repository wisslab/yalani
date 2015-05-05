/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaiec.retro.data;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;

public final class SelectDatabase implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        final DatabasePanel panel = new DatabasePanel();
        DialogDescriptor dd = new DialogDescriptor(
                    panel,
                    "Database",
                    true,
                    DialogDescriptor.PLAIN_MESSAGE,
                    null,
                    null
                );

        // if the database location has changed and the user did not set the new location, display a dialog
        dd.setButtonListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("OK") && panel.isLocationChanged()) {
                      int res = JOptionPane.showConfirmDialog(panel, "Set new Location ?", "Please confirm", JOptionPane.YES_NO_OPTION);
                    if (res==JOptionPane.YES_OPTION) {
                        panel.setNewDatabaseLocation();
                    }
                }
            }
        });

        DialogDisplayer.getDefault().notify(dd);
    }
}
