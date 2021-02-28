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

import com.jaspersoft.ireport.designer.sheet.properties.OnErrorTypeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.LazyProperty;
import com.jaspersoft.ireport.designer.sheet.properties.ImageUsingCacheProperty;
import com.jaspersoft.ireport.designer.sheet.properties.VerticalAlignmentProperty;
import com.jaspersoft.ireport.designer.sheet.properties.HorizontalAlignmentProperty;
import com.jaspersoft.ireport.designer.sheet.properties.ScaleImageProperty;
import com.jaspersoft.ireport.designer.sheet.properties.FillProperty;
import com.jaspersoft.ireport.designer.sheet.properties.ImageExpressionProperty;
import com.jaspersoft.ireport.designer.sheet.properties.RadiusProperty;
import com.jaspersoft.ireport.designer.sheet.properties.LineDirectionProperty;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.sheet.JRPenProperty;
import com.jaspersoft.ireport.designer.sheet.properties.ImageEvaluationTimeProperty;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignEllipse;
import net.sf.jasperreports.engine.design.JRDesignGraphicElement;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignRectangle;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Sheet;

/**
 *
 * @author gtoffoli
 */
public class GraphicElementPropertiesFactory {

    /**
     * Get the GraphicElement properties...
     */
    public static List<Sheet.Set> getGraphicPropertySets(JRDesignGraphicElement element, JasperDesign jd)
    {
        JRDesignDataset dataset = ModelUtils.getElementDataset(element, jd);
        
        List<Sheet.Set> list = new ArrayList<Sheet.Set>();
        Sheet.Set propertySet = Sheet.createPropertiesSet();
        propertySet.setName("GRAPHIC_ELEMENT_PROPERTIES");
        propertySet.setDisplayName("Graphic properties");
        //propertySet.put(new PenProperty( element ));
        propertySet.put(new JRPenProperty(element.getLinePen(), element));
        propertySet.put(new FillProperty( element ));
        
        list.add(propertySet);
        
        if (element instanceof JRDesignImage)
        {
            Sheet.Set imagePropertySet = Sheet.createPropertiesSet();
            imagePropertySet.setName("IMAGE_ELEMENT_PROPERTIES");
            imagePropertySet.setDisplayName("Image properties");
            imagePropertySet.put(new ImageExpressionProperty((JRDesignImage)element, dataset));
            imagePropertySet.put(new ImageExpressionClassNameProperty((JRDesignImage)element) );
            imagePropertySet.put(new ScaleImageProperty( (JRDesignImage)element ));
            imagePropertySet.put(new HorizontalAlignmentProperty( (JRDesignImage)element ));
            imagePropertySet.put(new VerticalAlignmentProperty( (JRDesignImage)element ));
            imagePropertySet.put(new ImageUsingCacheProperty( (JRDesignImage)element ));
            imagePropertySet.put(new LazyProperty( (JRDesignImage)element ));
            imagePropertySet.put(new OnErrorTypeProperty( (JRDesignImage)element ));
            imagePropertySet.put(new ImageEvaluationTimeProperty((JRDesignImage)element, dataset));//, dataset));
            imagePropertySet.put(new EvaluationGroupProperty((JRDesignImage)element, dataset));
            list.add(imagePropertySet);
        }
        else if (element instanceof JRDesignLine)
        {
            Sheet.Set linePropertySet = Sheet.createPropertiesSet();
            linePropertySet.setName("LINE_ELEMENT_PROPERTIES");
            linePropertySet.setDisplayName("Line properties");
            linePropertySet.put(new LineDirectionProperty( (JRDesignLine)element ));
            list.add(linePropertySet);
        }
        else if (element instanceof JRDesignRectangle)
        {
            Sheet.Set rectanglePropertySet = Sheet.createPropertiesSet();
            rectanglePropertySet.setName("RECTANGLE_ELEMENT_PROPERTIES");
            rectanglePropertySet.setDisplayName("Rectangle properties");
            rectanglePropertySet.put(new RadiusProperty( (JRDesignRectangle)element ));
            list.add(rectanglePropertySet);
        }
        else if (element instanceof JRDesignEllipse)
        {
            // Nothing to do...
        }
        
        return list;
    }
    
    
}
