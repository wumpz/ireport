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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.tools.PageFormatPanel;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

public final class EditPageFormatAction extends NodeAction {

    @Override
    protected void performAction(Node[] nodes) {
        
        JasperDesign jd = IReportManager.getInstance().getActiveReport();
        if (jd != null)
        {

            PageFormatPanel pfp = new PageFormatPanel();
            pfp.setJasperDesign(jd);
            pfp.showDialog(null, true);
        }  
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public String getName() {
        return I18n.getString("action.pageFormat");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected boolean enable(Node[] nodes) {
        return IReportManager.getInstance().getActiveReport() != null;
    }
}