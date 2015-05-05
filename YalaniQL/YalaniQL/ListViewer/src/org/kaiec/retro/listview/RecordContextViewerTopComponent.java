/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaiec.retro.listview;

import org.kaiec.retro.data.CloseListener;
import org.kaiec.retro.data.CloseEvent;
import java.awt.Desktop;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import org.kaiec.retro.data.EventLog;
import org.kaiec.retro.data.PrefSerializer;
import org.kaiec.retro.data.Record;
import org.openide.util.Exceptions;
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
@ConvertAsProperties(dtd = "-//org.kaiec.retro.listview//RecordContextViewer//EN",
autostore = false)
public final class RecordContextViewerTopComponent extends TopComponent {

    private static RecordContextViewerTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "RecordContextViewerTopComponent";
    private Lookup.Result<Record> result;
    private Record record;
    List<ExternalLink> links = new ArrayList<ExternalLink>();

    public RecordContextViewerTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(RecordContextViewerTopComponent.class, "CTL_RecordContextViewerTopComponent"));
        // setToolTipText(NbBundle.getMessage(RecordContextViewerTopComponent.class, "HINT_RecordContextViewerTopComponent"));
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
        links = ps.loadList("Links");
        System.out.println("Links loaded: " + links.size());
        if (links.isEmpty()) createDefaultLinks();
        jList1.setModel(new SimpleListModel(links));

        CloseEvent.getInstance().addCloseListener(new CloseListener() {

            public void closing() {
                ps.saveList("Links", links);
            }
        });
    }

    public void setRecord(Record record) {
        this.record = record;
        headerField.setText(record.getSignature() + ": " + record.getTitle());
        headerField.setCaretPosition(0);
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

    public static boolean browse(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                    return true;
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } catch (URISyntaxException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        String osName = System.getProperty("os.name");
        try {
            if (osName.startsWith("Mac OS")) {
                Class fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL",
                        new Class[]{String.class});
                openURL.invoke(null, new Object[]{url});
            } else if (osName.startsWith("Windows")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else { //assume Unix or Linux
                String[] browsers = {
                    "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++) {
                    if (Runtime.getRuntime().exec(
                            new String[]{"which", browsers[count]}).waitFor() == 0) {
                        browser = browsers[count];
                    }
                }
                if (browser == null) {
                    throw new Exception("Could not find web browser");
                } else {
                    Runtime.getRuntime().exec(new String[]{browser, url});
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        headerField = new javax.swing.JTextArea();
        addLinkButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        addLinkButton1 = new javax.swing.JButton();
        addLinkButton2 = new javax.swing.JButton();

        headerField.setColumns(20);
        headerField.setEditable(false);
        headerField.setLineWrap(true);
        headerField.setRows(5);
        headerField.setWrapStyleWord(true);
        jScrollPane1.setViewportView(headerField);

        addLinkButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/kaiec/retro/listview/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addLinkButton, org.openide.util.NbBundle.getMessage(RecordContextViewerTopComponent.class, "RecordContextViewerTopComponent.addLinkButton.text")); // NOI18N
        addLinkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLinkButtonActionPerformed(evt);
            }
        });

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
        jScrollPane2.setViewportView(jList1);

        addLinkButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/kaiec/retro/listview/edit.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addLinkButton1, org.openide.util.NbBundle.getMessage(RecordContextViewerTopComponent.class, "RecordContextViewerTopComponent.addLinkButton1.text")); // NOI18N
        addLinkButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLinkButton1ActionPerformed(evt);
            }
        });

        addLinkButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/kaiec/retro/listview/remove.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addLinkButton2, org.openide.util.NbBundle.getMessage(RecordContextViewerTopComponent.class, "RecordContextViewerTopComponent.addLinkButton2.text")); // NOI18N
        addLinkButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLinkButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addLinkButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addLinkButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addLinkButton2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addLinkButton)
                    .addComponent(addLinkButton1)
                    .addComponent(addLinkButton2))
                .addContainerGap(155, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void createDefaultLinks() {
        ExternalLink link;
        link = new ExternalLink("Amazon");
        links.add(link);
        link.setUrl("http://www.amazon.de/s/ref=nb_sb_noss?__mk_de_DE=%C5M%C5Z%D5%D1"
                + "&url=search-alias%3Daps"
                + "&field-keywords="
                + "##1##"
                + "+%22"
                + "##2##"
                + "%22&x=0&y=0");
        link.getReplacements().put("##1##", ExternalLink.FIRST_AUTHOR_LASTNAME);
        link.getReplacements().put("##2##", ExternalLink.TITLE);
        link.setEncoding("ISO8859-15");

        link = new ExternalLink("Google");
        links.add(link);
        link.setUrl("http://www.google.de/search?q=%22##1##%22");
        link.getReplacements().put("##1##", ExternalLink.TITLE);
        link.setEncoding("UTF-8");

        link = new ExternalLink("Verbund");
        links.add(link);
        link.setUrl("http://swb.bsz-bw.de/DB=2.1/PPNSET?PPN=##1##");
        link.getReplacements().put("##1##", ExternalLink.PPN);
        link.setEncoding("UTF-8");

        link = new ExternalLink("BVB (KVK)");
        links.add(link);
        link.setUrl("http://kvk.ubka.uni-karlsruhe.de/hylib-bin/kvk/"
                + "nph-kvk2.cgi?maske=kvk-last&lang=de"
                + "&title=KIT-Bibliothek%3A+Karlsruher+Virtueller+Katalog+KVK+%3A+Ergebnisanzeige"
                + "&head=http%3A%2F%2Fwww.ubka.uni-karlsruhe.de%2Fkvk%2Fkvk"
                + "%2Fkvk-kit-head-de-2010-11-08.html"
                + "&header=http%3A%2F%2Fwww.ubka.uni-karlsruhe.de%2F"
                + "kvk%2Fkvk%2Fkvk-kit-header-de-2010-11-08.html"
                + "&spacer=http%3A%2F%2Fwww.ubka.uni-karlsruhe.de%2Fkvk%2Fkvk"
                + "%2Fkvk-kit-spacer-de-2010-11-08.html&footer=http%3A%2F"
                + "%2Fwww.ubka.uni-karlsruhe.de%2Fkvk%2Fkvk%2Fkvk-kit-footer-de-"
                + "2010-11-08.html&css=none&input-charset=utf-8&kvk-session=O6UTRUI4&ALL=&"
                + "Timeout=10&TI=##1##&PY=&AU=&SB=&CI=&SS=&ST=&PU=&kataloge=BVB");
        link.getReplacements().put("##1##", ExternalLink.TITLE);
        link.setEncoding("ISO8859-15");

        link = new ExternalLink("Katalog");
        links.add(link);
        link.setUrl("https://aleph.bib.uni-mannheim.de/F"
                + "?func=find-c&ccl_term=WID%3D"
                + "##1##"
                + "&adjacent=N&local_base=MAN01");
        link.getReplacements().put("##1##", ExternalLink.PPN);
        link.setEncoding("ISO8859-15");

        link = new ExternalLink("Primo");
        links.add(link);
        link.setUrl("http://vs30.kobv.de/primo_library/libweb/action/search.do?dscnt=0"
                + "&vl%28159901251UI1%29=books&vl%2814779587UI0%29=lsr10&"
                + "scp.scps=scope%3A%28MAN%29%2Cscope%3A%28MAN_ALEPH%"
                + "29&frbg=&tab=default_tab&dstmp=1294161572155&srt="
                + "rank&ct=search&mode=Basic&dum=true&tb=t&indx=1&vl"
                + "%281UIStartWith0%29=contains&vl%28freeText0%29="
                + "##1##"
                + "&fn=search&vid=MAN_UB");

        link.getReplacements().put("##1##", ExternalLink.SIGNATUR);
        link.setEncoding("ISO8859-15");


    }

    private void openLink(ExternalLink link) {
        Record rec = getRecord();
        if (rec == null) {
            return;
        }
        browse(link.applyReplacements(rec));
    }
    
    private void addLinkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLinkButtonActionPerformed
        LinkConfigPanel myPanel = new LinkConfigPanel(new ExternalLink());
        NotifyDescriptor nd = new NotifyDescriptor(
                myPanel, // instance of your panel
                "Title", // title of the dialog
                NotifyDescriptor.OK_CANCEL_OPTION, // it is Yes/No dialog ...
                NotifyDescriptor.QUESTION_MESSAGE, // ... of a question type => a question mark icon
                null, // we have specified YES_NO_OPTION => can be null, options specified by L&F,
                // otherwise specify options as:
                //     new Object[] { NotifyDescriptor.YES_OPTION, ... etc. },
                NotifyDescriptor.OK_OPTION // default option is "Yes"
                );

        // let's display the dialog now...
        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
            links.add(myPanel.getUpdatedLink());
            ((SimpleListModel) jList1.getModel()).datachanged();
        }

    }//GEN-LAST:event_addLinkButtonActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        if (evt.getClickCount() == 2) {
            ExternalLink link = (ExternalLink) jList1.getSelectedValue();
            if (link != null && getRecord()!=null) {
                EventLog event = EventLog.createEvent(EventLog.LINK);
                event.setType2(link.getName());
                event.setExternalId(getRecord().getExternalId());
                openLink(link);
            }
        }
    }//GEN-LAST:event_jList1MouseClicked

    private void addLinkButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLinkButton1ActionPerformed
        ExternalLink link = (ExternalLink) jList1.getSelectedValue();
        if (link != null) {
            LinkConfigPanel myPanel = new LinkConfigPanel(link);
            NotifyDescriptor nd = new NotifyDescriptor(
                    myPanel, // instance of your panel
                    "Title", // title of the dialog
                    NotifyDescriptor.OK_CANCEL_OPTION, // it is Yes/No dialog ...
                    NotifyDescriptor.QUESTION_MESSAGE, // ... of a question type => a question mark icon
                    null, // we have specified YES_NO_OPTION => can be null, options specified by L&F,
                    // otherwise specify options as:
                    //     new Object[] { NotifyDescriptor.YES_OPTION, ... etc. },
                    NotifyDescriptor.OK_OPTION // default option is "Yes"
                    );

            // let's display the dialog now...
            if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
                myPanel.getUpdatedLink();
                ((SimpleListModel) jList1.getModel()).datachanged();
            }
        }
    }//GEN-LAST:event_addLinkButton1ActionPerformed

    private void addLinkButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLinkButton2ActionPerformed
        ExternalLink link = (ExternalLink) jList1.getSelectedValue();
        links.remove(link);
        ((SimpleListModel) jList1.getModel()).datachanged();
    }//GEN-LAST:event_addLinkButton2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addLinkButton;
    private javax.swing.JButton addLinkButton1;
    private javax.swing.JButton addLinkButton2;
    private javax.swing.JTextArea headerField;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized RecordContextViewerTopComponent getDefault() {
        if (instance == null) {
            instance = new RecordContextViewerTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the RecordContextViewerTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized RecordContextViewerTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(RecordContextViewerTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof RecordContextViewerTopComponent) {
            return (RecordContextViewerTopComponent) win;
        }
        Logger.getLogger(RecordContextViewerTopComponent.class.getName()).warning(
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

class SimpleListModel extends AbstractListModel {

    private List list;

    public SimpleListModel(List list) {
        this.list = list;
    }

    public Object getElementAt(int index) {
        return list.get(index);
    }

    public int getSize() {
        return list.size();
    }

    public void datachanged() {
        fireContentsChanged(this, 0, list.size() - 1);
    }
}
