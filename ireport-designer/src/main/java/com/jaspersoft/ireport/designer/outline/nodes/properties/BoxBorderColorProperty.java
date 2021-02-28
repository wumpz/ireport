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
package com.jaspersoft.ireport.designer.outline.nodes.properties;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.engine.JRBox;
import org.openide.nodes.PropertySupport;

    
/**
 *
 */
public final class BoxBorderColorProperty extends PropertySupport.ReadWrite {

    JRBox box = null;

    /**
     * property can be border, topBorder, 
     **/
    @SuppressWarnings("unchecked")
    public BoxBorderColorProperty(JRBox box, String propertyName, String propertyDisplayName, 
                                  String propertyDesc)
    {
        super(propertyName, java.awt.Color.class,
              propertyDisplayName,
              propertyDesc);
        this.box = box;
    }

    @Override
    public Object getValue() {
//FIXME
//            if (this.getName().equals( JRBaseStyle.PROPERTY_BORDER_COLOR)) return box.getBorderColor();
//            if (this.getName().equals( JRBaseStyle.PROPERTY_TOP_BORDER_COLOR)) return box.getTopBorderColor();
//            if (this.getName().equals( JRBaseStyle.PROPERTY_LEFT_BORDER_COLOR)) return box.getLeftBorderColor();
//            if (this.getName().equals( JRBaseStyle.PROPERTY_RIGHT_BORDER_COLOR)) return box.getRightBorderColor();
//            if (this.getName().equals( JRBaseStyle.PROPERTY_BOTTOM_BORDER_COLOR)) return box.getBottomBorderColor();
        return null;
    }

    @Override
    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (val == null || val instanceof Color)
        {
            Color newValue =  (Color)val;
            Color oldValue = null;
            String methodName = "BorderColor";
//FIXME
//                if (this.getName().equals( JRBaseStyle.PROPERTY_BORDER_COLOR))
//                {
//                    methodName = "BorderColor";
//                    oldValue = box.getOwnBorderColor();
//                    box.setBorderColor(newValue);
//                }
//                if (this.getName().equals( JRBaseStyle.PROPERTY_TOP_BORDER_COLOR)){
//                    methodName = "TopBorderColor";
//                    oldValue = box.getOwnTopBorderColor();
//                    box.setTopBorderColor(newValue);
//                }
//                if (this.getName().equals( JRBaseStyle.PROPERTY_LEFT_BORDER_COLOR)){
//                    methodName = "LeftBorderColor";
//                    oldValue = box.getOwnLeftBorderColor();
//                    box.setLeftBorderColor(newValue);
//                }
//                if (this.getName().equals( JRBaseStyle.PROPERTY_RIGHT_BORDER_COLOR)){
//                    methodName = "RightBorderColor";
//                    oldValue = box.getOwnRightBorderColor();
//                    box.setRightBorderColor(newValue);
//                }
//                if (this.getName().equals( JRBaseStyle.PROPERTY_BOTTOM_BORDER_COLOR)){
//                    methodName = "BottomBorderColor";
//                    oldValue = box.getOwnBottomBorderColor();
//                    box.setBottomBorderColor(newValue);
//                }

            ObjectPropertyUndoableEdit urob =
                    new ObjectPropertyUndoableEdit(
                        box,
                        methodName, 
                        Color.class,
                        oldValue,newValue);
            // Find the undoRedo manager...
            IReportManager.getInstance().addUndoableEdit(urob);
        }
    }

    @Override
    public boolean isDefaultValue() {
//FIXME
//            if (this.getName().equals( JRBaseStyle.PROPERTY_BORDER_COLOR)) return null == box.getOwnBorderColor();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_TOP_BORDER_COLOR)) return null == box.getOwnTopBorderColor();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_LEFT_BORDER_COLOR)) return null == box.getOwnLeftBorderColor();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_RIGHT_BORDER_COLOR)) return null == box.getOwnRightBorderColor();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_BOTTOM_BORDER_COLOR)) return null == box.getOwnBottomBorderColor();
        return true;
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
