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
import net.sf.jasperreports.engine.JRImageAlignment;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.type.VerticalImageAlignEnum;

    
/**
 *  Class to manage the JRDesignElement.PROPERTY_POSITION_TYPE property
 */
public final class VerticalGraphicAlignmentProperty extends EnumProperty
{
    private final JRImageAlignment element;

    @SuppressWarnings("unchecked")
    public VerticalGraphicAlignmentProperty(JRImageAlignment element)
    {
        super(VerticalImageAlignEnum.class, element);
        this.element = element;
    }

    @Override
    public String getName()
    {
        return JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.VerticalAlignment");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.VerticalAlignmentdetail");
    }

    @Override
    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(VerticalImageAlignEnum.TOP, I18n.getString("Global.Property.Top")));
        tags.add(new Tag(VerticalImageAlignEnum.MIDDLE, I18n.getString("Global.Property.Middle")));
        tags.add(new Tag(VerticalImageAlignEnum.BOTTOM, I18n.getString("Global.Property.Bottom")));
        //tags.add(new Tag(new Byte(JRAlignment.VERTICAL_ALIGN_JUSTIFIED), I18n.getString("Global.Property.Justified")));
        return tags;
    }

    @Override
    public Object getPropertyValue()
    {
        return element.getVerticalImageAlign();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return element.getOwnVerticalImageAlign();
    }

    @Override
    public Object getDefaultValue()
    {
        return null;
    }

    @Override
    public void setPropertyValue(Object alignment)
    {
        element.setVerticalImageAlign((VerticalImageAlignEnum)alignment);
    }

}
