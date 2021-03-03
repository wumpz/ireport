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

import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.components.table.nodes.TableCellNode;
import com.jaspersoft.ireport.components.table.nodes.TableNullCellNode;
import com.jaspersoft.ireport.components.table.undo.AddTableCellUndoableEdit;
import com.jaspersoft.ireport.components.table.undo.AddTableColumnUndoableEdit;
import com.jaspersoft.ireport.components.table.undo.DeleteTableColumnUndoableEdit;
import com.jaspersoft.ireport.designer.IReportManager;
import java.util.List;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

public class GroupColumnsAction extends NodeAction {

    public static final int BEFORE = 0;
    public static final int AFTER = 1;
    public static final int AT_THE_END = 2;
    public static final int AT_THE_START = 3;

    public GroupColumnsAction()
    {
        super();
    }
    
    
    public String getName()
    {
        return NbBundle.getMessage(AddTableColumnAction.class, "GroupColumnsAction.Name.CTL_GroupColumnsAction");
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

        StandardColumnGroup newColumn = new StandardColumnGroup();
        newColumn.setWidth(0);
        StandardColumn fakeStdCol = new StandardColumn();
        fakeStdCol.setWidth(0);
        DesignCell fakeDetail = new DesignCell();
        fakeDetail.setHeight(0);
        fakeStdCol.setDetailCell(fakeDetail);
        newColumn.addColumn(fakeStdCol);

        StandardTable table = null;
        JasperDesign jd = null;


        Object parent = null;
        List<BaseColumn> columns = null;

        int minIndex = -1;
        int maxIndex = -1;
        int count = 0;
        for (int i=0; i<activatedNodes.length; ++i)
        {
            Object thisParent = null;
            BaseColumn thisColumn = null;
            if (activatedNodes[i] instanceof TableCellNode)
            {
                TableCellNode node = (TableCellNode)activatedNodes[i];
                thisParent = TableModelUtils.getColumnParent(node.getTable(), node.getColumn());
                thisColumn = node.getColumn();
            }
            if (activatedNodes[i] instanceof TableNullCellNode)
            {
                TableNullCellNode node = (TableNullCellNode)activatedNodes[i];
                thisParent = TableModelUtils.getColumnParent(node.getTable(), node.getColumn());
                thisColumn = node.getColumn();
            }
            if (parent == null) parent = thisParent;
            if (columns == null)
            {
                columns = TableModelUtils.getColumns(parent);
            }

            int index = columns.indexOf(thisColumn);

            if (minIndex < 0) minIndex = index;
            if (maxIndex < 0) maxIndex = index;

            if (maxIndex < index) maxIndex = index;
            if (minIndex > index) maxIndex = index;

            count++;
        }

        // Add the new column
        TableModelUtils.addColumn(parent, newColumn,maxIndex+1);
         AddTableColumnUndoableEdit edit = new AddTableColumnUndoableEdit(table, jd, newColumn, parent, maxIndex+1);

        // Move the children columns

        for (int i=maxIndex; i>=minIndex; --i)
        {
            BaseColumn col = columns.get(i);
            TableModelUtils.removeColumn(parent, col, i);

            edit.concatenate(new DeleteTableColumnUndoableEdit(table, jd, col, parent, i));

            TableModelUtils.addColumn(newColumn, col, 0);

            edit.concatenate(new AddTableColumnUndoableEdit(table, jd, col, newColumn, 0));
        }
        int colIndex = newColumn.getColumns().indexOf(fakeStdCol);
        TableModelUtils.removeColumn(newColumn, fakeStdCol, colIndex);
        edit.concatenate(new DeleteTableColumnUndoableEdit(table, jd, fakeStdCol, newColumn, colIndex));
        edit.setPresentationName("Grouping Columns");
        IReportManager.getInstance().addUndoableEdit(edit);
        TableModelUtils.fixTableLayout(table, jd);

    }

    /*
    protected UndoableEdit addColumn(StandardTable table, JasperDesign jd, Object container, BaseColumn newColumn, int index)
    {
        List<BaseColumn> columns =  (container instanceof StandardTable) ? ((StandardTable)container).getColumns() :((StandardColumnGroup)container).getColumns();
        columns.add(index, newColumn);
        if (container instanceof StandardTable)
        {
            ((StandardTable)container).getEventSupport().fireCollectionElementAddedEvent(StandardTable.PROPERTY_COLUMNS, null, index);
        }
        else
        {
            ((StandardColumnGroup)container).getEventSupport().fireCollectionElementAddedEvent(StandardTable.PROPERTY_COLUMNS, null, index);
        }

        AddTableColumnUndoableEdit edit = new AddTableColumnUndoableEdit(table, jd, newColumn, container, index);
        return edit;
    }
    
    protected Object findParentColumn(StandardTable table, BaseColumn col)
    {
        if (table.getColumns().contains(col)) return table;
        List<BaseColumn> columns = table.getColumns();
        for (BaseColumn c : columns)
        {
            if (c instanceof StandardColumnGroup)
            {
                Object obj = findColumn((StandardColumnGroup)c, col);
                if (obj != null) return obj;
            }
        }
        return null;
    }
    
    public Object findColumn(StandardColumnGroup group, BaseColumn col)
    {
        if (group.getColumns().contains(col)) return group;
        List<BaseColumn> columns = group.getColumns();
        for (BaseColumn c : columns)
        {
            if (c instanceof StandardColumnGroup)
            {
                Object obj = findColumn((StandardColumnGroup)c, col);
                if (obj != null) return obj;
            }
        }
        return null;
    }
*/
    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length == 0) return false;

        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (!(activatedNodes[i] instanceof TableCellNode ||
                  activatedNodes[i] instanceof TableNullCellNode)) return false;
        }

        // Check if they are sibling columns...
        Object parent = null;
        List<BaseColumn> columns = null;

        int minIndex = -1;
        int maxIndex = -1;
        int count = 0;
        for (int i=0; i<activatedNodes.length; ++i)
        {
            Object thisParent = null;
            BaseColumn thisColumn = null;
            if (activatedNodes[i] instanceof TableCellNode)
            {
                TableCellNode node = (TableCellNode)activatedNodes[i];
                thisParent = TableModelUtils.getColumnParent(node.getTable(), node.getColumn());
                thisColumn = node.getColumn();
            }
            if (activatedNodes[i] instanceof TableNullCellNode)
            {
                TableNullCellNode node = (TableNullCellNode)activatedNodes[i];
                thisParent = TableModelUtils.getColumnParent(node.getTable(), node.getColumn());
                thisColumn = node.getColumn();
            }
            if (parent == null) parent = thisParent;
            if (columns == null)
            {
                columns = TableModelUtils.getColumns(parent);
            }


            if (thisParent != parent)
            {
                System.out.println(" Different parent for col " + i);
                System.out.flush();
                return false;
            }

            int index = columns.indexOf(thisColumn);

            if (minIndex < 0) minIndex = index;
            if (maxIndex < 0) maxIndex = index;

            if (maxIndex < index) maxIndex = index;
            if (minIndex > index) maxIndex = index;

            count++;
        }

        if ((maxIndex - minIndex +1) != count)
        {
            System.out.println(" Count mismatch: " + maxIndex +"  " + minIndex + "  " + (maxIndex-minIndex) + "  " + count);
            System.out.flush();
            return false;
        }

        return true;
    }
}