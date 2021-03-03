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
package com.jaspersoft.ireport.components.table.actions;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.utils.SubMenuAction;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import com.jaspersoft.ireport.designer.widgets.SelectionWidget;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JPopupMenu;
import net.sf.jasperreports.engine.design.JRDesignElement;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.openide.actions.CopyAction;
import org.openide.nodes.Node;
import org.openide.nodes.NodeOp;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class TableElementPopupMenuProvider implements PopupMenuProvider {

    public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
        
        JRDesignElement element = getElement(widget);
        
        if (element == null) {
            return null;
        }
        
        if ( !((AbstractReportObjectScene)widget.getScene()).getSelectedObjects().contains(element) )
        {
            return null;
        }
        
        Node node = null;
        
        
            // We can assume that the node is selected in this explorer window...
            Node[] selectedNodes = IReportManager.getInstance().getActiveVisualView().getExplorerManager().getSelectedNodes();
            if (selectedNodes != null && selectedNodes.length > 0)
            {
                node = selectedNodes[0];
            }
            //node = IReportManager.getInstance().findNodeOf(element, IReportManager.getInstance().getActiveVisualView().getExplorerManager().getRootContext());
        
        if (selectedNodes != null && selectedNodes.length > 0)
        {
            Action[] actions = NodeOp.findActions(selectedNodes);
            List<Action> actionsList = new ArrayList<Action>(Arrays.asList(actions));

            int index = actionsList.indexOf(SystemAction.get(CopyAction.class));

            if (index < 0) index = 0;
            if (index == 0 && actionsList.size() > 2) index = 2;

            actionsList.add(index, SubMenuAction.getAction("ireport/tablecomponent"));
            actionsList.add(index+1, null);

            actions = actionsList.toArray(new Action[actionsList.size()]);

            if (actions != null && actions.length > 0)
            {
                ActionMap actionsMap = new ActionMap();
                for (int i=0; i<actions.length; ++i)
                {
                    if (actions[i] != null && actions[i].getValue( Action.ACTION_COMMAND_KEY) != null)
                        actionsMap.put( actions[i].getValue( Action.ACTION_COMMAND_KEY), actions[i]);
                }
                
                List<Lookup> allLookups = new ArrayList<Lookup>();
                allLookups.add(node.getLookup());
                allLookups.add(Utilities.actionsGlobalContext());
                allLookups.add(Lookups.singleton(actionsMap));
                Lookup lookup = new ProxyLookup(allLookups.toArray(new Lookup[allLookups.size()]));
                return Utilities.actionsToPopup(actions, lookup);
            }
            
            
        }
        
        
        return null;
    }
    
    private JRDesignElement getElement(Widget widget) {
        
        if (widget == null) return null;
        
        if (widget instanceof JRDesignElementWidget)
        {
            JRDesignElement element = ((JRDesignElementWidget)widget).getElement();
            return element;
        }
        else if (widget instanceof SelectionWidget)
        {
            return getElement( ((SelectionWidget)widget).getRealWidget() );
        }
        return null;
    }

}
