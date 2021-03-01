/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2013 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.ireport.designer.standalone.menu;

import com.jaspersoft.ireport.designer.standalone.IReportStandaloneManager;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.wizards.CustomTemplateWizard;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Dialog;
import java.io.File;
import org.openide.DialogDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;

public final class ReportWizardAction extends CallableSystemAction {

    public void performAction() {
        

            CustomTemplateWizard wizardDescriptor = new CustomTemplateWizard();

            //wizardDescriptor.putProperty("useCustomChooserPanel", "true");
            File targetFolder = Misc.findStartingDirectory();
            DataFolder df = DataFolder.findFolder(FileUtil.toFileObject(targetFolder));
            wizardDescriptor.setTargetFolder(df);

            
            try {
            FileObject templateFileObject = Repository.getDefault().getDefaultFileSystem().getRoot().getFileObject("Templates/Report/Report");
            wizardDescriptor.setTemplate(DataObject.find(templateFileObject));
            } catch (DataObjectNotFoundException ex) {
            }
            
            
            wizardDescriptor.setTargetName("report.jrxml"); // NOI18N
            
            
//            NewJrxmlWizardIterator wIterator = new NewJrxmlWizardIterator();
//            wIterator.initialize(wizardDescriptor);
//            wizardDescriptor.setPanelsAndSettings(wIterator, wizardDescriptor);

            // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
            //wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
            //wizardDescriptor.setTitle("New report");
            Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
            dialog.setVisible(true);
            dialog.toFront();
//
//            System.out.println(wizardDescriptor.getValue() + " " + WizardDescriptor.FINISH_OPTION);
//            System.out.flush();
//            boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
//            if (!cancelled) {
//                Set objects = wizardDescriptor.getInstantiatedObjects();
//                if (objects != null)
//                {
//                    Iterator iter = objects.iterator();
//                    int i=0;
//                    while (iter.hasNext())
//                    {
//                         System.out.println("Object " + i);
//                         i++;
//                            System.out.flush();
//                        DataObject dob = (DataObject)iter.next();
//                        dob.getCookie(OpenCookie.class).open();
//                    }
//
//                    System.out.println("Objects " + i);
//                         i++;
//                            System.out.flush();
//                }
//                else
//                {
//                    System.out.println("Objects is null...");
//                            System.out.flush();
//                }
//
//            }

    
    }

    public String getName() {
        return I18n.getString( IReportStandaloneManager.class, "CTL_ReportWizardAction"); // NOI18N
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE); // NOI18N
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
