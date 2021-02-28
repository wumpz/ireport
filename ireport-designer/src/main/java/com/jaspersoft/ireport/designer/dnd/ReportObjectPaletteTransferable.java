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
package com.jaspersoft.ireport.designer.dnd;

import com.jaspersoft.ireport.designer.palette.PaletteItem;
import com.jaspersoft.ireport.designer.palette.PaletteItemAction;
import com.jaspersoft.ireport.designer.palette.PaletteUtils;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Properties;
import org.openide.util.datatransfer.ExTransferable;

/**
 *
 * @author gtoffoli
 */
public class ReportObjectPaletteTransferable extends ExTransferable.Single {

    private PaletteItem paletteItem = null;
    
    /**
     * Action is a PaletteItemAction class name
     * paletteData is accessor data that can be passed to this palette item...
     */
    public ReportObjectPaletteTransferable(String action, Object paletteData)
    {
        super( PaletteUtils.PALETTE_ITEM_DATA_FLAVOR);
        
        Properties properties = new Properties();
        properties.setProperty( PaletteItem.ACTION, action);
        paletteItem = new PaletteItem(properties);
        paletteItem.setData(paletteData);
        
    }
    
    protected Object getData() throws IOException, UnsupportedFlavorException {
        return paletteItem;
    }

    
}
