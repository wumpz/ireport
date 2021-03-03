/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2013 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.ireport.components.table.widgets;

import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.components.table.TableObjectScene;
import com.jaspersoft.ireport.designer.utils.Java2DUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.SeparatorWidget;

/**
 * A BandBorderWidget is just a line that is sensible to mouse dragging.
 * @author gtoffoli
 */
public class TableCellSeparatorWidget extends SeparatorWidget {

    final private Stroke stroke = new BasicStroke(1.0f);
    private int index = 0;
    
    public TableCellSeparatorWidget(TableObjectScene scene, int index, Orientation orientation) {
        super(scene, orientation);
        this.index=index;
        
        // We set a border to improve the sensible area....
        if (Orientation.HORIZONTAL.equals(orientation))
        {
            setBorder(BorderFactory.createEmptyBorder(0, 3));
            setCursor( Cursor.getPredefinedCursor( Cursor.S_RESIZE_CURSOR) );
       }
        else
        {
            setBorder(BorderFactory.createEmptyBorder(3, 0));
            setCursor( Cursor.getPredefinedCursor( Cursor.W_RESIZE_CURSOR) );
        }
        setForeground(new Color(0,0,0,0));
        updateBounds();
    }
    
    public void updateBounds()
    {
        if (getOrientation() == Orientation.HORIZONTAL)
        {
            setPreferredLocation(new Point( 0, ((TableObjectScene)getScene()).getHorizontalSeparators().get(getIndex())  ));
            setPreferredBounds(new Rectangle( -10,-3, ((TableObjectScene)getScene()).getTableMatrix().getTableDesignWidth()+10,7));
        }
        else
        {
            setPreferredLocation(new Point(((TableObjectScene)getScene()).getVerticalSeparators().get(getIndex()), 0));
            setPreferredBounds(new Rectangle( -3,-10,7, ((TableObjectScene)getScene()).getTableMatrix().getTableDesignHeight()+10));
        }
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
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
