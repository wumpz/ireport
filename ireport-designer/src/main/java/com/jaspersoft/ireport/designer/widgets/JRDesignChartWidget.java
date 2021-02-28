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
package com.jaspersoft.ireport.designer.widgets;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.design.JRDesignChart;

/**
 *
 * @author gtoffoli
 */
public class JRDesignChartWidget extends JRDesignElementWidget {

    private Image chartImage = null;
    private Image staticChartImage = null;
    
    public JRDesignChartWidget(AbstractReportObjectScene scene, JRDesignChart element) {
        super(scene, element);
        ((JRBaseChartPlot)element.getPlot()).getEventSupport().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                JRDesignChartWidget.this.revalidate(true);
                //JRDesignChartWidget.this.repaint();
                JRDesignChartWidget.this.getScene().validate();
            }
        });
    }
    
    /*
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

            JasperDesign jd = ((ReportObjectScene)this.getScene()).getJasperDesign();
            JRDesignChart e = (JRDesignChart)this.getElement();

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
            chartImage = recreateChartImage();
        }
        
        return chartImage;
    }

    public void setChartImage(java.awt.Image image) {
        this.chartImage = image;
    }
    
    
   
    
    
    private Image recreateChartImage()
    {
        JRDesignChart chart = (JRDesignChart)getElement();
        String imgUri = null;
        switch (chart.getChartType())
        {
            case JRDesignChart.CHART_TYPE_AREA:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/area_big.png";
                break;
            case JRDesignChart.CHART_TYPE_BAR:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/bar_big.png";
                break;
            case JRDesignChart.CHART_TYPE_BAR3D:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/bar3d_big.png";
                break;
            case JRDesignChart.CHART_TYPE_BUBBLE:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/bubble_big.png";
                break;
            case JRDesignChart.CHART_TYPE_CANDLESTICK:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/candlestick_big.png";
                break;
            case JRDesignChart.CHART_TYPE_HIGHLOW:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/highlow_big.png";
                break;
            case JRDesignChart.CHART_TYPE_LINE:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/line_big.png";
                break;
            case JRDesignChart.CHART_TYPE_METER:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/meter_big.png";
                break;
            case JRDesignChart.CHART_TYPE_MULTI_AXIS:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/multiaxis_big.png";
                break;
            case JRDesignChart.CHART_TYPE_PIE:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/pie_big.png";
                break;
            case JRDesignChart.CHART_TYPE_PIE3D:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/pie3d_big.png";
                break;
            case JRDesignChart.CHART_TYPE_SCATTER:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/scatter_big.png";
                break;
            case JRDesignChart.CHART_TYPE_STACKEDAREA:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/stackedarea_big.png";
                break;
            case JRDesignChart.CHART_TYPE_STACKEDBAR:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/stackedbar_big.png";
                break;
            case JRDesignChart.CHART_TYPE_STACKEDBAR3D:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/stackedbar3d_big.png";
                break;
            case JRDesignChart.CHART_TYPE_THERMOMETER:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/thermometer_big.png";
                break;
            case JRDesignChart.CHART_TYPE_TIMESERIES:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/timeseries_big.png";
                break;
            case JRDesignChart.CHART_TYPE_XYAREA:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/xyarea_big.png";
                break;
            case JRDesignChart.CHART_TYPE_XYBAR:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/xybar_big.png";
                break;
            case JRDesignChart.CHART_TYPE_XYLINE:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/xyline_big.png";
                break;
            case JRDesignChart.CHART_TYPE_GANTT:
                imgUri = "/com/jaspersoft/ireport/designer/charts/icons/gantt_big.png";
                break;
        }
        
        if (staticChartImage == null && imgUri != null)
        {
            staticChartImage = Misc.loadImageFromResources(imgUri);
        }
        chartImage = staticChartImage;
        
        return chartImage;
    }

     */
    
     @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        JRDesignChartWidget.this.revalidate(true);
        JRDesignChartWidget.this.getScene().validate();
    }
    
    
}
