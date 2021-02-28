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
package com.jaspersoft.ireport.designer.actions;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.menu.RunReportAction;
import javax.swing.Action;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

public final class CompileReportAction extends NodeAction {

    public String getName() {
        return I18n.getString("CompileReport.Name");
    }



    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        //putValue("noIconInMenu", Boolean.TRUE);
        putValue(Action.SHORT_DESCRIPTION, I18n.getString("CompileReport.Name"));
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    protected void performAction(org.openide.nodes.Node[] activatedNodes) {

        JrxmlVisualView view = IReportManager.getInstance().getActiveVisualView();
        if (view != null)
        {
            
            RunReportAction.runReport(view.getEditorSupport(), true);
        }
    }

    @Override
    protected String iconResource() {
        return "com/jaspersoft/ireport/designer/resources/compile-16.png";
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        
        return IReportManager.getInstance().getActiveVisualView() != null;
    }
}