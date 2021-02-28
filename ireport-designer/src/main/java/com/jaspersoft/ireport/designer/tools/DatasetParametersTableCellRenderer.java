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

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRExpression;

/**
 *
 * @author gtoffoli
 */
public class DatasetParametersTableCellRenderer extends DefaultTableCellRenderer {
    
    /** Creates a new instance of TableCellRenderer */
    public DatasetParametersTableCellRenderer() {
        super();
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, 
                                            Object value,
                                            boolean isSelected,
                                            boolean hasFocus, int row, int column)
    {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,row, column);
        
        setIcon(null);
        
        if (value instanceof JRDatasetParameter && value != null)
        {
            setText( ((JRDatasetParameter)value).getName() );
        }
        else if (value instanceof JRExpression)
        {
            JRExpression exp = (JRExpression)value;
            if (exp == null) setText("");
            else if (exp.getText() == null) setText("");
            else setText(exp.getText());
        }
        
        return this;
    }
}
