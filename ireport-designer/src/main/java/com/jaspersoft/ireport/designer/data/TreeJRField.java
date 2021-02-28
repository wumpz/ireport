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
package com.jaspersoft.ireport.designer.data;

import net.sf.jasperreports.engine.design.JRDesignField;

/**
 * This class is used in extended datasource tree.
 * @author  Administrator
 */
public class TreeJRField
{
    private JRDesignField field= null;
    
    private Class obj = null;
   
    @Override
    public String toString()
    {
        if (field != null)
        {
            return field.getName() + " (" + this.getObj().getName() +")";
        }
        return "???";
    }
    
    /**
     * Getter for property field.
     * @return Value of property field.
     */
    public JRDesignField getField() {
        return field;
    }
    
    /**
     * Setter for property field.
     * @param field New value of property field.
     */
    public void setField(JRDesignField field) {
        this.field = field;
    }
    
    /**
     * Getter for property obj.
     * @return Value of property obj.
     */
    public java.lang.Class getObj() {
        return obj;
    }
    
    /**
     * Setter for property obj.
     * @param obj New value of property obj.
     */
    public void setObj(java.lang.Class obj) {
        this.obj = obj;
    }
    
}
