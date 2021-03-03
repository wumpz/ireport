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
package com.jaspersoft.ireport.components.barcode.barbecue;

import com.jaspersoft.ireport.designer.sheet.properties.BooleanProperty;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.components.barbecue.StandardBarbecueComponent;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseStyle;

/**
 * Class to manage the JRBaseStyle.PROPERTY_ITALIC property
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */

public class BarbecueDrawTextProperty extends BooleanProperty{

    private final StandardBarbecueComponent component;

    @SuppressWarnings("unchecked")
    public BarbecueDrawTextProperty(StandardBarbecueComponent component)
    {
        super(component);
        this.component = component;
    }
    @Override
    public String getName()
    {
        return StandardBarbecueComponent.PROPERTY_DRAW_TEXT;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("barbecue.property.drawText.name");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("barbecue.property.drawText.description");
    }

    @Override
    public Boolean getBoolean()
    {
        return getOwnBoolean();
    }

    @Override
    public Boolean getOwnBoolean()
    {
        return component.isDrawText();
    }

    @Override
    public boolean supportsDefaultValue() {
        return false;
    }

    @Override
    public Boolean getDefaultBoolean()
    {
        return false;
    }

    @Override
    public void setBoolean(Boolean isItalic)
    {
        if (isItalic != null)
        {
            component.setDrawText(isItalic.booleanValue());
        }
        else
        {
            component.setDrawText(false);
        }
    }
    
}
