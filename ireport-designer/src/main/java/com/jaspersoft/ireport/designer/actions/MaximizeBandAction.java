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
import com.jaspersoft.ireport.designer.ModelUtils;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;
import com.jaspersoft.ireport.designer.outline.nodes.BandNode;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.designer.undo.PropertyUndoableEdit;

public final class MaximizeBandAction extends NodeAction {

    public String getName() {
        return I18n.getString("MaximizeBandAction.Name");
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

        JRDesignBand band = ((BandNode)activatedNodes[0]).getBand();
        int height = ModelUtils.getMaxBandHeight(band, ((BandNode)activatedNodes[0]).getJasperDesign() );
        int oldValue = band.getHeight();
        if (oldValue < height)
        {
            band.setHeight(height);
            ObjectPropertyUndoableEdit undo = new ObjectPropertyUndoableEdit(band,"Height",Integer.TYPE,oldValue,height);
            IReportManager.getInstance().addUndoableEdit(undo);
            IReportManager.getInstance().notifyReportChange();
        }
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        return ( activatedNodes != null &&
                 activatedNodes.length == 1 &&
                 activatedNodes[0] instanceof BandNode);
    }
}