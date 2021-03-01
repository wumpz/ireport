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

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.validation.ImageElementValidationItem;
import com.jaspersoft.ireport.jasperserver.validation.SubReportElementValidationItem;
import com.jaspersoft.ireport.jasperserver.validation.TemplateElementValidationItem;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class RepositoryJrxmlFile extends RepositoryFile {

    /** Creates a new instance of RepositoryFolder */
    public RepositoryJrxmlFile(JServer server, ResourceDescriptor descriptor) {
        super( server, descriptor);
        //MainFrame.getMainInstance().addReportFrameActivatedListener( this );
    }

    public String toString()
    {
        if (getDescriptor() != null)
        {
            return ""+getDescriptor().getLabel();
        }   
        return "???";
    }

    @Override
    public String getExtension()
    {
        return "jrxml";
    }
    
    
    
    
/*
    public void reportFrameActivated(ReportFrameActivatedEvent reportFrameActivatedEvent) {
        
        
        if (getReportFrame() == null) return;
        if (reportFrameActivatedEvent.getReportFrame() == null)
        {
            // All frames are begin closed...
            setReportFrame(null);
            IRPlugin.getMainInstance().getRepositoryExplorer().updateToolBar();
            return;
        }
        
        // Check if the reportFrame is still open...
        JInternalFrame[] frames = MainFrame.getMainInstance().getJMDIDesktopPane().getAllFrames();
        
        boolean found = false;
        for (int i=0; i<frames.length; ++i)
        {
            if (frames[i] == getReportFrame())
            {
                found = true;
                break;
            }
        }
        if (!found)
        {
            setReportFrame(null);
            IRPlugin.getMainInstance().getRepositoryExplorer().updateToolBar();
        }
        
    }

    public void editFile() throws Exception 
    {
        if (getReportFrame() == null)
        {
            // Download the file...
            // 1. Create a temp file name...
            String tmpFileName = IRPlugin.createTmpFileName(this.getDescriptor().getName(),".jrxml");
            getServer().getWSClient().get(this.getDescriptor(), new File(tmpFileName));
            setReportFrame( MainFrame.getMainInstance().openFile( tmpFileName ) );
            
            // look for the property "com.jaspersoft.ji.adhoc.edited"
            for (int i=0; i<getReportFrame().getReport().getJRproperties().size(); ++i )
            {
                it.businesslogic.ireport.JRProperty p = (it.businesslogic.ireport.JRProperty)getReportFrame().getReport().getJRproperties().get(i);
                if (p.getName().equals( "com.jaspersoft.ji.adhoc" ) &&
                    p.getValue().equals("1"))
                {
                    p.setValue("0");
                    try {
                        
                        SwingUtilities.invokeLater( new Runnable() {
                            
                           public void run()
                           {
                               if (javax.swing.JOptionPane.showConfirmDialog(MainFrame.getMainInstance(),
                                IRPlugin.getString("messages.adhoc", "You have selected to edit an Ad Hoc report.\n" +
                               "If you continue, the report will lose its sorting and grouping.\n" +
                               "Furthermore, any changes you make in iReport will be lost\n" +
                               "next Time you edit it via the Ad Hoc report editor.\nContinue anyway?"),
                               IRPlugin.getString("alert","Alert!"),
                               javax.swing.JOptionPane.YES_NO_OPTION,
                               javax.swing.JOptionPane.WARNING_MESSAGE) == javax.swing.JOptionPane.NO_OPTION)
                               {
                               		getReportFrame().setVisible(false);
                                        getReportFrame().getMainFrame().getJMDIDesktopPane().internalFrameClosed(reportFrame);
                                        getReportFrame().dispose();
                                        //getReportFrame().removeInternalFrameListener(IRPlugin.this);
                                        //getReportFrame().getReport().removeSubDatasetObjectChangedListener(IRPlugin.this);
                               } 
                           }
                            
                        });
                    
                    } catch (Exception ex)
                    {
                        
                    }
                }
            }
            
            
            // Load all images...
            loadRepositoryImages(getReportFrame().getReport().getElements());
        }
        else
        {
            getReportFrame().setSelected(true);
        }
    }
    
    /**
     * Try to load images with expression starting by repo: and type String...
     * This method is recursice on crosstab elements...
     *
    protected void loadRepositoryImages(java.util.Vector elements)
    {
        Enumeration enumElements = elements.elements();
        while (enumElements.hasMoreElements())
        {
            ReportElement re = (ReportElement)enumElements.nextElement();
            if (re instanceof ImageReportElement)
            {
                ImageReportElement ire = (ImageReportElement)re;
                //System.out.println("Getting image for:" + ire.getImageClass() + " / " + ire.getImageExpression());
                String expression =ire.getImageExpression();
                expression = Misc.string_replace("\\","\\\\",expression);
		expression = Misc.string_replace("","\"",expression);
                
                if ((ire.getImageClass().length() == 0 || ire.getImageClass().equals("java.lang.String")) &&
                    expression.toLowerCase().startsWith("repo:"))
                {
                    ResourceDescriptor rd = new ResourceDescriptor();
                    expression = expression.substring(5);
                    if (expression.startsWith("/"))
                    {
                        rd.setUriString(expression);
                    }
                    else
                    {
                        rd.setUriString(this.getDescriptor().getParentFolder() + "/" + expression);
                    }
                    //System.out.println("Getting:" + rd.getUriString());
                    RepositoryFile rf = new RepositoryFile(this.getServer(), rd);
                    try {
                        String imgFile = rf.getFile();
                        if (imgFile != null)
                        {
                            javax.swing.ImageIcon img = new javax.swing.ImageIcon(imgFile);
                            ire.setImg(img.getImage());
                        }
                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
            if (re instanceof CrosstabReportElement)
            {
                loadRepositoryImages( ((CrosstabReportElement)re).getElements());
            }
        }
        
        
    }
    
   
    /**
     * way to check for references....
     *
    public boolean validateUrls(JServer server, RepositoryReportUnit repoUnit) {
        
        List elementValidationItems = new java.util.ArrayList();
        
        if (getReportFrame() != null)
        {
            elementValidationItems = identifyElementValidationItems(getReportFrame().getReport() , this.getDescriptor());
            
            Report report = getReportFrame().getReport();
            boolean skipValidation = false;
            for (int i=0; i< report.getJRproperties().size(); ++i)
            {
                JRProperty prop = (JRProperty)report.getJRproperties().get(i);
                if (prop.getName() != null && prop.getName().equals("com.jaspersoft.irplugin.validation"))
                {
                    if (prop.getValue().equals("0"))
                    {
                        skipValidation = true;
                    }
                    break;
                }
            }
            
            if (elementValidationItems.size() > 0 && !skipValidation)
            {
                JrxmlValidationDialog jvd = new JrxmlValidationDialog(MainFrame.getMainInstance(),true);
                jvd.setElementVelidationItems( elementValidationItems );
                jvd.setServer( server );
                jvd.setReportUnit( repoUnit );
                jvd.setReport( getReportFrame().getReport() );
                jvd.setVisible(true);
                
                if (jvd.getDialogResult() == JOptionPane.CANCEL_OPTION)
                {
                    return false;
                }
            }
        }
        
        return true;
        
    }
    */
    public static String getValidName( String name, ResourceDescriptor ruDesc)
    {
        return getValidName( name, ruDesc, 0);
    }
    
    private static String getValidName( String name, ResourceDescriptor ruDesc, int k )
    {
        // Check if the name is duplicated....
        String ext = "";
        String base = name;
        if (name.lastIndexOf(".") >  0)
        {
            ext = name.substring( name.lastIndexOf(".") );
            base = name.substring(0, name.lastIndexOf(".") );
        }
        String newName = base + ( (k==0) ? "" :  "-"+k) + ext;
        
        for (int i=0; i<ruDesc.getChildren().size(); ++i)
        {
            ResourceDescriptor rd = (ResourceDescriptor) ruDesc.getChildren().get(i);
            if (rd.getName() != null && rd.getName().equals(newName))
            {
                return getValidName( name,  ruDesc, ++k );
            }
        }
        return newName;
    }
    
    public static File getFileFromExpression(String exp, String clazz, String baseDir)
    {
        // Assuming null class is String...
        if (clazz == null || clazz.equals("java.lang.String"))
        {
             if (!baseDir.endsWith(File.separator))
             {
                 baseDir = baseDir + File.separator;
             }
             if (exp.startsWith("$P{SUBREPORT_DIR} + "))
             {
                exp = exp.substring("$P{SUBREPORT_DIR} + ".length());
                if (baseDir != null)
                {
                    exp = baseDir + exp;
                }
             }  
             else if (exp.startsWith("\".\\\\"))
             {
                 exp = baseDir + exp.substring(4);
             }else if (exp.startsWith("\"./"))
             {
                 exp = baseDir + exp.substring(3);
             }
             
            // try to resolve this file locally....

            //exp = Misc.string_replace("\\","\\\\",exp);
            // Could be a file...
            exp = Misc.string_replace("\\","\\\\",exp);
            exp = Misc.string_replace("","\"",exp);

            if (exp.toLowerCase().startsWith("repo:") ||
                exp.toLowerCase().startsWith("http://") ||
                exp.toLowerCase().startsWith("https://"))
            {
                return null;
            }
            
            if (exp.toLowerCase().endsWith(".jasper"))
            {
                exp = exp.substring(0, exp.lastIndexOf(".jasper")) + ".jrxml"; 
            }

            File f = new File( exp );
            if (!f.exists())
            {
                // Let's see if it is looked in the right place...
                if (f.getParent() == null || f.getParent().equals(""))
                {
                    f = new File(baseDir, f.getName());
                    if (!f.exists()) return null;
                }
            }
            
            return f;
        }
        return null;
    }
    
    
    
    public static List identifyElementValidationItems(JasperDesign report, ResourceDescriptor parentDescriptor, String reportDir)
    {
            List elementValidationItems = new ArrayList();
            
            // look for style references...
            JRReportTemplate[] templates = report.getTemplates();
            for (JRReportTemplate template : templates)
            {
                String fname = Misc.getExpressionText(template.getSourceExpression());
                File f = getFileFromExpression(fname, "java.lang.String",reportDir);
                if (f != null)
                {
                    TemplateElementValidationItem ievi = new TemplateElementValidationItem(template);
                    ievi.setOriginalFileName( f );

                    String name = getValidName( f.getName(), parentDescriptor );
                    ievi.setParentFolder( parentDescriptor.getParentFolder());
                    ievi.setResourceName( name );
                    ievi.setProposedExpression("\"repo:" + name +"\"");
                    elementValidationItems.add(ievi);
                }
            }


            List<JRDesignElement> elements = ModelUtils.getAllElements(report, true);
            for (JRDesignElement re : elements)
            {
                if (re instanceof JRDesignImage)
                {
                    JRDesignImage ire = (JRDesignImage)re;
                    if (ire.getExpression() == null) continue;
                    
                    String fname = ire.getExpression().getText();
                          
                    File f = getFileFromExpression(fname, ire.getExpression().getValueClassName(),reportDir);
                    
                    if (f != null)
                    {
                        ImageElementValidationItem ievi = new ImageElementValidationItem();
                        ievi.setOriginalFileName( f );

                        String name = getValidName( f.getName(), parentDescriptor );
                        ievi.setParentFolder( parentDescriptor.getParentFolder());
                        ievi.setResourceName( name );
                        ievi.setProposedExpression("\"repo:" + name +"\"");
                        ievi.setReportElement(ire);
                        elementValidationItems.add(ievi);
                    }
                }
                else if (re instanceof JRDesignSubreport)
                {
                    JRDesignSubreport sre = (JRDesignSubreport)re;
                    if (sre.getExpression() == null) continue;
                    
                    String fname = sre.getExpression().getText();
                          
                    File f = getFileFromExpression(fname, sre.getExpression().getValueClassName(), reportDir);

//                    if (f.getName().toUpperCase().endsWith(".jasper"))
//                    {
//                        // look for the jrxml...
//                        String name = Misc.changeFileExtension( f.getName(), "jrxml");
//                        f = new File(f.getParent(), name);
//                    }

                        if (f != null)
                        {
                            SubReportElementValidationItem ievi = new SubReportElementValidationItem();
                            ievi.setOriginalFileName( f );
                            
                            String name = getValidName( f.getName(), parentDescriptor );
                            ievi.setParentFolder( parentDescriptor.getParentFolder());
                            ievi.setResourceName( name );
                            ievi.setProposedExpression("\"repo:" + name +"\"" );
                            ievi.setReportElement(sre);
                            elementValidationItems.add(ievi);
                        }

                }
            }
            
            return elementValidationItems;
    }
    
    
}
