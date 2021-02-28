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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.charts.JRValueDisplay;
import net.sf.jasperreports.charts.design.JRDesignThermometerPlot;
import net.sf.jasperreports.charts.design.JRDesignValueDisplay;
import org.openide.nodes.PropertySupport;
    
    
/**
 *  Class to manage the JRDesignValueDisplay.PROPERTY_COLOR property
 */
public final class ThermometerValueColorProperty extends PropertySupport.ReadWrite {
    
    // FIXME: it should extend ColorProperty
    JRDesignThermometerPlot element = null;

    
    public ThermometerValueColorProperty(JRDesignThermometerPlot element)
    {
        super( JRDesignValueDisplay.PROPERTY_COLOR, java.awt.Color.class,
              I18n.getString("Value_Color"),
              I18n.getString("The_color_of_the_ticks."));
        this.element = element;
    }

    @Override
    public Object getValue() {
        return element.getValueDisplay() == null ? null : element.getValueDisplay().getColor();
    }

    @Override
    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        setPropertyValue(val);
    }

    private void setPropertyValue(Object val)
    {
        if (val == null || val instanceof Color)
        {
            JRValueDisplay oldValue = element.getValueDisplay();
            JRDesignValueDisplay newValue = new JRDesignValueDisplay( element.getValueDisplay(), element.getChart());
            newValue.setColor((Color)val);
            element.setValueDisplay(newValue);
            ObjectPropertyUndoableEdit urob =
                    new ObjectPropertyUndoableEdit(
                        element,
                        I18n.getString("ValueDisplay"), 
                        JRValueDisplay.class,
                        oldValue,newValue);
            // Find the undoRedo manager...
            IReportManager.getInstance().addUndoableEdit(urob);
        }
    }
    
    @Override
    public boolean isDefaultValue() {
        return null == element.getValueDisplay() || null == element.getValueDisplay().getColor();
    }

    @Override
    public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
        setValue(null);
    }

    @Override
    public boolean supportsDefaultValue() {
        return true;
    }
}
