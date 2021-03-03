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
package com.jaspersoft.ireport.components.table.nodes.properties;

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.properties.EnumProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.util.List;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.WhenNoDataTypeTableEnum;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *  Class to manage the WhenNoDataType property
 */
public final class WhenNoDataTypeProperty extends EnumProperty
{
    private final StandardTable table;

    @SuppressWarnings("unchecked")
    public WhenNoDataTypeProperty(StandardTable table)
    {
        super(WhenNoDataTypeTableEnum.class, table);
        this.table = table;
    }

    @Override
    public String getName()
    {
        return StandardTable.PROPERTY_WHEN_NO_DATA_TYPE;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("table.property.WhenNoDataType.name");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("table.property.WhenNoDataType.desc");
    }

    @Override
    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(WhenNoDataTypeTableEnum.ALL_SECTIONS_NO_DETAIL, I18n.getString("table.property.WhenNoDataType.ALL_SECTIONS_NO_DETAIL")));
        tags.add(new Tag(WhenNoDataTypeTableEnum.BLANK, I18n.getString("table.property.WhenNoDataType.BLANK")));
        return tags;
    }

    @Override
    public Object getPropertyValue()
    {
        return table.getWhenNoDataType() == null ? getDefaultValue() : table.getWhenNoDataType();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return getPropertyValue();
    }

    @Override
    public Object getDefaultValue()
    {
        return WhenNoDataTypeTableEnum.BLANK;
    }

    @Override
    public void setPropertyValue(Object type)
    {
        table.setWhenNoDataType((WhenNoDataTypeTableEnum)type);
    }

}
