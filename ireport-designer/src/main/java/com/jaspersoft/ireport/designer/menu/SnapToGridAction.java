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
package com.jaspersoft.ireport.designer.menu;

import com.jaspersoft.ireport.designer.ReportDesignerPanel;
import java.util.Iterator;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import org.openide.util.ContextAwareAction;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.actions.Presenter;

public final class SnapToGridAction extends CallableSystemAction
        implements Presenter.Menu, LookupListener {

    private static JCheckBoxMenuItem SNAP_TO_GRID_MENU;
    private final Lookup lkp;
    private final Lookup.Result <? extends ReportDesignerPanel> result;

    public void performAction() {
        
       // reportDesignerPanel
       Iterator<? extends ReportDesignerPanel> i = result.allInstances().iterator();
       if (i.hasNext())
       {
         ReportDesignerPanel rdp = i.next();
         rdp.setSnapToGrid(SNAP_TO_GRID_MENU.isSelected());
       }
    }
    
    public SnapToGridAction(){
            this (Utilities.actionsGlobalContext());
    }
    
    private SnapToGridAction(Lookup lkp) {
        
        
        SNAP_TO_GRID_MENU  = new JCheckBoxMenuItem(getName());
        this.lkp = lkp;
        result = lkp.lookupResult(ReportDesignerPanel.class);
        result.addLookupListener(this);
        result.allItems();
        SNAP_TO_GRID_MENU.addActionListener(this);
        setMenu();
    }
    
    public void resultChanged(LookupEvent e) {
        setMenu();
    }
    
    protected void setMenu(){
        
            Iterator<? extends ReportDesignerPanel> i = result.allInstances().iterator();
            SNAP_TO_GRID_MENU.setEnabled(i.hasNext());
            if (i.hasNext())
            {
                ReportDesignerPanel rdp = i.next();
                SNAP_TO_GRID_MENU.setSelected(rdp.isGridVisible());
            }
        }
    
    public String getName() {
        return NbBundle.getMessage(SnapToGridAction.class, "CTL_SnapToGridAction");
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    public JMenuItem getMenuPresenter()
    {
        return SNAP_TO_GRID_MENU;
    }
       
}