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

import com.jaspersoft.ireport.jasperserver.RepositoryFile;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.jasperserver.RepositoryReportUnit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Mutex;

/**
 *
 * @author gtoffoli
 */
public class ReportUnitChildren extends Index.KeysChildren implements PropertyChangeListener {

    private RepositoryReportUnit reportUnit = null;
    private Lookup doLkp = null;
    private ReportUnitInputControlsNode controlsNode = null;
    private ReportUnitResourcesNode resourcesNode = null;
    private boolean calculating = false;
            
       
    public ReportUnitChildren(RepositoryReportUnit reportUnit, Lookup doLkp) {
        super(new ArrayList());
        this.reportUnit = reportUnit;
        this.doLkp = doLkp;
        controlsNode = new ReportUnitInputControlsNode(getReportUnit(), doLkp);
        resourcesNode = new ReportUnitResourcesNode(getReportUnit(), doLkp);
    }

    /*
    @Override
    protected List<Node> initCollection() {
        return recalculateKeys();
    }
    */
    
    
    protected Node[] createNodes(Object key) {
        
        if (key instanceof RepositoryFile)
        {
            return new Node[]{new FileNode((RepositoryFile)key, doLkp)};
        }
        else if (key instanceof Node)
        {
            return new Node[]{(Node)key};
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
    
    public void recalculateKeys(final boolean reload) {
        if (isCalculating()) return;
        setCalculating(true);
        final List l = (List)lock();
        l.clear();
        
        // We dived here connection, main jrxml, input controls and resources 
        
        Runnable run = new Runnable() {

            public void run() {
                
                SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                       ((ReportUnitNode)getNode()).setLoading(true);
                    }
                });
                
                List children = getReportUnit().getChildren(reload);
                
                boolean mainReportFound = false;
                
                for (int i=0; i<children.size(); ++i)
                {
                    
                    RepositoryFolder item = (RepositoryFolder)children.get(i);
                    
                    if (item.isDataSource()) continue;
                    if (item.getDescriptor().isMainReport())
                    {
                        if (!mainReportFound) // This check is a fix for a WS bug that sends this descriptor twice
                        {
                            l.add(item);
                        }
                        mainReportFound = true;
                    }
                }
                l.add(controlsNode);
                l.add(resourcesNode);
                    
                SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                       update();
                       ((InputControlsChildren)controlsNode.getChildren()).recalculateKeys();
                       ((ResourcesChildren)resourcesNode.getChildren()).recalculateKeys();
                       ((ReportUnitNode)getNode()).setLoading(false);
                       setCalculating(false);
                    }
                });
            }
        };
        
        Thread t = new Thread(run);
        t.start();
    }
    
    @SuppressWarnings("unchecked")
    public void reorder() { 
            Mutex.Action action = new Mutex.Action(){ 
                public Object run(){ 
                    Index.Support.showIndexedCustomizer(ReportUnitChildren.this.getIndex()); 
                    return null; 
                } 
            }; 
            MUTEX.writeAccess(action); 
        }

    public void propertyChange(PropertyChangeEvent evt) {
        
    }

    public RepositoryReportUnit getReportUnit() {
        return reportUnit;
    }

    public void setReportUnit(RepositoryReportUnit reportUnit) {
        this.reportUnit = reportUnit;
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
