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
package com.jaspersoft.ireport.designer.jrctx;

import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

/**
 *
 * @author gtoffoli
 */
public class ExportAsJarAction extends NodeAction {

    public String getName() {
        return I18n.getString("ExportAsJarAction.name");
    }

    @Override
    protected String iconResource() {
        return "com/jaspersoft/ireport/designer/resources/export_jrctx_action-16.png";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    protected void performAction(org.openide.nodes.Node[] activatedNodes) {

        JRCTXEditorSupport editorSupport =  activatedNodes[0].getLookup().lookup(JRCTXEditorSupport.class);
        ExportToJarDialog d = new ExportToJarDialog(Misc.getMainFrame(), true);
        d.setJRCTXEditorSupport(editorSupport);
        d.setVisible(true);

    }


    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length == 0) return false;

        // Check if all the elements are a JRBoxContainer
        return (activatedNodes.length == 1 && activatedNodes[0].getLookup().lookup(JRCTXEditorSupport.class) != null);
    }
}
