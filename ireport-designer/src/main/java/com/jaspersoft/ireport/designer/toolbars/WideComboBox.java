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

package com.jaspersoft.ireport.designer.toolbars;

import java.awt.Dimension;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 *
 * @version $Id: WideComboBox.java 0 2009-10-23 12:48:57 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class WideComboBox extends JComboBox {

    public WideComboBox()
    {
        super();
        setup();
    }

    public WideComboBox(ComboBoxModel aModel)
    {
        super(aModel);
        setup();
    }


    private void setup()
    {
        
        this.addPopupMenuListener(new PopupMenuListener() {

            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                JComboBox box = WideComboBox.this;
                Object comp = box.getUI().getAccessibleChild(box, 0);
                if (!(comp instanceof JPopupMenu)) return; // Not on a standard look and feel.

                JPopupMenu menu = (JPopupMenu)comp;

                for (int i=0; i<menu.getComponentCount(); ++i)
                {
                    if (menu.getComponent(i) instanceof JScrollPane)
                    {
                        JScrollPane scroller = (JScrollPane)menu.getComponent(i);
                        Dimension scrollSize = new Dimension(250, scroller.getPreferredSize().height);
                        scroller.setMaximumSize(scrollSize);
                        scroller.setPreferredSize(scrollSize);
                        scroller.setMinimumSize(scrollSize);

                        break;
                    }
                }
                //menu.setBounds(menu.getBounds().x, menu.getBounds().y, 300, menu.getPreferredSize().height);

                menu.updateUI();

                /*
                menu.setPreferredSize(new Dimension(300, ));
                menu.doLayout();
                */
                /*
               
                Dimension size = new Dimension();
                size.width = 300; //box.getPreferredSize().width;
                size.height = scrollPane.getPreferredSize().height;
                System.out.println("Menu size: " + size);
                System.out.flush();
                scrollPane.setPreferredSize(size);
                scrollPane.setSize(size);
                scrollPane.updateUI();
                */
             }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                
                
            }

            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        
    }


}


