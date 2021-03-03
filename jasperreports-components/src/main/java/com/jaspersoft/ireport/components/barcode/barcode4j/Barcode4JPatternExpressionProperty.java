/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2013 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.ireport.components.barcode.barcode4j;

import com.jaspersoft.ireport.designer.sheet.properties.ExpressionProperty;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.components.barcode4j.BarcodeComponent;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;

    
/**
 * Class to manage the JRDesignSubreport.PROPERTY_CONNECTION_EXPRESSION property.
 */
public final class Barcode4JPatternExpressionProperty extends ExpressionProperty
{
    private final BarcodeComponent component;

    public Barcode4JPatternExpressionProperty(BarcodeComponent component, JRDesignDataset dataset)
    {
        super(component, dataset);
        this.component = component;
    }

    @Override
    public String getName()
    {
        return "patternExpression";
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("barcode4j.property.patternExpression.name");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("barcode4j.property.patternExpression.description");
    }

    @Override
    public String getDefaultExpressionClassName()
    {
        return String.class.getName();
    }

    @Override
    public JRDesignExpression getExpression()
    {
        return (JRDesignExpression)component.getPatternExpression();
    }

    @Override
    public void setExpression(JRDesignExpression expression)
    {
        component.setPatternExpression(expression);
    }
    
}
