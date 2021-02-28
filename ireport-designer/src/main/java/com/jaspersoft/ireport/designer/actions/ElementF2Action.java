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

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;

/**
 *
 * @version $Id: ElementF2Action.java 0 2009-12-30 11:24:29 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class ElementF2Action extends AbstractAction {

    public void actionPerformed(ActionEvent e) {
        // Look for selected elements...
        JrxmlVisualView view = IReportManager.getInstance().getActiveVisualView();

        if (view == null) return;

        AbstractReportObjectScene scene = view.getReportDesignerPanel().getActiveScene();
        if (scene == null) return;

        if (scene.getSelectedObjects().isEmpty()) return;

        Object selectedObject = scene.getSelectedObjects().iterator().next();

        if (selectedObject instanceof JRDesignTextField)
        {
           // Find the node for this element...
            Node node = IReportManager.getInstance().findNodeOf(selectedObject, view.getExplorerManager().getRootContext());
            if (node != null)
            {
                SystemAction.get(EditTextfieldExpressionAction.class).performAction(new Node[]{node});
            }
        }


    }

}
