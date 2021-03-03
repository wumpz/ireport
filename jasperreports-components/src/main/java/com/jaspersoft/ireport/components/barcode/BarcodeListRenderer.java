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
package com.jaspersoft.ireport.components.barcode;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;


/**
 *
 * @author gtoffoli
 */
public class BarcodeListRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null && value instanceof BarcodeDescriptor)
        {
            BarcodeDescriptor bd = (BarcodeDescriptor)value;
            label.setIcon(bd.getIcon());
            //label.setHorizontalTextPosition(JLabel.LEFT);
            String desc = "";
//            if (bd.getDescription() != null &&
//                bd.getDescription().length() > 0)
//            {
//                desc = "<br><font color=\"lightGray\">" + bd.getDescription() + "</font>";
//            }
            label.setText("<html>" + bd.getName() + desc);
            label.setAlignmentX( JLabel.TOP_ALIGNMENT);
        }

        return label;
    }

}
