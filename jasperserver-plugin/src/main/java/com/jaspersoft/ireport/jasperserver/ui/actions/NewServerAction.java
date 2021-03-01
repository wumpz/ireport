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
package com.jaspersoft.ireport.jasperserver.ui.actions;

import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.ui.ServerDialog;
import com.jaspersoft.ireport.jasperserver.ui.nodes.ResourceNode;
import com.jaspersoft.ireport.jasperserver.ui.nodes.ServerChildren;
import javax.swing.JOptionPane;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

public final class NewServerAction extends NodeAction {

    public String getName() {
        return NbBundle.getMessage(NewServerAction.class, "CTL_NewServerAction");
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

    protected void performAction(org.openide.nodes.Node[] activatedNodes) {
        
        ServerDialog sd = new ServerDialog(Misc.getMainFrame(), true);
        sd.setVisible(true);
        if (sd.getDialogResult() == JOptionPane.OK_OPTION) {
            JasperServerManager.getMainInstance().getJServers().add(sd.getJServer());
            JasperServerManager.getMainInstance().saveConfiguration();
            
            Node root = activatedNodes[0].getParentNode();
            while (root.getParentNode() != null) root = root.getParentNode();
            if (root.getChildren() instanceof ServerChildren)
            {
                ((ServerChildren)root.getChildren()).recalculateKeys();
            }
        }
        
        
    }

    // Check all the selected nodes are servers or their children...
    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        return true;
    }
}