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
package com.jaspersoft.ireport.designer.fonts;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author gtoffoli
 */
public class CheckBoxList extends JList
{
    
   public CheckBoxList()
   {
      super();
      
      setModel( new DefaultListModel());
      setCellRenderer(new CheckboxCellRenderer());
      
      addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
               int index = locationToIndex(e.getPoint());

               if (index != -1) {
                  Object obj = getModel().getElementAt(index);
                  if (obj instanceof JCheckBox)
                  {
                    JCheckBox checkbox = (JCheckBox)obj;
                              
                    checkbox.setSelected(
                                     !checkbox.isSelected());
                    repaint();
                  }
               }
            }
         }
      
      );

      setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
   }
   
   
    @SuppressWarnings("unchecked")
   public int[] getCheckedIdexes()
   {
       java.util.List list = new java.util.ArrayList();
       DefaultListModel dlm = (DefaultListModel)getModel();
       for (int i=0; i<dlm.size(); ++i)
       {
            Object obj = getModel().getElementAt(i);
            if (obj instanceof JCheckBox)
            {
                JCheckBox checkbox = (JCheckBox)obj;
                if (checkbox.isSelected())
                {
                    list.add(new Integer(i));
                }
            }
       }
       
       int[] indexes = new int[list.size()];
       
       for (int i=0; i<list.size(); ++i)
       {
            indexes[i] = ((Integer)list.get(i)).intValue();
       }
       
       return indexes;
   }
   
   
    @SuppressWarnings("unchecked")
   public java.util.List getCheckedItems()
   {
       java.util.List list = new java.util.ArrayList();
       DefaultListModel dlm = (DefaultListModel)getModel();
       for (int i=0; i<dlm.size(); ++i)
       {
            Object obj = getModel().getElementAt(i);
            if (obj instanceof JCheckBox)
            {
                JCheckBox checkbox = (JCheckBox)obj;
                if (checkbox.isSelected())
                {
                    list.add(checkbox);
                }
            }
       }
       return list;
   }
}

