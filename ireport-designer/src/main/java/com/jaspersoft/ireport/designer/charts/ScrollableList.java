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
package com.jaspersoft.ireport.designer.charts;
import java.awt.*;
import javax.swing.*;

public class ScrollableList extends JList
{
   protected boolean trackWidth = true;
   protected boolean trackHeight = false;

   public ScrollableList()
   {
      super();
      setCellRenderer(new ChartCellRenderer());
   }
   
   public int getVisibleRowCount()
   {
      return 0;
   }
   
   public boolean getScrollableTracksViewportWidth()
   {
      return trackWidth;
   }

   public void setScrollableTracksViewportWidth(
      boolean trackWidth)
   {
      this.trackWidth = trackWidth;
   }

   public boolean getScrollableTracksViewportHeight()
   {
      return trackHeight;
   }

   public void setScrollableTracksViewportHeight(
      boolean trackHeight)
   {
      this.trackHeight = trackHeight;
   }
   
   public void setLayoutOrientation(
      int orientation)
   {
      super.setLayoutOrientation(orientation);
      if (orientation == VERTICAL)
      {
         setScrollableTracksViewportWidth(true);
         setScrollableTracksViewportHeight(false);
      }
      if (orientation == VERTICAL_WRAP)
      {
         setScrollableTracksViewportWidth(false);
         setScrollableTracksViewportHeight(true);
      }
      if (orientation == HORIZONTAL_WRAP)
      {
         setScrollableTracksViewportWidth(true);
         setScrollableTracksViewportHeight(false);
      }
      revalidate();
   }
}
