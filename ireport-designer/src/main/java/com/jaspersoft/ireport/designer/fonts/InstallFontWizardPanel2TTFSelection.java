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
package com.jaspersoft.ireport.designer.fonts;

import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

public class InstallFontWizardPanel2TTFSelection implements WizardDescriptor.AsynchronousValidatingPanel {

    private InstallFontWizardDescriptor wizard = null;
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private InstallFontVisualPanel1TTFSelection component;

    public InstallFontWizardPanel2TTFSelection(InstallFontWizardDescriptor wizard) {
        this.wizard = wizard;
    }

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    public Component getComponent() {
        if (component == null) {
            component = new InstallFontVisualPanel1TTFSelection(this);
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

        try {
            component.validateForm();
            getWizard().putProperty("WizardPanel_errorMessage", null);
            return true;
        } catch (Exception ex) {
            getWizard().putProperty("WizardPanel_errorMessage", ex.getMessage());
        }
        return true;
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

    // You can use a settings object to keep track of state. Normally the
    // settings object will be the WizardDescriptor, so you can use
    // WizardDescriptor.getProperty & putProperty to store information entered
    // by the user.
    public void readSettings(Object settings) {
        component.readSettings(settings);
    }

    public void storeSettings(Object settings) {
        component.storeSettings(settings);
    }

    /**
     * @return the wizard
     */
    public InstallFontWizardDescriptor getWizard() {
        return wizard;
    }

    /**
     * @param wizard the wizard to set
     */
    public void setWizard(InstallFontWizardDescriptor wizard) {
        this.wizard = wizard;
    }

    public void prepareValidation() {
        
    }

    public void validate() throws WizardValidationException {

        // Load the file...
        File file = new File("" + getWizard().getProperty("normal_ttf_file"));

        if (!file.exists())
        {
            String err = "Unable to file the file: " + file.getPath();
            throw new WizardValidationException(component, err, err);
        }

        java.awt.Font f = null;
        try {
            f = java.awt.Font.createFont(Font.TRUETYPE_FONT,  new java.io.FileInputStream(file));
            getWizard().putProperty("family_name" , f.getFamily());
        }
        catch (IllegalArgumentException ett)
        {
            String err = ett.getMessage() +" No TrueType font";
            throw new WizardValidationException(component, err, err);
        }
        catch (java.awt.FontFormatException ef)
        {
            String err = ef.getMessage() +" Invalid font.";
            throw new WizardValidationException(component, err, err);
        }
         catch (java.io.IOException ioex)
        {
            String err = ioex.getMessage() +" Error loading the file.";
            throw new WizardValidationException(component, err, err);
        }
        catch (Exception ex)
        {
            String err = ex.getMessage() +" Unknown error.";
            throw new WizardValidationException(component, err, err);
        }
    }
}

