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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.dnd.ReportObjectPaletteTransferable;
import com.jaspersoft.ireport.designer.outline.nodes.IRIndexedNode;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.jasperserver.RepositoryReportUnit;
import com.jaspersoft.ireport.jasperserver.ui.actions.AddResourceAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.DeleteAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.PropertiesAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.RefreshAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.RunReportUnitAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.RunReportUnitCookieImpl;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.Action;
import javax.swing.ImageIcon;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class ReportUnitNode extends IRIndexedNode implements ResourceNode {

    private RepositoryReportUnit reportUnit = null;
    private boolean loading = false;
    
    protected static final ImageIcon loadingIcon = new ImageIcon(FolderNode.class.getResource("/com/jaspersoft/ireport/jasperserver/res/waiting.png"));
    protected static final ImageIcon reportUnitIcon = new ImageIcon(ReportUnitNode.class.getResource("/com/jaspersoft/ireport/jasperserver/res/reportunit.png"));
    
    public boolean hasCustomizer() {
        return false;
    }
    
    public ReportUnitNode(RepositoryReportUnit reportUnit, Lookup doLkp) {
        this(new ReportUnitChildren(reportUnit, doLkp), reportUnit, doLkp);
    }
    
    public ReportUnitNode(ReportUnitChildren pc, RepositoryReportUnit reportUnit, Lookup doLkp) {
        super(pc, pc.getIndex(), new ProxyLookup(doLkp, Lookups.fixed(new RunReportUnitCookieImpl(), reportUnit, reportUnit.getServer())));
        this.reportUnit = reportUnit;
        getLookup().lookup(RunReportUnitCookieImpl.class).setNode(this);
        
        IReportManager.getPreferences().addPreferenceChangeListener(new PreferenceChangeListener() {

            public void preferenceChange(PreferenceChangeEvent pce) {
                fireDisplayNameChange(null, getDisplayName());
            }
        });
    }

    
        @Override
    public String getDisplayName() {
        
            
        String baseName = "";
        if (!IReportManager.getPreferences().getBoolean("jasperserver.showResourceIDs", false))
        {
            baseName =  ""+ getReportUnit().getDescriptor().getLabel();
        }
        else
        {
            baseName =  ""+getReportUnit().getDescriptor().getName();
        }
        
        return baseName + ( (isLoading()) ? " (Loading....)" : "");
    }
        


    @Override
    public Image getIcon(int arg0) {
    
        if (isLoading())
        {
            return loadingIcon.getImage();
        }
        return reportUnitIcon.getImage();
    }
    
    @Override
    public Image getOpenedIcon(int arg0) {
    
        if (isLoading())
        {
            return loadingIcon.getImage();
        }
        return reportUnitIcon.getImage();
    
    }
    
    

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        this.fireIconChange();
    }

    public RepositoryReportUnit getReportUnit() {
        return reportUnit;
    }

    public void setReportUnit(RepositoryReportUnit reportUnit) {
        this.reportUnit = reportUnit;
    }
    
    @Override
    public Action[] getActions(boolean arg0) {
        
        
        List<Action> actions = new ArrayList<Action>();

        
        actions.add(SystemAction.get( RunReportUnitAction.class));
        actions.add(SystemAction.get( AddResourceAction.class));
        actions.add(null);
        actions.add(SystemAction.get( CopyAction.class));
        actions.add(SystemAction.get( CutAction.class));
        actions.add(SystemAction.get( DeleteAction.class));
        actions.add(null);
        
        List<Action> actionsToAdd = JasperServerManager.getContributedMenuActionsFor( this.getResourceDescriptor() );
        
        if (actionsToAdd != null)
        {
            actions.addAll(actionsToAdd);
        }
        

        actions.add(SystemAction.get( RefreshAction.class));
        actions.add(SystemAction.get( PropertiesAction.class));
            
        return actions.toArray(new Action[actions.size()]);
    }

    public ResourceDescriptor getResourceDescriptor() {
        return getReportUnit().getDescriptor();
    }

    public RepositoryFolder getRepositoryObject() {
        return getReportUnit();
    }

    public void refreshChildrens(boolean reload) {
        ReportUnitChildren children = (ReportUnitChildren)getChildren();
        children.recalculateKeys(reload);
    }
    
    public void updateDisplayName() {
        fireDisplayNameChange(null,null);
    }
    
    public static RepositoryReportUnit getParentReportUnit(Node node)
    {
        if (node == null) return null;
        
        if ( (node instanceof ResourceNode) &&
            ((ResourceNode)node).getRepositoryObject() instanceof RepositoryReportUnit)
        {
            return (RepositoryReportUnit)((ResourceNode)node).getRepositoryObject();
        }
        
        return getParentReportUnit(node.getParentNode());
    }

 
     @Override
    public Transferable drag() throws IOException {
        ExTransferable tras = ExTransferable.create(clipboardCut());
        
        tras.put(new ReportObjectPaletteTransferable( 
                    "com.jaspersoft.ireport.jasperserver.ui.actions.CreateDrillDownAction",
                    getReportUnit()));
        return tras;
    }


    @Override
    public boolean canCopy() {
        return true;
    }

    @Override
    public boolean canCut() {
        return true;
    }

}
