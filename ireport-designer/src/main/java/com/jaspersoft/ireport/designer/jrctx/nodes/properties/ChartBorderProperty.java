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
package com.jaspersoft.ireport.designer.jrctx.nodes.properties;

import com.jaspersoft.ireport.locale.I18n;
import java.awt.Stroke;
import net.sf.jasperreports.chartthemes.simple.ChartSettings;
import net.sf.jasperreports.chartthemes.simple.PaintProvider;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class ChartBorderProperty extends AbstractPenProperty
{
    private final ChartSettings chartSettings;

    @SuppressWarnings("unchecked")
    public ChartBorderProperty(ChartSettings chartSettings)
    {
        super(chartSettings);
        this.chartSettings = chartSettings;
    }

    @Override
    public String getName()
    {
        return ChartSettings.PROPERTY_borderPaint;//FIXMETHEME
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property." + getName());
    }

    @Override
    public String getShortDescription()
    {
        return getDisplayName();
    }

    public PaintProvider getPaintProvider()
    {
        return chartSettings.getBorderPaint();
    }

    public void setPaintProvider(PaintProvider paintProvider)
    {
        chartSettings.setBorderPaint(paintProvider);
    }

    public Stroke getStroke()
    {
        return chartSettings.getBorderStroke();
    }

    public void setStroke(Stroke stroke)
    {
        chartSettings.setBorderStroke(stroke);
    }

}
