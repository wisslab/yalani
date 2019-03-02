/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaiec.retro.listview;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.font.LineMetrics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.kaiec.retro.data.ImportRVKDescs;

/**
 *
 * @author kai
 */
public class RVKEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

    private Object value;
    private String sep;
    private ColorConfigPanel ccp;
    private RVKPanel panel;

    public RVKEditor(JTable table, String sep, ColorConfigPanel ccp) {
        this.sep = sep;
        this.ccp = ccp;
        this.panel = new RVKPanel(this, table);

    }

    public Component getTableCellRendererComponent(final JTable table, Object o, boolean isSelected, boolean hasFocus, int row, int column) {
        panel.configure((String) o, sep, ccp, isSelected, row, column);
        return panel;
    }

    public Object getCellEditorValue() {
        return value;

    }

    public void stopEdit() {
        fireEditingStopped();
    }

    public Component getTableCellEditorComponent(JTable jtable, Object o, boolean isSelected, int i, int i1) {
        this.value = o;
        Component c = getTableCellRendererComponent(jtable, o, isSelected, false, i, i1);
        RenderUtility.configureColors(c, jtable, true, i, i1);
        return c;
    }

    


   


}

class RVKPanel extends JPanel {

    private List<RVKButton> buttons = new ArrayList<RVKButton>();
    private Map<TablePosition,List<RVKButton>> buttonMap = new HashMap<TablePosition, List<RVKButton>>();
    private int row,column;

    private JTable table;
    private RVKEditor rvkeditor;

    public void configure(String value, String sep, ColorConfigPanel ccp, boolean isSelected, final int row, int column) {
        this.row = row;
        this.column = column;
        removeAll();
        buttons.clear();
        int modelRow = table.convertRowIndexToModel(row);
        RenderUtility.configureColors(this, table, isSelected, row, column);
        if (value != null) {
            StringTokenizer st = new StringTokenizer(value, sep);
            
            while (st.hasMoreTokens()) {
                String next = st.nextToken();
//                String desc = ImportRVKDescs.getDescription(next);
//                if (desc!=null) next = next + ": " + desc;
                final RVKButton button = new RVKButton(next);

               
                button.setBackground(ccp.getColor(next, Color.WHITE));
                button.setRow(modelRow);
                // label.setToolTipText(ImportRVKDescs.getDescription(next));
                add(button);
                button.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Click: " + button.getRow() + " " + button.getText());
                        table.getModel().setValueAt(button.getText(), button.getRow(), ListTableModel.COL_EDIT);
                        rvkeditor.stopEdit();
                        if (row>0&&row<table.getRowCount()) table.getSelectionModel().setSelectionInterval(row, row);
                    }
                });
                // System.out.println(button.getBounds());
                buttons.add(button);
            }

    }
    }

    public RVKPanel(final RVKEditor rvkeditor, final JTable table) {
        this.table = table;
        this.rvkeditor = rvkeditor;
        setLayout(new FlowLayout(FlowLayout.LEFT));
            ToolTipManager.sharedInstance().registerComponent(this);
            
        

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        List<RVKButton> list = new ArrayList<RVKButton>();
        buttonMap.put(new TablePosition(row, column), list);
        list.addAll(buttons);
    }



    @Override
    public String getToolTipText(MouseEvent event) {
        //StringBuffer sb = new StringBuffer();
        List<RVKButton> list = buttonMap.get(new TablePosition(row, column));
        if (list==null) return null;
        for (RVKButton b:list) {
            // sb.append(b.getText());
            if (b.getBounds().contains(event.getPoint())) return b.getToolTipText();
        }
        return null;
        // return sb.toString();
//        Component c = SwingUtilities.getDeepestComponentAt(this, event.getX(), event.getY());
//        if (c!=null) return c.getName();
//        return "null";
    }
}

class RVKButton extends JButton {

    private int row;

    public RVKButton(String text) {
        super(text);
        ToolTipManager.sharedInstance().registerComponent(this);
 //setBorder(BorderFactory.createLineBorder(Color.black));
                setForeground(Color.BLACK);
                setOpaque(true);
                // setMargin(null);
                setBorder(BorderFactory.createEmptyBorder(0,2,0,2));
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(getForeground());
        g.setFont(getFont());

        FontMetrics fm = getFontMetrics(getFont());
        LineMetrics lm = fm.getLineMetrics(getText(), g);
        g.drawString(getText(), getBorder().getBorderInsets(this).left,getBorder().getBorderInsets(this).top + (int)lm.getAscent());
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth()-1, getHeight()-1);
    }



    @Override
    public String getToolTipText() {
        return "<html>" + ImportRVKDescs.getDescription(getText()).replaceAll(" / ", "<br>") + "</html>";
    }
}

class TablePosition {
    private int row;
    private int col;

    public TablePosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TablePosition other = (TablePosition) obj;
        if (this.row != other.row) {
            return false;
        }
        if (this.col != other.col) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.row;
        hash = 79 * hash + this.col;
        return hash;
    }


}
