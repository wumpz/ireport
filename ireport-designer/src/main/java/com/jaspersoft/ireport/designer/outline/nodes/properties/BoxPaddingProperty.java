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
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.engine.JRBox;
import org.openide.nodes.PropertySupport;

    
/**
 *  Class to manage the JRDesignElement.PROPERTY_PRINT_REPEATED_VALUES property
 */
public final class BoxPaddingProperty extends PropertySupport.ReadWrite {

    JRBox box = null;

    /**
     *
     **/
    @SuppressWarnings("unchecked")
    public BoxPaddingProperty(JRBox box, String propertyName, String propertyDisplayName, 
                                  String propertyDesc)
    {
        super(propertyName, Integer.class,
              propertyDisplayName,
              propertyDesc);
        this.box = box;
    }

    @Override
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
//FIXME
//            if (this.getName().equals( JRBaseStyle.PROPERTY_PADDING)) return box.getPadding();
//            if (this.getName().equals( JRBaseStyle.PROPERTY_TOP_PADDING)) return box.getTopPadding();
//            if (this.getName().equals( JRBaseStyle.PROPERTY_LEFT_PADDING)) return box.getLeftPadding();
//            if (this.getName().equals( JRBaseStyle.PROPERTY_RIGHT_PADDING)) return box.getRightPadding();
//            if (this.getName().equals( JRBaseStyle.PROPERTY_BOTTOM_PADDING)) return box.getBottomPadding();
        return null;
    }

    @Override
    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (val == null || val instanceof Integer)
        {
            Integer newValue =  (Integer)val;
            Integer oldValue = null;
            String methodName = "Padding";
//FIXME
//                if (this.getName().equals( JRBaseStyle.PROPERTY_PADDING))
//                {
//                    methodName = "Padding";
//                    oldValue = box.getOwnPadding();
//                    box.setPadding(newValue);
//                }
//                if (this.getName().equals( JRBaseStyle.PROPERTY_TOP_PADDING)){
//                    methodName = "TopPadding";
//                    oldValue = box.getOwnTopPadding();
//                    box.setTopPadding(newValue);
//                }
//                if (this.getName().equals( JRBaseStyle.PROPERTY_LEFT_PADDING)){
//                    methodName = "LeftBorderColor";
//                    oldValue = box.getOwnLeftPadding();
//                    box.setLeftPadding(newValue);
//                }
//                if (this.getName().equals( JRBaseStyle.PROPERTY_RIGHT_PADDING)){
//                    methodName = "RightBorderColor";
//                    oldValue = box.getOwnRightPadding();
//                    box.setRightPadding(newValue);
//                }
//                if (this.getName().equals( JRBaseStyle.PROPERTY_BOTTOM_PADDING)){
//                    methodName = "BottomBorderColor";
//                    oldValue = box.getOwnBottomPadding();
//                    box.setBottomPadding(newValue);
//                }

            ObjectPropertyUndoableEdit urob =
                    new ObjectPropertyUndoableEdit(
                        box,
                        methodName, 
                        Integer.class,
                        oldValue,newValue);
            // Find the undoRedo manager...
            IReportManager.getInstance().addUndoableEdit(urob);
        }
    }

    @Override
    public boolean isDefaultValue() {
//FIXME
//            if (this.getName().equals( JRBaseStyle.PROPERTY_PADDING)) return null == box.getOwnPadding();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_TOP_PADDING)) return null == box.getOwnTopPadding();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_LEFT_PADDING)) return null == box.getOwnLeftPadding();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_RIGHT_PADDING)) return null == box.getOwnRightPadding();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_BOTTOM_PADDING)) return null == box.getOwnBottomPadding();
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
