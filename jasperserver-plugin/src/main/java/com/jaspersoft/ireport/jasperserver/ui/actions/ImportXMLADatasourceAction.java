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

import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.connection.JDBCConnection;
import com.jaspersoft.ireport.designer.connection.JRXMLADataSourceConnection;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.ui.nodes.ResourceNode;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

public final class ImportXMLADatasourceAction extends NodeAction {


    
    public String getName() {
        return NbBundle.getMessage(ImportXMLADatasourceAction.class, "CTL_ImportXMLADatasourceAction");
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
        
        for (int i=0; i<activatedNodes.length; ++i)
        {
            final Node node = activatedNodes[i];
            ResourceDescriptor rd = ((ResourceNode)node).getResourceDescriptor();
            int ds_count = 0;
            if (rd.getWsType().equals(ResourceDescriptor.TYPE_OLAP_XMLA_CONNECTION))
            {
                    boolean skipDs = false;
                    List<IReportConnection> conns = IReportManager.getInstance().getConnections();

                     int index = -1;
                     for (IReportConnection con : conns)
                     {
                         if (con.getName().equals( rd.getLabel() ))
                         {
                             if (JOptionPane.showConfirmDialog(Misc.getMainFrame(),
                                     JasperServerManager.getFormattedString("repositoryExplorer.message.duplicatedConnectionName",
                                     "A connection named {0} is already present.\nWould you like to replace the existing connection?",
                                     new Object[]{rd.getLabel()})) != JOptionPane.OK_OPTION)
                             {
                                 skipDs=true;
                                 break;
                             }
                             else
                             {
                                 IReportManager.getInstance().removeConnection(con);
                                 break;
                             }
                         }
                     }

                     if (skipDs) continue;

                     JRXMLADataSourceConnection conn = new JRXMLADataSourceConnection();
                     conn.setName(rd.getLabel());
                     conn.setUsername(rd.getResourcePropertyValue( ResourceDescriptor.PROP_XMLA_USERNAME ) );
                     conn.setPassword( rd.getResourcePropertyValue( ResourceDescriptor.PROP_XMLA_PASSWORD ) );
                     conn.setUrl( rd.getResourcePropertyValue( ResourceDescriptor.PROP_XMLA_URI ) );
                     conn.setCatalog( rd.getResourcePropertyValue( ResourceDescriptor.PROP_XMLA_CATALOG ) );
                     conn.setDatasource( rd.getResourcePropertyValue( ResourceDescriptor.PROP_XMLA_DATASOURCE ) );

                     IReportManager.getInstance().addConnection(conn);

                     IReportManager.getInstance().setDefaultConnection(conn);
                     ds_count++;
            }

            if (ds_count > 0)
            {
                JOptionPane.showMessageDialog(Misc.getMainFrame(), JasperServerManager.getFormattedString("repositoryExplorer.message.connectionImported",
                                     "{0} connection(s) succefully imported in iReport.", new Object[]{ds_count}),"",JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
        
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length < 1) return false;
        
        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (!(activatedNodes[i] instanceof ResourceNode))
            {
                return false;
            }
            
            ResourceNode node = (ResourceNode)activatedNodes[i];
            if (node.getResourceDescriptor() == null ||
                !node.getResourceDescriptor().getWsType().equals(ResourceDescriptor.TYPE_OLAP_XMLA_CONNECTION))
            {
                return false;
            }
        }
        
        return true;
    }

    
}