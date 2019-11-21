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
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Kai Eckert (http://www.kaiec.org)
 */
public class MultilineTableCellRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

    private String linebreak = null;
    private Object value;

    private MyJTextArea textarea = new MyJTextArea();

  public MultilineTableCellRenderer(final JTextField filterField) {
    textarea.setOpaque(true);
    textarea.setWrapStyleWord(true);
    textarea.setLineWrap(true);
    textarea.setEditable(true);
    textarea.setSelectionColor(Color.blue);
    textarea.setMargin(new Insets(2, 2, 2, 2));
    textarea.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                String text = textarea.getText().replaceAll("\n", " ");
                int caret = textarea.viewToModel2D(e.getPoint());
//                System.out.println("Caret;: " + caret);
		int next = text.substring(caret).indexOf(" ");
  //              System.out.println("Rest: " + textarea.getText().substring(caret));
    //            System.out.println("Next: " + next);
                if (next==-1) next = text.length()-caret;
      //          System.out.println("Next: " + next);
                int last = text.substring(0,caret).lastIndexOf(" ");
        //        System.out.println("Start: " + textarea.getText().substring(0,caret));
          //      System.out.println("Last: " + last);
                String word = text.substring(last+1,next+caret);
                if (word.endsWith(",")) word.substring(0,word.length()-1);
                if (word.endsWith(":")) word.substring(0,word.length()-1);
                if (word.endsWith(".")) word.substring(0,word.length()-1);
            //    System.out.println(word);
                filterField.setText(word);
            }

    });

  }

    public MultilineTableCellRenderer(String linebreak, JTextField field) {
        this(field);
        this.linebreak = linebreak;

    }

    public Object getCellEditorValue() {
        return value;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.value = value;
        Component c = getTableCellRendererComponent(table, value, isSelected, false, row, column);
        RenderUtility.configureColors(c, table, true, row, column);
        return c;
    }





  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
      boolean hasFocus, int row, int column) {
      RenderUtility.configureColors(textarea, table, isSelected, row, column);
      String out = ""; 
      if (value!=null) {
          out = value.toString();
          if (linebreak!=null) out = out.replaceAll(linebreak, "\n");
      }
      textarea.setText(out);
    return textarea;
  }
}

class MyJTextArea extends JTextArea {

    @Override
    public void paint(Graphics grphcs) {
      // System.out.println("Text: " + getText() + "/"+getSize() + "/" + getPreferredSize());
//      while (getPreferredSize().getHeight()<getSize().getHeight() && getFont().getSize2D() < 12f) {
//
//        setFont(getFont().deriveFont(getFont().getSize2D()+0.2f));
//      }
//      while (getPreferredSize().getHeight()>getSize().getHeight()) {
//
//        // System.out.println("Text: " + getText() + "/"+getSize() + "/" + getPreferredSize()+"/"+getFont().getSize2D());
//        setFont(getFont().deriveFont(getFont().getSize2D()-0.2f));
//      }
        super.paint(grphcs);
    }

   

}
