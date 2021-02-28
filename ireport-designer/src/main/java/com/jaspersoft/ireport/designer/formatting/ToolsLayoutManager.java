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
package com.jaspersoft.ireport.designer.formatting;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 *
 * @author gtoffoli
 */
public class ToolsLayoutManager implements LayoutManager {

    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    public Dimension preferredLayoutSize(Container parent) {
        // Number of columns * heigh....
        int width = parent.getWidth();
        int maxWidth = getMaximumComponentWidth(parent);
        int columns = getColumnsRequired(parent, width);
        
        int component_height = getMaximumComponentHeight(parent);
        int rows = parent.getComponents().length / columns;
        if (parent.getComponentCount() % columns > 0) rows++;
        
        int height = rows * component_height;
        
        if (parent.getParent() != null && parent.getParent() instanceof JScrollPane)
        {
            JScrollPane scrollpane = (JScrollPane)parent.getParent();
            if (height > scrollpane.getVisibleRect().height)
            {
                // need to recalculate all using the new width...
                width -= scrollpane.getVerticalScrollBar().getWidth();
                columns = getColumnsRequired(parent, width);
                rows = parent.getComponents().length / columns;
                if (parent.getComponentCount() % columns > 0) rows++;
            }
        }
        
        return new Dimension(Math.max(maxWidth, width),height);
    }

    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(0,0);
    }

    public void layoutContainer(Container parent) {
        
        int width = parent.getWidth();
        int maxWidth = getMaximumComponentWidth(parent);
        
        int columns = getColumnsRequired(parent, width);
        int columnWidth = width/columns;
        
        int component_height = getMaximumComponentHeight(parent);
        int rows = parent.getComponents().length / columns;
        if (parent.getComponentCount() % columns > 0) rows++;
        
        // Check if vertial scrollbar is requires...
        int height = rows * component_height;
        
        if (parent.getParent() != null && parent.getParent() instanceof JScrollPane)
        {
            JScrollPane scrollpane = (JScrollPane)parent.getParent();
            if (height > scrollpane.getVisibleRect().height)
            {
                // need to recalculate all using the new width...
                width -= scrollpane.getVerticalScrollBar().getWidth();
                columns = getColumnsRequired(parent, width);
                columnWidth = width/columns;
                rows = parent.getComponents().length / columns;
                if (parent.getComponentCount() % columns > 0) rows++;
            }
        }

        int current_column = 0;
        int current_row = -1;
        Component[] components = parent.getComponents();
        for (int i=0; i<components.length; ++i)
        {
            Component c = components[i];
            if (current_column == 0) current_row++;
            
            c.setBounds( current_column*columnWidth, current_row*component_height, 
                         columnWidth, component_height);
            
            current_column++;
            current_column %= columns;
        }
        
    }
    
    private int getColumnsRequired(Container parent, int width)
    {
        int maxWidth = getMaximumComponentWidth(parent);
        int columns = 1;
        if (width > maxWidth && maxWidth > 0 && parent.getComponentCount() > 1)
        {
            columns = width/maxWidth;
        }
        return columns;
    }

    private int getMaximumComponentWidth(Container parent)
    {
        int width = 0;
        Component[] components = parent.getComponents();
        for (int i=0; i<components.length; ++i)
        {
            Component c = components[i];
            width = c.getPreferredSize().width > width ? c.getPreferredSize().width : width;
        }
        return width;
    }
    
    private int getMaximumComponentHeight(Container parent)
    {
        int height = 0;
        Component[] components = parent.getComponents();
        for (int i=0; i<components.length; ++i)
        {
            Component c = components[i];
            height = c.getPreferredSize().height > height ? c.getPreferredSize().height : height;
        }
        return height;
    }
    
}
