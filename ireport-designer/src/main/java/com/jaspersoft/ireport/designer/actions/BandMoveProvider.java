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
package com.jaspersoft.ireport.designer.actions;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.widgets.*;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Point;
import java.util.List;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.fill.JRFillBand;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class BandMoveProvider implements MoveProvider {

    int startY = 0;
    boolean reversOrder = false;
    
    public BandMoveProvider()
    {
        this(false);
    }
    
    public BandMoveProvider( boolean reversOrder)
    {
        this.reversOrder =  reversOrder;
    }
    
    public void movementStarted(Widget w) {
        
        startY = w.getPreferredLocation().y;
        w.setForeground(ReportObjectScene.EDITING_DESIGN_LINE_COLOR);
    }

    public void movementFinished(Widget w) {
        // we have to update the whole visual things...
        w.setForeground(ReportObjectScene.DESIGN_LINE_COLOR);
        
        ReportObjectScene scene = (ReportObjectScene)w.getScene();
        JRBand b = ((BandSeparatorWidget)w).getBand();
        
        // If it is not reversOrder, find the first band that covers this band.
        // This makes sense only when this band is 0 hight
        if (!reversOrder && b.getHeight() == 0)
        {
            // Look for the right band...
             List<JRBand> bands = ModelUtils.getBands(scene.getJasperDesign());
             JRBand rightBand = bands.get(0);
             for (JRBand tmpBand : bands)
             {
                 if (tmpBand == b) break;
                 if (tmpBand.getHeight() == 0) continue;
                 rightBand = tmpBand;
             }
             b = rightBand;
        }
        
        int delta = w.getPreferredLocation().y - startY;

        // Update the main page...
        int originalHight = b.getHeight();
        int newValue = b.getHeight() + delta;
        ((JRDesignBand)b).setHeight( newValue );
                
        ObjectPropertyUndoableEdit edit = new ObjectPropertyUndoableEdit(
                b, "Height", Integer.TYPE, originalHight,  newValue); // NOI18N
        IReportManager.getInstance().addUndoableEdit(edit);
        
        ((PageWidget)scene.getPageLayer().getChildren().get(0)).updateBounds();
        
        // Update band separators widgets...
        List<Widget> list = scene.getBandSeparatorsLayer().getChildren();
        for (Widget separatorWidget : list)
        {
            ((BandSeparatorWidget)separatorWidget).updateBounds();
        }
        list = scene.getElementsLayer().getChildren();
        for (Widget elementWidget : list)
        {
            ((JRDesignElementWidget)elementWidget).updateBounds();
            ((JRDesignElementWidget)elementWidget).getSelectionWidget().updateBounds();
        }
        
        ((ReportObjectScene)w.getScene()).validate();
        
    }

    public Point getOriginalLocation(Widget widget) {
        return widget.getPreferredLocation();
    }

    public void setNewLocation(Widget widget, Point newLocation) {
        // JRBand b = ((BandSeparatorWidget)w).getBand();
        //((JRDesignBand)b).setHeight( b.getHeight() + (newLocation.y - originalLocation.y));
        // Updatre
        //((ReportObjectScene)w.getScene())
        widget.setPreferredLocation(newLocation);
    }

}
