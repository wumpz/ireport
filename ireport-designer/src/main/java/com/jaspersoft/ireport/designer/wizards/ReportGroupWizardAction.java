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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.undo.AddGroupUndoableEdit;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Component;
import java.awt.Dialog;
import java.text.MessageFormat;
import javax.swing.JComponent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;

// An example action demonstrating how the wizard could be called from within
// your code. You can copy-paste the code below wherever you need.
public final class ReportGroupWizardAction extends CallableSystemAction {

    
    
    private WizardDescriptor.Panel[] panels;

    public void performAction() {
        
        JasperDesign jd = IReportManager.getInstance().getActiveReport();
        if (jd == null) return;
        
        // Recreate the panel...
        panels = null;
        WizardDescriptor wizardDescriptor = new WizardDescriptor(getPanels(jd));
        
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle("New group wizard");
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
        
            String groupName = (String)wizardDescriptor.getProperty("name");
            String exp = (String)wizardDescriptor.getProperty("expression");
            if (groupName != null)
            {
                try {
                    JRDesignGroup grp = new JRDesignGroup();
                    grp.setName(groupName);
                    grp.setExpression(Misc.createExpression(null, exp));

                    
                    
                    if (wizardDescriptor.getProperty("header") != null &&
                        wizardDescriptor.getProperty("header").equals("true"))
                    {
                        JRDesignBand b = new JRDesignBand();
                        b.setHeight(50);
                        ((JRDesignSection)grp.getGroupHeaderSection()).addBand(b);
                    }
                    
                    if (wizardDescriptor.getProperty("footer") != null &&
                        wizardDescriptor.getProperty("footer").equals("true"))
                    {
                        JRDesignBand b = new JRDesignBand();
                        b.setHeight(50);
                        ((JRDesignSection)grp.getGroupFooterSection()).addBand(b);
                    }
                    
                    jd.addGroup(grp);
                    
                    AddGroupUndoableEdit edit = new AddGroupUndoableEdit(grp, jd.getMainDesignDataset());
                    IReportManager.getInstance().addUndoableEdit(edit);
                    
                } catch (JRException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            
        }
    }

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels(JasperDesign jd) {
        if (panels == null) {
            panels = new WizardDescriptor.Panel[]{
                new ReportGroupWizardPanel1(jd),
                new ReportGroupWizardPanel2(jd)
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

    public String getName() {
        return "Add Report Group";
    }

    @Override
    public String iconResource() {
        return null;
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
