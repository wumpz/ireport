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

import com.jaspersoft.ireport.designer.ModelUtils;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignPropertyExpression;

/**
 *
 * @author gtoffoli
 */
public class GenericProperty {

    private String key = null;
    private Object value = null;
    private boolean useExpression = false;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isUseExpression() {
        return useExpression;
    }

    public void setUseExpression(boolean useExpression) {
        this.useExpression = useExpression;
    }
    
    public GenericProperty(String key, Object value)
    {
        this.key = key;
        this.value = value;
    }
    
    public GenericProperty(String key, JRDesignExpression exp)
    {
        this.key = key;
        this.value = ModelUtils.cloneExpression(exp);
        this.useExpression = true;
    }
    public GenericProperty()
    {
        this.key = "";
    }
    
    public JRDesignExpression getExpression()
    {
        return (value instanceof JRDesignExpression) ? (JRDesignExpression)value : null;
    }
    
    public JRDesignPropertyExpression toPropertyExpression()
    {
        JRDesignPropertyExpression pp = new JRDesignPropertyExpression();
        pp.setName(key);
        pp.setValueExpression(getExpression());
        return pp;
    }
    
    @Override
    public String toString()
    {
        return getKey();
    }
    
}
