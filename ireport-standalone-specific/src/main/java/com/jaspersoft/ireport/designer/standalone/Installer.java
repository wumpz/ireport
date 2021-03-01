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
package com.jaspersoft.ireport.designer.standalone;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.standalone.actions.ImportSettingsAction;
import com.jaspersoft.ireport.designer.standalone.actions.ImportSettingsUtilities;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.modules.ModuleInstall;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.windows.TopComponent.Registry;
import org.openide.windows.WindowManager;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    ModuleInstall subInstaller = null;
    
    @Override
    public void restored() {
        
        
        if (Utilities.isMac())
        {
            subInstaller = new org.netbeans.modules.applemenu.Install();
            subInstaller.restored();
            
        }
        // By default, do nothing.
        // Put your startup code here.
        WindowManager.getDefault().getRegistry().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals( Registry.PROP_TC_CLOSED))
                {
                    System.gc();
                }
            }
        });

        // Import settings...
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

            public void run() {
                String[] versions = ImportSettingsUtilities.getAvailableVersions();
                if (versions != null && versions.length > 0 && IReportManager.getPreferences().getBoolean("askForImportingSettings", true))
                {
                    try {
                        SystemAction.get(ImportSettingsAction.class).performAction();
                    } catch (Exception ex)
                    {}
                    IReportManager.getPreferences().putBoolean("askForImportingSettings", false);
                }
            }
        });
    }
    
    
    @Override
    public void uninstalled()
    {
        if (Utilities.isMac() && subInstaller != null)
        {
            subInstaller.uninstalled();
        }
    }
    
}
