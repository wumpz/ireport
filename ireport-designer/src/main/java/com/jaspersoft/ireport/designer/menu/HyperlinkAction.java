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

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.tools.HyperlinkPanel;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

/**
 *
 * @author gtoffoli
 */
public final class HyperlinkAction extends NodeAction {

    private static HyperlinkAction instance = null;
    
    public static synchronized HyperlinkAction getInstance()
    {
        if (instance == null)
        {
            instance = new HyperlinkAction();
        }
        
        return instance;
    }
    
    private HyperlinkAction()
    {
        super();
    }
    
    
    public String getName() {
        return I18n.getString("HyperlinkAction.Property.Hyperlink");
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
        
        
        JRHyperlink hyperlink = (JRHyperlink) ((ElementNode)activatedNodes[0]).getElement();
        JasperDesign design = ((ElementNode)activatedNodes[0]).getJasperDesign();
                        
        HyperlinkPanel pd = new HyperlinkPanel();
        pd.setExpressionContext(new ExpressionContext( ModelUtils.getElementDataset(((ElementNode)activatedNodes[0]).getElement(), design)) );
        pd.setHyperlink(hyperlink);
        pd.showDialog( Misc.getMainFrame() );
        
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        return (activatedNodes != null &&
                activatedNodes.length == 1 &&
                activatedNodes[0] instanceof ElementNode &&
                ((ElementNode)activatedNodes[0]).getElement() instanceof JRHyperlink);
    }
}
