/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2013 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.ireport.components.sort.properties;

import com.jaspersoft.ireport.designer.sheet.properties.ColorProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Color;
import net.sf.jasperreports.components.sort.SortComponent;


    
/**
 * Class to manage the JRDesignElement.PROPERTY_FORECOLOR property
 * 
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public final class HandlerColorProperty extends ColorProperty 
{
    private final SortComponent component;

    @SuppressWarnings("unchecked")
    public HandlerColorProperty(SortComponent component)
    {
        super(component);
        this.component = component;
    }

    @Override
    public String getName()
    {
        return SortComponent.PROPERTY_HANDLER_COLOR;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.HandlerColor");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.HandlerColor.desc");
    }

    @Override
    public Color getColor() 
    {
        return component.getHandlerColor();
    }

    @Override
    public Color getOwnColor()
    {
        return getColor();
    }

    @Override
    public Color getDefaultColor()
    {
        return Color.WHITE;
    }

    @Override
    public void setColor(Color color)
    {
        if (color == null)
        {
            component.setHandlerColor(null);
        }
        else
        {
            component.setHandlerColor(color);
        }
    }

    @Override
    public boolean supportsDefaultValue()
    {
        return true;
    }
}
