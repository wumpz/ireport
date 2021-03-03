
package com.jaspersoft.ireport.components.table.nodes.properties;


import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.sheet.GenericProperty;
import com.jaspersoft.ireport.designer.sheet.editors.JRPropertiesMapPropertyEditor;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertyExpression;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignPropertyExpression;
import org.openide.nodes.PropertySupport;


/**
 *
 * @author gtoffoli
 */


public class ColumnPropertyExpressionsProperty extends PropertySupport {

    PropertyEditor editor = null;
    StandardBaseColumn column = null;
    
    @SuppressWarnings("unchecked")
    public ColumnPropertyExpressionsProperty(StandardBaseColumn column, JRDesignDataset dataset)
    {
       super( "expressionProperties", List.class, "Properties expressions","List of property expressions for this element", true,true);
       setValue("canEditAsText", Boolean.FALSE);
       setValue("useList", Boolean.TRUE);
       setValue("canUseExpression", Boolean.TRUE);
       this.setValue(ExpressionContext.ATTRIBUTE_EXPRESSION_CONTEXT, new ExpressionContext(dataset));
       
       setValue("hintType", com.jaspersoft.ireport.designer.sheet.editors.JRPropertyDialog.SCOPE_TEXT_ELEMENT);
       
       this.column = column;
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        
        JRPropertiesMap map = column.getPropertiesMap();
        List properties = new ArrayList();
        String[] names = map.getPropertyNames();
        
        for (int i=0; i<names.length; ++i)
        {
            properties.add(new GenericProperty(names[i], map.getProperty(names[i])));
        }
        
        // add to the list the expression properties...
        JRPropertyExpression[] expProperties = column.getPropertyExpressions();
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
        
        ModelUtils.replacePropertiesMap(map, column.getPropertiesMap());
        replaceExpressionProperties(column, values);
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
    
    
    
    /**
     * replace the expression properties in element with the ones found in newExpressionProperties.
     */
    public static void replaceExpressionProperties(StandardBaseColumn column, List<GenericProperty> newExpressionProperties)
    {
        // Update names.
        
        List usedProps = new ArrayList();
        List propertyExpressions = column.getPropertyExpressionsList();
        
        for(int i = 0; i < propertyExpressions.size(); i++)
        {
            column.removePropertyExpression((JRPropertyExpression)propertyExpressions.get(i));
        }
        
        if (newExpressionProperties == null) return;
        for(GenericProperty prop : newExpressionProperties)
        {
            if (!prop.isUseExpression()) continue;
            JRDesignPropertyExpression newProp = new JRDesignPropertyExpression();
            newProp.setName(prop.getKey());
            newProp.setValueExpression(prop.getExpression());
            column.addPropertyExpression(newProp);
        }
        
    }
    
}
