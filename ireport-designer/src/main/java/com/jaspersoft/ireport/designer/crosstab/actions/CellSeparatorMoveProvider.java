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
package com.jaspersoft.ireport.designer.crosstab.actions;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.widgets.*;
import com.jaspersoft.ireport.designer.crosstab.CrosstabObjectScene;
import com.jaspersoft.ireport.designer.crosstab.undo.CellResizeUndoableEdit;
import com.jaspersoft.ireport.designer.crosstab.widgets.CellSeparatorWidget;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Color;
import java.awt.Point;
import java.util.List;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabCell;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.engine.design.JRDesignElement;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.widget.SeparatorWidget.Orientation;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class CellSeparatorMoveProvider implements MoveProvider {

    int startY = 0;
    int startX = 0;
    boolean reversOrder = false;
    
    public CellSeparatorMoveProvider()
    {
        this(false);
    }
    
    public CellSeparatorMoveProvider( boolean reversOrder)
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
        
        if (!(w instanceof CellSeparatorWidget)) return;

        CellSeparatorWidget separator = (CellSeparatorWidget)w;
        CrosstabObjectScene scene = (CrosstabObjectScene)w.getScene();
         
        // Update the width of all the cells having the widget.getIndex()
        // as right border...
        //List<JRDesignCellContents> cells = getAllCells(scene.getDesignCrosstab());
        int delta = 0;
        java.util.List<CellResizeUndoableEdit> undos = new java.util.ArrayList<CellResizeUndoableEdit>();

        int currentPosition = 0;
        
        if (separator.getOrientation() == Orientation.HORIZONTAL)
        {
            currentPosition = startY;
            delta = w.getPreferredLocation().y - startY;
            JRCrosstabColumnGroup[] groups = scene.getDesignCrosstab().getColumnGroups();
            if (separator.getIndex() < groups.length)
            {
               // The change must be done on the cell of the row group...
               int h = ((JRDesignCrosstabColumnGroup)groups[separator.getIndex()]).getHeight();
               JRDesignCrosstabColumnGroup cell = (JRDesignCrosstabColumnGroup)groups[separator.getIndex()];
               undos.add( new CellResizeUndoableEdit(cell,I18n.getString("Global.Property.Height"),Integer.TYPE, h, h+delta ) );
               cell.setHeight( h + delta );
            }
            else
            {
                //JRCrosstabCell[][] cells = scene.getDesignCrosstab().getCells();
                JRCrosstabColumnGroup[] col_groups = scene.getDesignCrosstab().getColumnGroups();
                JRCrosstabRowGroup[] row_groups = scene.getDesignCrosstab().getRowGroups();
                JRCrosstabCell[][] cells = ModelUtils.normalizeCell(scene.getDesignCrosstab().getCells(),row_groups,col_groups);
                
                // get tje cells at the line index - groups-length
                int index = separator.getIndex() - groups.length ;
                index = cells.length - (index + 1);
                
                for (int i=0; i<cells[index].length; ++i)
                {
                    if (cells[index][i] != null)
                    {
                        int h = -1;
                        JRDesignCrosstabCell cell = (JRDesignCrosstabCell)cells[index][i];
                        
                        if (cell.getHeight() != null)
                        {
                            h = cell.getHeight();
                        }
                        else if (cell.getContents() != null)
                        {
                            h = cell.getContents().getHeight();
                        }
                        if (h >= 0)
                        {
                            undos.add( new CellResizeUndoableEdit(cell,I18n.getString("Global.Property.Height"),Integer.class, h, h+delta ) );
                            cell.setHeight( h + delta );
                        }
                    }
                }
            }
        }
        else
        {
            currentPosition = startX;
            delta = w.getPreferredLocation().x - startX;
            JRCrosstabRowGroup[] groups = scene.getDesignCrosstab().getRowGroups();
            if (separator.getIndex() < groups.length)
            {
               // The change must be done on the cell of the row group...
                JRDesignCrosstabRowGroup group = (JRDesignCrosstabRowGroup)groups[separator.getIndex()];
                int width = group.getWidth();
                undos.add( new CellResizeUndoableEdit(group,I18n.getString("Global.Property.Width"),Integer.TYPE, width, width+delta ) );
                group.setWidth( width + delta );
            }
            else
            {
                //JRCrosstabCell[][] cells = scene.getDesignCrosstab().getCells();
                
                JRCrosstabColumnGroup[] col_groups = scene.getDesignCrosstab().getColumnGroups();
                JRCrosstabRowGroup[] row_groups = scene.getDesignCrosstab().getRowGroups();
                JRCrosstabCell[][] cells = ModelUtils.normalizeCell(scene.getDesignCrosstab().getCells(),row_groups,col_groups);

                
                // get tje cells at the line index - groups-length
                
                int index = separator.getIndex() - groups.length;
                index = cells[0].length - (index + 1);
                
                for (int i=0; i<cells.length; ++i)
                {
                    JRDesignCrosstabCell cell = (JRDesignCrosstabCell)cells[i][index];
                    if (cell != null)
                    {
                        int width = -1;
                        if (cell.getWidth() != null)
                        {
                            width = cell.getWidth();
                        }
                        else if (cell.getContents() != null)
                        {
                            width = cell.getContents().getWidth();
                        }
                        if (width >= 0)
                        {
                            undos.add( new CellResizeUndoableEdit(cell,I18n.getString("Global.Property.Width"),Integer.class, width, width+delta ) );
                            cell.setWidth( width + delta );
                        }
                    }
                }
            }
        }
              
        if (delta != 0 && undos.size() > 0)
        {
            // Change the position of all the elements under the
            // modified line or on his right...
            CellResizeUndoableEdit mainUndo = undos.get(0);
            
            mainUndo.setMain(true);
            mainUndo.setCrosstab(scene.getDesignCrosstab());
            for (int i=1; i<undos.size(); ++i)
            {
                CellResizeUndoableEdit undo = undos.get(i);
                mainUndo.concatenate(undo);
            }
                
            IReportManager.getInstance().addUndoableEdit(mainUndo, false);
            
            scene.getDesignCrosstab().preprocess();
            scene.rebuildDocument();
        
        }
    
    }

    public Point getOriginalLocation(Widget widget) {
        return widget.getPreferredLocation();
    }

    public void setNewLocation(Widget widget, Point newLocation) {
        widget.setPreferredLocation(newLocation);
    }

}
