/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2009 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.ireport.designer.data;

import java.awt.Color;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import org.springframework.ui.jasperreports.JasperReportsUtils;

/**
 *
 * @author gtoffoli
 */
public class SortFieldCellRenderer extends DefaultListCellRenderer {
    
    static ImageIcon ascIcon;
    static ImageIcon descIcon;
    
    /** Creates a new instance of SortFieldCellRenderer */
    public SortFieldCellRenderer() {
        if (ascIcon == null) ascIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/data/arrow_up.png"));
        if (descIcon == null) descIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/data/arrow_down.png"));
    }

    @Override
    public java.awt.Component getListCellRendererComponent(javax.swing.JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        java.awt.Component retValue;
        
        retValue = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if (value instanceof JRDesignSortField && retValue instanceof JLabel)
        {
            JRDesignSortField sf = (JRDesignSortField)value;
            JLabel label = (JLabel)retValue;

            String t = sf.getType() == SortFieldTypeEnum.FIELD ? "Field" : "Variable";

            Color cf = Color.green.darker().darker();
            Color cv = Color.BLUE;
            String c = sf.getType() == SortFieldTypeEnum.FIELD ? getEncodedColor(cf) : getEncodedColor(cv);

            String text = "<html>" + sf.getName() + " " + ((isSelected) ? t : "<font color=\"" + c + "\">" + t + "</font>");


            label.setText( text);
            
            label.setIcon( sf.getOrderValue() == SortOrderEnum.DESCENDING ? descIcon : ascIcon );
        }
        
        return retValue;
    }
    
    
    public static String getEncodedColor(java.awt.Color c) {
        String nums = "0123456789ABCDEF";
        String s = "#";
        s += nums.charAt( c.getRed()/16 );
        s += nums.charAt( c.getRed()%16 );
        s += nums.charAt( c.getGreen()/16 );
        s += nums.charAt( c.getGreen()%16 );
        s += nums.charAt( c.getBlue()/16 );
        s += nums.charAt( c.getBlue()%16 );
        return s;
    }


}
