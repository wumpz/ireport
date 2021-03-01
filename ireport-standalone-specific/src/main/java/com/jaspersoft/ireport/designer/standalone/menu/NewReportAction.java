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
import com.jaspersoft.ireport.designer.templates.TemplatesFrame;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.wizards.CustomTemplateWizard;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.io.File;
import java.util.Enumeration;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.openide.DialogDisplayer;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.loaders.TemplateWizard;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;

public final class NewReportAction extends CallableSystemAction {


    static final String TEMPLATES_DIR="Templates/Report/";
    private FileObject nodesFileObject = null;

    private JMenu menu = null;

    public void performAction(String targetName, String templateName) {
      
            TemplateWizard wizardDescriptor = new CustomTemplateWizard();

            //wizardDescriptor.putProperty("useCustomChooserPanel", "true"); // NOI18N
            File targetFolder = Misc.findStartingDirectory();
            DataFolder df = DataFolder.findFolder(FileUtil.toFileObject(targetFolder));
            wizardDescriptor.setTargetFolder(df);
            wizardDescriptor.setTargetName(targetName); // NOI18N
            

            try {
                FileObject templateFileObject = Repository.getDefault().getDefaultFileSystem().getRoot().getFileObject(templateName);
                wizardDescriptor.setTemplate(DataObject.find(templateFileObject));
            } catch (DataObjectNotFoundException ex) {
            }
            
//            EmptyReportWizardIterator wIterator = new EmptyReportWizardIterator();
//            wIterator.initialize(wizardDescriptor);
//            wizardDescriptor.setPanelsAndSettings(wIterator, wizardDescriptor);

            // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
//            wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));

            wizardDescriptor.setTitle("New"); 
            Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
            dialog.setVisible(true);
            dialog.toFront();
         
    }

    public String getName() {
        return I18n.getString( IReportStandaloneManager.class, "CTL_NewReportAction"); // NOI18N
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

    /*
    @Override
    public JMenuItem getMenuPresenter() {
        
        refreshMenu();

        return menu;
    }
    */

    public void refreshMenu()
    {

        if (menu == null)
        {
            menu = new JMenu(I18n.getString( IReportStandaloneManager.class, "CTL_NewReportAction"));
            if (nodesFileObject == null)
            {
                nodesFileObject = Repository.getDefault().getDefaultFileSystem().getRoot().getFileObject(TEMPLATES_DIR);
                
                if (nodesFileObject != null)
                {
                    nodesFileObject.addFileChangeListener(new FileChangeListener() {

                        public void fileFolderCreated(FileEvent arg0) {
                        }

                        public void fileDataCreated(FileEvent arg0) {
                        }

                        public void fileChanged(FileEvent arg0) {
                            refreshMenu();
                        }

                        public void fileDeleted(FileEvent arg0) {
                            refreshMenu();
                        }

                        public void fileRenamed(FileRenameEvent arg0) {
                            refreshMenu();
                        }

                        public void fileAttributeChanged(FileAttributeEvent arg0) {
                        }
                    });
                }
            }
        }

        if (menu == null) return;
        
        menu.removeAll();
        // Load all the templates available under Templates/Report....
        
        if (nodesFileObject == null) return;
        DataFolder nodesDataFolder = DataFolder.findFolder(nodesFileObject);
        if (nodesDataFolder == null) return;

        Enumeration<DataObject> enObj = nodesDataFolder.children();
        while (enObj.hasMoreElements())
        {
            DataObject dataObject = enObj.nextElement();
            FileObject fileObject = dataObject.getPrimaryFile();
            final String name = fileObject.getNameExt();

            final String filePath= TEMPLATES_DIR + fileObject.getNameExt();

            JMenuItem subMenu = new JMenuItem(I18n.getString(filePath));
            subMenu.setIcon( new ImageIcon( dataObject.getNodeDelegate().getIcon( BeanInfo.ICON_COLOR_16x16)));
            subMenu.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    performAction(name,filePath);
                }
            });

            menu.add(subMenu);
        }
    }



    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public void performAction() {
        // do nothing...
        TemplatesFrame td = new TemplatesFrame(Misc.getMainFrame(), true);
        td.setVisible(true);
    }
}
