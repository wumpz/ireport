/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2013 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of iReport.
 *
 * iReport is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * iReport is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with iReport. If not, see <http://www.gnu.org/licenses/>.
 */
package com.jaspersoft.ireport.designer.tools;


/**
 *
 * @author gtoffoli
 */

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 *
 * @author gtoffoli
 */
public class ColorsListCellRenderer extends DefaultListCellRenderer  {
           
    /** Creates a new instance of ColorsListCellRenderer */
    public ColorsListCellRenderer() {
        super();
    }
    
    
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if (value instanceof java.awt.Color)
        {
            java.awt.Color color = (java.awt.Color)value;
            
            label.setText( HexColorChooserPanel.getEncodedColor(color) );
            //"[" + color.getRed() + "," + color.getGreen() + "," +  color.getBlue()+ "]" 
            label.setIcon(new ColorIcon(color));
        }
        else
        {
            label.setIcon(new ColorIcon(java.awt.Color.WHITE));
        }
        
        //setBackground(isSelected ? selectionBackground : background);
        return label;
    }
   
}

