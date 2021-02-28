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
package com.jaspersoft.ireport.designer.palette.actions;

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.crosstab.wizard.CrosstabWizardPanel1;
import com.jaspersoft.ireport.designer.crosstab.wizard.CrosstabWizardPanel2;
import com.jaspersoft.ireport.designer.crosstab.wizard.CrosstabWizardPanel3;
import com.jaspersoft.ireport.designer.crosstab.wizard.CrosstabWizardPanel4;
import com.jaspersoft.ireport.designer.crosstab.wizard.CrosstabWizardPanel5;
import com.jaspersoft.ireport.designer.utils.ColorSchemaGenerator;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Point;
import java.awt.dnd.DropTargetDropEvent;
import java.text.MessageFormat;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.crosstabs.JRCrosstabGroup;
import net.sf.jasperreports.crosstabs.design.JRCrosstabOrigin;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabCell;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabDataset;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabMeasure;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.crosstabs.type.CrosstabTotalPositionEnum;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.base.JRBaseTextElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.PenEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import org.netbeans.api.visual.animator.SceneAnimator;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.Exceptions;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class CreateCrosstabAction extends CreateReportElementAction 
{

    private WizardDescriptor.Panel[] panels;

    private Color getCellBackgroundColor(Color baseColor, String variant, JRDesignCrosstab crosstab, JRCrosstabOrigin origin) {
        
        int c_index = -1; // max between column and row index...
        int r_index = -1;
        
        Color[] colorMapping = new Color[]{
                ColorSchemaGenerator.createColor(baseColor, 3, variant), // Group headers
                ColorSchemaGenerator.createColor(baseColor, 2, variant), // Group 1 totals...
                ColorSchemaGenerator.createColor(baseColor, 1, variant)  // Group >1 totals...
                };
        
        if (origin.getColumnGroupName() != null)
        {
            c_index = (Integer)crosstab.getColumnGroupIndicesMap().get(origin.getColumnGroupName());
            c_index = (crosstab.getColumnGroupsList().size()-1) - c_index;
        }
        if (origin.getRowGroupName() != null)
        {
            r_index = (Integer)crosstab.getRowGroupIndicesMap().get(origin.getRowGroupName());
            r_index = (crosstab.getRowGroupsList().size()-1) - r_index;
        }
        
        int groupIndex = Math.max(c_index, r_index);
        
        if (groupIndex < 0) return null;
        groupIndex = Math.min(groupIndex, 1);
        
        switch (origin.getType())
        {
            case JRCrosstabOrigin.TYPE_DATA_CELL:
            {
                return colorMapping[groupIndex+1];
            }
            case JRCrosstabOrigin.TYPE_ROW_GROUP_HEADER:
            case JRCrosstabOrigin.TYPE_COLUMN_GROUP_HEADER:
            {
                return colorMapping[0];
            }
            case JRCrosstabOrigin.TYPE_ROW_GROUP_TOTAL_HEADER:
            case JRCrosstabOrigin.TYPE_COLUMN_GROUP_TOTAL_HEADER:
            {
                return colorMapping[groupIndex+1];
            }
        }    
        
        return null;
    }

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
        if (panels == null) {
            panels = new WizardDescriptor.Panel[]{
                new CrosstabWizardPanel1(),
                new CrosstabWizardPanel2(),
                new CrosstabWizardPanel3(),
                new CrosstabWizardPanel4(),
                new CrosstabWizardPanel5()
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
    
    public JRDesignElement createReportElement(JasperDesign jd)
    {
        // force the init all the time a crosstab is created...
        panels = null;
        
        WizardDescriptor wizardDescriptor = new WizardDescriptor(getPanels());
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle("New crosstab");
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
        
            try {
                
                int cWidth = 0;
                int cHeight = 0;
                
                JRDesignCrosstab crosstab = new JRDesignCrosstab();
                // 1. Creating the connection
                JRDesignDataset dataset = (JRDesignDataset)wizardDescriptor.getProperty("dataset");
                if (!dataset.isMainDataset())
                {
                    // Need to add a datasetRun...
                    JRDesignDatasetRun datasetRun = new JRDesignDatasetRun();
                    datasetRun.setDatasetName( dataset.getName() );
                    // we assume this crosstab is filled with a connection...
                    datasetRun.setConnectionExpression(Misc.createExpression("java.sql.Connection", "$P{REPORT_CONNECTION}"));
                    ((JRDesignCrosstabDataset)crosstab.getDataset()).setDatasetRun(datasetRun);
                }

                // 2. Add rows groups
                JRDesignCrosstabRowGroup row1 = (JRDesignCrosstabRowGroup)wizardDescriptor.getProperty("row1");
                row1.setWidth(70);
                cWidth += 70;
                JRDesignCrosstabRowGroup row2 = (JRDesignCrosstabRowGroup)wizardDescriptor.getProperty("row2");
                crosstab.addRowGroup(row1);
                if (row2 != null) 
                {
                    row2.setName( getUniqueName(row2.getName(), row1));
                    row2.setWidth(70);
                    cWidth += 70;
                    crosstab.addRowGroup(row2);
                }

                JRDesignCrosstabColumnGroup col1 = (JRDesignCrosstabColumnGroup)wizardDescriptor.getProperty("column1");
                col1.setHeight(30);
                cHeight += 30;
                col1.setName( getUniqueName(col1.getName(), row1, row2));
                
                JRDesignCrosstabColumnGroup col2 = (JRDesignCrosstabColumnGroup)wizardDescriptor.getProperty("column2");
                crosstab.addColumnGroup(col1);
                if (col2 != null)
                {
                    col2.setName( getUniqueName(col2.getName(), col1, row1, row2));
                    col2.setHeight(30);
                    cHeight += 30;
                    crosstab.addColumnGroup(col2);
                }

                // 3. Add the measure
                JRDesignCrosstabMeasure measure = (JRDesignCrosstabMeasure)wizardDescriptor.getProperty("measure");
                crosstab.addMeasure( measure );

                // Create data cells...
                JRDesignCrosstabCell cell = new JRDesignCrosstabCell();
                cell.setWidth(50);
                cWidth += 50;
                cell.setHeight(25);
                cHeight += 25;
                cell.setContents(new JRDesignCellContents());
                ((JRDesignCellContents)cell.getContents()).addElement( createField( Misc.createExpression( measure.getValueClassName(), "$V{" + measure.getName() + "}"), 50, 25));
                crosstab.addCell(cell);
                // Add data element...
                

                List cols = crosstab.getColumnGroupsList();
                List rows = crosstab.getRowGroupsList();
                for (int i=0; i<rows.size(); ++i)
                {
                    JRDesignCrosstabRowGroup row = (JRDesignCrosstabRowGroup)rows.get(i);
                    JRDesignCrosstabCell rowCell = new JRDesignCrosstabCell();
                    rowCell.setRowTotalGroup( row.getName() );
                    rowCell.setContents(new JRDesignCellContents());
                    //rowCell.setWidth(50);
                    rowCell.setHeight(25);
                    cHeight += 25;
                    ((JRDesignCellContents)rowCell.getContents()).addElement( createField( Misc.createExpression( measure.getValueClassName(),  "$V{" + measure.getName() + "}"), 50, 25));
                    crosstab.addCell(rowCell);
                    
                    for (int j=0; j<cols.size(); ++j)
                    {
                        JRDesignCrosstabColumnGroup col = (JRDesignCrosstabColumnGroup)cols.get(j);

                        if (i == 0)
                        {
                            JRDesignCrosstabCell colCell = new JRDesignCrosstabCell();
                            colCell.setColumnTotalGroup( col.getName() );
                            colCell.setContents(new JRDesignCellContents());
                            colCell.setWidth(50);
                            cWidth += 50;
                            //colCell.setHeight(25);
                            ((JRDesignCellContents)colCell.getContents()).addElement( createField( Misc.createExpression( measure.getValueClassName(),  "$V{" + measure.getName() + "}"), 50, 25));
                            crosstab.addCell(colCell);
                        }

                        JRDesignCrosstabCell subtotalCell = new JRDesignCrosstabCell();
                        subtotalCell.setRowTotalGroup( row.getName() );
                        subtotalCell.setColumnTotalGroup( col.getName() );
                        subtotalCell.setContents(new JRDesignCellContents());
                        ((JRDesignCellContents)subtotalCell.getContents()).addElement( createField( Misc.createExpression( measure.getValueClassName(), "$V{" + measure.getName() + "}"), 50, 25));
                        crosstab.addCell(subtotalCell);
                    }
                }
                
                boolean addRowTotal = true;
                if (wizardDescriptor.getProperty("rowGroupTotals") != null)
                {
                    addRowTotal = (Boolean)wizardDescriptor.getProperty("rowGroupTotals");
                }
                
                if (addRowTotal)
                {
                    row1.setTotalPosition( CrosstabTotalPositionEnum.END );
                    if (row2 != null)
                    {
                        row2.setTotalPosition( CrosstabTotalPositionEnum.END );
                    }
                }
                
                boolean addColumnTotal = true;
                if (wizardDescriptor.getProperty("columnGroupTotals") != null)
                {
                    addColumnTotal = (Boolean)wizardDescriptor.getProperty("columnGroupTotals");
                }
                
                if (addColumnTotal)
                {
                    col1.setTotalPosition( CrosstabTotalPositionEnum.END );
                    if (col2 != null)
                    {
                        col2.setTotalPosition( CrosstabTotalPositionEnum.END );
                    }
                }
                
                boolean showGrid = true;
                if (wizardDescriptor.getProperty("showGrid") != null)
                {
                    showGrid = (Boolean)wizardDescriptor.getProperty("showGrid");
                }
                
                String schemaColor = (String) wizardDescriptor.getProperty("schemaColor");
                String schemaVariant = (String) wizardDescriptor.getProperty("schemaVariant");
                boolean whiteGrid = false;
                if (wizardDescriptor.getProperty("whiteGrid") != null)
                {
                    whiteGrid = (Boolean)wizardDescriptor.getProperty("whiteGrid");
                }
                
                // adding fields...
                // 1. Header fields...
                ((JRDesignCellContents)row1.getHeader()).addElement( createField( Misc.createExpression( row1.getBucket().getExpression().getValueClassName() , "$V{" + row1.getName() +"}") , 70,25) );
                if (addRowTotal)
                {
                    ((JRDesignCellContents)row1.getTotalHeader()).addElement( createLabel("Total " + row1.getName(), (row2 != null) ? 140 : 70,25) );
                }
                if (row2 != null)
                {
                    ((JRDesignCellContents)row2.getHeader()).addElement( createField(Misc.createExpression( row2.getBucket().getExpression().getValueClassName() , "$V{" + row2.getName() +"}"), 70,25) );
                    if (addRowTotal)
                    {
                        ((JRDesignCellContents)row2.getTotalHeader()).addElement( createLabel("Total " + row2.getName(), 70,25) );
                    }
                }
                
                ((JRDesignCellContents)col1.getHeader()).addElement( createField(Misc.createExpression( col1.getBucket().getExpression().getValueClassName() , "$V{" + col1.getName() +"}"), 50,30) );
                if (addColumnTotal)
                {
                    ((JRDesignCellContents)col1.getTotalHeader()).addElement( createLabel("Total " + col1.getName(), 50, (col2 != null) ? 60 : 30) );
                }
                if (col2 != null)
                {
                    ((JRDesignCellContents)col2.getHeader()).addElement( createField(Misc.createExpression( col2.getBucket().getExpression().getValueClassName() , "$V{" + col2.getName() +"}"), 50,30) );
                    if (addColumnTotal)
                    {
                        ((JRDesignCellContents)col2.getTotalHeader()).addElement( createLabel("Total " + col2.getName(), 50,30) );
                    }
                }
                
                // we have to add the right border all around the cells...
                List<JRDesignCellContents> contents = ModelUtils.getAllCells(crosstab);
                
                Color baseColor = schemaColor != null ? ColorSchemaGenerator.getColor(schemaColor) : null;
                schemaVariant = schemaVariant == null ? ColorSchemaGenerator.SCHEMA_LIGHT : schemaVariant;
                
                
                for (JRDesignCellContents content : contents)
                {
                    if (content == null) continue;

                    if (showGrid)
                    {
                        content.getLineBox().getPen().setLineColor((whiteGrid) ? Color.WHITE : Color.BLACK);
                        content.getLineBox().getPen().setLineWidth(0.5f);
                        content.getLineBox().getPen().setLineStyle( LineStyleEnum.SOLID);
                    }
                    
                    if (baseColor != null)
                    {
                        Color c = getCellBackgroundColor(baseColor, schemaVariant, crosstab, content.getOrigin());
                        if (c != null)
                        {
                            content.setBackcolor(c);
                            content.setMode( ModeEnum.OPAQUE);
                            int luminance = (30*c.getRed() + 59*c.getGreen() + 11*c.getBlue())/255;
                            
                            if (luminance < 50 ) 
                            {
                                // Set the text white...
                                JRElement[] elements = content.getElements();
                                for (int i=0; i<elements.length; ++i)
                                {
                                    if (elements[i] instanceof JRDesignTextElement)
                                    {
                                        ((JRDesignTextElement)elements[i]).setForecolor(Color.WHITE);
                                    }
                                }
                            }
                        }
                    }
                }
                
                crosstab.setWidth(cWidth);
                crosstab.setHeight(cHeight);
                
                return crosstab;
            
            } catch (Exception ex)
            {
                ex.printStackTrace();
                return null;
            }
            
        }
        
        return null;
    }
    
    private JRDesignStaticText createLabel(String text, int w, int h)
    {
        JRDesignStaticText element = new JRDesignStaticText();
        element.setX(0);
        element.setY(0);
        element.setWidth(w);
        element.setHeight(h);
        element.setText(text);
        element.setHorizontalAlignment( HorizontalAlignEnum.CENTER );
        element.setVerticalAlignment( VerticalAlignEnum.MIDDLE);
        return element;
    }
    
    private JRDesignTextField createField(JRDesignExpression exp, int w, int h)
    {
        JRDesignStyle dataTextfieldStyle = null;
        String styleName = "Crosstab Data Text";

        if (!getJasperDesign().getStylesMap().containsKey(styleName))
        {
            try {
                dataTextfieldStyle = new JRDesignStyle();
                dataTextfieldStyle.setName(styleName);
                dataTextfieldStyle.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                getJasperDesign().addStyle(dataTextfieldStyle);
            } catch (JRException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        else
        {
            dataTextfieldStyle = (JRDesignStyle)getJasperDesign().getStylesMap().get(styleName);
        }
                
        JRDesignTextField element = new JRDesignTextField();
        element.setX(0);
        element.setY(0);
        element.setWidth(w);
        element.setHeight(h);
        //element.setHorizontalAlignment( JRAlignment.HORIZONTAL_ALIGN_CENTER );
        //element.setVerticalAlignment( JRAlignment.VERTICAL_ALIGN_MIDDLE);
        if (dataTextfieldStyle != null)
        {
            element.setStyle(dataTextfieldStyle);
        }
        try {
        CreateTextFieldAction.setMatchingClassExpression(exp,exp.getValueClassName(), true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        element.setExpression(exp);
        return element;
    }
    
    private String getUniqueName(String base, JRCrosstabGroup... grps)
    {
        int j = 1;
        String new_name = base;
        boolean valid = false;
        
        while (!valid)
        {
            valid = true;
            
            for (int i=0; i<grps.length; ++i)
            {
                if (grps[i] != null && grps[i].getName().equals( new_name) )
                {
                    valid = false;
                    break;
                }
            }
            if (valid) break;
            else
            {
                new_name = base + j;
                j++;
            }
        }
        
        return new_name;
    }

    @Override
    public void adjustElement(JRDesignElement[] elements, int index, Scene theScene, JasperDesign jasperDesign, Object parent, Point dropLocation) {

        elements[index].setX(0);
        elements[index].setY(0);
        elements[index].setWidth(jasperDesign.getPageWidth() - jasperDesign.getLeftMargin() - jasperDesign.getRightMargin());
        if (parent instanceof JRBand)
        {
            elements[index].setHeight( ((JRBand)parent).getHeight() );
        }
    }



    /*
    @Override
    public void drop(DropTargetDropEvent dtde) {

        // at this point we should have all done....
        // let's try to adjust the size...
        JRDesignElement element = createReportElement(getJasperDesign());

        if (element == null) return;
        // Find location...
        dropElementAt(getScene(), getJasperDesign(), element, dtde.getLocation());

        final Widget w = ((ReportObjectScene)getScene()).findElementWidget(element);
        final JRBand b = ModelUtils.bandOfElement(element, getJasperDesign());

        if (w != null)
        {
            SwingUtilities.invokeLater( new Runnable() {

                   public void run() {

                            SceneAnimator sc = new  SceneAnimator(getScene());

                            Point p = new Point( getJasperDesign().getLeftMargin(),
                                                 ModelUtils.getBandLocation(b, getJasperDesign()));

                            sc.animatePreferredLocation(w, p);

                            // set the final position here....
                            //sc.animatePreferredLocation(arg0, arg1);
                    }
               });
        }

    }
    */



}
