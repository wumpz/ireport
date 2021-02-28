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
package com.jaspersoft.ireport.designer.charts.datasets.wizards;

import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Component;
import java.awt.Dialog;
import java.text.MessageFormat;
import javax.swing.JComponent;
import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.charts.design.JRDesignCategoryDataset;
import net.sf.jasperreports.charts.design.JRDesignCategorySeries;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;

// An example action demonstrating how the wizard could be called from within
// your code. You can copy-paste the code below wherever you need.
public final class CategoryDatasetWizardAction extends CallableSystemAction {

    private WizardDescriptor.Panel[] panels;

    public void performAction() {
    }

    public void configureChart(JRDesignCategoryDataset dataset, byte chartType)
    {
        WizardDescriptor wizardDescriptor = new WizardDescriptor(getPanels());
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle("Category Chart Wizard");
        wizardDescriptor.putProperty("chartType", chartType);
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {

            JRDesignDataset ds = (JRDesignDataset)wizardDescriptor.getProperty("dataset");

            if (ds == null) return;

            if (!ds.isMainDataset())
            {
                JRDesignDatasetRun dr = new JRDesignDatasetRun();
                dr.setDatasetName(ds.getName());
                dataset.setDatasetRun(dr);
            }
            else
            {
                dataset.setDatasetRun(null);
            }

            if (wizardDescriptor.getProperty("seriesExpression") != null &&
                wizardDescriptor.getProperty("categoryExpression") != null &&
                wizardDescriptor.getProperty("valueExpression") != null)
            {
                JRDesignCategorySeries series = new JRDesignCategorySeries();
                series.setSeriesExpression( Misc.createExpression("java.lang.Object",  (String)wizardDescriptor.getProperty("seriesExpression")));
                series.setCategoryExpression( Misc.createExpression("java.lang.Object",  (String)wizardDescriptor.getProperty("categoryExpression")));
                series.setValueExpression( Misc.createExpression("java.lang.Number",  (String)wizardDescriptor.getProperty("valueExpression")));
                while (dataset.getSeriesList().size() > 0)
                {
                    dataset.removeCategorySeries((JRCategorySeries) dataset.getSeriesList().get(0));
                }
                dataset.addCategorySeries(series);
            }
        }
    }

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
        if (panels == null) {
            panels = new WizardDescriptor.Panel[]{
                        new CategoryDatasetWizardPanel1(),
                        new CategoryDatasetWizardPanel2(),
                        new CategoryDatasetWizardPanel3()
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
                    // TODO if using org.openide.dialogs >= 7.8, can use WizardDescriptor.PROP_*:
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
        return "Start Sample Wizard";
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
