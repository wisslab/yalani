/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaiec.retro.data;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import org.openide.util.Exceptions;

/**
 *
 * @author kai
 * @param <E>
 */
public class PrefSerializer<E extends Serializable> {

    private java.util.prefs.Preferences prefs;
    private XMLEncoder encoder;
    private ByteArrayOutputStream baos;
    private XStream xstream;

    /**
     *
     */
    public PrefSerializer() {
        prefs = java.util.prefs.Preferences.userNodeForPackage(getClass());
        baos = new ByteArrayOutputStream();
        encoder = new XMLEncoder(baos);
        xstream = new XStream(new DomDriver());
    }

    private String encode(E item) {
//        encoder.flush();
//        baos.reset();
//        encoder.writeObject(item);
//        encoder.flush();
//        return baos.toString();
        return xstream.toXML(item);
    }

    private E decode(String input) {
//        InputStream bais = new ByteArrayInputStream(input.getBytes());
//        XMLDecoder decoder = new XMLDecoder(bais);
        try {
//            return decoder.readObject();
            return (E) xstream.fromXML(input);
        } catch (Throwable t) {
            System.err.println(t + ": " + t.getMessage());
            t.printStackTrace();
        }
        return null;
    }

    public void saveList(String name, List<E> list) {
        try {
            for (String key : prefs.keys()) {
                if (key.startsWith(name+"_")) {
                    prefs.remove(key);
                }
            }
            for (int i = 0; i < list.size(); i++) {
                prefs.put(name + "_" + i, encode(list.get(i)));
            }
        } catch (BackingStoreException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public List<E> loadList(String name) {
        List<E> res = new ArrayList<E>();
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
                    E o = decode(value);
                    if (o == null) {
                        continue;
                    }
                    res.add(i, o);

                }

            }
        } catch (BackingStoreException bse) {
            throw new RuntimeException("Error loading preferences: " + bse, bse);
        }
        return res;
    }

    public void saveObject(String name, E item) {
        prefs.put(name, encode(item));
    }

    public E loadObject(String name) {
        return decode(prefs.get(name, ""));
    }
}
