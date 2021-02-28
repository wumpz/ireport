/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2009 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.ireport.designer.borders;


import com.jaspersoft.ireport.designer.utils.Java2DUtils;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.widget.Widget;

/**
 * @author Giulio Toffoli
 */
public final class SimpleLineBorder implements Border {

    private  Widget widget = null;
    public static Color COLOR_1 = new Color(192, 192, 192, 128);
    
    public static final Insets INSETS = new Insets(0, 0, 0, 0); //new Insets(9, 8, 10, 9);
    
    public Insets getInsets() {
        return INSETS;
    }
    
    public SimpleLineBorder()
    {
        super();
    }
    
    public SimpleLineBorder(Widget w)
    {
        super();
        this.widget = w;
    }

    public void paint(Graphics2D gr, Rectangle bounds) {
        
        Stroke oldStroke = gr.getStroke();
                
        Stroke bs = new BasicStroke(1);
        
        if (getWidget() != null && getWidget().getScene() != null)
        {
            double zoom = getWidget().getScene().getZoomFactor();
            bs = Java2DUtils.getInvertedZoomedStroke(bs, zoom);
        }
        
        gr.setStroke(bs);
        
        gr.setPaint(COLOR_1);
        Rectangle2D r = new Rectangle2D.Double (bounds.x + INSETS.left + 0.5, bounds.y + INSETS.top + 0.5, bounds.width - INSETS.left - INSETS.right - 1.0, bounds.height - INSETS.top - INSETS.bottom - 1.0);
        gr.draw(r);
        gr.setStroke(oldStroke);
        
    }

    public boolean isOpaque() {
        return false;
    }

    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }
}
