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
import com.jaspersoft.ireport.designer.sheet.properties.ExpressionProperty;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.charts.JRDataRange;
import net.sf.jasperreports.charts.design.JRDesignDataRange;
import net.sf.jasperreports.charts.design.JRDesignThermometerPlot;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;
    
    
/**
 *  Class to manage the JRDesignDataRange.PROPERTY_LOW_EXPRESSION property
 */
public final class ThermometerMediumDataRangeLowExpressionProperty extends ExpressionProperty 
{
    private final JRDesignThermometerPlot plot;

    public ThermometerMediumDataRangeLowExpressionProperty(JRDesignThermometerPlot plot, JRDesignDataset dataset)
    {
        super(plot, dataset);
        this.plot = plot;
    }

    @Override
    public String getName()
    {
        return I18n.getString("MEDIUM_RANGE_") + JRDesignDataRange.PROPERTY_LOW_EXPRESSION;//FIXMETD concatenation?
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Medium_Data_Range_Low_Expression");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Medium_Data_Range_Low_Expression.");
    }

    @Override
    public String getDefaultExpressionClassName()
    {
        return Number.class.getName();
    }

    @Override
    public JRDesignExpression getExpression()
    {
        JRDataRange dataRange = plot.getMediumRange();
        return dataRange == null ? null : (JRDesignExpression) dataRange.getLowExpression();
    }

    @Override
    public void setExpression(JRDesignExpression expression)
    {
        //FIXMETD plot.setCategoryAxisLabelExpression(expression);
    }

    @Override
    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        JRDataRange oldValue =  plot.getMediumRange();
        JRDesignDataRange newValue = new JRDesignDataRange(null);
        
        if (oldValue != null) 
        {
            try {
                newValue = (JRDesignDataRange)oldValue.clone();
            } catch (Exception ex) {}
        }
        
        //System.out.println("Setting as value: " + val);
        if (val == null || val.equals(I18n.getString("")))
        {
            newValue.setLowExpression(null);
        }
        else
        {
            String s = (val != null) ? val+I18n.getString("") : I18n.getString("");
            
            JRDesignExpression newExp = new JRDesignExpression();
            newExp.setText(s);
            newExp.setValueClassName( I18n.getString("java.lang.Number") );
            newValue.setLowExpression(newExp);
        }
        
        try {
            plot.setMediumRange(newValue);

            ObjectPropertyUndoableEdit urob =
                        new ObjectPropertyUndoableEdit(
                            plot,
                            "MediumRange",
                            JRDataRange.class,
                            oldValue,
                            newValue
                            );
                // Find the undoRedo manager...
            IReportManager.getInstance().addUndoableEdit(urob);
        } catch (Exception ex) { 
            // No exception should be never thrown...
        }
        //System.out.println("Done: " + val);
    }
}
