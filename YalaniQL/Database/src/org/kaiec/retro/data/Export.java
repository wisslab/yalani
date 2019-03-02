/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaiec.retro.data;

import au.com.bytecode.opencsv.CSVWriter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.openide.util.Exceptions;

public final class Export implements ActionListener {

    public void actionPerformed(ActionEvent e) {
       CSVWriter writer = CSVUtil.getCSVWriter();
        try {
            for (Record rec : RecordList.getInstance().getRecords()) {
                if (rec.isContext()) {
                    continue;
                }
                CSVUtil.writeRecord(rec, writer);
            }
            writer.close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    
}
