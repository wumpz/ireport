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

import com.jaspersoft.ireport.components.table.TableCell;
import com.jaspersoft.ireport.components.table.TableMatrix;
import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.components.table.nodes.TableNullCellNode;
import com.jaspersoft.ireport.components.table.undo.AddTableCellUndoableEdit;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

public final class AddTableCellAction extends NodeAction {

    private static AddTableCellAction instance = null;
    
    public static synchronized AddTableCellAction getInstance()
    {
        if (instance == null)
        {
            instance = new AddTableCellAction();
        }
        
        return instance;
    }
    
    private AddTableCellAction()
    {
        super();
    }
    
    
    public String getName() {
        return NbBundle.getMessage(AddTableCellAction.class, "AddTableCellAction.Name.CTL_AddTableCellAction");
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
            if (activatedNodes[i] instanceof TableNullCellNode)
            {
                TableNullCellNode cellNode = (TableNullCellNode)activatedNodes[i];
                JasperDesign jd = cellNode.getLookup().lookup(JasperDesign.class);
                
                DesignCell cell = new DesignCell();

                TableMatrix tm = new TableMatrix(jd, cellNode.getTable());
 
                TableCell tc = tm.findTableCell(cellNode.getColumn(), cellNode.getSection(), (cellNode.getGroup() != null) ? cellNode.getGroup().getName() : null);

                int h = tm.getCellBounds(tc).height;
                if (h == 0) h = 30;

                cell.setHeight(h);
                // TODO -> Add the default style...
                TableModelUtils.addCell(cellNode.getColumn(), cell, cellNode.getSection(), (cellNode.getGroup() != null) ? cellNode.getGroup().getName() : null);

                System.out.println(" Added cell for group: " + cellNode.getSection() + "  " + ((cellNode.getGroup() != null) ? cellNode.getGroup().getName() : null));
                System.out.flush();

                AddTableCellUndoableEdit edit = new AddTableCellUndoableEdit(cellNode.getTable(), jd,cell, cellNode.getColumn(), cellNode.getSection(),cellNode.getGroup());
                IReportManager.getInstance().addUndoableEdit(edit);

                TableModelUtils.fixTableLayout(cellNode.getTable(), jd);
            }
            
        }
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length == 0) return false;
        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (!(activatedNodes[i] instanceof TableNullCellNode)) return false;
        }
        return true;
    }
}