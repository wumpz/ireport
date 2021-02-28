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
package com.jaspersoft.ireport.designer.outline;

import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Node.Cookie;

/**
 *
 * @author gtoffoli
 */
public class GenericCookie<T> implements Cookie {

    T obj = null;
    public GenericCookie(T obj)
    {
        this.obj = obj;
    }
    
    public T getObject()
    {
        return obj;
    }
    
    public static class JasperDesignCookie extends GenericCookie<JasperDesign> {
        
        public JasperDesignCookie(JasperDesign obj)
        {
            super(obj);
        }
    }
    
    public static class JRDesignDatasetCookie extends GenericCookie<JRDesignDataset> {
        
        public JRDesignDatasetCookie(JRDesignDataset obj)
        {
            super(obj);
        }
    }
    
    
}
