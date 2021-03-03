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
package com.jaspersoft.ireport.components.html;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import javax.swing.ImageIcon;
import net.sf.jasperreports.components.html.HtmlComponent;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;

/**
 *
 * @author gtoffoli
 */
public class HtmlComponentWidget extends JRDesignElementWidget {

    private ImageIcon htmlIcon = null;

    public HtmlComponentWidget(AbstractReportObjectScene scene, JRDesignElement element) {
        super(scene, element);

//        if (((JRDesignComponentElement)element).getComponent() instanceof HtmlComponent)
//        {
//            HtmlComponent c = (HtmlComponent)((JRDesignComponentElement)element).getComponent();
//            c.getEventSupport().addPropertyChangeListener(this);
//        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {


        /*
        if (evt.getPropertyName().equals(HtmlComponent.PROPERTY_SCALE_TYPE) ||
            evt.getPropertyName().equals(HtmlComponent.PROPERTY_VERTICAL_ALIGN) ||
            evt.getPropertyName().equals(HtmlComponent.PROPERTY_HORIZONTAL_ALIGN))
        {
            updateBounds();
            this.repaint();
            this.revalidate(true);
            this.getSelectionWidget().updateBounds();
            this.getSelectionWidget().revalidate(true);
            getScene().validate();
        }
        */
        super.propertyChange(evt);
    }

    @Override
    protected void paintWidgetImplementation() {

        //super.paintWidget();
        Graphics2D gr = getScene().getGraphics();
        
        if (htmlIcon == null && ((JRDesignComponentElement)getElement()).getComponent() instanceof HtmlComponent)
        {
            htmlIcon = new ImageIcon(this.getClass().getResource("/com/jaspersoft/ireport/components/html/html-32.png"));
        }
        
        Composite oldComposite = gr.getComposite();
        gr.fillRect(0, 0, getElement().getWidth(), getElement().getHeight());
        gr.setComposite(oldComposite);
        Shape oldClip = gr.getClip();
        Shape rect = new Rectangle2D.Float(0,0,getElement().getWidth(), getElement().getHeight());
        gr.clip(rect);
        if (htmlIcon != null)
        {
            gr.drawImage(htmlIcon.getImage(), 4, 4, null);
        }
        gr.setClip(oldClip);

    }

}
