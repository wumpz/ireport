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

import com.jaspersoft.ireport.designer.sheet.properties.*;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.engine.base.JRBaseParagraph;
import net.sf.jasperreports.engine.base.JRBaseStyle;

/**
 * Class to manage the JRBaseStyle.PROPERTY_ITALIC property
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */

public class FirstLineIndentProperty extends IntegerProperty{

    private JRBaseStyle style = null;

    @SuppressWarnings("unchecked")
    public FirstLineIndentProperty(JRBaseStyle style)
    {
        super(style);
        this.style = style;
    }
    @Override
    public String getName()
    {
        return JRBaseParagraph.PROPERTY_FIRST_LINE_INDENT;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.FirstLineIndent");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.FirstLineIndent.desc");
    }

    @Override
    public Integer getInteger()
    {
        return style.getParagraph().getFirstLineIndent() == null ? new Integer(0) : style.getParagraph().getFirstLineIndent();
    }

    @Override
    public Integer getOwnInteger()
    {
        return style.getParagraph().getOwnFirstLineIndent() == null ? new Integer(0) : style.getParagraph().getOwnFirstLineIndent();
    }

    @Override
    public Integer getDefaultInteger()
    {
        return new Integer(0);
    }

    @Override
    public void setInteger(Integer num)
    {
        if (num == null || num.intValue() == 0)
        {
            style.getParagraph().setFirstLineIndent(null);
        }
        else
        {
            style.getParagraph().setFirstLineIndent(num);
        }
    }

    @Override
    public void validateInteger(Integer value) {
        return;
    }
    
}
