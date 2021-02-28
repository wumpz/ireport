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

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.ColumnControlButton;
import org.jdesktop.swingx.table.ColumnControlPopup;

/**
 *
 * @author gtoffoli
 */
public class CustomColumnControlButton extends ColumnControlButton {

    private JXTable table = null;
    
    
    
    @SuppressWarnings("unchecked")
    public CustomColumnControlButton(JXTable table, Icon icon) {
        
        super(table, icon);
        this.table = table;
        
        ColumnControlPopup cc = null;
        cc = getColumnControlPopup();
        Action resetOrderAction = new AbstractAction("Reset order"){

            public void actionPerformed(ActionEvent e) {
                resetOrder();
            }
        };

        List actions = new java.util.ArrayList();
        actions.add(resetOrderAction);

        cc.addAdditionalActionItems(actions);
        
    }
    
    public void resetOrder()
    {
        table.resetSortOrder();
    }
}
