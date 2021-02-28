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
import net.sf.jasperreports.engine.base.JRBaseBreak;
import net.sf.jasperreports.engine.design.JRDesignBreak;
import net.sf.jasperreports.engine.type.BreakTypeEnum;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class BreakTypeProperty extends EnumProperty
{
    private final JRDesignBreak breakElement;

    @SuppressWarnings("unchecked")
    public BreakTypeProperty(JRDesignBreak breakElement)
    {
        super( BreakTypeEnum.class, breakElement);
        this.breakElement = breakElement;
    }

    @Override
    public String getName()
    {
        return JRBaseBreak.PROPERTY_TYPE;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.BreakType");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.BreakType");
    }

    @Override
    public List getTagList() 
    {
        List tags = new java.util.ArrayList();

        tags.add(new Tag(BreakTypeEnum.PAGE, I18n.getString("Global.Property.BreakTypePage")));
        tags.add(new Tag(BreakTypeEnum.COLUMN, I18n.getString("Global.Property.BreakTypeColumn")));
        return tags;
    }

    @Override
    public Object getPropertyValue()
    {
        return breakElement.getTypeValue();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return breakElement.getTypeValue();
    }

    @Override
    public Object getDefaultValue()
    {
        return BreakTypeEnum.PAGE;
    }

    @Override
    public void setPropertyValue(Object type)
    {
        breakElement.setType((BreakTypeEnum)type);
    }

  
}
