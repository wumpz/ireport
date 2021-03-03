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
package com.jaspersoft.ireport.components.map;

import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import net.sf.jasperreports.components.map.Item;
import net.sf.jasperreports.components.map.ItemProperty;

/**
 *
 * @author gtoffoli
 */
public class MarkerListCellRenderer extends DefaultListCellRenderer {

    
    public static final ImageIcon markerIcon = new ImageIcon( MarkerListCellRenderer.class.getClassLoader().getResource("/com/jaspersoft/ireport/components/map/map-16.png")  );
    
    @Override
    public Component getListCellRendererComponent(
         JList list,
         Object value,
         int index,
         boolean isSelected,
         boolean cellHasFocus)
     {
         JLabel label = (JLabel)super.getListCellRendererComponent(list,value,index,isSelected, cellHasFocus);
         label.setIcon(markerIcon);
         
         if (value instanceof Item)
         {
             Item marker = (Item)value;
             
             String displayName = "";
             String latitude = "";
             String longitude = "";
             // Find propertiess...
             for (ItemProperty p : marker.getProperties())
             {
                 if (p.getName().equals("latitude"))
                 {
                     latitude = "" + (p.getValue() == null ? Misc.getExpressionText( p.getValueExpression() ) : p.getValue());
                 }
                 if (p.getName().equals("longitude"))
                 {
                     longitude = "" + (p.getValue() == null ? Misc.getExpressionText( p.getValueExpression() ) : p.getValue());
                 }
             }
             
             label.setText( "Lat: " + latitude + ", Lon: " + longitude);
         }
        
         
         return this;
     }
}