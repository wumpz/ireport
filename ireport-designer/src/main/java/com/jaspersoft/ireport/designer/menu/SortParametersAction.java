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

import com.jaspersoft.ireport.designer.outline.nodes.ParametersNode;
import com.jaspersoft.ireport.designer.outline.nodes.SortableParametersNode;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;
import org.openide.util.actions.Presenter;

/**
 *
 * @version $Id: SortFieldsAction.java 0 2010-01-05 10:49:06 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class SortParametersAction  extends NodeAction implements Presenter.Menu, Presenter.Popup {

    private static JCheckBoxMenuItem SORT_MENU;

    public String getName() {
        return I18n.getString("Inspector.SortParameters");
    }

    public SortParametersAction()
    {
        super();
        SORT_MENU = new JCheckBoxMenuItem(getName());
        SORT_MENU.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                performAction(getActivatedNodes());
            }
        });
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

    protected void performAction(org.openide.nodes.Node[] activatedNodes) {
        ((SortableParametersNode)activatedNodes[0]).setSort(!((SortableParametersNode)activatedNodes[0]).isSort());
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes != null &&
                activatedNodes.length == 1 &&
                activatedNodes[0] instanceof SortableParametersNode)
        {
            SORT_MENU.setEnabled(true);
            SORT_MENU.setSelected( ((SortableParametersNode)activatedNodes[0]).isSort());
            return true;
        }
        SORT_MENU.setEnabled(false);
        return false;
    }

    @Override
    public JMenuItem getMenuPresenter()
    {
        return SORT_MENU;
    }

    @Override
    public JMenuItem getPopupPresenter()
    {
        return SORT_MENU;
    }
}

