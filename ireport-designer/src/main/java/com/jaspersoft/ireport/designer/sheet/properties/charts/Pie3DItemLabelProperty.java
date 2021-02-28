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

import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.charts.JRItemLabel;
import net.sf.jasperreports.charts.design.JRDesignPie3DPlot;



/**
 *
 * @author gtoffoli
 */
public class Pie3DItemLabelProperty extends AbstractItemLabelProperty {

    JRDesignPie3DPlot plot = null;
    public Pie3DItemLabelProperty(JRDesignPie3DPlot plot)
    {
        super(plot, plot.getChart());
        this.plot = plot;
    }

    @Override
    public String getName()
    {
        return JRDesignPie3DPlot.PROPERTY_ITEM_LABEL;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.ItemLabel");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.ItemLabel");
    }


    @Override
    public JRItemLabel getItemLabel() {
        return plot.getItemLabel();
    }

    @Override
    public void setItemLabel(JRItemLabel item) {
        plot.setItemLabel(item);
    }

}
