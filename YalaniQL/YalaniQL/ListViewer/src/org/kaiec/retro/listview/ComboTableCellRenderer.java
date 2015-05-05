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

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Kai Eckert (http://www.kaiec.org)
 */
public class ComboTableCellRenderer implements TableCellRenderer {

    DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer();

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        tableRenderer = (DefaultTableCellRenderer) tableRenderer.getTableCellRendererComponent(table,
                value, isSelected, hasFocus, row, column);

        RenderUtility.configureColors(tableRenderer, table, isSelected, row, column);
        tableRenderer.setText((String) value);
        tableRenderer.setVerticalAlignment(SwingConstants.TOP);
        tableRenderer.setLocation(3, 3);
        return tableRenderer;
    }
}
