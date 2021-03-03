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
package com.jaspersoft.ireport.components.spiderchart.properties;

import com.jaspersoft.ireport.designer.sheet.properties.ColorProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Color;
import net.sf.jasperreports.components.spiderchart.StandardChartSettings;
import net.sf.jasperreports.engine.base.JRBaseChart;
    
    
/**
 *  Class to manage the JRBaseChart.PROPERTY_LEGEND_COLOR property
 *  @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public final class LegendColorProperty extends ColorProperty {

    private final StandardChartSettings chartSettings;

    
    public LegendColorProperty(StandardChartSettings chartSettings)
    {
        super(chartSettings);
        this.chartSettings = chartSettings;
    }

    @Override
    public String getName()
    {
        return JRBaseChart.PROPERTY_LEGEND_COLOR;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.LegendColor");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.LegendColordetail");
    }

    @Override
    public Color getColor() 
    {
        return chartSettings.getLegendColor();
    }

    @Override
    public Color getOwnColor()
    {
        return chartSettings.getLegendColor();
    }

    @Override
    public Color getDefaultColor()
    {
        return null;
    }

    @Override
    public void setColor(Color color)
    {
        chartSettings.setLegendColor(color);
    }
    
}
