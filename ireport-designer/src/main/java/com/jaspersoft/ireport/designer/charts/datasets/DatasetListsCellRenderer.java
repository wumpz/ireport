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
package com.jaspersoft.ireport.designer.charts.datasets;

import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import net.sf.jasperreports.charts.design.JRDesignCategorySeries;
import net.sf.jasperreports.charts.design.JRDesignGanttSeries;
import net.sf.jasperreports.charts.design.JRDesignPieSeries;
import net.sf.jasperreports.charts.design.JRDesignTimePeriodSeries;
import net.sf.jasperreports.charts.design.JRDesignTimeSeries;
import net.sf.jasperreports.charts.design.JRDesignXySeries;
import net.sf.jasperreports.charts.design.JRDesignXyzSeries;

/**
 *
 * @author gtoffoli
 */
public class DatasetListsCellRenderer extends DefaultListCellRenderer {

    public Component getListCellRendererComponent(
         JList list,
         Object value,
         int index,
         boolean isSelected,
         boolean cellHasFocus)
     {
         JLabel label = (JLabel)super.getListCellRendererComponent(list,value,index,isSelected, cellHasFocus);
         label.setIcon(null);
         
         if (value instanceof JRDesignTimePeriodSeries)
         {
                  label.setText( "Time period series [" + Misc.getExpressionText( ((JRDesignTimePeriodSeries)value).getSeriesExpression() ) +"]");
         }
         else if (value instanceof JRDesignCategorySeries)
         {
                  label.setText( "Category series [" + Misc.getExpressionText( ((JRDesignCategorySeries)value).getSeriesExpression() ) +"]");
         }
         else if (value instanceof JRDesignXySeries)
         {
                  label.setText( "XY series [" + Misc.getExpressionText( ((JRDesignXySeries)value).getSeriesExpression() ) +"]");
         }
         else if (value instanceof JRDesignTimeSeries)
         {
                  label.setText( "Time series [" + Misc.getExpressionText( ((JRDesignTimeSeries)value).getSeriesExpression() ) +"]");
         }
         else if (value instanceof JRDesignXyzSeries)
         {
                  label.setText( "XYZ series [" + Misc.getExpressionText( ((JRDesignXyzSeries)value).getSeriesExpression() ) +"]");
         }
         else if (value instanceof JRDesignGanttSeries)
         {
                  label.setText( "Gantt series [" + Misc.getExpressionText( ((JRDesignGanttSeries)value).getSeriesExpression() ) +"]");
         }
         else if (value instanceof JRDesignPieSeries)
         {
                  if (list.getModel().getSize() <= 1)
                  {
                      label.setText( "Default pie series");
                  }
                  else
                  {
                      label.setText( "Pie series [" + (index+1) + "]");
                  }
         }
         
         return this;
     }
    
    
}
