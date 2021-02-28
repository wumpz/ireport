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
package com.jaspersoft.ireport.designer.utils;

import com.jaspersoft.ireport.locale.I18n;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataFolder;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.actions.Presenter;
import org.openide.util.lookup.Lookups;

public class SubMenuAction extends AbstractAction implements HelpCtx.Provider, Presenter.Popup {


    private static Map<String,SubMenuAction> nodeActionsMap = new HashMap<String,SubMenuAction>();

    private JMenu menu = null;
    private String layerPath = null;

    public static SubMenuAction getAction(String layerPath)
    {
        SubMenuAction action = null;
        if (!nodeActionsMap.containsKey(layerPath))
        {
            action = new SubMenuAction(layerPath);
            nodeActionsMap.put(layerPath, action);
        }
        else
        {
            action = nodeActionsMap.get(layerPath);
        }
    
        // update status...
        return action;
    }

    private SubMenuAction(String layerPath) {
        this.layerPath = layerPath;
    }

    public String getName() {
        return I18n.getString(getLayerPath());
    }

//    @Override
//    protected void initialize() {
//        super.initialize();
//        // see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
//        putValue("noIconInMenu", Boolean.TRUE); // NOI18N
//    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    
    public JMenuItem getPopupPresenter() {

        
            menu = new JMenu(getName());

            FileObject nodesFileObject = Repository.getDefault().getDefaultFileSystem().getRoot().getFileObject(getLayerPath());
            
            if (nodesFileObject == null) return null;
            DataFolder nodesDataFolder = DataFolder.findFolder(nodesFileObject);

            if (nodesDataFolder == null) return null;

            Lookup lookup = Lookups.forPath(getLayerPath()); // NOI18N
            Collection<? extends Object> nodeActions = lookup.lookupAll(Object.class);

            Lookup context = Lookup.getDefault();

            Iterator<? extends Object> it = nodeActions.iterator();
            while (it.hasNext ()) {
                
                Object obj =it.next();
                if (obj instanceof Action)
                {
                    JMenuItem mi = new JMenuItem();
                    org.openide.awt.Actions.connect(mi, (Action)obj, true);
                    Icon icon = (Icon) ((Action)obj).getValue( Action.SMALL_ICON );
                    if (icon != null) mi.setIcon(icon);
                    menu.add(mi);
                }
                else if (obj instanceof JSeparator)
                {
                    menu.add((JSeparator)obj);
                }
            }
            /*
            Enumeration<DataObject> enObj = nodesDataFolder.children();
            while (enObj.hasMoreElements())
            {
                DataObject dataObject = enObj.nextElement();
                NodeAction nodeAction = dataObject.getLookup().lookup(NodeAction.class);

                if (nodeAction != null)
                {
                    menu.add(nodeAction.getMenuPresenter());
                }
            }
            */
        

        return menu;
    }
//
//    @Override
//    protected boolean asynchronous() {
//        return false;
//    }

    /**
     * @return the layerPath
     */
    public String getLayerPath() {
        return layerPath;
    }

    /**
     * @param layerPath the layerPath to set
     */
    public void setLayerPath(String layerPath) {
        this.layerPath = layerPath;
    }

//    @Override
//    protected void performAction(Node[] arg0) {
//    }
//
//    @Override
//    protected boolean enable(Node[] arg0) {
//        return true;
//    }

    public void actionPerformed(ActionEvent arg0) {

    }


}
