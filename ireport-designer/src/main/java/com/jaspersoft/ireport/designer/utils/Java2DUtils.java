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

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.Stack;

/**
 *
 * @author gtoffoli
 */
public class Java2DUtils
{
  private static Stack clipBoundsStack = new Stack();
  private static Stack transforms = new Stack();

  public static void setClip(Graphics g, int x, int y, int width, int height)
  {
    setClip(g, new Rectangle(x, y, width, height));
  }

    @SuppressWarnings("unchecked")
  public static void setClip(Graphics g, Rectangle clipBounds)
  {
    Rectangle currentClipBounds;

    clipBounds = new Rectangle(clipBounds);
    clipBounds.width += 1;
    clipBounds.height += 1;

    currentClipBounds = g.getClipBounds();
    if(currentClipBounds != null)
    {
      clipBounds = clipBounds.intersection(g.getClipBounds());
    }

    clipBoundsStack.push(currentClipBounds);
    g.setClip(clipBounds);
  }

  public static void resetClip(Graphics g)
  {
    g.setClip((Shape) clipBoundsStack.pop());
  }
  
    @SuppressWarnings("unchecked")
    public static void setTransform(Graphics2D g2, AffineTransform transform)
  {
    AffineTransform current;


    current = g2.getTransform();
    transforms.push(current);
    g2.setTransform(transform);
  }


  public static void resetTransform(Graphics2D g2)
  {
    if(transforms.empty())
    {
      return;
    }


    g2.setTransform((AffineTransform) transforms.pop());
  }
  
  /**
   * This function provides a way to cancel the effects of the zoom on a particular Stroke.
   * All the stroke values (as line width, dashes, etc...) are divided by the zoom factor.
   * This allow to have essentially a fixed Stroke independent by the zoom.
   * The returned Stroke is a copy.
   * Remember to restore the right stroke in the graphics when done.
   * 
   * It works only with instances of BasicStroke
   * 
   * zoom is the zoom factor.
   */
  public static Stroke getInvertedZoomedStroke(Stroke stroke, double zoom)
  {
            if (stroke == null || !(stroke instanceof BasicStroke )) return stroke;
            
            BasicStroke bs = (BasicStroke)stroke;
            float[] dashArray = bs.getDashArray();
            
            float[] newDashArray = null;
            if (dashArray != null)
            {
                newDashArray = new float[dashArray.length];
                for (int i=0; i<newDashArray.length; ++i)
                {
                    newDashArray[i] = (float)(dashArray[i] / zoom);
                }
            }
            
            BasicStroke newStroke = new BasicStroke(       
                            (float)(bs.getLineWidth() / zoom),
                            bs.getEndCap(),
                            bs.getLineJoin(),
                            bs.getMiterLimit(),
                            //(float)(bs.getMiterLimit() / zoom),
                            newDashArray,
                            (float)(bs.getDashPhase() / zoom)
                    );
            return newStroke;
  }
}
