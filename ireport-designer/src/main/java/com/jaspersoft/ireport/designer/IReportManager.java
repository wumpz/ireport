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
package com.jaspersoft.ireport.designer;

import com.jaspersoft.ireport.designer.connection.IReportConnectionFactory;
import com.jaspersoft.ireport.designer.connection.DefaultIReportConnectionFactory;
import com.jaspersoft.ireport.designer.connection.JREmptyDatasourceConnection;
import com.jaspersoft.ireport.designer.fonts.TTFFontsLoader;
import com.jaspersoft.ireport.designer.data.queryexecuters.QueryExecuterDef;
import com.jaspersoft.ireport.designer.export.DefaultExporterFactory;
import com.jaspersoft.ireport.designer.export.ExporterFactory;
import com.jaspersoft.ireport.designer.fonts.TTFFontsLoaderMonitor;
import com.jaspersoft.ireport.designer.outline.OutlineTopComponent;
import com.jaspersoft.ireport.designer.outline.nodes.CrosstabNode;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.templates.GenericFileTemplateItemAction;
import com.jaspersoft.ireport.designer.templates.ReportTemplateItemAction;
import com.jaspersoft.ireport.designer.templates.TemplateItemAction;
import com.jaspersoft.ireport.designer.undo.AggregatedUndoableEdit;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoableEdit;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.design.JRDesignChartDataset;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.FileResolver;
import org.apache.xerces.parsers.DOMParser;
import org.netbeans.api.db.explorer.JDBCDriver;
import org.netbeans.api.db.explorer.JDBCDriverManager;
import org.openide.awt.StatusDisplayer;
import org.openide.awt.UndoRedo;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbPreferences;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.Lookups;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author gtoffoli
 */
public class IReportManager {
    
    public static final String PROPERTY_CONNECTIONS = "PROPERTY_CONNECTIONS";
    public static final String PROPERTY_DEFAULT_CONNECTION = "PROPERTY_DEFAULT_CONNECTION";
    public static final String PROPERTY_SHOW_CELL_NAMES = "PROPERTY_SHOW_CELL_NAMES";
    public static final String PROPERTY_SHOW_BAND_NAMES = "PROPERTY_SHOW_BAND_NAMES";
    public static final String PROPERTY_JRPROPERTY_PREFIX = "ireport.jrproperty.";
    
    
    public static final String CURRENT_DIRECTORY = "CURRENT_DIRECTORY";
    public static final String IREPORT_CLASSPATH = "IREPORT_CLASSPATH";
    public static final String IREPORT_HIDDEN_CLASSPATH = "IREPORT_HIDDEN_CLASSPATH";
    public static final String IREPORT_RELODABLE_CLASSPATH = "IREPORT_RELODABLE_CLASSPATH";
    public static final String IREPORT_FONTPATH = "IREPORT_FONTPATH";
    public static final String DEFAULT_CONNECTION_NAME = "DEFAULT_CONNECTION_NAME";
    
    public static final String USE_AUTO_REGISTER_FIELDS = "UseAutoRegiesterFields";
    public static final String TEMPLATE_PATH = "TEMPLATE_PATH";
    
    
    private static ReportClassLoader reportClassLoader = null;
    private static IReportManager mainInstance = null;

    public static HashMap<String, ElementNodeFactory> elementNodeFactories = new HashMap<String, ElementNodeFactory>();

    private boolean noNetwork = false;


//    /**
//     * The jasperReports properties are stored in the JRProperties class.
//     * Anyway the set of properties stored here may change at run time when
//     * a report is compiled or executed. This methods returns the properties
//     * has they are stored in iReport. If you need to set a JRProperty,
//     * make sure you use the method setPersistentJRPRoperty().
//     * The reloadJasperReportsProperties method relies on this one to
//     * reload the properties. Please note that changes made to the JRProperties
//     * that are not done using setPersistentJRPRoperty may be lost during the
//     * use of iReport.
//     * 
//     * @return
//     */
//    public Properties getPersistentJRProperties()
//    {
//        Properties persistentProperties = new Properties();
//
//        Properties props = getDefaultJasperReportsProperties();
//        Enumeration en = props.keys();
//        while (en.hasMoreElements())
//        {
//            String key = (String) en.nextElement();
//            persistentProperties.setProperty(key, props.getProperty(key));
//        }
//
//        // Setting JR properties saved in iReport....
//        String[] keys = null;
//        try {
//            keys = getPreferences().keys();
//        } catch (BackingStoreException ex) {
//            //Exceptions.printStackTrace(ex);
//        }
//        for (int i=0; keys != null && i<keys.length; ++i)
//        {
//            if (keys[i].startsWith(PROPERTY_JRPROPERTY_PREFIX))
//            {
//                String name = keys[i].substring(PROPERTY_JRPROPERTY_PREFIX.length());
//                String value = getPreferences().get(keys[i], null);
//                if (value == null || name.length() == 0)
//                {
//                    getPreferences().remove(keys[i]);
//                }
//                else
//                {
//                    persistentProperties.setProperty(name, value);
//                }
//            }
//        }
//
//        return persistentProperties;
//    }

//    /**
//     * This method reset the JRProperties to the set of persisten properties
//     * set in iReport.
//     */
//    public void reloadJasperReportsProperties() {
//        
//        Properties props = getPersistentJRProperties();
//        Enumeration en = props.keys();
//        while (en.hasMoreElements())
//        {
//            String key = (String) en.nextElement();
//            JRProperties.setProperty(key, props.getProperty(key));
//        }
//
//        // remove other keys...
//        List keys = JRProperties.getProperties("");
//        for (int i=0; i<keys.size(); ++i)
//        {
//            PropertySuffix k = (JRProperties.PropertySuffix)keys.get(i);
//            if (!props.containsKey(k.getKey()))
//            {
//                JRProperties.removePropertyValue(k.getKey());
//            }
//        }
//    }

    /**
     * This is the most clean way to set a JRProperties property.
     * The property is set in the JRProperties and it is cached
     * to be restored in case a restore of the properties is performed
     * (i.e. after a report execution).
     * 
     * @param key - If null does nothing
     * @param value - If null, set the property to null (which means like remove it)
     */
    public void setJRProperty(String key, String value)
    {
        if (key == null) return;
        
        Preferences prefs = getPreferences();
        
        if (value != null)
        {
            prefs.put( PROPERTY_JRPROPERTY_PREFIX + key, value);
        }
        else
        {
            prefs.remove( PROPERTY_JRPROPERTY_PREFIX + key);
        }
    }
    
    
    

    private java.util.ArrayList<IReportConnection> connections = null;
    private java.util.ArrayList<QueryExecuterDef> queryExecuters = null;
    private java.util.List<IRFont> fonts = null;
    private java.util.List<FileResolver> fileResolvers = null;
    private java.util.HashMap parameterValues = new java.util.HashMap();
    private List<Tag> customLinkTypes = new ArrayList<Tag>();
    private final Set<JrxmlVisualViewActivatedListener> listeners = new HashSet<JrxmlVisualViewActivatedListener>(1); // or can use ChangeSupport in NB 6.0

    private final Set<JasperDesignActivatedListener> jasperDesignActivatedListeners = new HashSet<JasperDesignActivatedListener>(1);

    private java.util.List<IReportConnectionFactory> iReportConnectionFactories = null;
    private IReportConnection defaultConnection = null;
    private PropertyChangeSupport propertyChangeSupport = null;
    private JRDesignChartDataset chartDatasetClipBoard = null;
    private java.util.List chartSeriesClipBoard = null;
    private boolean forceAggregateUndo = false;
    private UndoableEdit lastUndoableEdit = null;
    private long lastUndoableEditTime = 0;
    private java.util.List<ExporterFactory> exporterFactories = null;
//    private Properties defaultJasperReportsProperties = null;


    public static ElementNode getComponentNode(JasperDesign jd, JRDesignComponentElement componentElement, Lookup lkp) {

        ElementNodeFactory factory = getElementNodeFactory(componentElement.getComponent().getClass().getName());
        if (factory != null)
        {
            return factory.createElementNode(jd, componentElement, lkp);
        }
        return null;
    }

    public static JRDesignElementWidget getComponentWidget(AbstractReportObjectScene scene, JRDesignComponentElement componentElement) {

        ElementNodeFactory factory = getElementNodeFactory(componentElement.getComponent().getClass().getName());
        if (factory != null)
        {
            return factory.createElementWidget(scene, componentElement);
        }
        return null;
    }

    public static ElementNodeFactory getElementNodeFactory(String componentClassName)
    {

        if (elementNodeFactories.containsKey(componentClassName))
        {
            return elementNodeFactories.get(componentClassName);
        }

        FileObject nodesFileObject = Repository.getDefault().getDefaultFileSystem().getRoot().getFileObject("ireport/components/nodes");
        if (nodesFileObject == null) return null;
        DataFolder nodesDataFolder = DataFolder.findFolder(nodesFileObject);
        if (nodesDataFolder == null) return null;

        Enumeration<DataObject> enObj = nodesDataFolder.children();
        while (enObj.hasMoreElements())
        {
            DataObject dataObject = enObj.nextElement();
            FileObject fileObject = dataObject.getPrimaryFile();
            String name = dataObject.getName();
            boolean instance = false;
            if (fileObject.getExt().equals("instance"))
            {
                instance = true;
            }
            name = name.replace('-', '.');

            try {
                if (name.equals(componentClassName))
                {
                    ElementNodeFactory enf = null;
                    if (instance)
                    {
                        Lookup lookup = Lookups.forPath("ireport/components/nodes"); // NOI18N
                        String elementFactoryClassName = (String)fileObject.getAttribute("instanceClass");
                        Collection<? extends ElementNodeFactory> elementNodeFactories = lookup.lookupAll(ElementNodeFactory.class);
                        
                        Iterator<? extends ElementNodeFactory> it = elementNodeFactories.iterator();
                        while (it.hasNext ()) {

                            ElementNodeFactory tmp_enf = it.next();

                            if (tmp_enf.getClass().getName().equals(elementFactoryClassName))
                            {
                                enf = tmp_enf;
                                break;
                            }
                        }
                    }
                    else
                    {
                        String elementFactoryClassName = (String)fileObject.getAttribute("elementFactory");
                        enf =((ElementNodeFactory)Class.forName(elementFactoryClassName).newInstance());

                    }
                    elementNodeFactories.put(componentClassName, enf);
                    return enf;
                }
               
            } catch (Throwable ex)
            {
                System.out.println("Unable to find element node Factory for the component class: " + name);
                ex.printStackTrace();
            }
        }
        return null;
    }


    /**
     * Look into the virtual file system for element decorators...
     *
     * @param element
     * @return
     */
    public static List<ElementDecorator> getElementDecorators(JRDesignElement element) {
        
        List<ElementDecorator> decorators = new ArrayList<ElementDecorator>();
        
        FileObject decoratorsFileObject = Repository.getDefault().getDefaultFileSystem().getRoot().getFileObject("ireport/decorators/elements");
        if (decoratorsFileObject == null) return decorators;
        DataFolder decoratorsDataFolder = DataFolder.findFolder(decoratorsFileObject);
        if (decoratorsDataFolder == null) return decorators;

        List<String> list = new ArrayList<String>();

        Collection<? extends ElementDecorator> elementDecoratorsInstances = Lookups.forPath("ireport/decorators/elements").lookupAll(ElementDecorator.class);
        Iterator<? extends ElementDecorator> it = elementDecoratorsInstances.iterator();
        while (it.hasNext ()) {

            ElementDecorator decorator = it.next();
            if (decorator.appliesTo(element))
            {
                decorators.add(decorator);
                list.add(decorator.getClass().getName());
            }
        }



        Enumeration<DataObject> enObj = decoratorsDataFolder.children();
        while (enObj.hasMoreElements())
        {
            DataObject dataObject = enObj.nextElement();
            String name = dataObject.getName();
            name = name.replace('-', '.');

            if (list.contains(name)) continue;
            try {
                ElementDecorator decorator = (ElementDecorator) Class.forName(name).newInstance();
                if (decorator.appliesTo(element))
                {
                    decorators.add(decorator);
                }
            } catch (Throwable ex)
            {
                //System.out.println("Unable to find element decorator class: " + name);
            }
        }

        
        return decorators;
    }
    
    

    public List<IRFont> getFonts() {
        return fonts;
    }

    public void reloadQueryExecuters() {

        // Clear not built-in query executers...
        //for (int i=0; i<queryExecuters.size(); ++i)
        //{
        //    QueryExecuterDef qe = queryExecuters.get(i);
        //    if (!qe.isBuiltin())
        //    {
        //        net.sf.jasperreports.engine.util.JRProperties.setProperty("net.sf.jasperreports.query.executer.factory." + qe.getLanguage(), null);
        //        queryExecuters.remove(qe);
        //   }
        //}


        queryExecuters.clear();

        
        JRPropertiesUtil jrPropUtils = IRLocalJasperReportsContext.getUtilities();
        
        
        // Adding defaults....
        addQueryExecuterDef(new QueryExecuterDef("sql",
                    jrPropUtils.getProperty("net.sf.jasperreports.query.executer.factory.sql"),
                    "com.jaspersoft.ireport.designer.data.fieldsproviders.SQLFieldsProvider", true), true);

        addQueryExecuterDef(new QueryExecuterDef("SQL",
                    jrPropUtils.getProperty("net.sf.jasperreports.query.executer.factory.SQL"),
                    "com.jaspersoft.ireport.designer.data.fieldsproviders.SQLFieldsProvider", true), true);

        addQueryExecuterDef(new QueryExecuterDef("xPath",
                    jrPropUtils.getProperty("net.sf.jasperreports.query.executer.factory.xPath"),
                    "com.jaspersoft.ireport.designer.data.fieldsproviders.XMLFieldsProvider", true), true);

        addQueryExecuterDef(new QueryExecuterDef("XPath",
                    jrPropUtils.getProperty("net.sf.jasperreports.query.executer.factory.XPath"),
                    "com.jaspersoft.ireport.designer.data.fieldsproviders.XMLFieldsProvider", true), true);

        addQueryExecuterDef(new QueryExecuterDef("hql",
                    jrPropUtils.getProperty("net.sf.jasperreports.query.executer.factory.hql"),
                    "com.jaspersoft.ireport.designer.data.fieldsproviders.HQLFieldsProvider", true), true);

        addQueryExecuterDef(new QueryExecuterDef("mdx",
                    jrPropUtils.getProperty("net.sf.jasperreports.query.executer.factory.mdx"),
                    "com.jaspersoft.ireport.designer.data.fieldsproviders.MDXFieldsProvider", true), true);

        addQueryExecuterDef(new QueryExecuterDef("MDX",
                    jrPropUtils.getProperty("net.sf.jasperreports.query.executer.factory.MDX"),
                    "com.jaspersoft.ireport.designer.data.fieldsproviders.MDXFieldsProvider", true), true);

        addQueryExecuterDef(new QueryExecuterDef("ejbql",
                    jrPropUtils.getProperty("net.sf.jasperreports.query.executer.factory.ejbql"),
                    "com.jaspersoft.ireport.designer.data.fieldsproviders.EJBQLFieldsProvider", true), true);

        addQueryExecuterDef(new QueryExecuterDef("EJBQL",
                    jrPropUtils.getProperty("net.sf.jasperreports.query.executer.factory.EJBQL"),
                    "com.jaspersoft.ireport.designer.data.fieldsproviders.EJBQLFieldsProvider", true), true);

        addQueryExecuterDef(new QueryExecuterDef("xmla-mdx",
                    jrPropUtils.getProperty("net.sf.jasperreports.query.executer.factory.xmla-mdx"),
                    "com.jaspersoft.ireport.designer.data.fieldsproviders.CincomMDXFieldsProvider", true), true);

        // Reload all the qe from the preferences...
        int k=0;
        Preferences pref = getPreferences();
        while (pref.get("queryExecuter."+k+".language", null) != null)
        {
            String lang = pref.get("queryExecuter."+k+".language", "");
            String clazz = pref.get("queryExecuter."+k+".class", "");
            String provider = pref.get("queryExecuter."+k+".provider", "");
            QueryExecuterDef qe = new QueryExecuterDef(lang, clazz, provider, false);
            addQueryExecuterDef(qe, true);
            k++;
        }
    }

    public void setFonts(List<IRFont> fonts) {
        this.fonts = fonts;
    }
    
    
    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }
            
    private IReportManager()
    {
        /*
        System.out.println("Drivers: ");
        JDBCDriver[] drivers = JDBCDriverManager.getDefault().getDrivers();
        for (int j = 0; j < drivers.length; j++) {
            System.out.println(drivers[j].getDisplayName() + " " + drivers[j].getClassName());
        }
        */
        
        propertyChangeSupport = new PropertyChangeSupport(this);
        
        
    }
    
    /**
     * This method initializa the manager. This code is not in the constructor
     * because it need to be executed using the ReportClassLoader, that requires an
     * instance of IReportManager.
     * 
     */
    private void initialize()
    {

        // Save the properties like they are when loaded from the default.jasperreports.properties file...
        // Add the custom properties to JasperReports properties as defaults...
        Properties ireportProps = new Properties();
        try
        {
              ireportProps.load( getClass().getResourceAsStream("/ireport.jasperreports.properties"));
              // add (or replace) the properties...
              Enumeration<String> names = (Enumeration<String>) ireportProps.propertyNames();
              while (names.hasMoreElements())
              {
                  String name = names.nextElement();
                  setJRProperty(name, ireportProps.getProperty(name));
              }
              
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
//        List props = IRLocalJasperReportsContext.getUtilities();
//
//        for (int i=0; i<props.size(); ++i)
//        {
//            JRProperties.PropertySuffix prop = (JRProperties.PropertySuffix)props.get(i);
//            if (prop.getKey() == null || prop.getValue() == null) continue;
//            getDefaultJasperReportsProperties().setProperty(prop.getKey(), prop.getValue());
//        }

        try {
            setJRProperty("net.sf.jasperreports.query.executer.factory.xmla-mdx",
                    "net.sf.jasperreports.engine.query.JRXmlaQueryExecuterFactory");
        } catch (Exception ex)
        { 
            System.out.println(I18n.getString("IReportManager.Error.WarningJRXmla") +ex.getMessage());
            System.out.flush();
        }

        try {
            setJRProperty("net.sf.jasperreports.xpath.executer.factory",
                    "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");

        } catch (Exception ex)
        { 
            System.out.println(I18n.getString("IReportManager.Error.ErrorJaxenXP") +ex.getMessage());
            System.out.flush();
        }

        // This initialize the query executers.
        getQueryExecuters();

        //reloadJasperReportsProperties();
        

        // Loading fonts...
        RequestProcessor.getDefault().post(new Runnable()
        {
            public void run()
            {
                //((ReportClassLoader)getReportClassLoader()).rescanAdditionalClasspath();
                // add the fonts directory to the classpath...
//                URL[] urls = null;
//                if (fontsDir != null && fontsDir.isDirectory())
//                {
//                    try {
//                        urls = new URL[]{fontsDir.toURI().toURL()};
//                    } catch (MalformedURLException ex) {
//                        Exceptions.printStackTrace(ex);
//                    }
//                }
//                else
//                {
//                    urls = new URL[0];
//                }
//                URLClassLoader urlCl = new URLClassLoader(
//                        urls, getReportClassLoader());

                Thread.currentThread().setContextClassLoader(getReportClassLoader());
                // Stuff to load the fonts...
                setFonts( TTFFontsLoader.loadTTFFonts(new TTFFontsLoaderMonitor() {

                    public void fontsLoadingStarted() {
                    }

                    public void fontsLoadingStatusUpdated(String statusMsg) {
                        StatusDisplayer.getDefault().setStatusText(statusMsg);
                    }

                    public void fontsLoadingFinished() {
                        StatusDisplayer.getDefault().setStatusText("");
                    }
                }) );
                
            }
        });
        
        /*
         System.out.println("Gif writers");
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("gif");
        while (writers.hasNext())
        {
            ImageWriter w=writers.next();
            System.out.println("For gif: " + w);
        }
        System.out.flush();
         */

        createPaletteItem();


        //create items for the template wizard....
        TemplateItemAction.addAction(new ReportTemplateItemAction());
        TemplateItemAction.addAction(new GenericFileTemplateItemAction(
                    I18n.getString("TemplateItemAction.Style.name"),I18n.getString("TemplateItemAction.Style.description"),
                    I18n.getString("Templates/Report/StyleTemplate.jrtx"),"Templates/Report/StyleTemplate.jrtx",
                    new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/template/new_style.png"))));

        TemplateItemAction.addAction(new GenericFileTemplateItemAction(
                    I18n.getString("TemplateItemAction.ChartTheme.name"),I18n.getString("TemplateItemAction.ChartTheme.description"),
                    "Templates/Report/ChartThemeTemplate.jrctx","Templates/Report/ChartThemeTemplate.jrctx",
                    new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/template/new_chart_theme.png"))));

        TemplateItemAction.addAction(new GenericFileTemplateItemAction(
                    I18n.getString("TemplateItemAction.ResourceBundle.name"),I18n.getString("TemplateItemAction.ResourceBundle.description"),
                    "Templates/Report/Bundle.properties","Templates/Report/Bundle.properties",
                    new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/template/new_resource_bundle.png"))));

        TemplateItemAction.addAction(new GenericFileTemplateItemAction(
                    I18n.getString("TemplateItemAction.GenericFile.name"),I18n.getString("TemplateItemAction.GenericFile.description"),
                    null,null,
                    new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/template/new_generic_file.png"))));



    }
    
    public static IReportManager getInstance()
    {
        if (mainInstance == null)
        {
            mainInstance = new IReportManager();
            Thread.currentThread().setContextClassLoader(getReportClassLoader());
            mainInstance.initialize();
        }
        return mainInstance;
    }
    
    /**
     * Return the available connections. 
     */
    public java.util.List<IReportConnection> getConnections()
    {
        if (connections == null)
        {
            connections = new java.util.ArrayList<IReportConnection>();
            // Load connections from preferences...
            for (int i=0; ; ++i)
            {
                String s = getPreferences().get("connection." + i, null);
                if (s == null) break;
                IReportConnection con = loadConnection(s);
                if (con != null) connections.add(con);
            }
            
            if (connections.size() == 0)
            {
                JREmptyDatasourceConnection c = new JREmptyDatasourceConnection();
                c.setName(I18n.getString("IReportManager.Type.EmptyDatasource"));
                connections.add(c);
                saveiReportConfiguration();
            }
        }
        return connections;
    }
    
    /**
     * Please use this method to add a connection.
     * It will fire an PROPERTY_CONNECTIONS cahnged event.
     * A call to saveiReportConfiguration() is suggested to make the changes permament
     */
    public void addConnection(IReportConnection con)
    {
        getConnections().add(con);
        saveiReportConfiguration();
        propertyChangeSupport.firePropertyChange(PROPERTY_CONNECTIONS, null, getConnections());
    }
    
    /**
     * Please use this method to remove a connection.
     * It will fire an PROPERTY_CONNECTIONS changed event.
     * A call to saveiReportConfiguration() is suggested to make the changes permament
     */
    public void removeConnection(IReportConnection con)
    {
        getConnections().remove(con);
        propertyChangeSupport.firePropertyChange(PROPERTY_CONNECTIONS, null, getConnections());
    }
    
    /***
     *  Load a connection from an XML to type iReportConnection
     * 
     */
    @SuppressWarnings("unchecked")
    public IReportConnection loadConnection(String xml)
    {
        try {
            
             ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
             Thread.currentThread().setContextClassLoader(DOMParser.class.getClassLoader());
             DOMParser parser = new DOMParser();
             org.xml.sax.InputSource input_sss  = new org.xml.sax.InputSource(new java.io.StringReader(xml));
             //input_sss.setSystemId(filename);
             parser.parse( input_sss );

             Thread.currentThread().setContextClassLoader(oldClassLoader);
             
             Document document = parser.getDocument();
             Node node = document.getDocumentElement();


            Node connectionNode = node;
             if (connectionNode.getNodeName() != null && connectionNode.getNodeName().equals("iReportConnection"))
             {
                // Take the CDATA...
                    String connectionName = "";
                    String connectionClass = "";
                    HashMap hm = new HashMap();
                    NamedNodeMap nnm = connectionNode.getAttributes();
                    if ( nnm.getNamedItem("name") != null) connectionName = nnm.getNamedItem("name").getNodeValue();
                    if ( nnm.getNamedItem("connectionClass") != null) connectionClass = nnm.getNamedItem("connectionClass").getNodeValue();

                    // Get all connections parameters...
                    NodeList list_child2 = connectionNode.getChildNodes();
                    for (int ck2=0; ck2< list_child2.getLength(); ck2++) {
                        String parameterName = "";
                        Node child_child = list_child2.item(ck2);
                        if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("connectionParameter")) {

                            NamedNodeMap nnm2 = child_child.getAttributes();
                            if ( nnm2.getNamedItem("name") != null)
                                parameterName = nnm2.getNamedItem("name").getNodeValue();
                            hm.put( parameterName,Misc.readPCDATA(child_child,false));
                        }
                    }

                    try {
                        IReportConnection con = (IReportConnection) Class.forName(connectionClass, true, getReportClassLoader()).newInstance();
                        con.loadProperties(hm);
                        con.setName(connectionName);
                        return con;
                    } catch (Exception ex) {

                        /*
                        JOptionPane.showMessageDialog(this,
                            Misc.formatString("Error loading  {0}", new Object[]{connectionName}), //"messages.connectionsDialog.errorLoadingConnection"
                            "Error", JOptionPane.ERROR_MESSAGE);
                         */
                        ex.printStackTrace();
                    }
                }
         } catch (Exception ex)
         {
             /*
             JOptionPane.showMessageDialog(this,
                                Misc.formatString("Error loading connections.\n{0}", new Object[]{ex.getMessage()}), //"messages.connectionsDialog.errorLoadingConnections"
                                "Error", JOptionPane.ERROR_MESSAGE);
             */
            ex.printStackTrace();
         }

         return null;
    }


    /**
     *  Return the current default connection reading the connection  name from the preferences
     *  if required.
     */
    public IReportConnection getDefaultConnection()
    {
        if (defaultConnection == null && connections.size() > 0)
        {
            String defaultConnectionName = getPreferences().get(DEFAULT_CONNECTION_NAME, null);
            if (defaultConnectionName != null)
            {
                for (IReportConnection con : getConnections())
                {
                    if (con.getName() != null && con.getName().equals(defaultConnectionName))
                    {
                        defaultConnection = con;
                        break;
                    }
                }
            }
            if (defaultConnection == null)
            {
                defaultConnection = connections.get(0);
            }
        }
        return defaultConnection;
    }
    
    public void setDefaultConnection(IReportConnection connection)
    {
        IReportConnection con = getDefaultConnection();
        defaultConnection = connection;
        if (defaultConnection == null)
        {
            getPreferences().remove(DEFAULT_CONNECTION_NAME);
        }
        else
        {
            getPreferences().put(DEFAULT_CONNECTION_NAME, defaultConnection.getName());
        }
        propertyChangeSupport.firePropertyChange(PROPERTY_DEFAULT_CONNECTION, con, defaultConnection);
    }
    
    public void saveiReportConfiguration()
    {
        try {
            int i=0;
            
            for (IReportConnection con : getConnections())
            {
                StringWriter sw = new StringWriter();
                con.save( new PrintWriter( sw ) );
                getPreferences().put("connection."+i,sw.toString());
                i++;
            }
            
            // Remove all the remaining connections...
            while (getPreferences().get("connection."+i, null) != null)
            {
                getPreferences().remove("connection."+i);
                i++;
            }
            
            getPreferences().flush();
        } catch (BackingStoreException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    

    /**
     * This method provides the preferred way to register a new connection type
     */
    @SuppressWarnings("unchecked")
    public void addConnectionImplementationFactory(IReportConnectionFactory factory)
    {
        getIReportConnectionFactories().add(factory);
    }
    /** 
     * This method provides the preferred way to register a new connection type
     */
    @SuppressWarnings("unchecked")
    public boolean addConnectionImplementation(String className)
    {

        DefaultIReportConnectionFactory connectionFactory = new DefaultIReportConnectionFactory(className);
        getIReportConnectionFactories().add(connectionFactory);
        /*
        if (getConnectionImplementations().contains(className)) return true;
        
        try {
            
            Class.forName(className, true, getReportClassLoader());
            getConnectionImplementations().add(className);
            return true;
        } catch (Throwable t)
        {
            t.printStackTrace();
        }
        */
        return true;
    }

    public static ClassLoader getReportClassLoader()
    {
        return getReportClassLoader(false);
    }

    public static ClassLoader getReportClassLoader(boolean recreate)
    {
        return IReportManager.getInstance().getReportClassLoaderImpl(recreate);
    }


    public static ClassLoader getJRExtensionsClassLoader()
    {
        if (reportClassLoader == null)
        {
            IReportManager.getInstance().getReportClassLoaderImpl(true);
        }
        return reportClassLoader;
    }

    private ClassLoader getReportClassLoaderImpl(boolean recreate)
    {
        if (recreate || reportClassLoader == null)
        {
            ClassLoader syscl = Lookup.getDefault().lookup(ClassLoader.class);
            reportClassLoader = new ReportClassLoader(syscl);
            //reportClassLoader.rescanAdditionalClasspath();
        
        }
        /*
        org.netbeans.editor.Registry.getMostActiveComponent();

        DataObject.Registry registries = DataObject.getRegistry();
        registries.
        Lookup lookup = Lookup.getDefault();
        lookup.lookup(arg0);

        ProxyClassLoader
        */
        // Add all the dabatase classpath entries...

        List<String> hiddenClasspath = getHiddenClasspath();

        JDBCDriverManager manager = JDBCDriverManager.getDefault();

        JDBCDriver[] drivers = manager.getDrivers();
        InstalledFileLocator locator = InstalledFileLocator.getDefault();


        for (int i=0; i<drivers.length; ++i)
        {
            URL[] urls = drivers[i].getURLs();
            for (int j=0; j<urls.length; ++j)
            {
                String path = urls[j].getPath();
                if (path.startsWith("/"))
                {
                    path = path.substring(1);
                }
                if (path.length() == 0) continue;
                File f = new File(path);
                if (!f.exists())
                {
                    f = locator.locate(path, null, false);
                }

                if (f != null && f.exists())
                {
                    try {
                        if (!hiddenClasspath.contains(f.getCanonicalPath()))
                        {
                            hiddenClasspath.add(f.getCanonicalPath());
                        }

                    } catch (IOException ex) {

                        ex.printStackTrace();
                    }
                }
            }
        }

        // Adding fonts directory...

        try {
            File fontsDir = Misc.getFontsDirectory();

            // This does not work apparently...
            if (!hiddenClasspath.contains(fontsDir.getCanonicalPath()))
            {
                hiddenClasspath.add(fontsDir.getCanonicalPath());
            }
        } catch (IOException ex) {}

        setHiddenClasspath(hiddenClasspath, false);
        reportClassLoader.rescanAdditionalClasspath();

        // set the not cached classpath...
        //

        String separator = System.getProperty("path.separator");
        List<String> rcp = getRelodableClasspath();
        /* THIS DOES NOT WORK... let's use a brand new classloader...
        String fullRcp = "";
        for (String path : rcp)
        {
            fullRcp += path + separator;
        }
        reportClassLoader.setRelodablePaths(fullRcp);
        */
        List<URL> urls = new ArrayList<URL>();
        int i=0;
        for (String path : rcp)
        {
            File f = new File(path);
            try {
                urls.add(f.toURI().toURL());
            } catch (MalformedURLException ex) {
                //Exceptions.printStackTrace(ex);
            }
        }

        ClassLoader newClassLoader = new IRURLClassLoader( urls.toArray(new URL[urls.size()]) , reportClassLoader);
        
        return newClassLoader;
    }

    /*
    public void registerAvailableJDBCDrivers()
    {
                // Reregister drivers...
        java.sql.DriverManager.registerDriver(driver);
        getReportClassLoader().
    }
    */

    /**
     *  Set the give object selected in the outline view.
     *  The lookup of the object is done looking first at the node that has this object in his lookup...
     */
    public void setSelectedObject(Object obj) {
        
        
        org.openide.nodes.Node root = OutlineTopComponent.getDefault().getExplorerManager().getRootContext();
        org.openide.nodes.Node node = null;
        if (obj == null)
        {
            node = root;
        }
        else
        {
            node = findNodeOf(obj, root);
        }
        
        if (node != null)
        {
            try {
                OutlineTopComponent.getDefault().getExplorerManager().setSelectedNodes(new org.openide.nodes.Node[]{node});
            } catch (PropertyVetoException ex) {
                // we are trying to highligh a node not present in the document...
                // Are we pasting something?
                // Look unti the parent...
            }
        }
        
    }
    
         
       

/**
     *  Set the give object selected in the outline view.
     *  The lookup of the object is done looking first at the node that has this object in his lookup...
     */
    public void addSelectedObject(Object obj) {
        
        
        org.openide.nodes.Node root = OutlineTopComponent.getDefault().getExplorerManager().getRootContext();
        org.openide.nodes.Node node = null;
        if (obj == null)
        {
            node = root;
        }
        else
        {
            node = findNodeOf(obj, root);
        }
        
        if (node != null)
        {
            try {
                List<org.openide.nodes.Node> selectedNodes = new ArrayList<org.openide.nodes.Node>();
                selectedNodes.addAll( Arrays.asList(OutlineTopComponent.getDefault().getExplorerManager().getSelectedNodes()));
                selectedNodes.add(node);
                OutlineTopComponent.getDefault().getExplorerManager().setSelectedNodes(selectedNodes.toArray(new org.openide.nodes.Node[selectedNodes.size()]));
            } catch (PropertyVetoException ex) {
                ex.printStackTrace();
            }
        }
        
    }
    
    /**
     *  Set the give object selected in the outline view.
     *  The lookup of the object is done looking first at the node that has this object in his lookup...
     */
    public void removeSelectedObject(Object obj) {
        
        
        org.openide.nodes.Node root = OutlineTopComponent.getDefault().getExplorerManager().getRootContext();
        org.openide.nodes.Node node = null;
        if (obj == null)
        {
            node = root;
        }
        else
        {
            node = findNodeOf(obj, root);
        }
        
        if (node != null)
        {
            try {
                List<org.openide.nodes.Node> selectedNodes = new ArrayList<org.openide.nodes.Node>();
                selectedNodes.addAll( Arrays.asList(OutlineTopComponent.getDefault().getExplorerManager().getSelectedNodes()));
                selectedNodes.remove(node);
                OutlineTopComponent.getDefault().getExplorerManager().setSelectedNodes(selectedNodes.toArray(new org.openide.nodes.Node[selectedNodes.size()]));
            } catch (PropertyVetoException ex) {
                ex.printStackTrace();
            }
        }
        
    }

//        private String str="";

    public org.openide.nodes.Node findNodeOf(Object obj, org.openide.nodes.Node root)
    {
        return findNodeOf(obj, root, true);
    }

    /**
     * Find the node that that has this object in his lookup, or the object is a well know object
     *  and can be found using a GenericLookup instance...
     * @param obj
     * @param root
     * @param recursive <- If tru, don't stop immediatly, but look for the most nested Node with this object in its lookup.
     * @return
     */
    public org.openide.nodes.Node findNodeOf(Object obj, org.openide.nodes.Node root, boolean recursive) {

        //str +=" ";
        org.openide.nodes.Node candidate = null;
        //System.out.println(str + "Looking for " + obj + " in "+root);
        //System.out.flush();
        if (obj == null || root == null) return null;
        
        // Look in the lookup
        if (root.getLookup().lookup(obj.getClass()) == obj)
        {
            if (!recursive) return root;
            
            candidate = root;
            if (obj instanceof JRDesignCrosstab &&
                root instanceof CrosstabNode)
            {
                return candidate;
            }
        }

        org.openide.nodes.Node[] children = root.getChildren().getNodes(true);
        for (int i=0; i<children.length; ++i)
        {
            org.openide.nodes.Node res = findNodeOf(obj, children[i],recursive);
            if (res != null)
            {
                //System.out.println(str + "Found in " + res);
                //System.out.flush();
                return res;
            }
            //str = str.substring(0, str.length()-1);
        }
        return candidate;
    }
    
    private void addDefaultConnectionImplementations()
    {
        addConnectionImplementation("com.jaspersoft.ireport.designer.connection.JDBCConnection");
        addConnectionImplementation("com.jaspersoft.ireport.designer.connection.JDBCNBConnection");
        addConnectionImplementation("com.jaspersoft.ireport.designer.connection.JRXMLDataSourceConnection");
        addConnectionImplementation("com.jaspersoft.ireport.designer.connection.JavaBeanDataSourceConnection");
        addConnectionImplementation("com.jaspersoft.ireport.designer.connection.JRCSVDataSourceConnection");
        addConnectionImplementation("com.jaspersoft.ireport.designer.connection.JRDataSourceProviderConnection");
        addConnectionImplementation("com.jaspersoft.ireport.designer.connection.JRCustomDataSourceConnection");
        addConnectionImplementation("com.jaspersoft.ireport.designer.connection.JREmptyDatasourceConnection");
        addConnectionImplementation("com.jaspersoft.ireport.designer.connection.JRHibernateConnection");
        addConnectionImplementation("com.jaspersoft.ireport.designer.connection.JRSpringLoadedHibernateConnection");
        addConnectionImplementation("com.jaspersoft.ireport.designer.connection.EJBQLConnection");
        addConnectionImplementation("com.jaspersoft.ireport.designer.connection.JRXMLADataSourceConnection");
        addConnectionImplementation("com.jaspersoft.ireport.designer.connection.MondrianConnection");
        addConnectionImplementation("com.jaspersoft.ireport.designer.connection.QueryExecuterConnection");
        addConnectionImplementation("com.jaspersoft.ireport.designer.connection.JRXlsDataSourceConnection");
        addConnectionImplementation("com.jaspersoft.ireport.designer.connection.JRXlsxDataSourceConnection");
    }
    
    /**
     *  Shortcut for getPreferences().get(key, def);
     */
    public String getProperty(String key, String def)
    {
        return getPreferences().get(key, def);
    }
    
    /**
     *  Shortcut for getPreferences().get(key, null);
     */
    public String getProperty(String key)
    {
        return getPreferences().get(key, null);
    }

    /**
     *  Return the user defined set of additional paths for the classpath.
     */
    public String getCurrentDirectory() {

        String s = getPreferences().get(CURRENT_DIRECTORY, System.getProperty("user.dir"));

        File f = new File(s);
        if (f.exists())
        {
            return s;
        }

        return System.getProperty("user.dir");
    }
    
    /**
     *  Set the path to use as current directory when opem a file/directory dialog box.
     */
    public void setCurrentDirectory(String currentDirectory) {
        setCurrentDirectory(currentDirectory, true);
    }
    
    /**
     *  Set the path to use as current directory when opem a file/directory dialog box.
     */
    public void setCurrentDirectory(String currentDirectory, boolean save) {
        setCurrentDirectory( new File(currentDirectory), save);
    }
    
    /**
     *  Set the current directory.
     *  If save = true, the config file is updated...
     */
    public void setCurrentDirectory( File f, boolean save) {
        String currentDirectory = "";
        if( f == null || !f.exists() ) return;

        try {
            if( f.isDirectory() ) {
                currentDirectory = f.getAbsolutePath();
            } else {
                currentDirectory = f.getParentFile().getAbsolutePath();
            }

            if (save) {
                getPreferences().put(CURRENT_DIRECTORY, currentDirectory);
            }
        } catch (Exception ex) {
        }
    }
    
    /**
     * Returns the Preferences object associated with iReport
     */
    public static Preferences getPreferences()
    {
        return NbPreferences.forModule(IReportManager.class);
    }
    
    /**
     *  Return the user defined set of additional paths for the classpath.
     */
    public List<String> getClasspath() {
        return getClasspath(getPreferences().get(IREPORT_CLASSPATH, ""));
    }

    public List<String> getHiddenClasspath() {
        return getClasspath(getPreferences().get(IREPORT_HIDDEN_CLASSPATH, ""));
    }

    private List<String> getClasspath(String cpString) {
        ArrayList<String> cp = new ArrayList<String>();
        if (cpString == null) return cp;

        String[] paths = cpString.split(";");
        for (int idx = 0; idx < paths.length; idx++) {
            cp.add(paths[idx]);
        }
        return cp;
    }

    /**
     *  Return the user defined set of additional paths for the classpath.
     */
    public List<String> getRelodableClasspath() {
        return getClasspath(getPreferences().get(IREPORT_RELODABLE_CLASSPATH, ""));
    }


    private void setClasspath(List<String> cp, String classpathType) {
        String classpathString = "";
        for (String path : cp)
        {
            classpathString += path + ";";
        }
        getPreferences().put(classpathType, classpathString);
    }

    /**
     *  Save the new classpath
     */
    public void setClasspath(List<String> cp) {
        setClasspath(cp, IREPORT_CLASSPATH);
        getReportClassLoader(true); // This recreates the classloader with the updated paths.
    }

    /**
     *  Save the new classpath
     */
    public void setHiddenClasspath(List<String> cp, boolean refreshClassloader) {
        setClasspath(cp, IREPORT_HIDDEN_CLASSPATH);
    }

    public void setRelodableClasspath(List<String> cp) {
        setClasspath(cp, IREPORT_RELODABLE_CLASSPATH);
    }

    public void updateConnection(int i, IReportConnection con) {
        getConnections().set(i, con);
        propertyChangeSupport.firePropertyChange(PROPERTY_CONNECTIONS, null, getConnections());
    }
    
    
    /** 
     * Return the list of QueryExecuterDef in memory.
     * When the list is initialized, it is fileld with the following predefined QueryExecuterDef languages:
     * sql, SQL, xPath, XPath, hql, HQL, mdx, MDX, ejbql, EJBQL, xmla-mdx
     */
    public ArrayList<QueryExecuterDef> getQueryExecuters() {
        
        if (queryExecuters == null)
        {
            queryExecuters = new java.util.ArrayList<QueryExecuterDef>();
            
            reloadQueryExecuters();
            
        }
        return queryExecuters;
    }
    
    /** 
     * This method provides the preferred way to register a new connection type
     * Return false if a custom QueryExecuter is already defined for the specified 
     * language and overrideSameLanguage is false.
     *
     * if qed is null, the method returns false.
     */
    public boolean addQueryExecuterDef(QueryExecuterDef qed, boolean overrideSameLanguage)
    {
        if (qed == null) return false;
        // Look for another qed with the same language...
        boolean found = false; // Override the QE at that position...
        
        for (int i=0; i<getQueryExecuters().size(); ++i)
        {
            QueryExecuterDef tqe = getQueryExecuters().get(i);
            if (tqe == null)
            {
                Misc.log("Error: null query executer");
                continue;
            }
            if (tqe.getLanguage() == null)
            {
                Misc.log("Error: null query language of executer " + tqe);
                continue;
            }
            if (tqe.getLanguage().equals( qed.getLanguage()) )
            {
                if (overrideSameLanguage)
                {
                   getQueryExecuters().set(i, qed);
                   found = true;
                   break;
                }
                else
                {
                    return false;
                }
            }
        }
        
        if (!found)
        {
            getQueryExecuters().add( qed );
        }
        
        // register the QE in the JasperServer properties...
        setJRProperty("net.sf.jasperreports.query.executer.factory." + qed.getLanguage(), qed.getClassName());
            
        
        return true;
    }
    
    public JasperDesign getActiveReport()
    {
        try {
            return OutlineTopComponent.getDefault().getCurrentJrxmlVisualView().getReportDesignerPanel().getJasperDesign();
        } catch (Exception ex)
        {
            return null;
        }
    }
    
    
    /**
     * Return the list of available fonts (AWT).
     **/
    public List<IRFont> getIRFonts()
    {
        if (fonts == null)
        {
            fonts = TTFFontsLoader.loadTTFFonts();
        }
        
        return fonts;
    }
    
    /**
     *  Return the user defined set of additional paths for the classpath.
     */
    public List<String> getFontpath() {
        ArrayList<String> cp = new ArrayList<String>();
        String[] paths = getPreferences().get(IREPORT_FONTPATH, "").split(";");
        for (int idx = 0; idx < paths.length; idx++) {
            cp.add(paths[idx]);
        }
        return cp;
    }
    
    /**
     *  Save the new classpath
     */
    public void setFontpath(List<String> cp) {
        String fontpathString = "";
        for (String path : cp)
        {
            fontpathString += path + ";";
        }
        getPreferences().put(IREPORT_FONTPATH, fontpathString);
        
        fonts = null; // Force a rescan when needed.
    }
    
    /**
     *  Shortcut for OutlineTopComponent.getDefault().getCurrentJrxmlVisualView();
     *  Easy way to get the current visual view, assuming it is visible and
     *  correctly registered in the OutlineTopComponent.
     **/
    public JrxmlVisualView getActiveVisualView()
    {
        return OutlineTopComponent.getDefault().getCurrentJrxmlVisualView();
    }
    
    /**
     *  Shortcut for getActiveVisualView().getEditorSupport().notifyModified();
     *  Easy way to say the current report was modified.
     **/
    public boolean notifyReportChange()
    {
        if (getActiveVisualView() != null)
        {
            return getActiveVisualView().getEditorSupport().notifyModified();
        }
        return false;
    }
    
    
     /**
     * Copy of a dataset
     */
    public JRDesignChartDataset getChartDatasetClipBoard() {
        return chartDatasetClipBoard;
    }
    
    /**
     * Set a JRDesignChartDataset to be used with another chart.
     */
    public void setChartDatasetClipBoard(JRDesignChartDataset chartDatasetClipBoard) {
        this.chartDatasetClipBoard = chartDatasetClipBoard;
    }
    
    /**
     * Place to store a list of series to be used with a dataset.
     */
    public java.util.List getChartSeriesClipBoard() {
        return chartSeriesClipBoard;
    }
    
    /**
     * Place to store a list of series to be used with a dataset.
     */
    public void setChartSeriesClipBoard(java.util.List list) {
        chartSeriesClipBoard = list;
    }
    
    
    /** This is a trick to aggregate undo operations done on a set of nodes...
     * We try to add all to the last undo op if it was created in the last
     * 100 milliseconds...
     */
    public void addUndoableEdit(UndoableEdit edit)
    {
        addUndoableEdit(edit, false);
    }
    
    public void setForceAggregateUndo(boolean b)
    {
        forceAggregateUndo = b;
    }
    
    public void addUndoableEdit(UndoableEdit edit, boolean aggregate)
    {
        notifyReportChange();
        if (aggregate || forceAggregateUndo)
        {
            if (lastUndoableEdit != null &&
                 lastUndoableEdit instanceof AggregatedUndoableEdit &&
                 System.currentTimeMillis() - lastUndoableEditTime < 100)
             {
                 ((AggregatedUndoableEdit)lastUndoableEdit).concatenate(edit);
                 lastUndoableEditTime = System.currentTimeMillis();
                 return;
             }
        }
        lastUndoableEditTime = System.currentTimeMillis();
        lastUndoableEdit = edit;
        if (getActiveVisualView() != null)
        {
            getActiveVisualView().getUndoRedoManager().undoableEditHappened(new UndoableEditEvent(this, edit));
        }
        else
        {
            // Check if the current lookup contains an undo manager...
            UndoRedo.Manager manager = Lookup.getDefault().lookup(UndoRedo.Manager.class);
            if (manager != null)
            {
                manager.undoableEditHappened(new UndoableEditEvent(this, edit));
            }
        }
    }
    
    public UndoableEdit getLastUndoableEdit()
    {
        return lastUndoableEdit;
    } 
    
    /**
     * Return the last value specified by the user for this parameter when prompted...
     * 
     * @param p
     * @return
     */
    public Object getLastParameterValue(JRParameter p)
    {
          Object o = parameterValues.get(p.getName() + " " + p.getValueClassName());
          return o;

          // There is no reason here to make type checking.
          // If there is a previous stored value, it's because iReport set it.
          // If the value is not congruent with the actual parameter type,
          // it's catched by the key used with the parameter.
          //if (p.getValueClass().isInstance(o))
          //{
          //    return o;
          //}
          //return null;
    }
    
    /**
     * set the last value specified by the user for this parameter when prompted...
     * 
     * @param p
     * @return
     */
    public void setLastParameterValue(JRParameter p, Object value)
    {
          if (p == null || value == null || p.getValueClass() == null) return;
          parameterValues.put(p.getName() + " " + p.getValueClassName(), value);
    }

    public java.util.List<FileResolver> getFileResolvers() {
        
        if (fileResolvers == null)
        {
            fileResolvers = new ArrayList<FileResolver>();
        }
        return fileResolvers;
    }

    public void setFileResolvers(java.util.List<FileResolver> fileResolvers) {
        this.fileResolvers = fileResolvers;
    }
    
    public final void addJrxmlVisualViewActivatedListener(JrxmlVisualViewActivatedListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }
    public final void removeJrxmlVisualViewActivatedListener(JrxmlVisualViewActivatedListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }
    public final void fireJrxmlVisualViewActivatedListenerEvent(JrxmlVisualView view) {
        Iterator<JrxmlVisualViewActivatedListener> it;
        synchronized (listeners) {
            it = new HashSet<JrxmlVisualViewActivatedListener>(listeners).iterator();
        }
    
        while (it.hasNext()) {
            it.next().jrxmlVisualViewActivated(view);
        }
    }


    public final void addJasperDesignActivatedListener(JasperDesignActivatedListener l) {
        synchronized (jasperDesignActivatedListeners) {
            jasperDesignActivatedListeners.add(l);
        }
    }

    public final void removeJasperDesignActivatedListener(JasperDesignActivatedListener l) {
        synchronized (jasperDesignActivatedListeners) {
            jasperDesignActivatedListeners.remove(l);
        }
    }

    public final void fireJasperDesignActivatedListenerEvent(JasperDesign jd) {
        Iterator<JasperDesignActivatedListener> it;
        synchronized (jasperDesignActivatedListeners) {
            it = new HashSet<JasperDesignActivatedListener>(jasperDesignActivatedListeners).iterator();
        }

        while (it.hasNext()) {
            it.next().jasperDesignActivated(jd);
        }
    }


    public void createPaletteItem()
    {

        //PaletteItem item = CreatePageNumberTextfieldAction.createPaletteItem();
    }

    /**
     * @return the customLinkTypes
     */
    public List<Tag> getCustomLinkTypes() {
        return customLinkTypes;
    }

    /**
     * @param customLinkTypes the customLinkTypes to set
     */
    public void setCustomLinkTypes(List<Tag> customLinkTypes) {
        this.customLinkTypes = customLinkTypes;
    }

    public void addCustomLinkType(String value, String desc)
    {
        customLinkTypes.add(new Tag(value, desc));
    }

    public boolean isBackgroundSeparated()
    {
        return getPreferences().getBoolean("ShowBackgroundAsSeparatedDocument",true);
    }

    /**
     * @return the iReportConnectionFactories
     */
    public final java.util.List<IReportConnectionFactory> getIReportConnectionFactories() {
        if (iReportConnectionFactories == null)
        {
            iReportConnectionFactories = new ArrayList<IReportConnectionFactory>();
            addDefaultConnectionImplementations();
        }
        return iReportConnectionFactories;
    }


    /**
     * @return the exporterFactories
     */
    public java.util.List<ExporterFactory> getExporterFactories() {
        if (exporterFactories == null)
        {
            exporterFactories = new ArrayList<ExporterFactory>();
            exporterFactories.add(new DefaultExporterFactory("pdf"));
            exporterFactories.add(new DefaultExporterFactory("csv"));
            exporterFactories.add(new DefaultExporterFactory("html"));
            exporterFactories.add(new DefaultExporterFactory("xhtml"));
            exporterFactories.add(new DefaultExporterFactory("layered_html"));
            exporterFactories.add(new DefaultExporterFactory("xls"));
            exporterFactories.add(new DefaultExporterFactory("xls2"));
            exporterFactories.add(new DefaultExporterFactory("xlsx"));
            exporterFactories.add(new DefaultExporterFactory("xls3"));
            exporterFactories.add(new DefaultExporterFactory("java2D"));
            exporterFactories.add(new DefaultExporterFactory("txt"));
            exporterFactories.add(new DefaultExporterFactory("rtf"));
            exporterFactories.add(new DefaultExporterFactory("odt"));
            exporterFactories.add(new DefaultExporterFactory("ods"));
            exporterFactories.add(new DefaultExporterFactory("docx"));
            exporterFactories.add(new DefaultExporterFactory("pptx"));
            exporterFactories.add(new DefaultExporterFactory("xml"));
        }
        return exporterFactories;
    }

//    /**
//     * @return the defaultJasperReportsProperties
//     */
//    public Properties getDefaultJasperReportsProperties() {
//        if (defaultJasperReportsProperties == null)
//        {
//            defaultJasperReportsProperties = new Properties();
//        }
//        return defaultJasperReportsProperties;
//    }
//
//    /**
//     * @param defaultJasperReportsProperties the defaultJasperReportsProperties to set
//     */
//    public void setDefaultJasperReportsProperties(Properties defaultJasperReportsProperties) {
//        this.defaultJasperReportsProperties = defaultJasperReportsProperties;
//    }

    public void addPathToClasspath(String path, boolean reloadable)
    {
        List<String> classpath = (reloadable) ? IReportManager.getInstance().getRelodableClasspath() : IReportManager.getInstance().getClasspath();

        if (classpath.contains(path)) return;

        classpath.add(path);

        if (reloadable) IReportManager.getInstance().setRelodableClasspath(classpath);
        else IReportManager.getInstance().setClasspath(classpath);

    }

    /**
     * @return the noNetwork
     */
    public boolean isNoNetwork() {
        return noNetwork;
    }

    /**
     * @param noNetwork the noNetwork to set
     */
    public void setNoNetwork(boolean noNetwork) {
        this.noNetwork = noNetwork;
    }

}
