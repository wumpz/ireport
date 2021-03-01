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
import com.jaspersoft.ireport.designer.dnd.DnDUtilities;
import com.jaspersoft.ireport.designer.outline.nodes.IRIndexedNode;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.jasperserver.ui.actions.AddResourceAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.DeleteAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.DeleteServerAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.ModifyServerAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.NewServerAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.PropertiesAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.RefreshAction;
import com.jaspersoft.ireport.jasperserver.ui.nodes.dnd.FolderPasteType;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.Action;
import javax.swing.ImageIcon;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.PasteAction;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class FolderNode extends IRIndexedNode implements ResourceNode {

    private boolean root = false;
    private RepositoryFolder folder = null;
    private boolean loading = false;
    
    protected static final ImageIcon loadingIcon = new ImageIcon(FolderNode.class.getResource("/com/jaspersoft/ireport/jasperserver/res/waiting.png"));
    protected static final ImageIcon serverIcon =  new ImageIcon(FolderNode.class.getResource("/com/jaspersoft/ireport/jasperserver/res/server.png"));
    protected static final ImageIcon folderIcon =  new ImageIcon(FolderNode.class.getResource("/com/jaspersoft/ireport/jasperserver/res/folder.png"));
    
    public boolean hasCustomizer() {
        return false;
    }
    
    public FolderNode(RepositoryFolder folder, Lookup doLkp) {
        this(new FolderChildren(folder, doLkp), folder, doLkp, false);
    }
    
    public FolderNode(RepositoryFolder folder, Lookup doLkp, boolean root) {
        this(new FolderChildren(folder, doLkp), folder, doLkp, root);
    }

    public FolderNode(FolderChildren pc, RepositoryFolder folder, Lookup doLkp, boolean root) {
        super(pc, pc.getIndex(), new ProxyLookup(doLkp, Lookups.fixed(folder, folder.getServer())));
        this.root = root;
        this.folder = folder;
        
        IReportManager.getPreferences().addPreferenceChangeListener(new PreferenceChangeListener() {

            public void preferenceChange(PreferenceChangeEvent pce) {
                fireDisplayNameChange(null, getDisplayName());
            }
        });
    }

    

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public RepositoryFolder getFolder() {
        return folder;
    }

    public void setFolder(RepositoryFolder folder) {
        this.folder = folder;
    }

    
    @Override
    public String getDisplayName() {
        
            
        String baseName = "";
        if (!IReportManager.getPreferences().getBoolean("jasperserver.showResourceIDs", false))
        {
            baseName =  ""+ getFolder().getDescriptor().getLabel();
        }
        else
        {
            baseName =  ""+getFolder().getDescriptor().getName();
            
            if (getFolder().isRoot())
            {
                baseName += " (" + getFolder().getDescriptor().getLabel() + ")";
            }
        }
        
        return baseName + ( (isLoading()) ? " (Loading....)" : "");
    }
        
    
    @Override
    public Image getIcon(int arg0) {
    
        if (isLoading())
        {
            return loadingIcon.getImage();
        }
        
        if (isRoot())
        {
            return serverIcon.getImage();
        }
        else
        {
            return folderIcon.getImage();
        }
    }
    
    @Override
    public Image getOpenedIcon(int arg0) {
    
        if (isLoading())
        {
            return loadingIcon.getImage();
        }
        else if (isRoot())
        {
            return serverIcon.getImage();
        }
        return folderIcon.getImage();
    
    }
    
    

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        this.fireIconChange();
    }

    @Override
    public Action[] getActions(boolean arg0) {


        List<Action> list = new ArrayList();
        list.add(SystemAction.get( AddResourceAction.class));
        list.add(null);

        if (isRoot())
        {

            list.add(SystemAction.get( NewServerAction.class));
            list.add(SystemAction.get( ModifyServerAction.class));
            list.add(SystemAction.get( DeleteServerAction.class));
            list.add(SystemAction.get( AddResourceAction.class));
            list.add(SystemAction.get( PasteAction.class ));
        }
        else
        {
            list.add(SystemAction.get( CopyAction.class ));
            list.add(SystemAction.get( CutAction.class ));
            list.add(SystemAction.get( PasteAction.class ));
            list.add(SystemAction.get( DeleteAction.class));
        }
        
        list.add(null);
        
        List<Action> actionsToAdd = JasperServerManager.getContributedMenuActionsFor( this.getResourceDescriptor() );
        
        if (actionsToAdd != null)
        {
            list.addAll(actionsToAdd);
        }
        
        
        list.add(SystemAction.get( RefreshAction.class));
        list.add(SystemAction.get( PropertiesAction.class));

        return list.toArray(new Action[list.size()]);
    }

    
    public ResourceDescriptor getResourceDescriptor() {
        return getFolder().getDescriptor();
    }

    public RepositoryFolder getRepositoryObject() {
        return getFolder();
    }

    public void refreshChildrens(boolean reload) {

        Object children = getChildren();
        if (children instanceof FolderChildren)
        {
            ((FolderChildren)children).recalculateKeys(reload);
        }
        else if (children instanceof ServerChildren)
        {
            ((ServerChildren)children).recalculateKeys();
        }
    }
    
    public void updateDisplayName() {
        fireDisplayNameChange(null,null);
    }


    @Override
    public PasteType getDropType(Transferable t, final int action, int index) {

        Node dropNode = NodeTransfer.node(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);
    
        return FolderPasteType.createFolderPasteType( DnDUtilities.getTransferAction(t), this, dropNode);

    }

    @Override
    public boolean canCopy() {
        return !isRoot();
    }

    @Override
    public boolean canCut() {
        return !isRoot();
    }



    @SuppressWarnings("unchecked")
    @Override
    protected void createPasteTypes(Transferable t, List s) {
        super.createPasteTypes(t, s);
        PasteType paste = getDropType(t, DnDConstants.ACTION_MOVE, -1);
        if (null != paste) {
            s.add(paste);
        }
    }
    
}
