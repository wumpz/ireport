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
package com.jaspersoft.ireport.designer.sheet;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author gtoffoli
 */
public class Tag {
    
    private Object value;
    private String name = "";
    
    public Tag(Object value, String name) {
        setName( name );
        setValue(value);
    }
    
    public Tag(String value) {
        setName( value );
        setValue(value);
    }
    
    public Tag(Object value) {
        setName( value+"");
        setValue(value);
    }
    
    public Tag(int value, String name) {
        setName( name );
        setValue(value);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
    public void setValue(int value) {
        this.value = ""+value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String toString()
    {
        return getName();
    }
    
    /**
     * Look for the first tag with the specified name.
     */
    public static final Tag findTagByName(String name, Collection c)
    {
            if (c == null) return null;
            Iterator i = c.iterator();
            while (i.hasNext())
            {
                Tag t = (Tag)i.next();
                if ( (name == null && t.getName() == null) ||
                     (t.getName() != null && t.getName().equals(name)) )
                {
                    return t;
                }
            }
            return null;
    }
    
    /**
     * Look for the first tag with the specified name.
     */
    public static final Tag findTagByValue(Object value, Collection c)
    {
            if (c == null) return null;
            Iterator i = c.iterator();
            while (i.hasNext())
            {
                Tag t = (Tag)i.next();
                if ( (value == null && t.getValue() == value) ||
                     (t.getValue() != null && t.getValue().equals(value)) )
                {
                    return t;
                }
            }
            return null;
    }
    
}
