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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.SimpleFileResolver;

/**
 *
 * @version $Id: JSFileResolver.java 0 2010-05-27 21:50:17 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class JSFileResolver extends SimpleFileResolver {

    JServer server = null;
    JasperDesign jasperDesign = null;
    String reportUnitUri = null;

    List<ResourceDescriptor> reportUnitResources = null;

    //.getProperty("ireport.jasperserver.reportUnit")

    public JSFileResolver(File parentFolder, JServer server, JasperDesign jasperDesign)
    {
        this(Arrays.asList(parentFolder), server, jasperDesign);
    }

    public JSFileResolver(List<File> parentFolders, JServer server, JasperDesign jasperDesign)
    {
        super(parentFolders);
        this.server = server;
        this.jasperDesign = jasperDesign;
        setResolveAbsolutePath(true);

        if (jasperDesign.getProperty("ireport.jasperserver.reportUnit") != null)
        {
            reportUnitUri = jasperDesign.getProperty("ireport.jasperserver.reportUnit");
        }
    }

    @Override
    public File resolveFile(String fileName) {

        if (fileName.startsWith("repo:"))
        {
            // resolve locally....
            String objectUri = fileName.substring(5);
            try {
            
                if (objectUri.contains("/"))
                {
                    // Locate the resource inside the repository...
                }
                else if (reportUnitUri != null)
                {
                    // Locate the resource inside the report unit, if any...
                    if (reportUnitResources == null)
                    {
                        ResourceDescriptor rd = new ResourceDescriptor();
                        rd.setWsType(ResourceDescriptor.TYPE_REPORTUNIT);
                        rd.setUriString(reportUnitUri);
                        rd = server.getWSClient().get(rd, null);
                        reportUnitResources = server.getWSClient().list(rd);
                        if (reportUnitResources == null)
                        {
                            reportUnitResources = new ArrayList<ResourceDescriptor>();
                        }
                    }

                    // find the resource...
                    for (ResourceDescriptor resource : reportUnitResources)
                    {

                        if (resource.getName().equals(objectUri))
                        {
                            if (resource.getWsType().equals(ResourceDescriptor.TYPE_IMAGE) ||
                                //resource.getWsType().equals(ResourceDescriptor.TYPE_JRXML) ||
                                resource.getWsType().equals(ResourceDescriptor.TYPE_RESOURCE_BUNDLE) ||
                                resource.getWsType().equals(ResourceDescriptor.TYPE_STYLE_TEMPLATE))
                            {
                                // Export the file in a temporary file...
                                String resolvedFileName = JasperServerManager.createTmpFileName(null, resource.getName());
                                // Export the file here..
                                File resolvedFile = new File(resolvedFileName);
                                server.getWSClient().get(resource, resolvedFile);
                                return resolvedFile;
                            }
                        }
                    }
                    System.out.println("Resource " + objectUri + " not found in the JasperServer Report at " + reportUnitUri);
                }
            } catch (Exception ex)
            {
                System.out.println("Unable to resolve " + objectUri + " on " + server.getName() + "server ( " +ex.getMessage()+")");
            }
        }
        return super.resolveFile(fileName);
    }

}
