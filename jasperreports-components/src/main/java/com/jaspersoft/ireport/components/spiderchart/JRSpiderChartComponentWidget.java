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
package com.jaspersoft.ireport.components.spiderchart;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import net.sf.jasperreports.components.spiderchart.SpiderChartComponent;
import net.sf.jasperreports.components.spiderchart.SpiderPlot;
import net.sf.jasperreports.components.spiderchart.StandardChartSettings;
import net.sf.jasperreports.components.spiderchart.StandardSpiderPlot;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;

/**
 *
 * @author gtoffoli
 */
public class JRSpiderChartComponentWidget extends JRDesignElementWidget {

    private SpiderChartIcon spiderChartIcon = null;

    public JRSpiderChartComponentWidget(AbstractReportObjectScene scene, JRDesignElement element) {
        super(scene, element);

        if (((JRDesignComponentElement)element).getComponent() instanceof SpiderChartComponent)
        {
            SpiderChartComponent c = (SpiderChartComponent)((JRDesignComponentElement)element).getComponent();
            c.getEventSupport().addPropertyChangeListener(this);
            if (c.getPlot() != null)
            {
                ((StandardSpiderPlot)c.getPlot()).getEventSupport().addPropertyChangeListener(this);
            }
            if (c.getChartSettings() != null)
            {
                ((StandardChartSettings)c.getChartSettings()).getEventSupport().addPropertyChangeListener(this);
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {


        if (evt.getPropertyName().equals(SpiderChartComponent.PROPERTY_CHART_SETTINGS) ||
            evt.getPropertyName().equals(SpiderChartComponent.PROPERTY_PLOT) ||

            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_LEGEND_COLOR) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_LEGEND_BACKGROUND_COLOR) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_LEGEND_FONT) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_LEGEND_POSITION) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_TITLE_EXPRESSION) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_TITLE_COLOR) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_TITLE_POSITION) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_TITLE_FONT) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_SHOW_LEGEND) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_SUBTITLE_COLOR) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_SUBTITLE_EXPRESSION) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_SUBTITLE_FONT) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_BACKCOLOR) ||

            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_AXIS_LINE_COLOR) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_AXIS_LINE_WIDTH) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_BACKCOLOR) ||

            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_BACKGROUND_ALPHA) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_FOREGROUND_ALPHA) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_HEAD_PERCENT) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_INTERIOR_GAP) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_LABEL_COLOR) ||

            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_LABEL_FONT) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_LABEL_GAP) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_MAX_VALUE_EXPRESSION) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_ROTATION) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_START_ANGLE) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_TABLE_ORDER) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_WEB_FILLED)
            )
        {
            updateBounds();
            this.repaint();
            this.revalidate(true);
            this.getSelectionWidget().updateBounds();
            this.getSelectionWidget().revalidate(true);
            getScene().validate();
        }
        
        super.propertyChange(evt);
    }

    //@Override
    protected void paintWidgetImplementation1() {


        if (spiderChartIcon == null && ((JRDesignComponentElement)getElement()).getComponent() instanceof SpiderChartComponent)
        {
            spiderChartIcon = new SpiderChartIcon();
        }

        if (spiderChartIcon != null && spiderChartIcon.getIcon(200) != null)
        {
            Graphics2D gr = getScene().getGraphics();
            java.awt.Rectangle r = getPreferredBounds();

            AffineTransform af = gr.getTransform();
            AffineTransform new_af = (AffineTransform) af.clone();
            AffineTransform translate = AffineTransform.getTranslateInstance(
                    getBorder().getInsets().left + r.x,
                    getBorder().getInsets().top + r.y);
            new_af.concatenate(translate);
            gr.setTransform(new_af);

            JRDesignElement e = this.getElement();

            //Composite oldComposite = gr.getComposite();
            Shape oldClip = gr.getClip();
            Shape rect = new Rectangle2D.Float(0,0,e.getWidth(), e.getHeight());
            gr.clip(rect);

            gr.setPaint(
                new GradientPaint(
                    0, e.getHeight(), new Color(255,255,255,(int)(0.25*255)),
                    e.getWidth(), 0, new Color(200,200,200,(int)(0.25*255) )
                )
            );
            gr.fillRect(0, 0, e.getWidth(), e.getHeight());
            //gr.setComposite(oldComposite);
            if (e.getWidth() > 10)
            {
                // Calculate the width....


                Image img_to_paint = spiderChartIcon.getIcon(Math.min( spiderChartIcon.getIcon().getIconWidth(), e.getWidth()),
                        Math.min( spiderChartIcon.getIcon().getIconHeight(), e.getHeight())).getImage();

                Composite oldComposite = gr.getComposite();
                gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                try {
                    gr.drawImage(img_to_paint, e.getWidth()/2 - img_to_paint.getWidth(null)/2,
                                e.getHeight()/2 - img_to_paint.getHeight(null)/2, null);
                } catch (Exception ex){}
                gr.setComposite(oldComposite);
            }
            gr.setClip(oldClip);

            gr.setTransform(af);

        }


    }

}
