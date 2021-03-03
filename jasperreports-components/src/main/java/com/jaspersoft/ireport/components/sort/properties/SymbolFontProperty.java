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
package com.jaspersoft.ireport.components.sort.properties;
 
import com.jaspersoft.ireport.designer.sheet.properties.AbstractFontProperty;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.components.sort.SortComponent;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */


public class SymbolFontProperty extends AbstractFontProperty
{
    private final SortComponent element;
        
    
    public SymbolFontProperty(SortComponent element, JasperDesign jasperDesign)
    {
        super(element, jasperDesign);
        this.element = element;
    }

    @Override
    public String getName()
    {
        return SortComponent.PROPERTY_SYMBOL_FONT;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("SymbolFontProperty.name");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("SymbolFontProperty.desc");
    }

    @Override
    public JRFont getFont()
    {
        return element.getSymbolFont();
    }

    @Override
    public void setFont(JRFont font)
    {
        element.setSymbolFont(font);
    }
    
}

