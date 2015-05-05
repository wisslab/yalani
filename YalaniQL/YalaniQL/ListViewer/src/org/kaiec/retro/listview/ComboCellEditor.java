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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Kai Eckert (http://www.kaiec.org)
 */
public class ComboCellEditor extends AbstractCellEditor implements TableCellEditor {

    JPanel panel = new JPanel();
    JComboBox comboBox = new JComboBox();
    private List<String> history = new ArrayList<String>();


    public ComboCellEditor(ComboTableCellRenderer renderer) {
        panel.setLayout(new BorderLayout());
        panel.add(comboBox, BorderLayout.NORTH);
    
        comboBox.setEditable(true);
    }

    public void focus() {
        comboBox.requestFocus();
    }

    public Object getCellEditorValue() {
        Object res = comboBox.getEditor().getItem();
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).equals(res)) return res;
        }
        if (!history.contains(res)) history.add(res.toString());
//        System.out.println("History:");
//        for (String item:history) {
//            System.out.println(item);
//        }
        return res;
    }   
    public Component getTableCellEditorComponent(final JTable table, Object value, boolean isSelected, int row, int column) {
                 int modelRow = table.convertRowIndexToModel( row );

        // 'value' is value contained in the cell located at (rowIndex, vColIndex)
        if (isSelected) { // cell (and perhaps other cells) are selected
        }
        String classes = (String) table.getModel().getValueAt(modelRow, ListTableModel.COL_CLASSES);
        // Configure the component with the specified value
        comboBox.removeAllItems();
        comboBox.addItem(value);
        StringTokenizer st = new StringTokenizer(classes, Character.toString((char) 31));
        while (st.hasMoreTokens()) {
            String next = st.nextToken();
            if (!next.equals(value)) comboBox.addItem(next);
        
        }
        for (String item:history) {
            comboBox.addItem(item);
        }

        //        comboBox.getEditor().addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent e) {
//                System.out.println(e.getActionCommand());
//            }
//        });
        comboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                 if (e.getActionCommand().equals("comboBoxEdited")){
//                     System.out.println(comboBox.getEditor().getItem());
//                     System.out.println(comboBox.getSelectedItem());
//                     System.out.println(((JComboBox)e.getSource()).getSelectedItem());
//                     // comboBox.setSelectedItem(comboBox.getEditor().getItem());
                     stopCellEditing();
                 }

                // stopCellEditing();
            }
        });



        // Return the configured component
        return panel;
    }



}
