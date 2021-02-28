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
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;

/**
 * Class to manage the JRDesignBand.PROPERTY_HEIGHT property
 * 
 * @author gtoffoli
 */
    public class MinHeightToStartNewPageProperty extends PropertySupport
    {
            private final JRDesignGroup group;
        
            @SuppressWarnings("unchecked")
            public MinHeightToStartNewPageProperty(JRDesignGroup group)
            {
                super(JRDesignGroup.PROPERTY_MIN_HEIGHT_TO_START_NEW_PAGE,Integer.class, I18n.getString("BandNode.Property.MinHeight"), I18n.getString("BandNode.Property.MinHeightdetail"), true, true);
                this.group = group;
            }
            
             @Override
            public boolean isDefaultValue() {
                return group.getMinHeightToStartNewPage() == 0;
            }

            @Override
            public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
                setPropertyValue(0);
            }

            @Override
            public boolean supportsDefaultValue() {
                return true;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return group.getMinHeightToStartNewPage();
            }

            // TODO: check page height consistency
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Integer)
                {
                    setPropertyValue((Integer)val);
                }
            }
            
            private void setPropertyValue(int val) throws IllegalArgumentException
            {
                    Integer oldValue = group.getMinHeightToStartNewPage();
                    Integer newValue = val;
                    
                    // Check if the height is too big....
                    if (newValue < 0)
                    {
                        IllegalArgumentException iae = annotateException(I18n.getString("BandNode.Property.Nodemessage")); 
                        throw iae; 
                    }
                    
                    group.setMinHeightToStartNewPage(newValue);
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                group,
                                "MinHeightToStartNewPage", 
                                Integer.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
            }
            
            public IllegalArgumentException annotateException(String msg)
            {
                IllegalArgumentException iae = new IllegalArgumentException(msg); 
                ErrorManager.getDefault().annotate(iae, 
                                        ErrorManager.EXCEPTION,
                                        msg,
                                        msg, null, null); 
                return iae;
            }
    }
