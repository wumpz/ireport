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

import com.jaspersoft.ireport.designer.utils.RoundGradientPaint;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.border.Border;

/**
 *
 * @author gtoffoli
 */
public class ThinLineBorder implements Border {

    Color color = Color.BLACK;
    
    public ThinLineBorder(Color c)
    {
        this.color = c;
    }
    private static Insets insets = new Insets(0, 0, 0, 0);
    
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        
        ((Graphics2D)g).setPaint(color);
        ((Graphics2D)g).setStroke( new BasicStroke(1));
        
        Rectangle2D r = new Rectangle2D.Double(x+0.5, y+0.5,width-1.0, height-1.0);
        
        ((Graphics2D)g).draw(r);
        
    }

    public Insets getBorderInsets(Component c) {
        return insets;
    }

    public boolean isBorderOpaque() {
        return true;
    }

}
