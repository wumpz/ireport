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
package com.jaspersoft.ireport.designer.styles;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.outline.nodes.AbstractStyleNode;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRSimpleTemplate;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTemplateReference;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignReportTemplate;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

/**
 *
 * @author gtoffoli
 */
public class ResetStyleAction extends NodeAction {

    @Override
    protected void performAction(Node[] nodes) {

            // Copy the style in the report...
        for (Node node : nodes)
        {
            JRBaseStyle style = ((AbstractStyleNode)node).getStyle();
            Node.PropertySet[] sets = node.getPropertySets();

            for (int i=0; i<sets.length; ++i)
            {
                Node.Property[] pp = sets[i].getProperties();
                for (int j=0; j<pp.length; ++j)
                {
                    if (pp[j].supportsDefaultValue() && !pp[j].isDefaultValue())
                    {
                        try {
                            pp[j].restoreDefaultValue();
                        } catch (Exception ex) {
                        }
                    }
                }
            }
        }

    }

    @Override
    protected boolean enable(Node[] nodes) {

        if (nodes.length == 0) return false;
        for (Node node : nodes)
        {
            if (!(node instanceof AbstractStyleNode)) return false;
        }

        return true;

    }

    @Override
    public String getName() {
        return I18n.getString("ResetStyleAction.name");
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

}
