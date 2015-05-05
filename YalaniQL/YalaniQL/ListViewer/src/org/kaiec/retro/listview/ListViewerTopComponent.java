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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowFilter.Entry;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.kaiec.retro.data.EventLog;
import org.kaiec.retro.data.Record;
import org.kaiec.retro.data.RecordList;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.kaiec.retro.listview//ListViewer//EN",
autostore = false)
public final class ListViewerTopComponent extends TopComponent {

    private static ListViewerTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "ListViewerTopComponent";
    private FilterConfigPanel filterConfigPanel;
    private JFrame filterConfigWindow;
    private JFrame colorConfigWindow;
    private ColorConfigPanel colorConfigPanel;
    private InstanceContent instanceContent = new InstanceContent();
    private Lookup.Result<Record> result;
    private Record currentLookup;

    public ListViewerTopComponent() {
        initComponents();
        UIManager.put("Tooltip.background", new Color(255, 255, 255));
        setName(NbBundle.getMessage(ListViewerTopComponent.class, "CTL_ListViewerTopComponent"));
        // setToolTipText(NbBundle.getMessage(ListViewerTopComponent.class, "HINT_ListViewerTopComponent"));
        // setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        ToolTipManager.sharedInstance().setInitialDelay(0);
        ToolTipManager.sharedInstance().setDismissDelay(100000);


        jTable1.setModel(new ListTableModel(RecordList.getInstance()));
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(jTable1.getModel());
//        sorter.setComparator(ListTableModel.COL_UPDATED, new Comparator<String>() {
//
//            public int compare(String o1, String o2) {
//            }
//        });
        jTable1.setRowSorter(sorter);
        sorter.addRowSorterListener(new RowSorterListener() {

            public void sorterChanged(RowSorterEvent e) {
                jTable1.repaint();
            }
        });


        filterConfigPanel = new FilterConfigPanel(this, (TableRowSorter) jTable1.getRowSorter());
        filterConfigWindow = new JFrame("Filterkonfiguration");
        filterConfigWindow.setVisible(false);
        filterConfigWindow.setSize(600, 400);
        filterConfigWindow.add(filterConfigPanel);
        filterConfigWindow.addWindowFocusListener(new WindowAdapter() {

            @Override
            public void windowGainedFocus(WindowEvent e) {
                EventLog event = EventLog.createEvent(EventLog.GOTFOCUS);
                event.setType2("FILTERCONF");
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                EventLog event = EventLog.createEvent(EventLog.LOSTFOCUS);
                event.setType2("FILTERCONF");
            }
        });
        colorConfigPanel = new ColorConfigPanel();
        colorConfigWindow = new JFrame("RVK Farbkonfiguration");
        colorConfigWindow.setVisible(false);
        colorConfigWindow.setSize(600, 400);
        colorConfigWindow.add(colorConfigPanel);
        colorConfigWindow.addWindowFocusListener(new WindowAdapter() {

            @Override
            public void windowGainedFocus(WindowEvent e) {
                EventLog event = EventLog.createEvent(EventLog.GOTFOCUS);
                event.setType2("COLORCONF");
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                EventLog event = EventLog.createEvent(EventLog.LOSTFOCUS);
                event.setType2("COLORCONF");
            }
        });

        ComboTableCellRenderer renderer = new ComboTableCellRenderer();

        TableCellEditor editor = new ComboCellEditor(renderer);

        jTable1.getColumnModel().getColumn(ListTableModel.COL_AUTHOR).setCellRenderer(new MultilineTableCellRenderer(Character.toString((char) 31), filterValue));
        jTable1.getColumnModel().getColumn(ListTableModel.COL_AUTHOR).setCellEditor(new MultilineTableCellRenderer(Character.toString((char) 31), filterValue));
        jTable1.getColumnModel().getColumn(ListTableModel.COL_TITLE).setCellRenderer(new MultilineTableCellRenderer(filterValue));
        jTable1.getColumnModel().getColumn(ListTableModel.COL_TITLE).setCellEditor(new MultilineTableCellRenderer(filterValue));
        jTable1.getColumnModel().getColumn(ListTableModel.COL_SERIES).setCellRenderer(new MultilineTableCellRenderer(filterValue));
        jTable1.getColumnModel().getColumn(ListTableModel.COL_SERIES).setCellEditor(new MultilineTableCellRenderer(filterValue));
        jTable1.getColumnModel().getColumn(ListTableModel.COL_EDITION).setCellRenderer(new MultilineTableCellRenderer(filterValue));
        jTable1.getColumnModel().getColumn(ListTableModel.COL_EDITION).setCellEditor(new MultilineTableCellRenderer(filterValue));
        jTable1.getColumnModel().getColumn(ListTableModel.COL_LANGUAGE).setCellRenderer(new MultilineTableCellRenderer(filterValue));
        jTable1.getColumnModel().getColumn(ListTableModel.COL_LANGUAGE).setCellEditor(new MultilineTableCellRenderer(filterValue));
        jTable1.getColumnModel().getColumn(ListTableModel.COL_YEAR).setCellRenderer(new MultilineTableCellRenderer(filterValue));
        jTable1.getColumnModel().getColumn(ListTableModel.COL_YEAR).setCellEditor(new MultilineTableCellRenderer(filterValue));
        jTable1.getColumnModel().getColumn(ListTableModel.COL_KEYWORDS).setCellRenderer(new MultilineTableCellRenderer(Character.toString((char) 31), filterValue));
        jTable1.getColumnModel().getColumn(ListTableModel.COL_KEYWORDS).setCellEditor(new MultilineTableCellRenderer(Character.toString((char) 31), filterValue));
        jTable1.getColumnModel().getColumn(ListTableModel.COL_SIG).setCellRenderer(new MultilineTableCellRenderer(filterValue));
        jTable1.getColumnModel().getColumn(ListTableModel.COL_SIG).setCellEditor(new MultilineTableCellRenderer(filterValue));
        jTable1.getColumnModel().getColumn(ListTableModel.COL_CLASSES).setCellRenderer(new RVKEditor(jTable1, Character.toString((char) 31), colorConfigPanel));
        jTable1.getColumnModel().getColumn(ListTableModel.COL_CLASSES).setCellEditor(new RVKEditor(jTable1, Character.toString((char) 31), colorConfigPanel));
        jTable1.getColumnModel().getColumn(ListTableModel.COL_EDIT).setCellRenderer(renderer);
        jTable1.getColumnModel().getColumn(ListTableModel.COL_EDIT).setCellEditor(editor);
        jTable1.getColumnModel().getColumn(ListTableModel.COL_UPDATED).setCellRenderer(new MultilineTableCellRenderer(filterValue));
        jTable1.getColumnModel().getColumn(ListTableModel.COL_UPDATED).setCellEditor(new MultilineTableCellRenderer(filterValue));
        jSlider1.setValue(30);

        initQuickFilter();

        filterValue.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                if (quickFilterButton.isSelected()) {
                    applyFilters();
                }
            }

            public void removeUpdate(DocumentEvent e) {
                if (quickFilterButton.isSelected()) {
                    applyFilters();
                }
            }

            public void changedUpdate(DocumentEvent e) {
                if (quickFilterButton.isSelected()) {
                    applyFilters();
                }
            }
        });

        filterColumn.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (quickFilterButton.isSelected()) {
                    applyFilters();
                }
            }
        });
        filterCrit.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (quickFilterButton.isSelected()) {
                    applyFilters();
                }
            }
        });
        jTable1.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == ' ') {
                    if (jTable1.isCellEditable(jTable1.getSelectedRow(), ListTableModel.COL_EDIT)) {
                        jTable1.editCellAt(jTable1.getSelectedRow(), ListTableModel.COL_EDIT);
                        jTable1.changeSelection(jTable1.getSelectedRow(), ListTableModel.COL_EDIT, false, false);
                        ((ComboCellEditor) jTable1.getCellEditor()).focus();
                    }
                }
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }
        });

        jTable1.getRowSorter().addRowSorterListener(new RowSorterListener() {

            public void sorterChanged(RowSorterEvent e) {
                updateStatus();
            }
        });
        updateStatus();
        instanceContent.add(this);
        associateLookup(new AbstractLookup(instanceContent));
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                if (currentLookup != null) {
                    instanceContent.remove(currentLookup);
                }
                if (jTable1.getSelectedRow() != -1) {
                    ListTableModel model = (ListTableModel) jTable1.getModel();
                    currentLookup = model.getRecord(jTable1.convertRowIndexToModel(jTable1.getSelectedRow()));
                    instanceContent.add(currentLookup);
                    // System.out.println("Lookup set: " + currentLookup.getPpn());
                    if (jTable1.getCellEditor() != null && jTable1.getEditingRow() != jTable1.getSelectedRow()) {
                        jTable1.getCellEditor().stopCellEditing();
                    }
                    updateStatus();
                }
            }
        });

        jTable1.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        jTable1.addMouseListener(new MouseAdapter() {
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                JTable t = (JTable) e.getComponent();
//                Point pt = e.getPoint();
//                int row = t.rowAtPoint(pt);
//                int col = t.columnAtPoint(pt);
//                if (t.convertRowIndexToModel(row) >= 0 && t.convertColumnIndexToModel(col) == 1) {
//                    TableCellEditor ce = t.getCellEditor(row, col);
//                    ce.stopCellEditing();
//                    Component c = ce.getTableCellEditorComponent(t, null, true, row, col);
//                    Point p = SwingUtilities.convertPoint(t, pt, c);
//                    Component b = SwingUtilities.getDeepestComponentAt(c, p.x, p.y);
//                    if (b instanceof RVKButton) {
//                        ((RVKButton) b).doClick();
//                    }
//                }
//
//            }
//
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                JTable t = (JTable) e.getComponent();
//                Point pt = e.getPoint();
//                int row = t.rowAtPoint(pt);
//                int col = t.columnAtPoint(pt);
//                System.out.println("" + row + "/" + col);
//                if (t.convertRowIndexToModel(row) >= 0 && t.convertColumnIndexToModel(col) == 1) {
//                    TableCellEditor ce = t.getCellEditor(row, col);
//                    ce.stopCellEditing();
//                    Component c = ce.getTableCellEditorComponent(t, null, true, row, col);
//                    Point p = SwingUtilities.convertPoint(t, pt, c);
//                    Component b = SwingUtilities.getDeepestComponentAt(c, p.x, p.y);
//                    if (b instanceof RVKButton) {
//                        System.out.println(((RVKButton) b).getText());
//                    }
//                }
//            }
//        });

        WindowManager.getDefault().getMainWindow().addWindowFocusListener(new WindowAdapter() {

            @Override
            public void windowGainedFocus(WindowEvent e) {
                EventLog event = EventLog.createEvent(EventLog.GOTFOCUS);
                event.setType2("MAIN");
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                EventLog event = EventLog.createEvent(EventLog.LOSTFOCUS);
                event.setType2("MAIN");
            }
        });
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                RecordList.getInstance().saveBackup();
            }
        }, 1800000, 1800000);


        memWarningLabel.setVisible(false);
        
        MemoryWarningSystem.setPercentageUsageThreshold(0.8);

        MemoryWarningSystem mws = new MemoryWarningSystem();
        mws.addListener(new MemoryWarningSystem.Listener() {

            public void memoryUsageLow(long usedMemory, long maxMemory) {
                memWarningLabel.setVisible(true);
                NotifyDescriptor d =
                        new NotifyDescriptor.Message("80% des zur verfügung stehenden Speichers sind voll. Es könnte zu Stabilitätsproblemen kommen.", NotifyDescriptor.INFORMATION_MESSAGE);
                DialogDisplayer.getDefault().notifyLater(d);
            }
        });

        

    }

    private void initQuickFilter() {
        filterColumn.removeAllItems();
        for (int i = 0; i < jTable1.getModel().getColumnCount(); i++) {
            filterColumn.addItem(jTable1.getModel().getColumnName(i));
        }
        filterColumn.addItem("Irgendwo");
        filterCrit.removeAllItems();
        filterCrit.addItem("enthält");
        filterCrit.addItem("enthält nicht");
        filterCrit.addItem("beginnt mit");
        filterCrit.addItem("ist");
        filterCrit.addItem("ist nicht");
        filterCrit.addItem("kleiner");
        filterCrit.addItem("größer");
        filterCrit.addItem("Wortzahl");
        filterValue.setText("");
    }

    public RowFilter<Object, Object> getRowFilter() {
        RowFilter<Object, Object> filter = new RowFilter<Object, Object>() {

            public boolean include(Entry entry) {

                // Irgendwo Filter
                if (filterColumn.getSelectedIndex() == filterColumn.getItemCount() - 1) {
                    for (int i = 0; i < filterColumn.getItemCount() - 1; i++) {
                        if (includeByColumn(entry, i)) {
                            return true;
                        }
                    }
                    return false;
                }

                // Normale Filter
                return includeByColumn(entry, filterColumn.getSelectedIndex());
            }
        };
        return filter;
    }

    private boolean includeByColumn(Entry entry, int column) {
        String value = null;
        if (entry.getValue(column) != null) {
            value = entry.getValue(column).toString();
        }
        if (value == null) {
            value = "";
        }
        value = value.toLowerCase();
        if (filterCrit.getSelectedItem().equals("enthält")) {
            return value.indexOf(filterValue.getText().toLowerCase()) != -1;
        } else if (filterCrit.getSelectedItem().equals("enthält nicht")) {
            return value.indexOf(filterValue.getText().toLowerCase()) == -1;
        } else if (filterCrit.getSelectedItem().equals("beginnt mit")) {
            return value.startsWith(filterValue.getText().toLowerCase());
        } else if (filterCrit.getSelectedItem().equals("ist")) {
            return value.equals(filterValue.getText().toLowerCase());
        } else if (filterCrit.getSelectedItem().equals("ist nicht")) {
            return !value.equals(filterValue.getText().toLowerCase());
        } else if (filterCrit.getSelectedItem().equals("kleiner")) {
            return value.compareTo(filterValue.getText().toLowerCase()) < 0;
        } else if (filterCrit.getSelectedItem().equals("größer")) {
            return value.compareTo(filterValue.getText().toLowerCase()) > 0;
        } else if (filterCrit.getSelectedItem().equals("Wortzahl")) {
            try {
                int number = Integer.parseInt(filterValue.getText());
                if (number==0 && value.trim().length()==0) return true;
                boolean rvk = filterColumn.getSelectedItem().equals(((ListTableModel) jTable1.getModel()).getColumnName(ListTableModel.COL_CLASSES));
                if (rvk) {
                    if (number > 1) {
                        return value.split(Character.toString((char) 31)).length == number;
                    } else if (number == 1) {
                        return (value.trim().length() > 0) && (value.indexOf(31) == -1);
                    }
                    return false;
                }
                else {
                    if (number > 1) {
                        return value.split(" ").length == number;
                    } else if (number == 1) {
                        return (value.trim().length() > 0) && (value.indexOf(" ") == -1);
                    }
                    return false;
                }
                
            } catch (NumberFormatException nfe) {
            }
        }
        return true;

    }

    public JTable getTable() {
        return jTable1;
    }

    public int getSelectedRow() {
        return jTable1.getSelectedRow();
    }

    public void selectRow(int i) {
        if (i >= 0 && i < jTable1.getRowCount()) {
            jTable1.getSelectionModel().setSelectionInterval(i, i);
        }
    }

    public Record getSelectedRecord() {
        int row = getSelectedRow();
        if (row == -1) {
            return null;
        }
        int mr = jTable1.convertRowIndexToModel(row);
        return ((ListTableModel) jTable1.getModel()).getRecord(mr);
    }

    public void stopEditing() {
        if (jTable1.getCellEditor() != null) {
            jTable1.getCellEditor().stopCellEditing();

        }
    }

    private void updateStatus() {
        int context = 0;
        int todo = 0;
        int count = jTable1.getRowSorter().getViewRowCount();
        ListTableModel model = (ListTableModel) jTable1.getModel();
        for (int i = 0; i < count; i++) {
            Record rec = model.getRecord(jTable1.convertRowIndexToModel(i));
            if (rec.isContext()) {
                context++;
            }
            if (!rec.isContext() && rec.getAssignment().isEmpty()) {
                todo++;
            }
        }
        int toedit = count - context;
        int done = toedit - todo;
        float percentage = ((float) Math.round((((float) done) / toedit) * 1000)) / 10;
        StringBuffer status = new StringBuffer();
        status.append("Rows: ").append(count).append(" (").append(toedit).append("/").append(context).append(")");
        status.append(" Todo/Done: " + todo + "/" + done + " (" + percentage + "%)");
        status.append(" Row #: " + (jTable1.getSelectedRow() + 1));
        statusLabel.setText(status.toString());

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSlider1 = new javax.swing.JSlider();
        filterButton = new javax.swing.JToggleButton();
        statusLabel = new javax.swing.JLabel();
        filterConfigButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        rvkColoButton = new javax.swing.JButton();
        contextButton = new javax.swing.JToggleButton();
        jButton2 = new javax.swing.JButton();
        massAssignment = new javax.swing.JButton();
        filterColumn = new javax.swing.JComboBox();
        filterCrit = new javax.swing.JComboBox();
        filterValue = new javax.swing.JTextField();
        quickFilterButton = new javax.swing.JToggleButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        memWarningLabel = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setRowMargin(3);
        jScrollPane1.setViewportView(jTable1);

        jSlider1.setMaximum(200);
        jSlider1.setMinimum(20);
        jSlider1.setOrientation(javax.swing.JSlider.VERTICAL);
        jSlider1.setValue(20);
        jSlider1.setInverted(true);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        filterButton.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(filterButton, org.openide.util.NbBundle.getMessage(ListViewerTopComponent.class, "ListViewerTopComponent.filterButton.text")); // NOI18N
        filterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(statusLabel, org.openide.util.NbBundle.getMessage(ListViewerTopComponent.class, "ListViewerTopComponent.statusLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(filterConfigButton, org.openide.util.NbBundle.getMessage(ListViewerTopComponent.class, "ListViewerTopComponent.filterConfigButton.text")); // NOI18N
        filterConfigButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterConfigButtonActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/kaiec/retro/listview/small_bullet_star_blue.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(ListViewerTopComponent.class, "ListViewerTopComponent.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(rvkColoButton, org.openide.util.NbBundle.getMessage(ListViewerTopComponent.class, "ListViewerTopComponent.rvkColoButton.text")); // NOI18N
        rvkColoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rvkColoButtonActionPerformed(evt);
            }
        });

        contextButton.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(contextButton, org.openide.util.NbBundle.getMessage(ListViewerTopComponent.class, "ListViewerTopComponent.contextButton.text")); // NOI18N
        contextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contextButtonActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/kaiec/retro/listview/refresh_12.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButton2, org.openide.util.NbBundle.getMessage(ListViewerTopComponent.class, "ListViewerTopComponent.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(massAssignment, org.openide.util.NbBundle.getMessage(ListViewerTopComponent.class, "ListViewerTopComponent.massAssignment.text")); // NOI18N
        massAssignment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                massAssignmentActionPerformed(evt);
            }
        });

        filterColumn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        filterCrit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        filterValue.setText(org.openide.util.NbBundle.getMessage(ListViewerTopComponent.class, "ListViewerTopComponent.filterValue.text")); // NOI18N

        quickFilterButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/kaiec/retro/listview/ok.png"))); // NOI18N
        quickFilterButton.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(quickFilterButton, org.openide.util.NbBundle.getMessage(ListViewerTopComponent.class, "ListViewerTopComponent.quickFilterButton.text")); // NOI18N
        quickFilterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quickFilterButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButton3, org.openide.util.NbBundle.getMessage(ListViewerTopComponent.class, "ListViewerTopComponent.jButton3.text")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButton4, org.openide.util.NbBundle.getMessage(ListViewerTopComponent.class, "ListViewerTopComponent.jButton4.text")); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        memWarningLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        memWarningLabel.setForeground(new java.awt.Color(255, 0, 51));
        org.openide.awt.Mnemonics.setLocalizedText(memWarningLabel, org.openide.util.NbBundle.getMessage(ListViewerTopComponent.class, "ListViewerTopComponent.memWarningLabel.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(statusLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 350, Short.MAX_VALUE)
                                .addComponent(memWarningLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(massAssignment)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rvkColoButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(contextButton))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 948, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(filterColumn, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterCrit, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterValue, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(quickFilterButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                        .addComponent(filterConfigButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(filterButton)
                        .addComponent(filterConfigButton))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(filterCrit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(filterColumn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(filterValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(quickFilterButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(contextButton)
                            .addComponent(rvkColoButton)
                            .addComponent(massAssignment)
                            .addComponent(statusLabel)
                            .addComponent(jButton3)
                            .addComponent(jButton4)
                            .addComponent(memWarningLabel))))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        jTable1.setRowHeight(jSlider1.getValue());
    }//GEN-LAST:event_jSlider1StateChanged

    private void filterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterButtonActionPerformed
        applyFilters();
    }//GEN-LAST:event_filterButtonActionPerformed

    private void filterConfigButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterConfigButtonActionPerformed
        filterConfigWindow.setVisible(true);
    }//GEN-LAST:event_filterConfigButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        for (int r = 0; r < jTable1.getRowCount(); r++) {
            // Get the preferred height
            int h = getPreferredRowHeight(jTable1, r, 2);
            // Now set the row height using the preferred height
            if (jTable1.getRowHeight(r) != h) {
                jTable1.setRowHeight(r, h);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void rvkColoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rvkColoButtonActionPerformed
        colorConfigWindow.setVisible(true);
    }//GEN-LAST:event_rvkColoButtonActionPerformed

    public String getFilterSignature() {
        return "(" + filterColumn.getSelectedItem() + "//" + filterCrit.getSelectedItem() + "//" + filterValue.getText() + ")";
    }

    public void applyFilters() {
        StringBuilder filterSignature = new StringBuilder();
        List<RowFilter<Object, Object>> flist = new ArrayList<RowFilter<Object, Object>>();



        if (!(contextButton.isSelected())) {
            flist.add(new RowFilter<Object, Object>() {

                @Override
                public boolean include(Entry<? extends Object, ? extends Object> entry) {
                    Record rec = ((ListTableModel) jTable1.getModel()).getRecord((Integer) entry.getIdentifier());
                    return !rec.isContext();
                }
            });
            filterSignature.append("!context");
        }

        if (filterButton.isSelected()) {
            flist.add(filterConfigPanel.getRowFilter());
            String f = filterConfigPanel.getFilterSignature();
            if (filterSignature.length() > 0 && f.length() > 0) {
                filterSignature.append(" && ");
            }
            filterSignature.append(f);
        }

        if (quickFilterButton.isSelected()) {
            flist.add(getRowFilter());
            String f = getFilterSignature();
            if (filterSignature.length() > 0 && f.length() > 0) {
                filterSignature.append(" && ");
            }
            filterSignature.append("Quick:" + f);
        }
        ((TableRowSorter) jTable1.getRowSorter()).setRowFilter(RowFilter.andFilter(flist));
        EventLog event = EventLog.createEvent(EventLog.FILTER_NEW);
        event.setMessage(filterSignature.toString());
    }

    private void contextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contextButtonActionPerformed
        applyFilters();

    }//GEN-LAST:event_contextButtonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        updateStatus();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void massAssignmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_massAssignmentActionPerformed
        PicklistPanel myPanel = new PicklistPanel(new PicklistEntry());
        NotifyDescriptor nd = new NotifyDescriptor(
                myPanel, // instance of your panel
                "(!) Zuweisung an alle sichtbaren Reihen", // title of the dialog
                NotifyDescriptor.OK_CANCEL_OPTION, // it is Yes/No dialog ...
                NotifyDescriptor.QUESTION_MESSAGE, // ... of a question type => a question mark icon
                null, // we have specified YES_NO_OPTION => can be null, options specified by L&F,
                // otherwise specify options as:
                //     new Object[] { NotifyDescriptor.YES_OPTION, ... etc. },
                NotifyDescriptor.OK_OPTION // default option is "Yes"
                );

        // let's display the dialog now...
        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
            String assignment = myPanel.getUpdatedEntry().getValue();
            RecordList.getInstance().setFireChanges(false);
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                int row = jTable1.convertRowIndexToModel(i);
                ((ListTableModel) jTable1.getModel()).setValueAt(assignment, row, ListTableModel.COL_EDIT);
            }
            EventLog event = EventLog.createEvent(EventLog.ASS_MASS);
            event.setMessage(assignment);
            RecordList.getInstance().setFireChanges(true);
            RecordList.getInstance().datachanged();
        }
    }//GEN-LAST:event_massAssignmentActionPerformed

    private void quickFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quickFilterButtonActionPerformed
        applyFilters();
    }//GEN-LAST:event_quickFilterButtonActionPerformed

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    new ExportFiltered().actionPerformed(evt);
}//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        RecordList.getInstance().saveBackup();
    }//GEN-LAST:event_jButton4ActionPerformed

    public int getPreferredRowHeight(JTable table, int rowIndex, int margin) {
        // Get the current default height for all rows
        int height = table.getRowHeight();
        // Determine highest cell in the row
//        for (int c=0; c<table.getColumnCount(); c++) {
//            TableCellRenderer renderer = table.getCellRenderer(rowIndex, c);
//            Component comp = table.prepareRenderer(renderer, rowIndex, c);
//            int h = comp.getPreferredSize().height + 2*margin;
//            height = Math.max(height, h);
//        }
        TableCellRenderer renderer = table.getCellRenderer(rowIndex, ListTableModel.COL_KEYWORDS);
        Component comp = table.prepareRenderer(renderer, rowIndex, ListTableModel.COL_KEYWORDS);
        int h = comp.getPreferredSize().height + 2 * margin;
        height = Math.max(height, h);
        return h;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton contextButton;
    private javax.swing.JToggleButton filterButton;
    private javax.swing.JComboBox filterColumn;
    private javax.swing.JButton filterConfigButton;
    private javax.swing.JComboBox filterCrit;
    private javax.swing.JTextField filterValue;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton massAssignment;
    private javax.swing.JLabel memWarningLabel;
    private javax.swing.JToggleButton quickFilterButton;
    private javax.swing.JButton rvkColoButton;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized ListViewerTopComponent getDefault() {
        if (instance == null) {
            instance = new ListViewerTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the ListViewerTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized ListViewerTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(ListViewerTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof ListViewerTopComponent) {
            return (ListViewerTopComponent) win;
        }
        Logger.getLogger(ListViewerTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }
}
