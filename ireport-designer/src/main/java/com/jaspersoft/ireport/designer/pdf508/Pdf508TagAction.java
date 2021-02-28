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

import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import javax.swing.JMenuItem;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

/**
 *
 * @author gtoffoli
 */
public class Pdf508TagAction extends NodeAction {

    Pdf508TagMenuUtility menuUtility = null;
    public Pdf508TagAction()
    {
        menuUtility = new Pdf508TagMenuUtility();
    }

    @Override
    public JMenuItem getMenuPresenter() {
        return menuUtility.getMenu();
    }

    @Override
    public JMenuItem getPopupPresenter() {
        return menuUtility.getMenu();
    }

    @Override
    protected void performAction(Node[] arg0) {
    }

    @Override
    protected boolean enable(Node[] nodes) {
        if (nodes.length == 1 &&
               nodes[0] instanceof ElementNode)
        {
            menuUtility.setElement( ((ElementNode)nodes[0]).getElement());
        }
        return false;
    }

    @Override
    public String getName() {
        return "PDF 508 Tag";
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



}
