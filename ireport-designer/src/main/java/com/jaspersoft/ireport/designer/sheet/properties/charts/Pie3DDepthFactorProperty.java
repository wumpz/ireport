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
import net.sf.jasperreports.charts.design.JRDesignPie3DPlot;
    
    
/**
 *  Class to manage the JRDesignPie3DPlot.PROPERTY_DEPTH_FACTOR property
 */
public final class Pie3DDepthFactorProperty extends DoubleProperty {

    private final JRDesignPie3DPlot plot;

    
    public Pie3DDepthFactorProperty(JRDesignPie3DPlot plot)
    {
        super(plot);
        this.plot = plot;
    }

    @Override
    public String getName()
    {
        return JRDesignPie3DPlot.PROPERTY_DEPTH_FACTOR;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Depth_Factor");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Depth_Factor.");
    }

    @Override
    public Double getDouble()
    {
        return plot.getDepthFactorDouble();
    }

    @Override
    public Double getOwnDouble()
    {
        return plot.getDepthFactorDouble();
    }

    @Override
    public Double getDefaultDouble()
    {
        return null;
    }

    @Override
    public void setDouble(Double depth)
    {
        plot.setDepthFactor(depth);
    }

    @Override
    public void validateDouble(Double depth)
    {
        if (depth != null && depth < 0)
        {
            throw annotateException(I18n.getString("The_depth_factor_must_be_a_positive_value."));
        }
    }

}
