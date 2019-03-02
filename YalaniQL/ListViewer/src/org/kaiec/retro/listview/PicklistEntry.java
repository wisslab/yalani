/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kaiec.retro.listview;

import java.io.Serializable;
import java.util.regex.Pattern;
import org.kaiec.retro.data.ImportRVKDescs;

/**
 *
 * @author kai
 */
public class PicklistEntry implements Serializable {
    private String value;
    Pattern p;


    public PicklistEntry(String value) {
        this.value = value;
       
    }

    public PicklistEntry() {
        this("");
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        tostring = null;
    }

    private String tostring;

    @Override
    public String toString() {
        if (tostring!=null) return tostring;
        if (value==null) return "";
        if (p==null) {
             p = Pattern.compile("[A-Z]{1,2} [0-9]+");
        }
        if (p.matcher(value).matches()) {
            tostring = value + ": " + ImportRVKDescs.getDescription(value);
            return tostring;
        }
        tostring = value;
        return tostring;
    }
    
}
