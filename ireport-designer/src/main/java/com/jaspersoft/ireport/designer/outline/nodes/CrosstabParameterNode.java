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

import com.jaspersoft.ireport.designer.sheet.properties.DefaultValueExpressionProperty;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.dnd.ReportObjectPaletteTransferable;
import com.jaspersoft.ireport.designer.sheet.JRPropertiesMapProperty;
import com.jaspersoft.ireport.designer.sheet.properties.CrosstabParameterValueExpressionProperty;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.crosstabs.JRCrosstabParameter;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.ErrorManager;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.actions.RenameAction;
import org.openide.actions.ReorderAction;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.ExTransferable;
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
public class CrosstabParameterNode extends ParameterNode implements PropertyChangeListener {

    private JRDesignCrosstab crosstab = null;

    public CrosstabParameterNode(JasperDesign jd, JRDesignCrosstab crosstab, JRDesignCrosstabParameter parameter, Lookup doLkp)
    {
        super (jd,parameter, new ProxyLookup(doLkp, Lookups.fixed(crosstab)));
        this.crosstab = crosstab;
    }

    /**
     *  This is the function to create the sheet...
     * 
     */
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        
        Sheet.Set set = Sheet.createPropertiesSet();
        
        JRDesignDataset dataset = getParentNode().getLookup().lookup(JRDesignDataset.class);
        
        set.put(new NameProperty(getParameter(),crosstab));
        set.put(new ParameterNode.ValueClassNameProperty( getParameter()));
        if (!getParameter().isSystemDefined())
        {
            // set.put(new ForPromptingProperty( getParameter())); // This not really useful for the Crosstab parameters...
            set.put(new CrosstabParameterValueExpressionProperty((JRDesignCrosstabParameter)getParameter(), ModelUtils.getElementDataset(crosstab, jd) ));
            set.put(new DefaultValueExpressionProperty(getParameter(), ModelUtils.getElementDataset(crosstab, jd) ));
            set.put(new DescriptionProperty(getParameter()));
            set.put(new JRPropertiesMapProperty( getParameter()) );
        }
        
        
        sheet.put(set);
        return sheet;
    }
    
    
    @Override
    public void destroy() throws IOException {
       
       if (!getParameter().isSystemDefined())
       {
          crosstab.removeParameter((JRCrosstabParameter)getParameter());
          // TODO: Undo operation
          
          // with this call to super we are assuming there is not JRDataset in this node lookup
          // otherwise the implementation of ParameterNode will try to remove the parameter
          // from the dataset.
          super.destroy(); 
       } // otherwise the component was likely already removed with a parent component
    }
        
    @Override
    public Action[] getActions(boolean popup) {
        return new Action[] {
            SystemAction.get( CopyAction.class ),
            SystemAction.get( CutAction.class ),
            SystemAction.get( RenameAction.class ),
            SystemAction.get( ReorderAction.class ),
            null,
            SystemAction.get( DeleteAction.class ) };
    }
    
    @Override
    public Transferable drag() throws IOException {
        ExTransferable tras = ExTransferable.create(clipboardCut());
        tras.put(new ReportObjectPaletteTransferable( 
                    "com.jaspersoft.ireport.designer.palette.actions.CreateTextFieldFromCrosstabParameterAction",
                    getParameter()));
        
        return tras;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setName(String s) {
        
        if (s.equals(""))
        {
            throw new IllegalArgumentException("Parameter name not valid.");
        }
        
        List<JRCrosstabParameter> currentParameters = null;
        currentParameters = crosstab.getParametersList();
        for (JRCrosstabParameter pa : currentParameters)
        {
            JRDesignCrosstabParameter p = (JRDesignCrosstabParameter)pa;
            if (p != getParameter() && p.getName().equals(s))
            {
                throw new IllegalArgumentException("Parameter name already in use.");
            }
        }
        
        String oldName = getParameter().getName();
        getParameter().setName(s);


        // We need to update the parameters map too...
        crosstab.getParametersMap().remove(oldName);
        crosstab.getParametersMap().put(s,(JRCrosstabParameter)getParameter());
        
        ObjectPropertyUndoableEdit opue = new ObjectPropertyUndoableEdit(
                    getParameter(), "Name", String.class, oldName, s);

        IReportManager.getInstance().addUndoableEdit(opue);
    }

    
    /***************  SHEET PROPERTIES DEFINITIONS **********************/
    
    
    /**
     *  Class to manage the JRDesignParameter.PROPERTY_NAME property
     */
    public static final class NameProperty extends PropertySupport.ReadWrite {

        JRDesignParameter parameter = null;
        JRDesignCrosstab crosstab = null;

        @SuppressWarnings("unchecked")
        public NameProperty(JRDesignParameter parameter, JRDesignCrosstab crosstab)
        {
            super(JRDesignParameter.PROPERTY_NAME, String.class,
                  "Name",
                  "Name of the parameter");
            this.parameter = parameter;
            this.crosstab = crosstab;
            this.setValue("oneline", Boolean.TRUE);
        }

        @Override
        public boolean canWrite()
        {
            return !getParameter().isSystemDefined();
        }

        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return getParameter().getName();
        }

        @SuppressWarnings("unchecked")
        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {


            if (val == null || val.equals(""))
            {
                IllegalArgumentException iae = annotateException("Parameter name not valid."); 
                throw iae; 
            }

            String s = val+"";

            List<JRCrosstabParameter> currentParameters = null;
            currentParameters = (List<JRCrosstabParameter>)crosstab.getParametersList();
            for (JRCrosstabParameter pa : currentParameters)
            {
                JRDesignCrosstabParameter p = (JRDesignCrosstabParameter)pa;
                if (p != getParameter() && p.getName().equals(s))
                {
                    IllegalArgumentException iae = annotateException("Parameter name already in use."); 
                    throw iae; 
                }
            }
            String oldName = getParameter().getName();
            getParameter().setName(s);

            // We need to update the parameters map too...
            crosstab.getParametersMap().remove(oldName);
            crosstab.getParametersMap().put(s,(JRCrosstabParameter)getParameter());

            ObjectPropertyUndoableEdit opue = new ObjectPropertyUndoableEdit(
                    getParameter(), "Name", String.class, oldName, getParameter().getName());

            IReportManager.getInstance().addUndoableEdit(opue);

        }

        public JRDesignParameter getParameter() {
            return parameter;
        }

        public IllegalArgumentException annotateException(String msg)
        {
            IllegalArgumentException iae = new IllegalArgumentException(msg); 
            ErrorManager.getDefault().annotate(iae, 
                                    ErrorManager.EXCEPTION,
                                    msg,
                                    msg, null, null); 
            return iae;
        }
    }

}
