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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.standalone.IReportStandaloneManager;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.io.File;
import javax.swing.JFileChooser;
import org.openide.ErrorManager;
import org.openide.cookies.EditCookie;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.HelpCtx;
import org.openide.util.UserCancelException;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.WindowManager;

/**
 *
 * @author gtoffoli
 */
public class OpenFileAction extends CallableSystemAction {

	public OpenFileAction() {
		putValue("noIconInMenu", Boolean.TRUE); // NOI18N
	}
	
	public String getName() {
		return I18n.getString( IReportStandaloneManager.class, "CTL_OpenFileAction");
	}

	public HelpCtx getHelpCtx() {
		return new HelpCtx(OpenFileAction.class);
	}

	protected String iconResource() {
		return "com/transposix/nbext/openFile.png"; // NOI18N
	}

	/**
	 * Creates and initializes a file chooser.
	 *
	 * @return  the initialized file chooser
	 */
	protected JFileChooser prepareFileChooser() {
		JFileChooser chooser = new JFileChooser();
		File currDir = Misc.findStartingDirectory();
		FileUtil.preventFileChooserSymlinkTraversal(chooser, currDir);
		HelpCtx.setHelpIDString(chooser, getHelpCtx().getHelpID());

        FileFilterAdapter mainFilter = new FileFilterAdapter(".jrxml", "JasperReports Template (.jrxml)");
		chooser.addChoosableFileFilter(mainFilter);
        chooser.addChoosableFileFilter(new FileFilterAdapter(".jrctx", "Chart Theme file (.jrctx)"));
        chooser.addChoosableFileFilter(new FileFilterAdapter(".jrtx", "Style Template file (.jrtx)"));
        chooser.addChoosableFileFilter(new FileFilterAdapter(".properties", "Resource Bundle (.properties)"));
        chooser.addChoosableFileFilter(new FileFilterAdapter(".jasper", "Compiled JasperReport (.jasper)"));

        chooser.setFileFilter(mainFilter);

		chooser.setMultiSelectionEnabled(true);
		return chooser;
	}
	
	/**
	 * Displays the specified file chooser and returns a list of selected files.
	 *
	 * @param  chooser  file chooser to display
	 * @return  array of selected files,
	 * @exception  org.openide.util.UserCancelException
	 *                     if the user cancelled the operation
	 */
	public static File[] chooseFilesToOpen(JFileChooser chooser)
			throws UserCancelException {
		File[] files;
		do {
			int selectedOption = chooser.showOpenDialog(
				WindowManager.getDefault().getMainWindow());
			
			if (selectedOption != JFileChooser.APPROVE_OPTION) {
				throw new UserCancelException();
			}

			// If the chooser is not configured for multi files, then use
			// File file = chooser.getSelectedFile();
			files = chooser.getSelectedFiles();
		} while (files.length == 0);
		return files;
	}
	
	/**
	 * {@inheritDoc} Displays a file chooser dialog
	 * and opens the selected files.
	 */
	public void performAction() {
		 JFileChooser chooser = prepareFileChooser();
		 File[] files;
		 try {
			 files = chooseFilesToOpen(chooser);
		 } catch (UserCancelException ex) {
			 return;
		 }

		 for (int i = 0; i < files.length; i++) {
			// Small hack ... OpenFile is User Utilities, which is a private API
			//
			// OpenFile.openFile(files[i], -1);
			open(files[i]);
		}
                
                 // Save the last path used for the chooser..
                 if (files.length > 0)
                 {
                     String dir = files[0].getParent();
                     IReportManager.getPreferences().put( IReportManager.CURRENT_DIRECTORY, dir);
                 }
	}

	public static void open (File f) {
		FileObject fob = FileUtil.toFileObject(FileUtil.normalizeFile(f));
		if (fob == null)
		return;
		
                try {
			// the process succeeded 
			DataObject dob = DataObject.find (fob);
                        if (f.getName().toLowerCase().endsWith(".properties")) // NOI18N
                        {
                            EditCookie oc = dob.getCookie (EditCookie.class);
                            if (oc != null)
				oc.edit();
                        }
                        else
                        {
                            OpenCookie oc = dob.getCookie (OpenCookie.class);
			if (oc != null)
				oc.open();
                        }
			
		} catch (DataObjectNotFoundException ex) {
			 ErrorManager.getDefault().notify(ex);
                }
	}

	
	protected boolean asynchronous() {
		return false;
	}

	

}
