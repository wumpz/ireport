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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

public class PaletteItemDataObject extends MultiDataObject {

    private PaletteItem paletteItem = null;
    
    public PaletteItemDataObject(FileObject pf, PaletteItemDataLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        
        InputStream input = pf.getInputStream();
        Properties props = new Properties();
        props.load(input);
        input.close();
        paletteItem = new PaletteItem(props);
        
    }

    protected Node createNodeDelegate() {
        return new PaletteItemDataNode(this, paletteItem);
    }

    @Override
    public Lookup getLookup() {
        return getCookieSet().getLookup();
    }
}