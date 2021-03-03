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

import com.jaspersoft.ireport.components.table.TableElementNode;
import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.components.table.nodes.TableCellNode;
import com.jaspersoft.ireport.components.table.nodes.TableColumnGroupNode;
import com.jaspersoft.ireport.components.table.nodes.TableNullCellNode;
import com.jaspersoft.ireport.components.table.nodes.TableSectionNode;
import com.jaspersoft.ireport.components.table.undo.AddTableColumnUndoableEdit;
import com.jaspersoft.ireport.designer.IReportManager;
import java.util.List;
import javax.swing.undo.UndoableEdit;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

public abstract class AddTableColumnAction extends NodeAction {

    public static final int BEFORE = 0;
    public static final int AFTER = 1;
    public static final int AT_THE_END = 2;
    public static final int AT_THE_START = 3;

    abstract public int getWhere();


    
    public AddTableColumnAction()
    {
        super();
    }
    
    
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

    public BaseColumn createNewColumn()
    {
        StandardColumn subColumn = new StandardColumn();
        subColumn.setWidth(90);
        DesignCell detailCell = new DesignCell();
        detailCell.setHeight(30);
        
        subColumn.setDetailCell(detailCell);

        DesignCell tableHeaderCell = new DesignCell();
        tableHeaderCell.setHeight(30);
        subColumn.setTableHeader(tableHeaderCell);

        DesignCell columnHeaderCell = new DesignCell();
        columnHeaderCell.setHeight(30);
        subColumn.setColumnHeader(columnHeaderCell);


        DesignCell tableFooterCell = new DesignCell();
        tableFooterCell.setHeight(30);
        subColumn.setColumnFooter(tableFooterCell);

        DesignCell columnFooterCell = new DesignCell();
        columnFooterCell.setHeight(30);
        subColumn.setTableFooter(columnFooterCell); 

        return subColumn;
    }

    protected void performAction(org.openide.nodes.Node[] activatedNodes) {

        BaseColumn newColumn = createNewColumn();
        StandardTable table = null;
        JasperDesign jd = null;

        
        int index = -1;
        Object container = null;

        if (activatedNodes[0] instanceof TableElementNode)
        {
            TableElementNode tableNode = (TableElementNode)activatedNodes[0];
            
            index = calculateIndex(tableNode.getTable().getColumns(), -1, getWhere());
            container = tableNode.getTable();
            jd = tableNode.getJasperDesign();
            table = tableNode.getTable();
        }
        else if (activatedNodes[0] instanceof TableSectionNode)
        {
            TableSectionNode tableSectionNode = (TableSectionNode)activatedNodes[0];

            index = calculateIndex(tableSectionNode.getTable().getColumns(), -1, getWhere());
            container = tableSectionNode.getTable();
            jd = tableSectionNode.getJasperDesign();
            table = tableSectionNode.getTable();
        }
        else if (activatedNodes[0] instanceof TableColumnGroupNode)
        {
            TableColumnGroupNode columnNode = (TableColumnGroupNode)activatedNodes[0];

            index = calculateIndex(columnNode.getColumnGroup().getColumns(), -1, getWhere());
            container = columnNode.getColumnGroup();
            jd = columnNode.getJasperDesign();
            table = columnNode.getTable();
        }
        else if (activatedNodes[0] instanceof TableCellNode)
        {
            TableCellNode cellNode = (TableCellNode)activatedNodes[0];
            BaseColumn col = cellNode.getColumn();
            // we need to find the parent column...
            container = findParentColumn(cellNode.getTable(), col); // this could be a table or even a column group... no problem
            List<BaseColumn> columns = (container instanceof StandardTable) ? ((StandardTable)container).getColumns() :((StandardColumnGroup)container).getColumns();

            index = calculateIndex(columns , columns.indexOf(col), getWhere());
            jd = cellNode.getJasperDesign();
            table = cellNode.getTable();
        }
        else if (activatedNodes[0] instanceof TableNullCellNode)
        {
            TableNullCellNode cellNode = (TableNullCellNode)activatedNodes[0];
            BaseColumn col = cellNode.getColumn();
            // we need to find the parent column...
            container = findParentColumn(cellNode.getTable(), col); // this could be a table or even a column group... no problem
            List<BaseColumn> columns = (container instanceof StandardTable) ? ((StandardTable)container).getColumns() :((StandardColumnGroup)container).getColumns();

            index = calculateIndex(columns , columns.indexOf(col), getWhere());
            jd = cellNode.getLookup().lookup(JasperDesign.class);
            table = cellNode.getTable();
        }
        else
        {
            TableCellNode cellNode = null;
            Node parent = activatedNodes[0].getParentNode();
            while (parent != null)
            {
                if (parent instanceof TableCellNode)
                {
                    cellNode = (TableCellNode)parent;
                    break;
                }
                parent = parent.getParentNode();
            }

            if (cellNode != null)
            {
                BaseColumn col = cellNode.getColumn();
                // we need to find the parent column...
                container = findParentColumn(cellNode.getTable(), col); // this could be a table or even a column group... no problem
                List<BaseColumn> columns = (container instanceof StandardTable) ? ((StandardTable)container).getColumns() :((StandardColumnGroup)container).getColumns();

                index = calculateIndex(columns , columns.indexOf(col), getWhere());
                jd = cellNode.getJasperDesign();
                table = cellNode.getTable();
            }
        }


        if (container != null && index != -1)
        {
         UndoableEdit edit = addColumn(table, jd, container, newColumn, index);
         IReportManager.getInstance().addUndoableEdit(edit);
         TableModelUtils.fixTableLayout(table, jd);
        }
    }

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

    protected int calculateIndex(List<BaseColumn> columns, int currentIndex, int where)
    {
        switch (where)
        {
            case BEFORE:
            {
                if (currentIndex >= 0) return currentIndex;
            }
            case AT_THE_START:
            {
                return 0;
            }
            case AFTER:
            {
                if (currentIndex >= 0) return currentIndex+1;
            }
            case AT_THE_END:
            {
                return columns.size();
            }
        }

        return 0;

    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length != 1) return false;
        if (activatedNodes[0] instanceof TableElementNode ||
              activatedNodes[0] instanceof TableSectionNode ||
              activatedNodes[0] instanceof TableColumnGroupNode ||
              activatedNodes[0] instanceof TableCellNode ||
              activatedNodes[0] instanceof TableNullCellNode) return true;
        // Check if a parent node is a TableCellNode...

        Node parent = activatedNodes[0].getParentNode();
        while (parent != null)
        {
            if (parent instanceof TableCellNode) return true;
            parent = parent.getParentNode();
        }

        return false;
    }
}