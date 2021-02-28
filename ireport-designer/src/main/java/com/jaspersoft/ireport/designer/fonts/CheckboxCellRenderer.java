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

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.border.*;

/**
 *
 * @author gtoffoli
 */
public class CheckboxCellRenderer extends DefaultListCellRenderer
{
      protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
            
      public Component getListCellRendererComponent(
                    JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus)
      {
         if (value instanceof CheckBoxListEntry)
         {
             CheckBoxListEntry checkbox = (CheckBoxListEntry) value;
             checkbox.setBackground(isSelected ?
                     list.getSelectionBackground() : list.getBackground());
             if (checkbox.isRed())
             {
                 checkbox.setForeground( Color.red );
             }
             else
             {
             checkbox.setForeground(isSelected ?
                     list.getSelectionForeground() : list.getForeground());
             }
             checkbox.setEnabled(isEnabled());
             checkbox.setFont(getFont());
             checkbox.setFocusPainted(false);
             checkbox.setBorderPainted(true);
             checkbox.setBorder(isSelected ?
              UIManager.getBorder(
               "List.focusCellHighlightBorder") : noFocusBorder);
             
             return checkbox;
         }
         else
         {
            return super.getListCellRendererComponent(list, value.getClass().getName(), index, isSelected, cellHasFocus);
         }
   }
}
