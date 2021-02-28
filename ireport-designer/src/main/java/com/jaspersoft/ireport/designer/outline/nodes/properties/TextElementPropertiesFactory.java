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

import com.jaspersoft.ireport.designer.sheet.properties.TextFieldPatternProperty;
import com.jaspersoft.ireport.designer.sheet.properties.StretchWithOverflowProperty;
import com.jaspersoft.ireport.designer.sheet.properties.BlankWhenNullProperty;
import com.jaspersoft.ireport.designer.sheet.properties.TextProperty;
import com.jaspersoft.ireport.designer.sheet.properties.LineSpacingProperty;
import com.jaspersoft.ireport.designer.sheet.properties.RotationProperty;
import com.jaspersoft.ireport.designer.sheet.properties.TextFieldExpressionProperty;
import com.jaspersoft.ireport.designer.sheet.properties.HorizontalAlignmentProperty;
import com.jaspersoft.ireport.designer.sheet.properties.VerticalAlignmentProperty;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.sheet.properties.MarkupProperty;
import com.jaspersoft.ireport.designer.sheet.properties.BoldProperty;
import com.jaspersoft.ireport.designer.sheet.properties.FirstLineIndentProperty;
import com.jaspersoft.ireport.designer.sheet.properties.FontNameProperty;
import com.jaspersoft.ireport.designer.sheet.properties.FontSizeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.ItalicProperty;
import com.jaspersoft.ireport.designer.sheet.properties.LeftIndentProperty;
import com.jaspersoft.ireport.designer.sheet.properties.LineSpacingSizeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.PdfEmbeddedProperty;
import com.jaspersoft.ireport.designer.sheet.properties.PdfEncodingProperty;
import com.jaspersoft.ireport.designer.sheet.properties.PdfFontNameProperty;
//import com.jaspersoft.ireport.designer.sheet.properties.ReportFontProperty;
import com.jaspersoft.ireport.designer.sheet.properties.RightIndentProperty;
import com.jaspersoft.ireport.designer.sheet.properties.SpacingAfterProperty;
import com.jaspersoft.ireport.designer.sheet.properties.SpacingBeforeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.StrikeThroughProperty;
import com.jaspersoft.ireport.designer.sheet.properties.TabStopWidthProperty;
import com.jaspersoft.ireport.designer.sheet.properties.TabStopsProperty;
import com.jaspersoft.ireport.designer.sheet.properties.TextFieldEvaluationGroupProperty;
import com.jaspersoft.ireport.designer.sheet.properties.TextFieldEvaluationTimeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.TextFieldExpressionClassNameProperty;
import com.jaspersoft.ireport.designer.sheet.properties.TextfieldPatternExpressionProperty;
import com.jaspersoft.ireport.designer.sheet.properties.UnderlineProperty;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Sheet;

/**
 *
 * @author gtoffoli
 */
public class TextElementPropertiesFactory {

    /**
     * Get the common properties...
     */
    public static Sheet.Set getTextPropertySet(JRDesignTextElement element, JasperDesign jd)
    {
        
        JRDesignDataset dataset = ModelUtils.getElementDataset(element, jd);
        Sheet.Set propertySet = Sheet.createPropertiesSet();
        propertySet.setName("TEXT_ELEMENT_PROPERTIES");
        propertySet.setDisplayName("Text properties");
        
        propertySet.put(new FontNameProperty( element ));
        propertySet.put(new FontSizeProperty( element ));
        
        propertySet.put(new BoldProperty( element ));
        propertySet.put(new ItalicProperty( element ));
        propertySet.put(new UnderlineProperty( element ));
        propertySet.put(new StrikeThroughProperty( element ));
        
        propertySet.put(new HorizontalAlignmentProperty( element ));
        propertySet.put(new VerticalAlignmentProperty( element ));
        propertySet.put(new RotationProperty( element ));
        propertySet.put(new LineSpacingProperty( element ));
        propertySet.put(new LineSpacingSizeProperty( element ));
        propertySet.put(new MarkupProperty( element ));

        propertySet.put(new FirstLineIndentProperty( element ));
        propertySet.put(new LeftIndentProperty( element ));
        propertySet.put(new RightIndentProperty( element));
        propertySet.put(new SpacingBeforeProperty( element ));
        propertySet.put(new SpacingAfterProperty( element ));

        propertySet.put(new TabStopWidthProperty( element ));
        propertySet.put(new TabStopsProperty( element ));

        propertySet.put(new PdfFontNameProperty( element ));
        propertySet.put(new PdfEmbeddedProperty( element ));
        propertySet.put(new PdfEncodingProperty( element ));

        //propertySet.put(new ReportFontProperty( element, jd ));


        
        
        //propertySet.put(new LeftProperty( element ));
        return propertySet;
    }
    
    /**
     * Get the static text properties...
     */
    public static Sheet.Set getStaticTextPropertySet(JRDesignStaticText element, JasperDesign jd)
    {
        
        //JRDesignDataset dataset = ModelUtils.getElementDataset(element, jd);
        
        Sheet.Set propertySet = Sheet.createPropertiesSet();
        propertySet.setName("STATIC_TEXT_ELEMENT_PROPERTIES");
        propertySet.setDisplayName("Static text properties");
        
        propertySet.put(new TextProperty( element ));
        return propertySet;
    }
    
    /**
     * Get the static text properties...
     */
    public static Sheet.Set getTextFieldPropertySet(JRDesignTextField element, JasperDesign jd)
    {
        
        JRDesignDataset dataset = ModelUtils.getElementDataset(element, jd);
        Sheet.Set propertySet = Sheet.createPropertiesSet();
        propertySet.setName("TEXTFIELD_ELEMENT_PROPERTIES");
        propertySet.setDisplayName("Text field properties");
        propertySet.put(new TextFieldExpressionProperty(element, dataset));
        propertySet.put(new TextFieldExpressionClassNameProperty(element));
        propertySet.put(new BlankWhenNullProperty(element));
        propertySet.put(new TextFieldPatternProperty(element));
        propertySet.put(new TextfieldPatternExpressionProperty(element,dataset));
        propertySet.put(new StretchWithOverflowProperty( element ));
        propertySet.put(new TextFieldEvaluationTimeProperty(element, dataset));
        propertySet.put(new TextFieldEvaluationGroupProperty(element, dataset));
        
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
        
        if (element instanceof  JRDesignStaticText)
        {
            sets.add( getStaticTextPropertySet((JRDesignStaticText)element, jd ));
        }
        else if (element instanceof  JRDesignTextField)
        {
            sets.add( getTextFieldPropertySet((JRDesignTextField)element, jd ));
        }
        
        if (element instanceof  JRDesignTextElement)
        {
            sets.add( getTextPropertySet((JRDesignTextElement)element, jd) );
        }
        
        return sets;
    }



}
