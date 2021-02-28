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
package com.jaspersoft.ireport.designer.compatibility;

import com.jaspersoft.ireport.designer.IRLocalJasperReportsContext;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.util.Map;
import net.sf.jasperreports.engine.xml.JRXmlBaseWriter;

/**
 *
 * @author gtoffoli
 */
public class VersionJRContext extends IRLocalJasperReportsContext {
    
    private String version = null;
    
    
    
    
    /**
     * Create a new context based on the defult IRLocalJAsperReportsContext.
     * 
     * If version is not null. and it contains underscores, underscores are
     * replaced by points.
     * If the version is not one of the available ones, it is reset to null.
     * 
     * @param version 
     */
    public VersionJRContext(String version)
    {
        super();
        
        if (version == null) version = "";
        
        if (version.length() > 0)
        {
            if (!JRXmlWriterHelper.getJRVersions().contains(version) )
            {
                version = version.replace("_", ".");
            }
            
            if (!JRXmlWriterHelper.getJRVersions().contains(version) )
            {
                Misc.log("XML writer for version " +version + " not found. Reset compatibility to default (null).");
                
                version = "";
            }
        }
        
        this.version = version;
    }

    @Override
    public Map<String, String> getProperties() {
        Map map = super.getProperties();
    
        
        map.remove(JRXmlBaseWriter.PROPERTY_REPORT_VERSION);
        if (version != null && version.length() > 0)
        {
            map.put(JRXmlBaseWriter.PROPERTY_REPORT_VERSION, version);
        }
        return map;
    }
    
    

    @Override
    public String getProperty(String key) {
        
        
        System.out.println("Getting the property: " + key);
        if (key.equals(JRXmlBaseWriter.PROPERTY_REPORT_VERSION))
        {
            
            return version.equals("") ? null : version;
        }
        
        return super.getProperty(key);
    }
    
}
