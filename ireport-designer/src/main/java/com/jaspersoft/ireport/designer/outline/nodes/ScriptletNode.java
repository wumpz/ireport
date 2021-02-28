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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.sheet.properties.StringProperty;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.engine.JRScriptlet;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignScriptlet;
import net.sf.jasperreports.engine.design.JasperDesign;
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
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * ScriptletNode detects the events fired by the subtended scriptlet.
 * Implements the support for the property sheet of a scriptlet.
 * If a scriptlet is system defined, it can not be cut.
 * Actions of a scriptlet node include copy, paste, reorder, rename and delete.
 * 
 * @author gtoffoli
 */
public class ScriptletNode extends IRAbstractNode implements PropertyChangeListener {

    JasperDesign jd = null;
    private JRDesignScriptlet scriptlet = null;

    public ScriptletNode(JasperDesign jd, JRDesignScriptlet scriptlet, Lookup doLkp)
    {
        super (Children.LEAF, new ProxyLookup(doLkp, Lookups.fixed(jd, scriptlet)));
        this.jd = jd;
        this.scriptlet = scriptlet;
        setDisplayName ( scriptlet.getName());
        super.setName( scriptlet.getName() );
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/scriptlet-16.png");
        
        scriptlet.getEventSupport().addPropertyChangeListener(this);
    }

    @Override
    public String getDisplayName() {
        return scriptlet.getName();
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
        
        set.put(new NameProperty( getScriptlet(),dataset));
        set.put(new ValueClassNameProperty( getScriptlet(), dataset));
        set.put(new DescriptionProperty(getScriptlet()));
        
        sheet.put(set);
        return sheet;
    }
    
    @Override
    public boolean canCut() {
        return !scriptlet.getName().equals("REPORT");
    }
    
    @Override
    public boolean canRename() {
        return !scriptlet.getName().equals("REPORT");
    }
    
    @Override
    public boolean canDestroy() {
        return !scriptlet.getName().equals("REPORT");
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

          System.out.println("Scriptlet removing...");
          System.out.flush();
          if (getParentNode() == null) return;

          System.out.println("Scriptlet removed...getParentNode() " + getParentNode());
          System.out.flush();

          JRDesignDataset dataset = getParentNode().getLookup().lookup(JRDesignDataset.class);
          if (dataset != null)
          {
            System.out.println("Scriptlet removed...dataset " + dataset);
            System.out.flush();
            dataset.removeScriptlet(getScriptlet());
            System.out.println("Scriptlet removed..." + dataset.getScriptletsList() + " " + dataset.getScriptletsMap());
            System.out.flush();

            dataset.getEventSupport().firePropertyChange(JRDesignDataset.PROPERTY_SCRIPTLETS, null, null);
          }
          super.destroy();
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
        return tras;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setName(String s) {
        
        if (s.equals("") || s.equals("REPORT"))
        {
            throw new IllegalArgumentException("Scriptlet name not valid.");
        }
        
        List<JRScriptlet> currentScriptlets = null;
        JRDesignDataset dataset = getParentNode().getLookup().lookup(JRDesignDataset.class);
        currentScriptlets = (List<JRScriptlet>)dataset.getScriptletsList();
        for (JRScriptlet pa : currentScriptlets)
        {
            JRDesignScriptlet p = (JRDesignScriptlet)pa;
            if (p != getScriptlet() && p.getName().equals(s))
            {
                throw new IllegalArgumentException("Scriptlet name already in use.");
            }
        }
        
        String oldName = getScriptlet().getName();
        getScriptlet().setName(s);
        dataset.getScriptletsMap().remove(oldName);
        dataset.getScriptletsMap().put(s,getScriptlet());
        
        ObjectPropertyUndoableEdit opue = new ObjectPropertyUndoableEdit(
                    getScriptlet(), "Name", String.class, oldName, s);

        IReportManager.getInstance().addUndoableEdit(opue);
    }

    public JRDesignScriptlet getScriptlet() {
        return scriptlet;
    }

    public void setScriptlet(JRDesignScriptlet scriptlet) {
        this.scriptlet = scriptlet;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignScriptlet.PROPERTY_NAME ))
        {
            super.setName(getScriptlet().getName());
            this.setDisplayName(getScriptlet().getName());
        }
        
        // Update the sheet
        this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue() );
    }
    
    /***************  SHEET PROPERTIES DEFINITIONS **********************/
    
    
    /**
     *  Class to manage the JRDesignScriptlet.PROPERTY_NAME property
     */
    public static final class NameProperty extends PropertySupport.ReadWrite {

        JRDesignScriptlet scriptlet = null;
        JRDesignDataset dataset = null;

        @SuppressWarnings("unchecked")
        public NameProperty(JRDesignScriptlet scriptlet, JRDesignDataset dataset)
        {
            super(JRDesignScriptlet.PROPERTY_NAME, String.class,
                  I18n.getString("ScriptletNode.Property.Name"),
                  I18n.getString("ScriptletNode.Property.Namedetail"));
            this.scriptlet = scriptlet;
            this.dataset = dataset;
            this.setValue("oneline", Boolean.TRUE);
        }

        @Override
        public boolean canWrite()
        {
            return !getScriptlet().getName().equals("REPORT");
        }

        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return getScriptlet().getName();
        }

        @SuppressWarnings("unchecked")
        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {


            if (val == null || val.equals(""))
            {
                IllegalArgumentException iae = annotateException(I18n.getString("ScriptletNode.Property.NameInvalid"));
                throw iae; 
            }

            String s = val+"";

            List<JRScriptlet> currentScriptlets = null;
            currentScriptlets = (List<JRScriptlet>)getDataset().getScriptletsList();
            for (JRScriptlet pa : currentScriptlets)
            {
                JRDesignScriptlet p = (JRDesignScriptlet)pa;
                if (p != getScriptlet() && p.getName().equals(s))
                {
                    IllegalArgumentException iae = annotateException(I18n.getString("ScriptletNode.Property.NameInUse"));
                    throw iae; 
                }
            }
            String oldName = getScriptlet().getName();
            getScriptlet().setName(s);
            dataset.getScriptletsMap().remove(oldName);
            dataset.getScriptletsMap().put(s,getScriptlet());

            ObjectPropertyUndoableEdit opue = new ObjectPropertyUndoableEdit(
                    getScriptlet(), "Name", String.class, oldName, getScriptlet().getName());

            IReportManager.getInstance().addUndoableEdit(opue);

        }

        public JRDesignDataset getDataset() {
            return dataset;
        }

        public void setDataset(JRDesignDataset dataset) {
            this.dataset = dataset;
        }

        public JRDesignScriptlet getScriptlet() {
            return scriptlet;
        }

        public void setScriptlet(JRDesignScriptlet scriptlet) {
            this.scriptlet = scriptlet;
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
     *  Class to manage the JRDesignScriptlet.PROPERTY_VALUE_CLASS_NAME property
     */
    public class ValueClassNameProperty extends StringProperty {

        JRDesignScriptlet scriptlet = null;
        PropertyEditor editor = null;
        JRDesignDataset dataset = null;

        @SuppressWarnings("unchecked")
        public ValueClassNameProperty(JRDesignScriptlet scriptlet, JRDesignDataset dataset)
        {
            super(scriptlet);
            setName(JRDesignScriptlet.PROPERTY_VALUE_CLASS_NAME);
            setDisplayName(I18n.getString("ScriptletNode.Property.ScriptletClass"));
            setShortDescription(I18n.getString("ScriptletNode.Property.ScriptletClass"));
            this.scriptlet = scriptlet;
            this.dataset = dataset;
        }

        @Override
        public String getString() {

            if (scriptlet.getName().equals("REPORT"))
            {
                return dataset.getScriptletClass();
            }
            else
            {
                return scriptlet.getValueClassName();
            }
        }

        @Override
        public String getOwnString() {
            return getString();
        }

        @Override
        public String getDefaultString() {
            return "net.sf.jasperreports.engine.JRDefaultScriptlet";
        }

        @Override
        public boolean supportsDefaultValue() {
            return false;
        }

        @Override
        public void setString(String value) {

            if (value == null ||
                value.equals(""))
            {
                // set the default value...
                value = "net.sf.jasperreports.engine.JRDefaultScriptlet";
            }

            if (scriptlet.getName().equals("REPORT"))
            {
                dataset.setScriptletClass(value);
                scriptlet.setValueClassName(value);
            }
            else
            {
                scriptlet.setValueClassName(value);
                JRDesignParameter p = (JRDesignParameter)dataset.getParametersMap().get(scriptlet.getName()+"_SCRIPTLET");
                if (p!=null)
                {
                    p.setValueClassName(value);
                }
            }
        }
    }
    
    
   
    
    /**
     *  Class to manage the JRDesignScriptlet.PROPERTY_VALUE_CLASS_NAME property
     */
    public class DescriptionProperty extends StringProperty {

        JRDesignScriptlet scriptlet = null;
        PropertyEditor editor = null;

        @SuppressWarnings("unchecked")
        public DescriptionProperty(JRDesignScriptlet scriptlet)
        {
            super(scriptlet);
            setName(JRDesignScriptlet.PROPERTY_DESCRIPTION);
            setDisplayName(I18n.getString("ScriptletNode.Property.ScriptletDescription"));
            setShortDescription(I18n.getString("ScriptletNode.Property.ScriptletDescription"));
            this.scriptlet = scriptlet;
        }

        @Override
        public boolean canWrite() {
            return !scriptlet.getName().equals("REPORT");
        }

        @Override
        public String getString() {
            return scriptlet.getDescription();
        }

        @Override
        public String getOwnString() {
            return scriptlet.getDescription();
        }

        @Override
        public String getDefaultString() {
            return "";
        }

        @Override
        public void setString(String value) {
            scriptlet.setDescription(value);
        }

    }
 
}
