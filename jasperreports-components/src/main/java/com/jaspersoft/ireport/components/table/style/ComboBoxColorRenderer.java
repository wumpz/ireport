/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.table.style;

import com.jaspersoft.ireport.designer.utils.ColorSchemaGenerator;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import org.openide.util.ImageUtilities;

/**
 *
 * @version $Id: ComboBoxColorRenderer.java 0 2010-04-07 20:20:49 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class ComboBoxColorRenderer extends DefaultListCellRenderer {

    BufferedImage image = null;
    public ComboBoxColorRenderer()
    {
        image = new BufferedImage(20, 10, BufferedImage.TYPE_INT_ARGB);
        image.createGraphics();
    }


    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        label.setIcon(null);
        Color c = ColorSchemaGenerator.getColor(""+value);
        if (c != null)
        {
            Graphics2D g = (Graphics2D) image.getGraphics();
            g.setColor(c);
            g.fillRect(0,0,20,10);
            g.setColor(Color.BLACK);
            g.drawRect(0,0,19,9);

            label.setIcon(ImageUtilities.image2Icon(image));
        }

        return label;
    }

}
