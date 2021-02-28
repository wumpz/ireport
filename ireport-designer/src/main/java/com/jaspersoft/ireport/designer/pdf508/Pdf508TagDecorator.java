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
package com.jaspersoft.ireport.designer.pdf508;

import com.jaspersoft.ireport.designer.ElementDecorator;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import net.sf.jasperreports.engine.design.JRDesignElement;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;

/**
 *
 * @author gtoffoli
 */
public class Pdf508TagDecorator implements ElementDecorator {

    final static ImageIcon startImage = new javax.swing.ImageIcon(Pdf508TagDecorator.class.getResource("/com/jaspersoft/ireport/designer/pdf508/corner1.png"));
    final static ImageIcon endImage = new javax.swing.ImageIcon(Pdf508TagDecorator.class.getResource("/com/jaspersoft/ireport/designer/pdf508/corner2.png"));

    public void paintWidget(Widget w) {

        if (!IReportManager.getInstance().getPreferences().getBoolean("showPDF508Tags", false)) return;

        Graphics2D gr = w.getScene().getGraphics();

        //Java2DUtils.setClip(gr,getClientArea());
        // Move the gfx 10 pixel ahead...
        Rectangle r = w.getPreferredBounds();

        AffineTransform af = gr.getTransform();
        AffineTransform new_af = (AffineTransform) af.clone();
        AffineTransform translate = AffineTransform.getTranslateInstance(
                w.getBorder().getInsets().left + r.x,
                w.getBorder().getInsets().top + r.y);
        new_af.concatenate(translate);
        gr.setTransform(new_af);

        if (w instanceof JRDesignElementWidget)
        {
            JRDesignElementWidget dew = (JRDesignElementWidget)w;

            String tagValue = "";
            String startString = "";
            String fullString = "";
            String endString = "";

            
            boolean drawstart = false;
            boolean drawend = false;

            String[] tags = {
                "net.sf.jasperreports.export.pdf.tag.h1","H1",
                "net.sf.jasperreports.export.pdf.tag.h2","H2",
                "net.sf.jasperreports.export.pdf.tag.h3","H3",
                "net.sf.jasperreports.export.pdf.tag.table","TBL",
                "net.sf.jasperreports.export.pdf.tag.tr","TR",
                "net.sf.jasperreports.export.pdf.tag.th","TH",
               "net.sf.jasperreports.export.pdf.tag.td","TD"
            };

            for (int i=0; i<tags.length; i += 2)
            {
                boolean start = false;
                boolean end = false;

                String prop = tags[i];
                String label = tags[i+1];

                tagValue = Pdf508TagMenuUtility.getPropertyValue(dew.getElement(),prop);
                if (tagValue.equals("full")) { drawstart = true; drawend=true;  fullString += label + " ";}
                else if (tagValue.equals("start"))  { drawstart = true; startString += label + " ";}
                else if (tagValue.equals("end"))  { drawend = true; endString = label + " " + endString; }

            }

            if (drawstart) drawStart(gr);
            if (drawend) drawEnd(gr, dew.getElement());


            Font f = gr.getFont();
            Color color = gr.getColor();

            startString = startString.trim();
            endString = endString.trim();
            fullString = fullString.trim();

            gr.setFont(new Font("SansSerif", 0, 8));
            gr.setColor(new Color(195,47,193));


            if (startString.length() > 0) {
                gr.drawString(startString, 4, 10);
            }

            if (endString.length() > 0) {
                int strWidth = gr.getFontMetrics().stringWidth(endString);
                gr.drawString(endString, dew.getElement().getWidth() - 3 - strWidth, dew.getElement().getHeight() - 3 );
            }

            if (fullString.length() > 0) {
                int strWidth = 0;
                if (startString.length() > 0) strWidth = gr.getFontMetrics().stringWidth(startString+" ");
                AttributedString as = new AttributedString(fullString);
                as.addAttribute(TextAttribute.FONT, gr.getFont());
                as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                gr.drawString(as.getIterator(), 4 + strWidth, 10);
            }

            gr.setFont(f);
            gr.setColor(color);
        }

        gr.setTransform(af);

    }

    public SystemAction[] getActions(Node node) {

        List<SystemAction> actions = new ArrayList<SystemAction>();
        if (node instanceof ElementNode)
        {
            actions.add(SystemAction.get(Pdf508TagAction.class));
        }
        return actions.toArray(new SystemAction[actions.size()]);
    }

    private void drawFull(Graphics2D gr, JRDesignElement element) {
       drawStart(gr);
       drawEnd(gr, element);
    }
    private void drawEnd(Graphics2D gr, JRDesignElement element) {

        gr.drawImage(endImage.getImage(), element.getWidth()-endImage.getIconWidth() , element.getHeight()-endImage.getIconHeight(), null);
    }
    private void drawStart(Graphics2D gr) {
        gr.drawImage(startImage.getImage(), 0, 0, null);
    }

    public boolean appliesTo(Object designElement) {
        return designElement instanceof JRDesignElement;
    }



}
