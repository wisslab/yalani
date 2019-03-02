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

import au.com.bytecode.opencsv.CSVWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.hibernate.Query;
import org.openide.util.ChangeSupport;
import org.openide.util.Exceptions;

/**
 *
 * @author Kai Eckert (http://www.kaiec.org)
 */
public class RecordList {

    private List<Record> list;

    private boolean fireChanges = true;

    private Logger log = Logger.getLogger(getClass().getName());
        
    private RecordList() {
        EventLog.createEvent(EventLog.APPSTART);
    }

    public boolean isFireChanges() {
        return fireChanges;
    }

    public void setFireChanges(boolean fireChanges) {
        this.fireChanges = fireChanges;
    }




    private void fireChange() {
        if (isFireChanges()) cs.fireChange();
    }

    private ChangeSupport cs = new ChangeSupport(this);
    public void addChangeListener(ChangeListener cl) {
        cs.addChangeListener(cl);
    }
    public void removeChangeListener(ChangeListener cl) {
        cs.removeChangeListener(cl);
    }
    public void datachanged() {
        list = null;
        fireChange();
    }

    public List<Record> getRecords() {
        if (list!=null) return list;
        try {
            list = HibernateUtil.getInstance().getSession().createQuery("From Record").list();
            for (Record rec:list) {
                rec.addAssignmentChangeListener(new ChangeListener() {

                    public void stateChanged(ChangeEvent e) {
                        fireChange();
                    }
                });
            }
            return list;
        } catch (Throwable t) {
            log.severe("No records found in database. Error: " + t);
            return new ArrayList<Record>();
        }

    }
    
     public List<Record> getRecordByPPN(String ppn) {
         List<Record> res = new ArrayList<Record>();
         Query q = HibernateUtil.getInstance().getSession().createQuery("From Record where ppn=:ppn");
            q.setString("ppn", ppn);
            res.addAll(q.list());
            return res;
    }

     public Record getRecordByExternalId(String id) {
            Record rec = (Record) HibernateUtil.getInstance().getSession().createQuery("From Record where externalId=:id").setString("id", id).uniqueResult();
            return rec;
    }

    static RecordList instance = new RecordList();
    public static RecordList getInstance() {
        return instance;
    }

    public void saveBackup() {
                
        HibernateUtil.getInstance().commitTransaction();
        // return;
        // DEAKTIVIERT FUER DEMO

        String f = HibernateUtil.getInstance().getPreferences().getDatabaseLocation();
        File file = new File(f);
        f = file.getParent() + File.separator + "dump_" + new Date().getTime();
        System.out.println("Backup file: " + f);
        try {
            CSVWriter writer = new CSVWriter(new PrintWriter(f), ';');
            int i = 0;
            for (Record rec : RecordList.getInstance().getRecords()) {
                if (rec.isContext()) {
                    continue;
                }
                CSVUtil.writeRecord(rec, writer);
                i++;
            }
                writer.close();
        System.out.println("Records written: " + i);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
         
    }
}
