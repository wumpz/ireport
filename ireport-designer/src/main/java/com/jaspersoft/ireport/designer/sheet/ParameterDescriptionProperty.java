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

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;

/**
 *
 * @author gtoffoli
 */
public class ParameterDescriptionProperty extends PropertySupport.ReadWrite {

    JRDesignParameter parameter = null;
    JRDesignDataset dataset = null;
    
    @SuppressWarnings("unchecked")
    public ParameterDescriptionProperty(JRDesignParameter parameter, JRDesignDataset dataset)
    {
        super("description", String.class,
              "Description",
              "Description");
        this.parameter = parameter;
        this.dataset = dataset;
        
    }
    
    public boolean canWrite()
    {
        return !getParameter().isSystemDefined();
    }
    
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return parameter.getDescription() == null ? "" : parameter.getDescription();
    }

    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        
        
        if (val == null || val.equals(""))
        {
            parameter.setDescription(null);
        }
        
        String s = val+"";
        getParameter().setDescription(s);
    }
    
    public JRDesignDataset getDataset() {
        return dataset;
    }

    public void setDataset(JRDesignDataset dataset) {
        this.dataset = dataset;
    }

    public JRDesignParameter getParameter() {
        return parameter;
    }

    public void setParameter(JRDesignParameter parameter) {
        this.parameter = parameter;
    }
    
    @Override
    public boolean isDefaultValue() {
        return getParameter().getDescription() == null;
    }

    @Override
    public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
        getParameter().setDescription(null);
    }

    @Override
    public boolean supportsDefaultValue() {
        return true;
    }
}
