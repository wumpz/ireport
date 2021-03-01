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
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.jasperserver.RepositoryReportUnit;
import com.jaspersoft.ireport.jasperserver.ui.ResourceChooser;
import com.jaspersoft.ireport.jasperserver.ui.nodes.ReportUnitInputControlsNode;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.util.List;
import javax.swing.JOptionPane;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

public final class AddExistingInputControlAction extends NodeAction {

    public String getName() {
        return NbBundle.getMessage(AddExistingInputControlAction.class, "CTL_AddExistingInputControlAction");
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

        if (activatedNodes == null || activatedNodes.length != 1) return;

        if (activatedNodes[0] instanceof ReportUnitInputControlsNode)
        {
            ReportUnitInputControlsNode ruicn = (ReportUnitInputControlsNode)activatedNodes[0];
            RepositoryReportUnit ru = ruicn.getReportUnit();

            // In this case we choose an input control from the repository....
            ResourceChooser rc = new ResourceChooser();
            rc.setMultipleSelection(true);

            String reportUnitUri = null;
            reportUnitUri = ru.getDescriptor().getUriString();
            rc.setServer( ru.getServer() );

            if (rc.showDialog(Misc.getMainFrame(), null) == JOptionPane.OK_OPTION)
            {
                List<ResourceDescriptor> rds = rc.getSelectedDescriptors();
                if (rds.size() == 0)
                {
                    return;
                }

                for (ResourceDescriptor rd : rds)
                {
                    if (!rd.getWsType().equals( ResourceDescriptor.TYPE_INPUT_CONTROL))
                    {
                        JOptionPane.showMessageDialog(Misc.getMainFrame(),
                                JasperServerManager.getFormattedString("repositoryExplorer.message.invalidInputControl","{0} is not an Input Control!",new Object[]{rd.getName()}),
                                     "",JOptionPane.ERROR_MESSAGE);
                        continue;
                    }


                    ResourceDescriptor newRd = new ResourceDescriptor();
                    newRd.setWsType( ResourceDescriptor.TYPE_INPUT_CONTROL);
                    newRd.setIsReference(true);
                    newRd.setReferenceUri( rd.getUriString() );
                    newRd.setIsNew(true);
                    newRd.setUriString(reportUnitUri+"/<cotnrols>");
                    try {
                        newRd = ru.getServer().getWSClient().modifyReportUnitResource(reportUnitUri, newRd, null);

                        RepositoryFolder obj = RepositoryFolder.createRepositoryObject(ru.getServer(), newRd);
                        if (ruicn.getRepositoryObject().isLoaded())
                        {
                            ruicn.getResourceDescriptor().getChildren().add( newRd );
                            ruicn.getRepositoryObject().getChildren().add(obj);
                            ruicn.refreshChildrens(false);
                        }

                    } catch (Exception ex)
                    {
                        JOptionPane.showMessageDialog(Misc.getMainFrame(),JasperServerManager.getFormattedString("messages.error.3", "Error:\n {0}", new Object[] {ex.getMessage()}));
                        ex.printStackTrace();
                    }
                }
            }

        }
        
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length != 1) return false;


        if (!(activatedNodes[0] instanceof ReportUnitInputControlsNode))
        {
                return false;
        }
        
        return true;
    }

    
}