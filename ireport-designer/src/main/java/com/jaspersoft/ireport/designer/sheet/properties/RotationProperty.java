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
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.type.RotationEnum;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class RotationProperty extends EnumProperty
{
    private final JRDesignTextElement element;

    @SuppressWarnings("unchecked")
    public RotationProperty(JRDesignTextElement element)
    {
        super(RotationEnum.class, element);
        this.element = element;
    }

    @Override
    public String getName()
    {
        return JRBaseStyle.PROPERTY_ROTATION;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.Rotation");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.Rotationdetail");
    }

    @Override
    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(RotationEnum.NONE, I18n.getString("Global.Property.None")));
        tags.add(new Tag(RotationEnum.LEFT, I18n.getString("Global.Property.Left")));
        tags.add(new Tag(RotationEnum.RIGHT, I18n.getString("Global.Property.Right")));
        tags.add(new Tag(RotationEnum.UPSIDE_DOWN, I18n.getString("Global.Property.UpsideDown")));
        return tags;
    }

    @Override
    public Object getPropertyValue()
    {
        return element.getRotationValue();
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
    public void setPropertyValue(Object rotation)
    {
        element.setRotation((RotationEnum)rotation);
    }

}
