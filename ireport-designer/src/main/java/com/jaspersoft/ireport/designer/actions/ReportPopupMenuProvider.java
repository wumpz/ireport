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

import com.jaspersoft.ireport.designer.outline.OutlineTopComponent;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.Node;
import org.openide.nodes.NodeOp;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class ReportPopupMenuProvider implements PopupMenuProvider {

    public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
        
        Node[] nodes = null;
        
        try {
            nodes = OutlineTopComponent.getDefault().getExplorerManager().getSelectedNodes();
        } catch (Exception ex) { }
       
        if (nodes != null && nodes.length > 0)
        {
            //return NodeOp.findContextMenu(nodes);
            Action[] actions = NodeOp.findActions(nodes);
            if (actions != null && actions.length > 0)
            {
                ActionMap actionsMap = new ActionMap();
                for (int i=0; i<actions.length; ++i)
                {
                    if (actions[i] != null && actions[i].getValue( Action.ACTION_COMMAND_KEY) != null)
                        actionsMap.put( actions[i].getValue( Action.ACTION_COMMAND_KEY), actions[i]);
                }
                
                List<Lookup> allLookups = new ArrayList<Lookup>();
                for (int i=0; i<nodes.length; ++i)
                {
                    allLookups.add(nodes[i].getLookup());
                }
                allLookups.add(Utilities.actionsGlobalContext());
                allLookups.add(Lookups.singleton(actionsMap));
                Lookup lookup = new ProxyLookup(allLookups.toArray(new Lookup[allLookups.size()]));
                return Utilities.actionsToPopup(actions, lookup);
            }
        
        }
        
        return null;
    }
}
