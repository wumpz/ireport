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

import com.jaspersoft.ireport.designer.connection.gui.ConnectionsDialog;
import com.jaspersoft.ireport.designer.tools.ClassPathDialog;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class DatasourcesAction extends CallableSystemAction {

    public void performAction() {
        
        Window pWin = Misc.getMainWindow();
        ConnectionsDialog connectionsDialog = null;
        if (pWin instanceof Dialog) connectionsDialog = new ConnectionsDialog((Dialog)pWin, true);
        else if (pWin instanceof Frame) connectionsDialog = new ConnectionsDialog((Frame)pWin, true);
        else connectionsDialog = new ConnectionsDialog((Dialog)null, true);
        
        connectionsDialog.setVisible(true);
    }

    public String getName() {
        return NbBundle.getMessage(DatasourcesAction.class, "CTL_DatasourcesAction");
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.FALSE);
    }

    /** The action's icon location.  
     * @return the action's icon location  
     */  
    protected String iconResource () {  
      return "com/jaspersoft/ireport/designer/menu/datasources.png"; // NOI18N  
    }  


    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
    
}