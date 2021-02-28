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

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.properties.StringListProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.util.List;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.design.JRDesignChart;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class RenderTypeProperty extends StringListProperty
{
    private final JRDesignChart chart;

    
    public RenderTypeProperty(JRDesignChart chart)
    {
        super(chart);
        this.chart = chart;
    }

    @Override
    public String getName()
    {
        return JRChart.PROPERTY_CHART_RENDER_TYPE;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Render_Type");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("The_render_type_of_the_chart.");
    }

    @Override
    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(JRChart.RENDER_TYPE_DRAW, JRChart.RENDER_TYPE_DRAW));
        tags.add(new Tag(JRChart.RENDER_TYPE_IMAGE, JRChart.RENDER_TYPE_IMAGE));
        tags.add(new Tag(JRChart.RENDER_TYPE_SVG, JRChart.RENDER_TYPE_SVG));
        return tags;
    }

    @Override
    public String getString()
    {
        return chart.getRenderType();
    }

    @Override
    public String getOwnString()
    {
        return chart.getRenderType();
    }

    @Override
    public String getDefaultString()
    {
        return null;
    }

    @Override
    public void setString(String renderType)
    {
        chart.setRenderType(renderType);
    }

}
