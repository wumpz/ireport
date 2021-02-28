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
package com.jaspersoft.ireport.designer.wizards;

import com.jaspersoft.ireport.designer.wizards.CustomChooserWizardPanel;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.TemplateWizard;

public final class EmptyReportWizardIterator implements WizardDescriptor.InstantiatingIterator {

    private int index;
    private WizardDescriptor wizard;
    private WizardDescriptor.Panel[] panels;

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
        if (panels == null) {
            
            WizardDescriptor.Panel targetChooserPanel = null;
                boolean useCustomChooserPanel = false;
                
                if (wizard.getProperty("useCustomChooserPanel") != null &&
                    wizard.getProperty("useCustomChooserPanel").equals("true"))
                {
                    targetChooserPanel = new CustomChooserWizardPanel(wizard);
                }
                else
                {
                    System.out.println("Using regular panel...");
                System.out.flush();
                    targetChooserPanel = ((TemplateWizard)wizard).targetChooser();
                }
            
            panels = new WizardDescriptor.Panel[]{
                targetChooserPanel
            };
            String[] steps = createSteps();
            for (int i = 0; i < panels.length; i++) {
                Component c = panels[i].getComponent();
                if (steps[i] == null) {
                    // Default step name to component name of panel. Mainly
                    // useful for getting the name of the target chooser to
                    // appear in the list of steps.
                    steps[i] = c.getName();
                }
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

    public Set instantiate() throws IOException {
        
        if (wizard.getProperty("filename") != null)
        {
           File f = new File( ""+wizard.getProperty("filename"));
           // Let's set the file folder...
           ((TemplateWizard)wizard).setTargetFolder(DataFolder.findFolder( FileUtil.toFileObject( f.getParentFile() )) );
           // Let's set the target folder...
           ((TemplateWizard)wizard).setTargetName(f.getName());
        }
            
            
        if (((TemplateWizard)wizard).getTargetFolder() != null)
        {
                String fname = ((TemplateWizard)wizard).getTargetName();
                String directory = ((TemplateWizard) wizard).getTargetFolder().getPrimaryFile().getPath();
                // We do some strong assumptions here:
                // 1. the directory exists
                // 2. we are not replacing another file if it was specified
                // 3. if specified, the file ends with .jrxml
                
                // Default name specified...
                // let's look for a new valid file name...
                if (fname == null)
                {
                    fname = "Report.jrxml";
                    File f = new File( directory,fname);
                    int i=1;
                    while (f.exists())
                    {
                        fname = "Report_" + i + ".jrxml";
                        f = new File( directory,fname);
                        i++;
                    }
                }
                
                ((TemplateWizard)wizard).setTargetName(fname);
            }
        
        DataFolder df = ((TemplateWizard)wizard).getTargetFolder();
        DataObject dTemplate = ((TemplateWizard)wizard).getTemplate();
        
        // Strip out the extension from the target name...
        String targetName = ((TemplateWizard)wizard).getTargetName();
        if (targetName.toLowerCase().endsWith(".jrxml"))
        {
            targetName = targetName.substring(0, targetName.length()-6);
        }
        
        DataObject dobj = dTemplate.createFromTemplate( df, targetName);
        
        OpenCookie cookie = dobj.getCookie( OpenCookie.class);
        cookie.open();
        return Collections.singleton (dobj.getPrimaryFile ());
    }

    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;
    }

    public void uninitialize(WizardDescriptor wizard) {
        panels = null;
    }

    public WizardDescriptor.Panel current() {
        return getPanels()[index];
    }

    public String name() {
        return index + 1 + ". from " + getPanels().length;
    }

    public boolean hasNext() {
        return index < getPanels().length - 1;
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

    // If nothing unusual changes in the middle of the wizard, simply:
    public void addChangeListener(ChangeListener l) {
    }

    public void removeChangeListener(ChangeListener l) {
    }

    // If something changes dynamically (besides moving between panels), e.g.
    // the number of panels changes in response to user input, then uncomment
    // the following and call when needed: fireChangeEvent();
    /*
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
     */

    /*
    // You could safely ignore this method. Is is here to keep steps which were
    // there before this wizard was instantiated. It should be better handled
    // by NetBeans Wizard API itself rather than needed to be implemented by a
    // client code.
    private String[] createSteps() {
        String[] beforeSteps = null;
        Object prop = wizard.getProperty("WizardPanel_contentData");
        if (prop != null && prop instanceof String[]) {
            beforeSteps = (String[]) prop;
        }

        if (beforeSteps == null) {
            beforeSteps = new String[0];
        }

        String[] res = new String[(beforeSteps.length - 1) + panels.length];
        for (int i = 0; i < res.length; i++) {
            if (i < (beforeSteps.length - 1)) {
                res[i] = beforeSteps[i];
            } else {
                res[i] = panels[i - beforeSteps.length + 1].getComponent().getName();
            }
        }
        return res;
    }
    */
    
    private String[] createSteps() {
        
        String[] res = new String[panels.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = panels[i].getComponent().getName();
        }
        return res;
    }
}
