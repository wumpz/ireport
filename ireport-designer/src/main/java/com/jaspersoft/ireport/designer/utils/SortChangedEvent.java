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
package com.jaspersoft.ireport.designer.utils;

import org.jdesktop.swingx.decorator.SortOrder;

/**
 *
 * @author  Administrator
 */
public class SortChangedEvent {
       
    
    private Object source = null;
    private int sortColumn = -1;
    private SortOrder sortType = SortOrder.UNSORTED;

    /**
     * Get the sorted column.
     * UNSORTED means no column is sorted currently
     * @return the sorted column
     */ 
    public int getSortColumn() {
        return sortColumn;
    }

    /**
     * Sort type can be 
     * UP, DOWN or UNSORTED
     * @return the sort type
     */ 
    public SortOrder getSortType() {
        return sortType;
    }
    
    
    /** Creates a new instance of ValueChangedEvent 
     * @param source 
     * @param sortColumn 
     * @param sortType 
     */
    public SortChangedEvent(Object source, int sortColumn, SortOrder sortType) {
        
        this.source = source;
        this.sortColumn = sortColumn;
        this.sortType = sortType;
    }
    
    /** Getter for property source.
     * @return Value of property source.
     *
     */
    public Object getSource() {
        return source;
    }
    
    /** Setter for property source.
     * @param source New value of property source.
     *
     */
    public void setSource(Object source) {
        this.source = source;
    }
    
     
}
