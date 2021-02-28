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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.sheet.SeriesColorsProperty;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import net.sf.jasperreports.chartthemes.simple.ColorProvider;
import net.sf.jasperreports.chartthemes.simple.PlotSettings;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
    
    
/**
 *
 */
public final class PlotSeriesColorsProperty extends SeriesColorsProperty 
{
    private final PlotSettings settings;

    @SuppressWarnings("unchecked")
    public PlotSeriesColorsProperty(PlotSettings settings)
    {
        super(JRBaseChartPlot.PROPERTY_SERIES_COLORS, 
              "Series Colors",
              "Series Colors");
        this.settings = settings;
    }

    @Override
    public Object getValue()
    {
        List list = new ArrayList();
        if (settings.getSeriesColorSequence() != null)
        {
            for(int i = 0; i < settings.getSeriesColorSequence().size(); i++)
            {
                list.add(
                    new JRBaseChartPlot.JRBaseSeriesColor(i, ((ColorProvider)settings.getSeriesColorSequence().get(i)).getColor())
                    
                    );
            }
        }
        return list;
    }

    @Override
    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        setPropertyValue(val);
    }

    @SuppressWarnings("unchecked")
    private void setPropertyValue(Object val)
    {
        if (val instanceof List)
        {
            List oldValue = (List)settings.getSeriesColorSequence();
            
            List colors = new ArrayList();
            if (oldValue == val) return;
            if (oldValue == null && val == null) return;

            // Check for changes...



            if (val != null)
            {
                boolean update = false;
                if (oldValue ==null || oldValue.size() != ((List)val).size())
                {
                    update = true;
                }
                else
                {
                    ;
                    for(int idx = 0; !update && idx < ((List)val).size(); ++idx)
                    {

                        Color c1 = ((JRBaseChartPlot.JRBaseSeriesColor)((List)val).get(idx)).getColor();
                        Color c2 = ((ColorProvider)oldValue.get(idx)).getColor();

                        if (c1 == null && c1 != c2)
                        {
                            update = true;
                        }
                        else if (c1 != null && !c1.equals(c2))
                        {
                            update = true;
                        }
                    }
                }

                if (!update) return;
                
                for(Iterator it = ((List)val).iterator(); it.hasNext();)
                {
                    colors.add(new ColorProvider(((JRBaseChartPlot.JRBaseSeriesColor)it.next()).getColor()));
                }
            }
            settings.setSeriesColorSequence(colors);
            
            ObjectPropertyUndoableEdit urob =
                    new ObjectPropertyUndoableEdit(
                        settings,
                        "SeriesColorSequence",
                        List.class,
                        oldValue,colors);
            // Find the undoRedo manager...
            IReportManager.getInstance().addUndoableEdit(urob);
        }
    }
}
