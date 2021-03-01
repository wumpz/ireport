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
package com.jaspersoft.ireport.jasperserver.ui.nodes;

import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryFile;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.jasperserver.RepositoryReportUnit;
import com.jaspersoft.ireport.jasperserver.ResourceHandler;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Mutex;

/**
 *
 * @author gtoffoli
 */
public class FolderChildren extends Index.KeysChildren implements PropertyChangeListener {

    private RepositoryFolder folder = null;
    private Lookup doLkp = null;
    private AbstractNode parentNode = null;
    private boolean calculating = false;

    public FolderChildren(RepositoryFolder folder, Lookup doLkp) {
        super(new ArrayList());
        this.folder = folder;
        this.doLkp = doLkp;
    }

    @Override
    protected void reorder(int[] permutations) {
        // reordering does nothing...
    }

    /*
    @Override
    protected List<Node> initCollection() {
        return recalculateKeys();
    }
    */
    


    protected Node[] createNodes(Object key) {
        
        if (key instanceof RepositoryFolder)
        {
            ResourceDescriptor rd = ((RepositoryFolder)key).getDescriptor();
            ResourceHandler rh = JasperServerManager.getResourceHandler(rd);

            if (rh != null)
            {
                return new Node[]{ rh.createRepositoryNode((RepositoryFolder)key, doLkp) };
            }
        }
        
        
        if (key instanceof RepositoryFile)
        {
            return new Node[]{new FileNode((RepositoryFile)key, doLkp)};
        }
        if (key instanceof RepositoryReportUnit)
        {
            return new Node[]{new ReportUnitNode((RepositoryReportUnit)key, doLkp)};
        }
        else if (key instanceof RepositoryFolder)
        {
            return new Node[]{new FolderNode((RepositoryFolder)key, doLkp)};
        }

        return new Node[]{};
    }
    
    
    
    @Override
    protected void addNotify() {
        super.addNotify();
        recalculateKeys();
    }
    
    
    @SuppressWarnings("unchecked")
    public void recalculateKeys() {
        recalculateKeys(false);
    }
    
    @SuppressWarnings("unchecked")
    public void recalculateKeys(final boolean refresh) {
        if (isCalculating()) return;
        setCalculating(true);

        final List l = (List)lock();
        l.clear();
        List params = null;
        
        Runnable run = new Runnable() {

            public void run() {
                
                SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                       ((FolderNode)getNode()).setLoading(true);
                    }
                });
                
                List children = folder.getChildren(true);
                if (children != null)
                {
                    l.addAll( children );
                    SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                           update();
                           ((FolderNode)getNode()).setLoading(false);
                           setCalculating(false);
                        }
                    });
                }
                else
                {
                    folder.setLoaded(false);
                    ((FolderNode)getNode()).setLoading(false);
                    setCalculating(false);
                }
            }
        };
        
        Thread t = new Thread(run);
        t.start();
    }
    
    @SuppressWarnings("unchecked")
    public void reorder() { 
            Mutex.Action action = new Mutex.Action(){ 
                public Object run(){ 
                    Index.Support.showIndexedCustomizer(FolderChildren.this.getIndex()); 
                    return null; 
                } 
            }; 
            MUTEX.writeAccess(action); 
        }

    public void propertyChange(PropertyChangeEvent evt) {
        
    }

    public RepositoryFolder getFolder() {
        return folder;
    }

    public void setFolder(RepositoryFolder folder) {
        this.folder = folder;
    }

    /**
     * @return the calculating
     */
    public boolean isCalculating() {
        return calculating;
    }

    /**
     * @param calculating the calculating to set
     */
    public void setCalculating(boolean calculating) {
        this.calculating = calculating;
    }
}
