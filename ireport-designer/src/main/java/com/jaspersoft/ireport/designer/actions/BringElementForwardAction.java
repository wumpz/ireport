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

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.outline.nodes.ElementGroupNode;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import java.util.Arrays;
import java.util.List;
import javax.swing.JMenuItem;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignElement;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

public final class BringElementForwardAction extends AbstractOrderChangeAction {

    public String getName() {
        return I18n.getString("BringElementForwardAction.Name");
    }

    @Override
    protected String iconResource() {
        return "com/jaspersoft/ireport/designer/resources/formatting/bringforward.png";
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.FALSE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    protected void performAction(org.openide.nodes.Node[] activatedNodes) {

        // 1. order the selected elements by index.
        Node parent = activatedNodes[0].getParentNode();

        Children childrens = parent.getChildren();
        int[] perms = new int[childrens.getNodesCount()];
        for (int i=0; i<perms.length; ++i)
        {
            perms[i] = i;
        }

        int[] indexes = new int[activatedNodes.length];

        List allNodes = Arrays.asList(childrens.getNodes());
        for (int i=0; i<activatedNodes.length; ++i)
        {
            indexes[i] = allNodes.indexOf(activatedNodes[i]);
        }

        Arrays.sort(indexes);

        for (int i=indexes.length-1; i>=0; --i)
        {
            swap(perms, indexes[i]+1, indexes[i]);
        }

        ((Index.KeysChildren)parent.getChildren()).getIndex().reorder(perms);

        AbstractOrderChangeAction.fireChangeOrder();
    }


    public void swap(int[] perms, int a, int b)
    {
        int indexOfA = -1;
        int indexOfB = -1;

        // find the original node ad position a...
        for (int i=0; i<perms.length; ++i)
        {
            if (perms[i] == a)
            {
                indexOfA = i;
            }
            if (perms[i] == b)
            {
                indexOfB = b;
            }
            if (indexOfA != -1 && indexOfB != -1)
            {
                perms[indexOfA] = b;
                perms[indexOfB] = a;
                return;
            }
        }
        // unable to find one of the indexes...
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        
        if (activatedNodes == null || activatedNodes.length == 0) return false;
        
        // Check if all the nodes are under the same parent...
        Node parent =  activatedNodes[0].getParentNode();
        
        if (parent == null) return false;
        
        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (activatedNodes[i].getParentNode() != parent) 
            {
                return false;
            }

            if (activatedNodes[i].getParentNode() != null &&
                activatedNodes[i].getParentNode() instanceof ElementNode)
            {
                    JRDesignElement multiaxischart = ((ElementNode)activatedNodes[i].getParentNode()).getElement();
                    if (multiaxischart instanceof JRDesignChart &&
                        ((JRDesignChart)multiaxischart).getChartType() == JRDesignChart.CHART_TYPE_MULTI_AXIS)
                    {
                        return false;
                    }
            }

            if (activatedNodes[i] instanceof ElementNode)
            {
                JRDesignElement element = ((ElementNode)activatedNodes[i]).getElement();
                JRElementGroup group = element.getElementGroup();
                if (group.getChildren().indexOf(element) == group.getChildren().size()-1) return false;
            }
            else if (activatedNodes[i] instanceof ElementGroupNode)
            {
                JRElementGroup element = ((ElementGroupNode)activatedNodes[i]).getElementGroup();
                JRElementGroup group = element.getElementGroup();
                if (group.getChildren().indexOf(element) == group.getChildren().size()-1) return false;
            }
        }
        
        return true;
    }

    @Override
    public JMenuItem getPopupPresenter() {
        JMenuItem item = super.getPopupPresenter();
        item.setIcon(getIcon());
        return item;
    }
}