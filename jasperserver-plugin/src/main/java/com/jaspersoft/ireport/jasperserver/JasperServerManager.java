/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2013 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.ireport.jasperserver;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.compiler.IReportCompiler;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.Action;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataFolder;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author gtoffoli
 */
    public class JasperServerManager {

        
        public static final int MAX_ID_LENGHT = 100;
        public static final int MAX_NAME_LENGHT = 100;
        
        
    private List<FileResourceUpdatingListener> resourceReplacingListeners = new ArrayList<FileResourceUpdatingListener>();

    public void addFileResourceUpdatingListener(FileResourceUpdatingListener listener)
    {
        resourceReplacingListeners.add(listener);
    }

    public void removeFileResourceUpdatingListener(FileResourceUpdatingListener listener)
    {
        resourceReplacingListeners.remove(listener);
    }
    

    private static JasperServerManager mainInstance = null;
    private static JrxmlNotifier jrxmlNotifier = null;

    public static JrxmlNotifier getJrxmlNotifier()
    {
        return jrxmlNotifier;
    }

    public static JasperServerManager getMainInstance() {
        if (mainInstance == null)
        {
            mainInstance = new JasperServerManager();
        }
        return mainInstance;
    }
    
    public static String REQUIRED_VERSION = ""; 
    public static String CURRENT_VERSION = "";
    public static final String PROPERTY_CHECK_FOR_UPDATE = "PROPERTY_CHECK_FOR_UPDATE";
    public static final String PROPERTY_USE_PROXY = "PROPERTY_USE_PROXY";
    public static final String PROPERTY_PROXY_URL = "PROPERTY_PROXY_URL";
    public static final String PROPERTY_PROXY_USE_AUTH = "PROPERTY_PROXY_USE_AUTH";
    public static final String PROPERTY_PROXY_USERNAME = "PROPERTY_PROXY_USERNAME";
    public static final String PROPERTY_PROXY_PASSWORD = "PROPERTY_PROXY_PASSWORD";
    
    /** This is the complete list of servers available on the tree.
     *  After an add or a remove, please save the configuration and update
     *  the repositoryExplorer.
     */
    private java.util.List jServers = new java.util.ArrayList();
    
    /**
     * Properties are stored in the user home directory under .ireport/jasperserverplugin.xml
     */
    private java.util.Properties properties = new java.util.Properties();
    /**
     * Properties are taken from /com/jaspersoft/ireport/jasperserver/default.jasperserver_irplugin.properties
     * Properties can be overridden with a file called:
     * /com/jaspersoft/ireport/jasperserver/irplugin.properties.
     * if it is not found, it will use:
     * jasperserver_irplugin.properties.
     */
    private java.util.Properties brandingProperties = new java.util.Properties();


    /**
     * This map helps to track where a jrxml comes from...
     *
     */
    private java.util.HashMap<String, RepositoryReportUnit> jrxmlReportUnitMap= new java.util.HashMap<String, RepositoryReportUnit>();

    /**
     * This map is used to register JSDataProviders
     *
     */
    private java.util.HashMap<String, JSDataProvider> dataProvidersMap= new java.util.HashMap<String, JSDataProvider>();

    private static java.util.ResourceBundle oLanguage = null;
    private static Locale pluginLocale = null;
    
    
    private JasperServerManager() {
        loadConfiguration();
        applyProxySettings();
        
        pluginLocale = Locale.getDefault();

        ActiveEditorTopComponentListener.getDefaultInstance().startListening();
        jrxmlNotifier = new JrxmlNotifier();

        this.addFileResourceUpdatingListener(jrxmlNotifier);
        IReportCompiler.addCompileListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final IReportCompiler compiler = (IReportCompiler)e.getSource();

                if (e.getID() == IReportCompiler.CL_COMPILE_OK)
                {
                    JasperDesign jd = compiler.getJrxmlVisualView().getEditorSupport().getCurrentModel();

                    System.out.println("Setting up the file resolver..." );
                    if (jd.getProperty("ireport.jasperserver.url") != null)
                    {
                        // find the server required to run this report...
                        List<JServer> servers = getJServers();
                        for (JServer server : servers)
                        {
                            if (server.getUrl().equals(jd.getProperty("ireport.jasperserver.url")))
                            {
                                // add to the list the jasper file directory and the current directory...
                                File jasperDir = FileUtil.toFile( compiler.getFile().getParent() );
                                if (!IReportManager.getPreferences().getBoolean("useReportDirectoryToCompile", true))
                                {
                                   jasperDir = new File(IReportManager.getPreferences().get("reportDirectoryToCompile", "."));
                                }
                                List<File> resolverPath = new ArrayList<File>();
                                if (jasperDir.exists())
                                {
                                    resolverPath.add(jasperDir);
                                }
                                resolverPath.add(new File("."));
                                
                                JSFileResolver resolver = new JSFileResolver(resolverPath, server, jd);
                                if (resolver != null)
                                {
                                    compiler.getAdditionalParameters().put("REPORT_FILE_RESOLVER", resolver);
                                    System.out.println("Added file resolver..." );
                                }
                            }
                        }
                    }
                }
            }
        });
    }
    
    
    
    
    
    public void applyProxySettings()
    {
        java.util.Properties props = getProperties();
        
        System.getProperties().remove("proxySet");
        System.getProperties().remove("proxyHost");
        System.getProperties().remove("proxyPort");
        System.getProperties().remove("http.proxyHost");
        System.getProperties().remove("http.proxyPort");
        System.getProperties().remove("http.proxyUser");
        System.getProperties().remove("http.proxyPassword");
        System.getProperties().remove("https.proxyHost");
        System.getProperties().remove("https.proxyPort");
              
        if (props.getProperty(JasperServerManager.PROPERTY_USE_PROXY, "false").equals("true"))
        {
            //System.getProperties().put( "proxySet", "true" );

            String urlProxy = props.getProperty(JasperServerManager.PROPERTY_PROXY_URL, "");
            String port = "8080";
            String server = urlProxy;
            if (urlProxy.indexOf(":") > 0)
            {
                port = urlProxy.substring(urlProxy.indexOf(":") + 1);
                server = urlProxy.substring(0, urlProxy.indexOf(":"));
            }

            System.getProperties().put( "http.proxyHost", server );
            System.getProperties().put( "http.proxyPort", port );
            //System.getProperties().put( "proxyHost", server );
            //System.getProperties().put( "proxyPort", port );
            
            if (props.getProperty(JasperServerManager.PROPERTY_PROXY_USE_AUTH, "false").equals("true"))
            {
                String userName = props.getProperty(JasperServerManager.PROPERTY_PROXY_USERNAME, "");
                String userPass = props.getProperty(JasperServerManager.PROPERTY_PROXY_PASSWORD, "");
                System.getProperties().put( "http.proxyUser", userName );
                System.getProperties().put( "http.proxyPassword", userPass );
            }
        }
    }
    
    
    
    /**
     * Load the plugin configuration from IREPORT_USER_HOME_DIR/jasperserverplugin.xml
     */
    public void loadConfiguration()
    {
        try {

            setProperties(new java.util.Properties());

            Preferences prefs = NbPreferences.forModule(JasperServerManager.class);

            String[] keys = prefs.keys();

            java.util.Properties props = getProperties();
            for (int i = 0; i < keys.length; ++i) {
                props.put(keys[i], prefs.get(keys[i], null));
            }

            for (int i = 0; props.getProperty("server." + i + ".name") != null; ++i) {
                JServer server = new JServer();
                server.setName(props.getProperty("server." + i + ".name"));
                server.setUrl(props.getProperty("server." + i + ".url"));
                server.setUsername(props.getProperty("server." + i + ".username"));
                String pwd = null;
                if (props.getProperty("server." + i + ".password.enc") != null) {
                    Encrypter enc = new Encrypter(getBrandingProperties().getProperty("irplugin.encrypt.passwords.key", "54fj245vn3vfdsmce4mg0jvs"));
                    pwd = enc.decrypt(props.getProperty("server." + i + ".password.enc"));
                } else {
                    pwd = props.getProperty("server." + i + ".password");
                }
                server.setPassword(pwd);
                server.setLocale(Locale.getDefault().toString());
                getJServers().add(server);
            }
            addProperties("/com/jaspersoft/ireport/jasperserver/default.jasperserver_irplugin.properties", getBrandingProperties());
            addProperties("/com/jaspersoft/ireport/jasperserver/jasperserver_irplugin.properties", getBrandingProperties());
            //java.lang.System.out.println(getBrandingProperties() + "");
            REQUIRED_VERSION = getBrandingProperties().getProperty("irplugin.server.required.version");
            CURRENT_VERSION = getBrandingProperties().getProperty("irplugin.version");

            //  try {
            //         Method m = it.businesslogic.ireport.gui.MainFrame.class.getMethod("addLinkType", new Class[]{java.lang.String.class});
            //         m.invoke( it.businesslogic.ireport.gui.MainFrame.getMainInstance(), new Object[]{ "ReportExecution" });
            // } catch (NoSuchMethodException ex) {
            // } catch (IllegalAccessException ex) {
            // } catch (InvocationTargetException ex) {
            // } catch (SecurityException ex) {
            // }
        } catch (BackingStoreException ex) {
            Exceptions.printStackTrace(ex);
        }
            
           //  try {
           //         Method m = it.businesslogic.ireport.gui.MainFrame.class.getMethod("addLinkType", new Class[]{java.lang.String.class});
           //         m.invoke( it.businesslogic.ireport.gui.MainFrame.getMainInstance(), new Object[]{ "ReportExecution" });
           // } catch (NoSuchMethodException ex) {
           // } catch (IllegalAccessException ex) {
           // } catch (InvocationTargetException ex) {
           // } catch (SecurityException ex) {
           // }
    }
    
    private void addProperties(String resourceUri, java.util.Properties props)
    {
        try {
            
            InputStream is = this.getClass().getResourceAsStream(resourceUri);
            if (is == null) return;
            props.load( is );
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }      
    }
    
    /**
     * Save the plugin configuration in IREPORT_USER_HOME_DIR/jasperserverplugin.xml
     */
    public boolean saveConfiguration()
    {
        Preferences prefs = NbPreferences.forModule(JasperServerManager.class);
        try {
            prefs.clear();
        } catch (BackingStoreException ex) {
            Exceptions.printStackTrace(ex);
        }
            
        // 1. remove all server.X tags...
        // 2. add all server configurations...
        for (int i = 0; i < getJServers().size(); ++i) {
            JServer server = (JServer) getJServers().get(i);
            prefs.put("server." + i + ".name", server.getName());
            prefs.put("server." + i + ".url", server.getUrl());
            prefs.put("server." + i + ".username", server.getUsername());

            if (getBrandingProperties().getProperty("irplugin.encrypt.passwords", "true").equals("true")) {
                Encrypter enc = new Encrypter(getBrandingProperties().getProperty("irplugin.encrypt.passwords.key", "54fj245vn3vfdsmce4mg0jvs"));
                prefs.put("server." + i + ".password.enc", enc.encrypt(server.getPassword()));
            } else {
                prefs.put("server." + i + ".password", server.getPassword());
            }
        }

        return true;
    }

    public static Preferences getPreferences()
    {
        return NbPreferences.forModule(JasperServerManager.class);
    }

    /**
     * Get Plugin properties
     */
    public java.util.Properties getProperties() {
        return properties;
    }

    /**
     * Set Plugin properties (this nethod should be never used....
     */
    public void setProperties(java.util.Properties properties) {
        this.properties = properties;
    }

    public java.util.List getJServers() {
        return jServers;
    }

    public void setJServers(java.util.List jServers) {
        this.jServers = jServers;
    }
    
    /**
     * Create a tmp file name. A complete path name is returned.
     * The location of the file is the "jstmp" directory inside
     * the IREPORT_USER_HOME_DIR
     * (i.e. C:\Documents and Settings\gtoffoli\.ireport\jstmp\...)
     * If this directory does not exist, it is created.
     *  
     * filePrefix (can be null)
     * fileExtension (can be null, default ".tmp")
     *
     * The caller is responsable for delation of this files.
     * No check if the file already exists is performed.
     */
    public static String createTmpFileName(String filePrefix, String fileExtension)
    {
            if (filePrefix == null) filePrefix = "";
            else filePrefix += "_";
            if (fileExtension != null && !fileExtension.startsWith("."))
            {
                fileExtension = "." + fileExtension;
            }
            
            if (fileExtension == null) fileExtension = ".tmp";
            
            String tmpDirectory = System.getProperty("java.io.tmpdir");
            tmpDirectory += File.separator + "jstmp";
            
            File tmpDirectoryFile = new File(tmpDirectory);
            if (!tmpDirectoryFile.exists())
            {
                tmpDirectoryFile.mkdirs();
            }
            
            return tmpDirectoryFile + File.separator + filePrefix + (new java.util.Date()).getTime() + fileExtension;
            
    }
    
    
    public java.util.Properties getBrandingProperties() {
        return brandingProperties;
    }

    public void setBrandingProperties(java.util.Properties brandingProperties) {
        this.brandingProperties = brandingProperties;
    }
    
    /**
     *  This method validates URL like:
     *
     *  123.123.123[:port]
     *  domain.domain.dom[:port]
     *
     */
    public static boolean isValidUrl(String url) 
    { 
        String strRegex = 
         "((([0-9]{1,3}\\.){3})[0-9]{1,3})" + // IP- 199.194.52.184 
         "|" + // allows either IP or domain 
         "(([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\.?)++" + // domain pice 
         "(:[0-9]{1,4})?"; // port number- :80 
         
         return url.matches( strRegex );
    } 
    
    /********************** I18n utilities.... *******************/
    public static String getFormattedString(String cID, String defaultValue, Object[] args)
    {
        String pattern = getString(cID, defaultValue );
        java.text.MessageFormat mf = new java.text.MessageFormat(pattern, Locale.getDefault());
        return mf.format(args);
    }
    
    
    
    public static String getString(String cID, String defaultValue)
    {
        try
        {
            String s = NbBundle.getMessage(JasperServerManager.class, cID);
            if (s != null && s.startsWith("${") && s.endsWith("}") )
            {
                String newKey = s.substring(2, s.length()-1);
                if (newKey.length() > 0)
                {
                    return getString(newKey, defaultValue);
                }
            }
            
            return s;
        }
        catch (MissingResourceException ex)
        {
            System.out.println("JS Explorer: Can't find the translation for key = " + cID +": using default (" + defaultValue + ")");
        }
        catch (Exception ex)
        {
            System.out.println("JS Explorer: Exception loading cID = " + cID +": " + ex.getMessage());
        }
        return defaultValue;
    }
    
    
    /**
     * Returns the languages configured in the BrandingProperties
     */
    public List getSupportedQueryLanguages()
    {
        Iterator iter = getBrandingProperties().keySet().iterator();
        java.util.ArrayList list = new java.util.ArrayList();
        
        while (iter.hasNext())
        {
            String key = ""+iter.next();
            if (key.startsWith("query.language.") && !key.endsWith(".enabled"))
            {
                String language = key.substring(15);
                if ( getBrandingProperties().getProperty(key + ".enabled","true").equals("true") )
                {
                    list.add( new Tag(language, getBrandingProperties().getProperty(key) ) );
                }
            }
        }
        
        // Sorting list...
        Object[] tobeordered = list.toArray();
        Arrays.sort(tobeordered, new Comparator() {
            public int compare(Object o1, Object o2) {
            
               return ((Tag)o1).getName().compareTo( ((Tag)o2).getName() );
            }
            public boolean equals(Object obj) {
               return (obj != null && this.equals(obj) );
            }
        } );
        
        list.clear();
        for (int i=0; i<tobeordered.length; ++i) list.add(tobeordered[i]);
        
        return list;
    }

    /**
     * @return the jrxmlReportUnitMap
     */
    public java.util.HashMap<String, RepositoryReportUnit> getJrxmlReportUnitMap() {
        return jrxmlReportUnitMap;
    }

    /**
     * @param jrxmlReportUnitMap the jrxmlReportUnitMap to set
     */
    public void setJrxmlReportUnitMap(java.util.HashMap<String, RepositoryReportUnit> jrxmlReportUnitMap) {
        this.jrxmlReportUnitMap = jrxmlReportUnitMap;
    }

    /**
     * @return the dataProvidersMap
     */
    public java.util.HashMap<String, JSDataProvider> getDataProvidersMap() {
        return dataProvidersMap;
    }

    /**
     * @param dataProvidersMap the dataProvidersMap to set
     */
    public void setDataProvidersMap(java.util.HashMap<String, JSDataProvider> dataProvidersMap) {
        this.dataProvidersMap = dataProvidersMap;
    }

    public void fireResourceReplacing_resourceWillBeUpdated(RepositoryFile repositoryFile, RepositoryReportUnit reportUnit,File file) throws Exception
    {
        for (FileResourceUpdatingListener listener :  resourceReplacingListeners)
        {
            listener.resourceWillBeUpdated(repositoryFile, reportUnit, file);
        }
    }

    public void fireResourceReplacing_resourceUpdated(RepositoryFile repositoryFile, RepositoryReportUnit reportUnit,File file) throws Exception
    {
        for (FileResourceUpdatingListener listener :  resourceReplacingListeners)
        {
            listener.resourceUpdated(repositoryFile, reportUnit, file);
        }
    }
    
    
    
    /**
     * Look into the virtual file system for element decorators...
     *
     * @param element
     * @return
     */
    public static List<Action> getContributedMenuActionsFor(ResourceDescriptor rd) {
        
        List<Action> actions = new ArrayList<Action>();
        
        FileObject contributorsFileObject = Repository.getDefault().getDefaultFileSystem().getRoot().getFileObject("ireport/server/menucontributors");
        if (contributorsFileObject == null) return actions;
        DataFolder contributorsDataFolder = DataFolder.findFolder(contributorsFileObject);
        if (contributorsDataFolder == null) return actions;

        List<String> list = new ArrayList<String>();

        Collection<? extends ResourceMenuContributor> resourceMenuContributorsInstances = Lookups.forPath("ireport/server/menucontributors").lookupAll(ResourceMenuContributor.class);
        Iterator<? extends ResourceMenuContributor> it = resourceMenuContributorsInstances.iterator();
        while (it.hasNext ()) {

            ResourceMenuContributor resourceMenuContributor = it.next();
            List<Action> tActions = resourceMenuContributor.contributeMenuFor(rd);
            
            if (tActions != null)
            {
                actions.addAll(tActions);
            }
        }
        
        return actions;
    }
    
    
    
    /**
     * Look into the virtual file system for element decorators...
     *
     * @param element
     * @return
     */
    public static ResourceHandler getResourceHandler(ResourceDescriptor rd) {
        
        ResourceHandler handler = null;
        
        FileObject contributorsFileObject = Repository.getDefault().getDefaultFileSystem().getRoot().getFileObject("ireport/server/resourcehandlers");
        if (contributorsFileObject == null) return null;
        DataFolder contributorsDataFolder = DataFolder.findFolder(contributorsFileObject);
        if (contributorsDataFolder == null) return null;

        List<String> list = new ArrayList<String>();

        Collection<? extends ResourceHandler> resourceHanlders = Lookups.forPath("ireport/server/resourcehandlers").lookupAll(ResourceHandler.class);
        Iterator<? extends ResourceHandler> it = resourceHanlders.iterator();
        while (it.hasNext ()) {

            ResourceHandler resourceHanlder = it.next();
            if (resourceHanlder.supportsResourceType(rd))
            {
                return resourceHanlder;
            }
        }
        
        return null;
    }
   
}
