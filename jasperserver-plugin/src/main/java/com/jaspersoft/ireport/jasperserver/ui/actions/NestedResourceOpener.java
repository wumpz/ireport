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

import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.ui.JRViewerTopComponent;
import com.jaspersoft.ireport.jasperserver.ui.RepositoryTopComponent;
import com.jaspersoft.ireport.jasperserver.ui.nodes.FileNode;
import com.jaspersoft.ireport.jasperserver.ui.nodes.FolderChildren;
import com.jaspersoft.ireport.jasperserver.ui.nodes.FolderNode;
import com.jaspersoft.ireport.jasperserver.ui.nodes.ReportUnitNode;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.util.Exceptions;
import org.openide.util.actions.SystemAction;

/**
 * Given a repository folder node, the class opens the main jrxml.
 * Why do we need this class? When the report unit is published,
 * the repository view is not yet updated, but we may want to open
 * the main jrxml from the newly created report unit. This class
 * wait for the report unit to be loaded in the view, and then wait
 * for the FileResource node containing the jrxml be available.
 * When this happens, the jrxml is opened.
 *
 * @version $Id: NestedResourceOpener.java 0 2009-10-06 18:27:45 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class NestedResourceOpener implements NodeListener {

    FolderNode rootNode = null;
    ReportUnitNode reportUnitNode = null;
    boolean refreshedChildren = false;
    String ruUri = null;

    public void openFile()
    {

        rootNode.removeNodeListener(this);
        if (reportUnitNode == null)
        {
            

            Node[] nodes = rootNode.getChildren().getNodes();
            
            // Look for the report unit node...
            for (int i=0; i<nodes.length; ++i)
            {
                if (nodes[i] instanceof ReportUnitNode)
                {
                    ReportUnitNode ruNode = (ReportUnitNode)nodes[i];
                    if (ruNode.getResourceDescriptor().getUriString().equals(ruUri))
                    {
                        System.out.flush();
                        reportUnitNode = ruNode;
                        break;
                    }
                }
            }
            
            if (reportUnitNode == null)
            {
                // listen for node changes...
                rootNode.addNodeListener(this);
                rootNode.refreshChildrens(true);
                return;
            }
        
        }

        if (reportUnitNode != null)
        {
            reportUnitNode.removeNodeListener(this);

            System.out.println("Report Unit available!");
            System.out.flush();

            // Look for report unit node in the root node...
            Node[] nodes = reportUnitNode.getChildren().getNodes();
            for (int i=0; nodes != null && i<nodes.length; ++i)
            {
                // Check if this is the report unit we are interested into...
                if (nodes[i] instanceof FileNode)
                {
                    final FileNode fileNode = (FileNode)nodes[i];
                    if (fileNode.getResourceDescriptor().isMainReport())
                    {
                        System.out.println("Opening filenode: " + fileNode);
                        System.out.flush();

                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    try {
                                        RepositoryTopComponent.findInstance().getExplorerManager().setSelectedNodes(new Node[]{fileNode});
                                    } catch (PropertyVetoException ex) {
                                        Exceptions.printStackTrace(ex);
                                    }
                                    SystemAction.get(OpenFileAction.class).performAction(new Node[]{fileNode});
                                }
                            });
                        
                        
                        return;
                    }
                }
            }

            reportUnitNode.addNodeListener(this);
            reportUnitNode.refreshChildrens(true);
        }

    }

    public NestedResourceOpener(FolderNode node, String ruUri)
    {
        this.rootNode = node;
        this.ruUri = ruUri;
    }

    public void childrenAdded(NodeMemberEvent evt) {
        if (reportUnitNode == null)
        {
            Node[] nodes = evt.getDelta();
            for (int i=0; i<nodes.length; ++i)
            {
                if (nodes[i] instanceof ReportUnitNode)
                {
                    ReportUnitNode ruNode = (ReportUnitNode)nodes[i];
                    if (ruNode.getResourceDescriptor().getUriString().equals(ruUri))
                    {
                        reportUnitNode = ruNode;
                        rootNode.removeNodeListener(this); // just in case...
                        break;
                    }
                }
            }
            openFile();
            return;
        }


        if (reportUnitNode != null)
        {
            Node[] nodes = evt.getDelta();
            for (int i=0; nodes != null && i<nodes.length; ++i)
            {
                // Check if this is the report unit we are interested into...
                if (nodes[i] instanceof FileNode)
                {
                    final FileNode fileNode = (FileNode)nodes[i];
                    if (fileNode.getResourceDescriptor().isMainReport())
                    {
                        System.out.println("Opening filenode: " + fileNode);
                        System.out.flush();

                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    try {
                                        RepositoryTopComponent.findInstance().getExplorerManager().setSelectedNodes(new Node[]{fileNode});
                                    } catch (PropertyVetoException ex) {
                                        Exceptions.printStackTrace(ex);
                                    }
                                    SystemAction.get(OpenFileAction.class).performAction(new Node[]{fileNode});
                                }
                            });


                        return;
                    }
                }
            }
        }
    }

    public void childrenRemoved(NodeMemberEvent arg0) {}

    public void childrenReordered(NodeReorderEvent arg0) {}

    public void nodeDestroyed(NodeEvent arg0) {}

    public void propertyChange(PropertyChangeEvent evt) {}

}
