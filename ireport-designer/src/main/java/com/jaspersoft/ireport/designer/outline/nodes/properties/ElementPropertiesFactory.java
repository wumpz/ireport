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

import com.jaspersoft.ireport.designer.sheet.properties.StyleProperty;
import com.jaspersoft.ireport.designer.sheet.properties.KeyProperty;
import com.jaspersoft.ireport.designer.sheet.properties.ModeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.RemoveLineWhenBlankProperty;
import com.jaspersoft.ireport.designer.sheet.properties.PrintWhenDetailOverflowsProperty;
import com.jaspersoft.ireport.designer.sheet.properties.PrintRepeatedValuesProperty;
import com.jaspersoft.ireport.designer.sheet.properties.PrintInFirstWholeBandProperty;
import com.jaspersoft.ireport.designer.sheet.properties.StretchTypeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.PositionTypeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.WidthProperty;
import com.jaspersoft.ireport.designer.sheet.properties.TopProperty;
import com.jaspersoft.ireport.designer.sheet.properties.LeftProperty;
import com.jaspersoft.ireport.designer.sheet.properties.HeightProperty;
import com.jaspersoft.ireport.designer.sheet.properties.PrintWhenExpressionProperty;
import com.jaspersoft.ireport.designer.sheet.properties.BackcolorProperty;
import com.jaspersoft.ireport.designer.sheet.properties.ForecolorProperty;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.outline.nodes.properties.charts.ChartPropertiesFactory;
import com.jaspersoft.ireport.designer.sheet.PropertyExpressionsProperty;
import com.jaspersoft.ireport.designer.sheet.properties.BreakTypeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.GenericElementEvaluationGroupProperty;
import com.jaspersoft.ireport.designer.sheet.properties.GenericElementEvaluationTimeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.GenericElementParametersProperty;
import com.jaspersoft.ireport.designer.sheet.properties.GenericElementTypeNameProperty;
import com.jaspersoft.ireport.designer.sheet.properties.GenericElementTypeNameSpaceProperty;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.design.JRDesignBreak;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignGenericElement;
import net.sf.jasperreports.engine.design.JRDesignGraphicElement;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Sheet;

/**
 * @author gtoffoli
 */
public class ElementPropertiesFactory {

    /**
     * Get the common properties...
     */
    public static Sheet.Set getCommonPropertySet(JRDesignElement element, JasperDesign jd)
    {
        
        JRDesignDataset dataset = ModelUtils.getElementDataset(element, jd);
        Sheet.Set propertySet = Sheet.createPropertiesSet();
        propertySet.put(new LeftProperty( element ));
        propertySet.put(new TopProperty( element ));
        propertySet.put(new WidthProperty( element ));
        propertySet.put(new HeightProperty( element ));
        propertySet.put(new ForecolorProperty(element));
        propertySet.put(new BackcolorProperty(element));
        propertySet.put(new ModeProperty(element));
        propertySet.put(new StyleProperty(element, jd));
        propertySet.put(new KeyProperty(element));
        propertySet.put(new PositionTypeProperty(element));
        propertySet.put(new StretchTypeProperty(element));
        propertySet.put(new PrintRepeatedValuesProperty(element));
        propertySet.put(new RemoveLineWhenBlankProperty(element));
        propertySet.put(new PrintInFirstWholeBandProperty(element));
        propertySet.put(new PrintWhenDetailOverflowsProperty(element));
        propertySet.put(new PrintWhenGroupChangesProperty(element, dataset));
        propertySet.put(new PrintWhenExpressionProperty(element, dataset));
        propertySet.put(new PropertyExpressionsProperty(element, dataset) );
        
       
        return propertySet;
    }


    public static Sheet.Set getBreakPropertySet(JRDesignBreak breakElement)
    {
        Sheet.Set propertySet = Sheet.createPropertiesSet();
        propertySet.setName("BREAK_PROPERTIES");
        propertySet.setDisplayName("Break properties");
        propertySet.put(new BreakTypeProperty(breakElement));

        return propertySet;
    }


    public static Sheet.Set getGenericElementPropertySet(JRDesignGenericElement genericElement, JasperDesign jd)
    {
        JRDesignDataset dataset = ModelUtils.getElementDataset(genericElement, jd);
        Sheet.Set propertySet = Sheet.createPropertiesSet();

        propertySet.setName("GENERIC_ELEMENT_PROPERTIES");
        propertySet.setDisplayName("Generic Element properties");
        propertySet.put(new GenericElementTypeNameProperty(genericElement));
        propertySet.put(new GenericElementTypeNameSpaceProperty(genericElement));
        propertySet.put(new GenericElementEvaluationTimeProperty(genericElement,dataset));
        propertySet.put(new GenericElementEvaluationGroupProperty(genericElement,dataset));
        propertySet.put(new GenericElementParametersProperty(genericElement,dataset));

        return propertySet;
    }
    /**
     * Get the common properties...
     */
    public static Sheet.Set getBoxPropertySet(JRBox box)
    {
        Sheet.Set propertySet = Sheet.createPropertiesSet();
        propertySet.setName("BOX_PROPERTIES");
        propertySet.setDisplayName("Box properties");
//FIXME
//        propertySet.put(new BoxBorderProperty( box, JRBaseStyle.PROPERTY_BORDER, "Border", "Type of border." ));
//        propertySet.put(new BoxBorderColorProperty( box, JRBaseStyle.PROPERTY_BORDER_COLOR, "Border Color", "Color of the border." ));
//        propertySet.put(new BoxPaddingProperty( box, JRBaseStyle.PROPERTY_PADDING, "Padding", "Padding for this element." ));
//       
//        propertySet.put(new BoxBorderProperty( box, JRBaseStyle.PROPERTY_TOP_BORDER, "Top Border", "Type of the top border." ));
//        propertySet.put(new BoxBorderColorProperty( box, JRBaseStyle.PROPERTY_TOP_BORDER_COLOR, "Top Border Color", "Color of the top border." ));
//        propertySet.put(new BoxPaddingProperty( box, JRBaseStyle.PROPERTY_TOP_PADDING, "Top Padding", "Top Padding." ));
//        
//        propertySet.put(new BoxBorderProperty( box, JRBaseStyle.PROPERTY_LEFT_BORDER, "Left Border", "Type of the left border." ));
//        propertySet.put(new BoxBorderColorProperty( box, JRBaseStyle.PROPERTY_LEFT_BORDER_COLOR, "Left Border Color", "Color of the left border." ));
//        propertySet.put(new BoxPaddingProperty( box, JRBaseStyle.PROPERTY_LEFT_PADDING, "Left Padding", "Left Padding." ));
//        
//        propertySet.put(new BoxBorderProperty( box, JRBaseStyle.PROPERTY_RIGHT_BORDER, "Right Border", "Type of the right border." ));
//        propertySet.put(new BoxBorderColorProperty( box, JRBaseStyle.PROPERTY_RIGHT_BORDER_COLOR, "Right Border Color", "Color of the right border." ));
//        propertySet.put(new BoxPaddingProperty( box, JRBaseStyle.PROPERTY_RIGHT_PADDING, "Right Padding", "Right Padding." ));
//        
//        propertySet.put(new BoxBorderProperty( box, JRBaseStyle.PROPERTY_BOTTOM_BORDER, "Bottom Border", "Type of bottom border." ));
//        propertySet.put(new BoxBorderColorProperty( box, JRBaseStyle.PROPERTY_BOTTOM_BORDER_COLOR, "Bottom Border Color", "Color of the bottom border." ));
//        propertySet.put(new BoxPaddingProperty( box, JRBaseStyle.PROPERTY_BOTTOM_PADDING, "Bottom Padding", "Bottom Padding." ));
        
        return propertySet;
    }
    
    
    
    /**
     * Convenient way to get all the properties of an element.
     * Properties positions could be reordered to have a better order.
     */
    public static List<Sheet.Set> getPropertySets(JRDesignElement element, JasperDesign jd)
    {
        List<Sheet.Set> sets = new ArrayList<Sheet.Set>();
        sets.add( getCommonPropertySet(element, jd) );
        
        if (element instanceof  JRDesignGraphicElement)
        {
            sets.addAll( GraphicElementPropertiesFactory.getGraphicPropertySets((JRDesignGraphicElement)element, jd) );
        }
        if (element instanceof  JRDesignTextElement)
        {
            sets.addAll( TextElementPropertiesFactory.getPropertySets(element, jd) );
        }
        if (element instanceof  JRDesignSubreport)
        {
            sets.addAll( SubreportPropertiesFactory.getPropertySets(element, jd) );
        }
        if (element instanceof JRDesignBreak)
        {
            sets.add( getBreakPropertySet((JRDesignBreak)element) );

        }
        
        if (element instanceof  JRDesignChart)
        {
            sets.addAll( ChartPropertiesFactory.getPropertySets((JRDesignChart)element, jd) );
        }
        
        
        
        if (element instanceof  JRBox)
        {
            sets.add( getBoxPropertySet((JRBox)element) );
        }

        if (element instanceof  JRDesignGenericElement)
        {
            sets.add(  getGenericElementPropertySet((JRDesignGenericElement)element, jd ) );
        }
        
        return sets;
    }
    
    
}
