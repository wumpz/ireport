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

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.action.WidgetAction.State;
import org.netbeans.api.visual.action.WidgetAction.WidgetKeyEvent;
import org.netbeans.api.visual.widget.Widget;


/**
 * @author Giulio Toffoli
 * 
 * This action moves all the selected widgets/elements of 1 pixel
 * (or 10 pixels if the shift key is down).
 *  
 */
public class KeyboardElementMoveAction extends WidgetAction.Adapter {

    public KeyboardElementMoveAction() {
        super();
    }

    @Override
    public State keyPressed(Widget widget, WidgetKeyEvent event) {
        
        if (!event.isControlDown())
        {
            if (event.getKeyCode() == KeyEvent.VK_LEFT ||
                event.getKeyCode() == KeyEvent.VK_RIGHT ||
                event.getKeyCode() == KeyEvent.VK_UP ||
                event.getKeyCode() == KeyEvent.VK_DOWN)
            {

                int len = (event.isShiftDown()) ? 10 : 1;

                Point delta = new Point(0,0);
                switch (event.getKeyCode())
                {
                    case KeyEvent.VK_LEFT: delta.x -= len; break;
                    case KeyEvent.VK_RIGHT: delta.x = len; break;
                    case KeyEvent.VK_UP: delta.y -= len; break;
                    case KeyEvent.VK_DOWN: delta.y = len; break;
                }

                List<JRDesignElementWidget> widgets = new ArrayList<JRDesignElementWidget>();
                AbstractReportObjectScene scene = (AbstractReportObjectScene)widget.getScene();

                Iterator iterSelectedObject = scene.getSelectedObjects().iterator();
                while (iterSelectedObject.hasNext())
                {
                    Object obj = iterSelectedObject.next();
                    Widget w = scene.findWidget(obj);
                    if (w instanceof JRDesignElementWidget)
                    {
                        widgets.add((JRDesignElementWidget)w);
                    }
                }

                move(widgets, delta);

                return State.CHAIN_ONLY;
            }
        }
        return State.REJECTED;
    }
    
    private void move (List<JRDesignElementWidget> widgets, Point delta) {

        ArrayList<Widget> changedWidgets = new ArrayList<Widget>();

        for (JRDesignElementWidget dew : widgets)
        {
            Point dewloc = dew.getPreferredLocation();
            dewloc.translate(delta.x, delta.y);
            dewloc = dew.convertLocalToModelLocation(dewloc);
            boolean b = dew.setChanging(true);
            try {
                dew.getElement().setX( dewloc.x);
                dew.getElement().setY( dewloc.y);
            } finally {
                dew.setChanging(b);
            }
            dew.updateBounds();

            // Sync the selection...
            dew.getSelectionWidget().updateBounds();
            
            changedWidgets.add(dew);
        }

        for (JRDesignElementWidget dew : widgets)
        {
            if (dew.getChildrenElements() != null)
            {
                updateChildren(dew, (AbstractReportObjectScene)dew.getScene(), changedWidgets);
            }
        }
    }


    private void updateChildren(JRDesignElementWidget dew, AbstractReportObjectScene scene, ArrayList<Widget> changedWidgets)
    {
          List listOfElements = dew.getChildrenElements();
          
          for (int i=0; i < listOfElements.size(); ++i)
          {
               if (listOfElements.get(i) instanceof JRDesignElement)
               {
                   JRDesignElement element = (JRDesignElement)listOfElements.get(i);
                   JRDesignElementWidget w = (JRDesignElementWidget)scene.findWidget(element);
                   if (changedWidgets.contains(w)) continue;
                   w.updateBounds();
                   w.getSelectionWidget().updateBounds();

                   if (w.getChildrenElements() != null)
                   {
                       updateChildren(w, scene, changedWidgets);
                   }

                   changedWidgets.add(w);
               }
          }
    }
}

