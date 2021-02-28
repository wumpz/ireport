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

import com.jaspersoft.ireport.designer.sheet.properties.*;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.chartthemes.simple.ChartSettings;
import org.jfree.ui.RectangleInsets;

    
/**
 *
 */
public final class ChartPaddingProperty extends IntegerProperty
{
    private final ChartSettings settings;

    @SuppressWarnings("unchecked")
    public ChartPaddingProperty(ChartSettings settings)
    {
        super(settings);
        this.settings = settings;
    }

    @Override
    public String getName()
    {
        return ChartSettings.PROPERTY_padding;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property." + getName());
    }

    @Override
    public String getShortDescription()
    {
        return getDisplayName();
    }

    @Override
    public Integer getInteger()
    {
        return settings.getPadding() == null ? new Integer(0) : new Integer((int)settings.getPadding().getTop());
    }

    @Override
    public Integer getOwnInteger()
    {
        return getInteger();
    }

    @Override
    public Integer getDefaultInteger()
    {
        return 0;
    }

    @Override
    public void setInteger(Integer width)
    {
        settings.setPadding(new RectangleInsets(width, width, width, width));
    }

    @Override
    public void validateInteger(Integer width)
    {
        if (width != null && width < 0)
        {
            throw annotateException(I18n.getString("Global.Property.Widthexception"));
        }
    }
}
