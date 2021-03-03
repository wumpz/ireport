/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.table;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @version $Id: TableModelUtils.java 0 2010-03-24 11:44:28 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class TableModelUtils {

    /**
     * Check if an element is orphan or not.
     * An element is orphan when his parent is null, or if it is null one of his ancestors
     */
    public static DesignCell getCell(JRDesignElement element) {

        JRElementGroup g1 = element.getElementGroup();
        while (g1 != null)
        {
            //if (!g1.getChildren().contains(element)) return null; // The element points to its parent, but its parent has not it as child
            if (g1 instanceof DesignCell) return (DesignCell)g1;
            g1 = g1.getElementGroup();
        }
        return null;
    }


    /**
     * add cell to a column
     */
    public static void addCell(BaseColumn column, DesignCell cell, byte section, String groupName) {

            if (cell == null || column == null) return;
            if (column instanceof StandardColumn) addCellToStandardColumn((StandardColumn)column, cell, section, groupName);
            if (column instanceof StandardColumnGroup) addCellToStandardColumnGroup((StandardColumnGroup)column, cell, section, groupName);
    }

    private static void addCellToStandardColumn(StandardColumn column, DesignCell cell, byte section, String groupName) {

        switch (section)
        {
            case TableCell.TABLE_HEADER: column.setTableHeader(cell); break;
            case TableCell.COLUMN_HEADER: column.setColumnHeader(cell); break;
            case TableCell.GROUP_HEADER: column.setGroupHeader(groupName,cell); break;
            case TableCell.DETAIL: column.setDetailCell(cell); break;
            case TableCell.GROUP_FOOTER: column.setGroupFooter(groupName,cell); break;
            case TableCell.COLUMN_FOOTER: column.setColumnFooter(cell); break;
            case TableCell.TABLE_FOOTER: column.setTableFooter(cell); break;
        }
    }

    private static void addCellToStandardColumnGroup(StandardColumnGroup column, DesignCell cell, byte section, String groupName) {

        switch (section)
        {
            case TableCell.TABLE_HEADER: column.setTableHeader(cell); break;
            case TableCell.COLUMN_HEADER: column.setColumnHeader(cell); break;
            case TableCell.GROUP_HEADER: column.setGroupHeader(groupName,cell); break;
            case TableCell.GROUP_FOOTER: column.setGroupFooter(groupName,cell); break;
            case TableCell.COLUMN_FOOTER: column.setColumnFooter(cell); break;
            case TableCell.TABLE_FOOTER: column.setTableFooter(cell); break;
        }
    }

    /**
     * add cell to a column
     */
    public static void removeCell(BaseColumn column, byte section, String groupName) {

            if (column == null) return;
            if (column instanceof StandardColumn) removeCellFromStandardColumn((StandardColumn)column, section, groupName);
            if (column instanceof StandardColumnGroup) removeCellFromStandardColumnGroup((StandardColumnGroup)column, section, groupName);
    }

    private static void removeCellFromStandardColumn(StandardColumn column, byte section, String groupName) {

        switch (section)
        {
            case TableCell.TABLE_HEADER: column.setTableHeader(null); break;
            case TableCell.COLUMN_HEADER: column.setColumnHeader(null); break;
            case TableCell.GROUP_HEADER: column.setGroupHeader(groupName,null); break;
            case TableCell.DETAIL: column.setDetailCell(null); break;
            case TableCell.GROUP_FOOTER: column.setGroupFooter(groupName,null); break;
            case TableCell.COLUMN_FOOTER: column.setColumnFooter(null); break;
            case TableCell.TABLE_FOOTER: column.setTableFooter(null); break;
        }
    }

    private static void removeCellFromStandardColumnGroup(StandardColumnGroup column, byte section, String groupName) {

        switch (section)
        {
            case TableCell.TABLE_HEADER: column.setTableHeader(null); break;
            case TableCell.COLUMN_HEADER: column.setColumnHeader(null); break;
            case TableCell.GROUP_HEADER: column.setGroupHeader(groupName,null); break;
            case TableCell.GROUP_FOOTER: column.setGroupFooter(groupName,null); break;
            case TableCell.COLUMN_FOOTER: column.setColumnFooter(null); break;
            case TableCell.TABLE_FOOTER: column.setTableFooter(null); break;
        }
    }

    public static void addColumn(Object parent, BaseColumn column, int index) {

       if (parent == null || column == null || index < 0) return;

       if (parent instanceof StandardTable)
       {
           ((StandardTable)parent).getColumns().add(index, column);
           ((StandardTable)parent).getEventSupport().firePropertyChange(StandardTable.PROPERTY_COLUMNS, null, ((StandardTable)parent).getColumns());
       }
       else if (parent instanceof StandardColumnGroup)
       {
           ((StandardColumnGroup)parent).getColumns().add(index, column);
           ((StandardColumnGroup)parent).getEventSupport().firePropertyChange(StandardTable.PROPERTY_COLUMNS, null, ((StandardColumnGroup)parent).getColumns());
       }
    }

    public static void removeColumn(Object parent, BaseColumn column, int index) {

       if (parent == null || column == null || index < 0) return;

       if (parent instanceof StandardTable)
       {
           ((StandardTable)parent).getColumns().remove(index);
           ((StandardTable)parent).getEventSupport().firePropertyChange(StandardTable.PROPERTY_COLUMNS, null, ((StandardTable)parent).getColumns());
       }
       else if (parent instanceof StandardColumnGroup)
       {
           ((StandardColumnGroup)parent).getColumns().remove(index);
           ((StandardColumnGroup)parent).getEventSupport().firePropertyChange(StandardTable.PROPERTY_COLUMNS, null, ((StandardColumnGroup)parent).getColumns());
       }
    }


    public static TableMatrix createTableMatrix(StandardTable table,JasperDesign jd) {

        TableMatrix matrix = new TableMatrix(jd,table);
        return matrix;
    }

    /**
     *  columnGroup can be a StandardTable or a StandardColumnGroup
     *  if it is not, the method returns null.
     */
    public static List<BaseColumn> getColumns(Object columnGroup) {

        if (columnGroup == null) return null;
        if (columnGroup instanceof StandardTable) return ((StandardTable)columnGroup).getColumns();
        if (columnGroup instanceof StandardColumnGroup) return ((StandardColumnGroup)columnGroup).getColumns();
        return null;
    }


    /**
     * Magical method to fix all the cell width/height/rowspan according to the calculated matrix....
     * This method may rise a lot of events, but none of them should impact the visual layout
     * since we are just synchronizing what we see with what the model holds
     */
    public static void fixTableLayout(StandardTable table, JasperDesign design)
    {
        TableMatrix matrix = createTableMatrix(table, design);

        for (TableCell cell : matrix.getCells())
        {
            Rectangle bounds = matrix.getCellBounds(cell);
            if (cell.getCell() != null)
            {
                if (cell.getCell().getHeight() == null ||
                    cell.getCell().getHeight().intValue() != bounds.height) cell.getCell().setHeight(bounds.height);

                int finalRowSpan = cell.getRowSpan();

                // for each spanning row, if the row contains 0 not null cells, remove it from the span value...
                if (finalRowSpan > 1)
                {
                    for (int spanningRow = 0; spanningRow < cell.getRowSpan(); ++spanningRow)
                    {
                        int spanningRowIndex = cell.getRow() + spanningRow;
                        boolean foundACell = false;
                        for (TableCell cellRow : matrix.getCells())
                        {
                            if (cellRow.getRow() == spanningRowIndex && cellRow.getRowSpan() == 1)
                            {
                                if (cellRow.getCell() != null)
                                {
                                    foundACell = true;
                                    break;
                                }
                            }
                        }
                        if (!foundACell) finalRowSpan--;
                    }
                }

                if (finalRowSpan < 1) finalRowSpan = 1;

                if (cell.getCell().getRowSpan() == null ||
                    cell.getCell().getRowSpan().intValue() != finalRowSpan) cell.getCell().setRowSpan(finalRowSpan);

            }
            Rectangle columnBounds = matrix.getColumnBounds(cell.getColumn());

            if (cell.getColumn().getWidth() == null ||
                cell.getColumn().getWidth().intValue() != columnBounds.width) ((StandardBaseColumn)cell.getColumn()).setWidth(columnBounds.width);
        }
    }


    public static int getColSpan(StandardColumnGroup column) {

        int span = 0;
        List<BaseColumn> columns = column.getColumns();
        for (BaseColumn c : columns)
        {
            if (c instanceof StandardColumn)
            {
                span++;
            }
            else if (c instanceof StandardColumnGroup)
            {
                span += getColSpan((StandardColumnGroup)c);
            }
        }
       return span;
    }

    /**
     * Find the phisical start index of a column.
     * @param table
     * @param column
     * @return
     */
    public static int getColumnIndex(StandardTable table, BaseColumn column) {

        Map<BaseColumn, Integer> mapColumnPositions = new HashMap<BaseColumn, Integer>();
        // traverse the whole structure...
        int currentIndex = 0;
        List<BaseColumn> columns = table.getColumns();
        for (BaseColumn c : columns)
        {
            mapColumnPositions.put(c, currentIndex);
            if (c instanceof StandardColumn)
            {
                currentIndex++;
            }
            else if (c instanceof StandardColumnGroup)
            {
                currentIndex = addColumnIndexes((StandardColumnGroup)c, currentIndex, c, mapColumnPositions);
            }
        }

        if (mapColumnPositions.containsKey(column)) return mapColumnPositions.get(column);
        return -1;
    }


    private static int addColumnIndexes(StandardColumnGroup parent, int currentIndex, BaseColumn column, Map<BaseColumn, Integer> mapColumnPositions) {

        List<BaseColumn> columns = parent.getColumns();
        for (BaseColumn c : columns)
        {
            mapColumnPositions.put(c, currentIndex);
            if (c instanceof StandardColumn)
            {
                currentIndex++;
            }
            else if (c instanceof StandardColumnGroup)
            {
                currentIndex = addColumnIndexes((StandardColumnGroup)c, currentIndex, c, mapColumnPositions);
            }
        }

        return currentIndex;
    }

    /**
     * Return true if in this list (or children) there are at least 2 standardColumns
     * @param columns
     * @return
     */
    public static int getStandardColumnsCount(List<BaseColumn> columns) {

        int count = 0;
        for (BaseColumn c : columns)
        {
            if (c instanceof StandardColumn)
            {
                count++;
            }
            if (c instanceof StandardColumnGroup)
            {
                count += getStandardColumnsCount( ((StandardColumnGroup)c).getColumns() );
            }

        }
        return count;
    }


    public static Object getColumnParent(StandardTable table, BaseColumn column) {

        List<BaseColumn> columns = table.getColumns();
        for (BaseColumn c : columns)
        {
            if (c == column) return table;
            if (c instanceof StandardColumnGroup)
            {
                Object parent = getColumnParent((StandardColumnGroup)c, column);
                if (parent != null) return parent;
            }
        }
        return null;
    }

    private static Object getColumnParent(StandardColumnGroup group, BaseColumn column) {

        List<BaseColumn> columns = group.getColumns();
        for (BaseColumn c : columns)
        {
            if (c == column) return group;
            if (c instanceof StandardColumnGroup)
            {
                Object parent = getColumnParent((StandardColumnGroup)c, column);
                if (parent != null) return parent;
            }
        }
        return null;
    }

    

}
