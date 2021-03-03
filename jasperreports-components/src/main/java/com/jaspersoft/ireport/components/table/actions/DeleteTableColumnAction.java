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

import com.jaspersoft.ireport.components.table.TableMatrix;
import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.components.table.nodes.TableCellNode;
import com.jaspersoft.ireport.components.table.nodes.TableColumnGroupNode;
import com.jaspersoft.ireport.components.table.nodes.TableNullCellNode;
import com.jaspersoft.ireport.components.table.nodes.TableSectionNode;
import com.jaspersoft.ireport.components.table.undo.DeleteTableColumnUndoableEdit;
import com.jaspersoft.ireport.designer.IReportManager;
import java.util.List;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

public final class DeleteTableColumnAction extends NodeAction {

    private static DeleteTableColumnAction instance = null;
    
    public static synchronized DeleteTableColumnAction getInstance()
    {
        if (instance == null)
        {
            instance = new DeleteTableColumnAction();
        }
        
        return instance;
    }
    
    private DeleteTableColumnAction()
    {
        super();
    }
    
    
    public String getName() {
        return NbBundle.getMessage(DeleteTableColumnAction.class, "DeleteTableColumnAction.Name.CTL_DeleteTableColumnAction");
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

        StandardTable table = null;
        JasperDesign jd = null;
        BaseColumn column = null;
        Node node = activatedNodes[0];
        if (node instanceof TableNullCellNode)
        {
            column = ((TableNullCellNode)node).getColumn();
            table = ((TableNullCellNode)node).getTable();
            jd = ((TableNullCellNode)node).getLookup().lookup(JasperDesign.class);
        }
        else if (node instanceof TableCellNode)
        {
            column = ((TableCellNode)activatedNodes[0]).getColumn();
            table = ((TableCellNode)node).getTable();
            jd = ((TableCellNode)node).getJasperDesign();
        }
        else if (node instanceof TableColumnGroupNode)
        {
            column = ((TableColumnGroupNode)activatedNodes[0]).getColumnGroup();
            table = ((TableColumnGroupNode)node).getTable();
            jd = ((TableColumnGroupNode)node).getJasperDesign();
        }
        else
        {

            Node parent = activatedNodes[0].getParentNode();
            while (parent != null)
            {
                if (parent instanceof TableCellNode)
                {
                    node = (TableCellNode)parent;
                    column = ((TableCellNode)node).getColumn();
                    table = ((TableCellNode)node).getTable();
                    jd = ((TableCellNode)node).getJasperDesign();
                    break;
                }
                parent = parent.getParentNode();
            }
        }

        if (table == null || column == null || jd == null) return;

        TableMatrix matrix = TableModelUtils.createTableMatrix(table, jd);

        Object columnParent = matrix.getColumnParent(column);
        List<BaseColumn> oldColumns = TableModelUtils.getColumns(columnParent);

        /*  Not useful. It is obvious I want to remove the whole column even if there are groups involved...
        if (oldColumns.size() == 1)
        {
            // Removing this cell we have to remove the parent column recursively...
            if (JOptionPane.showConfirmDialog(
                Misc.getMainFrame(),
                "You are deleting the only colum available in this column group so the group will be removed.\n" +
                "All the headers of the groups having only this detail column as child will be removed as well.\n\n"+
                "\nContinue anyway?",
                "Deleting Column Group",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                ImageUtilities.image2Icon(ImageUtilities.loadImage("com/jaspersoft/ireport/components/table/deleting_group.png"))) != JOptionPane.OK_OPTION) return;
        }
        */

        int position = oldColumns.indexOf(column);
        TableModelUtils.removeColumn(columnParent, column, position);

        DeleteTableColumnUndoableEdit edit = new DeleteTableColumnUndoableEdit(table, jd, column, columnParent,position);
        
        // Remove all the column groups with no longer children recursively....
        while (oldColumns.size() == 0 && columnParent instanceof StandardColumnGroup)
        {
            Object oldParentParent = matrix.getColumnParent((StandardColumnGroup)columnParent);
            if (oldParentParent != null) // This shoule be ALWAYS true....
            {
                if (oldParentParent instanceof StandardTable)
                {
                    position = ((StandardTable)oldParentParent).getColumns().indexOf((StandardColumnGroup)columnParent);
                    ((StandardTable)oldParentParent).removeColumn((StandardColumnGroup)columnParent);
                    edit.concatenate(new DeleteTableColumnUndoableEdit(table, jd, (StandardColumnGroup)columnParent, oldParentParent, position));
                    oldColumns = ((StandardTable)oldParentParent).getColumns();
                    columnParent = oldParentParent;
                }
                else
                {
                    position = ((StandardColumnGroup)oldParentParent).getColumns().indexOf((StandardColumnGroup)columnParent);
                    ((StandardColumnGroup)oldParentParent).removeColumn((StandardColumnGroup)columnParent);
                    edit.concatenate(new DeleteTableColumnUndoableEdit(table, jd, (StandardColumnGroup)columnParent, oldParentParent, position));
                    oldColumns = ((StandardColumnGroup)oldParentParent).getColumns();
                    columnParent = oldParentParent;
                }
            }
        }

        IReportManager.getInstance().addUndoableEdit(edit);
        TableModelUtils.fixTableLayout(table, jd);
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length != 1) return false;
        
        boolean typeOk = false;
        
        Node node = activatedNodes[0];
        if (node instanceof TableNullCellNode ||
              node instanceof TableCellNode ||
              node instanceof TableColumnGroupNode) typeOk = true;



        if (!typeOk)
        {
            Node parent = activatedNodes[0].getParentNode();
            while (parent != null)
            {
                if (parent instanceof TableCellNode)
                {
                    node = (TableCellNode)parent;
                    typeOk = true;
                    break;
                }
                parent = parent.getParentNode();
            }
        }

        if (!typeOk) return false;

        Node parent = node.getParentNode();
        if (parent == null) return false;

        // Check if there are at leat 2 standard columns...


        if (parent instanceof TableSectionNode && TableModelUtils.getStandardColumnsCount( ((TableSectionNode)parent).getTable().getColumns()) > 1) return true;
        if (parent instanceof TableColumnGroupNode && TableModelUtils.getStandardColumnsCount( ((TableColumnGroupNode)parent).getTable().getColumns()) > 1) return true;

        return false;

    }

    
}