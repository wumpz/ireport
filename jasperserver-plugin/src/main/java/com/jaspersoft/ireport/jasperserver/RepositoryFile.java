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

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.io.File;


/**
 *
 * @author gtoffoli
 */
public class RepositoryFile extends RepositoryFolder {

    private String localFileName = null;
    
    
    /** Creates a new instance of RepositoryFolder */
    public RepositoryFile(JServer server, ResourceDescriptor descriptor) {
        super( server, descriptor);
    }

    public String toString()
    {
        if (getDescriptor() != null)
        {
            return ""+getDescriptor().getLabel();
        }   
        return "???";
    }
    
    /**
     * This method return the file rapresented by this resource file.
     * The file is cached in a temporary directory for subsequent calls to this method.
     * Please note: the file is never removed... a delete of this file should be done
     * on plugin startup....
     * The method returns the cached file name.
     *
     */
    public String getFile() throws Exception
    {
        if (localFileName == null || localFileName.length() == 0 || !(new File(localFileName).exists()))
        {
            try {
                String localFile = JasperServerManager.getMainInstance().createTmpFileName("file",getExtension());
                File file = new File(localFile);
                getServer().getWSClient().get(getDescriptor(), file);
                this.localFileName = file.getCanonicalPath();
           } catch (Exception ex)
           {
                throw ex;
           }
        }
        return localFileName;
    }
    
    public String getExtension()
    {
        String name = getDescriptor().getName();
        String ext = null;
        if (name.lastIndexOf(".") >= 0)
        {
            ext = name.substring(name.lastIndexOf(".")+1);
        }
        if (ext != null && ext.length() > 0) return ext;
        
        
        // Often the wstype is the same as the typical file extension...
        // Let's try to use it, even better than nothing...
        if (getDescriptor().getWsType() != null &&
            getDescriptor().getWsType().length() <= 5) return getDescriptor().getWsType();
        
        return null;
    }
    /**
     * If localFileName exists, remove it and set localFileName to NULL.
     */
    public void resetFileCache()
    {
        if (localFileName != null)
        {
            File f = new File(localFileName);
            if (f.exists())
            {
                f.delete();
            }
        }
        
        localFileName = null;
    }
    
}
