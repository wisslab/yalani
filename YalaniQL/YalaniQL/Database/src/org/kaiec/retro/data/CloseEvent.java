/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kaiec.retro.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kai
 */
public class CloseEvent {
    private List<CloseListener> listeners = new ArrayList<CloseListener>();

    private CloseEvent() {
    }


    public void addCloseListener(CloseListener listener) {
        listeners.add(listener);
    }
    public void removeCloseListener(CloseListener listener) {
        listeners.remove(listener);
    }

    public void closing() {
        System.out.println("Closing application...");
        RecordList.getInstance().saveBackup();
        EventLog.createEvent(EventLog.APPEND);
        for (CloseListener cl:listeners) {
            cl.closing();
        }
    }

    private static CloseEvent instance = new CloseEvent();
    public static CloseEvent getInstance() {
        return instance;
    }

}


