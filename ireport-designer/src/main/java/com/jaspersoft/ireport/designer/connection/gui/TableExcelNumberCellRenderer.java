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


package com.jaspersoft.ireport.designer.connection.gui;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author gtoffoli
 */
public class TableExcelNumberCellRenderer extends DefaultTableCellRenderer {

    static final String digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value != null && value instanceof Integer)
        {
            int val = ((Integer)value).intValue();

            String number = "" + digits.charAt(val%26);
            while (val > 0)
            {
                val = val/26;
                int index = (val%26)-1;

                if (val == 0) break;
                if (val%26 == 0)
                {
                    // We have to remove 26 from val and print a Z...
                    val-=26;
                    index = 25;
                }
                number = digits.charAt(index) + number;
            }

            label.setText(getText() + " (" + number + ")");
        }

        return label;
    }
}