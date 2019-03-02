/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kaiec.retro.listview;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.kaiec.retro.data.Record;
import org.openide.util.Exceptions;

/**
 *
 * @author kai
 */
public class ExternalLink implements Serializable {
    private String name;
    private String url;
    private String encoding;
    private Map<String,String> replacements = new HashMap<String, String>();

    public static final String TITLE = "Titel";
    public static final String PPN = "PPN";
    public static final String SIGNATUR = "Signatur";
    public static final String KEYWORDS = "Schlagwörter";
    public static final String FIRST_AUTHOR_LASTNAME = "Erstautor (Nachn.)";


    public static final String MARKER1 = "##1##";
    public static final String MARKER2 = "##2##";

    public ExternalLink(String name) {
        this.name = name;
    }

    public ExternalLink() {
    }

    

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getReplacements() {
        return replacements;
    }

    public void setReplacements(Map<String, String> replacements) {
        this.replacements = replacements;
    }

    
    @Override
    public String toString() {
        return getName();
    }


    private String replace(String source, String marker, Record rec) {
            if (getReplacements().containsKey(marker)) {
            try {
                String value;
                String rep = getReplacements().get(marker);
                if (rep == null) {
                    return source;
                } else if (rep.equals(TITLE)) {
                    value = rec.getTitle();
                } else if (rep.equals(KEYWORDS)) {
                    value = rec.getSubjects();
                } else if (rep.equals(PPN)) {
                    value = rec.getPpn();
                } else if (rep.equals(SIGNATUR)) {
                    value = rec.getSignature();
                } else if (rep.equals(FIRST_AUTHOR_LASTNAME)) {
                    value = rec.getFirstAuthorLastname();
                } else {
                    return source;
                }
                source = source.replaceAll(marker, URLEncoder.encode(value, getEncoding()));
            } catch (UnsupportedEncodingException ex) {
                Exceptions.printStackTrace(ex);
            }
            }

            return source;
    }

    public String applyReplacements(Record rec) {
        String res = getUrl();
        res = replace(res, MARKER1, rec);
        res =  replace(res, MARKER2, rec);
        return res;
    }
    
}
