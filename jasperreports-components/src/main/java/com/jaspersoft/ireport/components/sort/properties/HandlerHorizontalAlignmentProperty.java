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
package com.jaspersoft.ireport.components.sort.properties;

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.properties.EnumProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.util.List;
import net.sf.jasperreports.components.sort.SortComponent;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class HandlerHorizontalAlignmentProperty extends EnumProperty
{
    private final SortComponent component;

    @SuppressWarnings("unchecked")
    public HandlerHorizontalAlignmentProperty(SortComponent component)
    {
        super(HorizontalAlignEnum.class,  component);
        this.component = component;
    }

    @Override
    public String getName()
    {
        return SortComponent.PROPERTY_HANDLER_HORIZONTAL_ALIGN;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.HandlerHorizontalAlignment");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.HandlerHorizontalAlignment.desc");
    }

    @Override
    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(HorizontalAlignEnum.LEFT, I18n.getString("Global.Property.Left")));
        tags.add(new Tag(HorizontalAlignEnum.CENTER, I18n.getString("Global.Property.Center")));
        tags.add(new Tag(HorizontalAlignEnum.RIGHT, I18n.getString("Global.Property.Right")));
       // tags.add(new Tag(HorizontalAlignEnum.JUSTIFIED, I18n.getString("Global.Property.Justified")));
        return tags;
    }

    @Override
    public Object getPropertyValue()
    {
        return component.getHandlerHorizontalAlign();
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
    public void setPropertyValue(Object alignment)
    {
        component.setHandlerHorizontalAlign((HorizontalAlignEnum)alignment);
    }

}
