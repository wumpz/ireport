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
package com.jaspersoft.ireport.components.spiderchart.properties;

import com.jaspersoft.ireport.designer.sheet.properties.AbstractFontProperty;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.components.spiderchart.StandardSpiderPlot;
import net.sf.jasperreports.engine.JRFont;
    
    
/**
 * @autor Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class LabelFontProperty extends AbstractFontProperty
{
    private final StandardSpiderPlot settings;
        
    public LabelFontProperty(StandardSpiderPlot settings)
    {
        super(settings, null);
        this.settings = settings;
    }
    
    @Override
    public String getName()
    {
        return StandardSpiderPlot.PROPERTY_LABEL_FONT;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.labelFont");
    }

    @Override
    public String getShortDescription()
    {
        return getDisplayName();
    }

    @Override
    public JRFont getFont()
    {
        return settings.getLabelFont();
    }

    @Override
    public void setFont(JRFont font)
    {
        settings.setLabelFont(font);
    }

}
