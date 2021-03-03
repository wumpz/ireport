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
package com.jaspersoft.ireport.components.sort;

import com.jaspersoft.ireport.components.map.MapIcon;
import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import net.sf.jasperreports.components.map.StandardMapComponent;
import net.sf.jasperreports.components.sort.SortComponent;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;

/**
 *
 * @author gtoffoli
 */
public class SortComponentWidget extends JRDesignElementWidget {

    public SortComponentWidget(AbstractReportObjectScene scene, JRDesignElement element) {
        super(scene, element);

         if (((JRDesignComponentElement)element).getComponent() instanceof SortComponent)
        {
            SortComponent c = (SortComponent)((JRDesignComponentElement)element).getComponent();
            c.getEventSupport().addPropertyChangeListener(this);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {


        if (evt.getPropertyName().equals(SortComponent.PROPERTY_HANDLER_COLOR) ||
            evt.getPropertyName().equals(SortComponent.PROPERTY_SYMBOL_FONT) ||
            evt.getPropertyName().equals(SortComponent.PROPERTY_HANDLER_HORIZONTAL_ALIGN) ||
            evt.getPropertyName().equals(SortComponent.PROPERTY_HANDLER_VERTICAL_ALIGN) )
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

    @Override
    protected void paintWidgetImplementation() {

        Graphics2D gr = getScene().getGraphics();
        Color oldColor =gr.getColor();
        Font oldFont = gr.getFont();
        Shape oldClip = gr.getClip();
        
        AffineTransform af = gr.getTransform();
            
        try {
            
            
            java.awt.Rectangle r = getPreferredBounds();

            
            AffineTransform new_af = (AffineTransform) af.clone();
            AffineTransform translate = AffineTransform.getTranslateInstance(
                    getBorder().getInsets().left + r.x,
                    getBorder().getInsets().top + r.y);
            new_af.concatenate(translate);
            gr.setTransform(new_af);

            
            JRDesignElement e = this.getElement();

            //Composite oldComposite = gr.getComposite();
            
            Shape rect = new Rectangle2D.Float(0,0,e.getWidth(), e.getHeight());
            gr.clip(rect);


            if (getBackground() != null && e.getModeValue() == ModeEnum.OPAQUE)
            {
                gr.setColor( e.getBackcolor() );
                gr.fillRect(0, 0, e.getWidth(), e.getHeight());
            }

            gr.setPaint( Color.lightGray);
            gr.drawRect(0, 0, e.getWidth()-1, e.getHeight()-1);

            // Draw the small arrow in the center...

            SortComponent c = (SortComponent)((JRDesignComponentElement)e).getComponent();

            /*
            int size = 10;
            try {
                size = Integer.parseInt(c.getHandlerFontSize());
            } catch (Exception ex)
            {
                size = 10;
            }
             
             */

            JRFont jrFont = c.getSymbolFont();
            
            if (jrFont != null)
            {
                
                Font f = new java.awt.Font(jrFont.getFontName(), jrFont.isBold() ? Font.BOLD : Font.PLAIN, jrFont.getFontSize() );
                gr.setFont(f);
            }

            
            Rectangle2D stringBounds = gr.getFontMetrics().getStringBounds("\u25B2", gr);

            
            Color col = Color.white;
            if (c.getHandlerColor() != null) col = c.getHandlerColor();

            gr.setColor( col );

            int x=0;
            if (c.getHandlerHorizontalAlign() == null) x=0;
            else if(c.getHandlerHorizontalAlign() == HorizontalAlignEnum.CENTER) x = (int)((e.getWidth()-stringBounds.getWidth())/2);
            else if(c.getHandlerHorizontalAlign() == HorizontalAlignEnum.RIGHT) x = (int)(e.getWidth()-stringBounds.getWidth());

            int y=0;
            if (c.getHandlerVerticalAlign() == null) y=(int)(stringBounds.getHeight());
            else if(c.getHandlerVerticalAlign() == VerticalAlignEnum.TOP) y = (int)(stringBounds.getHeight());
            else if(c.getHandlerVerticalAlign() == VerticalAlignEnum.MIDDLE) y = (int)((e.getHeight()+stringBounds.getHeight())/2);
            else if(c.getHandlerVerticalAlign() == VerticalAlignEnum.BOTTOM) y = (int)(e.getHeight());

            gr.drawString("\u25B2", x, y);
            
            

        } catch (Exception ex)
        {
        } 
        finally {

            gr.setTransform(af);
            gr.setColor(oldColor);
            gr.setFont(oldFont);
            gr.setClip(oldClip);
        }
        //super.paintWidgetImplementation();

    }

}
