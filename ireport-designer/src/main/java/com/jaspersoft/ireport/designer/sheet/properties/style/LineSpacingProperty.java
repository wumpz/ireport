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
import com.jaspersoft.ireport.designer.sheet.properties.ByteProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.util.List;
import net.sf.jasperreports.engine.base.JRBaseParagraph;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.type.LineSpacingEnum;

/**
 * Class to manage the JRDesignElement.PROPERTY_POSITION_TYPE property
 */
final public class LineSpacingProperty extends ByteProperty {


    private JRBaseStyle style = null;

    @SuppressWarnings("unchecked")
    public LineSpacingProperty(JRBaseStyle style)
    {
        super(style);
        this.style = style;
    }
    @Override
    public String getName()
    {
        return JRBaseParagraph.PROPERTY_LINE_SPACING;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.LineSpacing");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.LineSpacingdetail");
    }

    @Override
    public List getTagList()
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(LineSpacingEnum.SINGLE.getValueByte(), I18n.getString("Global.Property.Single")));
        tags.add(new Tag(LineSpacingEnum.ONE_AND_HALF.getValueByte(), I18n.getString("Global.Property.1.5")));
        tags.add(new Tag(LineSpacingEnum.DOUBLE.getValueByte(), I18n.getString("Global.Property.Double")));
        tags.add(new Tag(LineSpacingEnum.AT_LEAST.getValueByte(), I18n.getString("Global.Property.LineSpacingAtLeast")));
        tags.add(new Tag(LineSpacingEnum.FIXED.getValueByte(), I18n.getString("Global.Property.LineSpacingFixed")));
        tags.add(new Tag(LineSpacingEnum.PROPORTIONAL.getValueByte(), I18n.getString("Global.Property.LineSpacingProportional")));

        return tags;
    }

    @Override
    public Byte getByte()
    {
        return (style.getParagraph().getLineSpacing() != null) ?
            style.getParagraph().getLineSpacing().getValueByte() : null;
    }

    @Override
    public Byte getOwnByte()
    {
        return (style.getParagraph().getOwnLineSpacing() != null) ?
            style.getParagraph().getOwnLineSpacing().getValueByte() : null;
    }

    @Override
    public Byte getDefaultByte()
    {
        return null;
    }

    @Override
    public void setByte(Byte lineSpacing)
    {
        style.getParagraph().setLineSpacing(LineSpacingEnum.getByValue(lineSpacing));
    }

}
