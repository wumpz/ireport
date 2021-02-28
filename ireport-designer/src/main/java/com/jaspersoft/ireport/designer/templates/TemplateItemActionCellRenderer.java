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

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 *
 * @author gtoffoli
 */
public class TemplateItemActionCellRenderer extends DefaultListCellRenderer {

    public Color normalColor = new Color(109,109,109);
    public Color selectedColor = new Color(22,152,212);
    public Color hoverColor = new Color(210,82,31);

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, false, false);
        label.setFont(new java.awt.Font("SansSerif", 1, 14));
        label.setForeground( isSelected ?  selectedColor : normalColor);

        if (value instanceof TemplateItemAction)
        {
            label.setText( ((TemplateItemAction)value).getDisplayName() );
            label.setIcon( ((TemplateItemAction)value).getIcon() );
        }
        return label;

    }




}
