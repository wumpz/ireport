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

import com.jaspersoft.ireport.components.table.DefaultTableCellElementsLayout;
import com.jaspersoft.ireport.components.table.TableObjectScene;
import com.jaspersoft.ireport.components.table.nodes.TableCellNode;
import com.jaspersoft.ireport.designer.GenericDesignerPanel;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

public abstract class TableCellDoLayoutAction extends NodeAction {


    public abstract int getLayoutType();
    
    public abstract String getName();


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
            TableCellNode cellNode =  null;
            if (activatedNodes[i] instanceof TableCellNode)
            {
                cellNode = (TableCellNode)activatedNodes[i];
            }
            else
            {
                Node parent = activatedNodes[i].getParentNode();
                while (parent != null)
                {
                    if (parent instanceof TableCellNode)
                    {
                        cellNode = (TableCellNode)parent;
                        break;
                    }
                    parent = parent.getParentNode();
                }
            }

            if (cellNode == null) continue;

            JasperDesign jd = cellNode.getLookup().lookup(JasperDesign.class);

            JrxmlVisualView view = IReportManager.getInstance().getActiveVisualView();
            if (view != null)
            {
                GenericDesignerPanel panel = view.getReportDesignerPanel().getElementPanel( cellNode.getComponentElement()  );
                if (panel != null)
                {
                    DefaultTableCellElementsLayout.doLayout(cellNode.getCell(), (TableObjectScene)panel.getScene(), getLayoutType());

                }
            }
        }
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length == 0) return false;
        for (int i=0; i<activatedNodes.length; ++i)
        {
            boolean isOk = false;
            if (activatedNodes[i] instanceof TableCellNode) isOk = true;

            if (!isOk)
            {
                Node parent = activatedNodes[0].getParentNode();
                while (parent != null)
                {
                    if (parent instanceof TableCellNode)
                    {
                        isOk = true;
                        break;
                    }
                    parent = parent.getParentNode();
                }
            }
            if (!isOk) return false;
        }
        return true;
    }
}