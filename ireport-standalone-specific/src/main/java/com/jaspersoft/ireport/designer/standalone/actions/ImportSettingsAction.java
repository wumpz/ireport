/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaspersoft.ireport.designer.standalone.actions;

import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Component;
import java.awt.Dialog;
import java.io.File;
import java.text.MessageFormat;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

// An example action demonstrating how the wizard could be called from within
// your code. You can copy-paste the code below wherever you need.
public final class ImportSettingsAction extends CallableSystemAction {

    private WizardDescriptor.Panel[] panels;

    public void performAction() {
        WizardDescriptor wizardDescriptor = new WizardDescriptor(getPanels());
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle(org.openide.util.NbBundle.getMessage(ImportSettingsVisualPanel1PlatformSelection.class, "CTL_ImportSettingsAction"));
        wizardDescriptor.putProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, false);
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {

            File dir = null;
            String selectedOption = (String)wizardDescriptor.getProperty("selectedOption");

            if (selectedOption == null || selectedOption.equals("0"))
            {
                // Selected a version
                String selectedVersion = (String)wizardDescriptor.getProperty("version");
                if (selectedVersion != null)
                {

                    dir = new File(System.getProperty("netbeans.user"));
                    dir = new File( dir.getParent(), selectedVersion);
                }
            }
            else
            {
                String selectedPath = (String)wizardDescriptor.getProperty("path");
                dir = new File(selectedPath);
            }

            try {
                ImportSettingsUtilities.importSettings(dir);
                JOptionPane.showMessageDialog(Misc.getMainFrame(), NbBundle.getMessage(ImportSettingsVisualPanel1PlatformSelection.class, "ImportSettingsAction.success"));
            } catch (Exception ex)
            {
                JOptionPane.showMessageDialog(Misc.getMainFrame(), NbBundle.getMessage(ImportSettingsVisualPanel1PlatformSelection.class, "ImportSettingsAction.error", ex.getMessage()));
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
                        new ImportSettingsWizardPanel1PlatformSelection(),
                        new ImportSettingsWizardPanel2Result()
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
        return org.openide.util.NbBundle.getMessage(ImportSettingsVisualPanel1PlatformSelection.class, "CTL_ImportSettingsAction");
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
