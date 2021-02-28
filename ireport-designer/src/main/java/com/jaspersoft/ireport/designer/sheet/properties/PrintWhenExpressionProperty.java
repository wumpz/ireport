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
import com.jaspersoft.ireport.designer.sheet.properties.ExpressionProperty;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;

    
/**
 * Class to manage the JRDesignElement.PROPERTY_PRINT_WHEN_EXPRESSION property
 */
public final class PrintWhenExpressionProperty extends ExpressionProperty 
{
    private final JRDesignElement element;

    public PrintWhenExpressionProperty(JRDesignElement element, JRDesignDataset dataset)
    {
        super(element, dataset);
        this.element = element;
        
        if (ModelUtils.getTopElementGroup(element) instanceof JRDesignCellContents)
        {
            JRDesignCellContents contents = (JRDesignCellContents) ModelUtils.getTopElementGroup(element);
            this.setValue(ExpressionContext.ATTRIBUTE_EXPRESSION_CONTEXT, new ExpressionContext(contents.getOrigin().getCrosstab()));
        }
    }

    @Override
    public String getName()
    {
        return JRDesignElement.PROPERTY_PRINT_WHEN_EXPRESSION;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.PrintWhenExpressio");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.PrintWhenExpressiondetail");
    }

    @Override
    public String getDefaultExpressionClassName()
    {
        return Boolean.class.getName();
    }

    @Override
    public JRDesignExpression getExpression()
    {
        return (JRDesignExpression)element.getPrintWhenExpression();
    }

    @Override
    public void setExpression(JRDesignExpression expression)
    {
        element.setPrintWhenExpression(expression);
    }

}
