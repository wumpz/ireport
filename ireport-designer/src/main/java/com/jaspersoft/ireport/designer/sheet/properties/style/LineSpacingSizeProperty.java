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

import com.jaspersoft.ireport.designer.sheet.properties.FloatProperty;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.engine.base.JRBaseParagraph;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.type.LineSpacingEnum;

/**
 * Class to manage the JRBaseStyle.PROPERTY_ITALIC property
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */

public class LineSpacingSizeProperty extends FloatProperty{

    private JRBaseStyle style = null;

    @SuppressWarnings("unchecked")
    public LineSpacingSizeProperty(JRBaseStyle style)
    {
        super(style);
        this.style = style;
    }
    @Override
    public String getName()
    {
        return JRBaseParagraph.PROPERTY_LINE_SPACING_SIZE;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.LineSpacingSize");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.LineSpacingSize.desc");
    }

    @Override
    public Float getFloat()
    {
        return style.getParagraph().getLineSpacingSize() == null ? new Float(0) : style.getParagraph().getLineSpacingSize();
    }

    @Override
    public Float getOwnFloat()
    {
        return style.getParagraph().getOwnLineSpacingSize() == null ? new Float(0) : style.getParagraph().getOwnLineSpacingSize();
    }

    @Override
    public Float getDefaultFloat()
    {
        return new Float(0);
    }

    @Override
    public void setFloat(Float num)
    {
        if (num == null || num.intValue() == 0)
        {
            style.getParagraph().setLineSpacingSize(null);
        }
        else
        {
            style.getParagraph().setLineSpacingSize(num);
        }
    }

    @Override
    public void validateFloat(Float value) {
        return;
    }

    @Override
    public boolean canWrite() {

        LineSpacingEnum val = style.getParagraph().getLineSpacing();

        return val != null &&
               val != LineSpacingEnum.DOUBLE &&
               val != LineSpacingEnum.ONE_AND_HALF &&
               val != LineSpacingEnum.SINGLE;

    }




}
