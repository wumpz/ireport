/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaspersoft.ireport.components.table.wizard;

import java.awt.Component;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

public class TableWizardPanel1 implements WizardDescriptor.FinishablePanel {
    private WizardDescriptor wizard = null;
    private TableWizardIterator wizardIterator = null;

    public TableWizardPanel1(WizardDescriptor wizard, TableWizardIterator iterator)
    {
        this.wizard = wizard;
        this.wizardIterator = iterator;
    }
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private TableVisualPanel1 component;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    public Component getComponent() {
        if (component == null) {
            component = new TableVisualPanel1( this );
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
        component.readSettings(settings);
    }

    public void storeSettings(Object settings) {
        component.storeSettings(settings);
    }

    public boolean isFinishPanel() {
        return component.isFinishPanel();
    }

    /**
     * @return the wizard
     */
    public WizardDescriptor getWizard() {
        return wizard;
    }

    /**
     * @return the wizardIterator
     */
    public TableWizardIterator getWizardIterator() {
        return wizardIterator;
    }
}

