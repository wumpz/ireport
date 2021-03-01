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
package com.jaspersoft.ireport.jasperserver.ui.inputcontrols;

import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.impl.CheckboxListInputControlUI;
import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.impl.TableListInputControlUI;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.InputControlQueryDataRow;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ListItem;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gtoffoli
 */
public class MultiSelectInputControl extends BasicInputControl{
    
    java.util.List wrappedItems = null;
    
    /** Creates a new instance of BooleanInputControl */
    public MultiSelectInputControl() {
        super();
    }
    
     public void setInputControl(ResourceDescriptor inputControl, List items) {
        
         this.inputControl = inputControl;
         
         boolean useCheckboxes = false;
         
         if (inputControl.getControlType() == ResourceDescriptor.IC_TYPE_MULTI_SELECT_LIST_OF_VALUES ||
             inputControl.getControlType() == ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY)
         {
            useCheckboxes = false;
            if (getInputControlUI() == null || !(getInputControlUI() instanceof TableListInputControlUI))
            {
                setInputControlUI( new TableListInputControlUI());
            }
         }
         else if (inputControl.getControlType() == ResourceDescriptor.IC_TYPE_MULTI_SELECT_LIST_OF_VALUES_CHECKBOX ||
             inputControl.getControlType() == ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY_CHECKBOX)
         {
            useCheckboxes = true;
            if (getInputControlUI() == null || !(getInputControlUI() instanceof CheckboxListInputControlUI))
            {
                setInputControlUI( new CheckboxListInputControlUI());
            }
         }
         
         String label = inputControl.getLabel() + ((inputControl.isMandatory()) ? "*" : "");
         getInputControlUI().setLabel(label);
        
        
        wrappedItems = new java.util.ArrayList();
        
        for (int i=0; items != null && items.size()>i; ++i)
        {
            Object itemObject = items.get(i);
            if (itemObject instanceof ListItem)
            {
                ListItem item = (ListItem)itemObject;
                if (useCheckboxes)
                {
                    wrappedItems.add( new ListItemWrapper(item));
                }
                else
                {
                    InputControlQueryDataRow icqd = new InputControlQueryDataRow();
                    icqd.setValue(item.getValue() );
                    List cols = new ArrayList();
                    cols.add(item.getLabel());
                    icqd.setColumnValues( cols );
                    wrappedItems.add( icqd );
                }
            }
            else if (itemObject instanceof InputControlQueryDataRow)
            {
                InputControlQueryDataRow qd =  (InputControlQueryDataRow)itemObject;
            
                if (useCheckboxes)
                {
                    List itemColumnValues = qd.getColumnValues();
                    String text = "";
            
                    for (int k=0; k<itemColumnValues.size(); ++k)
                    {
                        if (k>0) text += " | ";
                        text += itemColumnValues.get(k);
                    }
                    wrappedItems.add( new ListItemWrapper(new ListItem(text, qd.getValue())));
                }
                else
                {
                    wrappedItems.add(qd);
                }
            }
           
        }
        
        getInputControlUI().setHistory( wrappedItems );
        getInputControlUI().setReadOnly( inputControl.isReadOnly() );
    
        List history = getHistory(inputControl.getUriString());
     
        if (history != null && history.size() > 0)
        {
            getInputControlUI().setValue( history.get(0) );
        }
     }
     
     public Object validate() throws InputValidationException
     {
        return getInputControlUI().getValue();
     }
     
}
