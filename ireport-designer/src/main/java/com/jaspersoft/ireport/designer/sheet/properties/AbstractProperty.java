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
package com.jaspersoft.ireport.designer.sheet.properties;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.undo.PropertyUndoableEdit;
import java.lang.reflect.InvocationTargetException;
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class AbstractProperty extends PropertySupport.ReadWrite
{
    protected final Object object;



    @SuppressWarnings("unchecked")
    public AbstractProperty(Class clazz, Object object)
    {
        super(null, clazz, null, null);
        this.object = object;
    }

    @Override
    public Object getValue()
    {
        Object value = getPropertyValue();
        return value == null ? "" : value;
    }

    @Override
    public void setValue(Object newValue) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException 
    {
        Object oldValue = getOwnPropertyValue();

        validate(newValue);
        
        setPropertyValue(newValue);

        PropertyUndoableEdit undo =
            new PropertyUndoableEdit(
                this,
                oldValue,
                newValue
                );
        IReportManager.getInstance().addUndoableEdit(undo);
    }

    @Override
    public boolean isDefaultValue() 
    {
        Object value = getPropertyValue();

        return
            (getDefaultValue() == null && value == null)
            || (getDefaultValue() != null && getDefaultValue().equals(value));
    }

    @Override
    public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException 
    {
        //FIXMETD check this super.restoreDefaultValue();
        setValue(getDefaultValue());
    }

    @Override
    public boolean supportsDefaultValue() 
    {
        return true;
    }

    public IllegalArgumentException annotateException(String msg)
    {
        IllegalArgumentException iae = new IllegalArgumentException(msg); 
        ErrorManager.getDefault().annotate(
            iae, 
            ErrorManager.EXCEPTION,
            msg,
            msg, null, null
            ); 
        return iae;
    }

    public abstract Object getPropertyValue();

    public abstract Object getOwnPropertyValue();

    public abstract Object getDefaultValue();

    public abstract void validate(Object value);

    public abstract void setPropertyValue(Object value);

}
