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
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.design.JRDesignGenericElement;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class GenericElementTypeNameSpaceProperty extends StringProperty
{
    private final JRDesignGenericElement element;

    @SuppressWarnings("unchecked")
    public GenericElementTypeNameSpaceProperty(JRDesignGenericElement element)
    {
        super(element);
        this.element = element;
        setValue("canEditAsText", true);
        setValue("oneline", true);
        setValue("suppressCustomEditor", false);
    }

    @Override
    public String getName()
    {
        return JRDesignGenericElement.PROPERTY_GENERIC_TYPE+"_namespace";
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.GenericTypeNameSpace");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.GenericTypeNameSpaceDetail");
    }

    @Override
    public String getString()
    {
        return getOwnString();
    }

    @Override
    public String getOwnString()
    {
        if (element.getGenericType() != null)
            return element.getGenericType().getNamespace();
        else
            return null;
    }

    @Override
    public String getDefaultString()
    {
        return null;
    }

    @Override
    public void setString(String namespace)
    {
        String name = "";
        if (element.getGenericType() != null)
        {
            name = element.getGenericType().getName();
        }
        JRGenericElementType type = new JRGenericElementType(namespace,name);
        element.setGenericType(type);
    }

    @Override
    public boolean supportsDefaultValue() {
        return false;
    }



}
