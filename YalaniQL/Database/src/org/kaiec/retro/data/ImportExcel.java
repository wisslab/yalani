/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaiec.retro.data;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.api.progress.ProgressHandle;

public final class ImportExcel implements ActionListener {


    public void actionPerformed(ActionEvent e) {

          final String file = DirectoryManager.getInstance().chooseFile("Listendatei auswählen", (Component) e.getSource());
        if (file==null) {
            return;
        }



                final ProgressHandle progressHandle = ProgressHandle.createHandle("Import");

        new Thread(new Runnable() {

            @Override
            public void run() {
            ListImporter importer = new ListImporter(file,false,true);
            importer.setProgressHandle(progressHandle);
            progressHandle.start();
            importer.start();
            progressHandle.finish();
            RecordList.getInstance().datachanged();
            }
        }, "RVK Descs").start();


    }
}
