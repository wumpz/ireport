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

import java.awt.Point;
import java.awt.event.MouseEvent;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.action.WidgetAction.State;
import org.netbeans.api.visual.action.WidgetAction.WidgetMouseEvent;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @version $Id: ExMoveAction.java 0 2010-08-25 16:25:14 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class ExMoveAction extends WidgetAction.LockedAdapter {

    private int modifiers=0;
    private int modifiersEx=0;

    private MoveStrategy strategy;
    private MoveProvider provider;

    private Widget movingWidget = null;
    private Point dragSceneLocation = null;
    private Point originalSceneLocation = null;
    private Point initialMouseLocation = null;

    public ExMoveAction (MoveStrategy strategy, MoveProvider provider) {
        this.strategy = strategy;
        this.provider = provider;
    }

    protected boolean isLocked () {
        return movingWidget != null;
    }

    public State mousePressed (Widget widget, WidgetMouseEvent event) {
        if (isLocked ())
            return State.createLocked (widget, this);
        if (event.getButton () == MouseEvent.BUTTON1  &&  event.getClickCount () == 1) {
            movingWidget = widget;
            initialMouseLocation = event.getPoint ();
            setModifiers(event.getModifiers());
            setModifiersEx(event.getModifiersEx());
            originalSceneLocation = provider.getOriginalLocation (widget);
            if (originalSceneLocation == null)
                originalSceneLocation = new Point ();
            dragSceneLocation = widget.convertLocalToScene (event.getPoint ());
            provider.movementStarted (widget);
            if (provider instanceof ReportAlignWithMoveStrategyProvider)
            {
                ((ReportAlignWithMoveStrategyProvider)provider).setAction(this);
            }
            return State.createLocked (widget, this);
        }
        return State.REJECTED;
    }

    public State mouseReleased (Widget widget, WidgetMouseEvent event) {

        setModifiers(event.getModifiers());
        setModifiersEx(event.getModifiersEx());

        boolean state;
        if (initialMouseLocation != null  &&  initialMouseLocation.equals (event.getPoint ()))
            state = true;
        else
            state = move (widget, event.getPoint ());
        if (state) {
            movingWidget = null;
            dragSceneLocation = null;
            originalSceneLocation = null;
            initialMouseLocation = null;
            provider.movementFinished (widget);
        }
        return state ? State.CONSUMED : State.REJECTED;
    }

    public State mouseDragged (Widget widget, WidgetMouseEvent event) {

        setModifiers(event.getModifiers());
        setModifiersEx(event.getModifiersEx());

        return move (widget, event.getPoint ()) ? State.createLocked (widget, this) : State.REJECTED;
    }

    private boolean move (Widget widget, Point newLocation) {
        if (movingWidget != widget)
            return false;
        initialMouseLocation = null;
        newLocation = widget.convertLocalToScene (newLocation);
        Point location = new Point (originalSceneLocation.x + newLocation.x - dragSceneLocation.x, originalSceneLocation.y + newLocation.y - dragSceneLocation.y);
        provider.setNewLocation (widget, strategy.locationSuggested (widget, originalSceneLocation, location));
        return true;
    }

    /**
     * @return the modifiers
     */
    public int getModifiers() {
        return modifiers;
    }

    /**
     * @param modifiers the modifiers to set
     */
    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    /**
     * @return the modifiersEx
     */
    public int getModifiersEx() {
        return modifiersEx;
    }

    /**
     * @param modifiersEx the modifiersEx to set
     */
    public void setModifiersEx(int modifiersEx) {
        this.modifiersEx = modifiersEx;
    }


}
