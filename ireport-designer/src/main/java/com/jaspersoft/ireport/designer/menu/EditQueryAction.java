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
import com.jaspersoft.ireport.designer.data.ReportQueryDialog;
import com.jaspersoft.ireport.designer.outline.nodes.DatasetNode;
import com.jaspersoft.ireport.designer.outline.nodes.ReportNode;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

public final class EditQueryAction extends NodeAction {

    @Override
    protected void performAction(Node[] nodes) {
        
        JRDesignDataset dataset = null;
        
        Node node = nodes[0];
        if (node instanceof ReportNode)
            dataset = (JRDesignDataset)((ReportNode)node).getJasperDesign().getMainDataset();
        else if (node instanceof DatasetNode)
            dataset = ((DatasetNode)node).getDataset();
        
       if (dataset != null)
       {
           Window pWin = Misc.getMainWindow();
           ReportQueryDialog rqd = null;
           if (pWin instanceof Dialog) rqd = new ReportQueryDialog((Dialog)pWin, true);
           else if (pWin instanceof Frame) rqd = new ReportQueryDialog((Frame)pWin, true);
           else rqd = new ReportQueryDialog((Dialog)null, true);

           rqd.setDataset(dataset);
           rqd.setVisible(true);
       }
    }

    public String getName() {
        return NbBundle.getMessage(EditQueryAction.class, "CTL_EditQueryAction");
    }

    @Override
    protected String iconResource() {
        return "com/jaspersoft/ireport/designer/resources/query-16.png";
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
        return (nodes != null && nodes.length == 1 && (nodes[0] instanceof ReportNode || nodes[0] instanceof DatasetNode));
    }
}