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
package com.jaspersoft.ireport.designer.sheet.properties.charts;

import com.jaspersoft.ireport.designer.sheet.properties.ColorProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Color;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
    
    
/**
 *  Class to manage the JRBaseChartPlot.PROPERTY_BACKCOLOR property
 */
public final class PlotBackgroundProperty extends ColorProperty {

    private final JRBaseChartPlot plot;

    
    public PlotBackgroundProperty(JRBaseChartPlot plot)
    {
        super(plot);
        this.plot = plot;
    }

    @Override
    public String getName()
    {
        return JRBaseChartPlot.PROPERTY_BACKCOLOR;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Background");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Background_of_the_chart.");
    }

    @Override
    public Color getColor() 
    {
        return plot.getBackcolor();
    }

    @Override
    public Color getOwnColor()
    {
        return plot.getOwnBackcolor();
    }

    @Override
    public Color getDefaultColor()
    {
        return null;
    }

    @Override
    public void setColor(Color color)
    {
        plot.setBackcolor(color);
    }

}
