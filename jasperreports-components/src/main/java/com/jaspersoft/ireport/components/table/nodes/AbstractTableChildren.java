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
package com.jaspersoft.ireport.components.table.nodes;

import com.jaspersoft.ireport.components.table.TableCell;
import com.jaspersoft.ireport.components.table.TableMatrix;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public abstract class AbstractTableChildren extends Index.KeysChildren implements PropertyChangeListener {

    private JasperDesign jd = null;
    private Lookup doLkp = null;
    private JRDesignComponentElement component = null;
    private JRDesignGroup group = null;
    private byte section = 0;

    private TableMatrix matrix = null;

    
    @SuppressWarnings("unchecked")
    public AbstractTableChildren(JasperDesign jd, JRDesignComponentElement componentElement, byte section, JRDesignGroup group, Lookup doLkp) {
        super(new ArrayList());
        this.jd = jd;
        this.doLkp = doLkp;
        this.section = section;
        this.group = group;
        this.component = componentElement;
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        recalculateKeys();
    }

    public abstract void recalculateKeysImpl();

    @SuppressWarnings("unchecked")
    public void recalculateKeys() {

        setMatrix(new TableMatrix(jd, (StandardTable)component.getComponent()));

        recalculateKeysImpl();

        // fire a name change for all the nodes...
        update();


    }

    protected String getCellPropertyName()
    {
        switch (getSection())
        {
            case TableCell.TABLE_HEADER: return StandardBaseColumn.PROPERTY_TABLE_HEADER;
            case TableCell.COLUMN_HEADER: return StandardBaseColumn.PROPERTY_COLUMN_HEADER;
            case TableCell.GROUP_HEADER: return StandardBaseColumn.PROPERTY_GROUP_HEADERS;
            case TableCell.GROUP_FOOTER: return StandardBaseColumn.PROPERTY_GROUP_FOOTERS;
            case TableCell.COLUMN_FOOTER: return StandardBaseColumn.PROPERTY_COLUMN_FOOTER;
            case TableCell.TABLE_FOOTER: return StandardBaseColumn.PROPERTY_TABLE_FOOTER;
            case TableCell.DETAIL: return StandardColumn.PROPERTY_DETAIL;
        }
        return null;
    }

//    @Override
//    public void propertyChange(PropertyChangeEvent evt) {
//
//        if (evt.getPropertyName().equals(getCellPropertyName()))
//        {
//            refreshKey(evt.getSource());
//        }
//        else
//        {
//            recalculateKeys();
//        }
//
//    }

    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals(getCellPropertyName()))
        {
            refreshKey(evt.getSource());
        }
        else if (evt.getPropertyName().equals( StandardTable.PROPERTY_COLUMNS) || evt.getPropertyName().equals( StandardTable.PROPERTY_DATASET_RUN))
        {
            recalculateKeys();
        }

        // Modify the name of each cell...
        updateNodeNames(getNodes());
    }
    
    private void updateNodeNames(Node[] nodesToUpdated)
    {
        if (nodesToUpdated == null || nodesToUpdated.length ==0) return;
        for (Node node : nodesToUpdated)
        {
            node.setDisplayName(null);
            updateNodeNames(node.getChildren().getNodes());
        }
    }

    protected Node[] createNodes(Object key)
    {
        List<Node> theNodes = new ArrayList<Node>();

        BaseColumn column = (BaseColumn)key;
            //node = new TableColumnNode(jd, component, (StandardColumn)key, table, section, group, doLkp );

        theNodes.add(createCellNode(column));

        if (key instanceof StandardColumnGroup)
        {
            Node node = new TableColumnGroupNode(jd, component, (StandardColumnGroup)key, getMatrix().getColumnParent(column), getSection(), group, doLkp);
            String name = "Column Group ";
            if (getMatrix() != null)
            {
               name += (getMatrix().getColumnIndex(column)+1);
            }
            node.setDisplayName(name);
            theNodes.add(node);
        }

        return theNodes.toArray(new Node[theNodes.size()]);
    }


//    public void updateCellNames()
//    {
//        for (Node node : getNodes())
//        {
//            node.setDisplayName(  getCellName(node) );
//            if (node instanceof TableColumnGroupNode)
//            {
//                if (node.getChildren() instanceof AbstractTableChildren)
//                {
//                    ((AbstractTableChildren)node.getChildren()).updateCellNames();
//                }
//            }
//        }
//    }

//    public String getCellName(Node cellNode)
//    {
//        if (getMatrix()==null) return "cell";
//        BaseColumn column = null;
//        boolean isNull = false;
//        if (cellNode instanceof TableNullCellNode)
//        {
//            column = ((TableNullCellNode)cellNode).getColumn();
//            isNull = true;
//        }
//        else if (cellNode instanceof TableNullCellNode)
//        {
//            column = ((TableCellNode)cellNode).getColumn();
//        }
//        else if (cellNode instanceof TableColumnGroupNode)
//        {
//            column = ((TableColumnGroupNode)cellNode).getColumnGroup();
//        }
//
//        if (section == TableCell.DETAIL)
//        {
//            return "Column " + (getMatrix().getColumnIndex(column)+1);
//        }
//
//        String name = "Column ";
//
//        if (getMatrix() != null)
//        {
//           name += (getMatrix().getColumnIndex(column)+1);
//        }
//        name += (column instanceof StandardColumnGroup) ? " Group Header" : "";
//        if (isNull) name += " (Empty)";
//
//        return name;
//
//    }


    public Node createCellNode(BaseColumn column)
    {
        Cell cell = null;

        if (column instanceof StandardColumn && getSection() == TableCell.DETAIL)
        {
            if (((StandardColumn)column).getDetailCell() != null)
            {
                TableCellNode node = new TableCellNode(jd, component, column, (DesignCell) ((StandardColumn)column).getDetailCell(), TableCell.DETAIL, null, doLkp);
                //node.setDisplayName(getCellName(node));
                return node;
            }
        }

        switch (getSection())
        {
            case TableCell.TABLE_HEADER: cell = column.getTableHeader(); break;
            case TableCell.COLUMN_HEADER: cell = column.getColumnHeader(); break;
            case TableCell.GROUP_HEADER: cell = column.getGroupHeader(group.getName()); break;
            case TableCell.GROUP_FOOTER: cell = column.getGroupFooter(group.getName()); break;
            case TableCell.COLUMN_FOOTER: cell = column.getColumnFooter(); break;
            case TableCell.TABLE_FOOTER: cell = column.getTableFooter(); break;
        }

        Node node = null;
        if (cell == null)
        {
            node = new TableNullCellNode(jd, component, column, getSection(), group, doLkp);
        }
        else
        {
            node = new TableCellNode(jd, component, column, (DesignCell)cell, getSection(), group, doLkp);
        }

        //node.setDisplayName(getCellName(node));
        return node;
    }

    /**
     * @return the matrix
     */
    public TableMatrix getMatrix() {
        return matrix;
    }

    /**
     * @param matrix the matrix to set
     */
    public void setMatrix(TableMatrix matrix) {
        this.matrix = matrix;
    }

    /**
     * @return the section
     */
    public byte getSection() {
        return section;
    }




}
