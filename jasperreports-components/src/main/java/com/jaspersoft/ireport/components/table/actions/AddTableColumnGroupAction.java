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
package com.jaspersoft.ireport.components.table.actions;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import org.openide.util.NbBundle;

public class AddTableColumnGroupAction extends AddTableColumnAction {

    @Override
    public BaseColumn createNewColumn()
    {
        StandardColumnGroup newColumn = new StandardColumnGroup();
        newColumn.setWidth(180);
        
        DesignCell tableHeaderCell = new DesignCell();
        tableHeaderCell.setHeight(30);
        newColumn.setTableHeader(tableHeaderCell);

        DesignCell columnHeaderCell = new DesignCell();
        columnHeaderCell.setHeight(30);
        newColumn.setColumnHeader(columnHeaderCell);


        DesignCell tableFooterCell = new DesignCell();
        tableFooterCell.setHeight(30);
        newColumn.setColumnFooter(tableFooterCell);

        DesignCell columnFooterCell = new DesignCell();
        columnFooterCell.setHeight(30);
        newColumn.setTableFooter(columnFooterCell);

        newColumn.addColumn(super.createNewColumn());
        newColumn.addColumn(super.createNewColumn());

        return newColumn;
    }

    @Override
    public int getWhere() {
        return AFTER;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(AddTableColumnAction.class, "AddTableColumnGroupAction.Name.CTL_AddTableColumnGroupAction");
    }

    
}