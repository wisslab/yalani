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
package org.kaiec.retro.listview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import org.openide.util.Exceptions;

public final class ListUIColors implements ActionListener {

    public void actionPerformed(ActionEvent e) {
         LookAndFeelInfo[] lookAndFeelInfos = UIManager
                .getInstalledLookAndFeels();
        for (LookAndFeelInfo lookAndFeelInfo : lookAndFeelInfos) {
            try {
                System.out.println("LookAndFeel: " + lookAndFeelInfo.getName());
                LookAndFeel lookAndFeel = (LookAndFeel) Class.forName(
                    lookAndFeelInfo.getClassName()).newInstance();
                UIDefaults defaults = lookAndFeel.getDefaults();
                for (Map.Entry<Object, Object> entry : defaults.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
                System.out.println("######################################");
            } catch (InstantiationException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalAccessException ex) {
                Exceptions.printStackTrace(ex);
            } catch (ClassNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

    }
}
