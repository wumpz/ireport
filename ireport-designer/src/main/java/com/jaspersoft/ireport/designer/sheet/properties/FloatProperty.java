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

import com.jaspersoft.ireport.designer.outline.nodes.properties.*;

    
/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public abstract class FloatProperty extends AbstractProperty
{
    @SuppressWarnings("unchecked")
    public FloatProperty(Object object)
    {
        super(Float.class, object);
        setValue("suppressCustomEditor", Boolean.TRUE);
    }

    @Override
    public Object getPropertyValue()
    {
        return getFloat();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return getOwnFloat();
    }

    @Override
    public Object getDefaultValue()
    {
        return getDefaultFloat();
    }

    @Override
    public void validate(Object value)
    {
        validateFloat((Float)value);
    }

    @Override
    public void setPropertyValue(Object value)
    {
        setFloat((Float)value);
    }

    public abstract Float getFloat();

    public abstract Float getOwnFloat();

    public abstract Float getDefaultFloat();

    public abstract void validateFloat(Float value);

    public abstract void setFloat(Float value);

}
