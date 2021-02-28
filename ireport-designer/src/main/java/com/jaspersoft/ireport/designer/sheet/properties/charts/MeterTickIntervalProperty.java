
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

import com.jaspersoft.ireport.designer.sheet.properties.DoubleProperty;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.charts.design.JRDesignMeterPlot;
    
    
/**
 *  Class to manage the JRDesignMeterPlot.PROPERTY_TICK_INTERVAL property
 */
public final class MeterTickIntervalProperty extends DoubleProperty {

    private final JRDesignMeterPlot plot;

    
    public MeterTickIntervalProperty(JRDesignMeterPlot plot)
    {
        super(plot);
        this.plot = plot;
    }

    @Override
    public String getName()
    {
        return JRDesignMeterPlot.PROPERTY_TICK_INTERVAL;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Tick_Interval");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Tick_Interval.");
    }

    @Override
    public Double getDouble()
    {
        return plot.getTickIntervalDouble();
    }

    @Override
    public Double getOwnDouble()
    {
        return plot.getTickIntervalDouble();
    }

    @Override
    public Double getDefaultDouble()
    {
        return null;
    }

    @Override
    public void setDouble(Double tickInterval)
    {
        plot.setTickInterval(tickInterval);
    }

    @Override
    public void validateDouble(Double tickInterval)
    {
        //FIXME: are there some constraints to be taken into account?
    }

}
