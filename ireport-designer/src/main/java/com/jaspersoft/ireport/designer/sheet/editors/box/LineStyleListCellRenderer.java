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
package com.jaspersoft.ireport.designer.sheet.editors.box;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.type.LineStyleEnum;

/**
 *
 * @author gtoffoli
 */
public class LineStyleListCellRenderer extends JComponent implements ListCellRenderer {

    private Color selectionBackground = null;
    private Color  background = null;
    private Color  selectionForeground = null;
    private Color  foreground = null;
    
    private LineStyleEnum styleName = null;
    
    public LineStyleListCellRenderer()
    {
        setOpaque(true);
        setBackground(Color.WHITE);
        selectionBackground = UIManager.getColor("List.selectionBackground");
        background = UIManager.getColor("List.background");
        selectionForeground = UIManager.getColor("List.selectionForeground");
        foreground = UIManager.getColor("List.foreground");
        
        
        setMinimumSize(new Dimension(20,16));
        setPreferredSize(new Dimension(20,16));
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
        if (isSelected)
        {
            setForeground(selectionForeground);
            setBackground(selectionBackground);
        }
        else 
        {
            setForeground(foreground);
            setBackground(background);
        }
        if (value instanceof LineStyleEnum)
        {
            styleName = (LineStyleEnum)value;
        }
        else
        {
            styleName = null;
        }
        repaint();
        return this;
        
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        g.setColor(getBackground());
        g.fillRect(0,0,getWidth(),getHeight());
        if (styleName != null)
        {
            Stroke oldStroke = ((Graphics2D)g).getStroke();
            
            Stroke stroke = null;
            float penWidth = 1f;

            if (styleName == LineStyleEnum.SOLID)
            {
                stroke = (Stroke) new BasicStroke(penWidth);
            }
            else if (styleName == LineStyleEnum.DASHED)
            {
                stroke =  (Stroke) new BasicStroke(penWidth, 
                                            BasicStroke.CAP_BUTT, 
                                            BasicStroke.JOIN_BEVEL, 0f, 
                                            new float[] { 5f, 3f }, 0f);
            }
            else if (styleName == LineStyleEnum.DOTTED)
            {
                stroke =  (Stroke) new BasicStroke(penWidth, 
                                            BasicStroke.CAP_BUTT, 
                                            BasicStroke.JOIN_BEVEL, 0f, 
                                            new float[] { 1f*penWidth, 1f*penWidth }, 0f);
            }
            else if (styleName == LineStyleEnum.DOUBLE)
            {
                stroke =  (Stroke) new BasicStroke((penWidth/3f));
            }
            
            if (stroke != null)
            {
                ((Graphics2D)g).setStroke(stroke);

                g.setColor(getForeground());
                
                if (styleName != LineStyleEnum.DOUBLE)
                {
                    ((Graphics2D)g).drawLine(5, getHeight()/2, getWidth()-5, getHeight()/2);
                }
                else
                {
                    ((Graphics2D)g).drawLine(5, (getHeight()/2) - 1, getWidth()-5, (getHeight()/2) - 1);
                    ((Graphics2D)g).drawLine(5, (getHeight()/2) + 1, getWidth()-5, (getHeight()/2) + 1);
                }
            }
            ((Graphics2D)g).setStroke(oldStroke);
        }
        
        g.setPaintMode();
        g.setColor(Color.LIGHT_GRAY);
        ((Graphics2D)g).drawLine(0, getHeight()-1,  getWidth(), getHeight() - 1);
    }
    
    
}
