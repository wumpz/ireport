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
package com.jaspersoft.ireport.designer.undo;

import java.lang.reflect.Method;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 *
 * @author gtoffoli
 */
public class ObjectPropertyUndoableEdit extends AggregatedUndoableEdit {

    private Object object;
    private String property;
    private Object oldValue;
    private Object newValue;
    private Class  propertyClass;
    
    /**
     * 
     * @param object It is the object on which call the set method
     * @param property The name of the method (without the initial "set", i.e. Name for setName, or Xyz for setXyz)
     * @param propertyClass The argument of the method. If the argument is primitive, specify the right type (i.e. Integer.TYPE)
     * @param oldValue The oldValue (used by the undo operation)
     * @param newValueThe oldValue (must be the current value of the attribute, it will be used for a redo operation)
     */
    public ObjectPropertyUndoableEdit(Object object, String property, Class propertyClass, Object oldValue, Object newValue)
    {
        super();
        this.object = object;
        this.property = property;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.propertyClass = propertyClass;

        setPresentationName(getProperty() + " of " + getObject());
    }
    
    @Override
    public void undo() throws CannotUndoException {
        
        super.undo();
        try {
            Method m = getObject().getClass().getMethod("set" + getProperty(),getPropertyClass());
            m.invoke(getObject(), new Object[]{getOldValue()});
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CannotUndoException();
        }
    }

    @Override
    public void redo() throws CannotRedoException {
        
        super.redo();
        try {
            Method m = getObject().getClass().getMethod("set" + getProperty(),getPropertyClass());
            m.invoke(getObject(), new Object[]{getNewValue()});
        } catch (Exception ex) {
            throw new CannotUndoException();
        }
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public void setOldValue(Object oldValue) {
        this.oldValue = oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }

    public Class getPropertyClass() {
        return propertyClass;
    }

    public void setPropertyClass(Class propertyClass) {
        this.propertyClass = propertyClass;
    }

}
