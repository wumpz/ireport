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
package com.jaspersoft.ireport.components.list;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.utils.Java2DUtils;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import net.sf.jasperreports.components.list.DesignListContents;
import net.sf.jasperreports.components.list.ListComponent;
import net.sf.jasperreports.components.list.StandardListComponent;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;

/**
 *
 * @author gtoffoli
 */
public class JRListComponentWidget extends JRDesignElementWidget {

    private static final BasicStroke DOTTED_LINE = new BasicStroke(
      1f,
      BasicStroke.CAP_ROUND,
      BasicStroke.JOIN_ROUND,
      1f,
      new float[] {2f},
      0f);

    private Image chartImage = null;

    public JRListComponentWidget(AbstractReportObjectScene scene, JRDesignElement element) {
        super(scene, element);

        if (((JRDesignComponentElement)element).getComponent() instanceof ListComponent)
        {
            ListComponent c = (ListComponent) ((JRDesignComponentElement)element).getComponent();
            DesignListContents contents = (DesignListContents) c.getContents();
            contents.getEventSupport().addPropertyChangeListener(this);
        }

        PropertyChangeListener pcl = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                    ListComponent c = (ListComponent) ((JRDesignComponentElement)getElement()).getComponent();
                    DesignListContents contents = (DesignListContents) c.getContents();
                    contents.setHeight( getElement().getHeight() );
                    contents.setWidth( getElement().getWidth() );
            }
        };

        getElement().getEventSupport().addPropertyChangeListener(JRDesignElement.PROPERTY_HEIGHT, pcl);
        getElement().getEventSupport().addPropertyChangeListener(JRDesignElement.PROPERTY_WIDTH, pcl);

    }




    
    @Override
    protected void paintWidgetImplementation() {

        super.paintWidgetImplementation();

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

        JRDesignElement element = this.getElement();
        
        StandardListComponent component = (StandardListComponent) ((JRDesignComponentElement)getElement()).getComponent();
        
        Shape oldClip = gr.getClip();
        Shape rect = new Rectangle2D.Float(0,0,element.getWidth(), element.getHeight());

        Stroke oldStroke = gr.getStroke();
        gr.clip(rect);
        gr.setStroke(DOTTED_LINE);
        gr.setColor(ReportObjectScene.DESIGN_LINE_COLOR );
        gr.drawLine( 0, component.getContents().getHeight() , element.getWidth(), component.getContents().getHeight());

        if (component.getContents().getWidth() != null && component.getContents().getWidth().intValue() != 0)
        {
            gr.drawLine( component.getContents().getWidth(), 0 , component.getContents().getWidth(), element.getHeight());
        }

        gr.setClip(oldClip);
        gr.setStroke(oldStroke);

        gr.setTransform(af);
    }
    
    

    public java.awt.Image getChartImage() {

        if (chartImage == null)
        {
            chartImage = Misc.loadImageFromResources("/com/jaspersoft/ireport/components/list/component.png", this.getClass().getClassLoader());
        }
        return chartImage;
    }

    @Override
    public List getChildrenElements() {

        JRDesignComponentElement component = (JRDesignComponentElement)getElement();
        if (component.getComponent() instanceof ListComponent)
        {
            ListComponent c = (ListComponent) component.getComponent();
            DesignListContents contents = (DesignListContents) c.getContents();
            return contents.getChildren();
        }
        
        return null;
    }

    /**
     * If a widget can have sub elements, this is the way the elements are
     * added.
     * @param element
     */
    public void addElement(JRDesignElement element)
    {
        JRDesignComponentElement component = (JRDesignComponentElement)getElement();
        if (component.getComponent() instanceof ListComponent)
        {
            ListComponent c = (ListComponent) component.getComponent();
            DesignListContents contents = (DesignListContents) c.getContents();
            contents.addElement(element);
        }
    }


}
