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
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
    
    
/**
 *  Class to manage the JRBaseChartPlot.PROPERTY_LABEL_ROTATION property
 */
public final class PlotLabelRotationProperty extends DoubleProperty {

    private final JRBaseChartPlot plot;

    
    public PlotLabelRotationProperty(JRBaseChartPlot plot)
    {
        super(plot);
        this.plot = plot;
    }

    @Override
    public String getName()
    {
        return JRBaseChartPlot.PROPERTY_LABEL_ROTATION;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Label_rotation");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Label_rotation.");
    }

    @Override
    public Double getDouble()
    {
        return plot.getLabelRotationDouble();
    }

    @Override
    public Double getOwnDouble()
    {
        return plot.getLabelRotationDouble();
    }

    @Override
    public Double getDefaultDouble()
    {
        return null;
    }

    @Override
    public void setDouble(Double labelRotation)
    {
        plot.setLabelRotation(labelRotation);
    }

    @Override
    public void validateDouble(Double labelRotation)
    {
        //FIXME: are there some constraints to be taken into account?
    }

}
