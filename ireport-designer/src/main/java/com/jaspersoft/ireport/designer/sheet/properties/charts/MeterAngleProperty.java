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

import com.jaspersoft.ireport.designer.sheet.properties.NullableIntegerProperty;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.charts.design.JRDesignMeterPlot;
    
    
/**
 *  Class to manage the JRDesignMeterPlot.PROPERTY_METER_ANGLE property
 */
public final class MeterAngleProperty extends NullableIntegerProperty {

    private final JRDesignMeterPlot plot;

    
    public MeterAngleProperty(JRDesignMeterPlot plot)
    {
        super(plot);
        this.plot = plot;
    }

    @Override
    public String getName()
    {
        return JRDesignMeterPlot.PROPERTY_METER_ANGLE;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Meter_Angle");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Meter_Angle.");
    }

    @Override
    public Integer getInteger()
    {
        return plot.getMeterAngleInteger();
    }

    @Override
    public Integer getOwnInteger()
    {
        return plot.getMeterAngleInteger();
    }

    @Override
    public Integer getDefaultInteger()
    {
        return null;
    }

    @Override
    public void setInteger(Integer height)
    {
        plot.setMeterAngle(height);
    }

    @Override
    public void validateInteger(Integer angle)
    {
        if (angle != null && (angle < 0 || angle > 360))
        {
            throw annotateException(I18n.getString("The_angle_should_be_in_the_range_0-360."));
        }
    }
    
}
