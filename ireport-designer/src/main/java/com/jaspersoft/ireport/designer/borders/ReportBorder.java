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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.utils.RoundGradientPaint;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.border.Border;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class ReportBorder implements Border {

    private ReportObjectScene scene = null;

    private static Insets insets = new Insets(10, 10, 10, 10);

    public ReportBorder()
    {
        this(null);
    }

    public ReportBorder(ReportObjectScene scene)
    {
        super();
        this.setScene(scene);
    }


    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        
        if (getScene() != null &&
             IReportManager.getInstance().isBackgroundSeparated())
        {
            JasperDesign jd = getScene().getJasperDesign();
            if (jd.getBackground() != null &&
                jd.getBackground().getHeight() > 0)
            {
                int dh = ModelUtils.getDesignHeight(jd);
                int bdh = jd.getBackground().getHeight();
                bdh += jd.getTopMargin();
                bdh += jd.getBottomMargin();

                dh -= bdh;
                dh -= 20;
                
                paintShadowBorder(g, x, y, width, dh);
                paintShadowBorder(g, x, y+dh+20, width, bdh+20);
                
                return;
            }
        }

        paintShadowBorder(g, x, y, width, height);
    }

    public void paintShadowBorder(Graphics g, int x, int y, int width, int height) {

        // TOP ______________________________________________
        Rectangle2D r = new Rectangle2D.Double(x+10, y, width-20, 10);
        GradientPaint gp = new GradientPaint(
                 0f, (float)(y+2), new Color(0,0,0,0),
                 0f, (float)(y+9.5),  new Color(0,0,0,60)); //
        
        
        ((Graphics2D)g).setPaint(gp);
        ((Graphics2D)g).fill(r);
        
        // BOTTOM ______________________________________________
        r = new Rectangle2D.Double(x+10, y+height-10, width-20, 10);
        gp = new GradientPaint(
                 0f, (float)(r.getY()), new Color(0,0,0,60),
                 0f, (float)(r.getY()+7.5),  new Color(0,0,0,0)); //
        ((Graphics2D)g).setPaint(gp);
        ((Graphics2D)g).fill(r);
        
        // LEFT ______________________________________________
        r = new Rectangle2D.Double(x, y+10, 10, height-20);
        gp = new GradientPaint(
                 (float)(r.getX()+2),0f,  new Color(0,0,0,0),
                 (float)(r.getX()+9.5), 0f, new Color(0,0,0,60)); //
        ((Graphics2D)g).setPaint(gp);
        ((Graphics2D)g).fill(r);
        
        // RIGHT ______________________________________________
        r = new Rectangle2D.Double(x+width-10, y+10, 10, height-20);
        gp = new GradientPaint(
                 (float)(r.getX()),0f,  new Color(0,0,0,60),
                 (float)(r.getX()+7.5), 0f, new Color(0,0,0,0)); //
        ((Graphics2D)g).setPaint(gp);
        ((Graphics2D)g).fill(r);
        
        // TOP LEFT ______________________________________________
        r = new Rectangle2D.Double(x, y, 10, 10);
        RoundGradientPaint rgp = new RoundGradientPaint(x+9.8f, y+9.8f, new Color(0,0,0,60), 
                new Point2D.Float(0,6.8f), new Color(0,0,0,0));

        ((Graphics2D)g).setPaint(rgp);
        ((Graphics2D)g).fill(r);
        
        // TOP RIGHT ______________________________________________
        r = new Rectangle2D.Double(x+width-10, y, 10, 10);
        rgp = new RoundGradientPaint(r.getX()+0.5, r.getY()+9.5f, new Color(0,0,0,60), 
                new Point2D.Float(0,6.5f), new Color(0,0,0,0));

        ((Graphics2D)g).setPaint(rgp);
        ((Graphics2D)g).fill(r);
        
         // BOTTOM RIGHT ______________________________________________
        r = new Rectangle2D.Double(x+width-10,  y+height-10, 10, 10);
        rgp = new RoundGradientPaint(r.getX()+0.5, r.getY()+0.5f, new Color(0,0,0,60), 
                new Point2D.Float(0,6.5f), new Color(0,0,0,0));

        ((Graphics2D)g).setPaint(rgp);
        ((Graphics2D)g).fill(r);
        
        r = new Rectangle2D.Double(x,  y+height-10, 10, 10);
        rgp = new RoundGradientPaint(r.getX()+9.5, r.getY()+0.5f, new Color(0,0,0,60), 
                new Point2D.Float(0,6.5f), new Color(0,0,0,0));

        ((Graphics2D)g).setPaint(rgp);
        ((Graphics2D)g).fill(r);
        
        
        
        //((Graphics2D)g).setPaint(Color.RED);
        //((Graphics2D)g).draw(r);
    }

    public Insets getBorderInsets(Component c) {
        return insets;
    }

    public boolean isBorderOpaque() {
        return true;
    }

    /**
     * @return the scene
     */
    public ReportObjectScene getScene() {
        return scene;
    }

    /**
     * @param scene the scene to set
     */
    public void setScene(ReportObjectScene scene) {
        this.scene = scene;
    }

}
