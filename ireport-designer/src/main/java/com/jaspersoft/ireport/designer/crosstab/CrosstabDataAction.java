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
package com.jaspersoft.ireport.designer.crosstab;

import com.jaspersoft.ireport.designer.charts.*;
import com.jaspersoft.ireport.designer.outline.nodes.CrosstabNode;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Dialog;
import java.awt.Frame;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

/**
 *
 * @author gtoffoli
 */
public final class CrosstabDataAction extends NodeAction {

    private CrosstabDataAction()
    {
        super();
    }
    
    
    public String getName() {
        return "Crosstab Data";
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
        
        JRDesignCrosstab crosstab = (JRDesignCrosstab)((CrosstabNode)activatedNodes[0]).getElement();
        JasperDesign design = ((ElementNode)activatedNodes[0]).getJasperDesign();
                     
        Object pWin = Misc.getMainWindow();
        CrosstabDataDialog pd = null;
        if (pWin instanceof Dialog) pd = new CrosstabDataDialog((Dialog)pWin,true);
        else pd = new CrosstabDataDialog((Frame)pWin,true);
        
        pd.setCrosstabElement(crosstab, design);
        pd.setVisible(true);
        
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        return (activatedNodes != null &&
                activatedNodes.length == 1 &&
                activatedNodes[0] instanceof CrosstabNode);
    }
}
