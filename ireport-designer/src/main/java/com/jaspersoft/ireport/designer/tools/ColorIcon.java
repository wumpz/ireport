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
package com.jaspersoft.ireport.designer.tools;
import java.awt.*;
import javax.swing.*;

public class ColorIcon implements Icon, SwingConstants {
    private int width = 32;
    private int height = 14;
    private java.awt.Color theColor = java.awt.Color.WHITE;

    public ColorIcon(java.awt.Color c) {
    	theColor = c;
    }

    public int getIconHeight() {
        return height;
    }

    public int getIconWidth() {
        return width;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        
        Color c1 = theColor;
        Color c2 = Color.BLACK;
        
        if (theColor == null)
        {
            c1 = Color.WHITE;
            c2 = Color.GRAY;
        }
        g.setColor(c1);
        
        g.translate(x, y);
        g.fillRect(0, 0, width-1, height-1);
        g.setColor(c2);
        g.drawRect(0, 0, width-1, height-1);
        g.translate(-x, -y);   //Restore graphics object
    }
}
