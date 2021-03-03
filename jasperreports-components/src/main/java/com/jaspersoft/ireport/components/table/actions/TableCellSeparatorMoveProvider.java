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
import com.jaspersoft.ireport.components.table.TableObjectScene;
import com.jaspersoft.ireport.components.table.undo.TableCellResizeUndoableEdit;
import com.jaspersoft.ireport.components.table.widgets.TableCellSeparatorWidget;
import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.IReportManager;
import java.awt.Color;
import java.awt.Point;
import java.util.List;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.SeparatorWidget.Orientation;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class TableCellSeparatorMoveProvider implements MoveProvider {

    int startY = 0;
    int startX = 0;
    boolean reversOrder = false;
    
    public TableCellSeparatorMoveProvider()
    {
        this(false);
    }
    
    public TableCellSeparatorMoveProvider( boolean reversOrder)
    {
        this.reversOrder =  reversOrder;
    }
    
    public void movementStarted(Widget w) {
        
        startY = w.getPreferredLocation().y;
        startX = w.getPreferredLocation().x;
        w.setForeground(AbstractReportObjectScene.EDITING_DESIGN_LINE_COLOR);

    }

    public void movementFinished(Widget w) {
        


        // we have to update the whole visual things...
        w.setForeground(new Color(0,0,0,0));
        
        if (!(w instanceof TableCellSeparatorWidget)) return;

        TableCellSeparatorWidget separator = (TableCellSeparatorWidget)w;
        TableObjectScene scene = (TableObjectScene)w.getScene();
         
        // Update the width of all the cells having the widget.getIndex()
        // as right border...
        //List<JRDesignCellContents> cells = getAllCells(scene.getDesignCrosstab());

        int delta = 0;
        java.util.List<TableCellResizeUndoableEdit> undos = new java.util.ArrayList<TableCellResizeUndoableEdit>();
        
        int currentPosition = 0;
        
        if (separator.getOrientation() == Orientation.HORIZONTAL)
        {
            currentPosition = startY;
            delta = w.getPreferredLocation().y - startY;

            // set the new cell hight for all the cells in this row...
            List<TableCell> cells = ((TableObjectScene)w.getScene()).getTableMatrix().getCells();
            TableMatrix matrix = ((TableObjectScene)w.getScene()).getTableMatrix();

            DesignCell firstNotNullCell = null;
            for (TableCell cell : cells)
            {
                if (cell.getRow()+cell.getRowSpan() == separator.getIndex() &&  cell.getCell() != null)
                {
                    firstNotNullCell = cell.getCell();
                    int oldHeight = matrix.getHorizontalSeparators().get(separator.getIndex()) - matrix.getHorizontalSeparators().get(cell.getRow());
                    int newHeight = oldHeight+delta;
                    oldHeight = cell.getCell().getHeight();

                    cell.getCell().setHeight(newHeight);
                    undos.add( new TableCellResizeUndoableEdit(scene.getTable(), scene.getJasperDesign(),cell.getCell(),"Height",Integer.class, oldHeight, newHeight ) );
                }
            }
            if (firstNotNullCell != null)
                firstNotNullCell.getEventSupport().firePropertyChange("ROW_HEIGHT", null, 0);

        }
        else
        {
            currentPosition = startX;
            delta = w.getPreferredLocation().x - startX;

            // Find the columns looking at the headers cells which have the right side on this index...


            TableMatrix matrix = ((TableObjectScene)w.getScene()).getTableMatrix();

            List<TableCell> cells = matrix.getCells();
            StandardBaseColumn firstNotNullColumn = null;
            for (TableCell cell : cells)
            {
                if (cell.getType() == TableCell.TABLE_HEADER &&
                    cell.getCol()+cell.getColSpan() == separator.getIndex())
                {
                    int oldWidth = matrix.getVerticalSeparators().get(separator.getIndex()) - matrix.getVerticalSeparators().get(cell.getCol());
                    int newWidth = oldWidth + delta;
                    oldWidth = cell.getColumn().getWidth();

                    ((StandardBaseColumn)cell.getColumn()).setWidth(newWidth);
                    firstNotNullColumn = (StandardBaseColumn)cell.getColumn();
                    undos.add( new TableCellResizeUndoableEdit(scene.getTable(), scene.getJasperDesign(),((StandardBaseColumn)cell.getColumn()),"Width",Integer.class, oldWidth, newWidth ) );
                }
            }
            if (firstNotNullColumn != null)
                firstNotNullColumn.getEventSupport().firePropertyChange("COLUMN_WIDTH", null, 0);
        }
              
        if (delta != 0 && undos.size() > 0)
        {
            // Change the position of all the elements under the
            // modified line or on his right...
            TableCellResizeUndoableEdit mainUndo = undos.get(0);
            
            mainUndo.setMain(true);

            for (int i=1; i<undos.size(); ++i)
            {
                TableCellResizeUndoableEdit undo = undos.get(i);
                mainUndo.concatenate(undo);
            }
                
            IReportManager.getInstance().addUndoableEdit(mainUndo, false);
            TableModelUtils.fixTableLayout(scene.getTable(), scene.getJasperDesign());
        }
    
    }

    public Point getOriginalLocation(Widget widget) {
        return widget.getPreferredLocation();
    }

    public void setNewLocation(Widget widget, Point newLocation) {
        widget.setPreferredLocation(newLocation);
    }

}
