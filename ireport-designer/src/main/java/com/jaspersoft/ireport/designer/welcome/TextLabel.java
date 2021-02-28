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

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class uses code from Piet Blok released under Apache License Version 2.0.
 *
 * @author gtoffoli
 */
public class TextLabel extends JLabel {

    private int maxWidth = 0;

    JLabel textLabel = null;
    JPanel offScreenPanel = null;

    @Override
    public final Dimension getPreferredSize() {
        Dimension preferred = super.getPreferredSize();
        if (getMaxWidth() > 0 && getMaxWidth() < preferred.width) {
            //preferred =  new Dimension(getMaxWidth(), preferred.height * (int)Math.ceil(preferred.width / (double)getMaxWidth()));
            preferred =  recalculatePreferredSize(maxWidth);
        }
        return preferred;
    }

    /**
     * @return the maxWidth
     */
    public int getMaxWidth() {
        return maxWidth;
    }

    /**
     * @param maxWidth the maxWidth to set
     */
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }



    private Dimension recalculatePreferredSize(int widthLimit) {
        if (textLabel == null)
        {
            textLabel = new JLabel(this.getText());
        }
        textLabel.setBorder(this.getBorder());
        textLabel.setIcon(this.getIcon());
        textLabel.setLocale(this.getLocale());
        textLabel.setDisabledIcon(this.getDisabledIcon());
        textLabel.setFont(this.getFont());
        textLabel.setHorizontalAlignment(this.getHorizontalAlignment());
        textLabel.setHorizontalTextPosition(this.getHorizontalTextPosition());
        textLabel.setVerticalAlignment(this.getVerticalAlignment());
        textLabel.setVerticalTextPosition(this.getVerticalTextPosition());
        textLabel.setIconTextGap(this.getIconTextGap());

        if (offScreenPanel == null)
        {
            offScreenPanel = new JPanel();
            offScreenPanel.setLayout(new BorderLayout());
            offScreenPanel.add(textLabel);
        }

        Dimension initialPreferred = offScreenPanel.getPreferredSize();
        offScreenPanel.setSize(widthLimit, 2 * initialPreferred.height
			+ initialPreferred.height * initialPreferred.width/ widthLimit);

        offScreenPanel.getLayout().layoutContainer(offScreenPanel);
        //offScreenPanel.paint(previewGraphics);

    	return offScreenPanel.getSize();

    }

}
