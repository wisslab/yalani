/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaiec.retro.data;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class ImportContextListAction implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        String file = DirectoryManager.getInstance().chooseFile("Listendatei auswählen", (Component) e.getSource());
        if (file!=null) {
            ListImporter importer = new ListImporter(file, true);
            importer.start();
            RecordList.getInstance().datachanged();
        }
    }
}
