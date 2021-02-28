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
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.base.JRBaseLine;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.type.LineDirectionEnum;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class LineDirectionProperty extends EnumProperty
{
    private final JRDesignLine line;

    @SuppressWarnings("unchecked")
    public LineDirectionProperty(JRDesignLine line)
    {
        super(LineDirectionEnum.class, line);
        this.line = line;
    }

    @Override
    public String getName()
    {
        return JRBaseLine.PROPERTY_DIRECTION;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.Direction");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.Directiondetail");
    }

    @Override
    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(LineDirectionEnum.TOP_DOWN, I18n.getString("Global.Property.TopDown")));
        tags.add(new Tag(LineDirectionEnum.BOTTOM_UP, I18n.getString("Global.Property.BottomUp")));
        return tags;
    }

    @Override
    public Object getPropertyValue()
    {
        return line.getDirectionValue();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return line.getDirectionValue();
    }

    @Override
    public Object getDefaultValue()
    {
        return LineDirectionEnum.TOP_DOWN;
    }

    @Override
    public void setPropertyValue(Object direction)
    {
        line.setDirection((LineDirectionEnum)direction);
    }

}
