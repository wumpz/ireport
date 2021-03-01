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
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ListItem;
/**
 *
 * @author gtoffoli
 */
public class ListItemWrapper implements Comparable {
    
    private ListItem item;
    /**
     * Creates a new instance of ListItemWrapper
     */
    public ListItemWrapper(ListItem item  ) {
        this.item = item;
    }
    
    public boolean equals(Object obj)
    {
        try {
        if (obj instanceof ListItem)
        {
            Object val = ((ListItem)obj).getValue();
            return (val == null) ? getItem().getValue() == null : val.equals(getItem().getValue());
        }
        if (obj instanceof ListItemWrapper)
        {
            Object val = ((ListItemWrapper)obj).getItem().getValue();
            return (val == null) ? getItem().getValue() == null : val.equals(getItem().getValue());
        }
        } catch (Exception ex)
        {
            
        }
        
        return super.equals(obj);
    }

    public ListItem getItem() {
        return item;
    }

    public void setItem(ListItem item) {
        this.item = item;
    }
    
    public String toString()
    {
        if (item != null) return ""+item.getLabel();
        return super.toString();
    }

    public int compareTo(Object o) {
        if (o instanceof ListItemWrapper)
        {
            ListItem newItem = ((ListItemWrapper)o).getItem();
            return item.getLabel().compareTo(newItem.getLabel());
        }
        return -1;
    }
}
