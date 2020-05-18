/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kaiec.retro.data;

import au.com.bytecode.opencsv.CSVWriter;
import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;
import org.kaiec.retro.data.DirectoryManager;
import org.kaiec.retro.data.ListImporter;
import org.kaiec.retro.data.Record;

/**
 *
 * @author kai
 */
public class CSVUtil {


    public static void writeRecord(Record rec, CSVWriter writer) {
        String[] strings = new String[24];
        strings[ListImporter.ASSIGNED] = rec.getAssignment();
        strings[ListImporter.PPN] = rec.getPpn();
        strings[ListImporter.BARCODE] = rec.getExternalId();
        strings[ListImporter.TITLE] = rec.getTitle();
        strings[ListImporter.TITLE2] = "";
        strings[ListImporter.YEAR] = rec.getYear();
        strings[ListImporter.PUBLISHER] = "";
        strings[ListImporter.EDITION] = rec.getEdition();
        strings[ListImporter.LANGUAGE] = "";
        strings[ListImporter.SUBJECT] = rec.getSubjects();
        strings[ListImporter.CLASSIFICATION] = rec.getClasses();
        strings[ListImporter.ISBN] = rec.getIsbn();
        strings[ListImporter.CONTRIBUTOR] = "";
        strings[ListImporter.SIGNATURE] = rec.getSignature();
        strings[ListImporter.AUTHOR] = rec.getCreators();
        strings[ListImporter.SERIES] = rec.getSeries();
        strings[ListImporter.CUSTOM1] = rec.getCustom1();
        strings[ListImporter.CUSTOM2] = rec.getCustom2();
        strings[ListImporter.CUSTOM3] = rec.getCustom3();
        strings[ListImporter.CUSTOM4] = rec.getCustom4();
        strings[ListImporter.CUSTOM5] = rec.getCustom5();
        strings[ListImporter.BNB] = "";
        strings[ListImporter.LOCS] = "";
        if (rec.getUpdated() != null) {
            strings[ListImporter.UPDATED] = Long.toString(rec.getUpdated().getTime());
        } else {
            strings[ListImporter.UPDATED] = "";
        }
        writer.writeNext(strings);

    }

    public static CSVWriter getCSVWriter() {
        String file = DirectoryManager.getInstance().chooseFile("Ziel auswählen", null, "CSVFile", ".csv");
        if (file != null) {
            File f = new File(file);
            if (f.exists()) {
                int response = JOptionPane.showConfirmDialog(null, "Überschreiben?");
                if (response != JOptionPane.YES_OPTION) {
                    return null;
                }
            }
            CSVWriter writer;
            try {
                writer = new CSVWriter(new PrintWriter(f), ';');
                return writer;
            } catch (FileNotFoundException fnfe) {
                JOptionPane.showMessageDialog(null, "Die Datei nicht schreibbar.");
                return null;
            }
        }
        return null;
    }

}
