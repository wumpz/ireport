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
package com.jaspersoft.ireport.designer.sheet.properties.style;

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.properties.EnumProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.util.List;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

/**
 * Class to manage the JRDesignElement.PROPERTY_POSITION_TYPE property
 */
public final class VerticalAlignmentProperty extends EnumProperty {

    private final JRBaseStyle style;

    @SuppressWarnings(value = "unchecked")
    public VerticalAlignmentProperty(JRBaseStyle style) {

        super(VerticalAlignEnum.class, style);
        this.style = style;
        setValue("suppressCustomEditor", Boolean.TRUE);
    }



    @Override
    public String getName()
    {
        return JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("AbstractStyleNode.Property.Vertical_Alignment");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("AbstractStyleNode.Property.VerticalDetail");
    }

    @Override
    public Object getPropertyValue()
    {
        return style.getVerticalAlignmentValue();
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
    public void setPropertyValue(Object newValue)
    {
        style.setVerticalAlignment((VerticalAlignEnum)newValue);
    }

    @Override
    public List getTagList()
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(VerticalAlignEnum.TOP, I18n.getString("Global.Property.Top")));
        tags.add(new Tag(VerticalAlignEnum.MIDDLE, I18n.getString("Global.Property.Middle")));
        tags.add(new Tag(VerticalAlignEnum.BOTTOM, I18n.getString("Global.Property.Bottom")));
        return tags;
    }
}
