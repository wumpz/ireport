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
package com.jaspersoft.ireport.designer.sheet.properties;

import com.jaspersoft.ireport.designer.sheet.editors.JRFontPropertyEditor;
import java.beans.PropertyEditor;
import java.util.prefs.PreferenceChangeListener;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.design.JasperDesign;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class AbstractFontProperty extends AbstractProperty
{
    protected JasperDesign jasperDesign = null;
    protected PropertyEditor editor = null;
    
    @SuppressWarnings("unchecked")
    public AbstractFontProperty(Object object, JasperDesign jasperDesign)
    {
        super(JRFont.class, object);
        this.jasperDesign = jasperDesign;
        setValue( "canEditAsText", Boolean.FALSE );
    }

    @Override
    public PropertyEditor getPropertyEditor() {
        
        if (editor == null)
        {
            editor = new JRFontPropertyEditor(jasperDesign);
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
        return getFont();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return getFont();
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
        setFont((JRFont)value);
    }

    public abstract JRFont getFont();

    public abstract void setFont(JRFont font);
}
