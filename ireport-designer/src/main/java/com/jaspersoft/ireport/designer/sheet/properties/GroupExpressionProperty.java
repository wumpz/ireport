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
import net.sf.jasperreports.engine.design.JRDesignGroup;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class GroupExpressionProperty extends ExpressionProperty 
{
    private final JRDesignGroup group;

    public GroupExpressionProperty(JRDesignGroup group, JRDesignDataset dataset)
    {
        super(group, dataset);
        this.group = group;
    }

    @Override
    public String getName()
    {
        return JRDesignGroup.PROPERTY_EXPRESSION;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("GroupExpressionProperty.Property.GroupExpression");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("GroupExpressionProperty.Property.GroupExpressiondetail");
    }

    @Override
    public String getDefaultExpressionClassName()
    {
        return Object.class.getName();
    }

    @Override
    public JRDesignExpression getExpression()
    {
        return (JRDesignExpression)group.getExpression();
    }

    @Override
    public void setExpression(JRDesignExpression expression)
    {
        group.setExpression(expression);
    }

}
