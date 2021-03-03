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

import com.jaspersoft.ireport.designer.sheet.properties.DoubleProperty;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.components.spiderchart.StandardSpiderPlot;
    
    
/**
 *  Class to manage the JRBaseChartPlot.PROPERTY_BACKGROUND_ALPHA property
 */
public final class LabelGapProperty extends DoubleProperty {

    private final StandardSpiderPlot spiderPlot;

    public LabelGapProperty(StandardSpiderPlot spiderPlot)
    {
        super(spiderPlot);
        this.spiderPlot = spiderPlot;
    }

    @Override
    public String getName()
    {
        return StandardSpiderPlot.PROPERTY_LABEL_GAP;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.LabelGap");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.LabelGap.desc");
    }

    @Override
    public Double getDouble()
    {
        return spiderPlot.getLabelGap();
    }

    @Override
    public Double getOwnDouble()
    {
        return getDouble();
    }

    @Override
    public Double getDefaultDouble()
    {
        return null;
    }

    @Override
    public void setDouble(Double labelGap)
    {
        spiderPlot.setLabelGap(labelGap);
    }

    @Override
    public void validateDouble(Double labelGap)
    {
        if (labelGap != null && (labelGap < 0f || labelGap > 1f))
        {
            throw annotateException(I18n.getString("The_value_must_be_between_0_and_1."));
        }
    }

}
