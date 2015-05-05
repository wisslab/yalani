/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kaiec.retro.listview;

import java.awt.Color;
import java.io.Serializable;

/**
 *
 * @author kai
 */
public class ColorPattern implements Serializable {
    private Color color = Color.ORANGE;
    private String text = "";

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
}
