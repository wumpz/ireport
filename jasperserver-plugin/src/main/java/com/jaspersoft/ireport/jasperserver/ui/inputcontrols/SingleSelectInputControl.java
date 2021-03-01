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

import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.impl.ListInputControlUI;
import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.impl.RadioListInputControlUI;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.InputControlQueryDataRow;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ListItem;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.util.List;

/**
 *
 * @author gtoffoli
 */
public class SingleSelectInputControl extends BasicInputControl{
    
    java.util.List wrappedItems = null;
    
    /** Creates a new instance of BooleanInputControl */
    public SingleSelectInputControl() {
        super();
    }
    
     public void setInputControl(ResourceDescriptor inputControl, List items) {
        
         this.inputControl = inputControl;
         
         String defaultNullLabel = "";
         
         if (inputControl.getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_LIST_OF_VALUES)
         {

            if (getInputControlUI() == null || !(getInputControlUI() instanceof ListInputControlUI))
            {
                setInputControlUI( new ListInputControlUI());
            }
         }
         else if (inputControl.getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY_RADIO ||
             inputControl.getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_LIST_OF_VALUES_RADIO)
         {

            if (getInputControlUI() == null || !(getInputControlUI() instanceof RadioListInputControlUI))
            {
                setInputControlUI( new RadioListInputControlUI());
                defaultNullLabel = "-None-";
            }

            if (getInputControlUI() instanceof RadioListInputControlUI)
            {
                defaultNullLabel = "-None-";
            }
         }
         
        String label = inputControl.getLabel() + ((inputControl.isMandatory()) ? "*" : "");
        getInputControlUI().setLabel(label);
        
        
        wrappedItems = new java.util.ArrayList();
        
        if (!inputControl.isMandatory())
        {
            wrappedItems.add(new ListItemWrapper(new ListItem(defaultNullLabel,null)));
        }
        
        for (int i=0; items != null && items.size()>i; ++i)
        {
            Object itemObject = items.get(i);
            if (itemObject instanceof ListItem)
            {
                ListItem item = (ListItem)itemObject;
                wrappedItems.add( new ListItemWrapper(item));
            }
            else if (itemObject instanceof InputControlQueryDataRow)
            {
                InputControlQueryDataRow qd =  (InputControlQueryDataRow)itemObject;
            
                List itemColumnValues = qd.getColumnValues();
                String text = "";
            
                for (int k=0; k<itemColumnValues.size(); ++k)
                {
                   if (k>0) text += " | ";
                   text += itemColumnValues.get(k);
                }
                
                wrappedItems.add( new ListItemWrapper(new ListItem(text, qd.getValue())));
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
