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
package com.jaspersoft.ireport.jasperserver.ui.wizards;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.compatibility.JRXmlWriterHelper;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryJrxmlFile;
import com.jaspersoft.ireport.jasperserver.RepositoryReportUnit;
import com.jaspersoft.ireport.jasperserver.validation.JrxmlValidationDialog;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.awt.Component;
import java.awt.Dialog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;

/**
 *
 * @author gtoffoli
 */
public class ReportUnitWizardDescriptor extends WizardDescriptor {

    private JServer server = null;
    private String parentFolder = null;
    
    private ResourceDescriptor newResourceDescriptor = null;
    
    
    private WizardDescriptor.Panel[] panels;
    
    public ReportUnitWizardDescriptor()
    {
        super();
        setTitleFormat(new MessageFormat("{0}"));
        setTitle("ReportUnit Wizard");
    }
    
    public boolean runWizard()
    {
        setPanelsAndSettings(new WizardDescriptor.ArrayIterator(getPanels()), this);
        Dialog dialog = DialogDisplayer.getDefault().createDialog(this);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = this.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
            return createTheReportUnit();
        }
        return false;
    }

    public JServer getServer() {
        return server;
    }

    public void setServer(JServer server) {
        this.server = server;
    }

    public String getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(String parentFolder) {
        this.parentFolder = parentFolder;
    }
    
    
    private List datasources = new ArrayList();
    /**
     * Accept a list of Strings or ResourceDescriptor
     */
    public void setDatasources(List datasources)
    {
        this.datasources = datasources;
    }

    public ResourceDescriptor getNewResourceDescriptor() {
        return newResourceDescriptor;
    }

    public void setNewResourceDescriptor(ResourceDescriptor newResourceDescriptor) {
        this.newResourceDescriptor = newResourceDescriptor;
    }

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
        if (panels == null) {
            panels = new WizardDescriptor.Panel[]{
                new ReportUnitWizardPanel1(this),
                new ReportUnitWizardPanel2(this),
                new ReportUnitWizardPanel3(this)
            };
            String[] steps = new String[panels.length];
            for (int i = 0; i < panels.length; i++) {
                Component c = panels[i].getComponent();
                // Default step name to component name of panel. Mainly useful
                // for getting the name of the target chooser to appear in the
                // list of steps.
                steps[i] = c.getName();
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    // Sets step number of a component
                    jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
                    // Sets steps names for a panel
                    jc.putClientProperty("WizardPanel_contentData", steps);
                    // Turn on subtitle creation on each step
                    jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                    // Show steps on the left side with the image on the background
                    jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                    // Turn on numbering of all steps
                    jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
                }
            }
        }
        return panels;
    }

    public List getDatasources() {
        return datasources;
    }
    
    public boolean createTheReportUnit()
    {
            File resourceFile = null;
            ResourceDescriptor rd = new ResourceDescriptor();
            
            //Stored settings:
            java.util.Iterator namesProps = getProperties().keySet().iterator();
            while (namesProps.hasNext())
            {
                String name = ""+namesProps.next();
                System.out.println(name + " " + getProperty(name));
                System.out.flush();
            }
        
            try {
                    rd.setWsType( ResourceDescriptor.TYPE_REPORTUNIT );
                    rd.setDescription(  ((String)getProperty("description")).trim() ); //getResource().getDescriptor().getDescription()
                    rd.setName( (String)getProperty("name")  );
                    String uri = getParentFolder();
                    if (!uri.endsWith("/")) uri = uri + "/";
                    uri += getProperty("name");
                    rd.setUriString( uri );
                    rd.setLabel(((String)getProperty("label")).trim() ); //getResource().getDescriptor().getLabel()  );
                    rd.setParentFolder( getParentFolder() );
                    rd.setIsNew( true );
                    rd.setResourceProperty( ResourceDescriptor.PROP_RU_ALWAYS_PROPMT_CONTROLS, true);

                    // Add the datasource resource...
                    if (JasperServerManager.getMainInstance().getBrandingProperties().getProperty("ireport.manage.datasources.enabled", "true").equals("true"))
                    {
                        ResourceDescriptor tmpDataSourceDescriptor = null;
                        if (getProperty("datasource_present") != null &&
                            getProperty("datasource_present").equals("true"))
                        {
                            if (((String)getProperty("datasource_is_local")).equals("false"))
                            {
                                tmpDataSourceDescriptor = new ResourceDescriptor();
                                tmpDataSourceDescriptor.setWsType( ResourceDescriptor.TYPE_DATASOURCE );
                                tmpDataSourceDescriptor.setReferenceUri( (String)getProperty("datasource_uri"));
                                tmpDataSourceDescriptor.setIsReference(true);
                            }
                            else
                            {
                                tmpDataSourceDescriptor = (ResourceDescriptor)getProperty("datasource_descriptor");
                                tmpDataSourceDescriptor.setIsReference(false);
                            }
                            rd.getChildren().add( tmpDataSourceDescriptor );
                        }
                    }


                    // Add the jrxml resource...
                    ResourceDescriptor jrxmlDescriptor = new ResourceDescriptor();
                    jrxmlDescriptor.setWsType( ResourceDescriptor.TYPE_JRXML );

                    if (((String)getProperty("jrxml_is_local")).equals("false")) 
                     {
                        jrxmlDescriptor.setIsNew(true);
                        jrxmlDescriptor.setMainReport(true);
                        jrxmlDescriptor.setIsReference(true);
                        jrxmlDescriptor.setReferenceUri( (String)getProperty("jrxml_file") );
                    }
                    else
                    {
                            jrxmlDescriptor.setName( getProperty("name") + "_jrxml");
                            jrxmlDescriptor.setLabel("Main jrxml"); //getResource().getDescriptor().getLabel()  );
                            jrxmlDescriptor.setDescription("Main jrxml"); //getResource().getDescriptor().getDescription()
                            jrxmlDescriptor.setIsNew(true);
                            jrxmlDescriptor.setHasData(true);
                            jrxmlDescriptor.setMainReport(true);
                            resourceFile = new File( (String)getProperty("jrxml_file"));
                    }
                    rd.getChildren().add( jrxmlDescriptor );
                        
                    System.out.println("Resource descriptor uri: " + rd.getUriString());
                    System.out.flush();
                    
                    // This call should be not lock... we should put it in a new thread...
                    // and maybe add a window to show the progress...
                    newResourceDescriptor = getServer().getWSClient().addOrModifyResource(rd, resourceFile);
                    
                    System.out.println("resourceFile = " + resourceFile);
                    System.out.flush();


                    JasperServerManager.getJrxmlNotifier().resourceWillBeUpdated(null, null, resourceFile);


                    if (resourceFile != null)
                    {
                        addRequiredResources(resourceFile, newResourceDescriptor);
                    }
            } catch (java.lang.Exception ex) {
                JOptionPane.showMessageDialog(Misc.getMainFrame(),JasperServerManager.getFormattedString("messages.error.3", "Error:\n {0}", new Object[] {ex.getMessage()}));
                ex.printStackTrace();
                return false;
            }
            
            return true;
    }
    
    private void addRequiredResources(File resourceFile, ResourceDescriptor rd) throws java.lang.Exception {
        
        addRequiredResources(getServer(), resourceFile, rd);
    }


    /**
     * Update the resources for the main jrxml...
     * 
     * @param server
     * @param resourceFile
     * @param reportUnitDescriptor
     * @throws java.lang.Exception
     */
    public static void addRequiredResources(JServer server, File resourceFile, ResourceDescriptor reportUnitDescriptor) throws java.lang.Exception {
        addRequiredResources(server, resourceFile, null, reportUnitDescriptor, null);
    }


    /**
     * Update the resources for the jrxml file described in jrxmlDescriptor.
     * If jrxmlDescriptor is null, the main jrxml is updated.
     *
     * @param server
     * @param resourceFile
     * @param model If the model is provided, the resource file is updated without using a temporary file.
     * @param reportUnitDescriptor
     * @param jrxmlDescriptor
     * @throws java.lang.Exception
     */
    public static void addRequiredResources(JServer server, File resourceFile, JasperDesign report, ResourceDescriptor reportUnitDescriptor, ResourceDescriptor jrxmlDescriptor) throws java.lang.Exception {

        boolean useTemporaryFile = false; //report == null;

        JrxmlVisualView view = Misc.getViewForFile(resourceFile);
        
        Thread.currentThread().setContextClassLoader( IReportManager.getReportClassLoader());
        if (report == null)
        {
            if (view != null && !useTemporaryFile)
            {
                report = view.getEditorSupport().getCurrentModel();
            }
            else
            {
                report = JRXmlLoader.load(resourceFile);
            }
        }

        List children = RepositoryJrxmlFile.identifyElementValidationItems(report, reportUnitDescriptor, resourceFile.getParent());

        if (children.size() > 0)
        {
            
            // We will create a temporary file somewhere else...
            if (useTemporaryFile)
            {
                String tmpFileName = JasperServerManager.createTmpFileName("newfile",".jrxml");
                JRXmlWriter.writeReport(report, new java.io.FileOutputStream(tmpFileName), "UTF-8");
                resourceFile = new File(tmpFileName);
            }
            else
            {
                view = null;
            }

            long modified = resourceFile.lastModified();

            if (reportUnitDescriptor != null)
            {
                report.setProperty("ireport.jasperserver.reportUnit", reportUnitDescriptor.getUriString());
            }

            report.setProperty("ireport.jasperserver.url", server.getUrl() );
            JrxmlValidationDialog jvd = new JrxmlValidationDialog(Misc.getMainFrame(),true);
            jvd.setElementVelidationItems( children );
            jvd.setServer( server );
            jvd.setVisualView( view );
            jvd.setFileName(resourceFile.getPath());
            jvd.setReportUnit( new RepositoryReportUnit(server, reportUnitDescriptor) );
            jvd.setReport( report );
            jvd.setVisible(true);


            if (view != null)
            {
                view.getEditorSupport().saveDocument();
            }
            else
            {
                writeReport(report, resourceFile);
            }

            jvd.getDialogResult();

            // Save the report and store it....
            // Look for the main jrxml...
            if (modified != resourceFile.lastModified())
            {
                for (int i=0; i<reportUnitDescriptor.getChildren().size(); ++i)
                {
                    ResourceDescriptor rdJrxml = (ResourceDescriptor)reportUnitDescriptor.getChildren().get(i);
                    if ( (jrxmlDescriptor != null && rdJrxml.getUriString().equals(jrxmlDescriptor.getUriString())) ||
                         (jrxmlDescriptor == null && rdJrxml.getWsType().equals(rdJrxml.TYPE_JRXML) && rdJrxml.isMainReport()))
                    {
                        rdJrxml.setIsNew(false);
                        rdJrxml.setHasData(true);
                        rdJrxml = server.getWSClient().modifyReportUnitResource(reportUnitDescriptor.getUriString(), rdJrxml, resourceFile );
                        // Refresh reportUnitResourceDescriptor....
                        reportUnitDescriptor.getChildren().set(i, rdJrxml);
                        break;
                    }
                }



            }
            else
            {
                System.out.println("Not modified...." + modified + " != " + resourceFile.lastModified());
                System.out.flush();
            }
        }
    }

    public static void writeReport(JasperDesign jd, File outputFile) throws java.lang.Exception
    {
        final String compatibility = IReportManager.getPreferences().get("compatibility", "");

        String content = "";
        if (compatibility.length() == 0)
        {
            content = JRXmlWriter.writeReport(jd, "UTF-8"); // IReportManager.getInstance().getProperty("jrxmlEncoding", System.getProperty("file.encoding") ));
        }
        else
        {
            content = JRXmlWriterHelper.writeReport(jd, "UTF-8", compatibility);
        }

        Writer out = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8");
        out.write(content);
        out.close();
    }
    
}
