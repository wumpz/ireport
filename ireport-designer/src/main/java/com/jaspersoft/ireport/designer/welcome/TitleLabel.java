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
package com.jaspersoft.ireport.designer.welcome;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.UIManager;

/**
 *
 * @author gtoffoli
 */
public class TitleLabel extends JLabel {

    public TitleLabel()
    {
        super();
        setFont(new Font( null, Font.BOLD, getDefaultFontSize()+12 ));
        setForeground(new Color(154,175,201));
    }
    
    public TitleLabel(String title)
    {
        this();
        setText(title);
        
    }

    /**
     * This function comes from the open IDE (welcome module)
     * @return
     */
    public static int getDefaultFontSize() {

        Integer theCustomFontSize = (Integer)UIManager.get("customFontSize"); // NOI18N

        if (theCustomFontSize != null) {
            return theCustomFontSize.intValue();
        } else {
            Font systemDefaultFont = UIManager.getFont("TextField.font"); // NOI18N
            return (systemDefaultFont != null) ? systemDefaultFont.getSize() : 12;
        }
    }

    @Override
    public void paint(Graphics g) {
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g);
    }



}
