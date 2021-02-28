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
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.sheet.MeterIntervalsProperty;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import net.sf.jasperreports.charts.design.JRDesignMeterPlot;
import net.sf.jasperreports.engine.design.JRDesignDataset;
    
    
/**
 *  Class to manage the JRDesignChart.PROPERTY_TITLE_COLOR property
 */
public final class MeterPlotIntervalsProperty extends MeterIntervalsProperty {

    JRDesignMeterPlot element = null;
    private final JRDesignDataset dataset;

    @SuppressWarnings("unchecked")
    public MeterPlotIntervalsProperty(JRDesignMeterPlot element, JRDesignDataset dataset)
    {
        super( JRDesignMeterPlot.PROPERTY_INTERVALS,
              "Meter Intervals",
              "Meter Intervals.");
        this.element = element;
        this.dataset = dataset;
        this.setValue("expressionContext", new ExpressionContext(dataset));
    }

    @Override
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return element.getIntervals();
    }

    @Override
    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        setPropertyValue(val);
    }

    @SuppressWarnings("unchecked")
    private void setPropertyValue(Object val)
    {
        if (val == null || val instanceof List)
        {
            if (val == element.getIntervals()) return;
            List oldValue = element.getIntervals();
            List newValue =  (List)val;
            
            element.setIntervals(newValue);
            
            ObjectPropertyUndoableEdit urob =
                    new ObjectPropertyUndoableEdit(
                        element,
                        "Intervals", 
                        List.class,
                        oldValue,newValue);
            // Find the undoRedo manager...
            IReportManager.getInstance().addUndoableEdit(urob);
        }
    }
}
