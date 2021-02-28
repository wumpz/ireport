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

package com.jaspersoft.ireport.designer;

import java.net.URL;
import java.net.URLClassLoader;

/**
 *
 * @version $Id: IRURLClassLoader.java 0 2010-02-01 18:15:30 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class IRURLClassLoader extends URLClassLoader {

    public IRURLClassLoader(URL[] urls, ClassLoader parent)
    {
        super(urls, parent);
    }

    @Override
    public URL findResource(String name) {

         URL url = super.findResource(name);
         while (url == null && name.startsWith("/"))
         {
             return findResource(name.substring(1));
         }
         return url;
    }


}
