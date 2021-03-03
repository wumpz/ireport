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


package com.jaspersoft.ireport.components.sort.properties;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.UIManager;

/**
 *
 * @author gtoffoli
 */
public class SortFieldCellRenderer extends DefaultListCellRenderer {

    private static ImageIcon fieldIcon = null;
    private static ImageIcon variableIcon = null;

    public SortFieldCellRenderer()
    {
        if (fieldIcon == null)
        {
            fieldIcon = new ImageIcon( SortFieldCellRenderer.class.getResource("/com/jaspersoft/ireport/components/sort/properties/field-16.png"));
        }
        if (variableIcon == null)
        {
            variableIcon = new ImageIcon( SortFieldCellRenderer.class.getResource("/com/jaspersoft/ireport/components/sort/properties/variable-16.png"));
        }

    }




    @Override
    public Component getListCellRendererComponent(JList list, Object object, int index, boolean isSelected, boolean cellHasFocus) {



        setOpaque(true);
        setBackground( (isSelected || cellHasFocus) ? UIManager.getColor("List.selectionBackground") : UIManager.getColor("List.background"));
        

        if (object != null && object instanceof String)
        {
            String str = (String)object;
            if (str.startsWith("F"))
            {
                setForeground(Color.green.darker().darker());
                setText( str.substring(1) );
                setIcon( fieldIcon );
            }
            else if (str.startsWith("V"))
            {
                setForeground(Color.BLUE);
                setText( str.substring(1) );
                setIcon( variableIcon   );
            }
        }

        if (isSelected)
        {
            setForeground(UIManager.getColor("List.selectionForeground"));
        }
        return this;
    }
}
