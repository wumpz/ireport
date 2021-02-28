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
import net.sf.jasperreports.charts.design.JRDesignThermometerPlot;
    
    
/**
 *  Class to manage the JRDesignThermometerPlot.PROPERTY_MERCURY_COLOR property
 *  @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public final class ThermometerMercuryColorProperty extends ColorProperty {

    private final JRDesignThermometerPlot element;

    
    public ThermometerMercuryColorProperty(JRDesignThermometerPlot element)
    {
        super(element);
        this.element = element;
    }

    @Override
    public String getName()
    {
        return JRDesignThermometerPlot.PROPERTY_MERCURY_COLOR;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Mercury_Color");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("The_color_of_the_mercury_inside_the_thermometer.");
    }

    @Override
    public Color getColor() 
    {
        return element.getMercuryColor();
    }

    @Override
    public Color getOwnColor()
    {
        // FIXME: check this
        // There is no own mercury color
        return element.getMercuryColor();
    }

    @Override
    public Color getDefaultColor()
    {
        return null;
    }

    @Override
    public void setColor(Color color)
    {
        element.setMercuryColor(color);
    }

}
