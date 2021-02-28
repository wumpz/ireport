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
package com.jaspersoft.ireport.designer.palette.actions;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.crosstab.CrosstabObjectScene;
import com.jaspersoft.ireport.designer.subreport.SubreportTemplateWizard;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Dialog;
import java.io.File;
import java.text.MessageFormat;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.util.Mutex;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class CreateSubreportAction extends CreateReportElementAction 
{

    public JRDesignElement createReportElement(JasperDesign jd)
    {
        if (getScene() instanceof CrosstabObjectScene)
        {
            Runnable r = new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(Misc.getMainFrame(), "You can not use a subreport inside a crosstab","Error", JOptionPane.WARNING_MESSAGE);
                }
            };
            
            Mutex.EVENT.readAccess(r); 
            return null;
        }
        
        JRDesignElement element = null;

        SubreportTemplateWizard wizardDescriptor = new SubreportTemplateWizard();
        
        try {
            FileObject fo = IReportManager.getInstance().getActiveVisualView().getEditorSupport().getDataObject().getPrimaryFile();
            DataFolder df = DataFolder.findFolder(FileUtil.toFileObject(FileUtil.toFile(fo).getParentFile()));
            wizardDescriptor.setTargetFolder(df);
            
            // Try to create a potential subreprt name...
            String fname = fo.getName();
            
            for (int i=1; i<100; ++i)
            {
                File f = new File( Misc.getDataFolderPath(df), fname + "_subreport" + i + ".jrxml");
                if (f.exists()) continue;

                wizardDescriptor.setTargetName( fname + "_subreport" + i);
                break;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // {0} will be replaced by WizardDescriptor.Panel.getComponent().getName()
        // {1} will be replaced by WizardDescriptor.Iterator.name()
        wizardDescriptor.setTitleFormat(new MessageFormat("{0} ({1})"));
        wizardDescriptor.setTitle("Subreport wizard");
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();

        
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {

            element = wizardDescriptor.getElement();
            if (element.getWidth() == 0) element.setWidth(200);
            if (element.getHeight() == 0) element.setHeight(100);
        }
        
        return element;
    }
    
}
