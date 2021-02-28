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
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import org.openide.nodes.PropertySupport;

/**
 *
 * @author gtoffoli
 */
public class StartNewColumnProperty extends PropertySupport
    {
            private final JRDesignGroup group;
        
            @SuppressWarnings("unchecked")
            public StartNewColumnProperty(JRDesignGroup group)
            {
                super(JRDesignGroup.PROPERTY_START_NEW_COLUMN,Boolean.class, I18n.getString("BandNode.Property.NewCol"), I18n.getString("BandNode.Property.NewColdetail"), true, true);
                this.group = group;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return group.isStartNewColumn();
            }

            @Override
            public boolean isDefaultValue() {
                return group.isStartNewColumn() == false;
            }

            @Override
            public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
                setPropertyValue(false);
            }

            @Override
            public boolean supportsDefaultValue() {
                return true;
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Boolean)
                {
                    setPropertyValue((Boolean)val);
                }
            }
            
            private void setPropertyValue(boolean val)
            {
                Boolean oldValue = group.isStartNewColumn();
                Boolean newValue = val;
                group.setStartNewColumn(newValue);
                
                ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                group,
                                "StartNewColumn", 
                                Boolean.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                IReportManager.getInstance().addUndoableEdit(urob);
            }
    }
    
