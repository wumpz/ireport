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

import com.jaspersoft.ireport.designer.sheet.properties.ExpressionProperty;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignParameter;


/**
 *  Class to manage the JRDesignParameter.PROPERTY_DEFAULT_VALUE_EXPRESSION property
 */
public final class DefaultValueExpressionProperty extends ExpressionProperty 
{
    private final JRDesignParameter parameter;
    private final JRDesignDataset dataset;

    public DefaultValueExpressionProperty(JRDesignParameter parameter, JRDesignDataset dataset)
    {
        super(parameter, dataset);
        this.parameter = parameter;
        this.dataset = dataset;
    }

    @Override
    public String getName()
    {
        return JRDesignParameter.PROPERTY_DEFAULT_VALUE_EXPRESSION;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("DefaultValueExpressionProperty.Property.DVE");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("DefaultValueExpressionProperty.Property.DVE");
    }

    @Override
    public String getDefaultExpressionClassName()
    {
        return Object.class.getName();
    }

    @Override
    public JRDesignExpression getExpression()
    {
        return (JRDesignExpression)parameter.getDefaultValueExpression();
    }

    @Override
    public void setExpression(JRDesignExpression expression)
    {
        expression.setValueClassName(parameter.getValueClassName());
        parameter.setDefaultValueExpression(expression);
    }

    @Override
    public boolean canWrite()
    {
        return !parameter.isSystemDefined();
    }

}
