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

import com.jaspersoft.ireport.designer.sheet.editors.FieldPatternPropertyEditor;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import net.sf.jasperreports.engine.base.JRBaseStyle;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class PatternProperty extends StringProperty
{
    private PropertyEditor editor = null;

    @Override
    public PropertyEditor getPropertyEditor() {
        if (editor == null)
        {
            editor = new FieldPatternPropertyEditor();
        }
        return editor;
    }
    
    @SuppressWarnings("unchecked")
    public PatternProperty(Object object)
    {
        super(object);
        setValue("suppressCustomEditor", Boolean.FALSE);
    }

    @Override
    public String getName()
    {
        return JRBaseStyle.PROPERTY_PATTERN;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("PatternProperty.Property.Pattern");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("PatternProperty.Property.Patterndetail");
    }

    @Override
    public String getString()
    {
        return getPattern();
    }

    @Override
    public String getOwnString()
    {
        return getOwnPattern();
    }

    @Override
    public String getDefaultString()
    {
        return null;
    }

    @Override
    public void setString(String pattern)
    {
        setPattern(pattern);
    }

    public abstract String getPattern();

    public abstract String getOwnPattern();

    public abstract void setPattern(String pattern);
}
