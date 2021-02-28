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
package com.jaspersoft.ireport.designer.palette;

import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.beans.BeanInfo;
import java.io.IOException;
import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.util.datatransfer.ExTransferable;

public class PaletteItemDataNode extends DataNode {

    
    private PaletteItem paletteItem;
    
    public PaletteItemDataNode(PaletteItemDataObject obj, PaletteItem paletteItem) {
        super(obj, Children.LEAF);
        this.paletteItem = paletteItem;
        setName( paletteItem.getId() );
        setShortDescription( paletteItem.getComment());
    }
    
    public Image getIcon(int i) {
        if( i == BeanInfo.ICON_COLOR_16x16 ||
            i == BeanInfo.ICON_MONO_16x16 ) {
                return paletteItem.getSmallImage();
        }
        return paletteItem.getBigImage();
    }
    
    public String getDisplayName() {
        return paletteItem.getDisplayName();
    }

    @Override
    public Transferable drag() throws IOException {
        
        ExTransferable tras = ExTransferable.create(super.drag());
        tras.put(new ExTransferable.Single( PaletteUtils.PALETTE_ITEM_DATA_FLAVOR) {

            protected Object getData() throws IOException, UnsupportedFlavorException {
                return paletteItem;
            }
                
        }  );
        
        return tras;
    }
    
    
}