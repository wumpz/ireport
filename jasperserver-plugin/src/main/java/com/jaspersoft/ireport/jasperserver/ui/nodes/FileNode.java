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
import com.jaspersoft.ireport.designer.outline.nodes.IRAbstractNode;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryFile;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.jasperserver.ui.RepositoryListCellRenderer;
import com.jaspersoft.ireport.jasperserver.ui.actions.DeleteAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.ImportDatasourceAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.ImportXMLADatasourceAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.OpenFileAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.PropertiesAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.RefreshAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.ReplaceFileAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.RunReportUnitAction;
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
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.ExTransferable;

/**
 *
 * @author gtoffoli
 */
public class FileNode extends IRAbstractNode implements ResourceNode {

    
    
    private RepositoryFile file = null;
    
    
    
    public FileNode(RepositoryFile file, Lookup doLkp) {
        super(Children.LEAF, doLkp);
        this.file = file;
        
        IReportManager.getPreferences().addPreferenceChangeListener(new PreferenceChangeListener() {

            public void preferenceChange(PreferenceChangeEvent pce) {
                fireDisplayNameChange(null, getDisplayName());
            }
        });
    }
    
    @Override
    public String getDisplayName() {
        
        if (!IReportManager.getPreferences().getBoolean("jasperserver.showResourceIDs", false))
        {
            return ""+getFile().getDescriptor().getLabel();
        }
        else
        {
            return ""+getFile().getDescriptor().getName();
        }
    }

    @Override
    public Image getIcon(int arg0) {

        return getResourceIcon(getFile().getDescriptor()).getImage();
        
    }
    
    public boolean hasCustomizer() {
        return false;
    }
    
    public static ImageIcon getResourceIcon(ResourceDescriptor resource)
    {
        return RepositoryListCellRenderer.getResourceIcon(resource);
    }
    
    public RepositoryFile getFile() {
        return file;
    }

    public void setFile(RepositoryFile file) {
        this.file = file;
    }




    @Override
    public Action[] getActions(boolean arg0) {
        
        List<Action> actions = new ArrayList<Action>();

        ResourceDescriptor resource = getFile().getDescriptor();

        if (resource.getWsType().equals(ResourceDescriptor.TYPE_IMAGE) ||
            resource.getWsType().equals(ResourceDescriptor.TYPE_JRXML) ||
            resource.getWsType().equals(ResourceDescriptor.TYPE_RESOURCE_BUNDLE) ||
            resource.getWsType().equals(ResourceDescriptor.TYPE_XML_FILE) ||
            resource.getWsType().equals(ResourceDescriptor.TYPE_STYLE_TEMPLATE))
        {
            actions.add(SystemAction.get( OpenFileAction.class));
            actions.add(SystemAction.get( ReplaceFileAction.class));
        }


        if (ReportUnitNode.getParentReportUnit(this) == null)
        {
            actions.add(SystemAction.get( CopyAction.class));
            actions.add(SystemAction.get( CutAction.class));
        }

        actions.add(SystemAction.get( DeleteAction.class));
        if (ReportUnitNode.getParentReportUnit(this) != null)
        {
            actions.add(null);
            actions.add(SystemAction.get( RunReportUnitAction.class));
        }

        if (getFile().getDescriptor().getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_JDBC))
        {
            actions.add(SystemAction.get( ImportDatasourceAction.class));
        }

        if (getFile().getDescriptor().getWsType().equals(ResourceDescriptor.TYPE_OLAP_XMLA_CONNECTION))
        {
            actions.add(SystemAction.get( ImportXMLADatasourceAction.class));
        }

        
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

    @Override
    public Action getPreferredAction() {
        if (getFile().getDescriptor().getWsType().equals(ResourceDescriptor.TYPE_JRXML))
        {
            return SystemAction.get( OpenFileAction.class);
        }

        return SystemAction.get( PropertiesAction.class);
    }

    public ResourceDescriptor getResourceDescriptor() {
        return getFile().getDescriptor();
    }

    public RepositoryFolder getRepositoryObject() {
        return getFile();
    }

    public void refreshChildrens(boolean b) {
    }

    public void updateDisplayName() {
        fireDisplayNameChange(null,null);
    }
    
    public void removeChildren(Node[] nodes)
    {
        getChildren().remove(nodes);
        fireNodeDestroyed();
    }
    
    @Override
    public Transferable drag() throws IOException {
        ExTransferable tras = ExTransferable.create(clipboardCut());
        
        if (getFile().getDescriptor().getWsType().equals(ResourceDescriptor.TYPE_IMAGE))
        {
            tras.put(new ReportObjectPaletteTransferable( 
                    "com.jaspersoft.ireport.jasperserver.ui.actions.CreateImageAction",
                    getFile()));
        }
        return tras;
    }
    
    @Override
    public boolean canCopy() {
        return ReportUnitNode.getParentReportUnit(this) == null;
    }

    @Override
    public boolean canCut() {
        return ReportUnitNode.getParentReportUnit(this) == null;
    }
}
