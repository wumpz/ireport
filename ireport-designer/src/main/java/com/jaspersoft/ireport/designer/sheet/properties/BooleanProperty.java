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

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class BooleanProperty extends AbstractProperty
{
    @SuppressWarnings("unchecked")
    public BooleanProperty(Object object)
    {
        super(Boolean.class, object);
    }

    @Override
    public Object getValue()
    {
        Object value = getPropertyValue();
        return value == null ? null : value;
    }

    @Override
    public Object getPropertyValue()
    {
        return getBoolean();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return getOwnBoolean();
    }

    @Override
    public Object getDefaultValue()
    {
        return getDefaultBoolean();
    }

    @Override
    public void validate(Object value)
    {
    }

    @Override
    public void setPropertyValue(Object value)
    {
        setBoolean((Boolean)value);
    }

    public abstract Boolean getBoolean();

    public abstract Boolean getOwnBoolean();

    public abstract Boolean getDefaultBoolean();

    public abstract void setBoolean(Boolean value);
}
