/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaspersoft.ireport.designer.standalone.actions;

import java.awt.Component;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

public class ImportSettingsWizardPanel1PlatformSelection implements WizardDescriptor.Panel {

    private WizardDescriptor wizardDescriptor = null;
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private Component component;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    public Component getComponent() {
        if (component == null) {
            component = new ImportSettingsVisualPanel1PlatformSelection(this);
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

        if (getWizardDescriptor() != null)
        {
            try {
                ((ImportSettingsVisualPanel1PlatformSelection)getComponent()).validateForm();
            } catch (Exception ex)
            {
                getWizardDescriptor().putProperty("WizardPanel_errorMessage", ex.getMessage());
                return false;
            }
            getWizardDescriptor().putProperty("WizardPanel_errorMessage", null);
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
        if (settings instanceof WizardDescriptor)
        {
            this.setWizardDescriptor((WizardDescriptor) settings);
        }
        ((ImportSettingsVisualPanel1PlatformSelection)getComponent()).readSettings(settings);
    }

    public void storeSettings(Object settings) {

        ((ImportSettingsVisualPanel1PlatformSelection)getComponent()).storeSettings(settings);
    }

    /**
     * @return the wizardDescriptor
     */
    public WizardDescriptor getWizardDescriptor() {
        return wizardDescriptor;
    }

    /**
     * @param wizardDescriptor the wizardDescriptor to set
     */
    public void setWizardDescriptor(WizardDescriptor wizardDescriptor) {
        this.wizardDescriptor = wizardDescriptor;
    }
}

