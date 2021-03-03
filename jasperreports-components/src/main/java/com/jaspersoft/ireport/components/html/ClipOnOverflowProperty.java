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
package com.jaspersoft.ireport.components.html;

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.properties.BooleanProperty;
import com.jaspersoft.ireport.designer.sheet.properties.EnumProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.util.List;
import net.sf.jasperreports.components.html.HtmlComponent;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class ClipOnOverflowProperty extends BooleanProperty
{
    private final HtmlComponent component;

    @SuppressWarnings("unchecked")
    public ClipOnOverflowProperty(HtmlComponent component)
    {
        super(component);
        this.component = component;
    }

    @Override
    public String getName()
    {
        return HtmlComponent.PROPERTY_CLIP_ON_OVERFLOW;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.clipOnOverflow");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.clipOnOverflow.description");
    }



    @Override
    public Boolean getBoolean() {
        return component.getClipOnOverflow();
    }

    @Override
    public Boolean getOwnBoolean() {
        return getBoolean();
    }

    @Override
    public Boolean getDefaultBoolean() {
        return null;
    }

    @Override
    public void setBoolean(Boolean value) {
        component.setClipOnOverflow(value);
    }

}
