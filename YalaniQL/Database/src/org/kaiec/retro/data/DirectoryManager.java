/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaiec.retro.data;

import java.awt.Component;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import javax.swing.JFileChooser;

/**
 *
 * @author kai
 */
public class DirectoryManager {

    private Map<String, String> directoryMap = new HashMap<String, String>();
    private String last;

    public String getDirectory(String key) {
        if (directoryMap.containsKey(key)) {
            return directoryMap.get(key);
        }
        return last;
    }

    public void setDirectory(String key, String value) {
        directoryMap.put(key, value);
        last = value;
    }

    private DirectoryManager() {
        load();
    }
    private static DirectoryManager instance;

    public static DirectoryManager getInstance() {
        if (instance == null) {
            instance = new DirectoryManager();
        }
        return instance;
    }

    public static void close() {
        if (instance == null) {
            return;
        }
        instance.save();
        instance = null;
    }

    public void load() {
        directoryMap.clear();
        java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(getClass());
        try {
            for (String key : prefs.keys()) {
                directoryMap.put(key, prefs.get(key, ""));
            }
            last = prefs.get("__LAST", "");
        } catch (BackingStoreException bse) {
            throw new RuntimeException("Error loading preferences: " + bse, bse);
        }
    }

    public void save() {
        java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(getClass());
        for (String key : directoryMap.keySet()) {
            prefs.put(key, directoryMap.get(key));
        }
        prefs.put("__LAST", last);
        try {
            prefs.flush();
        } catch (BackingStoreException bse) {
            throw new RuntimeException("Error saving preferences: " + bse, bse);
        }
    }

    public String chooseFile(String title, Component caller, String callerId) {
        return chooseFile(title, caller, callerId, null);
    }
    
    public String chooseFile(String title, Component caller, String callerId, final String ending) {
        String file = null;
        String dir = getDirectory(callerId);
        JFileChooser chooser = new JFileChooser(dir);
        chooser.setDialogTitle(title);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (ending!=null) {
            chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().toLowerCase().endsWith(ending);
                }

                @Override
                public String getDescription() {
                    return "*." + ending;
                }
            });
        }
        chooser.setAcceptAllFileFilterUsed(true);
        int returnVal = chooser.showOpenDialog(caller);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile().getAbsolutePath();

            try {
                setDirectory(callerId, chooser.getCurrentDirectory().getCanonicalPath());
                save();
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
        return file;
    }

    public String chooseFile(String title, String callerId) {
        return chooseFile(title, null, callerId);
    }


    public String chooseFile(String title, Component caller) {
        return chooseFile(title, caller, caller.getClass().getName());
    }
}
