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
package com.jaspersoft.ireport.components.table;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import net.sf.jasperreports.engine.design.JRDesignElement;

/**
 *
 * @author gtoffoli
 */
public class TableComponentWidget extends JRDesignElementWidget {

    private Image tableImage = null;

    public TableComponentWidget(AbstractReportObjectScene scene, JRDesignElement element) {
        super(scene, element);
    }

    
    @Override
    protected void paintWidgetImplementation() {

        //super.paintWidgetImplementation();

        Graphics2D gr = getScene().getGraphics();

        
        // Move the gfx 10 pixel ahead...
        Rectangle r = getPreferredBounds();

        AffineTransform af = gr.getTransform();
        AffineTransform new_af = (AffineTransform) af.clone();
        AffineTransform translate = AffineTransform.getTranslateInstance(
                getBorder().getInsets().left + r.x,
                getBorder().getInsets().top + r.y);
        new_af.concatenate(translate);
        gr.setTransform(new_af);

        //Composite oldComposite = gr.getComposite();
        Paint oldPaint = gr.getPaint();
        gr.setPaint(new Color(232,232,234,64));
        gr.fillRect(0, 0, getElement().getWidth(), getElement().getHeight());
        gr.setPaint(oldPaint);
        //gr.setComposite(oldComposite);
        Shape oldClip = gr.getClip();
        Shape rect = new Rectangle2D.Float(0,0,getElement().getWidth(), getElement().getHeight());
        gr.clip(rect);
        gr.drawImage(getTableIcon(), 4, 4, null);
        gr.setClip(oldClip);

        gr.setTransform(af);
    }
    
    

    public java.awt.Image getTableIcon() {

        if (tableImage == null)
        {
            tableImage = Misc.loadImageFromResources("/com/jaspersoft/ireport/components/table/table-48.png", this.getClass().getClassLoader());
        }
        return tableImage;
    }


}
