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
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.netbeans.api.visual.action.AlignWithMoveDecorator;
import org.netbeans.api.visual.action.AlignWithWidgetCollector;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class AlignWithSupport {
    private static final int GRAVITY = 10;

    private AlignWithWidgetCollector collector;
    private LayerWidget interractionLayer;
    private AlignWithMoveDecorator decorator;

    private ConnectionWidget lineWidget1, lineWidget2;

    public AlignWithSupport (AlignWithWidgetCollector collector, LayerWidget interractionLayer, AlignWithMoveDecorator decorator) {
        this.collector = collector;
        this.interractionLayer = interractionLayer;
        this.decorator = decorator;
    }

    protected Point locationSuggested (Widget widget, Rectangle sceneWidgetBounds, Point suggestedLocation, boolean horizontal, boolean vertical, boolean bothSides, boolean snapHack) {
        Point point = new Point (suggestedLocation);
        
        if (IReportManager.getPreferences().getBoolean("noMagnetic", false)) return point;

        Collection<Rectangle> regions = collector.getRegions (widget);

        if (horizontal) {
            boolean snap = false;
            int xs = 0, x = 0, dx = 0, y1 = 0, y2 = 0;

            int b1 = sceneWidgetBounds.x;
            int b2 = sceneWidgetBounds.x + sceneWidgetBounds.width;

            for (Rectangle rectangle : regions) {
                int a1 = rectangle.x;
                int a2 = a1 + rectangle.width;

                int d;
                boolean snapNow = false;

                d = Math.abs (a1 - b1);
                if ((snap && d < dx) || (!snap && d < GRAVITY)) {
                    snap = snapNow = true;
                    x = xs = a1;
                    dx = d;
                }

                if (bothSides) {
                    d = Math.abs (a1 - b2);
                    if ((snap && d < dx) || (!snap && d < GRAVITY)) {
                        snap = snapNow = true;
                        x = a1;
                        xs = a1 - sceneWidgetBounds.width;
                        dx = d;
                    }
                }

                d = Math.abs (a2 - b1);
                if ((snap && d < dx) || (!snap && d < GRAVITY)) {
                    snap = snapNow = true;
                    x = xs = a2;
                    dx = d;
                }

                if (bothSides) {
                    d = Math.abs (a2 - b2);
                    if ((snap && d < dx) || (!snap && d < GRAVITY)) {
                        snap = snapNow = true;
                        x = a2;
                        xs = a2 - sceneWidgetBounds.width;
                        dx = d;
                    }
                }

                if (snapNow) {
                    y1 = rectangle.y;
                    y2 = rectangle.y + rectangle.height;
                }
            }

            if (snap) {
                point.x = xs;
                if (snapHack)
                    point.x -= widget.getBounds ().x;
            }


            if (interractionLayer != null)
                lineWidget1.setControlPoints (snap ? Arrays.asList (
                    new Point (x, Math.min (sceneWidgetBounds.y, y1)),
                    new Point (x, Math.max (sceneWidgetBounds.y + sceneWidgetBounds.height, y2))
                ) : Collections.<Point>emptyList (), true);
        }

        if (vertical) {
            boolean snap = false;
            int ys = 0, y = 0, dy = 0, x1 = 0, x2 = 0;

            int b1 = sceneWidgetBounds.y;
            int b2 = sceneWidgetBounds.y + sceneWidgetBounds.height;

            for (Rectangle rectangle : regions) {
                int a1 = rectangle.y;
                int a2 = a1 + rectangle.height;

                int d;
                boolean snapNow = false;

                d = Math.abs (a1 - b1);
                if ((snap && d < dy) || (!snap && d < GRAVITY)) {
                    snap = snapNow = true;
                    y = ys = a1;
                    dy = d;
                }

                d = Math.abs (a1 - b2);
                if ((snap && d < dy) || (!snap && d < GRAVITY)) {
                    snap = snapNow = true;
                    ys = a1 - sceneWidgetBounds.height;
                    y = a1;
                    dy = d;
                }

                d = Math.abs (a2 - b1);
                if ((snap && d < dy) || (!snap && d < GRAVITY)) {
                    snap = snapNow = true;
                    y = ys = a2;
                    dy = d;
                }

                d = Math.abs (a2 - b2);
                if ((snap && d < dy) || (!snap && d < GRAVITY)) {
                    snap = snapNow = true;
                    ys = a2 - sceneWidgetBounds.height;
                    y = a2;
                    dy = d;
                }

                if (snapNow) {
                    x1 = rectangle.x;
                    x2 = rectangle.x + rectangle.width;
                }
            }
            if (snap) {
                point.y = ys;
                if (snapHack)
                    point.y -= widget.getBounds ().y;
            }

            if (interractionLayer != null)
                lineWidget2.setControlPoints (snap ? Arrays.asList (
                    new Point (Math.min (sceneWidgetBounds.x, x1), y),
                    new Point (Math.max (sceneWidgetBounds.x + sceneWidgetBounds.width, x2), y)
                ) : Collections.<Point>emptyList (), true);
        }

        return point;
    }

    public void show () {
        if (interractionLayer != null) {
            if (lineWidget1 == null)
                lineWidget1 = decorator.createLineWidget (interractionLayer.getScene ());
            if (lineWidget2 == null)
                lineWidget2 = decorator.createLineWidget (interractionLayer.getScene ());
            interractionLayer.addChild (lineWidget1);
            interractionLayer.addChild (lineWidget2);
            lineWidget1.setControlPoints (Collections.<Point>emptySet (), true);
            lineWidget2.setControlPoints (Collections.<Point>emptySet (), true);
        }
    }

    public void hide () {
        if (interractionLayer != null) {
            if (lineWidget1 != null) interractionLayer.removeChild (lineWidget1);
            if (lineWidget2 != null) interractionLayer.removeChild (lineWidget2);
        }
    }
    
    public Point snapToGrid(Point suggestedLocation, int sizeGrid)
    {
        
        if (IReportManager.getPreferences().getBoolean("noMagnetic", false)) return suggestedLocation;
        
        int d = (suggestedLocation.x)%sizeGrid;
        if (d < sizeGrid/2)
        {
            suggestedLocation.x -= d; 
        }
        else
        {
            suggestedLocation.x += sizeGrid -d; 
        }
        
        d = (suggestedLocation.y)%sizeGrid;
        if (d < sizeGrid/2)
        {
            suggestedLocation.y -= d; 
        }
        else
        {
            suggestedLocation.y += sizeGrid - d; 
        }

        return suggestedLocation;
    }
}
