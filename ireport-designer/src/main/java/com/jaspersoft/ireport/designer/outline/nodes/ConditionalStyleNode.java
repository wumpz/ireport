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
package com.jaspersoft.ireport.designer.outline.nodes;

import com.jaspersoft.ireport.designer.actions.EditConditionalStyleExpressionBandAction;
import com.jaspersoft.ireport.designer.editor.FullExpressionContext;
import com.jaspersoft.ireport.designer.sheet.properties.ExpressionProperty;
import com.jaspersoft.ireport.designer.styles.ResetStyleAction;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.Action;
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.actions.NewAction;
import org.openide.actions.PasteAction;
import org.openide.actions.ReorderAction;
import org.openide.nodes.Sheet;
import org.openide.nodes.Sheet.Set;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * ParameterNode detects the events fired by the subtended parameter.
 * Implements the support for the property sheet of a parameter.
 * If a parameter is system defined, it can not be cut.
 * Actions of a parameter node include copy, paste, reorder, rename and delete.
 * 
 * @author gtoffoli
 */
public class ConditionalStyleNode extends AbstractStyleNode implements PropertyChangeListener {

    private JRDesignStyle parentStyle;
    
    public ConditionalStyleNode(JasperDesign jd, JRDesignConditionalStyle style, Lookup doLkp, JRDesignStyle parentStyle)
    {
        super(jd, style, new ProxyLookup( Lookups.singleton(style), doLkp));
        this.parentStyle = parentStyle;
        style.getEventSupport().addPropertyChangeListener(this);
        this.setName("conditionalStyle");
    }
    
    public JRDesignConditionalStyle getConditionalStyle()
    {
        return (JRDesignConditionalStyle)getStyle();
    }

    @Override
    public String getDisplayName() {

        if (getConditionalStyle().getConditionExpression() != null)
        {
            return Misc.getExpressionText(getConditionalStyle().getConditionExpression());
        }
        else
        {
            return "<No condition set>";
        }
    }

    /**
     *  This is the function to create the sheet...
     * 
     */
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        
        Set set = sheet.get(Sheet.PROPERTIES);
        Property[] props = set.getProperties();
        
        // Remove all the properties...
        for (int i=0; i<props.length; ++i)
        {
            set.remove(props[i].getName());
        }
        // Add the missing properties...
        set.put(new ConditionExpressionProperty( getConditionalStyle(), jd));
        
        for (int i=0; i<props.length; ++i)
        {
            set.put(props[i]);
        }
        
        return sheet;
    }
    

    @Override
    public boolean canRename() {
        return false;
    }

    @Override
    public boolean canCut() {
        return true;
    }



    
    @Override
    public void destroy() throws IOException {
       
          int index = parentStyle.getConditionalStyleList().indexOf(getStyle());
          
          // add destroy....
          parentStyle.removeConditionalStyle(getConditionalStyle());
          // TODO: Add the undo operation
          //DeleteStyleUndoableEdit undo = new DeleteStyleUndoableEdit(getStyle(), jd,index); //newIndex
          //IReportManager.getInstance().addUndoableEdit(undo);
          
          super.destroy();
    }

    public JRDesignStyle getParentStyle() {
        return parentStyle;
    }

    public void setParentStyle(JRDesignStyle parentStyle) {
        this.parentStyle = parentStyle;
    }
        
    public void propertyChange(PropertyChangeEvent evt) {
        
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignConditionalStyle.PROPERTY_CONDITION_EXPRESSION ))
        {
            fireDisplayNameChange(null, null);
        }
        
        super.propertyChange(evt);
    }

    @Override
    public Action getPreferredAction() {
        return SystemAction.get(EditConditionalStyleExpressionBandAction.class); 
    }


    @Override
    public Action[] getActions(boolean popup) {
        return new Action[] {
            SystemAction.get(EditConditionalStyleExpressionBandAction.class),null,
            SystemAction.get( NewAction.class),
            SystemAction.get( CopyAction.class ),
            SystemAction.get( PasteAction.class),
            SystemAction.get( CutAction.class ),
            SystemAction.get( ResetStyleAction.class ),
            SystemAction.get( ReorderAction.class ),
            null,
            SystemAction.get( DeleteAction.class ) };
    }
    
    
    
    /***************  SHEET PROPERTIES DEFINITIONS **********************/
    
    /**
     */
    public static final class ConditionExpressionProperty extends ExpressionProperty
    {
        private final JRDesignConditionalStyle conditionalStyle;
        private final JasperDesign jd;

        public JRDesignConditionalStyle getConditionalStyle() {
            return conditionalStyle;
        }
        
        @SuppressWarnings("unchecked")
        public ConditionExpressionProperty(JRDesignConditionalStyle conditionalStyle, JasperDesign jd)
        {
            super(conditionalStyle, new FullExpressionContext(jd));
            setName( JRDesignConditionalStyle.PROPERTY_CONDITION_EXPRESSION);
            setDisplayName("Condition Expression");
            setShortDescription("The expression used as condition. It should return a boolean object.");
            this.conditionalStyle = conditionalStyle;
            this.jd = jd;
        }
        
        public JasperDesign getJasperDesign()
        {
            return jd;
        }
        
        @Override
        public String getDefaultExpressionClassName() {
            return "java.lang.Boolean";
        }

        @Override
        public JRDesignExpression getExpression() {
            return (JRDesignExpression)conditionalStyle.getConditionExpression();
        }

        @Override
        public void setExpression(JRDesignExpression expression) {
            conditionalStyle.setConditionExpression(expression);
        }
        
    }

}
