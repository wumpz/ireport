/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.transformer.tool;

import com.jaspersoft.ireport.addons.transformer.Transformer;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @version $Id: TransformerCellRenderer.java 0 2010-05-31 18:50:22 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class TransformerCellRenderer extends DefaultListCellRenderer{

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof Transformer)
        {
            setText( ((Transformer)value).getName() ) ;
        }

        return this;
    }




}
