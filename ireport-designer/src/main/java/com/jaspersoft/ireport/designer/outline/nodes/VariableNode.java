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

import com.jaspersoft.ireport.designer.sheet.properties.VariableExpressionProperty;
import com.jaspersoft.ireport.designer.sheet.properties.InitialValueExpressionProperty;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.dnd.ReportObjectPaletteTransferable;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;
import org.openide.ErrorManager;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.actions.RenameAction;
import org.openide.actions.ReorderAction;
import org.openide.nodes.Children;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.WeakListeners;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class VariableNode extends IRAbstractNode implements PropertyChangeListener {

    JasperDesign jd = null;
    private JRDesignVariable variable = null;

    public VariableNode(JasperDesign jd, JRDesignVariable variable, Lookup doLkp)
    {
        super (Children.LEAF, new ProxyLookup(doLkp, Lookups.fixed(jd, variable)));
        this.jd = jd;
        this.variable = variable;
        setDisplayName ( variable.getName());
        super.setName( variable.getName() );
        if (variable.isSystemDefined())
        {
            setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/variable-16.png");
        }
        else
        {
            setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/variable-16.png");
        }
        
        variable.getEventSupport().addPropertyChangeListener(this);
    }

    @Override
    public String getDisplayName() {
        return variable.getName();
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
        
        set.put(new NameProperty( getVariable(),dataset));
        set.put(new ValueClassNameProperty( getVariable()));
        if (!getVariable().isSystemDefined())
        {
            set.put(new CalculationProperty(getVariable()));
            set.put(new ResetTypeProperty(getVariable(), dataset));
            set.put(new ResetGroupProperty(getVariable(), dataset));
            set.put(new IncrementTypeProperty(getVariable(), dataset));
            set.put(new IncrementGroupProperty(getVariable(), dataset));
            set.put(new IncrementerFactoryClassNameProperty(getVariable()));
            set.put(new VariableExpressionProperty(getVariable(),dataset));
            set.put(new InitialValueExpressionProperty(getVariable(),dataset));
        }
        
        sheet.put(set);
        return sheet;
    }
    
    @Override
    public boolean canCut() {
        return !variable.isSystemDefined();
    }
    
    @Override
    public boolean canRename() {
        return !variable.isSystemDefined();
    }
    
    @Override
    public boolean canDestroy() {
        return !variable.isSystemDefined();
    }
    
    @Override
    public Transferable clipboardCut() throws IOException {
        return NodeTransfer.transferable(this, NodeTransfer.CLIPBOARD_CUT);
    }
    
    @Override
    public Transferable clipboardCopy() throws IOException {
        return NodeTransfer.transferable(this, NodeTransfer.CLIPBOARD_COPY);
    }
    
    @Override
    public void destroy() throws IOException {
       
       if (!getVariable().isSystemDefined())
       {
           
          JRDesignDataset dataset = getParentNode().getLookup().lookup(JRDesignDataset.class);
          dataset.removeVariable(getVariable());
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
                    "com.jaspersoft.ireport.designer.palette.actions.CreateTextFieldFromVariableAction",
                    getVariable()));
        
        return tras;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setName(String s) {
        
        if (s.equals(""))
        {
            throw new IllegalArgumentException("Variable name not valid.");
        }
        
        List<JRVariable> currentVariables = null;
        JRDesignDataset dataset = getParentNode().getLookup().lookup(JRDesignDataset.class);
        currentVariables = (List<JRVariable>)dataset.getVariablesList();
        for (JRVariable pa : currentVariables)
        {
            JRDesignVariable p = (JRDesignVariable)pa;
            if (p != getVariable() && p.getName().equals(s))
            {
                throw new IllegalArgumentException("Variable name already in use.");
            }
        }
        
        String oldName = getVariable().getName();
        getVariable().setName(s);
        dataset.getVariablesMap().remove(oldName);
        dataset.getVariablesMap().put(s,getVariable());
        
        ObjectPropertyUndoableEdit opue = new ObjectPropertyUndoableEdit(
                    getVariable(), "Name", String.class, oldName, s);

        IReportManager.getInstance().addUndoableEdit(opue);
    }

    public JRDesignVariable getVariable() {
        return variable;
    }

    public void setVariable(JRDesignVariable variable) {
        this.variable = variable;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignVariable.PROPERTY_NAME ))
        {
            super.setName(getVariable().getName());
            this.setDisplayName(getVariable().getName());
            if (this.getParentNode() != null && this.getParentNode() instanceof VariablesNode)
            {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        ((VariablesNode)(VariableNode.this.getParentNode())).updateSorting();
                    }
                });
            }
        }
        
        // Update the sheet
        this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue() );
    }
    
    
    
    
    
    
    
    
    
    
    /***************  SHEET PROPERTIES DEFINITIONS **********************/
    
    
    /**
     *  Class to manage the JRDesignVariable.PROPERTY_NAME property
     */
    public static final class NameProperty extends PropertySupport.ReadWrite {

        JRDesignVariable variable = null;
        JRDesignDataset dataset = null;

        @SuppressWarnings("unchecked")
        public NameProperty(JRDesignVariable variable, JRDesignDataset dataset)
        {
            super(JRDesignVariable.PROPERTY_NAME, String.class,
                  I18n.getString("VariableNode.Property.Name"),
                  I18n.getString("VariableNode.Property.Namedetail"));
            this.variable = variable;
            this.dataset = dataset;
            this.setValue("oneline", Boolean.TRUE);
        }

        @Override
        public boolean canWrite()
        {
            return !getVariable().isSystemDefined();
        }

        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return getVariable().getName();
        }

        @SuppressWarnings("unchecked")
        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {


            if (val == null || val.equals(""))
            {
                IllegalArgumentException iae = annotateException(I18n.getString("VariableNode.Property.VariableInvalid")); 
                throw iae; 
            }

            String s = val+"";

            List<JRVariable> currentVariables = null;
            currentVariables = (List<JRVariable>)getDataset().getVariablesList();
            for (JRVariable pa : currentVariables)
            {
                JRDesignVariable p = (JRDesignVariable)pa;
                if (p != getVariable() && p.getName().equals(s))
                {
                    IllegalArgumentException iae = annotateException(I18n.getString("VariableNode.Property.VariableInUse")); 
                    throw iae; 
                }
            }
            String oldName = getVariable().getName();
            getVariable().setName(s);
            dataset.getVariablesMap().remove(oldName);
            dataset.getVariablesMap().put(s,getVariable());

            ObjectPropertyUndoableEdit opue = new ObjectPropertyUndoableEdit(
                    getVariable(), "Name", String.class, oldName, getVariable().getName());

            IReportManager.getInstance().addUndoableEdit(opue);

        }

        public JRDesignDataset getDataset() {
            return dataset;
        }

        public void setDataset(JRDesignDataset dataset) {
            this.dataset = dataset;
        }

        public JRDesignVariable getVariable() {
            return variable;
        }

        public void setVariable(JRDesignVariable variable) {
            this.variable = variable;
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
    
    
    /**
     *  Class to manage the JRDesignVariable.PROPERTY_VALUE_CLASS_NAME property
     */
    private static final class ValueClassNameProperty extends PropertySupport.ReadWrite {

        JRDesignVariable variable = null;
        PropertyEditor editor = null;

        @SuppressWarnings("unchecked")
        public ValueClassNameProperty(JRDesignVariable variable)
        {
            super(JRDesignVariable.PROPERTY_VALUE_CLASS_NAME, String.class,
                  I18n.getString("VariableNode.Property.VariableClass"),
                  I18n.getString("VariableNode.Property.VariableClass"));
            this.variable = variable;
        }

        @Override
        public boolean canWrite()
        {
            return !getVariable().isSystemDefined();
        }

        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return getVariable().getValueClassName();
        }

        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

            if (val == null) 
            {
                return;
            }
            if (val instanceof String)
            {
                String s = ((String)val).trim();
                if (s.length() == 0) s = "java.lang.String";

                String oldValue = getVariable().getValueClassName();
                String newValue = s;
                getVariable().setValueClassName(s);
                
                ObjectPropertyUndoableEdit urob = new ObjectPropertyUndoableEdit(getVariable(),"ValueClassName", String.class ,oldValue,newValue );
                
                if (getVariable().getExpression() != null)
                {
                    ((JRDesignExpression)getVariable().getExpression()).setValueClassName(s);
                    ObjectPropertyUndoableEdit urob2 = new ObjectPropertyUndoableEdit((JRDesignExpression)getVariable().getExpression(),"ValueClassName", String.class ,oldValue,newValue );
                    urob.addEdit(urob2);
                }
                if (getVariable().getInitialValueExpression() != null)
                {
                    ((JRDesignExpression)getVariable().getInitialValueExpression()).setValueClassName(s);
                    ObjectPropertyUndoableEdit urob2 = new ObjectPropertyUndoableEdit((JRDesignExpression)getVariable().getInitialValueExpression(),"ValueClassName", String.class ,oldValue,newValue );
                    urob.addEdit(urob2);
                }
                
                
                // Find the undoRedo manager...
                IReportManager.getInstance().addUndoableEdit(urob);
            }
        }

        @Override
        public boolean isDefaultValue() {
            return getVariable().getValueClassName().equals("java.lang.String");
        }

        @Override
        public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
            super.restoreDefaultValue();
            setValue("java.lang.String");
            editor.setValue("java.lang.String");
        }

        @Override
        public boolean supportsDefaultValue() {
            return true;
        }

        public JRDesignVariable getVariable() {
            return variable;
        }

        @Override
        @SuppressWarnings("unchecked")
        public PropertyEditor getPropertyEditor() {

            if (editor == null)
            {
                if (getVariable().isSystemDefined()){
                    editor = super.getPropertyEditor();
                }
                else
                {
                    java.util.List classes = new ArrayList();
                    classes.add(new Tag("java.lang.Boolean"));
                    classes.add(new Tag("java.lang.Byte"));
                    classes.add(new Tag("java.util.Date"));
                    classes.add(new Tag("java.sql.Timestamp"));
                    classes.add(new Tag("java.sql.Time"));
                    classes.add(new Tag("java.lang.Double"));
                    classes.add(new Tag("java.lang.Float"));
                    classes.add(new Tag("java.lang.Integer"));
                    classes.add(new Tag("java.lang.Long"));
                    classes.add(new Tag("java.lang.Short"));
                    classes.add(new Tag("java.math.BigDecimal"));
                    classes.add(new Tag("java.lang.Number"));
                    classes.add(new Tag("java.lang.String"));
                    classes.add(new Tag("java.util.Collection"));
                    classes.add(new Tag("java.util.List"));
                    classes.add(new Tag("java.lang.Object"));
                    classes.add(new Tag("java.io.InputStream"));
                    classes.add(new Tag("net.sf.jasperreports.engine.JREmptyDataSource"));
                    editor = new ComboBoxPropertyEditor(true, classes);
                }
            }
            return editor;
        }

        @Override
        public Object getValue(String attributeName) {
            if ("canEditAsText".equals(attributeName)) return true;
            if ("oneline".equals(attributeName)) return true;
            if ("suppressCustomEditor".equals(attributeName)) return false;
            return super.getValue(attributeName);
        }
    }
    
    
   /**
     *  Class to manage the JRDesignVariable.PROPERTY_CALCULATION property
     */
    private static final class CalculationProperty extends PropertySupport
    {
            //private JRDesignDataset dataset = null;
            private JRDesignVariable variable = null;
            private ComboBoxPropertyEditor editor;
            
            @SuppressWarnings("unchecked")
            public CalculationProperty(JRDesignVariable variable)
            {
                // TODO: Replace WhenNoDataType with the right constant
                super( JRDesignVariable.PROPERTY_CALCULATION,CalculationEnum.class, I18n.getString("VariableNode.Property.Calculation"), I18n.getString("VariableNode.Property.Calculationdetail"), true, true);
                //this.dataset = dataset;
                this.variable = variable;
                setValue("suppressCustomEditor", Boolean.TRUE);
            }

            @Override
            public boolean isDefaultValue() {
                return variable.getCalculationValue() == CalculationEnum.NOTHING;
            }

            @Override
            public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {

                setValue(CalculationEnum.NOTHING);
            }

            @Override
            public boolean supportsDefaultValue() {
                return true;
            }

            @Override
            @SuppressWarnings("unchecked")
            public PropertyEditor getPropertyEditor() {

                if (editor == null)
                {
                    java.util.ArrayList l = new java.util.ArrayList();
                    
                    l.add(new Tag(CalculationEnum.NOTHING, I18n.getString("VariableNode.Property.Nothing")));
                    l.add(new Tag(CalculationEnum.COUNT, I18n.getString("VariableNode.Property.Count")));
                    l.add(new Tag(CalculationEnum.DISTINCT_COUNT, I18n.getString("VariableNode.Property.DistinctCount")));
                    l.add(new Tag(CalculationEnum.SUM, I18n.getString("VariableNode.Property.Sum")));
                    l.add(new Tag(CalculationEnum.AVERAGE, I18n.getString("VariableNode.Property.Average")));
                    l.add(new Tag(CalculationEnum.LOWEST, I18n.getString("VariableNode.Property.Lowest")));
                    l.add(new Tag(CalculationEnum.HIGHEST, I18n.getString("VariableNode.Property.Highest")));
                    l.add(new Tag(CalculationEnum.STANDARD_DEVIATION, I18n.getString("VariableNode.Property.StandardDeviation")));
                    l.add(new Tag(CalculationEnum.VARIANCE, I18n.getString("VariableNode.Property.Variance")));
                    l.add(new Tag(CalculationEnum.SYSTEM, I18n.getString("VariableNode.Property.System")));
                    l.add(new Tag(CalculationEnum.FIRST, I18n.getString("VariableNode.Property.First")));
                    
                    editor = new ComboBoxPropertyEditor(false, l);
                }
                return editor;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return variable.getCalculationValue();
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof CalculationEnum)
                {
                    CalculationEnum oldValue = variable.getCalculationValue();
                    CalculationEnum newValue = (CalculationEnum)val;
                    variable.setCalculation(newValue);
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                variable,
                                "Calculation", 
                                CalculationEnum.class,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
                }
            }
    }
    
    /**
     *  Class to manage the JRDesignVariable.PROPERTY_CALCULATION property
     */
    private static final class ResetTypeProperty extends PropertySupport
    {
            private JRDesignDataset dataset = null;
            private JRDesignVariable variable = null;
            private ComboBoxPropertyEditor editor;
            
            @SuppressWarnings("unchecked")
            public ResetTypeProperty(JRDesignVariable variable, JRDesignDataset dataset)
            {
                // TODO: Replace WhenNoDataType with the right constant
                super( JRDesignVariable.PROPERTY_RESET_TYPE,ResetTypeEnum.class, I18n.getString("VariableNode.Property.Resettype"), I18n.getString("VariableNode.Property.Resettypedetail"), true, true);
                this.variable = variable;
                this.dataset = dataset;
                setValue("suppressCustomEditor", Boolean.TRUE);
            }

            @Override
            public boolean isDefaultValue() {
                return variable.getResetTypeValue() == ResetTypeEnum.REPORT;
            }

            @Override
            public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
                setPropertyValue(ResetTypeEnum.REPORT);
            }

            @Override
            public boolean supportsDefaultValue() {
                return true;
            }

            @Override
            @SuppressWarnings("unchecked")
            public PropertyEditor getPropertyEditor() {

                if (editor == null)
                {
                    java.util.ArrayList l = new java.util.ArrayList();
                    
                    l.add(new Tag(ResetTypeEnum.REPORT, I18n.getString("VariableNode.Property.Report")));
                    l.add(new Tag(ResetTypeEnum.COLUMN, I18n.getString("VariableNode.Property.Column")));
                    l.add(new Tag(ResetTypeEnum.GROUP, I18n.getString("VariableNode.Property.Group")));
                    l.add(new Tag(ResetTypeEnum.NONE, I18n.getString("VariableNode.Property.None")));
                    l.add(new Tag(ResetTypeEnum.PAGE, I18n.getString("VariableNode.Property.Page")));
                    
                    editor = new ComboBoxPropertyEditor(false, l);
                }
                return editor;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return variable.getResetTypeValue();
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof ResetTypeEnum)
                {
                    setPropertyValue((ResetTypeEnum)val);
                }
            }
            
            private void setPropertyValue(ResetTypeEnum val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                    ResetTypeEnum oldValue = variable.getResetTypeValue();
                    ResetTypeEnum newValue = val;
                    
                    variable.setResetType(newValue);
                    
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                variable,
                                "ResetType", 
                                Byte.TYPE,
                                oldValue,newValue);
                    
                    JRGroup oldGroupValue = variable.getResetGroup();
                    JRGroup newGroupValue = null;
                    if ( val == ResetTypeEnum.GROUP )
                    {
                        if (dataset.getGroupsList().size() == 0)
                        {
                            IllegalArgumentException iae = annotateException(I18n.getString("VariableNode.Property.Message")); 
                            throw iae; 
                        }
                    
                        newGroupValue = (JRGroup)dataset.getGroupsList().get(0);
                    }
                    
                    if (oldGroupValue != newGroupValue)
                    {
                        ObjectPropertyUndoableEdit urobGroup =
                                new ObjectPropertyUndoableEdit(
                                    variable,
                                    "ResetGroup", 
                                    JRGroup.class,
                                    oldGroupValue,newGroupValue);
                        variable.setResetGroup(newGroupValue);
                        urob.concatenate(urobGroup);
                    }
                    
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
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
    
    /**
     *  Class to manage the JRDesignVariable.PROPERTY_CALCULATION property
     */
    private static final class ResetGroupProperty extends PropertySupport implements PropertyChangeListener
    {
            private JRDesignDataset dataset = null;
            private JRDesignVariable variable = null;
            private ComboBoxPropertyEditor editor;
            
            @SuppressWarnings("unchecked")
            public ResetGroupProperty(JRDesignVariable variable, JRDesignDataset dataset)
            {
                // TODO: Replace WhenNoDataType with the right constant
                super( JRDesignVariable.PROPERTY_RESET_GROUP,JRGroup.class, I18n.getString("VariableNode.Property.Resetgroup"), I18n.getString("VariableNode.Property.Resetgroupdetail"), true, true);
                this.variable = variable;
                this.dataset = dataset;
                dataset.getEventSupport().addPropertyChangeListener(WeakListeners.propertyChange(this, dataset.getEventSupport()));
                setValue("suppressCustomEditor", Boolean.TRUE);
            }

            @Override
            public boolean canWrite() {
                return !variable.isSystemDefined() && variable.getResetTypeValue() == ResetTypeEnum.GROUP;
            }

            
            @Override
            @SuppressWarnings("unchecked")
            public PropertyEditor getPropertyEditor() {

                if (editor == null)
                {
                    editor = new ComboBoxPropertyEditor(false, getListOfTags());
                }
                return editor;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return variable.getResetGroup() == null ? "" : variable.getResetGroup();
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof JRGroup)
                {
                    JRGroup oldValue = variable.getResetGroup();
                    JRGroup newValue = (JRGroup)val;
                    variable.setResetGroup(newValue);
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                variable,
                                "ResetGroup", 
                                JRGroup.class,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
                }
            }

        public void propertyChange(PropertyChangeEvent evt) {
            if (editor == null) return;
            if (evt.getPropertyName() == null) return;
            if (evt.getPropertyName().equals( JRDesignDataset.PROPERTY_GROUPS) ||
                evt.getPropertyName().equals( JRDesignGroup.PROPERTY_NAME))
            {
               editor.setTagValues(getListOfTags());
            }
        }

       private java.util.ArrayList getListOfTags()
        {
            java.util.ArrayList l = new java.util.ArrayList();
            List groups = dataset.getGroupsList();
            l.add(new Tag( null , ""));
            for (int i=0; i<groups.size(); ++i)
            {
                JRDesignGroup group = (JRDesignGroup)groups.get(i);
                l.add(new Tag( group , group.getName()));
                group.getEventSupport().removePropertyChangeListener(JRDesignGroup.PROPERTY_NAME, this); // Just in case...
                group.getEventSupport().addPropertyChangeListener(JRDesignGroup.PROPERTY_NAME, WeakListeners.propertyChange(this, group.getEventSupport()));
            }
            return l;
        }
    }
    
    
    /**
     *  Class to manage the JRDesignVariable.PROPERTY_INCREMENT_TYPE property
     */
    private static final class IncrementTypeProperty extends PropertySupport
    {
            private JRDesignDataset dataset = null;
            private JRDesignVariable variable = null;
            private ComboBoxPropertyEditor editor;
            
            @SuppressWarnings("unchecked")
            public IncrementTypeProperty(JRDesignVariable variable, JRDesignDataset dataset)
            {
                // TODO: Replace WhenNoDataType with the right constant
                super( JRDesignVariable.PROPERTY_INCREMENT_TYPE,IncrementTypeEnum.class, I18n.getString("VariableNode.Property.Incrementtype"), I18n.getString("VariableNode.Property.Incrementtypedetail"), true, true);
                this.variable = variable;
                this.dataset = dataset;
                setValue("suppressCustomEditor", Boolean.TRUE);
            }

            @Override
            public boolean isDefaultValue() {
                return variable.getIncrementTypeValue() == IncrementTypeEnum.NONE;
            }

            @Override
            public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
                setPropertyValue(IncrementTypeEnum.NONE);
            }

            @Override
            public boolean supportsDefaultValue() {
                return true;
            }

            @Override
            @SuppressWarnings("unchecked")
            public PropertyEditor getPropertyEditor() {

                if (editor == null)
                {
                    java.util.ArrayList l = new java.util.ArrayList();
                    
                    l.add(new Tag(IncrementTypeEnum.REPORT, I18n.getString("VariableNode.Property.Report")));
                    l.add(new Tag(IncrementTypeEnum.COLUMN, I18n.getString("VariableNode.Property.Column")));
                    l.add(new Tag(IncrementTypeEnum.GROUP, I18n.getString("VariableNode.Property.Group")));
                    l.add(new Tag(IncrementTypeEnum.NONE, I18n.getString("VariableNode.Property.None")));
                    l.add(new Tag(IncrementTypeEnum.PAGE, I18n.getString("VariableNode.Property.Page")));
                    
                    editor = new ComboBoxPropertyEditor(false, l);
                }
                return editor;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return variable.getIncrementTypeValue();
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof IncrementTypeEnum)
                {
                     setPropertyValue((IncrementTypeEnum)val);
                }
            }
            
            private void setPropertyValue(IncrementTypeEnum val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                    IncrementTypeEnum oldValue = variable.getIncrementTypeValue();
                    IncrementTypeEnum newValue = val;
                    
                    variable.setIncrementType(newValue);
                    
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                variable,
                                "IncrementType", 
                                IncrementTypeEnum.class,
                                oldValue,newValue);
                    
                    JRGroup oldGroupValue = variable.getIncrementGroup();
                    JRGroup newGroupValue = null;
                    if ( val == IncrementTypeEnum.GROUP )
                    {
                        if (dataset.getGroupsList().isEmpty())
                        {
                            IllegalArgumentException iae = annotateException(I18n.getString("VariableNode.Property.Message")); 
                            throw iae; 
                        }
                    
                        newGroupValue = (JRGroup)dataset.getGroupsList().get(0);
                    }
                    
                    if (oldGroupValue != newGroupValue)
                    {
                        ObjectPropertyUndoableEdit urobGroup =
                                new ObjectPropertyUndoableEdit(
                                    variable,
                                    "IncrementGroup", 
                                    JRGroup.class,
                                    oldGroupValue,newGroupValue);
                        variable.setIncrementGroup(newGroupValue);
                        urob.concatenate(urobGroup);
                    }
                    
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
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
    
    /**
     *  Class to manage the JRDesignVariable.PROPERTY_INCREMENT_GROUP property
     */
    private static final class IncrementGroupProperty extends PropertySupport implements PropertyChangeListener
    {
            private JRDesignDataset dataset = null;
            private JRDesignVariable variable = null;
            private ComboBoxPropertyEditor editor;
            
            @SuppressWarnings("unchecked")
            public IncrementGroupProperty(JRDesignVariable variable, JRDesignDataset dataset)
            {
                // TODO: Replace WhenNoDataType with the right constant
                super( JRDesignVariable.PROPERTY_INCREMENT_GROUP,JRGroup.class, I18n.getString("VariableNode.Property.Incrementgroup"), I18n.getString("VariableNode.Property.Incrementgroupdetail"), true, true);
                this.variable = variable;
                this.dataset = dataset;
                dataset.getEventSupport().addPropertyChangeListener(WeakListeners.propertyChange(this, dataset.getEventSupport()));
                setValue("suppressCustomEditor", Boolean.TRUE);
            }

            @Override
            public boolean canWrite() {
                return !variable.isSystemDefined() && variable.getIncrementTypeValue() == IncrementTypeEnum.GROUP;
            }

            
            @Override
            @SuppressWarnings("unchecked")
            public PropertyEditor getPropertyEditor() {

                if (editor == null)
                {
                    editor = new ComboBoxPropertyEditor(false, getListOfTags());

                }
                return editor;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return variable.getIncrementGroup() == null ? "" : variable.getIncrementGroup();
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof JRGroup)
                {
                    JRGroup oldValue = variable.getIncrementGroup();
                    JRGroup newValue = (JRGroup)val;
                    variable.setIncrementGroup(newValue);
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                variable,
                                "IncrementGroup", 
                                JRGroup.class,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
                }
            }

        public void propertyChange(PropertyChangeEvent evt) {
            if (editor == null) return;
            if (evt.getPropertyName() == null) return;
            if (evt.getPropertyName().equals( JRDesignDataset.PROPERTY_GROUPS) ||
                evt.getPropertyName().equals( JRDesignGroup.PROPERTY_NAME))
            {
                editor.setTagValues(getListOfTags());
            }
        }

        private java.util.ArrayList getListOfTags()
        {
            java.util.ArrayList l = new java.util.ArrayList();
            List groups = dataset.getGroupsList();
            l.add(new Tag( null , ""));
            for (int i=0; i<groups.size(); ++i)
            {
                JRDesignGroup group = (JRDesignGroup)groups.get(i);
                l.add(new Tag( group , group.getName()));
                group.getEventSupport().removePropertyChangeListener(JRDesignGroup.PROPERTY_NAME, this); // Just in case...
                group.getEventSupport().addPropertyChangeListener(JRDesignGroup.PROPERTY_NAME, WeakListeners.propertyChange(this, group.getEventSupport()));
            }
            return l;
        }
    }
    
    
    /**
     *  Class to manage the JRDesignVariable.PROPERTY_NAME property
     */
    public static final class IncrementerFactoryClassNameProperty extends PropertySupport.ReadWrite {

        JRDesignVariable variable = null;

        @SuppressWarnings("unchecked")
        public IncrementerFactoryClassNameProperty(JRDesignVariable variable)
        {
            super(JRDesignVariable.PROPERTY_INCREMENTER_FACTORY_CLASS_NAME, String.class,
                  I18n.getString("VariableNode.Property.IncrementerFactoryClass"),
                  I18n.getString("VariableNode.Property.IncrementerFactoryClassdetail"));
            this.variable = variable;
            this.setValue("oneline", Boolean.TRUE);
        }

        @Override
        public boolean canWrite()
        {
            return !getVariable().isSystemDefined();
        }

        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return (getVariable().getIncrementerFactoryClassName() == null) ? "" : getVariable().getIncrementerFactoryClassName();
        }

        @SuppressWarnings("unchecked")
        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

            String s = val == null ?  null : val+"";
            if (s != null && (s.trim().length() == 0 ||
                s.equals("null"))) s = null;

            String oldName = getVariable().getIncrementerFactoryClassName();
            getVariable().setIncrementerFactoryClassName(s);

            ObjectPropertyUndoableEdit opue = new ObjectPropertyUndoableEdit(
                    getVariable(), "IncrementerFactoryClassName", String.class, oldName, getVariable().getName());

            IReportManager.getInstance().addUndoableEdit(opue);

        }

        public JRDesignVariable getVariable() {
            return variable;
        }

        public void setVariable(JRDesignVariable variable) {
            this.variable = variable;
        }
    }
}
