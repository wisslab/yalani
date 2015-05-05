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
package org.kaiec.retro.data;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.netbeans.api.progress.ProgressHandle;

/**
 *
 * @author Kai Eckert (http://www.kaiec.org)
 */
public class ListImporter {

    private String csvfile;
    private String delim2 = Character.toString((char) 31);
    // @result = ($id,$lang,$author,$title,$year,$edition,$pub,$sws,$rvks,$isbn,$contributor);
    // autor, rvk, sw und contributor sind mit ";" getrennt
    public static final int PPN = 0;             // Keine ID mehr!!!
    public static final int LANGUAGE = 1;
    public static final int AUTHOR = 2;
    public static final int TITLE = 3;
    public static final int YEAR = 4;
    public static final int EDITION = 5;
    public static final int PUBLISHER = 6;
    public static final int SUBJECT = 7;
    public static final int CLASSIFICATION = 8;
    public static final int ISBN = 9;
    public static final int CONTRIBUTOR = 10;
    public static final int TITLE2 = 11;   // Nr. 304, Einheitssachtitel, wird ignoriert
    public static final int BNB = 12;      // ID in BNB, ignoriert
    public static final int LOCS = 13;   // LoC Subject Headings, evtl. zu Schlagworten dazu
    public static final int SERIES = 14;  // Reihentitel
    public static final int SIGNATURE = 15;
    public static final int BARCODE = 16;  // WIrd als interne ID benutzt
    public static final int ASSIGNED = 17;
    public static final int UPDATED = 18;
    private ISBNTool isbntool = new ISBNTool();
    private Pattern isbnPattern = Pattern.compile("[0-9X-]{10,}");
    private boolean context = false;
    private boolean excel = false;
    private ProgressHandle progressHandle;
    private boolean old = false;

    public ListImporter(String csvfile) {

        this.csvfile = csvfile;
    }

    public ListImporter(String csvfile, boolean context) {
        this.context = context;
        this.csvfile = csvfile;
    }

    public ListImporter(String csvfile, boolean context, boolean excel) {
        this.context = context;
        this.csvfile = csvfile;
        this.excel = excel;
    }

    public ListImporter(String csvfile, boolean context, boolean excel, boolean old) {
        this.context = context;
        this.csvfile = csvfile;
        this.excel = excel;
        this.old = old;
    }

    public ProgressHandle getProgressHandle() {
        return progressHandle;
    }

    public void setProgressHandle(ProgressHandle progressHandle) {
        this.progressHandle = progressHandle;
    }

    public void start() {
        if (old) {
            readOldFile();
        } else {
            readFile();
        }
    }

    private void readFile() {
        try {
            System.out.println("Converting: " + csvfile);
            char delim = '\t';
            char quote = CSVWriter.NO_QUOTE_CHARACTER;
            if (excel) {
                delim = ';';
                quote = '"';
            }
            CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csvfile), Charset.forName("UTF-8")), delim, quote, false);
            String[] nextLine;
            // writer.setProperty("showXmlDeclaration","true");
            // writer.setProperty("tab","8");
            // writer.setProperty("relativeURIs","same-document,relative");


            int count = 0;

            HibernateUtil hibernateUtil = HibernateUtil.getInstance();
            // hibernateUtil.getSession().clear();
            // hibernateUtil.getSession().createQuery("delete from Record").executeUpdate();

            RecordList.getInstance().setFireChanges(false);
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                System.out.println(nextLine[0] + "/" + nextLine[1] + " etc... " + nextLine.length);
                String id = nextLine[BARCODE];
                String ppn = nextLine[PPN];
                String author = nextLine[AUTHOR].trim();
                String title = nextLine[TITLE].trim();
                String year = nextLine[YEAR].trim();
                String publisher = nextLine[PUBLISHER].trim();
                String edition = nextLine[EDITION].trim();
                String language = nextLine[LANGUAGE];
                String subjects = nextLine[SUBJECT].trim();
                String classes = nextLine[CLASSIFICATION].trim();
                String isbn = nextLine[ISBN].trim();
                String contributors = nextLine[CONTRIBUTOR].trim();
                String signature = nextLine[SIGNATURE].trim();
                String series = nextLine[SERIES].trim();
                String assigned = nextLine[ASSIGNED].trim();
                String updated = null;
                if (nextLine.length >= 19) {
                    updated = nextLine[UPDATED].trim();
                }

                if (excel && assigned.isEmpty()) {
                    continue;
                }

                Record record;
                if (excel) {
                    if (id == null || id.trim().isEmpty()) {
                        // Fallback für alte Daten...
                        List<Record> res = RecordList.getInstance().getRecordByPPN(ppn);
                        for (Record rec : res) {
                            rec.setAssignment(assigned, true);
                            if (updated != null && !updated.isEmpty()) {
                                rec.setUpdated(new Date(Long.parseLong(updated)));
                            } else {
                                rec.setUpdated(null);
                            }

                        }
                        continue;
                    }
                    record = RecordList.getInstance().getRecordByExternalId(id);
                    if (record == null) {
                        continue;
                    }
                } else {

                    record = new Record();
                }
                if (!excel) {
                    record.setPpn(ppn);
                    record.setExternalId(id);
                    record.setTitle(title);
                    record.setClasses(classes);
                    record.setYear(year);
                    record.setSubjects(subjects);
                    record.setSignature(signature);
                    record.setIsbn(isbn);
                    record.setCreators(author + delim2 + contributors);
                    record.setContext(context);
                    record.setEdition(edition);
                    record.setLanguage(language);
                    record.setSeries(series);
                }
                record.setAssignment(assigned, true);
                if (updated != null && !updated.isEmpty()) {
                    record.setUpdated(new Date(Long.parseLong(updated)));
                } else {
                    record.setUpdated(null);
                }


                try {
                    hibernateUtil.getSession().saveOrUpdate(record);
                } catch (Exception e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "" + record.getExternalId());
                    // Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Database Error!", e);
                }
                ++count;
                if (progressHandle != null) {
                    progressHandle.progress(count + ": " + record.getTitle());
                }
            }
            System.out.println();
            System.out.println();
            System.out.println("Finished!");
            reader.close();
            RecordList.getInstance().setFireChanges(true);
            RecordList.getInstance().datachanged();
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }



    }

    private void readOldFile() {
        try {
            System.out.println("Reading old file: " + csvfile);
            char delim = '\t';
            char quote = CSVWriter.NO_QUOTE_CHARACTER;
            if (excel) {
                delim = ';';
                quote = '"';
            }
            CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csvfile), Charset.forName("UTF-8")), delim, quote, false);
            String[] nextLine;
            // writer.setProperty("showXmlDeclaration","true");
            // writer.setProperty("tab","8");
            // writer.setProperty("relativeURIs","same-document,relative");


            int count = 0;

            HibernateUtil hibernateUtil = HibernateUtil.getInstance();
            // hibernateUtil.getSession().clear();
            // hibernateUtil.getSession().createQuery("delete from Record").executeUpdate();

            RecordList.getInstance().setFireChanges(false);
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                System.out.println(nextLine[0] + "/" + nextLine[1] + " etc... " + nextLine.length);
                String ppn = nextLine[0];
                String assigned = nextLine[nextLine.length - 2].trim();
                String updated = null;
                updated = nextLine[nextLine.length - 1].trim();

                if (assigned.isEmpty()) {
                    continue;
                }

                Record record;
                if (excel) {
                    List<Record> res = RecordList.getInstance().getRecordByPPN(ppn);
                    for (Record rec : res) {
                        rec.setAssignment(assigned, true);
                        if (updated != null && !updated.isEmpty()) {
                            rec.setUpdated(new Date(Long.parseLong(updated)));
                        } else {
                            rec.setUpdated(null);
                        }

                    }
                    continue;
                }


                ++count;
                if (progressHandle != null) {
                    progressHandle.progress(count);
                }
            }
            System.out.println();
            System.out.println();
            System.out.println("Finished!");
            reader.close();
            RecordList.getInstance().setFireChanges(true);
            RecordList.getInstance().datachanged();
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }



    }
}
