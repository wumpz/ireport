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
package com.jaspersoft.ireport.jasperserver.ui.nodes.dnd;

import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.ui.nodes.FolderNode;
import com.jaspersoft.ireport.jasperserver.ui.nodes.ResourceNode;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.util.datatransfer.PasteType;

/**
 *
 * @author gtoffoli
 */
public class FolderPasteType extends PasteType {

    private FolderNode destinationNode;
    private Node originNode;
    private int dropAction = 0;

    public static FolderPasteType createFolderPasteType(int dropAction, FolderNode toFolderNode, Node originNode)
    {
        if (toFolderNode == null || originNode == null) return null;

        if (originNode instanceof ResourceNode)
        {
            // 1. It is not possible to copy from two different servers...
            if (toFolderNode.getFolder().getServer() != ((ResourceNode)originNode).getRepositoryObject().getServer())
            {
                return null;
            }



            // 2. Check that we are not coping a parent in a child...
            String origin_uri = ((ResourceNode)originNode).getResourceDescriptor().getUriString();
            String destination_uri = toFolderNode.getResourceDescriptor().getUriString();

            
            if (origin_uri.equals("/")) return null;
            if (destination_uri.equals(origin_uri))
            {
                return null;
            }

            //if (!destination_uri.equals("/") && destination_uri.startsWith(origin_uri)) return null;



            //if (((ResourceNode)originNode).getResourceDescriptor().getParentFolder().equals(destination_uri)) return null;

            return new FolderPasteType(dropAction, toFolderNode, originNode);
        }
        return null;
    }


    protected FolderPasteType(int dropAction, FolderNode toFolderNode, Node originNode) {
        this.destinationNode = toFolderNode;
        this.originNode = originNode;
        this.dropAction = dropAction;
    }

    @SuppressWarnings("unchecked")
    public Transferable paste() throws IOException {

        ResourceNode origin = (ResourceNode)getOriginNode();
        ResourceDescriptor origin_rd = origin.getResourceDescriptor();

        Node originParent = getOriginNode().getParentNode();

        String origin_uri = origin.getResourceDescriptor().getUriString();
        String destination_uri = getDestinationNode().getResourceDescriptor().getUriString();


        Map<String,ResourceDescriptor> existingLabelsMap = new HashMap<String,ResourceDescriptor>();
        try {
            List<ResourceDescriptor>  newFolderChildrens = origin.getRepositoryObject().getServer().getWSClient().list(getDestinationNode().getResourceDescriptor());
            for (ResourceDescriptor rd : newFolderChildrens)
            {
               existingLabelsMap.put(rd.getLabel(), rd);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            // ignore errors on listing content of the dest directory...
            // in the worst case rename of the resource will not occur.
        }


        if ((getDropAction() & NodeTransfer.MOVE) != 0) // Moving parameter...
        {
            try {

                // 1. Get the list of children in the new folder...

                String label = origin_rd.getLabel();

                String newResName = origin_rd.getName();
                if (destination_uri.endsWith("/")) newResName = destination_uri + newResName;
                else newResName = destination_uri + "/" + newResName;

                // find a better name for the moved resource...

                // If we are coping to two different URI and the new URI already exists, ask the user
                // if we want to replace a resource which may have the same label...
                if (!origin_rd.getUriString().equals(newResName) && existingLabelsMap.containsKey(origin_rd.getLabel()))
                {
                    int idx = 0;
                    while (existingLabelsMap.containsKey(label))
                    {
                        label = origin_rd.getLabel() + " copy";
                        if (idx > 0) label += " " + (idx+1);
                        idx++;
                    }

                    ResourceDescriptor rd = existingLabelsMap.get(origin_rd.getLabel());
                    CopyReplaceDialog d = new CopyReplaceDialog(Misc.getMainFrame(), true);
                    d.setResourceDescriptors(origin_rd, rd, label, true);
                    d.setVisible(true);

                    if (d.getDialogResult() == HighlightingPanel.TYPE_DONT_COPY) return null;
                    else if (d.getDialogResult() == HighlightingPanel.TYPE_COPY_REPLACE)
                    {
                        origin.getRepositoryObject().getServer().getWSClient().delete(rd);
                        origin.getRepositoryObject().getServer().getWSClient().move(origin_rd, destination_uri);

                        existingLabelsMap.clear();
                        label = origin_rd.getLabel();
                    }
                    else if (d.getDialogResult() == HighlightingPanel.TYPE_COPY)
                    {
                        // a copy must be perfomed explicitally,
                        // otherwise the API move method will complain about
                        // a duplicated resource...
                        ResourceDescriptor oldResource = new ResourceDescriptor();
                        oldResource.setWsType( origin_rd.getWsType() );
                        oldResource.setName( origin_rd.getName() );
                        oldResource.setParentFolder( origin_rd.getParentFolder() );
                        oldResource.setUriString( origin_rd.getUriString() );
                        ResourceDescriptor newRd = origin.getRepositoryObject().getServer().getWSClient().copy(origin_rd, newResName);

                        origin_rd.setParentFolder(destination_uri);

                        // Refresh the resource name assigned by the copy method
                        origin_rd.setName( newRd.getName() );
                        origin_rd.setUriString(destination_uri + "/" + origin_rd.getName());

                        //label = origin_rd.getLabel();

                        // Remove the old resource...
                        origin.getRepositoryObject().getServer().getWSClient().delete(oldResource);
                    }
                }
                else
                {
                    origin.getRepositoryObject().getServer().getWSClient().move(origin_rd, destination_uri);
                }

                
                // refresh childrens of both the nodes...
                if (originParent != null && originParent instanceof FolderNode)
                {
                        ((FolderNode)originParent).refreshChildrens(true);
                }

                origin_rd.setParentFolder(destination_uri);
                origin_rd.setUriString(destination_uri + "/" + origin_rd.getName());
                getDestinationNode().getResourceDescriptor().getChildren().add(origin_rd);

                if (!label.equals(origin_rd.getLabel()))
                {
                    // update the resource name...
                    origin_rd.setLabel(label);
                    try {
                        origin.getRepositoryObject().getServer().getWSClient().putResource(origin_rd, null);

                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                        // ignore renaming errors...
                    }
                }
                
                getDestinationNode().refreshChildrens(false);

            } catch (Exception ex)
            {
                Misc.showErrorMessage("Unable to move the resource: " + ex.getMessage(), "Move error");
                ex.printStackTrace();
            }

        } else // Duplicating
        {
            try {

                System.out.println("Pasting resource... " + ((ResourceNode)originNode).getResourceDescriptor().getUriString() + " to " + destination_uri);
                System.out.flush();

                String newResName = origin_rd.getName();
                if (destination_uri.endsWith("/")) newResName = destination_uri + newResName;
                else newResName = destination_uri + "/" + newResName;

                String label = origin_rd.getLabel();
                // find a better name for the moved resource...
                int idx = 0;
                while (existingLabelsMap.containsKey(label))
                {
                    label = origin_rd.getLabel() + " copy";
                    if (idx > 0) label += " " + (idx+1);
                    idx++;
                }

                // If we are coping to two different URI and the new URI already exists, ask the user
                // if we want to replace a resource which may have the same label...
                if (!origin_rd.getUriString().equals(newResName) && existingLabelsMap.containsKey(origin_rd.getLabel()))
                {
                    
                    
                    ResourceDescriptor rd = existingLabelsMap.get(origin_rd.getLabel());
                    CopyReplaceDialog d = new CopyReplaceDialog(Misc.getMainFrame(), true);
                    d.setResourceDescriptors(origin_rd, rd, label, false);
                    d.setVisible(true);

                    if (d.getDialogResult() == HighlightingPanel.TYPE_DONT_COPY) return null;
                    else if (d.getDialogResult() == HighlightingPanel.TYPE_COPY_REPLACE)
                    {
                        origin.getRepositoryObject().getServer().getWSClient().delete(rd);
                        // remove the old resource from the new folder...
                        List<ResourceDescriptor> list = getDestinationNode().getResourceDescriptor().getChildren();
                        for (ResourceDescriptor rdd : list)
                        {
                            if (rdd.getName().equals(rd.getName()))
                            {
                                getDestinationNode().getResourceDescriptor().getChildren().remove(rdd);
                                break;
                            }
                        }
                        existingLabelsMap.clear();
                        label = origin_rd.getLabel();
                    }

                }

                ResourceDescriptor newRd = origin.getRepositoryObject().getServer().getWSClient().copy(origin_rd, newResName);

                String definitive_uri_string = "";
                if (destination_uri.endsWith("/")) definitive_uri_string = destination_uri + newRd.getName();
                else definitive_uri_string = destination_uri + "/" + newRd.getName();

                newRd.setUriString(definitive_uri_string);

                
                if (!label.equals(newRd.getLabel()))
                {
                    // update the resource name...
                    newRd.setLabel(label);
                    try {
                        origin.getRepositoryObject().getServer().getWSClient().putResource(newRd, null);

                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                        // ignore renaming errors...
                    }
                }

                // refresh childrens of both the nodes...
                getDestinationNode().getResourceDescriptor().getChildren().add(newRd);
                getDestinationNode().refreshChildrens(false);



            } catch (Exception ex)
            {
                ex.printStackTrace();
                Misc.showErrorMessage("Unable to copy the resource: " + ex.getMessage(), "Move error");
            }
        }
        return null;
    }

    /**
     * @return the destinationNode
     */
    public FolderNode getDestinationNode() {
        return destinationNode;
    }

    /**
     * @param destinationNode the destinationNode to set
     */
    public void setDestinationNode(FolderNode destinationNode) {
        this.destinationNode = destinationNode;
    }

    /**
     * @return the originNode
     */
    public Node getOriginNode() {
        return originNode;
    }

    /**
     * @param originNode the originNode to set
     */
    public void setOriginNode(Node originNode) {
        this.originNode = originNode;
    }

    /**
     * @return the dropAction
     */
    public int getDropAction() {
        return dropAction;
    }

    /**
     * @param dropAction the dropAction to set
     */
    public void setDropAction(int dropAction) {
        this.dropAction = dropAction;
    }
}
