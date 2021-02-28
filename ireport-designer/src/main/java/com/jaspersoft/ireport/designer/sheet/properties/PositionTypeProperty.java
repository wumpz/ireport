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

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.locale.I18n;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.type.PositionTypeEnum;

    
/**
 *  Class to manage the JRDesignElement.PROPERTY_POSITION_TYPE property
 */
public final class PositionTypeProperty extends EnumProperty
{
    private final JRDesignElement element;

    @SuppressWarnings("unchecked")
    public PositionTypeProperty(JRDesignElement element)
    {
        super(PositionTypeEnum.class, element);
        this.element = element;
    }

    @Override
    public String getName()
    {
        return JRDesignElement.PROPERTY_POSITION_TYPE;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.PositionType");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.PositionTypedetail");
    }

    @Override
    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(PositionTypeEnum.FIX_RELATIVE_TO_TOP, I18n.getString("Global.Property.FixTop")));
        tags.add(new Tag(PositionTypeEnum.FLOAT, I18n.getString("Global.Property.PositionFloat")));
        tags.add(new Tag(PositionTypeEnum.FIX_RELATIVE_TO_BOTTOM, I18n.getString("Global.Property.FixBottom")));
        return tags;
    }

    @Override
    public Object getPropertyValue()
    {
        return element.getPositionTypeValue();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return element.getPositionTypeValue();
    }

    @Override
    public Object getDefaultValue()
    {
        return PositionTypeEnum.FIX_RELATIVE_TO_TOP;
    }

    @Override
    public void setPropertyValue(Object positionType)
    {
        element.setPositionType((PositionTypeEnum)positionType);
    }

}
