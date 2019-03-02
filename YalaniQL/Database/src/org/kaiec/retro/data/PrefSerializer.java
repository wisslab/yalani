/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaiec.retro.data;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import org.openide.util.Exceptions;

/**
 *
 * @author kai
 */
public class PrefSerializer {

    private java.util.prefs.Preferences prefs;
    private XMLEncoder encoder;
    private ByteArrayOutputStream baos;
    private XStream xstream;

    public PrefSerializer(Class clazz) {
        prefs = java.util.prefs.Preferences.userNodeForPackage(getClass());
        baos = new ByteArrayOutputStream();
        encoder = new XMLEncoder(baos);
        xstream = new XStream(new DomDriver());
    }

    private String encode(Serializable item) {
//        encoder.flush();
//        baos.reset();
//        encoder.writeObject(item);
//        encoder.flush();
//        return baos.toString();
        return xstream.toXML(item);
    }

    private Object decode(String input) {
//        InputStream bais = new ByteArrayInputStream(input.getBytes());
//        XMLDecoder decoder = new XMLDecoder(bais);
        try {
//            return decoder.readObject();
            return xstream.fromXML(input);
        } catch (Throwable t) {
            System.err.println(t + ": " + t.getMessage());
            t.printStackTrace();
        }
        return null;
    }

    public void saveList(String name, List list) {
        try {
            for (String key : prefs.keys()) {
                if (key.startsWith(name+"_")) {
                    prefs.remove(key);
                }
            }
            for (int i = 0; i < list.size(); i++) {
                prefs.put(name + "_" + i, encode((Serializable) list.get(i)));
            }
        } catch (BackingStoreException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public List loadList(String name) {
        List<Serializable> res = new ArrayList<Serializable>();
        try {
            for (String key : prefs.keys()) {
                if (key.startsWith(name)) {
                    String value = prefs.get(key, "");
                    if (value.equals("")) {
                        continue;
                    }
                    int i = -1;
                    try {
                        i = Integer.parseInt(key.substring(name.length() + 1));
                    } catch (NumberFormatException nfe) {
                    }
                    if (i == -1) {
                        continue;
                    }
                    // System.out.println("Decoding: " + key + "(" + i + "): " + value);
                    Object o = decode(value);
                    if (o == null) {
                        continue;
                    }
                    res.add(i, (Serializable) o);

                }

            }
        } catch (BackingStoreException bse) {
            throw new RuntimeException("Error loading preferences: " + bse, bse);
        }
        return res;
    }

    public void saveObject(String name, Serializable item) {
        prefs.put(name, encode(item));
    }

    public Serializable loadObject(String name) {
        return (Serializable) decode(prefs.get(name, ""));
    }
}
