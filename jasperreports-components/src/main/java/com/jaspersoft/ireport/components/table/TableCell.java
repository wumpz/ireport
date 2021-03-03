/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.table;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.DesignCell;

/**
 *
 * @version $Id: CellDescriptor.java 0 2010-03-25 13:28:58 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class TableCell {

    public static final byte TABLE_HEADER = 0;
    public static final byte COLUMN_HEADER = 1;
    public static final byte GROUP_HEADER = 2;
    public static final byte DETAIL = 3;
    public static final byte GROUP_FOOTER = 4;
    public static final byte COLUMN_FOOTER = 5;
    public static final byte TABLE_FOOTER = 6;


    private int type = 0;
    private int col = 0;
    private int row = 0;
    private int rowSpan = 1;
    private int colSpan = 1;
    private String groupName = null;
    private DesignCell cell = null;
    private BaseColumn column = null;

    /**
     * @return the col
     */
    public int getCol() {
        return col;
    }

    /**
     * @param col the col to set
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * @return the rowSpan
     */
    public int getRowSpan() {
        return rowSpan;
    }

    /**
     * @param rowSpan the rowSpan to set
     */
    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    /**
     * @return the colSpan
     */
    public int getColSpan() {
        return colSpan;
    }

    /**
     * @param colSpan the colSpan to set
     */
    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    /**
     * @return the cell
     */
    public DesignCell getCell() {
        return cell;
    }

    /**
     * @param cell the cell to set
     */
    public void setCell(DesignCell cell) {
        this.cell = cell;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString()
    {
        return "Cell ["+row+","+col+", " + rowSpan + "," + colSpan + "] " + cell;
    }

    /**
     * @return the column
     */
    public BaseColumn getColumn() {
        return column;
    }

    /**
     * @param column the column to set
     */
    public void setColumn(BaseColumn column) {
        this.column = column;
    }

}
