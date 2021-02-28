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
package com.jaspersoft.ireport.designer.sheet.properties.charts;

import com.jaspersoft.ireport.designer.sheet.properties.*;
import java.beans.PropertyEditor;
import net.sf.jasperreports.charts.JRItemLabel;
import net.sf.jasperreports.engine.JRChart;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class AbstractItemLabelProperty extends AbstractProperty
{
    protected JRChart chart = null;
    protected PropertyEditor editor = null;
    
    @SuppressWarnings("unchecked")
    public AbstractItemLabelProperty(Object object, JRChart chart)
    {
        super(JRItemLabel.class, object);
        this.chart = chart;
        setValue( "canEditAsText", Boolean.FALSE );
    }

    @Override
    public PropertyEditor getPropertyEditor() {
        
        if (editor == null)
        {
            editor = new JRItemLabelPropertyEditor(chart);
        }
        return editor;
    }

    @Override
    public Object getValue()
    {
        return getPropertyValue();
    }

    @Override
    public Object getPropertyValue()
    {
        return getItemLabel();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return getItemLabel();
    }

    @Override
    public Object getDefaultValue()
    {
        return null;
    }

    @Override
    public void validate(Object value)
    {
    }

    @Override
    public void setPropertyValue(Object value)
    {
        setItemLabel((JRItemLabel)value);
    }

    public abstract JRItemLabel getItemLabel();

    public abstract void setItemLabel(JRItemLabel font);
}
