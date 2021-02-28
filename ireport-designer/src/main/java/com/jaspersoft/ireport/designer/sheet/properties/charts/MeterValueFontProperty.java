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

import com.jaspersoft.ireport.designer.sheet.properties.AbstractFontProperty;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.charts.design.JRDesignMeterPlot;
import net.sf.jasperreports.charts.design.JRDesignValueDisplay;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.design.JasperDesign;
    
    
/**
 * Class to manage the JRDesignValueDisplay.PROPERTY_FONT
 */
public final class MeterValueFontProperty extends AbstractFontProperty
{
    private final JRDesignMeterPlot plot;
        
    public MeterValueFontProperty(JRDesignMeterPlot plot, JasperDesign jasperDesign)
    {
        super(plot, jasperDesign);
        this.plot = plot;
    }
    
    @Override
    public String getName()
    {
        return JRDesignValueDisplay.PROPERTY_FONT;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Value_Font");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Value_Font");
    }

    @Override
    public JRFont getFont()
    {
        return plot.getValueDisplay() == null ? null : plot.getValueDisplay().getFont();
    }

    @Override
    public void setFont(JRFont font)
    {
        JRDesignValueDisplay newValue = 
            new JRDesignValueDisplay(plot.getValueDisplay(), plot.getChart());
        newValue.setFont(font);
        plot.setValueDisplay(newValue);
    }

}
