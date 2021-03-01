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
package com.jaspersoft.ireport.jasperserver.ui.inputcontrols.impl;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.InputControlQueryDataRow;


/**
 *
 * @author gtoffoli
 */
public class MultiColumnListInputControlUI extends BasicInputControlUI {
    
    /**
     * Creates a new instance of ListInputControlUI
     */
    public MultiColumnListInputControlUI() {
        super();
        setComboEditable(false);
        getJComboBoxValue().setMinimumSize(new java.awt.Dimension(400,26));
        //getJComboBoxValue().setPreferredSize(new java.awt.Dimension(400,30));
    }
    
    public void setHistory(java.util.List values){
        
        getJComboBoxValue().removeAllItems();
        if (values == null) return;
        
        // Try to understand how much columns...
        int maxColumns = 1;
        for (int i=0; i<values.size(); ++i)
        {
            InputControlQueryDataRow qd =  (InputControlQueryDataRow)values.get(i);
            maxColumns = (qd.getColumnValues().size()>maxColumns) ? qd.getColumnValues().size() : maxColumns;
        }
        //System.out.println("ItemRenderer set to" + maxColumns );
        getJComboBoxValue().setRenderer(new ItemRenderer(maxColumns));
        
        for (int i=0; i<values.size(); ++i)
        {
            getJComboBoxValue().addItem( values.get(i));
        }
        
        if (getJComboBoxValue().getItemCount() > 0)
        {
            getJComboBoxValue().setSelectedIndex(0);
        }
        
        getJComboBoxValue().updateUI();
    }
    
    public void setValue(Object v)
    {
        for (int i=0; i<getJComboBoxValue().getItemCount(); ++i)
        {
            Object val = getJComboBoxValue().getItemAt(i);
            
            if (val instanceof InputControlQueryDataRow)
            {
                val = ((InputControlQueryDataRow)val).getValue();
                if ( ((val == null) ? val == v : val.equals(v)) )
                {
                    getJComboBoxValue().setSelectedIndex(i);
                    return;
                }
            }
        }
        
        getJComboBoxValue().setSelectedItem(v);
    }
     
     public Object getValue()
    {
        Object val = getJComboBoxValue().getSelectedItem();
        if (val == null) return null;
        if (val instanceof InputControlQueryDataRow)
        {
            return ((InputControlQueryDataRow)val).getValue();
        }
        
        return val;
    }
     
     
     
}
