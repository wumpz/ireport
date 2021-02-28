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

import com.jaspersoft.ireport.designer.widgets.SelectionWidget;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.SwingUtilities;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.action.WidgetAction.State;
import org.netbeans.api.visual.action.WidgetAction.WidgetKeyEvent;
import org.netbeans.api.visual.action.WidgetAction.WidgetMouseEvent;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class SelectAction extends WidgetAction.LockedAdapter {

    private boolean aiming = false;
    private Widget aimedWidget = null;
    private boolean invertSelection;
    private SelectProvider provider;
    private boolean rightButton = false;

    public SelectAction (SelectProvider provider) {
        this.provider = provider;
    }

    protected boolean isLocked () {
        return aiming;
    }

    public State mousePressed (Widget widget, WidgetMouseEvent event) {

        if (isLocked ())
            return State.createLocked (widget, this);
        if (event.getButton ()  ==  MouseEvent.BUTTON1 ||  event.getButton () == MouseEvent.BUTTON3)
        {

            rightButton = event.getButton () == MouseEvent.BUTTON3;
            widget.getScene().getView().requestFocus();

            invertSelection = (event.getModifiersEx () & MouseEvent.CTRL_DOWN_MASK) != 0;
            Point localLocation = event.getPoint ();
            if (provider.isSelectionAllowed (widget, localLocation, invertSelection)) {
                aiming = provider.isAimingAllowed (widget, localLocation, invertSelection);
                if (aiming) {
                    updateState (widget, localLocation);
                    return State.createLocked (widget, this);
                } else {
                    provider.select (widget, localLocation, invertSelection);
                    // check if the widget is still selected...
                    Object object = null;
                    if (widget instanceof SelectionWidget)
                    {
                         object = ((ObjectScene)widget.getScene()).findObject ( ((SelectionWidget)widget).getRealWidget() );
                    }
                    else
                    {
                        object = ((ObjectScene)widget.getScene()).findObject (widget);
                    }
                    
                    if (!((ObjectScene)widget.getScene()).getSelectedObjects().contains (object))
                    {
                        return State.CONSUMED;
                    }
                    return State.CHAIN_ONLY;
                }
            }
        }
        return State.REJECTED;
    }

    public State mouseReleased (final Widget widget, final WidgetMouseEvent event) {
        if (aiming) {
            Point point = event.getPoint ();
            updateState (widget, point);
            if (aimedWidget != null)
                provider.select (widget, point, invertSelection);
            updateState (null, null);
            aiming = false;

            if (event.getButton() == MouseEvent.BUTTON3 && rightButton)
            {
                // if there is a PopupMenuAction, use it..

                List<WidgetAction> actions = widget.getActions().getActions();
                for (final WidgetAction action : actions)
                {
                    if (action.getClass().getName().contains("PopupMenuAction"))
                    {
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                action.mousePressed(widget, event);
                                action.mouseReleased(widget, event);
                            }
                        });
                        break;
                    }
                }
            }

            return State.CONSUMED;
        }
        return super.mouseReleased (widget, event);
    }

    private void updateState (Widget widget, Point localLocation) {
        if (widget != null  &&  ! widget.isHitAt (localLocation))
            widget = null;
        if (widget == aimedWidget)
            return;
        if (aimedWidget != null)
            aimedWidget.setState (aimedWidget.getState ().deriveWidgetAimed (false));
        aimedWidget = widget;
        if (aimedWidget != null)
            aimedWidget.setState (aimedWidget.getState ().deriveWidgetAimed (true));
    }

    public State keyTyped (Widget widget, WidgetKeyEvent event) {
        if (! aiming  &&  event.getKeyChar () == KeyEvent.VK_SPACE) {
            provider.select (widget, null, (event.getModifiersEx () & MouseEvent.CTRL_DOWN_MASK) != 0);
            return State.CONSUMED;
        }
        return State.REJECTED;
    }

}
