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
import net.sf.jasperreports.engine.base.JRBaseSubreport;
import net.sf.jasperreports.engine.design.JRDesignSubreport;

/**
 *  Class to manage the JRDesignTextElement.PROPERTY_ITALIC property
 * @author gtoffoli
 */
public final class SubreportUsingCacheProperty extends BooleanProperty
{
    private final JRDesignSubreport subreport;

    @SuppressWarnings("unchecked")
    public SubreportUsingCacheProperty(JRDesignSubreport subreport)
    {
        super(subreport);
        this.subreport = subreport;
    }
    @Override
    public String getName()
    {
        return JRBaseSubreport.PROPERTY_USING_CACHE;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.UsingCache");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.UsingCachedetail");
    }

    @Override
    public Boolean getBoolean()
    {
        return subreport.isUsingCache();
    }

    @Override
    public Boolean getOwnBoolean()
    {
        return subreport.isOwnUsingCache();
    }

    @Override
    public Boolean getDefaultBoolean()
    {
        return null;
    }

    @Override
    public void setBoolean(Boolean isUsingCache)
    {
        subreport.setUsingCache(isUsingCache);
    }

}
