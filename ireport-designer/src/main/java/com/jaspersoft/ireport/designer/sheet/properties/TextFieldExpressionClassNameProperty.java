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
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import org.openide.nodes.PropertySupport;

/**
 *
 * Class to manage the JRDesignVariable.PROPERTY_VALUE_CLASS_NAME property
 */
public class TextFieldExpressionClassNameProperty  extends PropertySupport.ReadWrite {

    // FIXME: refactorize this
    private final JRDesignTextField element;
    PropertyEditor editor = null;

    
    public TextFieldExpressionClassNameProperty(JRDesignTextField element)
    {
        super(JRDesignExpression.PROPERTY_VALUE_CLASS_NAME, String.class,
              I18n.getString("Expression_Class"),
              I18n.getString("Expression_Class"));
        this.element = element;

        setValue("canEditAsText", true);
        setValue("oneline", true);
        setValue("suppressCustomEditor", false);
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {

        if (element.getExpression() == null) return "java.lang.String";
        if (element.getExpression().getValueClassName() == null) return "java.lang.String";
        return element.getExpression().getValueClassName();
    }


    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        JRDesignExpression oldExp =  (JRDesignExpression)element.getExpression();
        JRDesignExpression newExp = null;
        //System.out.println("Setting as value: " + val);

        String newVal = (val != null) ? val+"" : "";
        newVal = newVal.trim();

        if ( newVal.equals("") )
        {
            newVal = null;
        }

        newExp = new JRDesignExpression();
        newExp.setText( (oldExp != null) ? oldExp.getText() : null );
        newExp.setValueClassName( newVal );
        element.setExpression(newExp);

        ObjectPropertyUndoableEdit urob =
                    new ObjectPropertyUndoableEdit(
                        element,
                        "Expression",
                        JRExpression.class,
                        oldExp,newExp);
            // Find the undoRedo manager...
            IReportManager.getInstance().addUndoableEdit(urob);

        //System.out.println("Done: " + val);
    }

    @Override
    public boolean isDefaultValue() {
        return element.getExpression() == null ||
               element.getExpression().getValueClassName() == null ||
               element.getExpression().getValueClassName().equals("java.lang.String");
    }

    @Override
    public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
        setValue(null);
        editor.setValue("java.lang.String");
    }

    @Override
    public boolean supportsDefaultValue() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PropertyEditor getPropertyEditor() {

        if (editor == null)
        {
            java.util.List classes = new ArrayList();
            classes.add(new Tag("java.lang.String"));
            classes.add(new Tag("java.lang.Boolean"));
            classes.add(new Tag("java.lang.Byte"));
            classes.add(new Tag("java.util.Date"));
            classes.add(new Tag("java.sql.Timestamp"));
            classes.add(new Tag("java.sql.Time"));
            classes.add(new Tag("java.lang.Double"));
            classes.add(new Tag("java.lang.Float"));
            classes.add(new Tag("java.lang.Integer"));
            classes.add(new Tag("java.lang.Long"));
            classes.add(new Tag("java.lang.Short"));
            classes.add(new Tag("java.math.BigDecimal"));
            classes.add(new Tag("java.lang.Number"));

            editor = new ComboBoxPropertyEditor(false, classes);
        }
        return editor;
    }
    
}
