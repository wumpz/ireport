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

import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.engine.base.JRBaseParagraph;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.type.LineSpacingEnum;

/**
 * Class to manage the JRBaseStyle.PROPERTY_ITALIC property
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */

public class LineSpacingSizeProperty extends FloatProperty{

    private JRDesignTextElement element = null;

    @SuppressWarnings("unchecked")
    public LineSpacingSizeProperty(JRDesignTextElement element)
    {
        super(element);
        this.element = element;
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
        return element.getParagraph().getLineSpacingSize() == null ? new Float(0) : element.getParagraph().getLineSpacingSize();
    }

    @Override
    public Float getOwnFloat()
    {
        return element.getParagraph().getOwnLineSpacingSize() == null ? new Float(0) : element.getParagraph().getOwnLineSpacingSize();
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
            element.getParagraph().setLineSpacingSize(null);
        }
        else
        {
            element.getParagraph().setLineSpacingSize(num);
        }
    }

    @Override
    public void validateFloat(Float value) {
        return;
    }

    @Override
    public boolean canWrite() {

        LineSpacingEnum val = element.getParagraph().getLineSpacing();

        return val != null &&
               val != LineSpacingEnum.DOUBLE &&
               val != LineSpacingEnum.ONE_AND_HALF &&
               val != LineSpacingEnum.SINGLE;

    }




}
