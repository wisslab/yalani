/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaiec.retro.listview;

import au.com.bytecode.opencsv.CSVWriter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JTable;
import org.kaiec.retro.data.CSVUtil;
import org.kaiec.retro.data.Record;
import org.openide.util.Exceptions;

public final class ExportFiltered implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        CSVWriter writer = CSVUtil.getCSVWriter();
        if (writer==null) return;
        try {
            JTable jTable1 = ListViewerTopComponent.findInstance().getTable();
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                int modelrow = jTable1.convertRowIndexToModel(i);
                Record rec = ((ListTableModel) jTable1.getModel()).getRecord(modelrow);
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
