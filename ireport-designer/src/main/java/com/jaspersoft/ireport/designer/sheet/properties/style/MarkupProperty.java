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
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.locale.I18n;
import java.util.List;
import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.base.JRBaseStyle;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class MarkupProperty extends StringListProperty
{
    private final JRBaseStyle style;

    @SuppressWarnings("unchecked")
    public MarkupProperty(JRBaseStyle style)
    {
        super(style);
        this.style = style;
    }

    @Override
    public String getName()
    {
        return JRBaseStyle.PROPERTY_MARKUP;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.Markup");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.Markupdetail");
    }

    @Override
    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(JRCommonText.MARKUP_NONE, JRCommonText.MARKUP_NONE));
        tags.add(new Tag(JRCommonText.MARKUP_STYLED_TEXT, JRCommonText.MARKUP_STYLED_TEXT));
        tags.add(new Tag(JRCommonText.MARKUP_RTF, JRCommonText.MARKUP_RTF));
        tags.add(new Tag(JRCommonText.MARKUP_HTML, JRCommonText.MARKUP_HTML));
        return tags;
    }

    @Override
    public String getString()
    {
        return style.getMarkup();
    }

    @Override
    public String getOwnString()
    {
        return style.getOwnMarkup();
    }

    @Override
    public String getDefaultString()
    {
        return null;
    }

    @Override
    public void setString(String markup)
    {
        style.setMarkup(markup);
    }

}
