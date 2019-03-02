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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class ImportListAction implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        String file = DirectoryManager.getInstance().chooseFile("Listendatei auswählen", (Component) e.getSource());
        if (file!=null) {
            ListImporter importer = new ListImporter(file);
            importer.start();
            RecordList.getInstance().datachanged();
        }
    }
}
