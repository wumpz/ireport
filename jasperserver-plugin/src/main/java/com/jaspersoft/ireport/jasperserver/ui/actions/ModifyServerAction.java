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
import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.jasperserver.ui.ServerDialog;
import com.jaspersoft.ireport.jasperserver.ui.nodes.ResourceNode;
import javax.swing.JOptionPane;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

public final class ModifyServerAction extends NodeAction {

    public String getName() {
        return NbBundle.getMessage(ModifyServerAction.class, "CTL_ModifyServerAction");
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
        
        if (!(activatedNodes[0] instanceof ResourceNode)) return;
        
        ResourceNode node = (ResourceNode)activatedNodes[0];
        RepositoryFolder rf = ((ResourceNode)activatedNodes[0]).getRepositoryObject();
        
        JServer server = rf.getServer();
        ServerDialog sd = new ServerDialog(Misc.getMainFrame(), true);
        sd.setJServer( server );
        sd.setVisible(true);
        if (sd.getDialogResult() == JOptionPane.OK_OPTION)
        {
            server.setName( sd.getJServer().getName());
            server.setUsername( sd.getJServer().getUsername() );
            server.setPassword( sd.getJServer().getPassword() );
            server.setUrl( sd.getJServer().getUrl() );

            // We should update the underline folder used to represent the root...
            rf.getDescriptor().setLabel(server.getName());
            JasperServerManager.getMainInstance().saveConfiguration();
            // Find the root of this server...
            
            Node theNode = activatedNodes[0];
            while (theNode.getParentNode() != null)
            {
                if (node instanceof ResourceNode)
                {
                    node = (ResourceNode)theNode;
                    if (node.getRepositoryObject().isRoot())
                    {
                        node.updateDisplayName();
                        break;
                    }
                }
            
                theNode = theNode.getParentNode();
            }
        }
     
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length != 1) return false;
        if ( activatedNodes[0] instanceof ResourceNode)
        {
            return true;
        }
        return false;
    }
}