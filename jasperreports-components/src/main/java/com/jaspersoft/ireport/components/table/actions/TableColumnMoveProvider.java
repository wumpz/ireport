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
import com.jaspersoft.ireport.components.table.TableObjectScene;
import com.jaspersoft.ireport.components.table.widgets.IndicatorWidget;
import java.awt.Point;
import java.util.List;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class TableColumnMoveProvider implements MoveProvider {

    Point originalLocation = null;
    boolean reversOrder = false;

    int sepIndex = -1;
    
    public TableColumnMoveProvider()
    {
        this(false);
    }
    
    public TableColumnMoveProvider( boolean reversOrder)
    {
        this.reversOrder =  reversOrder;
    }
    
    public void movementStarted(Widget w) {
        
        originalLocation = w.getPreferredLocation();
    }

    public void movementFinished(Widget w) {

        // Valid conditions:
        // 1. The new location cannot be a child of the column
        if (sepIndex < 0 || !(w instanceof IndicatorWidget) || !( ((IndicatorWidget)w).getData() instanceof BaseColumn))
        {
            w.setPreferredLocation(originalLocation);
            return;
        }
        
        IndicatorWidget widget = (IndicatorWidget)w;
        BaseColumn column = (BaseColumn)widget.getData();

        TableObjectScene scene = (TableObjectScene)widget.getScene();
        TableMatrix matrix = scene.getTableMatrix();

        List<StandardColumn> columns = matrix.getStandardColumns();
        StandardColumn siblingColumn = null;
        if (sepIndex >= columns.size())
        {
            siblingColumn = columns.get(columns.size()-1);
        }
        else
        {
            siblingColumn = columns.get(sepIndex);
        }

        Object newParent = matrix.getColumnParent(siblingColumn);

        List<BaseColumn> newColumns = (newParent instanceof StandardTable) ? ((StandardTable)newParent).getColumns() : ((StandardColumnGroup)newParent).getColumns();
        int newPosition = newColumns.indexOf(siblingColumn);
        if (sepIndex >= columns.size()) newPosition++;

        matrix.moveColumn(column, newParent, newPosition);

        // Update the column position...
        originalLocation.x = matrix.getColumnBounds((BaseColumn)widget.getData()).x;
        w.setPreferredLocation(originalLocation);

        // if the widget has been removed from a event, re-add it to the scene...
        if (scene.getIndicatorsLayer().getChildren().size() == 0)
        {
            scene.getIndicatorsLayer().addChild(w);
            scene.validate();
        }
    }

    public Point getOriginalLocation(Widget widget) {
        return widget.getPreferredLocation();
    }

    public void setNewLocation(Widget widget, Point newLocation) {
        widget.setPreferredLocation(newLocation);

        if (widget instanceof IndicatorWidget)
        {
            TableObjectScene scene = (TableObjectScene)widget.getScene();
            TableMatrix matrix = scene.getTableMatrix();

            List<Integer> vertLines = matrix.getVerticalSeparators();
            List<Integer> horizLines = matrix.getHorizontalSeparators();
            int closest = widget.getBounds().width/2;
            int mid = newLocation.x + closest;

            int i = 0;
            sepIndex = -1;
            for (Integer v : vertLines)
            {
                if (Math.abs(mid - v)  < closest)
                {
                    closest = Math.abs(mid - v);
                    sepIndex = i;
                }
                i++;
            }

            ((IndicatorWidget)widget).setLastIndicatedIndex(sepIndex);
        }

    }

}
