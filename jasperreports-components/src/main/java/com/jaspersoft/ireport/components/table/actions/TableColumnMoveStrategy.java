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
import com.jaspersoft.ireport.components.table.widgets.IndicatorWidget;
import java.awt.Point;
import java.util.List;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class TableColumnMoveStrategy implements MoveStrategy {
    
    public Point locationSuggested(Widget w, Point originalLocation, Point suggestedLocation) {

        if (w instanceof IndicatorWidget && ((IndicatorWidget)w).getType() == IndicatorWidget.COLUMN)
        {
            IndicatorWidget columnIndicator = (IndicatorWidget)w;
            suggestedLocation.y = originalLocation.y; // Which should be always 0...

            if (suggestedLocation.x < - (w.getBounds().width/2)) suggestedLocation.x = - (w.getBounds().width/2);
            else
            {
                List<Integer> verticalSeparators = ((TableObjectScene)w.getScene()).getVerticalSeparators();

                int tableEnd = verticalSeparators.get( verticalSeparators.size() - 1);

                if (suggestedLocation.x > tableEnd - (w.getBounds().width/2)) suggestedLocation.x = tableEnd - (w.getBounds().width/2);
            }
        }
        
        return suggestedLocation;
    }
}
