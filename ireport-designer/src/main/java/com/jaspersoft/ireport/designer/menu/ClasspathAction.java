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

import com.jaspersoft.ireport.designer.tools.ClassPathDialog;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.Enumeration;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class ClasspathAction extends CallableSystemAction {

    public void performAction() {
        
        Window pWin = Misc.getMainWindow();
        ClassPathDialog cpd = null;
        if (pWin instanceof Dialog) cpd = new ClassPathDialog((Dialog)pWin, true);
        else if (pWin instanceof Frame) cpd = new ClassPathDialog((Frame)pWin, true);
        else cpd = new ClassPathDialog((Dialog)null, true);
        
        
        //ClassPathDialog cpd = new ClassPathDialog(, true);
        
        cpd.setClasspath( IReportManager.getInstance().getClasspath() );
        cpd.setVisible(true);
        if (cpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION )
        {
            IReportManager.getInstance().setClasspath( cpd.getClasspath() );
        }
        
    }

    public String getName() {
        return NbBundle.getMessage(ClasspathAction.class, "CTL_ClasspathAction");
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}