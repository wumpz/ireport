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
package com.jaspersoft.ireport.designer.jrtx;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader;
import org.openide.util.NbBundle;

public class JRTXDataLoader extends UniFileLoader {

    public static final String REQUIRED_MIME = "text/x-jrtx+xml";
    private static final long serialVersionUID = 1L;

    public JRTXDataLoader() {
        super("com.jaspersoft.ireport.designer.jrtx.JRTXDataObject");
    }

    @Override
    protected String defaultDisplayName() {
        return NbBundle.getMessage(JRTXDataLoader.class, "LBL_JRTX_loader_name");
    }

    @Override
    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME);
    }

    protected MultiDataObject createMultiObject(FileObject primaryFile) throws DataObjectExistsException, IOException {
        return new JRTXDataObject(primaryFile, this);
    }

    @Override
    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }
}
