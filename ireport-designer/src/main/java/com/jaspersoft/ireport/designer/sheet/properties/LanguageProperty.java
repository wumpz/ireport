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
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class LanguageProperty extends StringListProperty
{
    private final JasperDesign jasperDesign;

    @SuppressWarnings("unchecked")
    public LanguageProperty(JasperDesign jasperDesign)
    {
        super(jasperDesign);
        this.jasperDesign = jasperDesign;
    }

    @Override
    public String getName()
    {
        return JasperDesign.PROPERTY_LANGUAGE;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("LanguageProperty.Property.Language");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("LanguageProperty.Property.Tooltip");
    }

    @Override
    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(JasperDesign.LANGUAGE_JAVA, "Java"));//FIXMETD confusion between lower case and upper case values
        tags.add(new Tag(JasperDesign.LANGUAGE_GROOVY, "Groovy"));
        tags.add(new Tag("javascript", "JavaScript"));
        return tags;
    }

    @Override
    public String getString()
    {
        return jasperDesign.getLanguage();
    }

    @Override
    public String getOwnString()
    {
        return jasperDesign.getLanguage();
    }

    @Override
    public String getDefaultString()
    {
        return JRReport.LANGUAGE_JAVA;
    }

    @Override
    public void setString(String language)
    {
        jasperDesign.setLanguage(language);
    }

}
