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
import java.awt.Color;
import net.sf.jasperreports.chartthemes.simple.AxisSettings;
import net.sf.jasperreports.chartthemes.simple.ColorProvider;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class AxisLabelPaintProperty extends ColorProperty 
{
    private final AxisSettings settings;

    @SuppressWarnings("unchecked")
    public AxisLabelPaintProperty(AxisSettings settings)
    {
        super(settings);
        this.settings = settings;
    }

    @Override
    public String getName()
    {
        return AxisSettings.PROPERTY_labelPaint;
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
    public Color getColor() 
    {
        ColorProvider colorProvider = 
            settings.getLabelPaint() instanceof ColorProvider
            ? (ColorProvider)settings.getLabelPaint() : null;
        return colorProvider == null ? null : colorProvider.getColor();
    }

    @Override
    public Color getOwnColor()
    {
        return getColor();
    }

    @Override
    public Color getDefaultColor()
    {
        return null;
    }

    @Override
    public void setColor(Color color)
    {
        settings.setLabelPaint(color == null ? null : new ColorProvider(color));
    }

}
