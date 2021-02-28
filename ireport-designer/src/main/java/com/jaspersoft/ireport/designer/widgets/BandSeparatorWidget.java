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
package com.jaspersoft.ireport.designer.widgets;

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.ThreadUtils;
import com.jaspersoft.ireport.designer.utils.Java2DUtils;
import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.SeparatorWidget;

/**
 * A BandBorderWidget is just a line that is sensible to mouse dragging.
 * @author gtoffoli
 */
public class BandSeparatorWidget extends SeparatorWidget implements PropertyChangeListener {

    final private Stroke stroke = new BasicStroke(1.0f);
    final private JRBand band;

    public JRBand getBand() {
        return band;
    }

    public BandSeparatorWidget(ReportObjectScene scene, JRBand b) {
        super(scene, Orientation.HORIZONTAL);
        this.band=b;
        
        // We set a border to improve the sensible area....
        setBorder( BorderFactory.createEmptyBorder(0, 3) );
        setCursor( Cursor.getPredefinedCursor( Cursor.S_RESIZE_CURSOR) );
        setForeground(ReportObjectScene.DESIGN_LINE_COLOR);
        
        // Add a listener to the band changes...
        if (b instanceof JRDesignBand)
        {
            ((JRDesignBand)b).getEventSupport().addPropertyChangeListener(this);
            if (((JRDesignBand)b).getOrigin().getGroupName() != null)
            {
                String gname = ((JRDesignBand)b).getOrigin().getGroupName();
                JRDesignGroup group = (JRDesignGroup) scene.getJasperDesign().getGroupsMap().get(gname);
                if (group != null)
                {
                    group.getEventSupport().addPropertyChangeListener(JRDesignGroup.PROPERTY_NAME , this );
                }
            }
        }
        
        updateBounds();
    }
    
    public void updateBounds()
    {
        JasperDesign jd = ((ReportObjectScene)this.getScene()).getJasperDesign();
        setPreferredLocation(new Point( 0, ModelUtils.getBandLocation(band,jd) + band.getHeight()) );
        setPreferredBounds(new Rectangle( 0,-3,jd.getPageWidth(),7));
    }
    
    
    /**
     * Paints the separator widget.
     */
    @Override
    protected void paintWidget() {
        Graphics2D gr = getGraphics();
        gr.setColor (getForeground());
        Rectangle bounds = getBounds ();
        Insets insets = getBorder ().getInsets ();
        
        gr.setStroke( Java2DUtils.getInvertedZoomedStroke(stroke, 
                this.getScene().getZoomFactor()));
        if (getOrientation() == Orientation.HORIZONTAL)
        {
            Rectangle2D r = new Rectangle2D.Double(0.0,0.0, bounds.width - insets.left - insets.right, 0.0 );
            gr.draw(r);
        }
        else
        {
            Rectangle2D r = new Rectangle2D.Double(0.0,0.0,0.0,bounds.height - insets.top - insets.bottom );
            gr.draw(r);
        }

//        if (getBand() != null)
//        {
//            gr.drawString("" + ModelUtils.getMaxBandHeight((JRDesignBand)getBand(), ((ReportObjectScene)getScene()).getJasperDesign()), 0, 0);
//        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
        Runnable r = null;
        
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignBand.PROPERTY_HEIGHT) ||
            evt.getPropertyName().equals( JRDesignGroup.PROPERTY_NAME))
        {
            r = new Runnable(){  
                 public void run()  {
                    ((ReportObjectScene)getScene()).refreshDocument();
                }};
        }
        else if (evt.getPropertyName().equals( JRDesignBand.PROPERTY_CHILDREN))
        {

            r = new Runnable(){  
                 public void run()  {
                    ((ReportObjectScene)getScene()).refreshElementGroup( (JRDesignBand)band);
                }};
        }
         
        if (r != null)
        {
             ThreadUtils.invokeInAWTThread(r);
        }
    }
}
