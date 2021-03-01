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

import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.ListItemWrapper;




/**
 *
 * @author gtoffoli
 */
public class ListInputControlUI extends BasicInputControlUI {
    
    /**
     * Creates a new instance of ListInputControlUI
     */
    public ListInputControlUI() {
        setComboEditable(false);
    }
    
     public void setValue(Object v)
    {
        for (int i=0; i<getJComboBoxValue().getItemCount(); ++i)
        {
            Object val = getJComboBoxValue().getItemAt(i);
            
            if (val instanceof ListItemWrapper)
            {
                val = ((ListItemWrapper)val).getItem().getValue();
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
        if (val instanceof ListItemWrapper)
        {
            return ((ListItemWrapper)val).getItem().getValue();
        }
        
        return val;
    }
}
