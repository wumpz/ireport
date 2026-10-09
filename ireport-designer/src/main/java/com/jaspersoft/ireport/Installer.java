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
package com.jaspersoft.ireport;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.Repository;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;
import org.openide.windows.WindowManager;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {

        
        // JAXP uses the platform provider; do not change the IDE-wide XML factories.
        // Explicit opt-in diagnostics for an isolated development profile.
        final String diagnosticFile = System.getProperty("ireport.diagnostics.file");
        if (diagnosticFile != null) {
            WindowManager.getDefault().invokeWhenUIReady(() -> {
                try {
                    FileObject file = FileUtil.toFileObject(FileUtil.normalizeFile(new java.io.File(diagnosticFile)));
                    org.openide.loaders.DataObject object = org.openide.loaders.DataObject.find(file);
                    org.openide.cookies.OpenCookie cookie = object.getLookup().lookup(org.openide.cookies.OpenCookie.class);
                    java.util.logging.Logger.getLogger(Installer.class.getName()).info(
                        "iReport diagnostic: mime=" + file.getMIMEType() + ", object=" + object.getClass().getName() + ", open=" + cookie);
                    if (cookie != null) cookie.open();
                    javax.swing.SwingUtilities.invokeLater(() -> java.util.logging.Logger.getLogger(Installer.class.getName()).info(
                        "iReport diagnostic opened components: " + WindowManager.getDefault().getRegistry().getOpened()));
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            });
        }

        /*
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

            public void dump(String f)
            {
               
                FileObject nodeFP = Repository.getDefault().getDefaultFileSystem().getRoot().getFileObject(f);
                System.out.println(" Dumping " + f + " -----------------------------");
                if (nodeFP != null)
                {
                    try {
                        System.out.println("URL: " + nodeFP.getURL());
                    } catch (FileStateInvalidException ex) {
                        Exceptions.printStackTrace(ex);
                    }

                    try {
                        FileUtil.copy(nodeFP.getInputStream(), System.out);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }

                    System.out.println(f + " END -----------------------------");
                    System.out.flush();
                }
                else
                {
                    System.out.println(f + " NOT FOND -----------------------------");
                }
            }
            public void run() {

                dump("Windows2Local/WindowManager.wswmgr");
                dump("Windows2/WindowManager.wswmgr");
                dump("Windows2Local/Modes/explorer/runtime.wstcref");
                dump("Windows2/Modes/explorer/runtime.wstcref");
            }
        });

         */


    }
}
