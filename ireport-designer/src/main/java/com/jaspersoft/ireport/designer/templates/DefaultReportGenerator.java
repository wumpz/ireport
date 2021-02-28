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
package com.jaspersoft.ireport.designer.templates;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.compatibility.JRXmlWriterHelper;
import com.jaspersoft.ireport.designer.utils.ImageExpressionFileResolver;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.TemplateWizard;

/**
 * Default report generator based of the iReport template files
 * having special fields as templates.
 * This report generator assumes the wizard is of type TemplateWizard.
 * If it is not, the filename property is used as destination file name.
 * The generator looks for the following properties:
 * - reportType
 * - query
 * - queryLanguage
 * - reportname
 * - selectedFields
 * - groupFields
 * - filename (optional)
 * 
 * @author gtoffoli
 */
public class DefaultReportGenerator extends AbstractReportGenerator {

    public FileObject generateReport(WizardDescriptor wizard) {
        
        Misc.log("Inside generateReport");
        
        try {
            // 1. Load the selected template...
            Misc.log("Generating design ...");
            
            JasperDesign jasperDesign = generateDesign(wizard);
            
            Misc.log("Generating design OK ...");
            File f = getFile(wizard);
            Misc.log("get the file ...");
            Misc.log("The file store the generated report is " + f);
            
            if (!f.exists()) {
               f.createNewFile();
            }

                    // Check the compatibility...
                    final String compatibility = IReportManager.getPreferences().get("compatibility", "");

                    String content = "";
                    if (compatibility.length() == 0)
                    {
                        content = JRXmlWriter.writeReport(jasperDesign, "UTF-8"); // IReportManager.getInstance().getProperty("jrxmlEncoding", System.getProperty("file.encoding") ));
                    }
                    else
                    {
                        content = JRXmlWriterHelper.writeReport(jasperDesign, "UTF-8", compatibility);
                    }

                    // Write content to file...
                    PrintWriter pw = new PrintWriter(new FileOutputStream(f.getPath()));
                    pw.write(content);
                    pw.close();


            JasperCompileManager.writeReportToXmlFile( jasperDesign, f.getPath() );
            
            return FileUtil.toFileObject(f);
        
        } catch (Exception ex) {
           Misc.log("Exception generating the file ...",ex);
            //ex.printStackTrace();
            //Misc.showErrorMessage("An error has occurred generating the report:\n" + ex.getMessage(), "Error", ex);
            return null;
        }
    }
    
    protected File getFile(WizardDescriptor wizard) throws Exception
    {
            // If we used our cool custom file chooser panel, we should find the
            // property filename set.
            File f = null;
            String fname = null;
            String directory = null;
            
            if (wizard.getProperty("filename") != null)
            {
               f = new File( ""+wizard.getProperty("filename"));
               
               if (wizard instanceof TemplateWizard)
               {
                   // Let's set the file folder...
                   DataFolder df = ((TemplateWizard)wizard).getTargetFolder();
                   if (df == null || df.getPrimaryFile() == null ||
                       !Misc.getDataFolderPath(df).equals( f.getParent()))
                   {
                       // Band idea...
                       //((TemplateWizard)wizard).setTargetFolder( DataFolder.findFolder( FileUtil.toFileObject( f.getParentFile() )) );
                       directory = f.getParent();
                   }
                        
                   // Let's set the target folder...
                   if (((TemplateWizard)wizard).getTargetName() == null ||
                       !((TemplateWizard)wizard).getTargetName().equals(f.getName()))
                   {
                       // Bad idea... 
                       //((TemplateWizard)wizard).setTargetName(f.getName());
                       fname = f.getName();
                   }
               }
            }
            
            if (wizard instanceof TemplateWizard)
            {
                if (((TemplateWizard)wizard).getTargetFolder() != null)
                {
                    if (fname == null) fname = ((TemplateWizard)wizard).getTargetName();
                    if (directory == null) directory = Misc.getDataFolderPath( ((TemplateWizard) wizard).getTargetFolder() );
                    // We do some strong assumptions here:
                    // 1. the directory exists
                    // 2. we are not replacing another file if it was specified
                    // 3. if specified, the file ends with .jrxml

                    // Default name specified...
                    // let's look for a new valid file name...
                    if (fname == null)
                    {
                        fname = "Report.jrxml";
                        f = new File( directory,fname);
                        int i=1;
                        while (f.exists())
                        {
                            fname = "Report_" + i + ".jrxml";
                            f = new File( directory,fname);
                            i++;
                        }
                    }
                    else
                    {
                        f = new File( directory,fname);
                    }

                    if (((TemplateWizard)wizard).getTargetName() == null ||
                       !((TemplateWizard)wizard).getTargetName().equals(fname ))
                    {
                        // Bad Idea...
                        //((TemplateWizard)wizard).setTargetName(fname);
                    }
                }
            }
            
            if (f == null)
            {
                throw new Exception("Filename not specified");
            }
            
            return f;
    }
    
    protected JasperDesign generateDesign(WizardDescriptor wizard) throws Exception
    {
            FileObject reportTemplate = (FileObject) wizard.getProperty("reportTemplate");
            
            String reportType = (String) wizard.getProperty("reportType");
            List<JRDesignField> selectedFields = (List<JRDesignField>) wizard.getProperty("selectedFields");
            List<JRDesignField> groupFields = (List<JRDesignField>) wizard.getProperty("groupFields");
            String query = (String) wizard.getProperty("query");
            String queryLanguage = (String) wizard.getProperty("queryLanguage");
                    
            JasperDesign jasperDesign = JRXmlLoader.load( reportTemplate.getInputStream() );

            jasperDesign.setName(""+wizard.getProperty("reportname"));
            if (reportType == null) reportType = "tabular";
            if (jasperDesign.getProperty("template.type") != null)
            {
                reportType = jasperDesign.getProperty("template.type");
            }

            boolean keepExtraGroups = false;
            boolean noLayoutChanges = false;

            if (wizard.getProperty("noFields") != null &&
                wizard.getProperty("noFields").equals("true"))
            {
                noLayoutChanges = true;
            }

            if (jasperDesign.getProperty("template.keepExtraGroups") != null &&
                jasperDesign.getProperty("template.keepExtraGroups").equals("true"))
            {
                keepExtraGroups = true;
            }

            jasperDesign.getProperty("");

            
            if (selectedFields == null) selectedFields = new ArrayList<JRDesignField>();
            if (groupFields == null) groupFields = new ArrayList<JRDesignField>();
            
            // Adding fields
            for (JRDesignField f : selectedFields)
            {
                jasperDesign.addField(f);
            }
            
            // Query...
            if (query != null)
            {
                JRDesignQuery designQuery = new JRDesignQuery();
                designQuery.setText(query);
                if (queryLanguage != null)
                {
                    designQuery.setLanguage(queryLanguage);
                }
                
                jasperDesign.setQuery(designQuery);
            }

            // Adjusting groups
            for (int i=0; i<groupFields.size(); ++i)
            {
                  if (jasperDesign.getGroupsList().size() <= i)
                  {
                      // Add a new group on the fly...
                      JRDesignGroup g =new JRDesignGroup();
                      g.setName(groupFields.get(i).getName());
                      jasperDesign.addGroup(g);
                  }


                  JRDesignGroup group = (JRDesignGroup)jasperDesign.getGroupsList().get(i);

//                  String oldGroupName = group.getName();
//                  if (oldGroupName != null && oldGroupName.length() > 0)
//                  {
//                    String newGroupName = groupFields.get(i).getName();
//                    ModelUtils.
//                  }
                  group.setName(groupFields.get(i).getName());
                  group.setExpression(Misc.createExpression("java.lang.Object", "$F{" + groupFields.get(i).getName() + "}"));
                  // find the two elements having as expression: G1Label and G1Field
                  if (group.getGroupHeaderSection() != null && group.getGroupHeaderSection().getBands().length > 0)
                  {
                        JRBand groupHeaderSection = group.getGroupHeaderSection().getBands()[0];
                        JRDesignStaticText st = findStaticTextElement(groupHeaderSection, "G"+(i+1)+"Label");
                        if (st == null) st = findStaticTextElement(groupHeaderSection, "GroupLabel");
                        if (st == null) st = findStaticTextElement(groupHeaderSection, "Group Label");
                        if (st == null) st = findStaticTextElement(groupHeaderSection, "Label");
                        if (st == null) st = findStaticTextElement(groupHeaderSection, "Group name");

                        if (st != null)
                        {
                            st.setText(groupFields.get(i).getName());
                        }

                        JRDesignTextField tf = findTextFieldElement(groupHeaderSection, "G"+(i+1)+"Field");
                        if (tf == null) tf = findTextFieldElement(groupHeaderSection, "GroupField");
                        if (tf == null) tf = findTextFieldElement(groupHeaderSection, "Group Field");
                        if (tf == null) tf = findTextFieldElement(groupHeaderSection, "Field");


                        if (tf != null)
                        {
                            JRDesignExpression expression = Misc.createExpression( groupFields.get(i).getValueClassName(), "$F{" + groupFields.get(i).getName() + "}");
                            // Fix the class (the Textfield has a limited set of type options...)
                            com.jaspersoft.ireport.designer.palette.actions.CreateTextFieldAction.setMatchingClassExpression(expression, groupFields.get(i).getValueClassName(), true);
                            tf.setExpression(expression);
                        }
                  }
            }

            // Remove extra groups...
            if (!keepExtraGroups && !noLayoutChanges)
            {
                while (groupFields.size() < jasperDesign.getGroupsList().size())
                {
                   jasperDesign.removeGroup( (JRDesignGroup)jasperDesign.getGroupsList().get(groupFields.size()));
                }
            }

            JRElementGroup detailBand = (jasperDesign.getDetailSection() != null &&
                                         jasperDesign.getDetailSection().getBands() != null &&
                                          jasperDesign.getDetailSection().getBands().length > 0) ? jasperDesign.getDetailSection().getBands()[0] : null;
           
            // Adjusting detail...
            if (!noLayoutChanges && reportType != null && reportType.equals("tabular"))
            {
                // Add the labels to the column header..
                JRElementGroup columnHeaderBand = (JRDesignBand)jasperDesign.getColumnHeader();
                
                // Find the label template...
                JRDesignStaticText labelElement = null;
                if (columnHeaderBand != null)
                {
                    labelElement = findStaticTextElement(columnHeaderBand, "DetailLabel" );
                    if (labelElement == null) labelElement = findStaticTextElement(columnHeaderBand, "Label");
                    if (labelElement == null) labelElement = findStaticTextElement(columnHeaderBand, "Header");
                }
                
                JRDesignTextField fieldElement = null;
                if (detailBand != null)
                {
                    fieldElement = findTextFieldElement(detailBand, "DetailField" );
                    if (fieldElement == null) fieldElement = findTextFieldElement(detailBand, "Field");
                }

                if (labelElement != null)
                {
                    columnHeaderBand = labelElement.getElementGroup();
                    removeElement(columnHeaderBand, labelElement);
                }
                if (fieldElement != null)
                {
                    detailBand = fieldElement.getElementGroup();
                    removeElement(detailBand, fieldElement);
                }
                
                int width = jasperDesign.getPageWidth() - jasperDesign.getRightMargin() - jasperDesign.getLeftMargin();
                if (detailBand != null && detailBand instanceof JRDesignFrame)
                {
                    width = ((JRDesignFrame)detailBand).getWidth();
                }
                int cols = selectedFields.size() - groupFields.size();
                if (cols > 0)
                {
                     width /= cols;
                     int currentX = 0;
                     for (JRDesignField f : selectedFields)
                     {
                         if (groupFields.contains(f)) continue;
                         if (labelElement != null && columnHeaderBand != null)
                         {
                             JRDesignStaticText newLabel = (JRDesignStaticText)labelElement.clone();
                             newLabel.setText( f.getName() );
                             newLabel.setX(currentX);
                             newLabel.setWidth(width);
                             addElement(columnHeaderBand, newLabel);
                         }
                         if (fieldElement != null && detailBand != null)
                         {
                             JRDesignTextField newTextField = (JRDesignTextField)fieldElement.clone();
                             JRDesignExpression expression = Misc.createExpression( f.getValueClassName(), "$F{" + f.getName() + "}");
                                // Fix the class (the Textfield has a limited set of type options...)
                             com.jaspersoft.ireport.designer.palette.actions.CreateTextFieldAction.setMatchingClassExpression(expression, f.getValueClassName(), true);
                             newTextField.setExpression(expression);
                             newTextField.setX(currentX);
                             newTextField.setWidth(width);
                             addElement(detailBand, newTextField);
                         }
                                 
                         currentX += width;
                     }
                }
                
            }
            else if (!noLayoutChanges && reportType != null && reportType.equals("columnar") && detailBand != null)
            {
                // Add the labels to the column header..
                JRElementGroup detailBandField = (JRDesignBand)jasperDesign.getDetailSection().getBands()[0];
                // Find the label template...

                JRDesignStaticText labelElement = findStaticTextElement(detailBand, "DetailLabel" );
                if (labelElement == null) labelElement = findStaticTextElement(detailBand, "Label");
                if (labelElement == null) labelElement = findStaticTextElement(detailBand, "Header");

                JRDesignTextField fieldElement = findTextFieldElement(detailBandField, "DetailField" );
                if (fieldElement == null) fieldElement = findTextFieldElement(detailBandField, "Field");

                if (labelElement != null)
                {
                    detailBand = labelElement.getElementGroup();
                    removeElement(detailBand, labelElement);
                }

                if (fieldElement != null)
                {
                    detailBandField = fieldElement.getElementGroup();
                    removeElement(detailBandField, fieldElement);
                }
                
                int currentY = 0;
                int rowHeight = 0; // Just to set a default...
                if (labelElement != null) rowHeight = labelElement.getHeight();
                if (fieldElement != null) rowHeight = Math.max( rowHeight, fieldElement.getHeight());
                // if rowHeight is still 0... no row will be added...
                for (JRDesignField f : selectedFields)
                {
                    if (groupFields.contains(f)) continue;
                    if (labelElement != null)
                    {
                        JRDesignStaticText newLabel = (JRDesignStaticText)labelElement.clone();
                        newLabel.setText( f.getName() );
                        newLabel.setY(currentY);
                        addElement(detailBand, newLabel);
                    }
                    if (fieldElement != null)
                    {
                        JRDesignTextField newTextField = (JRDesignTextField)fieldElement.clone();
                        JRDesignExpression expression = Misc.createExpression( f.getValueClassName(), "$F{" + f.getName() + "}");
                            // Fix the class (the Textfield has a limited set of type options...)
                        com.jaspersoft.ireport.designer.palette.actions.CreateTextFieldAction.setMatchingClassExpression(expression, f.getValueClassName(), true);
                        newTextField.setExpression(expression);
                        newTextField.setY(currentY);
                        addElement(detailBandField, newTextField);
                    }

                    currentY += rowHeight;
                }

                setGroupHeight(detailBand,currentY);
                setGroupHeight(detailBandField,currentY);
                
            }

            // Copy resources (Images)
            //Misc.log("Copying resources...");

            List<JRDesignElement> elements = ModelUtils.getAllElements(jasperDesign);
            for (JRDesignElement ele : elements)
            {
                if (ele instanceof JRDesignImage)
                {
                    JRDesignImage imgelement = (JRDesignImage)ele;
                    if (imgelement.getExpression() != null &&
                        imgelement.getExpression().getValueClassName().equals("java.lang.String"))
                    {
                        // If this refers to a static position... copy the image locally to the
                        // document page if the file does not exists...
                        File file = FileUtil.toFile(reportTemplate);
                        
                        if (file.getParentFile() != null)
                        {
                              File reportFolder = file.getParentFile();
                              ImageExpressionFileResolver fr = new ImageExpressionFileResolver(imgelement, reportFolder+"", jasperDesign);
                              File f = fr.resolveFile(null);

                              //Misc.log("Resolving: " + Misc.getExpressionText(imgelement.getExpression()) + " "+ f);

                              if (f != null)
                              {
                                  JRDesignExpression exp = Misc.createExpression("java.lang.String", "\"" + f.getName() + "\"");
                                  imgelement.setExpression(exp);
                                  File destFolder = getFile(wizard).getParentFile();
                                  //Misc.log("Destination Folder: " + destFolder);
                                  if (destFolder != null)
                                  {

                                    File destFile = new File(destFolder, f.getName());
                                    //Misc.log("Destination File: " + destFile);
                                    if (!destFile.exists())
                                    {
                                        Misc.copyFile(f, destFile);
                                    }
                                  }
                              }
                        }
                    }
                }
            }


            return jasperDesign;
    }

    public void removeElement(JRElementGroup container, JRDesignElement element)
    {
        if (container instanceof JRDesignElementGroup)
       {
           ((JRDesignElementGroup)container).removeElement(element);
       }
       if (container instanceof JRDesignFrame)
       {
           ((JRDesignFrame)container).removeElement(element);
       }
    }

    public void addElement(JRElementGroup container, JRDesignElement element)
    {
        if (container instanceof JRDesignElementGroup)
       {
           ((JRDesignElementGroup)container).addElement(element);

       }
       if (container instanceof JRDesignFrame)
       {
           ((JRDesignFrame)container).addElement(element);
       }
    }

    private void setGroupHeight(JRElementGroup container, int currentY) {
        if (container instanceof JRDesignBand)
       {
           ((JRDesignBand)container).setHeight(Math.max(currentY, ((JRDesignBand)container).getHeight()));

       }
       if (container instanceof JRDesignFrame)
       {
           ((JRDesignFrame)container).setHeight(Math.max(currentY, ((JRDesignFrame)container).getHeight()));
       }
    }
}
