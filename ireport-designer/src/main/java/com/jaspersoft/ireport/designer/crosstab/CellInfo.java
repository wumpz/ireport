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

package com.jaspersoft.ireport.designer.crosstab;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;

/**
 *
 * @version $Id: CellPosition.java 0 2009-10-20 00:05:37 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class CellInfo
{
    private int x = 0;
    private int y = 0;
    private int colSpan = 1;
    private int rowSpan = 1;
    private int top = -1;
    private int left = -1;
    private JRDesignCellContents cellContents = null;

    public CellInfo(int x, int y, int colSpan, int rowSpan, JRCellContents cellContents)
    {
        this.x = x;
        this.y = y;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
        this.cellContents = (JRDesignCellContents)cellContents;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the top
     */
    public int getTop() {
        return top;
    }

    /**
     * @param top the top to set
     */
    public void setTop(int top) {
        this.top = top;
    }

    /**
     * @return the left
     */
    public int getLeft() {
        return left;
    }

    /**
     * @param left the left to set
     */
    public void setLeft(int left) {
        this.left = left;
    }

    /**
     * @return the cellContents
     */
    public JRDesignCellContents getCellContents() {
        return cellContents;
    }

    /**
     * @param cellContents the cellContents to set
     */
    public void setCellContents(JRDesignCellContents cellContents) {
        this.cellContents = cellContents;
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

}

