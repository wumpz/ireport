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
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.charts.design.JRDesignMeterPlot;
import org.openide.nodes.PropertySupport;
    
    
/**
 *  Class to manage the JasperDesign.PROPERTY_PAGE_WIDTH property
 */
public final class MeterUnitsProperty extends PropertySupport
{
        private final JRDesignMeterPlot element;
    
        @SuppressWarnings("unchecked")
        public MeterUnitsProperty(JRDesignMeterPlot element)
        {
            super(JRDesignMeterPlot.PROPERTY_UNITS,String.class, "Units", "Units", true, true);
            this.element = element;
            this.setValue("oneline", Boolean.TRUE);
        }
        
        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return (element.getUnits() == null) ? "" : element.getUnits();
        }

        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (val instanceof String)
            {
                String oldValue = element.getUnits();
                String newValue = (val == null) ? null : val.toString();
                element.setUnits(newValue);
            
                ObjectPropertyUndoableEdit urob =
                        new ObjectPropertyUndoableEdit(
                            element,
                            "Units", 
                            String.class,
                            oldValue,newValue);
                // Find the undoRedo manager...
                IReportManager.getInstance().addUndoableEdit(urob);
        
            }
        }
}
