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
package com.jaspersoft.ireport.designer.jrctx.nodes.editors;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;

import java.beans.PropertyEditorSupport;

// bugfix# 9219 for attachEnv() method
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import java.beans.FeatureDescriptor;
import net.sf.jasperreports.chartthemes.simple.PaintProvider;
import net.sf.jasperreports.engine.util.JRColorUtil;
import org.openide.nodes.Node;

/** 
 * A property editor for PaintProvider values.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JasperDesignViewer.java 1988 2007-12-04 16:17:57Z teodord $
 */
public class PaintProviderPropertyEditor extends PropertyEditorSupport implements ExPropertyEditor
{
    public boolean isEditable(){
        return false;
    }

    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public void paintValue(Graphics grx, Rectangle rectangle) 
    {
        PaintProvider paintProvider = getValue() instanceof PaintProvider ? (PaintProvider)getValue() : null;
        if (paintProvider == null)
        {
            super.paintValue(grx, rectangle);
        }
        else
        {
            Color color = grx.getColor();
            Paint paint = paintProvider.getPaint();
            int px = 18;
            
            grx.drawRect(rectangle.x, rectangle.y + rectangle.height / 2 - 5 , 10, 10);

            GradientPaint gradientPaint = paint instanceof GradientPaint ? (GradientPaint)paint : null;
            if (gradientPaint != null)
            {
                //grx.drawRect(rectangle.x, rectangle.y + rectangle.height / 2 - 5 , 20, 10);
                paint = new GradientPaint(0, rectangle.y + rectangle.height / 2 - 4, gradientPaint.getColor1(), 0, rectangle.y + rectangle.height / 2 + 5, gradientPaint.getColor2());
                //paint = new GradientPaint(1, 1, gradientPaint.getColor1(), 19, 1, gradientPaint.getColor2());
                ((Graphics2D)grx).setPaint(paint);
                //grx.fillRect(rectangle.x + 1, rectangle.y + rectangle.height / 2 - 4 , 19, 9);
                //px = 28;
            }
            else
            {
                //grx.drawRect(rectangle.x, rectangle.y + rectangle.height / 2 - 5 , 10, 10);
                ((Graphics2D)grx).setPaint(paint);
                //grx.fillRect(rectangle.x + 1, rectangle.y + rectangle.height / 2 - 4 , 9, 9);
                //px = 18;
            }

            grx.fillRect(rectangle.x + 1, rectangle.y + rectangle.height / 2 - 4 , 9, 9);
            
            grx.setColor(color);

            FontMetrics fm = grx.getFontMetrics();
            grx.drawString(getAsText(), rectangle.x + px, rectangle.y +
                          (rectangle.height - fm.getHeight()) / 2 + fm.getAscent());
        }
    }
    
    /** sets new value */
    @Override
    public String getAsText() 
    {
        PaintProvider paintProvider = getValue() instanceof PaintProvider ? (PaintProvider)getValue() : null;
        if (paintProvider == null || paintProvider.getPaint() == null)
        {
            return "";
        }
        else
        {
            Paint paint = paintProvider.getPaint();

            GradientPaint gradientPaint = paint instanceof GradientPaint ? (GradientPaint)paint : null;
            if (gradientPaint != null)
            {
                return 
                    "#" + JRColorUtil.getColorHexa(gradientPaint.getColor1())
                    + " #" + JRColorUtil.getColorHexa(gradientPaint.getColor2());
            }
            else
            {
                return 
                    "#" + JRColorUtil.getColorHexa((Color)paint);
            }
        }
    }
    
    /** sets new value */
    @Override
    public void setAsText(String s) {
        return;
    }

    @Override
    public boolean supportsCustomEditor () {
        return customEd;
    }

    @Override
    public java.awt.Component getCustomEditor ()
    {
        PaintProvider paintProvider = getValue() instanceof PaintProvider ? (PaintProvider)getValue() : null;
        return new PaintProviderPropertyEditorPanel(paintProvider, false, null, this, env); // NOI18N
    }

    //private String instructions=null;
    //private boolean oneline=false;
    private boolean customEd=true;
    private PropertyEnv env;

    // bugfix# 9219 added attachEnv() method checking if the user canWrite in text box 
    public void attachEnv(PropertyEnv env) 
    {
        FeatureDescriptor desc = env.getFeatureDescriptor();
        if (desc instanceof Node.Property){
            Node.Property prop = (Node.Property)desc;
            //enh 29294 - support one-line editor & suppression of custom
            //editor
            //instructions = (String) prop.getValue ("instructions"); //NOI18N
            //oneline = Boolean.TRUE.equals (prop.getValue ("oneline")); //NOI18N
            customEd = !Boolean.TRUE.equals (prop.getValue ("suppressCustomEditor")); //NOI18N
        }
        this.env = env;
    }
}

