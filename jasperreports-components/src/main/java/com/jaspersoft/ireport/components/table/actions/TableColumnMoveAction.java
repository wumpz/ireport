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

import com.jaspersoft.ireport.components.table.widgets.IndicatorWidget;
import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author David Kaspar
 */
public class TableColumnMoveAction extends WidgetAction.LockedAdapter {

    private MoveStrategy strategy;
    private MoveProvider provider;

    private Widget movingWidget = null;
    private Point dragSceneLocation = null;
    private Point originalSceneLocation = null;
    
    public TableColumnMoveAction () {
        this.strategy = new TableColumnMoveStrategy();
        this.provider = new TableColumnMoveProvider();
    }

    protected boolean isLocked () {
        return movingWidget != null;
    }

    public State mousePressed (Widget widget, WidgetMouseEvent event) {
        if (event.getButton () == MouseEvent.BUTTON1  &&  event.getClickCount () == 1) {

                movingWidget = widget;
                
                ((AbstractReportObjectScene)movingWidget.getScene()).getJComponent().setCursor(IndicatorWidget.getClosedHandCursor());

                ((IndicatorWidget)movingWidget).setMovingColumnMode(true);

                originalSceneLocation = provider.getOriginalLocation (widget);
                if (originalSceneLocation == null)
                    originalSceneLocation = new Point ();
                dragSceneLocation = widget.convertLocalToScene (event.getPoint ());
                provider.movementStarted (widget);
                return State.createLocked (widget, this);
        }
        return State.REJECTED;
    }

    @Override
    public State mouseReleased (Widget widget, WidgetMouseEvent event) {
        boolean state = move (widget, event.getPoint ());
        if (state) {
            ((AbstractReportObjectScene)movingWidget.getScene()).getJComponent().setCursor(IndicatorWidget.getOpenHandCursor());
            ((IndicatorWidget)movingWidget).setMovingColumnMode(false);
            movingWidget = null;
            provider.movementFinished(widget);
        }
        return state ? State.CONSUMED : State.REJECTED;
    }

    @Override
    public State mouseDragged (Widget widget, WidgetMouseEvent event) {
        return move (widget, event.getPoint ()) ? State.createLocked (widget, this) : State.REJECTED;
    }

    private boolean move (Widget widget, Point newLocation) {
        if (movingWidget != widget)
            return false;
        newLocation = widget.convertLocalToScene (newLocation);
        Point location = new Point (originalSceneLocation.x + newLocation.x - dragSceneLocation.x, originalSceneLocation.y + newLocation.y - dragSceneLocation.y);
        provider.setNewLocation (widget, strategy.locationSuggested (widget, originalSceneLocation, location));
        return true;
    }

}

