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
package com.jaspersoft.ireport.designer.errorhandler;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author gtoffoli
 */
public class ProblemIconTableCellRenderer extends DefaultTableCellRenderer {
    
    static ImageIcon errorIcon;
    static ImageIcon infoIcon;
    static ImageIcon warningIcon;
    
    public ProblemIconTableCellRenderer()
    {
        super();
        if (errorIcon == null) errorIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/errorhandler/error.png"));
        if (infoIcon == null) infoIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/errorhandler/information.png"));
        if (warningIcon == null) warningIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/errorhandler/warning.png"));
    } 

    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        java.awt.Component retValue;
        
        retValue = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (value != null && value instanceof ProblemItem)
        {
            ProblemItem pi = (ProblemItem)value;
            ((JLabel)retValue).setText("");
            if (pi.getProblemType() == ProblemItem.ERROR) ((JLabel)retValue).setIcon( errorIcon );
            if (pi.getProblemType() == ProblemItem.INFORMATION) ((JLabel)retValue).setIcon( infoIcon );
            if (pi.getProblemType() == ProblemItem.WARNING) ((JLabel)retValue).setIcon( warningIcon );
            
        }
        
        return retValue;
    }
}
