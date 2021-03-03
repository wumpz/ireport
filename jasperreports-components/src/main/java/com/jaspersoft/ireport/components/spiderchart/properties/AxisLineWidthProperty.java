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

import com.jaspersoft.ireport.designer.sheet.properties.FloatProperty;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.components.spiderchart.StandardSpiderPlot;
    
    
/**
 *  Class to manage the JRBaseChartPlot.PROPERTY_BACKGROUND_ALPHA property
 */
public final class AxisLineWidthProperty extends FloatProperty {

    private final StandardSpiderPlot spiderPlot;

    public AxisLineWidthProperty(StandardSpiderPlot spiderPlot)
    {
        super(spiderPlot);
        this.spiderPlot = spiderPlot;
    }

    @Override
    public String getName()
    {
        return StandardSpiderPlot.PROPERTY_AXIS_LINE_WIDTH;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.AxisLineWidth");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.AxisLineWidth.desc");
    }

    @Override
    public Float getFloat()
    {
        return spiderPlot.getAxisLineWidth();
    }

    @Override
    public Float getOwnFloat()
    {
        return getFloat();
    }

    @Override
    public Float getDefaultFloat()
    {
        return null;
    }

    @Override
    public void setFloat(Float width)
    {
        spiderPlot.setAxisLineWidth(width);
    }

    @Override
    public void validateFloat(Float width)
    {

    }

}
