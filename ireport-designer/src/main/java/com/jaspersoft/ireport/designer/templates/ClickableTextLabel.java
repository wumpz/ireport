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
package com.jaspersoft.ireport.designer.templates;

import com.jaspersoft.ireport.designer.welcome.TextLabel;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author gtoffoli
 */
public class ClickableTextLabel extends TextLabel {

    public Color normalColor = new Color(109,109,109);
    public Color selectedColor = new Color(22,152,212);
    public Color hoverColor = new Color(210,82,31);

    private boolean selected = false;
    public static final String PROPERTY_SELECTED = "selected";


    public ClickableTextLabel()
    {
        super();

        setForeground(normalColor);

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (!isSelected())
                {
                    setForeground(hoverColor);
                }
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (!isSelected())
                {
                    setForeground(normalColor);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (!selected)
                {
                    setSelected(true);
                    firePropertyChange(PROPERTY_SELECTED, false, true);
                }
            }
        });

    }

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        this.setForeground((selected) ? selectedColor : normalColor);
    }

}
