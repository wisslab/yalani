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

package org.kaiec.retro.data;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author kai
 */
public class Preferences implements DatabaseSettings {
    private Logger log = Logger.getLogger(getClass().getName());
    private String databaseLocation;
    private String relativeDatabaseLocation;
    private String basedir;

    public Preferences() {
        log.setLevel(Level.FINE);
        basedir = getBasedir();
        log.info("Detected basedir: " + basedir);
        load();
    }
    
    private String getBasedir() {
        String s = getClass().getResource("Preferences.class").getPath();
        if (s.endsWith("modules\\org-kaiec-retro-data.jar!\\org\\kaiec\\retro\\data\\Preferences.class")) {
            s = s.substring(0,s.length()-"modules\\org-kaiec-retro-data.jar!\\org\\kaiec\\retro\\data\\Preferences.class".length());
            log.fine("Path detection, JAR URI, Windows");
        } else if (s.endsWith("modules/org-kaiec-retro-data.jar!/org/kaiec/retro/data/Preferences.class")) {
            s = s.substring(0,s.length()-"modules/org-kaiec-retro-data.jar!/org/kaiec/retro/data/Preferences.class".length());        
            log.fine("Path detection, JAR URI, UNIX");
        } else {
            log.fine("Path detection, Fallback");
            return new File("XXX").getAbsoluteFile().getParentFile().getAbsolutePath();
        }
        File f;
        try {
            f = new File(new URI(s));
        } catch (URISyntaxException ex) {
            throw new RuntimeException("Error in URI: " + s + "; " + ex, ex);
        }
        return f.getAbsolutePath();
    }



    public String getDatabaseLocation() {
        return databaseLocation;
    }

    public String getRelativeDatabaseLocation() {
        return relativeDatabaseLocation;
    }

    public void setDatabaseLocation(String databaseLocation) {
        String oldValue = this.databaseLocation;
        this.databaseLocation = databaseLocation;
        this.relativeDatabaseLocation = getRelativePath(databaseLocation, new File(".").getAbsolutePath(), null);
        firePropertyChangeEvent(DATABASE_LOCATION, oldValue, this.databaseLocation);
    }

     public void setRelativeDatabaseLocation(String relDatabaseLocation) {
        File f = new File(relDatabaseLocation);
        try {
            setDatabaseLocation(f.getCanonicalPath());
        } catch (IOException ex) {
            throw new RuntimeException("Path error: " + relDatabaseLocation + "; " + ex, ex);
        }
    }

   
    private final static String DATABASE_LOCATION = "databaseLocation";
    private final static String REL_DATABASE_LOCATION = "relDatabaseLocation";
    
    public void load() {
        // java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(getClass());
        java.util.Properties prefs = new Properties();
        try {
                prefs.load(new FileInputStream(new File(basedir + File.separator + "_prefs")));
        } catch (FileNotFoundException ex) {
                return;
                // throw new RuntimeException("Error loading preferences: " + ex, ex);
        } catch (IOException ex) {
                throw new RuntimeException("Error loading preferences: " + ex, ex);
        }
        setRelativeDatabaseLocation(prefs.getProperty(REL_DATABASE_LOCATION, "defaultdb" + File.separator + "defaultdb"));
    }

    public void save() {
        // java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(getClass());
        java.util.Properties prefs = new Properties();
        prefs.put(DATABASE_LOCATION, getDatabaseLocation());
        prefs.put(REL_DATABASE_LOCATION, getRelativeDatabaseLocation());
            try {
                prefs.store(new FileOutputStream(new File(basedir + File.separator + "_prefs")), "");
            } catch (IOException ex) {
            throw new RuntimeException("Error saving preferences: " + ex, ex);
            }
    }

    public String getConnectionUrl() {
        return "jdbc:h2:" + getDatabaseLocation();// + ";TRACE_LEVEL_FILE=3";
    }

    private List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.remove(listener);
    }
    private void firePropertyChangeEvent(String name, Object oldValue, Object newValue) {
        for (PropertyChangeListener listener:listeners) {
            listener.propertyChange(new PropertyChangeEvent(this, name, oldValue, newValue));
        }
    }
    
     public static String getRelativePath(String targetPath, String basePath, String pathSeparator) {
        if (pathSeparator==null) pathSeparator = File.separator;
        // Normalize the paths
        String normalizedTargetPath = FilenameUtils.normalizeNoEndSeparator(targetPath);
        String normalizedBasePath = FilenameUtils.normalizeNoEndSeparator(basePath);

        // Undo the changes to the separators made by normalization
        if (pathSeparator.equals("/")) {
            normalizedTargetPath = FilenameUtils.separatorsToUnix(normalizedTargetPath);
            normalizedBasePath = FilenameUtils.separatorsToUnix(normalizedBasePath);

        } else if (pathSeparator.equals("\\")) {
            normalizedTargetPath = FilenameUtils.separatorsToWindows(normalizedTargetPath);
            normalizedBasePath = FilenameUtils.separatorsToWindows(normalizedBasePath);

        } else {
            throw new IllegalArgumentException("Unrecognised dir separator '" + pathSeparator + "'");
        }

        String[] base = normalizedBasePath.split(Pattern.quote(pathSeparator));
        String[] target = normalizedTargetPath.split(Pattern.quote(pathSeparator));

        // First get all the common elements. Store them as a string,
        // and also count how many of them there are.
        StringBuffer common = new StringBuffer();

        int commonIndex = 0;
        while (commonIndex < target.length && commonIndex < base.length
                && target[commonIndex].equals(base[commonIndex])) {
            common.append(target[commonIndex] + pathSeparator);
            commonIndex++;
        }

        if (commonIndex == 0) {
            // No single common path element. This most
            // likely indicates differing drive letters, like C: and D:.
            // These paths cannot be relativized.
            throw new PathResolutionException("No common path element found for '" + normalizedTargetPath + "' and '" + normalizedBasePath
                    + "'");
        }   

        // The number of directories we have to backtrack depends on whether the base is a file or a dir
        // For example, the relative path from
        //
        // /foo/bar/baz/gg/ff to /foo/bar/baz
        // 
        // ".." if ff is a file
        // "../.." if ff is a directory
        //
        // The following is a heuristic to figure out if the base refers to a file or dir. It's not perfect, because
        // the resource referred to by this path may not actually exist, but it's the best I can do
        boolean baseIsFile = true;

        File baseResource = new File(normalizedBasePath);

        if (baseResource.exists()) {
            baseIsFile = baseResource.isFile();

        } else if (basePath.endsWith(pathSeparator)) {
            baseIsFile = false;
        }

        StringBuffer relative = new StringBuffer();

        if (base.length != commonIndex) {
            int numDirsUp = baseIsFile ? base.length - commonIndex - 1 : base.length - commonIndex;

            for (int i = 0; i < numDirsUp; i++) {
                relative.append(".." + pathSeparator);
            }
        }
        relative.append(normalizedTargetPath.substring(common.length()));
        return relative.toString();
    }


    static class PathResolutionException extends RuntimeException {
        PathResolutionException(String msg) {
            super(msg);
        }
    }    


}


