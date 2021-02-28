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

import com.jaspersoft.ireport.designer.sheet.properties.SubreportUsingCacheProperty;
import com.jaspersoft.ireport.designer.sheet.properties.SubreportParametersProperty;
import com.jaspersoft.ireport.designer.sheet.properties.SubreportExpressionProperty;
import com.jaspersoft.ireport.designer.sheet.properties.ParametersMapExpressionProperty;
import com.jaspersoft.ireport.designer.sheet.properties.DataSourceExpressionProperty;
import com.jaspersoft.ireport.designer.sheet.properties.ConnectionExpressionProperty;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.sheet.properties.RunToBottomProperty;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Sheet;

/**
 *
 * @author gtoffoli
 */
public class SubreportPropertiesFactory {

    
    
    /**
     * Get the static text properties...
     */
    public static Sheet.Set getSubreportPropertySet(JRDesignSubreport element, JasperDesign jd)
    {
        
        JRDesignDataset dataset = ModelUtils.getElementDataset(element, jd);
        Sheet.Set propertySet = Sheet.createPropertiesSet();
        propertySet.setName("SUBREPORT_ELEMENT_PROPERTIES");
        propertySet.setDisplayName("Subreport properties");
        propertySet.put(new SubreportExpressionProperty(element, dataset));
        propertySet.put(new SubreportExpressionClassNameProperty(element));
        propertySet.put(new SubreportUsingCacheProperty(element));
        propertySet.put(new RunToBottomProperty(element));
        propertySet.put(new ParametersMapExpressionProperty(element, dataset));
        propertySet.put(new ConnectionTypeProperty(element) );
        propertySet.put(new ConnectionExpressionProperty(element, dataset));
        propertySet.put(new DataSourceExpressionProperty(element, dataset));
        propertySet.put(new SubreportParametersProperty(element, dataset));
        propertySet.put(new SubreportReturnValuesProperty(element, dataset));
        
        //propertySet.put(new LeftProperty( element ));
        return propertySet;
    }
    
    /**
     * Convenient way to get all the properties of an element.
     * Properties positions could be reordered to have a better order.
     */
    public static List<Sheet.Set> getPropertySets(JRDesignElement element, JasperDesign jd)
    {
        List<Sheet.Set> sets = new ArrayList<Sheet.Set>();
        
        if (element instanceof  JRDesignSubreport)
        {
            sets.add( getSubreportPropertySet((JRDesignSubreport)element, jd ));
        }
        
        return sets;
    }
    
    
}
