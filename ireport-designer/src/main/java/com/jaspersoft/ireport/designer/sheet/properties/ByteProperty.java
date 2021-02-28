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

import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import java.beans.PropertyEditor;
import java.util.List;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class ByteProperty extends AbstractProperty
{
    private ComboBoxPropertyEditor editor;

    @SuppressWarnings("unchecked")
    public ByteProperty(Object object)
    {
        super(Byte.class, object);
        setValue("suppressCustomEditor", Boolean.TRUE);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PropertyEditor getPropertyEditor() 
    {
        if (editor == null)
        {
            editor = new ComboBoxPropertyEditor(false, getTagList());
        }
        return editor;
    }

    @Override
    public Object getPropertyValue()
    {
        return getByte();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return getOwnByte();
    }

    @Override
    public Object getDefaultValue()
    {
        return getDefaultByte();
    }

    @Override
    public void validate(Object value)
    {
    }

    @Override
    public void setPropertyValue(Object value)
    {
        setByte((Byte)value);
    }

    public abstract List getTagList();

    public abstract Byte getByte();

    public abstract Byte getOwnByte();

    public abstract Byte getDefaultByte();

    public abstract void setByte(Byte value);
}
