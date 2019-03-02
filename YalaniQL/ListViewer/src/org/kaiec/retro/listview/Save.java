/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaiec.retro.listview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.kaiec.retro.data.HibernateUtil;

public final class Save implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        HibernateUtil.getInstance().commitTransaction();
    }
}
