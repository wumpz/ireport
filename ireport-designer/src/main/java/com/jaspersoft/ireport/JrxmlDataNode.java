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
package com.jaspersoft.ireport;

import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

public class JrxmlDataNode extends DataNode {

    private static final String IMAGE_ICON_BASE = "com/jaspersoft/ireport/designer/resources/report-16.png";

    private final ChangeCallback callback = null;
    
    public static interface ChangeCallback {
        public void modelChanged(JasperDesign model)  throws IllegalArgumentException;
    }
    
    public JrxmlDataNode(JrxmlDataObject obj) {
        super(obj, Children.LEAF);
        setIconBaseWithExtension(IMAGE_ICON_BASE);
        setShortDescription(  FileUtil.toFile( obj.getPrimaryFile() ) +"" );
    }

    JrxmlDataNode(JrxmlDataObject obj, Lookup lookup) {
        super(obj, Children.LEAF, lookup);
        setIconBaseWithExtension(IMAGE_ICON_BASE);
        setShortDescription(  FileUtil.toFile( obj.getPrimaryFile() ) +"" );
    }

       
}