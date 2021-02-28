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
import javax.swing.event.*;

public class JListView extends JPanel
   implements ChangeListener
{
   protected JScrollPane scroll;
   private ScrollableList list;
   
   int listLayout = JList.HORIZONTAL_WRAP;
   
   public JListView()
   {
      setLayout(new BorderLayout());
      
      add(BorderLayout.CENTER, scroll = 
         new JScrollPane(
         list = new ScrollableList() ));
      scroll.getViewport().setBackground(
         getList().getBackground());
      stateChanged(new ChangeEvent(this));
   }
   
   public void stateChanged(ChangeEvent event)
   {
      getList().setLayoutOrientation(listLayout);
      if (listLayout == JList.VERTICAL)
      {
         scroll.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
         scroll.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      }
      if (listLayout == JList.VERTICAL_WRAP)
      {
         scroll.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_NEVER);
         scroll.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      }
      if (listLayout == JList.HORIZONTAL_WRAP)
      {
         scroll.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
         scroll.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      }
      scroll.revalidate();
   }

    public ScrollableList getList() {
        return list;
    }

    public void setList(ScrollableList list) {
        this.list = list;
    }
   

}
