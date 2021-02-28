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

import com.jaspersoft.ireport.designer.widgets.*;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.crosstab.CrosstabObjectScene;
import com.jaspersoft.ireport.designer.crosstab.widgets.CellSeparatorWidget;
import java.awt.Color;
import java.awt.Point;
import java.util.List;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.widget.SeparatorWidget.Orientation;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class CellSeparatorMoveStrategy implements MoveStrategy {

    boolean reversOrder = false;
    
    public CellSeparatorMoveStrategy()
    {
        this(false);
    }
    
    public CellSeparatorMoveStrategy( boolean reversOrder)
    {
        this.reversOrder =  reversOrder;
    }
    
    public Point locationSuggested(Widget w, Point originalLocation, Point suggestedLocation) {

        if (w instanceof CellSeparatorWidget)
        {
            CellSeparatorWidget separator = (CellSeparatorWidget)w;
            CrosstabObjectScene scene = (CrosstabObjectScene)w.getScene();
            Orientation orientation = ((CellSeparatorWidget)w).getOrientation();
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
        /*
        JRBand b = ((BandSeparatorWidget)w).getBand();
        JasperDesign jd = ((ReportObjectScene)w.getScene()).getJasperDesign();
        
        if (!reversOrder && b.getHeight() == 0)
        {
            // Look for the right band...
             List<JRBand> bands = ModelUtils.getBands(jd);
             JRBand rightBand = bands.get(0);
             for (JRBand tmpBand : bands)
             {
                 if (tmpBand == b) break;
                 if (tmpBand.getHeight() == 0) continue;
                 rightBand = tmpBand;
             }
             b = rightBand;
        }
        
        // y must be between the bottom of the previous band and max design height + band height + current band height

        int bLocation = ModelUtils.getBandLocation(b, jd);
        int maxY = bLocation + ModelUtils.getMaxBandHeight((JRDesignBand)b, jd); 
       
        w.setForeground(ReportObjectScene.EDITING_DESIGN_LINE_COLOR);
        if (bLocation >= suggestedLocation.y) 
        {
            suggestedLocation.y = bLocation;
            w.setForeground(Color.RED);
        }
        else
        {
            suggestedLocation.y = Math.min(maxY, suggestedLocation.y);
            if (suggestedLocation.y == maxY)
            {
                w.setForeground(Color.RED);
            }
        }
        */
        return suggestedLocation;
    }
}
