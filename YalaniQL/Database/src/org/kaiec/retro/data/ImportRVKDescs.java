/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaiec.retro.data;

import au.com.bytecode.opencsv.CSVReader;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Exceptions;

public final class ImportRVKDescs implements ActionListener {

    private Map<String,Long> stopwords = new HashMap<String, Long>();

    private void doTheAction(ProgressHandle ph, String file) {
        try {
            System.out.println("Converting: " + file);
            ph.start(820000);
            CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file), "UTF-8"), '\t', Character.MIN_VALUE, false);
            String[] nextLine;
            // writer.setProperty("showXmlDeclaration","true");
            // writer.setProperty("tab","8");
            // writer.setProperty("relativeURIs","same-document,relative");


            int count = 0;

            Connection conn = HibernateUtil.getInstance().getSession().disconnect();
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO RVK (notation, description) VALUES (?,?)");
            conn.setAutoCommit(true);
            while ((nextLine = reader.readNext()) != null) {
                String notation = nextLine[0].trim();
                String description = nextLine[1].trim();
                StringTokenizer st = new StringTokenizer(description, " ");
                while (st.hasMoreElements()) {
                    String word = st.nextToken();
                    if (stopwords.containsKey(word)) stopwords.put(word, stopwords.get(word)+1);
                    else stopwords.put(word, 1L);
                }
                pstmt.setString(1, notation);
                pstmt.setString(2, description);
                pstmt.executeUpdate();
                ph.progress("#" + count++ + ": " + notation, count);
            }
            System.out.println("Finished!");
            ph.finish();
            for (String key:stopwords.keySet()) {
                if (stopwords.get(key)>10) {
                    System.out.println(key + ": " + stopwords.get(key));
                }
            }
            reader.close();
            HibernateUtil.getInstance().reconnect();
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void actionPerformed(ActionEvent e) {



        final String file = DirectoryManager.getInstance().chooseFile("Beschreibungsdatei auswählen", (Component) e.getSource());
        if (file==null) {
            return;
        }



                final ProgressHandle progressHandle = ProgressHandleFactory.createHandle("RVK Descs");

        new Thread(new Runnable() {

            @Override
            public void run() {
                doTheAction(progressHandle, file);
            }
        }, "RVK Descs").start();

    
    }

    public static String getDescription(String notation) {
        String desc = "";
        try {
            Query q = HibernateUtil.getInstance().getSession().createSQLQuery("SELECT description from RVK where notation=:not");
            q.setString("not", notation);
            Clob clob = (Clob) q.uniqueResult();
            if (clob!=null) {
                desc = clob.getSubString(1, (int) clob.length());
            }

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
        return desc;
    }

    public static List<String> getRVKs(String text) {
            List<String> res = new ArrayList<String>();
            String query = RecreateDatabase.getFulltextQuery("text");
            if (query==null) return res;
            Query q = HibernateUtil.getInstance().getSession().createSQLQuery(query);
            q.setString("text", text);
            res.addAll(q.list());

        return res;

    }

    public static String removeStopwords(String input) {
        for (String sw:getStopwords()) {
            input = input.replaceAll(" " + sw + " ", " ");
        }
        return input;
    }

    public static List<String> swlist = null;

    private static List<String> getStopwords() {
        try {
            if (swlist != null) {
                return swlist;
            }
            swlist = new ArrayList<String>();
            BufferedReader r = new BufferedReader(new InputStreamReader(new ImportRVKDescs().getClass().getResourceAsStream("stopwords_de.txt")));
            String line = null;
            while ((line = r.readLine()) != null) {
                swlist.add(line);
            }
            return swlist;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

}
