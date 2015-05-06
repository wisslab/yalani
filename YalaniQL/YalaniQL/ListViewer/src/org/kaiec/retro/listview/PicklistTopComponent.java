/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaiec.retro.listview;

import org.kaiec.retro.data.CloseListener;
import org.kaiec.retro.data.CloseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.kaiec.retro.data.EventLog;
import org.kaiec.retro.data.PrefSerializer;
import org.kaiec.retro.data.Record;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.kaiec.retro.listview//Picklist//EN",
autostore = false)
public final class PicklistTopComponent extends TopComponent {

    private static PicklistTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "PicklistTopComponent";
    private Lookup.Result<Record> result;
    private Record record;
    List<PicklistEntry> picklist = new ArrayList<PicklistEntry>();
    public PicklistTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(PicklistTopComponent.class, "CTL_PicklistTopComponent"));
        // setToolTipText(NbBundle.getMessage(PicklistTopComponent.class, "HINT_PicklistTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        Lookup.Template<Record> template = new Lookup.Template<Record>(Record.class);
        this.result = Utilities.actionsGlobalContext().lookup(template);
        this.result.addLookupListener(new LookupListener() {

            public void resultChanged(LookupEvent arg0) {
                Record rec = getRecordFromLookup();
                if (rec != null) {
                    setRecord(rec);
                }

            }
        });

        final PrefSerializer ps = new PrefSerializer(getClass());
        picklist = ps.loadList("Picklist");

        jList1.setModel(new SimpleListModel(picklist));

        CloseEvent.getInstance().addCloseListener(new CloseListener() {

            public void closing() {
                ps.saveList("Picklist", picklist);
            }
        });
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public Record getRecord() {
        return record;
    }

    public Record getRecordFromLookup() {
        Iterator it = result.allInstances().iterator();
        if (!it.hasNext()) {
            return null;
        }
        return (Record) it.next();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        addButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        delButton = new javax.swing.JButton();

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/kaiec/retro/listview/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addButton, org.openide.util.NbBundle.getMessage(PicklistTopComponent.class, "PicklistTopComponent.addButton.text")); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        editButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/kaiec/retro/listview/edit.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(editButton, org.openide.util.NbBundle.getMessage(PicklistTopComponent.class, "PicklistTopComponent.editButton.text")); // NOI18N
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        delButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/kaiec/retro/listview/remove.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(delButton, org.openide.util.NbBundle.getMessage(PicklistTopComponent.class, "PicklistTopComponent.delButton.text")); // NOI18N
        delButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addButton)
                    .addComponent(editButton)
                    .addComponent(delButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        PicklistPanel myPanel = new PicklistPanel(new PicklistEntry());
        NotifyDescriptor nd = new NotifyDescriptor(
                myPanel, // instance of your panel
                "Picklisteneintrag", // title of the dialog
                NotifyDescriptor.OK_CANCEL_OPTION, // it is Yes/No dialog ...
                NotifyDescriptor.QUESTION_MESSAGE, // ... of a question type => a question mark icon
                null, // we have specified YES_NO_OPTION => can be null, options specified by L&F,
                // otherwise specify options as:
                //     new Object[] { NotifyDescriptor.YES_OPTION, ... etc. },
                NotifyDescriptor.OK_OPTION // default option is "Yes"
                );

        // let's display the dialog now...
        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
            picklist.add(myPanel.getUpdatedEntry());
            ((SimpleListModel) jList1.getModel()).datachanged();
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        PicklistEntry p = (PicklistEntry) jList1.getSelectedValue();
        if (p != null) {
            PicklistPanel myPanel = new PicklistPanel(p);
            NotifyDescriptor nd = new NotifyDescriptor(
                    myPanel, // instance of your panel
                    "Picklisteneintrag", // title of the dialog
                    NotifyDescriptor.OK_CANCEL_OPTION, // it is Yes/No dialog ...
                    NotifyDescriptor.QUESTION_MESSAGE, // ... of a question type => a question mark icon
                    null, // we have specified YES_NO_OPTION => can be null, options specified by L&F,
                    // otherwise specify options as:
                    //     new Object[] { NotifyDescriptor.YES_OPTION, ... etc. },
                    NotifyDescriptor.OK_OPTION // default option is "Yes"
                    );

            // let's display the dialog now...
            if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
                myPanel.getUpdatedEntry();
                ((SimpleListModel) jList1.getModel()).datachanged();
            }
        }
    }//GEN-LAST:event_editButtonActionPerformed

    private void delButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delButtonActionPerformed
        PicklistEntry p = (PicklistEntry) jList1.getSelectedValue();
        picklist.remove(p);
        ((SimpleListModel) jList1.getModel()).datachanged();
    }//GEN-LAST:event_delButtonActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        if (evt.getClickCount() == 2) {
            PicklistEntry entry = (PicklistEntry) jList1.getSelectedValue();
            if (entry != null && record!=null) {
                EventLog event = EventLog.createEvent(EventLog.PICKLIST);
                event.setType2(entry.getValue());
                event.setExternalId(record.getExternalId());
                int row = ListViewerTopComponent.findInstance().getSelectedRow();
                ListViewerTopComponent.findInstance().stopEditing();
                record.setAssignment(entry.getValue());
                Record tmp = record = ListViewerTopComponent.findInstance().getSelectedRecord();
                if (tmp!=null && tmp.equals(record)) {
                    ListViewerTopComponent.findInstance().selectRow(row+1);
                    record = ListViewerTopComponent.findInstance().getSelectedRecord();
                } else if (tmp==null) {
                    ListViewerTopComponent.findInstance().selectRow(row);
                    record = ListViewerTopComponent.findInstance().getSelectedRecord();
                }
            }
        }
    }//GEN-LAST:event_jList1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton delButton;
    private javax.swing.JButton editButton;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized PicklistTopComponent getDefault() {
        if (instance == null) {
            instance = new PicklistTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the PicklistTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized PicklistTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(PicklistTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof PicklistTopComponent) {
            return (PicklistTopComponent) win;
        }
        Logger.getLogger(PicklistTopComponent.class.getName()).warning(
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