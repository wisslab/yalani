/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kaiec.retro.listview;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Date;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author kai
 */
public class MultiLabelRenderer extends JPanel implements TableCellRenderer {

    private String sep;
    public MultiLabelRenderer(String sep) {
        this.sep = sep;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        
    }



    public Component getTableCellRendererComponent(JTable table, Object o, boolean isSelected, boolean bln1, int row, int i1) {
        if (isSelected) {
          setBackground(new Color(table.getSelectionBackground().getRGB()));
          setForeground(new Color(table.getSelectionForeground().getRGB()));
      } else {
          if (row%2==0) {
              setBackground(Color.WHITE);
          } else {
            setBackground(new Color(table.getBackground().getRGB()));
          }
          setForeground(new Color(table.getForeground().getRGB()));
      }
        this.removeAll();
        if (o!=null && o instanceof String) {
            StringTokenizer st = new StringTokenizer(o.toString(), sep);
            while (st.hasMoreTokens()) {
                String next = st.nextToken();
//                String desc = ImportRVKDescs.getDescription(next);
//                if (desc!=null) next = next + ": " + desc;
                JLabel label = new JLabel(next);
                label.setBorder(BorderFactory.createLineBorder(Color.black));
                if (next.startsWith("SK")) {
                    label.setBackground(Color.green);
                    label.setOpaque(true);
                } else {
                    label.setBackground(getBackground());
                    label.setOpaque(true);
                }
                label.setForeground(getForeground());
                add(label);
            }
        }        
        
        // table.addMouseMotionListener(new MyMouseMotionListener(this));

        return this;
    }


}

class MyMouseMotionListener implements MouseMotionListener {
    private JPanel panel;

    public MyMouseMotionListener(JPanel panel) {
        this.panel = panel;
    }

    public void mouseDragged(MouseEvent me) {
    }

    public void mouseMoved(MouseEvent me) {
        JTable table = (JTable) me.getSource();
        int row = table.rowAtPoint(me.getPoint());
        int col = table.columnAtPoint(me.getPoint());
        Rectangle rect = table.getCellRect(row, col, false);
        int x = me.getPoint().x - rect.x;
        int y = me.getPoint().y - rect.y;
        System.out.println(new Date());
        System.out.println(""+x+"/"+y);
    }

    }
