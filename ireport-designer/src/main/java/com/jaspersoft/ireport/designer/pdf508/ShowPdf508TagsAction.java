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
package com.jaspersoft.ireport.designer.pdf508;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.locale.I18n;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import org.openide.util.HelpCtx;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.actions.Presenter;

public final class ShowPdf508TagsAction extends CallableSystemAction
        implements Presenter.Menu, LookupListener {

    private static JCheckBoxMenuItem SHOW_TAGS_MENU;
    
    public void performAction() {

        IReportManager.getPreferences().putBoolean("showPDF508Tags", SHOW_TAGS_MENU.isSelected());
        if (IReportManager.getInstance().getActiveReport() != null)
        {
            IReportManager.getInstance().getActiveVisualView().getReportDesignerPanel().repaint();
        }
    }
    
    private ShowPdf508TagsAction() {

        SHOW_TAGS_MENU  = new JCheckBoxMenuItem(getName());
        SHOW_TAGS_MENU.addActionListener(this);
        setMenu();
    }
    
    public void resultChanged(LookupEvent e) {
        setMenu();
    }
    
    protected void setMenu(){
       SHOW_TAGS_MENU.setSelected(IReportManager.getPreferences().getBoolean("showPDF508Tags",false));
    }
    
    public String getName() {
        return I18n.getString("pdf508.showTags");
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
        return SHOW_TAGS_MENU;
    }
       
}