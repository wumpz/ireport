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
package com.jaspersoft.ireport.designer.styles;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.BeanInfo;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTemplateReference;
import net.sf.jasperreports.engine.type.ModeEnum;
import org.openide.explorer.view.Visualizer;
import org.openide.nodes.Node;

/**
 *
 * @author gtoffoli
 */
public class StyleListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        Node node = Visualizer.findNode(value);
        if (node != null) value = node;
        
        if (value instanceof LibraryStyleNode)
        {
            JRStyle style = ((LibraryStyleNode)value).getStyle();


            String fontName = style.getFontName();
            if (fontName == null) fontName = "SansSerif";
            int size = style.getFontSize() == null ? 10 : style.getFontSize();
            int font_style = 0;
            if (style.isBold() != null && style.isBold().booleanValue()) font_style |= Font.BOLD;
            if (style.isItalic() != null && style.isItalic().booleanValue()) font_style |= Font.ITALIC;

            label.setFont(new Font(fontName,font_style, size));

            String text = style.getName();
            if (style.isStrikeThrough() != null && style.isStrikeThrough().booleanValue()) text = "<s>" + text + "</s>";
            if (style.isUnderline() != null && style.isUnderline().booleanValue()) text = "<u>" + text + "</u>";
            label.setIcon(null);
            label.setText("<html>" +text);

            if (!isSelected)
            {
                label.setForeground(style.getForecolor());
                if (style.getModeValue() != null && style.getModeValue() == ModeEnum.OPAQUE)
                {
                    label.setBackground(style.getBackcolor());
                    label.setOpaque(true);
                }
            }
            
        }
        else if (value instanceof LibraryTemplateReferenceNode)
        {
            JRTemplateReference template = ((LibraryTemplateReferenceNode)value).getReference();
            label.setText( ((LibraryTemplateReferenceNode)value).getDisplayName() );
            Image img = ((LibraryTemplateReferenceNode)value).getIcon(BeanInfo.ICON_COLOR_16x16);
            if (img != null)
            {
                label.setIcon( new ImageIcon(img));
            }
            else
            {
                label.setIcon(null);
            }
        }

        label.setPreferredSize(null);
        Dimension d = label.getPreferredSize();
        if (d != null)
        {
            d.height += 4;
            setPreferredSize(d);
        }

        return label;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.lightGray);
        g.drawLine(4, this.getHeight()-2, this.getWidth()-8, this.getHeight()-2);
    }




}
