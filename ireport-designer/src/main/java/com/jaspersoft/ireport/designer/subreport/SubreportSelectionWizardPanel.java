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

import com.jaspersoft.ireport.designer.JrxmlLoader;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

public class SubreportSelectionWizardPanel implements WizardDescriptor.FinishablePanel, WizardDescriptor.AsynchronousValidatingPanel  {

    private WizardDescriptor wizard= null;
    private SubreportWizardIterator wizardIterator = null;
    
    public SubreportSelectionWizardPanel(WizardDescriptor wizard, SubreportWizardIterator witerator)
    {
        this.wizard = wizard;
        this.wizardIterator = witerator;
    }
     
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private SubreportSelectionVisualPanel component;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    public Component getComponent() {
        if (component == null) {
            component = new SubreportSelectionVisualPanel(this);
        }
        return component;
    }

    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
    // If you have context help:
    // return new HelpCtx(SampleWizardPanel1.class);
    }

    public boolean isValid() {
        if (component == null) return false;
        
        try {
            component.validateForm();
            getWizard().putProperty("WizardPanel_errorMessage",null);
            return true;
        } catch (IllegalArgumentException ex)
        {
            getWizard().putProperty("WizardPanel_errorMessage", ex.getMessage());
        }
        return false;
    }
    
    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1); // or can use ChangeSupport in NB 6.0
    
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
    
    public void updateWizardPanels()
    {
        storeSettings(getWizard());
        getWizardIterator().updatePanels();
        getWizardIterator().fireChangeEvent();
    }

    // You can use a settings object to keep track of state. Normally the
    // settings object will be the WizardDescriptor, so you can use
    // WizardDescriptor.getProperty & putProperty to store information entered
    // by the user.
    public void readSettings(Object settings) {
        if (getWizard().getProperty("subreport_type") == null)
        {
            updateWizardPanels();
        }
    }

    public void storeSettings(Object settings) {
        
        getWizard().putProperty("subreport_type", new Integer(component.getSelectedSubreportType()));
        getWizard().putProperty("subreport_filename", component.getSelectedReportName());
    
        // At that point we should read the available parameters in the selected report....
    
    }

    public boolean isFinishPanel() {
        return component != null && component.canFinish();
    }

    public

    WizardDescriptor getWizard() {
        return wizard;
    }

    public void setWizard(WizardDescriptor wizard) {
        this.wizard = wizard;
    }

    public SubreportWizardIterator getWizardIterator() {
        return wizardIterator;
    }

    public void setWizardIterator(SubreportWizardIterator wizardIterator) {
        this.wizardIterator = wizardIterator;
    }

    public void prepareValidation() {
    }

    public void validate() throws WizardValidationException {
        // We just need to check in the specified report is valid or not...
   
        try {
            getWizard().putProperty("subreport_parameters", null);
            getWizard().putProperty("subreport_directory", null);
            if (component.getSelectedSubreportType() == 1)
            {
                String filename = component.getSelectedReportName();
                JRParameter[] params = null;
                if (filename.toLowerCase().endsWith(".jrxml"))
                {
                    JasperDesign jd = JRXmlLoader.load(filename);
                    params = jd.getParameters();
                }
                else if (filename.toLowerCase().endsWith(".jasper"))
                {
                    JasperReport report = (JasperReport)JRLoader.loadObject(new File(filename));
                    params = report.getParameters();
                }

                // remove buildit params...
                List newParams = new ArrayList();
                for (int i=0; i<params.length; ++i)
                {
                    if (params[i].isSystemDefined()) continue;
                    newParams.add(params[i]);
                }

                getWizard().putProperty("subreport_parameters", newParams.toArray(new JRParameter[newParams.size()]));

                try {
                   File f = new File(filename);
                   getWizard().putProperty("subreport_directory", f.getParentFile().getAbsolutePath() + File.separator);
                } catch (Exception ex) {}
            }
        } catch (Exception ex)
        {
            throw new WizardValidationException(component, "The report specified is not valid", "The report specified is not valid");
        }
        
        getWizardIterator().fireChangeEvent();
    }
    
    
}

