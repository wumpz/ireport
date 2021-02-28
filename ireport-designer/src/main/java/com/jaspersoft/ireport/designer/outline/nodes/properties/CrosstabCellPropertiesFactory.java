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
package com.jaspersoft.ireport.designer.outline.nodes.properties;

import com.jaspersoft.ireport.designer.sheet.properties.CellBackcolorProperty;
import com.jaspersoft.ireport.designer.sheet.properties.CellModeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.CellStyleProperty;
import com.jaspersoft.ireport.designer.ModelUtils;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Sheet;

/**
 *
 * @author gtoffoli
 */
public class CrosstabCellPropertiesFactory {

    /**
     * Get the GraphicElement properties...
     */
    public static List<Sheet.Set> getCrosstabCellPropertySets(JRDesignCellContents cell, JasperDesign jd)
    {
        JRDesignDataset dataset = ModelUtils.getElementDataset(cell.getOrigin().getCrosstab(), jd);
        
        List<Sheet.Set> list = new ArrayList<Sheet.Set>();
        Sheet.Set propertySet = Sheet.createPropertiesSet();
        propertySet.setName("CELL_PROPERTIES");
        propertySet.setDisplayName("Cell properties");
        propertySet.put(new CellModeProperty( cell ));
        propertySet.put(new CellBackcolorProperty( cell ));
        propertySet.put(new CellStyleProperty( cell, jd ));
        
        list.add(propertySet);
        
        return list;
    }
    
}
