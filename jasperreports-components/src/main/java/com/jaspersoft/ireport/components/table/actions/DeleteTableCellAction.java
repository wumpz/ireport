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
import com.jaspersoft.ireport.components.table.nodes.TableCellNode;
import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.components.table.undo.DeleteTableCellUndoableEdit;
import com.jaspersoft.ireport.components.table.undo.DeleteTableColumnUndoableEdit;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.util.List;
import javax.swing.JOptionPane;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.GroupCell;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.HelpCtx;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

public final class DeleteTableCellAction extends NodeAction {

    private static DeleteTableCellAction instance = null;
    
    public static synchronized DeleteTableCellAction getInstance()
    {
        if (instance == null)
        {
            instance = new DeleteTableCellAction();
        }
        
        return instance;
    }
    
    private DeleteTableCellAction()
    {
        super();
    }
    
    
    public String getName() {
        return NbBundle.getMessage(DeleteTableCellAction.class, "DeleteTableCellAction.Name.CTL_DeleteTableCellAction");
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
            if (activatedNodes[i] instanceof TableCellNode)
            {
                TableCellNode cellNode = (TableCellNode)activatedNodes[i];

                StandardTable table = cellNode.getTable();
                JasperDesign jd = cellNode.getJasperDesign();
                
                BaseColumn column = cellNode.getColumn();

                TableMatrix matrix = TableModelUtils.createTableMatrix(cellNode.getTable(), cellNode.getJasperDesign() );
                Object parentGroup = matrix.getColumnParent(column);

                DesignCell cell = cellNode.getCell();

                if (column instanceof StandardColumn)
                {
                    // If this is the only column, check if this is the only cell...
                    int cells = 0;
                    if ( ((StandardColumn)column).getTableHeader() != null) cells++;
                    if ( ((StandardColumn)column).getTableFooter() != null) cells++;
                    if ( ((StandardColumn)column).getColumnHeader() != null) cells++;
                    if ( ((StandardColumn)column).getColumnFooter() != null) cells++;
                    if ( ((StandardColumn)column).getDetailCell() != null) cells++;
                    
                    for (GroupCell groupCell : ((StandardColumn)column).getGroupHeaders())
                    {
                        if (groupCell.getCell() != null) cells++;
                    }
                    
                    for (GroupCell groupCell : ((StandardColumn)column).getGroupFooters())
                    {
                        if (groupCell.getCell() != null) cells++;
                    }
                
                    if (cells == 1)
                    {
                        
                        if (matrix.getStandardColumns().size() == 1)
                        {
                            JOptionPane.showMessageDialog(Misc.getMainFrame(), "This is the only cell of a detail column in the table, and it cannot be removed.", "Cannot delete cell", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        
                        if (TableModelUtils.getColumns(parentGroup).size() == 1)
                        {
                            // Removing this cell we have to remove the parent column recursively...
                            if (JOptionPane.showConfirmDialog(
                                Misc.getMainFrame(),
                                "You are deleting the only cell available in this column and the column will be removed.\n" +
                                "All the headers of the groups having only this detail column as child will be removed as well.\n\n"+
                                "\nContinue anyway?",
                                "Deleting Column Group",
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.WARNING_MESSAGE,
                                ImageUtilities.image2Icon(ImageUtilities.loadImage("com/jaspersoft/ireport/components/table/deleting_group.png"))) != JOptionPane.OK_OPTION) return;
                        }

                        // Let's proceed... remove the cell from the column (this is actually not necessary...
                        // TableModelUtils.removeCell(cellNode.getColumn(), cellNode.getSection(), (cellNode.getGroup() != null) ? cellNode.getGroup().getName() : null);

                        // Remove the column from the parent...
                        int position = TableModelUtils.getColumns(parentGroup).indexOf(column);
                        TableModelUtils.removeColumn(parentGroup, column, position);

                        DeleteTableColumnUndoableEdit edit = new DeleteTableColumnUndoableEdit(table,jd,column, parentGroup, position);
                        List<BaseColumn> oldColumns = TableModelUtils.getColumns(parentGroup);

                        // Remove all the column groups with no longer children recursively....
                        while (oldColumns.size() == 0 && parentGroup instanceof StandardColumnGroup)
                        {
                            Object oldParentParent = matrix.getColumnParent((StandardColumnGroup)parentGroup);
                            if (oldParentParent != null) // This shoule be ALWAYS true....
                            {
                                if (oldParentParent instanceof StandardTable)
                                {
                                    position = ((StandardTable)oldParentParent).getColumns().indexOf((StandardColumnGroup)parentGroup);
                                    ((StandardTable)oldParentParent).removeColumn((StandardColumnGroup)parentGroup);
                                    edit.concatenate(new DeleteTableColumnUndoableEdit(table,jd,(StandardColumnGroup)parentGroup, oldParentParent, position));
                                    oldColumns = ((StandardTable)oldParentParent).getColumns();
                                    parentGroup = oldParentParent;
                                }
                                else
                                {
                                    position = ((StandardColumnGroup)oldParentParent).getColumns().indexOf((StandardColumnGroup)parentGroup);
                                    ((StandardColumnGroup)oldParentParent).removeColumn((StandardColumnGroup)parentGroup);
                                    edit.concatenate(new DeleteTableColumnUndoableEdit(table,jd,(StandardColumnGroup)parentGroup, oldParentParent, position));
                                    oldColumns = ((StandardColumnGroup)oldParentParent).getColumns();
                                    parentGroup = oldParentParent;
                                }
                            }
                        }

                        edit.setPresentationName("Remove Cell");
                        IReportManager.getInstance().addUndoableEdit(edit);
                        TableModelUtils.fixTableLayout(table, jd);
                        return;

                    }
                }

                // Green light to proceed! This is the easy case!

                TableModelUtils.removeCell(cellNode.getColumn(), cellNode.getSection(), (cellNode.getGroup() != null) ? cellNode.getGroup().getName() : null);

                // Now we should check if this column has at least a cell. If not, the column must be recursively removed. In that case prompt for
                

                DeleteTableCellUndoableEdit edit = new DeleteTableCellUndoableEdit(table,jd,cell, cellNode.getColumn(), cellNode.getSection(),cellNode.getGroup());
                IReportManager.getInstance().addUndoableEdit(edit);
                TableModelUtils.fixTableLayout(table, jd);
            }
            
        }
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length == 0) return false;
        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (!(activatedNodes[i] instanceof TableCellNode)) return false;
        }
        return true;
    }
}