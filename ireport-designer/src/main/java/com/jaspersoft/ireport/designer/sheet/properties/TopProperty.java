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

import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.engine.design.JRDesignElement;

    
/**
 *  Class to manage the JRDesignElement.PROPERTY_Y property
 */
public final class TopProperty extends IntegerProperty
{
    private final JRDesignElement element;

    @SuppressWarnings("unchecked")
    public TopProperty(JRDesignElement element)
    {
        super(element);
        this.element = element;
    }

    @Override
    public String getName()
    {
        return JRDesignElement.PROPERTY_Y;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.Top");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.Topdetail");
    }

    @Override
    public Integer getInteger()
    {
        return element.getY();
    }

    @Override
    public Integer getOwnInteger()
    {
        return element.getY();
    }

    @Override
    public Integer getDefaultInteger()
    {
        return 0;//FIXMETD is this a fair default? do we even have a default?
    }

    @Override
    public void setInteger(Integer y)
    {
        element.setY(y);
    }

    @Override
    public void validateInteger(Integer y)
    {
        if (y < 0)
        {
            throw annotateException(I18n.getString("Global.Property.Topexception"));
        }
    }

}
