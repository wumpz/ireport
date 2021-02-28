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

package com.jaspersoft.ireport.designer.crosstab;

import com.jaspersoft.ireport.designer.ModelUtils;
import javax.swing.JButton;
import net.sf.jasperreports.crosstabs.design.JRCrosstabOrigin;

/**
 *
 * @version $Id: JCellButton.java 0 2009-10-19 19:15:18 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class JCellButton extends JButton {

    private CellInfo cellInfo = null;

    public JCellButton(CellInfo cellInfo)
    {
        super();
        setToolTipText(ModelUtils.nameOf(cellInfo.getCellContents()));
        this.cellInfo = cellInfo;
        refreshText();
    }

    public void refreshText()
    {
        JRCrosstabOrigin origin = cellInfo.getCellContents().getOrigin();
        switch (origin.getType())
        {
            case JRCrosstabOrigin.TYPE_HEADER_CELL:
                setText("H");
                break;
            case JRCrosstabOrigin.TYPE_DATA_CELL:
                setText("D/D");
                break;
            case JRCrosstabOrigin.TYPE_COLUMN_GROUP_HEADER:
                setText("CH");
                break;
            case JRCrosstabOrigin.TYPE_COLUMN_GROUP_TOTAL_HEADER:
                setText("TCH");
                break;
            case JRCrosstabOrigin.TYPE_ROW_GROUP_HEADER:
                setText("RH");
                break;
            case JRCrosstabOrigin.TYPE_ROW_GROUP_TOTAL_HEADER:
                setText("TRH");
                break;
            case JRCrosstabOrigin.TYPE_WHEN_NO_DATA_CELL:
                setText("NDC");
                break;
        }

        setText( "<html>" + getText() + " [" + getCellInfo().getX() + "," + getCellInfo().getY()+"]<br>(" +getCellInfo().getLeft() + "," + getCellInfo().getTop() + "," + getCellInfo().getCellContents().getWidth() + "," + getCellInfo().getCellContents().getHeight() + ")");
    }

  
    /**
     * @return the cellInfo
     */
    public CellInfo getCellInfo() {
        return cellInfo;
    }

    /**
     * @param cellInfo the cellInfo to set
     */
    public void setCellInfo(CellInfo cellInfo) {
        this.cellInfo = cellInfo;
    }


}
