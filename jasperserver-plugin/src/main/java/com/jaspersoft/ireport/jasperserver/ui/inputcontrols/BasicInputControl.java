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
package com.jaspersoft.ireport.jasperserver.ui.inputcontrols;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.impl.BasicInputControlUI;
import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.impl.DateInputControlUI;
import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.impl.DateTimeInputControlUI;
import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.impl.InputControlUI;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.awt.event.ActionListener;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.regex.Pattern;
import javax.swing.JComponent;
import java.text.SimpleDateFormat;
import net.sf.jasperreports.types.date.DateRangeBuilder;
import net.sf.jasperreports.types.date.InvalidDateRangeExpressionException;

/**
 *
 * @author gtoffoli
 */
public class BasicInputControl {
    
    /*
     * This map stores the "recent" list of values for each control name...
     * As key, the control URI is used...
     * Each key stores a List.
     *
     */
    public static java.util.Map valueHistories = new java.util.HashMap();

    public void addActionListener(ActionListener listener)
    {
        getInputControlUI().addActionListener(listener);
    }
    
    protected ResourceDescriptor inputControl = null;
    private ResourceDescriptor dataType = null;
    private InputControlUI inputControlUI;

    private Object defaultValue = null;

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        getInputControlUI().setValue(defaultValue);
    }
    
    /** Creates a new instance of BasicInputControl */
    public BasicInputControl() {
         setInputControlUI(new BasicInputControlUI());
    }
    
    public JComponent getUI()
    {
        return (JComponent)getInputControlUI();
    }
    
    public Object getValue() throws InputValidationException
    {
        return validate();
    }

    public ResourceDescriptor getInputControl() {
        return inputControl;
    }

    public void setInputControl(ResourceDescriptor inputControl, java.util.List values)
    {
        setInputControl(inputControl, (ResourceDescriptor)null);
    }
    
    public void setInputControl(ResourceDescriptor inputControl)
    {
        setInputControl(inputControl, (ResourceDescriptor)null);
    }
            
    public void setInputControl(ResourceDescriptor inputControl, ResourceDescriptor dataType) {
        this.inputControl = inputControl;
        this.dataType = dataType;

        if (dataType != null && dataType.getDataType() == dataType.DT_TYPE_DATE)
        {
            setInputControlUI(new DateInputControlUI());
        }
        else if (dataType != null && dataType.getDataType() == ResourceDescriptor.DT_TYPE_DATE_TIME)
        {
            setInputControlUI(new DateTimeInputControlUI());
        }

        String label = inputControl.getLabel() + ((inputControl.isMandatory()) ? "*" : "");
        getInputControlUI().setLabel(label);
        getInputControlUI().setReadOnly( inputControl.isReadOnly() );
        
        java.util.List history = getHistory(inputControl.getUriString());
        java.util.List revisedHistory = new java.util.ArrayList();
        if (dataType != null && dataType.getDataType() == dataType.DT_TYPE_DATE)
        {
            /*
            SimpleDateFormat format = new SimpleDateFormat(IReportManager.getInstance().getProperty("dateformat", "d/M/y"));
            for (int i=0; i<history.size(); ++i)
            {
                Object obj = history.get(i);
                if (obj instanceof java.util.Date)
                {
                    revisedHistory.add(  format.format(obj));
                }
                else
                    revisedHistory.add(  obj);
            }
            */
        }
        else if (dataType != null && dataType.getDataType() == dataType.DT_TYPE_DATE_TIME)
        {
            /*
            SimpleDateFormat format = new SimpleDateFormat(IReportManager.getInstance().getProperty("timeformat", "d/M/y H:m:s"));
            for (int i=0; i<history.size(); ++i)
            {
                Object obj = history.get(i);
                if (obj instanceof java.util.Date)
                {
                    revisedHistory.add(  format.format(obj));
                }
                else
                    revisedHistory.add(  obj);
            }
            */
        }
        else
        {
            revisedHistory = history;
        }
        getInputControlUI().setHistory( revisedHistory );
        
        
        
    }
    
    public void addHistoryValue(String controlUri, Object value)
    {
        
        if (controlUri == null) return;
        java.util.List list = getHistory(controlUri);
        if (list.contains( value)) list.remove(value );
        list.add(0,value);
    }
    
    public java.util.List getHistory(String controlUri)
    {
        if (controlUri == null) return null;
        
        java.util.List list = null;
        if ( valueHistories.get(controlUri) == null)
        {
            list = new java.util.ArrayList();
            valueHistories.put(controlUri, list);
            return list;
        }
        
        return (java.util.List)valueHistories.get(controlUri);
    }

    public InputControlUI getInputControlUI() {
        return inputControlUI;
    }

    public void setInputControlUI(InputControlUI inputControlUI) {
        this.inputControlUI = inputControlUI;
    }
    
    public Object validate() throws InputValidationException
    {
        Object val = getInputControlUI().getValue();
        if (getInputControl().getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_VALUE)
        {
            // Look for the datatype....
            if (getDataType() != null)
            {
                if (val == null) return null;
                
                String strVal = ""+val;
                
                
                
                switch(getDataType().getDataType())
		{
			case ResourceDescriptor.DT_TYPE_TEXT :
			{
				//if (
				//	getDataType().getMaxValue()getMaxLength() != null
				//	&& getDataType().getMaxLength().intValue() < (val+"").length()
				//	)
				//{
				//	wrapper.setErrorMessage(messages.getMessage("fillParameters.error.invalidType", null, Locale.getDefault()));
				//}
				//else 
                                if (getDataType().getPattern() != null
					&& getDataType().getPattern().trim().length() > 0
					&& !Pattern.matches(getDataType().getPattern(), (strVal)))
				{
					throw new InputValidationException(
                                                JasperServerManager.getFormattedString("basicInputControl.patternError","{0} does not match the pattern ({1})",new Object[]{getInputControl().getLabel(), getDataType().getPattern()}));

                                }
                                val = strVal;
				break;
			}
			case ResourceDescriptor.DT_TYPE_NUMBER :
			{
				//FIXME take care of input mask
				try
				{
                                    if (strVal.trim().length() == 0) return null;
				    val = new java.math.BigDecimal(strVal);
				}
				catch(NumberFormatException e)
				{
                                    throw new InputValidationException(
                                            JasperServerManager.getFormattedString("basicInputControl.invalidNumber","Invalid number set for {0}",new Object[]{getInputControl().getLabel()}));
				}
                                break;
			}
			case ResourceDescriptor.DT_TYPE_DATE_TIME :
                        {
                            if (val instanceof String)
                            {
                                 if (strVal.trim().length() == 0) return null;
                                
                                 // Check if the format is either  "d/M/y H:m:s" or "a date range"
                                 SimpleDateFormat format = new SimpleDateFormat(IReportManager.getInstance().getProperty("timeformat", "d/M/y H:m:s"));
                                 ParseException pe = null;
                                         
                                try
                                { 
                                        val = format.parse(strVal);
                                }
                                catch (ParseException e)
                                {
                                    pe = e;
                                        
                                }
                                
                                if (pe != null)
                                {
                                    boolean validDate = false;
                                    if (IReportManager.getPreferences().getBoolean("jasperserver.useRelativeDateExpressions", true))
                                    {
                                        DateRangeBuilder drb = new DateRangeBuilder(strVal);
                                        try {

                                            drb.set(Timestamp.class).toDateRange();
                                            validDate = true;
                                        } catch (InvalidDateRangeExpressionException ex)
                                        {

                                        }
                                    }
                                    
                                    if (!validDate)
                                    {
                                        throw new InputValidationException(
                                                JasperServerManager.getFormattedString("basicInputControl.invalidDatetime","Invalid value set for {0} is not a valid datetime in the form {1} or a date range expression.",new Object[]{getInputControl().getLabel(),IReportManager.getInstance().getProperty("timeformat", "d/M/y H:m:s")}));
                                    }
                                }
                                 
                            }
                            
                            break;
                        }
                            
                            
			case ResourceDescriptor.DT_TYPE_DATE :
			{
                            
                            if (val instanceof String)
                            {
                                 if (strVal.trim().length() == 0) return null;
                                
                                 // Check if the format is either  "d/M/y H:m:s" or "a date range"
                                 SimpleDateFormat format = new SimpleDateFormat(IReportManager.getInstance().getProperty("dateformat", "d/M/y") );
                                 ParseException pe = null;
                                         
                                try
                                { 
                                        val = format.parse(strVal);
                                }
                                catch (ParseException e)
                                {
                                    pe = e;
                                        
                                }
                                 
                                if (pe != null)
                                {
                                    boolean validDate = false;
                                    if (IReportManager.getPreferences().getBoolean("jasperserver.useRelativeDateExpressions", true))
                                    {
                                        DateRangeBuilder drb = new DateRangeBuilder(strVal);
                                        try {

                                            drb.toDateRange();
                                            validDate = true;
                                        } catch (InvalidDateRangeExpressionException ex)
                                        {

                                        }
                                    }
                                    
                                    if (!validDate)
                                    {
                                        throw new InputValidationException(
                                                JasperServerManager.getFormattedString("basicInputControl.invalidDate","Invalid value set for {0} is not a valid date in the form {1} or a valid date range expression.",new Object[]{getInputControl().getLabel(),IReportManager.getInstance().getProperty("dateformat", "d/M/y")}));
                                    }
                                }
                                
                                 
                                 
                            }
                            break;
			}
		}
            }
        }
        
        return val;
    }

    public ResourceDescriptor getDataType() {
        return dataType;
    }

    public void setDataType(ResourceDescriptor dataType) {
        this.dataType = dataType;
    }
}
