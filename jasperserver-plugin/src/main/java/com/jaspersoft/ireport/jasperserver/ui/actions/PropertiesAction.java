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
import com.jaspersoft.ireport.jasperserver.RepositoryFile;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.jasperserver.RepositoryReportUnit;
import com.jaspersoft.ireport.jasperserver.ui.resources.ObjectPropertiesDialog;
import com.jaspersoft.ireport.jasperserver.ui.ServerDialog;
import com.jaspersoft.ireport.jasperserver.ui.nodes.FileNode;
import com.jaspersoft.ireport.jasperserver.ui.nodes.FolderNode;
import com.jaspersoft.ireport.jasperserver.ui.nodes.ReportUnitNode;
import com.jaspersoft.ireport.jasperserver.ui.nodes.ResourceNode;
import com.jaspersoft.ireport.jasperserver.ui.resources.DataSourceDialog;
import com.jaspersoft.ireport.jasperserver.ui.resources.DataTypeDialog;
import com.jaspersoft.ireport.jasperserver.ui.resources.InputControlDialog;
import com.jaspersoft.ireport.jasperserver.ui.resources.ListOfValuesDialog;
import com.jaspersoft.ireport.jasperserver.ui.resources.QueryDialog;
import com.jaspersoft.ireport.jasperserver.ui.resources.ReportOptionsDialog;
import com.jaspersoft.ireport.jasperserver.ui.resources.ReportUnitDialog;
import com.jaspersoft.ireport.jasperserver.ui.resources.ResourceReferenceDialog;
import com.jaspersoft.ireport.jasperserver.ui.resources.XMLAConnectionDialog;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.util.List;
import javax.swing.JOptionPane;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

public final class PropertiesAction extends NodeAction {

    public String getName() {
        return NbBundle.getMessage(PropertiesAction.class, "CTL_PropertiesAction");
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
        
        if (activatedNodes[0] instanceof FolderNode)
        {
             if (rf.isRoot())
             {
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
                    node.updateDisplayName();
                }
             }
             else
             {
                ObjectPropertiesDialog opd = new ObjectPropertiesDialog(Misc.getMainFrame(), true);
                opd.setResource( rf  );
                opd.setVisible( true );

                if (opd.getDialogResult() == JOptionPane.OK_OPTION)
                {
                    // Probably we shuld use a better event driven mechanism to
                    // update the ui....
                    node.updateDisplayName();
                }
             }
             
         }
        else if (rf instanceof RepositoryReportUnit)
        {
             RepositoryReportUnit rru = (RepositoryReportUnit)rf;
             ReportUnitDialog rrd = new ReportUnitDialog(Misc.getMainFrame(), true);
             rrd.setParentFolder( rru.getDescriptor().getParentFolder() );
             rrd.setServer(rru.getServer());
            
             rrd.setControlsSupportActive(false);
             rrd.setResourcesSupportActive(false);
             
             List datasources = null;
             if (JasperServerManager.getMainInstance().getBrandingProperties().getProperty("ireport.manage.datasources.enabled", "true").equals("true"))
             {
                try {
                       datasources = rru.getServer().getWSClient().listDatasources(); 
                       rrd.setDatasources(datasources);
                } catch (Exception ex)
                {

                   //JOptionPane.showMessageDialog(Misc.getMainFrame(),
                   //        JasperServerManager.getFormattedString("repositoryExplorer.message.errorListingDatasources", "Error getting the list of available datasources:\n{0}", new Object[] {ex.getMessage()}));
                   //ex.printStackTrace();
                }
             }
             
             rrd.setResource(rru);
             rrd.setVisible( true );
             
             if (rrd.getDialogResult() == JOptionPane.OK_OPTION)
             {
                // Probably we shuld use a better event driven mechanism to
                // update the ui....
                node.updateDisplayName();
             }
         }
        else if (rf instanceof RepositoryFile)
         {
            if (rf.getDescriptor().getWsType().equals( ResourceDescriptor.TYPE_DATASOURCE_JDBC) ||
                rf.getDescriptor().getWsType().equals( ResourceDescriptor.TYPE_DATASOURCE_JNDI) ||
                rf.getDescriptor().getWsType().equals( ResourceDescriptor.TYPE_DATASOURCE_BEAN) ||
                rf.getDescriptor().getWsType().equals( ResourceDescriptor.TYPE_DATASOURCE_VIRTUAL) ||
                (rf.getDescriptor().getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_CUSTOM) &&
                 rf.getDescriptor().getResourcePropertyValue(ResourceDescriptor.PROP_DATASOURCE_CUSTOM_SERVICE_CLASS) != null &&
                 ( rf.getDescriptor().getResourcePropertyValue(ResourceDescriptor.PROP_DATASOURCE_CUSTOM_SERVICE_CLASS).equals("com.jaspersoft.hadoop.hive.jasperserver.HiveDataSourceService") ||
                   rf.getDescriptor().getResourcePropertyValue(ResourceDescriptor.PROP_DATASOURCE_CUSTOM_SERVICE_CLASS).equals("com.jaspersoft.mongodb.jasperserver.MongoDbDataSourceService"))    
                )
               )
            {
                DataSourceDialog dsd = new DataSourceDialog(Misc.getMainFrame(), true);
                dsd.setParentFolder( rf.getDescriptor().getParentFolder() );
                dsd.setServer(rf.getServer());
                dsd.setResource(rf);
                dsd.setVisible( true );

                if (dsd.getDialogResult() == JOptionPane.OK_OPTION)
                {
                    node.updateDisplayName();
                }

            }
            else if (rf.getDescriptor().getWsType().equals( ResourceDescriptor.TYPE_OLAP_XMLA_CONNECTION))
            {
                XMLAConnectionDialog dsd = new XMLAConnectionDialog(Misc.getMainFrame(), true);
                dsd.setParentFolder( rf.getDescriptor().getParentFolder() );
                dsd.setServer(rf.getServer());
                dsd.setResource(rf);
                dsd.setVisible( true );

                if (dsd.getDialogResult() == JOptionPane.OK_OPTION)
                {
                    node.updateDisplayName();
                }

            }
            else if (rf.getDescriptor().getWsType().equals( ResourceDescriptor.TYPE_REFERENCE))
            {
                ResourceReferenceDialog rrd = new ResourceReferenceDialog(Misc.getMainFrame(), true);
                rrd.setParentFolder( rf.getDescriptor().getParentFolder() );
                rrd.setServer(rf.getServer());
                rrd.setResource(rf);
                rrd.setVisible( true );

                if (rrd.getDialogResult() == JOptionPane.OK_OPTION)
                {
                    node.updateDisplayName();
                }
            }
            else if (rf.getDescriptor().getWsType().equals( ResourceDescriptor.TYPE_DATA_TYPE))
            {
                DataTypeDialog rrd = new DataTypeDialog(Misc.getMainFrame(), true);
                rrd.setParentFolder( rf.getDescriptor().getParentFolder() );
                rrd.setServer(rf.getServer());
                rrd.setResource(rf);
                rrd.setVisible( true );

                if (rrd.getDialogResult() == JOptionPane.OK_OPTION)
                {
                    node.updateDisplayName();
                }
            }
            else if (rf.getDescriptor().getWsType().equals( ResourceDescriptor.TYPE_LOV))
            {
                ListOfValuesDialog rrd = new ListOfValuesDialog(Misc.getMainFrame(), true);
                rrd.setParentFolder( rf.getDescriptor().getParentFolder() );
                rrd.setServer(rf.getServer());
                rrd.setResource(rf);
                rrd.setVisible( true );

                if (rrd.getDialogResult() == JOptionPane.OK_OPTION)
                {
                    node.updateDisplayName();
                }
            }
            else if (rf.getDescriptor().getWsType().equals( ResourceDescriptor.TYPE_QUERY))
            {
                QueryDialog rrd = new QueryDialog(Misc.getMainFrame(), true);
                rrd.setParentFolder( rf.getDescriptor().getParentFolder());
                rrd.setServer(rf.getServer());
                
                List datasources = null;
                if (JasperServerManager.getMainInstance().getBrandingProperties().getProperty("ireport.manage.datasources.enabled", "true").equals("true"))
                {
                    try {
                           datasources = rf.getServer().getWSClient().listDatasources(); 
                           rrd.setDatasources(datasources);
                    } catch (Exception ex)
                    {

                       JOptionPane.showMessageDialog(Misc.getMainFrame(),
                               JasperServerManager.getFormattedString("repositoryExplorer.message.errorListingDatasources", "Error getting the list of available datasources:\n{0}", new Object[] {ex.getMessage()}));
                       ex.printStackTrace();
                    }
                }
                rrd.setResource(rf);
                rrd.setVisible( true );

                if (rrd.getDialogResult() == JOptionPane.OK_OPTION)
                {
                    node.updateDisplayName();
                }
            }
            else if (rf.getDescriptor().getWsType().equals( ResourceDescriptor.TYPE_INPUT_CONTROL))
            {
                InputControlDialog rrd = new InputControlDialog(Misc.getMainFrame(), true);
                rrd.setParentFolder( rf.getDescriptor().getParentFolder() );
                rrd.setServer(rf.getServer());
                rrd.setResource(rf);
                rrd.setVisible( true );

                if (rrd.getDialogResult() == JOptionPane.OK_OPTION)
                {
                    node.updateDisplayName();
                }
            }
            else if (rf.getDescriptor().getWsType().equals( "ReportOptionsResource"))
            {
                ReportOptionsDialog rrd = new ReportOptionsDialog(Misc.getMainFrame(), true);
                rrd.setParentFolder( rf.getDescriptor().getParentFolder() );
                rrd.setServer(rf.getServer());
                rrd.setResource(rf);
                rrd.setVisible( true );

                if (rrd.getDialogResult() == JOptionPane.OK_OPTION)
                {
                    node.updateDisplayName();
                }
            }
            else
            {
                ObjectPropertiesDialog opd = new ObjectPropertiesDialog(Misc.getMainFrame(), true);
                opd.setResource( rf  );
                opd.setVisible( true );

                if (opd.getDialogResult() == JOptionPane.OK_OPTION)
                {
                    node.updateDisplayName();
                }
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