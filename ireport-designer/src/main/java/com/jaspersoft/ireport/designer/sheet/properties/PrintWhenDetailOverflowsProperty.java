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
import net.sf.jasperreports.engine.design.JRDesignElement;

    
/**
 *  Class to manage the JRDesignElement.PROPERTY_PRINT_REPEATED_VALUES property
 */
public final class PrintWhenDetailOverflowsProperty extends BooleanProperty
{
    private JRDesignElement element = null;

    @SuppressWarnings("unchecked")
    public PrintWhenDetailOverflowsProperty(JRDesignElement element)
    {
        super(element);
        this.element = element;
    }
    @Override
    public String getName()
    {
        return JRDesignElement.PROPERTY_PRINT_WHEN_DETAIL_OVERFLOWS;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.PrintWhenDetailOverflows");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.PrintWhenDetailOverflows.");
    }

    @Override
    public Boolean getBoolean()
    {
        return element.isPrintWhenDetailOverflows();
    }

    @Override
    public Boolean getOwnBoolean()
    {
        return element.isPrintWhenDetailOverflows();
    }

    @Override
    public Boolean getDefaultBoolean()
    {
        return true;
    }

    @Override
    public void setBoolean(Boolean isPrint)
    {
        element.setPrintWhenDetailOverflows(isPrint);
    }

}
