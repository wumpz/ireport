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
package com.jaspersoft.ireport.designer.formatting.actions;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;

/**
 *
 * @author gtoffoli
 */
public class OrganizeAsTableAction extends AbstractFormattingToolAction {

    
    public OrganizeAsTableAction()
    {
        super();
        putValue(Action.NAME, getName());
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
    }
    
    @Override
    protected String iconResource() {
        return "com/jaspersoft/ireport/designer/resources/formatting/organize.png";
    }

    
    @Override
    protected void performAction(Node[] nodes) {
        
        if (nodes.length == 0) return;
        JasperDesign jd = nodes[0].getLookup().lookup(JasperDesign.class);
        if (jd == null) return;
        
        List<JRDesignElement> elements = getSelectedElements(nodes);


        elements = this.sortXY(elements);
        
        IReportManager.getInstance().setForceAggregateUndo(true);
        SystemAction.get(AlignMarginTopAction.class).performAction(nodes);
        
        int currentX = 0;
        for (JRDesignElement element : elements)
        {
            // 1. Find the parent...
            Rectangle oldBounds = getElementBounds(element);
            element.setX( currentX  );
            currentX += element.getWidth();
            addTransformationUndo( element, oldBounds, true);
        }
        
        SystemAction.get(SameHeightMinAction.class).performAction(nodes);
        SystemAction.get(IncreaseHSpaceAction.class).performAction(nodes);
        IReportManager.getInstance().setForceAggregateUndo(false);
    }

    @Override
    public String getName() {
        return I18n.getString("formatting.tools.organizeAsTable");
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public JMenuItem getPopupPresenter() {
        JMenuItem item = super.getPopupPresenter();
        item.setIcon(getIcon());
        return item;
    }

    

}
