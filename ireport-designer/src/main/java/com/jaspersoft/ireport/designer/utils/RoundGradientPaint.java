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

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.ColorModel;
 
/**
 * @author gtoffoli
 */
public class RoundGradientPaint
    implements Paint {
  protected Point2D mPoint;
  protected Point2D mRadius;
  protected Color mPointColor, mBackgroundColor;
  
  public RoundGradientPaint(double x, double y, Color pointColor,
      Point2D radius, Color backgroundColor) {
    if (radius.distance(0, 0) <= 0)
      throw new IllegalArgumentException("Radius must be greater than 0.");
    mPoint = new Point2D.Double(x, y);
    mPointColor = pointColor;
    mRadius = radius;
    mBackgroundColor = backgroundColor;
  }
  
  public PaintContext createContext(ColorModel cm,
      Rectangle deviceBounds, Rectangle2D userBounds,
      AffineTransform xform, RenderingHints hints) {
    Point2D transformedPoint = xform.transform(mPoint, null);
    Point2D transformedRadius = xform.deltaTransform(mRadius, null);
    return new RoundGradientContext(transformedPoint, mPointColor,
        transformedRadius, mBackgroundColor);
  }
  
  public int getTransparency() {
    int a1 = mPointColor.getAlpha();
    int a2 = mBackgroundColor.getAlpha();
    return (((a1 & a2) == 0xff) ? OPAQUE : TRANSLUCENT);
  }
}


