/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaiec.retro.listview;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;

/**
 *
 * @author kai
 */
public class RenderUtility {

    public static void configureColors(Component in, JTable table, boolean isSelected, int row, int column) {
        ListTableModel model = (ListTableModel) table.getModel();
        int modelRow = table.convertRowIndexToModel(row);
        if (isSelected) {
            in.setBackground(new Color(table.getSelectionBackground().getRGB()));
            in.setForeground(new Color(table.getSelectionForeground().getRGB()));
            if (model.getRecord(modelRow).isContext()) {
                in.setFont(in.getFont().deriveFont(Font.ITALIC));
                in.setForeground(new Color(100, 100, 100));
            } else {
                in.setFont(in.getFont().deriveFont(Font.PLAIN));
            }
        } else {
            if (row % 2 == 0) {
                in.setBackground(Color.WHITE);
            } else {
                in.setBackground(Color.LIGHT_GRAY);
            }
            if (model.getRecord(modelRow).isContext()) {
                in.setFont(in.getFont().deriveFont(Font.ITALIC));
                in.setForeground(new Color(100, 100, 100));
            } else {
                in.setFont(in.getFont().deriveFont(Font.PLAIN));
                in.setForeground(new Color(table.getForeground().getRGB()));
            }
        }

    }
}
