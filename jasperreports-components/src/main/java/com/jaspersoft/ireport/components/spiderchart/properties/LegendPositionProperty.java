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

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.properties.AbstractProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.components.spiderchart.StandardChartSettings;
    
    
/**
 *  Class to manage the JRBaseChart.PROPERTY_LEGEND_POSITION property
 */
public final class LegendPositionProperty extends AbstractProperty
{
    private final StandardChartSettings chart;

    
    public LegendPositionProperty(StandardChartSettings chart)
    {
        super(EdgeEnum.class, chart);
        this.chart = chart;
    }

    @Override
    public String getName()
    {
        return StandardChartSettings.PROPERTY_LEGEND_POSITION;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.LegendPosition");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.LegendPosition");
    }

    public List getTagList() 
    {
        List tags = new ArrayList();
        tags.add(new Tag(EdgeEnum.TOP, I18n.getString("Global.Property.Top")));
        tags.add(new Tag(EdgeEnum.BOTTOM, I18n.getString("Global.Property.Bottom")));
        tags.add(new Tag(EdgeEnum.LEFT, I18n.getString("Global.Property.Left")));
        tags.add(new Tag(EdgeEnum.RIGHT, I18n.getString("Global.Property.Right")));
        return tags;
    }

    @Override
    public Object getPropertyValue() {
        return chart.getLegendPosition();
    }

    @Override
    public Object getOwnPropertyValue() {
        return chart.getLegendPosition();
    }

    @Override
    public Object getDefaultValue() {
        return null;
    }

    @Override
    public void validate(Object value) {

    }

    @Override
    public void setPropertyValue(Object value) {
        chart.setLegendPosition( (EdgeEnum)value);
    }

}
