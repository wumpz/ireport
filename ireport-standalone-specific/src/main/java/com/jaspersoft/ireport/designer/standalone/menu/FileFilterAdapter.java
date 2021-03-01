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
package com.jaspersoft.ireport.designer.standalone.menu;

import java.io.File;
import javax.swing.filechooser.FileFilter;


/**
 *
 * @author gtoffoli
 */
public class FileFilterAdapter extends FileFilter {

    private String ext = "";
    private String desc = "";

    public FileFilterAdapter(String ext, String desc)
    {
        super();
        this.ext = ext;
        this.desc = desc;
    }

    @Override
    public boolean accept(File f) {

        if (f.isDirectory()) return true;
        if (f.exists() && f.getName().toLowerCase().endsWith(ext)) // NOI18N
        {
            return true;
        }
        return false;
    }

    public String getDescription() {
        return desc;
    }
}
