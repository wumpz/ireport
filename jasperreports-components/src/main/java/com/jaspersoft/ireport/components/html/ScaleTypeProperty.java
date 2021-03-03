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
package com.jaspersoft.ireport.components.html;

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.properties.EnumProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.util.List;
import net.sf.jasperreports.components.html.HtmlComponent;
import net.sf.jasperreports.engine.type.ScaleImageEnum;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class ScaleTypeProperty extends EnumProperty
{
    private final HtmlComponent component;

    @SuppressWarnings("unchecked")
    public ScaleTypeProperty(HtmlComponent component)
    {
        super(ScaleImageEnum.class,  component);
        this.component = component;

    }

    @Override
    public String getName()
    {
        return HtmlComponent.PROPERTY_SCALE_TYPE;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.HtmlScaleType");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.HtmlScaleType.desc");
    }

    @Override
    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(null, "<Default>"));
        tags.add(new Tag(ScaleImageEnum.CLIP, I18n.getString("Global.Property.HtmlScaleType.Clip")));
        tags.add(new Tag(ScaleImageEnum.FILL_FRAME, I18n.getString("Global.Property.HtmlScaleType.Fill_Frame")));
        tags.add(new Tag(ScaleImageEnum.RETAIN_SHAPE, I18n.getString("Global.Property.HtmlScaleType.Retain_Shape")));
        tags.add(new Tag(ScaleImageEnum.REAL_HEIGHT, I18n.getString("Global.Property.HtmlScaleType.Real_Height")));
        tags.add(new Tag(ScaleImageEnum.REAL_SIZE, I18n.getString("Global.Property.HtmlScaleType.Real_Size")));
        return tags;
    }

    @Override
    public Object getPropertyValue()
    {
        return component.getScaleType();
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
        component.setScaleType((ScaleImageEnum)alignment);
    }

}
