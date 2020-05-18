/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SingleFilterPanel.java
 *
 * Created on 07.12.2010, 13:25:40
 */
package org.kaiec.retro.listview;

import java.util.ResourceBundle;
import javax.swing.RowFilter;
import javax.swing.RowFilter.Entry;
import org.kaiec.retro.data.Preferences;

/**
 *
 * @author kai
 */
public class SingleFilterPanel extends javax.swing.JPanel {

    FilterConfigPanel root;
    ResourceBundle i18n = java.util.ResourceBundle.getBundle("org/kaiec/retro/listview/Bundle");

    /** Creates new form SingleFilterPanel */
    public SingleFilterPanel(FilterConfigPanel root) {
        initComponents();
        this.root = root;
        filterColumn.removeAllItems();
        // Skip last column that contains the timestamp, this is
        // not usable for filters. See issue #2
        for (int i = 0; i < root.getModel().getColumnCount() - 1; i++) {
            String name = root.getModel().getColumnName(i);
	    if (name.equals(Preferences.HIDE_CUSTOM_FIELD)) continue;
            filterColumn.addItem(name);
        }
        // Anywhere is not (yet) supported here! See issue #2
        // filterColumn.addItem(i18n.getString("anywhere"));
        filterCrit.removeAllItems();
        filterCrit.addItem(i18n.getString("filter-contains"));
        filterCrit.addItem(i18n.getString("filter-contains-not"));
        filterCrit.addItem(i18n.getString("filter-begins-with"));
        filterCrit.addItem(i18n.getString("filter-is"));
        filterCrit.addItem(i18n.getString("filter-is-not"));
        filterCrit.addItem(i18n.getString("filter-smaller"));
        filterCrit.addItem(i18n.getString("filter-bigger"));
        filterCrit.addItem(i18n.getString("filter-wordcount"));
        filterValue.setText("");
    }

    private boolean includeByColumn(Entry entry, int column) {
        String value = (String) entry.getValue(column);
        value = value.toLowerCase();
        if (filterCrit.getSelectedItem().equals(i18n.getString("filter-contains"))) {
            return value.indexOf(filterValue.getText().toLowerCase()) != -1;
        } else if (filterCrit.getSelectedItem().equals(i18n.getString("filter-contains-not"))) {
            return value.indexOf(filterValue.getText().toLowerCase()) == -1;
        } else if (filterCrit.getSelectedItem().equals(i18n.getString("filter-begins-with"))) {
            return value.startsWith(filterValue.getText().toLowerCase());
        } else if (filterCrit.getSelectedItem().equals(i18n.getString("filter-is"))) {
            return value.equals(filterValue.getText().toLowerCase());
        } else if (filterCrit.getSelectedItem().equals(i18n.getString("filter-is-not"))) {
            return !value.equals(filterValue.getText().toLowerCase());
        } else if (filterCrit.getSelectedItem().equals(i18n.getString("filter-smaller"))) {
            return value.compareTo(filterValue.getText().toLowerCase()) < 0;
        } else if (filterCrit.getSelectedItem().equals(i18n.getString("filter-bigger"))) {
            return value.compareTo(filterValue.getText().toLowerCase()) > 0;
        } else if (filterCrit.getSelectedItem().equals(i18n.getString("filter-wordcount"))) {
            try {
                int number = Integer.parseInt(filterValue.getText());
                if (number==0 && value.trim().length()==0) return true;
                boolean rvk = filterColumn.getSelectedItem().equals(((ListTableModel) entry.getModel()).getColumnName(ListTableModel.COL_CLASSES));
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

    public String getFilterSignature() {
        return "(" + filterColumn.getSelectedItem() + "//" + filterCrit.getSelectedItem() + "//" + filterValue.getText() + ")";
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filterColumn = new javax.swing.JComboBox();
        filterCrit = new javax.swing.JComboBox();
        filterValue = new javax.swing.JTextField();
        removeButton = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(32767, 59));

        filterColumn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        filterCrit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        filterValue.setText(org.openide.util.NbBundle.getMessage(SingleFilterPanel.class, "SingleFilterPanel.filterValue.text")); // NOI18N

        removeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/kaiec/retro/listview/remove.png"))); // NOI18N
        removeButton.setText(org.openide.util.NbBundle.getMessage(SingleFilterPanel.class, "SingleFilterPanel.removeButton.text")); // NOI18N
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(filterColumn, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterCrit, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterValue, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(filterValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeButton)
                    .addComponent(filterCrit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterColumn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        root.removeFilter(this);
    }//GEN-LAST:event_removeButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox filterColumn;
    private javax.swing.JComboBox filterCrit;
    private javax.swing.JTextField filterValue;
    private javax.swing.JButton removeButton;
    // End of variables declaration//GEN-END:variables
}
