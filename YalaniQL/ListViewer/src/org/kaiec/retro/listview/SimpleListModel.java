/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaiec.retro.listview;

import java.util.List;
import javax.swing.AbstractListModel;

/**
 *
 * @author kai
 */
class SimpleListModel extends AbstractListModel {

    private List list;

    public SimpleListModel(List list) {
        this.list = list;
    }

    public Object getElementAt(int index) {
        return list.get(index);
    }

    public int getSize() {
        return list.size();
    }

    public void datachanged() {
        fireContentsChanged(this, 0, list.size() - 1);
    }
}
