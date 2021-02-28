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
package com.jaspersoft.ireport.designer.menu;

import com.jaspersoft.ireport.designer.IReportManager;
import java.io.File;
import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.loaders.DataShadow;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public final class OpenReportDirectoryInFavoritesAction extends NodeAction {

    private static DataFolder getFavoritesFolder() {
        try {
            FileObject fo = FileUtil.createFolder (
                Repository.getDefault().getDefaultFileSystem().getRoot(),
                "Favorites" // NOI18N
            );
            DataFolder folder = DataFolder.findFolder(fo);
            return folder;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            return DataFolder.findFolder (
                Repository.getDefault().getDefaultFileSystem().getRoot()
            );
        }

    }

    private boolean isInFavorites (FileObject fo) {
            DataFolder f = getFavoritesFolder();

            DataObject [] arr = f.getChildren();
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] instanceof DataShadow) {
                    if (fo.equals(((DataShadow) arr[i]).getOriginal().getPrimaryFile())) {
                        return true;
                    }
                }
            }
            return false;
        }

    @Override
    protected void performAction(Node[] nodes) {
        try {

            File directory = FileUtil.toFile(IReportManager.getInstance().getActiveVisualView().getEditorSupport().getDataObject().getPrimaryFile());
            directory = directory.getParentFile();
            FileObject dirFO = FileUtil.toFileObject(directory);
            DataObject dirDO = DataObject.find(dirFO);
            // Add the directory to the favorites folder...
            if (!isInFavorites(dirFO))
            {
                DataShadow createdDO = dirDO.createShadow(getFavoritesFolder());
            }

            TopComponent win = WindowManager.getDefault().findTopComponent("favorites");
            if (win != null) {
                win.open();
                win.requestActive();
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    public String getName() {
        return NbBundle.getMessage(OpenReportDirectoryInFavoritesAction.class, "CTL_OpenReportDirectoryInFavoritesAction");
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }


    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected boolean enable(Node[] nodes) {
        return IReportManager.getInstance().getActiveVisualView() != null;
    }
}