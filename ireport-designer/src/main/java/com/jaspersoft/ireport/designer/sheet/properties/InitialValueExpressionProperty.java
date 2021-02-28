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
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.type.CalculationEnum;


/**
 * Class to manage the JRDesignVariable.PROPERTY_INITIAL_VALUE_EXPRESSION property
 * @author gtoffoli
 */
public final class InitialValueExpressionProperty extends ExpressionProperty 
{
    private final JRDesignVariable variable;
    private final JRDesignDataset dataset;

    public InitialValueExpressionProperty(JRDesignVariable variable, JRDesignDataset dataset)
    {
        super(variable, dataset);
        this.variable = variable;
        this.dataset = dataset;
    }

    @Override
    public String getName()
    {
        return JRDesignVariable.PROPERTY_INITIAL_VALUE_EXPRESSION;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("InitialValueExpressionProperty.Property.InitialValueExpression");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("InitialValueExpressionProperty.Property.InitialValueExpression");
    }

    @Override
    public String getDefaultExpressionClassName()
    {
        return Object.class.getName();
    }

    @Override
    public JRDesignExpression getExpression()
    {
        return (JRDesignExpression)variable.getInitialValueExpression();
    }

    @Override
    public void setExpression(JRDesignExpression expression)
    {
        // This avoid incompatibilities with the
        // variable...
        if (expression != null)
        {
            /*
             if (variable.getCalculationValue() == CalculationEnum.COUNT ||
                variable.getCalculationValue() == CalculationEnum.DISTINCT_COUNT)
            {
                expression.setValueClassName(variable.getValueClassName());
            }
            else
            {
                
            }
             */
            expression.setValueClassName(variable.getValueClassName());
        }
        variable.setInitialValueExpression(expression);
    }

    @Override
    public boolean canWrite()
    {
        return !variable.isSystemDefined();
    }

}
