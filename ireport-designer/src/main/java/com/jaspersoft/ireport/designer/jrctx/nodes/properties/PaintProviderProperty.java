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
package com.jaspersoft.ireport.designer.jrctx.nodes.properties;

import com.jaspersoft.ireport.designer.jrctx.nodes.editors.PaintProviderPropertyEditor;
import com.jaspersoft.ireport.designer.sheet.properties.*;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.chartthemes.simple.PaintProvider;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class PaintProviderProperty extends AbstractProperty
{
    private PaintProviderPropertyEditor editor = null;
    
    @SuppressWarnings("unchecked")
    public PaintProviderProperty(Object object)
    {
        super(PaintProvider.class, object);
        
        setValue("canEditAsText", Boolean.FALSE);
    }

    @Override
    public Object getPropertyValue()
    {
        return getPaintProvider();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return getOwnPaintProvider();
    }

    @Override
    public Object getDefaultValue()
    {
        return getDefaultPaintProvider();
    }

    @Override
    public PropertyEditor getPropertyEditor()
    {
        if(editor == null)
        {
            editor = new PaintProviderPropertyEditor();
        }
        return editor;
    }

    @Override
    public void validate(Object value)
    {
    }

    @Override
    public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
        setPropertyValue(getDefaultPaintProvider());
    }

    @Override
    public void setPropertyValue(Object value)
    {
        setPaintProvider((PaintProvider)value);
    }

    public abstract PaintProvider getPaintProvider();

    public abstract PaintProvider getOwnPaintProvider();

    public abstract PaintProvider getDefaultPaintProvider();

    public abstract void setPaintProvider(PaintProvider paintProvider);

}
