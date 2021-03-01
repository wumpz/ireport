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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.JOptionPane;
import org.openide.util.Mutex;


/**
 *
 * @author gtoffoli
 */
public class RepositoryFolder {
    
    private ResourceDescriptor descriptor;
    private JServer server = null;
    private boolean root = false;
    
    private List children = null;
    
    private boolean loaded = false;

    /** Creates a new instance of RepositoryFolder */
    public RepositoryFolder(JServer server, ResourceDescriptor descriptor, boolean root) {
        this.descriptor = descriptor;
        this.server = server;
        this.root = root;
    }
    
    /** Creates a new instance of RepositoryFolder */
    public RepositoryFolder(JServer server, ResourceDescriptor descriptor) {
        this(server, descriptor, false);
    }

    public String toString()
    {
        if (getDescriptor() != null)
        {
            if (!IReportManager.getPreferences().getBoolean("jasperserver.showResourceIDs", false))
            {
                return ""+getDescriptor().getLabel();
            }
            else
            {
                return ""+getDescriptor().getName();
            }
        }
        
        return "???";
    }

    public JServer getServer() {
        return server;
    }

    public void setServer(JServer server) {
        this.server = server;
    }

    public ResourceDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(ResourceDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
    
    public List getChildren()
    {
        return getChildren(false);
    }
    
    public List getChildren(boolean refresh)
    {
        if (children == null || refresh || !isLoaded()) children = null;
        
        if (children == null)
        {
            try {
                List descriptors = getServer().getWSClient().list(getDescriptor());
                children = parseDescriptors(descriptors);
                loaded = true;
            } catch (Exception ex) {
                
                final String msg = ex.getMessage();
                Mutex.EVENT.readAccess(new Runnable() {

                    public void run() {
                        JOptionPane.showMessageDialog(Misc.getMainFrame(),JasperServerManager.getFormattedString("messages.error.3", "Error:\n {0}", new Object[] {msg}));
                
                    }
                }); 
                
                ex.printStackTrace();
            }
        } 
        
        return children;
    }
    
    /** Creates a set of RepositoryXXX classes from a set of Descriptors...
     * 
     * @param descriptors
     * @return
     */
    public List parseDescriptors(List descriptors)
    {
       List list = new ArrayList();
       if (descriptors != null)
       {
           for (int i=0; i<descriptors.size(); ++i)
           {
                ResourceDescriptor rd = (ResourceDescriptor)descriptors.get(i);
                
                list.add( createRepositoryObject(getServer(), rd ) );
           }
       }
       return list;
    }
    
    public static RepositoryFolder createRepositoryObject(JServer srv, ResourceDescriptor rd)
    {
        RepositoryFolder rf = null;
        if (rd.getWsType() == null)
        {
            rd.setWsType(ResourceDescriptor.TYPE_UNKNOW);
        }

        if (rd.getWsType().equals( ResourceDescriptor.TYPE_REPORTUNIT)  )
        {
            rf = new RepositoryReportUnit(srv, rd );
        }
        else if (rd.getWsType().equals( ResourceDescriptor.TYPE_FOLDER)  )
        {
            rf = new RepositoryFolder(srv, rd );
        }
        else if (rd.getWsType().equals( ResourceDescriptor.TYPE_JRXML)  )
        {
            rf = new RepositoryJrxmlFile(srv, rd );
        }
        else if (rd.getWsType().equals( ResourceDescriptor.TYPE_STYLE_TEMPLATE)  )
        {
            rf = new RepositoryJrtxFile(srv, rd );
        }
        else if (rd.getWsType().equals( ResourceDescriptor.TYPE_RESOURCE_BUNDLE)  )
        {
            rf = new RepositoryBundleFile(srv, rd );
        }
        
        
        ResourceHandler rh = JasperServerManager.getResourceHandler(rd);
        
        if (rh != null)
        {
            rf = rh.createRepositoryObject(srv, rd);
        }
        
        if (rf == null)
        {
            rf = new RepositoryFile(srv, rd );
        }
        
        return rf;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }
    
    public boolean isDataSource()
    {
       return isDataSource(getDescriptor());
    }
    
    public static boolean isDataSource(ResourceDescriptor rd)
    {
      if (rd == null) return false;
      if (rd.getWsType() == null) return false;
      if (rd.getWsType().equals( rd.TYPE_DATASOURCE) ||
          rd.getWsType().equals( rd.TYPE_DATASOURCE_JDBC) ||
          rd.getWsType().equals( rd.TYPE_DATASOURCE_JNDI) ||
          rd.getWsType().equals( rd.TYPE_DATASOURCE_BEAN) ||
          rd.getWsType().equals( rd.TYPE_DATASOURCE_VIRTUAL) ||
          rd.getWsType().equals( "Domain") ||
          rd.getWsType().equals(rd.TYPE_DATASOURCE_CUSTOM))
      {
          return true;
      }
      
      return false;
    }
    
    
}
