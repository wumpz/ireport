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

import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.data.WizardFieldsProvider;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Component;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.jasperreports.engine.design.JRDesignField;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

public class NewJrxmlWizardPanel2 implements WizardDescriptor.AsynchronousValidatingPanel {

    private WizardDescriptor wizard;
   
    public NewJrxmlWizardPanel2(WizardDescriptor wizard)
    {
        this.wizard = wizard;
    }
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private NewJrxmlVisualPanel2 component;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    public Component getComponent() {
        if (component == null) {
            component = new NewJrxmlVisualPanel2(this);
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
        
        IReportConnection con = component.getConnection();
        if (con instanceof WizardFieldsProvider &&
            component.getQuery().trim().length() == 0)
        {
            getWizard().putProperty("WizardPanel_errorMessage", I18n.getString("Wizards.Property.Invalidquery"));
            return false;
        }
        
        getWizard().putProperty("WizardPanel_errorMessage", null);
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
        
    }

    public void storeSettings(Object settings) {
    }

    public WizardDescriptor getWizard() {
        return wizard;
    }

    public void setWizard(WizardDescriptor wizard) {
        this.wizard = wizard;
    }

    public void prepareValidation() {
    }

    public void validate() throws WizardValidationException {
        
        List<JRDesignField> discoveredFields = null;
        getWizard().putProperty("discoveredFields", discoveredFields);
        getWizard().putProperty("discoveredFieldsNeedRefresh", "true");
        getWizard().putProperty("query", null);
        getWizard().putProperty("queryLanguage", null);
            
        try {
            IReportConnection con = component.getConnection();
            if (con instanceof WizardFieldsProvider)
            {
                discoveredFields = ((WizardFieldsProvider)con).readFields(component.getQuery());
                getWizard().putProperty("query", component.getQuery());
                getWizard().putProperty("queryLanguage", ((WizardFieldsProvider)con).getQueryLanguage());
            }
            else
            {
                discoveredFields = null;
            }
            
            getWizard().putProperty("discoveredFields", discoveredFields);
            
        } catch (Exception ex)
        {
            Misc.showErrorMessage(ex.getMessage(),"Query error", ex);
            throw new WizardValidationException(component, ex.getMessage(),ex.getLocalizedMessage() );
        }
    }
}

