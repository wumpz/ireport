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
package com.jaspersoft.ireport.jasperserver;

import java.io.File;
import java.util.HashMap;
import javax.swing.ImageIcon;
import net.sf.jasperreports.engine.util.FileResolver;

/**
 *
 * @author gtoffoli
 */
public class RepoImageCache extends HashMap<String, File> implements FileResolver {

    
    private static RepoImageCache instance = null;
    
    public static RepoImageCache getInstance()
    {
        if (instance == null)
        {
            instance = new RepoImageCache();
        }
        return instance;
    }

    public File resolveFile(String arg0) {
        
        if (containsKey(arg0))
        {
            return get(arg0);
        }
        
        return null;
    }
    
    
}
