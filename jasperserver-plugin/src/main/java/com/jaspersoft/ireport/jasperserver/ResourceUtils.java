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
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;

/**
 *
 * @author gtoffoli
 */
public class ResourceUtils {

    /**
     * Create a clone of the resource descriptor. The replication is recursive.
     * @param rd The ResourceDescriptor to clone
     * @return the new clone
     */
    public static ResourceDescriptor cloneResourceDescriptor(ResourceDescriptor rd)
    {
        ResourceDescriptor newRd = new ResourceDescriptor();
        newRd.setName( rd.getName());
        newRd.setWsType( rd.getWsType() );
        newRd.setLabel( rd.getLabel());
        newRd.setDescription( rd.getDescription());
        newRd.setUriString( rd.getUriString());
        newRd.setIsNew( rd.getIsNew());
        
        for (int i=0; i< rd.getChildren().size(); ++i)
        {
            ResourceDescriptor tmpRd = (ResourceDescriptor)rd.getChildren().get(i);
            newRd.getChildren().add( cloneResourceDescriptor( tmpRd ) );
        }
        
        for (int i=0; i< rd.getProperties().size(); ++i)
        {
            ResourceProperty tmpRp = (ResourceProperty)rd.getProperties().get(i);
            newRd.getProperties().add( cloneResourceProperty( tmpRp ) );
        }
        
        return newRd;
    }
    
    /**
     * Create a clone of the resource property. The replication is recursive.
     * @param rp The ResourceProperty to clone
     * @return the new clone
     */
    public static ResourceProperty cloneResourceProperty(ResourceProperty rp)
    {
        ResourceProperty newRp = new ResourceProperty(rp.getName(), rp.getValue());
        
        for (int i=0; i< rp.getProperties().size(); ++i)
        {
            ResourceProperty tmpRp = (ResourceProperty)rp.getProperties().get(i);
            newRp.getProperties().add( cloneResourceProperty( tmpRp ) );
        }
        
        return newRp;
    }

}
