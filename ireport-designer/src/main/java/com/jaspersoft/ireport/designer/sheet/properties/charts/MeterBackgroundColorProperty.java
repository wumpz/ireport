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
import net.sf.jasperreports.charts.design.JRDesignMeterPlot;
    
    
/**
 *  Class to manage the JRDesignMeterPlot.PROPERTY_METER_BACKGROUND_COLOR property
 *  @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public final class MeterBackgroundColorProperty extends ColorProperty {

    private final JRDesignMeterPlot element;

    
    public MeterBackgroundColorProperty(JRDesignMeterPlot element)
    {
        super(element);
        this.element = element;
    }

    @Override
    public String getName()
    {
        return JRDesignMeterPlot.PROPERTY_METER_BACKGROUND_COLOR;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Meter_Background_Color");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("The_background_color_of_the_meter.");
    }

    @Override
    public Color getColor() 
    {
        return element.getMeterBackgroundColor();
    }

    @Override
    public Color getOwnColor()
    {
        // FIXME: check this;  
        // there is no own background color for the meterPlot element
        return element.getMeterBackgroundColor();
    }

    @Override
    public Color getDefaultColor()
    {
        // FIXME: check this; 
        // there is no own background color for the meterPlot element
        return null;
    }

    @Override
    public void setColor(Color color)
    {
        element.setMeterBackgroundColor(color);
    }
}
