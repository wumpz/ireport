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
package com.jaspersoft.ireport.designer.sheet;

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.sheet.editors.JRPropertiesMapPropertyEditor;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertyExpression;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import org.openide.nodes.PropertySupport;

/**
 * This class should be able to handle both common properties
 * and expression based properties.
 * 
 * @author gtoffoli
 */
public class PropertyExpressionsProperty  extends PropertySupport {

    PropertyEditor editor = null;
    JRDesignElement element = null;
    
    @SuppressWarnings("unchecked")
    public PropertyExpressionsProperty(JRDesignElement element, JRDesignDataset dataset)
    {
       super( "properties", List.class, "Properties expressions","List of property expressions for this element", true,true);
       setValue("canEditAsText", Boolean.FALSE);
       setValue("useList", Boolean.TRUE);
       setValue("canUseExpression", Boolean.TRUE);
       this.setValue(ExpressionContext.ATTRIBUTE_EXPRESSION_CONTEXT, new ExpressionContext(dataset));
       
       if(element instanceof JRTextField)
       {
           setValue("hintType", com.jaspersoft.ireport.designer.sheet.editors.JRPropertyDialog.SCOPE_TEXT_ELEMENT);
       }
       else 
       {
           setValue("hintType", com.jaspersoft.ireport.designer.sheet.editors.JRPropertyDialog.SCOPE_ELEMENT);
       }
       
       this.element = element;
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        
        JRPropertiesMap map = element.getPropertiesMap();
        List properties = new ArrayList();
        String[] names = map.getPropertyNames();
        
        for (int i=0; i<names.length; ++i)
        {
            properties.add(new GenericProperty(names[i], map.getProperty(names[i])));
        }
        
        // add to the list the expression properties...
        JRPropertyExpression[] expProperties = element.getPropertyExpressions();
        for (int i=0; expProperties != null &&  i<expProperties.length; ++i)
        {
            properties.add(new GenericProperty(expProperties[i].getName(), (JRDesignExpression)expProperties[i].getValueExpression()));
        }
        
        return properties;
    }

    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //if (!(val instanceof JRPropertiesMap)) throw new IllegalArgumentException();
        
        if (!(val instanceof List)) throw new IllegalArgumentException();
        
        // Fill this map with the content of the map we got here...
        
        // 1. Create the map...
        JRPropertiesMap map = new JRPropertiesMap();
        List values = (List)val;
        for (int i=0; i <values.size(); ++i)
        {
            GenericProperty prop = (GenericProperty)values.get(i);
            if (!prop.isUseExpression())
            {
                map.setProperty(prop.getKey(), (String)prop.getValue());
            }
        }
        
        ModelUtils.replacePropertiesMap(map, element.getPropertiesMap());
        ModelUtils.replaceExpressionProperties(element, values);
        
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
    }
    
    @Override
    public PropertyEditor getPropertyEditor() {
        
        if (editor == null)
        {
            editor = new JRPropertiesMapPropertyEditor();
        }
        return editor;
    }
    
    
}

