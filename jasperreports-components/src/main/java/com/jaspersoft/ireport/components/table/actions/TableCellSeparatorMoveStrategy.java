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

import com.jaspersoft.ireport.components.table.TableObjectScene;
import com.jaspersoft.ireport.components.table.widgets.TableCellSeparatorWidget;
import java.awt.Point;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.widget.SeparatorWidget.Orientation;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class TableCellSeparatorMoveStrategy implements MoveStrategy {

    boolean reversOrder = false;
    
    public TableCellSeparatorMoveStrategy()
    {
        this(false);
    }
    
    public TableCellSeparatorMoveStrategy( boolean reversOrder)
    {
        this.reversOrder =  reversOrder;
    }
    
    public Point locationSuggested(Widget w, Point originalLocation, Point suggestedLocation) {

        if (w instanceof TableCellSeparatorWidget)
        {
            TableCellSeparatorWidget separator = (TableCellSeparatorWidget)w;
            TableObjectScene scene = (TableObjectScene)w.getScene();
            Orientation orientation = ((TableCellSeparatorWidget)w).getOrientation();
            if (orientation == Orientation.HORIZONTAL)
            {
                suggestedLocation.x = 0;
                int previousY = separator.getIndex() > 0 ? scene.getHorizontalSeparators().get(  separator.getIndex() -1 ) : 0 ;
                suggestedLocation.y = Math.max(previousY, suggestedLocation.y);
            }
            else
            {
                suggestedLocation.y = 0;
                int previousX = separator.getIndex() > 0 ? scene.getVerticalSeparators().get(  separator.getIndex() -1  ) : 0;
                suggestedLocation.x = Math.max(previousX, suggestedLocation.x);
            }
        }
        
        return suggestedLocation;
    }
}
