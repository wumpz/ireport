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
package com.jaspersoft.ireport.designer.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gtoffoli
 */
public class MultilineToolbarLayout implements LayoutManager {

    int SPACER = 4;
    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    public Dimension preferredLayoutSize(Container parent) {

        List<List<Component>> component_rows = getComponentRows(parent);

        int height = parent.getInsets().top + parent.getInsets().bottom;
        for (List<Component> comps : component_rows)
        {
            height += getMostHeightComponent(comps);
        }
        height += (SPACER * (component_rows.size() - 1));
        return new Dimension(getMinAvailableWidth(parent), height);

    }

    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(0,0);//preferredLayoutSize(parent);
    }

    public void layoutContainer(Container parent) {

        int currentWidth = 0;
        int currentHeight = 0;

        List<List<Component>> component_rows = getComponentRows(parent);

        for (List<Component> comps : component_rows)
        {
            for (Component c : comps)
            {
                c.setLocation( currentWidth  , currentHeight);
                c.setSize( c.getPreferredSize() );
                currentWidth += c.getWidth() + SPACER;
            }

            currentHeight += getMostHeightComponent(comps) + SPACER;
            currentWidth=0;
        }
    }
    
    private int getMostHeightComponent(List<Component> rowComponents)
    {
        int max = 0;
        for (Component c: rowComponents)
        {
            if (c.getPreferredSize().height > max) max = c.getPreferredSize().height;
        }
        
        return max;
    }

    /**
     * The width is the width of the component or the most wide child.
     * @param parent
     * @return
     */
    public int getMinAvailableWidth(Container parent)
    {
        // get the minimum between the max component width and the parent width...
        int width = parent.getWidth();
        for (int i=0; i<parent.getComponentCount(); ++i)
        {
            Component c = parent.getComponent(i);
            if (width < c.getPreferredSize().width) width = c.getPreferredSize().width;
        }

        return width;
    }

    /**
     * Subdivide all the components in rows that are no wider than the min available width
     * @param parent
     * @return
     */
    public List<List<Component>> getComponentRows(Container parent)
    {

        List<List<Component>> component_rows = new ArrayList<List<Component>>();
        int min_width = getMinAvailableWidth(parent);

        int current_row_index = 0;
        int current_row_width = 0;

        // There is always at least a row...
        for (int i=0; i<parent.getComponentCount(); ++i)
        {
            Component c = parent.getComponent(i);

            if (component_rows.size() == current_row_index)
            {
                component_rows.add(new ArrayList<Component>());
            }

            if (component_rows.get(current_row_index).size() > 0)
            {
                current_row_width += SPACER;
                if (current_row_width + c.getPreferredSize().width > min_width)
                {
                    current_row_index++;
                    current_row_width=0;
                    --i;
                }
                else
                {
                    current_row_width += c.getPreferredSize().width;
                    component_rows.get(current_row_index).add(c);
                }
            }
            else
            {
                current_row_width += c.getPreferredSize().width;
                component_rows.get(current_row_index).add(c);
            }
        }

        return component_rows;
    }

}
