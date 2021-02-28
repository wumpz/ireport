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

import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.sheet.editors.ExpressionPropertyEditor;
import java.beans.PropertyEditor;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class ExpressionProperty extends AbstractProperty
{
    private PropertyEditor editor = null;
    
    public ExpressionProperty(Object object, ExpressionContext context)
    {
        super(String.class, object);
        this.setValue(ExpressionContext.ATTRIBUTE_EXPRESSION_CONTEXT, context);
    }
    
    @SuppressWarnings("unchecked")
    public ExpressionProperty(Object object, JRDesignDataset dataset)
    {
        this(object, new ExpressionContext(dataset));
    }

    @Override
    public Object getPropertyValue()
    {
        JRDesignExpression expression = getExpression();
        
        if (expression == null)
            return null;
        
        return expression.getText();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return getPropertyValue();
    }

    @Override
    public Object getDefaultValue() 
    {
        return null;
    }

    @Override
    public void validate(Object value)
    {
    }

    @Override
    public void setPropertyValue(Object value)
    {
        setExpression(createExpression((String)value));
    }

    @Override
    public PropertyEditor getPropertyEditor() 
    {
        if (editor == null)
        {
            editor = new ExpressionPropertyEditor();
        }
        return editor;
    }
    
    private JRDesignExpression createExpression(String text)
    {
        JRDesignExpression newExp = null;
        
        JRDesignExpression oldExp = getExpression();
        String defaultValueClassName = getDefaultExpressionClassName();
        String valueClassName = oldExp == null || oldExp.getValueClassName() == null  ? defaultValueClassName : oldExp.getValueClassName();

        if (
            (text != null && text.trim().length() > 0) 
            || !defaultValueClassName.equals(valueClassName)
            )
        {
            newExp = new JRDesignExpression();
            newExp.setText(text);
            newExp.setValueClassName(valueClassName);
        }

        return newExp;
    }

    public abstract String getDefaultExpressionClassName();

    public abstract JRDesignExpression getExpression();

    public abstract void setExpression(JRDesignExpression expression);
}
