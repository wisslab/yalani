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

package org.kaiec.retro.listview;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import org.kaiec.retro.data.HibernateUtil;
import org.kaiec.retro.data.ImportRVKDescs;
import org.kaiec.retro.data.Record;
import org.kaiec.retro.data.RecordList;

/**
 *
 * @author Kai Eckert (http://www.kaiec.org)
 */


public class ListTableModel extends DefaultTableModel {
    public final static int COL_AUTHOR = 0;
    public final static int COL_TITLE = 1;
    public final static int COL_SERIES = 2;
    public final static int COL_YEAR = 3;
    public final static int COL_EDITION = 4;
    public final static int COL_LANGUAGE = 5;
    public final static int COL_KEYWORDS = 6;
    public final static int COL_SIG = 7;
    public final static int COL_CUSTOM1 = 8;
    public final static int COL_CUSTOM2 = 9;
    public final static int COL_CUSTOM3 = 10;
    public final static int COL_CUSTOM4 = 11;
    public final static int COL_CUSTOM5 = 12;
    public final static int COL_CLASSES = 13;
    public final static int COL_EDIT = 14;
    public final static int COL_UPDATED = 15;
   
    private List<Record> records = new ArrayList<Record>();

    public ListTableModel(final RecordList list) {
        this.records = list.getRecords();
        list.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                records = list.getRecords();
                fireTableDataChanged();
            }
        });
	
	
        fireTableStructureChanged();
        fireTableDataChanged();

    }
    
    

    public List<Record> getRecords() {
        return records;
    }



    public void setRecords(List<Record> records) {
        this.records = records;
	this.nameCache.clear();
        fireTableDataChanged();
    }


    public Record getRecord(int row) {
        return records.get(row);
    }


    @Override
    public int getColumnCount() {
        return 16;
    }
    
    private HashMap<String, Integer> nameCache = new HashMap<>();
    
    public int getColumnIndexByName(String name) {
	    if (nameCache.containsKey(name)) {
		    return nameCache.get(name);
	    }
	    for (int i=0;i<this.getColumnCount();i++) {
		    if (getColumnName(i).equals(name)) {
			    nameCache.put(name, i);
			    return i;
		    }
	    }
	    return -1;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case COL_AUTHOR: return java.util.ResourceBundle.getBundle("org/kaiec/retro/listview/Bundle").getString("list-author");
            case COL_TITLE: return java.util.ResourceBundle.getBundle("org/kaiec/retro/listview/Bundle").getString("list-title");
            case COL_SERIES: return java.util.ResourceBundle.getBundle("org/kaiec/retro/listview/Bundle").getString("list-series");
            case COL_YEAR: return java.util.ResourceBundle.getBundle("org/kaiec/retro/listview/Bundle").getString("list-year");
            case COL_KEYWORDS: return java.util.ResourceBundle.getBundle("org/kaiec/retro/listview/Bundle").getString("list-subjects");
            case COL_EDITION: return java.util.ResourceBundle.getBundle("org/kaiec/retro/listview/Bundle").getString("list-edition");
            case COL_LANGUAGE: return java.util.ResourceBundle.getBundle("org/kaiec/retro/listview/Bundle").getString("list-language");
            case COL_SIG: return java.util.ResourceBundle.getBundle("org/kaiec/retro/listview/Bundle").getString("list-callnumber");
            case COL_CUSTOM1: return HibernateUtil.getInstance().getPreferences().getCustom1();
            case COL_CUSTOM2: return HibernateUtil.getInstance().getPreferences().getCustom2();
            case COL_CUSTOM3: return HibernateUtil.getInstance().getPreferences().getCustom3();
            case COL_CUSTOM4: return HibernateUtil.getInstance().getPreferences().getCustom4();
            case COL_CUSTOM5: return HibernateUtil.getInstance().getPreferences().getCustom5();
            case COL_CLASSES: return java.util.ResourceBundle.getBundle("org/kaiec/retro/listview/Bundle").getString("list-classes");
            case COL_EDIT: return java.util.ResourceBundle.getBundle("org/kaiec/retro/listview/Bundle").getString("list-new-label");
            case COL_UPDATED: return java.util.ResourceBundle.getBundle("org/kaiec/retro/listview/Bundle").getString("list-last-modified");
            default: return "???";
        }
    }

    @Override
    public int getRowCount() {
        if (records==null) return 0;
        return records.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        Record rec = records.get(row);
        switch (column) {
            case COL_AUTHOR: return rec.getCreators();
            case COL_TITLE: return rec.getTitle();
            case COL_SERIES: return rec.getSeries();
            case COL_YEAR: return rec.getYear();
            case COL_KEYWORDS: return rec.getSubjects();
            case COL_EDITION: return rec.getEdition();
            case COL_LANGUAGE: return rec.getLanguage();
            case COL_SIG: return rec.getSignature();
            case COL_CUSTOM1: return rec.getCustom1();
            case COL_CUSTOM2: return rec.getCustom2();
            case COL_CUSTOM3: return rec.getCustom3();
            case COL_CUSTOM4: return rec.getCustom4();
            case COL_CUSTOM5: return rec.getCustom5();
            case COL_CLASSES: return rec.getClasses();
            // Deaktiviert...
            case -1: {
                String classes = rec.getClasses();
                StringTokenizer st = new StringTokenizer(classes, ";");
                StringBuilder sb = new StringBuilder();
                while (st.hasMoreTokens()) {
                    String next = st.nextToken();
                    sb.append(next);
                    String desc = ImportRVKDescs.getDescription(next);
                    if (desc!=null) {
                        sb.append(": ").append(desc);
                    }
                    sb.append("\n");
                }
                return sb.toString();
            }
            case COL_EDIT: return rec.getAssignment();
            case COL_UPDATED: {
//                if (rec.getUpdated()==null) return "";
//                return DateFormat.getDateTimeInstance().format(rec.getUpdated());
                return rec.getUpdated();
            }
            default: return "Möp";
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // return (column==COL_EDIT || column==COL_CLASSES || column==COL_TITLE) && (!records.get(row).isContext());
        if (records.get(row).isContext()) return false;
	if (column==COL_EDIT || column==COL_CLASSES) return true;
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (column==COL_EDIT) {
            records.get(row).setAssignment(aValue.toString());
            fireTableRowsUpdated(row, row);
        }
    }

    

  

    

}
