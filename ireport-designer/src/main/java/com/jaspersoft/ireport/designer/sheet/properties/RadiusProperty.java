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
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignRectangle;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class RadiusProperty extends IntegerProperty
{
    private final JRDesignRectangle rectangle;

    @SuppressWarnings("unchecked")
    public RadiusProperty(JRDesignRectangle rectangle)
    {
        super(rectangle);
        this.rectangle = rectangle;
    }

    @Override
    public String getName()
    {
        return JRBaseStyle.PROPERTY_RADIUS;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("RadiusPropertyRadius.Property.Radius");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.Radiusdetail");
    }

    @Override
    public Integer getInteger()
    {
        return rectangle.getRadius();
    }

    @Override
    public Integer getOwnInteger()
    {
        return rectangle.getOwnRadius();
    }

    @Override
    public Integer getDefaultInteger()
    {
        return null;
    }

    @Override
    public void setInteger(Integer radius)
    {
        rectangle.setRadius(radius);
    }

    @Override
    public void validateInteger(Integer radius)
    {
        if (radius != null && radius < 0)
        {
            throw annotateException(I18n.getString("Global.Property.Radiusexception"));
        }
    }

}
