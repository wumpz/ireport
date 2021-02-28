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
public class ReprintHeaderProperty  extends PropertySupport
    {
            private final JRDesignGroup group;
        
            @SuppressWarnings("unchecked")
            public ReprintHeaderProperty(JRDesignGroup group)
            {
                super(JRDesignGroup.PROPERTY_REPRINT_HEADER_ON_EACH_PAGE,Boolean.class, I18n.getString("BandNode.Property.ReprintHeader"), I18n.getString("BandNode.Property.ReprintHeaderdetail"), true, true);
                this.group = group;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return group.isReprintHeaderOnEachPage();
            }

            @Override
            public boolean isDefaultValue() {
                return group.isReprintHeaderOnEachPage() == false;
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
                Boolean oldValue = group.isReprintHeaderOnEachPage();
                Boolean newValue = val;
                group.setReprintHeaderOnEachPage(newValue);
                
                ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                group,
                                "ReprintHeaderOnEachPage", 
                                Boolean.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                IReportManager.getInstance().addUndoableEdit(urob);
            }
    }