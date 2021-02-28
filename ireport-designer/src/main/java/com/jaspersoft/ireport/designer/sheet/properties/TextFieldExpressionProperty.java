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

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignTextField;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class TextFieldExpressionProperty extends ExpressionProperty 
{
    private final JRDesignTextField textField;

    public TextFieldExpressionProperty(JRDesignTextField textField, JRDesignDataset dataset)
    {
        super(textField, dataset);
        this.textField = textField;
        
        // if this textfield is child of a crosstab replace the expression context..
        if (ModelUtils.getTopElementGroup(textField) instanceof JRDesignCellContents)
        {
            JRDesignCellContents contents = (JRDesignCellContents) ModelUtils.getTopElementGroup(textField);
            this.setValue(ExpressionContext.ATTRIBUTE_EXPRESSION_CONTEXT, new ExpressionContext(contents.getOrigin().getCrosstab()));
        }
    }


    @Override
    public String getName()
    {
        return JRDesignTextField.PROPERTY_EXPRESSION;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.TextFieldExpression");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.TextFieldExpressiondetail");
    }

    @Override
    public String getDefaultExpressionClassName()
    {
        return String.class.getName();
    }

    @Override
    public JRDesignExpression getExpression()
    {
        return (JRDesignExpression)textField.getExpression();
    }

    @Override
    public void setExpression(JRDesignExpression expression)
    {
        textField.setExpression(expression);
    }

}
