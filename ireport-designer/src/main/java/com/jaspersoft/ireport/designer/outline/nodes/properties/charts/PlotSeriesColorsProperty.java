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
package com.jaspersoft.ireport.designer.outline.nodes.properties.charts;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.sheet.SeriesColorsProperty;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.base.JRBaseChartPlot.JRBaseSeriesColor;
    
    
/**
 *  Class to manage the JRBaseChartPlot.PROPERTY_BACKGROUND_ALPHA property
 */
public final class PlotSeriesColorsProperty extends SeriesColorsProperty {

    JRBaseChartPlot element = null;

    @SuppressWarnings("unchecked")
    public PlotSeriesColorsProperty(JRBaseChartPlot element)
    {
        super(JRBaseChartPlot.PROPERTY_SERIES_COLORS, 
              "Series Colors",
              "Series Colors");
        this.element = element;
    }

    

    @Override
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return sortedSetAsList( element.getSeriesColors() );
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
            if (!isListChanged( (List)val, element.getSeriesColors()) ) return;

            SortedSet oldValue = new TreeSet();
            if (element.getSeriesColors() != null) oldValue.addAll(element.getSeriesColors());
            SortedSet newValue =  (val == null) ? null : listAsSortedSet((List)val);
            
            element.setSeriesColors(newValue);
            
            ObjectPropertyUndoableEdit urob =
                    new ObjectPropertyUndoableEdit(
                        element,
                        "SeriesColors", 
                        Collection.class,
                        oldValue,newValue);
            // Find the undoRedo manager...
            IReportManager.getInstance().addUndoableEdit(urob);
        }
    }
}
