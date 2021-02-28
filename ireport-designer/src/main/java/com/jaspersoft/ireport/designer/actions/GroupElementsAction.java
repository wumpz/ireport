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
package com.jaspersoft.ireport.designer.actions;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.outline.nodes.ElementGroupNode;
import com.jaspersoft.ireport.designer.undo.AddElementGroupUndoableEdit;
import com.jaspersoft.ireport.designer.undo.DeleteElementGroupUndoableEdit;
import com.jaspersoft.ireport.designer.undo.DeleteElementUndoableEdit;
import com.jaspersoft.ireport.designer.undo.GroupElementsUndoableEdit;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import org.openide.nodes.Children;
import org.openide.nodes.Node;


public final class GroupElementsAction extends NodeAction {

        
    public String getName() {
        return I18n.getString("GroupElementsAction.name");
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

        GroupElementsUndoableEdit undoEdit = new GroupElementsUndoableEdit();

        JRDesignElementGroup newgroup = new JRDesignElementGroup();

        List<JRDesignElement> elements = new ArrayList<JRDesignElement>();
        List<JRDesignElementGroup> groups = new ArrayList<JRDesignElementGroup>();

        int firstIndex = 0;
        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (activatedNodes[i] instanceof ElementNode)
            {
                JRDesignElement element = ((ElementNode)activatedNodes[i]).getElement();
                elements.add(element);
            }
            else if (activatedNodes[i] instanceof ElementGroupNode)
            {
                JRDesignElementGroup element = ((ElementGroupNode)activatedNodes[i]).getElementGroup();
                groups.add(element);
            }
        }

        // Find the parent...
        JRElementGroup parent = activatedNodes[0].getParentNode().getLookup().lookup(JRElementGroup.class);

        if (parent != null)
        {
            for (JRDesignElement element : elements)
            {
                int index = parent.getChildren().indexOf(element);
                if (parent instanceof JRDesignElementGroup)
                {
                    ((JRDesignElementGroup)parent).removeElement(element);
                }
                else if (parent instanceof JRDesignBand)
                {
                    ((JRDesignBand)parent).removeElement(element);
                }
                else if (parent instanceof JRDesignCellContents)
                {
                    ((JRDesignCellContents)parent).removeElement(element);
                }
                else if (parent instanceof JRDesignFrame)
                {
                    ((JRDesignFrame)parent).removeElement(element);
                }

                undoEdit.concatenate(new DeleteElementUndoableEdit(element, parent, index));
                newgroup.addElement(element);
            }

            for (JRDesignElementGroup group : groups)
            {
                int index = parent.getChildren().indexOf(group);
                if (parent instanceof JRDesignElementGroup)
                {
                    ((JRDesignElementGroup)parent).removeElementGroup(group);
                }
                else if (parent instanceof JRDesignBand)
                {
                    ((JRDesignBand)parent).removeElementGroup(group);
                }
                else if (parent instanceof JRDesignCellContents)
                {
                    ((JRDesignCellContents)parent).removeElementGroup(group);
                }
                else if (parent instanceof JRDesignFrame)
                {
                    ((JRDesignCellContents)parent).removeElementGroup(group);
                }

                undoEdit.concatenate(new DeleteElementGroupUndoableEdit(group, parent, index));
                newgroup.addElementGroup(group);
            }

            
        }

        if (parent instanceof JRDesignElementGroup)
        {
            ((JRDesignElementGroup)parent).addElementGroup(newgroup);
        }
        else if (parent instanceof JRDesignBand)
        {
            ((JRDesignBand)parent).addElementGroup(newgroup);
        }
        else if (parent instanceof JRDesignCellContents)
        {
            ((JRDesignCellContents)parent).addElementGroup(newgroup);
        }
        else if (parent instanceof JRDesignFrame)
        {
            ((JRDesignFrame)parent).addElementGroup(newgroup);
        }

        undoEdit.concatenate(new AddElementGroupUndoableEdit(newgroup, parent));

        // Select all the group childrens...
        for (JRDesignElementGroup group : groups)
        {
            IReportManager.getInstance().addSelectedObject(group);
        }

        // Select all the group childrens...
        for (JRDesignElement element : elements)
        {
            IReportManager.getInstance().addSelectedObject(element);
        }

        IReportManager.getInstance().addUndoableEdit(undoEdit);
    }


    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {

        if (activatedNodes.length == 0) return false;

        Node parent = activatedNodes[0].getParentNode();
        // All the nodes must have the same parent and must be all "close" to each other...

        List<Node> selectedNodes = new ArrayList<Node>();

        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (!(
                    // Minimal requirements must be true...
                   (activatedNodes[i] instanceof ElementNode ||
                    activatedNodes[i] instanceof ElementGroupNode) &&
                    parent == activatedNodes[i].getParentNode()
                  )
                )
            {
                return false;
            }
            
            selectedNodes.add(activatedNodes[i]);
        }


        Children children = parent.getChildren();
        Node[] nodes = children.getNodes();
        
        boolean fistFound = false;
        // Find the first and the last...
        for (int i=0; i<nodes.length; ++i)
        {
            if (selectedNodes.size() == 0) break;
            if (selectedNodes.size() > 0 && selectedNodes.contains(nodes[i]))
            {
                fistFound = true;
                selectedNodes.remove(nodes[i]);
                continue;
            }
            if (fistFound) return false;
        }

        return true;
    }
}