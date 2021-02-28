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

import com.jaspersoft.ireport.designer.sheet.properties.FilterExpressionProperty;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.data.queryexecuters.QueryExecuterDef;
import com.jaspersoft.ireport.designer.menu.EditQueryAction;
import com.jaspersoft.ireport.designer.sheet.JRPropertiesMapProperty;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.sheet.properties.EnumProperty;
import com.jaspersoft.ireport.designer.undo.DeleteDatasetUndoableEdit;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.Action;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;
import org.openide.actions.CopyAction;
import org.openide.actions.DeleteAction;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class DatasetNode extends IRAbstractNode implements PropertyChangeListener {


    JasperDesign jd = null;
    JRDesignDataset dataset = null;

    public DatasetNode(JasperDesign jd, JRDesignDataset dataset, Lookup doLkp)
    {
        super (new DatasetChildren(jd, dataset, doLkp), new ProxyLookup(doLkp, Lookups.singleton(jd)));
        this.jd = jd;
        this.dataset = dataset;
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/dataset-16.png");
        dataset.getEventSupport().addPropertyChangeListener(this);
    }

    public JRDesignDataset getDataset() {
        return dataset;
    }
    
    @Override
    public String getDisplayName() {
        return dataset.getName();
    }
    

    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set datasetPropertySet = Sheet.createPropertiesSet();
        datasetPropertySet.setName("DATASET_PROPERTIES");
        datasetPropertySet.setDisplayName("Dataset properties");
        
        fillDatasetPropertySet(datasetPropertySet, dataset, jd);
        sheet.put(datasetPropertySet);
        return sheet;
    }
    
    public static Sheet.Set fillDatasetPropertySet(Sheet.Set datasetPropertySet, JRDesignDataset dataset, JasperDesign jasperDesign)
    {
        if (!dataset.isMainDataset())
        {
            datasetPropertySet.put(new NameProperty( dataset, jasperDesign ));
        }
        datasetPropertySet.put(new ScriptletProperty( dataset ));
        datasetPropertySet.put(new ResourceBundleProperty( dataset ));
        datasetPropertySet.put(new WhenResourceMissingTypeProperty( dataset ));
        datasetPropertySet.put(new QueryTextProperty( dataset ));
        datasetPropertySet.put(new QueryLanguageProperty( dataset ));
        datasetPropertySet.put(new FilterExpressionProperty( dataset ));
        
        datasetPropertySet.put(new JRPropertiesMapProperty(dataset));
        
        return datasetPropertySet;
    }
    
    @Override
    public Action[] getActions(boolean context) {
        
        Action[] actions = super.getActions(context);
        java.util.ArrayList<Action> myactions = new java.util.ArrayList<Action>();
        for (int i=0; i<actions.length; ++i)
        {
            myactions.add(actions[i]);
        }
        
        myactions.add(SystemAction.get(EditQueryAction.class));
        myactions.add(null);
        myactions.add(SystemAction.get(CopyAction.class));
        myactions.add(SystemAction.get(DeleteAction.class));
        
        return myactions.toArray(new Action[myactions.size()]);
    }

    /**
     *  Class to manage the JasperDesign.PROPERTY_PAGE_WIDTH property
     */
    private static final class NameProperty extends PropertySupport
    {
            private final JRDesignDataset dataset;
            private final JasperDesign jd;
        
            @SuppressWarnings("unchecked")
            public NameProperty(JRDesignDataset dataset, JasperDesign jd)
            {
                super(JRDesignDataset.PROPERTY_NAME,String.class, "Dataset name", "The name of this dataset", true, true);
                this.dataset = dataset;
                this.jd= jd;
                this.setValue("oneline", Boolean.TRUE);
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return dataset.getName();
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof String)
                {
                    String oldValue = dataset.getName();
                    String newValue = (String)val;
                    dataset.setName(newValue);
                
                    if (!dataset.isMainDataset())
                    {
                        jd.getDatasetMap().remove(oldValue);
                        jd.getDatasetMap().put(newValue,dataset);
                    }
                    
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                dataset,
                                "Name", 
                                String.class,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
            
                }
            }
    }
    
    /**
     *  Class to manage the JRDesignDataset.PROPERTY_SCRIPTLET_CLASS property
     */
    private static final class ScriptletProperty extends PropertySupport
    {
            private final JRDesignDataset dataset;
        
            @SuppressWarnings("unchecked")
            public ScriptletProperty(JRDesignDataset dataset)
            {
                super(JRDesignDataset.PROPERTY_SCRIPTLET_CLASS,String.class, "Scriptlet class", "The scriptlet class to use with the dataset",true, true);
                this.dataset = dataset;
                this.setValue("oneline", Boolean.TRUE);
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return (dataset.getScriptletClass() == null) ? "" : dataset.getScriptletClass();
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof String)
                {
                    String oldValue = dataset.getScriptletClass();
                    String newValue = (val == null || ((String)val).trim().length() == 0) ? null : ((String)val).trim();
                    dataset.setScriptletClass( newValue );
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                dataset,
                                "ScriptletClass", 
                                String.class,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
                    dataset.getEventSupport().firePropertyChange( JRDesignDataset.PROPERTY_SCRIPTLETS, 0, 0);
                }
            }
    }
    
    /**
     *  Class to manage the JRDesignDataset.PROPERTY_RESOURCE_BUNDLE property
     */
    private static final class ResourceBundleProperty extends PropertySupport
    {
            private final JRDesignDataset dataset;
        
            @SuppressWarnings("unchecked")
            public ResourceBundleProperty(JRDesignDataset dataset)
            {
                super(JRDesignDataset.PROPERTY_RESOURCE_BUNDLE,String.class, "Resource bundle", "The base name of the resource bundle used to localize the report",true, true);
                this.dataset = dataset;
                this.setValue("oneline", Boolean.TRUE);
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return (dataset.getResourceBundle() == null) ? "" : dataset.getResourceBundle();
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof String)
                {
                    String oldValue = dataset.getResourceBundle();
                    String newValue = (val == null || ((String)val).trim().length() == 0) ? null : ((String)val).trim();
                    dataset.setResourceBundle( newValue );
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                dataset,
                                "ResourceBundle", 
                                String.class,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
            
                }
            }
    }
    
    /**
     *  Class to manage the WhenResourceMissingType property
     */
    private static final class WhenResourceMissingTypeProperty extends EnumProperty
    {
            private final JRDesignDataset dataset;
            
            @SuppressWarnings("unchecked")
            public WhenResourceMissingTypeProperty(JRDesignDataset dataset)
            {
                // TODO: Replace WhenNoDataType with the right constant
                super(WhenResourceMissingTypeEnum.class, dataset);
                this.dataset = dataset;
                setValue("suppressCustomEditor", Boolean.TRUE);
            }

            @Override
        public String getName()
        {
            return JRDesignDataset.PROPERTY_WHEN_RESOURCE_MISSING_TYPE;
        }

        @Override
        public String getDisplayName()
        {
            return I18n.getString("DatasetNode.Property.ResourceMissig");
        }

        @Override
        public String getShortDescription()
        {
            return I18n.getString("DatasetNode.Property.ResourceMissigdetail");
        }

        @Override
        public List getTagList()
        {
            List tags = new java.util.ArrayList();
            tags.add(new Tag(WhenResourceMissingTypeEnum.EMPTY, I18n.getString("DatasetNode.Property.Empty")));
            tags.add(new Tag(WhenResourceMissingTypeEnum.ERROR, I18n.getString("DatasetNode.Property.Error")));
            tags.add(new Tag(WhenResourceMissingTypeEnum.KEY, I18n.getString("DatasetNode.Property.Key")));
            tags.add(new Tag(WhenResourceMissingTypeEnum.NULL, I18n.getString("DatasetNode.Property.Null")));
            return tags;
        }

        @Override
        public Object getPropertyValue()
        {
            return dataset.getWhenResourceMissingTypeValue();
        }

        @Override
        public Object getOwnPropertyValue()
        {
            return getPropertyValue();
        }

        @Override
        public Object getDefaultValue()
        {
            return WhenResourceMissingTypeEnum.NULL;
        }

        @Override
        public void setPropertyValue(Object val)
        {
            dataset.setWhenResourceMissingType((WhenResourceMissingTypeEnum)val);
        }
    }

    /**
     *  Class to manage the WhenResourceMissingType property
     */
    private static final class QueryLanguageProperty extends PropertySupport implements PreferenceChangeListener
    {
            private final JRDesignDataset dataset;
            private ComboBoxPropertyEditor editor;
            
            @SuppressWarnings("unchecked")
            public QueryLanguageProperty(JRDesignDataset dataset)
            {
                // TODO: Replace WhenNoDataType with the right constant
                super("queryLanguage",String.class, I18n.getString("DatasetNode.Property.Query"), I18n.getString("DatasetNode.Property.Query"), true, true);
                //FIXMETD properties might have the same name in different classes. reconsider this
                //super(JRDesignQuery.PROPERTY_LANGUAGE,String.class, "Query Language", "The language for the dataset query", true, true);
                this.dataset = dataset;
                setValue("suppressCustomEditor", Boolean.TRUE);
                this.setValue("oneline", Boolean.TRUE);
                this.setValue("canEditAsText", Boolean.TRUE);
                IReportManager.getPreferences().addPreferenceChangeListener(this);
            }

            @Override
            @SuppressWarnings("unchecked")
            public PropertyEditor getPropertyEditor() {

                if (editor == null)
                {
                    editor = new ComboBoxPropertyEditor(true, new ArrayList());
                    updateLanguages();
                }
                return editor;
            }

            protected void updateLanguages()
            {
                java.util.ArrayList list = new java.util.ArrayList();

                /*
                    list.add( new Tag("sql","SQL"));
                    list.add( new Tag("hql","Hibernate Query Language (HQL)"));
                    list.add( new Tag("xPath","XPath"));
                    list.add( new Tag("ejbql","EJBQL"));
                    list.add( new Tag("mdx","MDX"));
                    list.add( new Tag("xmla-mdx","XMLA-MDX"));
                 */

                    List<QueryExecuterDef> quxecuters = IReportManager.getInstance().getQueryExecuters();

                    synchronized(this)
                    {
                        for (int i=0; i<quxecuters.size(); ++i)
                        {
                            QueryExecuterDef qe = quxecuters.get(i);
                            String s = I18n.getString("language."+qe.getLanguage());
                            if (s == null || s.equals("language."+qe.getLanguage()))
                            {
                                s = qe.getLanguage();
                            }
                            list.add( new Tag(qe.getLanguage(),s));
                        }
                    }
                    
                    java.util.List<QueryExecuterDef> queryExecuters = IReportManager.getInstance().getQueryExecuters();

                    synchronized(this)
                    {
                        for (int k=0; k<queryExecuters.size(); ++k)
                        {
                            QueryExecuterDef qe = quxecuters.get(k);
                            String s = qe.getLanguage();
                            boolean found = false;
                            for (int i=0; i<list.size(); ++i)
                            {
                                Tag t = (Tag)list.get(i);
                                if (s.toLowerCase().equals( (t.getValue()+"").toLowerCase() ) )
                                {
                                    found = true;
                                }
                            }
                            if (!found)
                            {
                                list.add( new Tag(qe.getLanguage()));
                            }
                        }
                    }

                    if (editor != null)
                    {
                        editor.setTagValues(list);
                    }
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                
                if (dataset.getQuery() != null && dataset.getQuery().getLanguage() != null)
                    return dataset.getQuery().getLanguage();
                return "SQL";
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                
                    JRDesignQuery oldValue = (JRDesignQuery)dataset.getQuery();
                    String lang = (val == null) ? "SQL" : val+"";
                    if (lang.trim().length() == 0) lang = "SQL";
                    
                    JRDesignQuery newValue = new JRDesignQuery();
                    if (oldValue != null && oldValue.getText() != null)
                    {
                        newValue.setText(oldValue.getText());
                    }
                    
                    newValue.setLanguage(lang);
                    
                    dataset.setQuery(newValue);
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                dataset,
                                "Query", 
                                JRDesignQuery.class,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
            }

        public void preferenceChange(PreferenceChangeEvent evt) {

            try {
                updateLanguages();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     *  Class to manage the JRDesignDataset.PROPERTY_RESOURCE_BUNDLE property
     */
    private static final class QueryTextProperty extends PropertySupport
    {
            private final JRDesignDataset dataset;
        
            @SuppressWarnings("unchecked")
            public QueryTextProperty(JRDesignDataset dataset)
            {
                super(JRDesignQuery.PROPERTY_TEXT,String.class, I18n.getString("DatasetNode.Property.QueryText"), I18n.getString("DatasetNode.Property.QueryTextdetail"),true, true);
                this.dataset = dataset;
                //this.setValue("oneline", Boolean.TRUE);
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                
                if (dataset.getQuery() != null && dataset.getQuery().getText() != null)
                    return dataset.getQuery().getText();
                return "";
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                
                    JRDesignQuery oldValue = (JRDesignQuery)dataset.getQuery();
                    String text = (val == null) ? "" : val+"";
                    
                    JRDesignQuery newValue = new JRDesignQuery();
                    if (oldValue != null && oldValue.getLanguage() != null)
                    {
                        newValue.setLanguage(oldValue.getLanguage());
                    }
                    else
                    {
                        newValue.setLanguage("SQL");
                    }
                    
                    newValue.setText(text);
                    
                    dataset.setQuery(newValue);
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                dataset,
                                "Query", 
                                JRDesignQuery.class,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
            }
    }
    
    
    public void propertyChange(final PropertyChangeEvent evt) {
        
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
        if (evt.getPropertyName() == null) return;
        
        if(acceptProperty(evt))
        {
            this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            if (evt.getPropertyName().equals( JRDesignDataset.PROPERTY_NAME ))
            {
                this.fireDisplayNameChange(null, null);
            }
        }
        
        if (evt.getPropertyName().equals(JRDesignDataset.PROPERTY_QUERY))
        {
            firePropertyChange(JRDesignQuery.PROPERTY_TEXT, evt.getOldValue(), evt.getNewValue());
            firePropertyChange(JRDesignQuery.PROPERTY_LANGUAGE,  evt.getOldValue(), evt.getNewValue());
        }
    }
    
    /**
     *  This method looks if the property name is one of those handled by this node type.
     */
    public static boolean acceptProperty(PropertyChangeEvent evt) {
        
        if (evt.getPropertyName() == null) return false;
        if (evt.getPropertyName().equals( JRDesignDataset.PROPERTY_NAME ) ||
            evt.getPropertyName().equals( JRDesignDataset.PROPERTY_RESOURCE_BUNDLE ) ||
            evt.getPropertyName().equals( JRDesignDataset.PROPERTY_SCRIPTLET_CLASS ) ||
            evt.getPropertyName().equals( JRDesignDataset.PROPERTY_FILTER_EXPRESSION) ||
            evt.getPropertyName().equals( "WhenNoDataType") ||
            evt.getPropertyName().equals( JRDesignQuery.PROPERTY_TEXT) ||
            evt.getPropertyName().equals( JRDesignQuery.PROPERTY_LANGUAGE))
        {
            return true;
        }
        return false;
    }

    @Override
    public void destroy() throws IOException {
       
        
        int index = this.jd.getDatasetsList().indexOf(dataset);
        jd.removeDataset(dataset);
        DeleteDatasetUndoableEdit edit = new DeleteDatasetUndoableEdit(dataset,jd,index);
       
        IReportManager.getInstance().addUndoableEdit(edit, true);
        super.destroy();
    }
    
    @Override
    public boolean canDestroy() {
        return true;
    }

}
