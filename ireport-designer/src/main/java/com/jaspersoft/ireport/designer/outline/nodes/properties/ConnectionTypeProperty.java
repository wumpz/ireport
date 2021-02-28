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
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import org.openide.nodes.PropertySupport;

    
/**
 *  Gost property to enable/disable datasource/connection expressions (PROPERTY_CONNECTION_TYPE)
 */
public final class ConnectionTypeProperty extends PropertySupport.ReadWrite {

    private final JRDesignSubreport element;
    PropertyEditor editor = null;

    @SuppressWarnings("unchecked")
    public ConnectionTypeProperty(JRDesignSubreport element)
    {
        super("PROPERTY_CONNECTION_TYPE", Integer.class,
              "Connection type",
              "You can choose to fill this subreport using a connection or a datasource or without providing any data.");
        this.element = element;

        setValue("suppressCustomEditor", true);
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {

        if (element.getConnectionExpression() == null &&
            element.getDataSourceExpression() == null) return new Integer(2);

        if (element.getConnectionExpression() == null &&
            element.getDataSourceExpression() != null) return new Integer(1);

        return new Integer(0);
    }


    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        if (val instanceof Integer)
        {
            switch ( ((Integer)val).intValue() )
            {
                case 0: 
                {
                    // Save old datasource expression if not null.. 
                    JRDesignExpression oldExp = (JRDesignExpression)element.getDataSourceExpression();
                    JRDesignExpression newExp = new JRDesignExpression();

                    ObjectPropertyUndoableEdit urob = new ObjectPropertyUndoableEdit(
                            element, "ConnectionExpression", JRExpression.class,
                            null,newExp);

                    if (oldExp != null)
                    {
                        ObjectPropertyUndoableEdit urob2 = new ObjectPropertyUndoableEdit(
                                element, "DataSourceExpression", JRExpression.class,
                                oldExp,null);

                        urob.concatenate(urob2);
                    }
                    element.setConnectionExpression(newExp);

                    IReportManager.getInstance().addUndoableEdit(urob);

                    break;
                }
                case 1:
                {
                    // Save old datasource expression if not null.. 
                    JRDesignExpression oldExp = (JRDesignExpression)element.getConnectionExpression();
                    JRDesignExpression newExp = new JRDesignExpression();

                    ObjectPropertyUndoableEdit urob = new ObjectPropertyUndoableEdit(
                            element, "DataSourceExpression", JRExpression.class,
                            null,newExp);

                    if (oldExp != null)
                    {
                        ObjectPropertyUndoableEdit urob2 = new ObjectPropertyUndoableEdit(
                                element, "ConnectionExpression", JRExpression.class,
                                oldExp,null);

                        urob.concatenate(urob2);
                    }
                    element.setDataSourceExpression(newExp);

                    IReportManager.getInstance().addUndoableEdit(urob);

                    break;
                }
                case 2:
                {
                    // Save old datasource expression if not null.. 
                    JRDesignExpression oldExp = (JRDesignExpression)element.getConnectionExpression();
                    ObjectPropertyUndoableEdit urob = null;
                    if (oldExp != null)
                    {
                        urob = new ObjectPropertyUndoableEdit(
                                element, "ConnectionExpression", JRExpression.class,
                                oldExp,null);
                    }


                    oldExp = (JRDesignExpression)element.getDataSourceExpression();
                    ObjectPropertyUndoableEdit urob2 = null;
                    if (oldExp != null)
                    {
                        urob2 = new ObjectPropertyUndoableEdit(
                                element, "DataSourceExpression", JRExpression.class,
                                oldExp,null);
                    }

                    if (urob != null && urob2 != null) urob.concatenate(urob2);
                    else if (urob2 != null) urob = urob2;

                    element.setDataSourceExpression(null);
                    element.setConnectionExpression(null);

                    IReportManager.getInstance().addUndoableEdit(urob);
                    break;
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public PropertyEditor getPropertyEditor() {

        if (editor == null)
        {
            java.util.List classes = new ArrayList();
            classes.add(new Tag(new Integer(0), "Use a connection expression"));
            classes.add(new Tag(new Integer(1), "Use a datasource expression"));
            classes.add(new Tag(new Integer(2), "Don't pass data."));

            editor = new ComboBoxPropertyEditor(false, classes);
        }
        return editor;
    }
}
