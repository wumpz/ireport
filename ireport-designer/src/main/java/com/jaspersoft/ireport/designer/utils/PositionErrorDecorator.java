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

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.ElementDecorator;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.widgets.ErrorWidget;
import com.jaspersoft.ireport.designer.widgets.ErrorsLayer;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;

/**
 *
 * @version $Id: PositionErrorDecorator.java 0 2009-12-02 16:30:36 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class PositionErrorDecorator implements ElementDecorator {

    private static ImageIcon warningIcon = null;

    public void paintWidget(final Widget w) {
        
        if (w instanceof JRDesignElementWidget)
        {
            // get the current jasperDesigner...
            JasperDesign jd = findJasperDesign(w);
            if (jd == null) return;

            JRDesignElementWidget elementWidget = (JRDesignElementWidget)w;
            if (IReportManager.getInstance().getPreferences().getBoolean("showPositionErrors", true) &&
                !((AbstractReportObjectScene)elementWidget.getScene()).isValidPosition(elementWidget))
            {
                // Draw a red frame....
                Graphics2D gr = w.getScene().getGraphics();

                Rectangle r = w.getPreferredBounds();

                AffineTransform af = gr.getTransform();
                AffineTransform new_af = (AffineTransform) af.clone();
                AffineTransform translate = AffineTransform.getTranslateInstance(
                        w.getBorder().getInsets().left + r.x,
                        w.getBorder().getInsets().top + r.y);
                new_af.concatenate(translate);
                gr.setTransform(new_af);

                gr.setColor(new Color(255,0,0,128));
                gr.setStroke(new BasicStroke(2.0f));
                gr.drawRect(0, 0, r.width, r.height);

                if (warningIcon == null)
                {
                    warningIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/errorhandler/warning.png"));
                }
                gr.drawImage(warningIcon.getImage(), r.width-warningIcon.getIconWidth()+5, -5, null);
                gr.setTransform(af);

                // add error widget...
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        ErrorWidget errorWidget = findErrorWidget(w);
                        if (errorWidget == null)
                        {
                            errorWidget = new ErrorWidget(w.getScene(),w);
                            //errorWidget.setBackground(new Color(255,0,0,128));
                            //errorWidget.setOpaque(true);
                            errorWidget.setToolTipText("<html><img src=\""+ getClass().getResource("/com/jaspersoft/ireport/designer/resources/errorhandler/warning.png") + "\"> Warning: the element position is invalid.");
                            findErrorsLayer(w, true).addChild(errorWidget);
                            errorWidget.setPreferredLocation(w.getPreferredLocation());
                            errorWidget.setPreferredBounds(w.getPreferredBounds());
                            w.getScene().validate();
                        }
                    }
                });
                
            }
            else
            {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        ErrorWidget errorWidget = findErrorWidget(w);
                        if (errorWidget != null) errorWidget.removeFromParent();
                    }
                });
                
            }
        }


    }

    public SystemAction[] getActions(Node node) {
        return new SystemAction[0];
    }

    public boolean appliesTo(Object designElement) {
        return designElement instanceof JRElement;
    }

    private JasperDesign findJasperDesign(Widget w) {
        if (w.getScene() instanceof AbstractReportObjectScene)
        {
            return ((AbstractReportObjectScene)w.getScene()).getJasperDesign();
        }
        return null;
    }

    private ErrorsLayer findErrorsLayer(Widget w, boolean create) {
        Scene scene = w.getScene();

        List<Widget> layers = scene.getChildren();
        for (Widget layer : layers)
        {
            if (layer instanceof ErrorsLayer)
            {
                return (ErrorsLayer)layer;
            }
        }
        ErrorsLayer errLayer = null;
        if (create)
        {
            errLayer = new ErrorsLayer(scene);
            scene.addChild(errLayer);
            scene.validate();
        }
        return errLayer;
    }

    private ErrorWidget findErrorWidget(Widget w) {

        ErrorsLayer layer = findErrorsLayer(w, false);
        if (layer == null) return null;
        List<Widget> widgets = layer.getChildren();
        for (Widget widget : widgets)
        {
            if (widget instanceof ErrorWidget &&
                ((ErrorWidget)widget).getReferringWidget() == w)
            {
                return (ErrorWidget)widget;
            }
        }

        return null;

    }

}
