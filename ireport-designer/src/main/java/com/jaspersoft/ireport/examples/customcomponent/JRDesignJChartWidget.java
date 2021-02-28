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
package com.jaspersoft.ireport.examples.customcomponent;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.widgets.*;
import com.jaspersoft.ireport.designer.utils.Java2DUtils;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import net.sf.jasperreports.engine.design.JRDesignElement;

/**
 *
 * @author gtoffoli
 */
public class JRDesignJChartWidget extends JRDesignElementWidget {

    private Image chartImage = null;
    
    public JRDesignJChartWidget(AbstractReportObjectScene scene, JRDesignElement element) {
        super(scene, element);
    }
    
    
    @Override
    protected void paintWidgetImplementation() {
        
        Image image = getChartImage();
        
        if (image != null)
        {
            Graphics2D gr = getScene().getGraphics();
        
            //Java2DUtils.setClip(gr,getClientArea());
            // Move the gfx 10 pixel ahead...
            Rectangle r = getPreferredBounds();

            AffineTransform af = gr.getTransform();
            AffineTransform new_af = (AffineTransform) af.clone();
            AffineTransform translate = AffineTransform.getTranslateInstance(
                    getBorder().getInsets().left + r.x,
                    getBorder().getInsets().top + r.y);
            new_af.concatenate(translate);
            gr.setTransform(new_af);

            JRDesignElement e = this.getElement();

            Java2DUtils.setClip(gr,getClientArea());
            gr.drawImage(image, 0, 0, e.getWidth(), e.getHeight(),
                                0, 0,  image.getWidth(null), image.getHeight(null), null);
            Java2DUtils.resetClip(gr);
            
            gr.setTransform(af);
        }
        else
        {
            super.paintWidget();
        }
    }

    public java.awt.Image getChartImage() {
        
        if (chartImage == null)
        {
            chartImage = Misc.loadImageFromResources("/com/jaspersoft/ireport/examples/customcomponent/component.png");
        }
        return chartImage;
    }
}
