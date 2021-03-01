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
package com.jaspersoft.ireport.jasperserver.ui.inputcontrols.impl;

import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.InputControlQueryDataRow;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

/**
 *
 * @author gtoffoli
 */
public // The combobox's renderer...
	class ItemRenderer extends JPanel implements ListCellRenderer
	{
		private JLabel[]	labels = null;
		//private JLabel		nameLabel = new JLabel(" ");
		//private JLabel		valueLabel = new JLabel(" ");
		int columns = 0;
 
		public ItemRenderer(int columns)
		{
			//setLayout(new GridBagLayout());
                        GridLayout g = new GridLayout(1,columns);
                        setLayout(g);

                        this.columns = columns;
 			labels = new JLabel[columns];
                        
                        //java.awt.GridBagConstraints gridBagConstraints = null;
                                
 			for (int i=0; i<columns; ++i)
 			{
 			   labels[i] = new JLabel(" ");
                           //gridBagConstraints = new java.awt.GridBagConstraints();
                           //gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                           //gridBagConstraints.weightx = 1.0;
                           //gridBagConstraints.weighty = 1.0;
                           
 			   add(labels[i]); //, gridBagConstraints);
 			}
 		}
 
 
		public Component getListCellRendererComponent(
							JList list,
							Object value,
							int index,
							boolean isSelected,
							boolean cellHasFocus )
		{
                    
                        if (value != null && value instanceof InputControlQueryDataRow)
                        {
                            InputControlQueryDataRow icqdr = (InputControlQueryDataRow)value;
                            if (value != null)
                            {
                                for (int i=0; i<this.columns; ++i)
                                {
                                    String s = " ";
                                    try {
                                       if (icqdr.getColumnValues().get(i) != null)
                                       {
                                           s = ""+icqdr.getColumnValues().get(i);
                                           
                                       }
                                    } catch (Exception ex) { }
                                    
                                    getLabels()[i].setText( s );
                                
                                }
                                this.updateUI();
                            }
                            
                        }
                        else
                        {

                            getLabels()[0].setText(Misc.nvl(value, " "));
                            
                            for (int i=1; i<this.columns; ++i)
                            {
                                getLabels()[i].setText(" ");
                            }
                        }
                     

                        setOpaque(isSelected);
                        
                        if (!isSelected)
                        {
                            Color bg = UIManager.getColor("List.background");
                            if (bg != null) this.setBackground(bg);
                            Color fg = UIManager.getColor("List.foreground");
                            for (int i=0; i<this.columns; ++i)
                            {
                                getLabels()[i].setForeground(fg);
                                getLabels()[i].setBackground(bg);
                            }
                        }
                        else
                        {
                            Color bg = UIManager.getColor("List.selectionBackground");
                            if (bg != null) this.setBackground(bg);
                            Color fg = UIManager.getColor("List.selectionForeground");
                            for (int i=0; i<this.columns; ++i)
                            {
                                getLabels()[i].setForeground(fg);
                                getLabels()[i].setBackground(bg);
                            }
                        }

                        for (int i=0; i<this.columns; ++i)
                        {
                            getLabels()[i].setOpaque(isSelected);
                        }

			return this;
		}

    public JLabel[] getLabels() {
        return labels;
    }

    public void setLabels(JLabel[] labels) {
        this.labels = labels;
    }
	}
