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
package com.jaspersoft.ireport.designer.subreport;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.templates.DefaultSubreportGenerator;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.wizards.ConnectionSelectionWizardPanel;
import com.jaspersoft.ireport.designer.wizards.FieldsSelectionWizardPanel;
import com.jaspersoft.ireport.designer.wizards.GroupingWizardPanel;
import com.jaspersoft.ireport.designer.wizards.TemplateListWizardPanel;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JRDesignSubreportParameter;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.WizardDescriptor;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.TemplateWizard;

public final class SubreportWizardIterator implements WizardDescriptor.InstantiatingIterator {

    
    WizardDescriptor wizard = null;
    List<WizardDescriptor.Panel> panels = null;
    
    private JRDesignSubreport element = null;
    
    
    // To invoke this wizard, copy-paste and run the following code, e.g. from
    // SomeAction.performAction():
    /*
    WizardDescriptor.Iterator iterator = new SubreportWizardIterator();
    WizardDescriptor wizardDescriptor = new WizardDescriptor(iterator);
    // {0} will be replaced by WizardDescriptor.Panel.getComponent().getName()
    // {1} will be replaced by WizardDescriptor.Iterator.name()
    wizardDescriptor.setTitleFormat(new MessageFormat("{0} ({1})"));
    wizardDescriptor.setTitle("Your wizard dialog title here");
    Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
    dialog.setVisible(true);
    dialog.toFront();
    boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
    if (!cancelled) {
    // do something
    }
     */
    
    // Current panel index...
    private int index;
    private int currentFinalStep = 5;
    
    public void appendWizardPanel(WizardDescriptor.Panel panel)
    {
        List<WizardDescriptor.Panel> l = new ArrayList<WizardDescriptor.Panel>();
        l.add(panel);
        appendWizardPanels(l);
    }
    
    public void appendWizardPanels(List<WizardDescriptor.Panel> newPanels)
    {
        
        getPanels().addAll(newPanels);
        
        for (WizardDescriptor.Panel panel : getPanels())
        {
            Component c = panel.getComponent();
            // Default step name to component name of panel.
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent) c;
                // Turn on subtitle creation on each step
                jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                // Show steps on the left side with the image on the background
                jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                // Turn on numbering of all steps
                jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
            }
        }
    }
    
    
    private List<WizardDescriptor.Panel> newSubreportPanelSet = null;
    private List<WizardDescriptor.Panel>  existingSubreportPanelSet = null;
    private List<WizardDescriptor.Panel> getPanels() {
        
        if (panels == null) {
            panels = new ArrayList<WizardDescriptor.Panel>();
            appendWizardPanel(new SubreportSelectionWizardPanel(wizard, this));
            newSubreportPanelSet = new ArrayList<WizardDescriptor.Panel>();
            newSubreportPanelSet.add(new TemplateListWizardPanel(wizard));
            newSubreportPanelSet.add(new ConnectionSelectionWizardPanel(wizard));
            newSubreportPanelSet.add(new FieldsSelectionWizardPanel(wizard));
            newSubreportPanelSet.add(new GroupingWizardPanel(wizard));
            //newSubreportPanelSet.add(new TemplateWizardPanel(wizard));
            newSubreportPanelSet.add(new SubreportCustomChooserWizardPanel(wizard));
            newSubreportPanelSet.add(new SubreportElementConnectionWizardPanel(wizard));
            appendWizardPanels(newSubreportPanelSet);
            updateStepLabels();
            wizard.putProperty("WizardPanel_contentData", ( (JComponent)(newSubreportPanelSet.get(0).getComponent()) ).getClientProperty("WizardPanel_contentData"));
        }
        return panels;
    }
    
    private int lastSelectedType = 0;
    
    public void updatePanels()
    {
        Integer subreportType = (Integer)wizard.getProperty("subreport_type");
        if (subreportType == null) return;
        if (lastSelectedType == subreportType.intValue()) return;
        
        lastSelectedType = subreportType.intValue();
        
        //  Based on the selected panel, modify the set of panels...
        // 1. Remove the set of panels after the first one...
        
        WizardDescriptor.Panel panel = getPanels().get(0);
        getPanels().clear();
        appendWizardPanel(panel);
        
        if (lastSelectedType == 0)
        {
            appendWizardPanels(newSubreportPanelSet);
        }
        else if (lastSelectedType == 1)
        {
            if (existingSubreportPanelSet == null)
            {
                existingSubreportPanelSet = new ArrayList<WizardDescriptor.Panel>();
                existingSubreportPanelSet.add(new SubreportElementConnectionWizardPanel(wizard));
                existingSubreportPanelSet.add(new SubreportParametersWizardPanel(wizard));
                existingSubreportPanelSet.add(new SubreportExpressionWizardPanel(wizard));
            }
            appendWizardPanels(existingSubreportPanelSet);
        }
        else if (lastSelectedType == 2)
        {
            // Nothing to add...
        }
        
        updateStepLabels();
    }
    
    private void updateStepLabels()
    {
        if (panels == null) return;
        // Adjust the step name for all the current available panels...
        String[] steps = new String[panels.size()];
        int i=0;
        for (WizardDescriptor.Panel pan : panels)
        {
            steps[i] = pan.getComponent().getName();
            i++;
        }
        
        i=0;
        for (WizardDescriptor.Panel pan : panels)
        {
            if (pan.getComponent() instanceof JComponent)
            {
                ((JComponent)pan.getComponent()).putClientProperty("WizardPanel_contentData", steps);
                ((JComponent)pan.getComponent()).putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
            }
            i++;
        }
    }
    

    
    
    public WizardDescriptor.Panel current() {
        return getPanels().get(index);
    }

    public String name() {
        return (index + 1) + " of " + getPanels().size();
    }

    public boolean hasNext() {
        // After the first one there will be always something even if we don't know what yet...
        return index < getPanels().size() - 1;
    }

    public boolean hasPrevious() {
        return index > 0;
    }

    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }

    
    // If something changes dynamically (besides moving between panels), e.g.
    // the number of panels changes in response to user input, then uncomment
    // the following and call when needed: fireChangeEvent();
    
    private Set<ChangeListener> listeners = new HashSet<ChangeListener>(1); // or can use ChangeSupport in NB 6.0
    
    public final void addChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }
    
    public final void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }
    
    protected final void fireChangeEvent() {
        Iterator<ChangeListener> it;
        synchronized (listeners) {
            it = new HashSet<ChangeListener>(listeners).iterator();
        }
        ChangeEvent ev = new ChangeEvent(this);
        while (it.hasNext()) {
            it.next().stateChanged(ev);
        }
    }
     
    public void initialize(WizardDescriptor wizard)
    {
        this.wizard = wizard;
    }

    public Set instantiate() throws IOException {
    
        // this is the finish method...
        element = new JRDesignSubreport(null);
        
        Integer subreportType = (Integer)wizard.getProperty("subreport_type");
        
        if (subreportType == null || subreportType.intValue() == 2)
        {
            return Collections.EMPTY_SET;
        }
        else if (subreportType.intValue() == 1)
        {
               //subreport_parameters_values
            try {
               configureElement(element);
            } catch (Exception ex)
            {
                // do nothing...
                ex.printStackTrace();
            }
        }
        else if (subreportType.intValue() == 0)
        {
            // add the expression...
            
            
            // TODO: make this configurable
            DefaultSubreportGenerator reportGenerator = new DefaultSubreportGenerator();

            FileObject createdFile = reportGenerator.generateReport(wizard);

            if (createdFile == null)
            {
                throw new IOException("Unable to create the report.");
            }
            // Add file to favorites..
            
            
            // check for subreport expression type...
            try {
               configureElement(element);
            } catch (Exception ex)
            {
                // do nothing...
                ex.printStackTrace();
            }

            
            OpenCookie cookie = DataObject.find(createdFile).getCookie( OpenCookie.class);
            cookie.open();
            
            // Open the subreport in the editor...
//            SwingUtilities.invokeLater(new Runnable(){
//                public void run()
//                {
//                    //OpenCookie cookie = createdDataObject.getCookie( OpenCookie.class);
//                    //cookie.open();
//                }
//            });
            
            return Collections.singleton( createdFile );
        }
        
        return Collections.EMPTY_SET;
    }

    private void configureElement(JRDesignSubreport element) throws IOException, JRException
    {
        Boolean useSubParam  = (Boolean)wizard.getProperty("addsubreportparameter");
            String exp = (String)wizard.getProperty("subreportexpression");
            String subreport_directory = (String)wizard.getProperty("subreport_directory");
            String con_exp = (String)wizard.getProperty("subreport_connection_expression");
            String ds_exp = (String)wizard.getProperty("subreport_datasource_expression");
            
            JRParameter[] sub_params = (JRParameter[])wizard.getProperty("subreport_parameters");
            String[] params_expressions = (String[])wizard.getProperty("subreport_parameters_values");
            
            if (useSubParam != null && useSubParam.booleanValue())
            {
                JasperDesign jd = IReportManager.getInstance().getActiveReport();
                
                if (!jd.getParametersMap().containsKey("SUBREPORT_DIR"))
                {
                JRDesignParameter param = new JRDesignParameter();
                param.setName("SUBREPORT_DIR");
                param.setForPrompting(false);
                // TODO: set as directory the target directory for compiled files....
                if (subreport_directory == null)
                {
                    subreport_directory = Misc.getDataFolderPath( ((TemplateWizard)wizard).getTargetFolder() ) + File.separator;
                }
                subreport_directory = Misc.string_replace("\\\\","\\",subreport_directory);
                subreport_directory = "\"" + subreport_directory + "\"";
                param.setDefaultValueExpression( Misc.createExpression("java.lang.String", subreport_directory));
                param.setValueClassName("java.lang.String");
                
                // add the parameter to the document...
                    try {
                        jd.addParameter(param);
                    } catch (JRException ex)
                    {}
                }
            }
            
            if (exp != null)
            {
                element.setExpression( Misc.createExpression("java.lang.String", exp) );
            }
            
            if (con_exp != null)
            {
                element.setConnectionExpression( Misc.createExpression("java.sql.Connection", con_exp) );
            }

            if (ds_exp != null)
            {
                element.setDataSourceExpression( Misc.createExpression("net.sf.jasperreports.engine.JRDataSource", ds_exp) );
            }
            
            if (sub_params != null && params_expressions != null)
            {
                for (int i=0; i<sub_params.length && i<params_expressions.length; ++i)
                {
                    JRDesignSubreportParameter sp = new JRDesignSubreportParameter();
                    sp.setName(  sub_params[i].getName() );
                    sp.setExpression( Misc.createExpression(null, params_expressions[i] ) );
                    element.addParameter(sp);
                }
            }
    }
    
    public void uninitialize(WizardDescriptor arg0) {
        
    }

    public JRDesignSubreport getElement() {
        return element;
    }
}
