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

import com.jaspersoft.ireport.designer.borders.ThinLineBorder;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import org.netbeans.api.visual.action.RectangularSelectDecorator;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class TranslucentRectangularSelectDecorator implements RectangularSelectDecorator {

    private Scene scene;

    public TranslucentRectangularSelectDecorator (Scene scene) {
        this.scene = scene;
    }

    public Widget createSelectionWidget () {
        Widget widget = new Widget(scene);
        //widget.setBorder (scene.getLookFeel ().getMiniBorder (ObjectState.createNormal ().deriveSelected (true)));
        widget.setBorder ( new ThinLineBorder(Color.BLUE));
        widget.setBackground(new Color(0,0,255,60));
        widget.setOpaque(true);
        return widget;
    }
    
    class RectangularWidget extends Widget
    {
        public RectangularWidget(Scene scene)
        {
            super(scene);
        }

        @Override
        protected void paintBackground() {
            
            Graphics2D g = this.getGraphics();
            g.setPaint(getBackground());
            Rectangle b = getBounds();
            Rectangle2D r = new Rectangle2D.Double(b.x +0.5, b.y+0.5, b.width-1.0, b.height-1.0);
            g.fill(r);
        }
        
    }

}
