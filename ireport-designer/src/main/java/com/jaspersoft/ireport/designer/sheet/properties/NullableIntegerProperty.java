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
public abstract class NullableIntegerProperty extends AbstractProperty
{
    @SuppressWarnings("unchecked")
    public NullableIntegerProperty(Object object)
    {
        super(Float.class, object);
        setValue("suppressCustomEditor", Boolean.TRUE);
    }

    @Override
    public Object getPropertyValue()
    {
        return getInteger();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return getOwnInteger();
    }

    @Override
    public Object getDefaultValue()
    {
        return getDefaultInteger();
    }

    @Override
    public void validate(Object value)
    {
        validateInteger(convert(value));
    }

    @Override
    public void setPropertyValue(Object value)
    {
        setInteger(convert(value));
    }

    private Integer convert(Object value)
    {
        Number number = (Number)value;
        return number == null ? null : new Integer(number.intValue());
    }

    public abstract Integer getInteger();

    public abstract Integer getOwnInteger();

    public abstract Integer getDefaultInteger();

    public abstract void validateInteger(Integer value);

    public abstract void setInteger(Integer value);

}
