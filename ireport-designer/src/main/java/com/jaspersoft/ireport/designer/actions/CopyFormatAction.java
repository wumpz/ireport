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
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;
import com.jaspersoft.ireport.designer.ModelUtils;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.nodes.Node.PropertySet;

public final class CopyFormatAction extends NodeAction {

        
   public static final String[] propertyNames = new String[] {
        JRBaseStyle.PROPERTY_BACKCOLOR,
        JRBaseStyle.PROPERTY_BLANK_WHEN_NULL,
        JRBaseStyle.PROPERTY_BOLD,
        JRBaseStyle.PROPERTY_FILL,
        JRBaseStyle.PROPERTY_FONT_NAME,
        JRBaseStyle.PROPERTY_FONT_SIZE,
        JRBaseStyle.PROPERTY_FORECOLOR,
        JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT,
        JRBaseStyle.PROPERTY_ITALIC,
        JRBaseStyle.PROPERTY_LINE_SPACING,
        JRBaseStyle.PROPERTY_MARKUP,
        JRBaseStyle.PROPERTY_MODE,
        JRBaseStyle.PROPERTY_PATTERN,
        JRBaseStyle.PROPERTY_PDF_EMBEDDED,
        JRBaseStyle.PROPERTY_PDF_ENCODING,
        JRBaseStyle.PROPERTY_PDF_FONT_NAME,
        JRBaseStyle.PROPERTY_RADIUS,
        JRBaseStyle.PROPERTY_ROTATION,
        JRBaseStyle.PROPERTY_SCALE_IMAGE,
        JRBaseStyle.PROPERTY_STRIKE_THROUGH,
        JRBaseStyle.PROPERTY_UNDERLINE,
        JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT,
        JRDesignElement.PROPERTY_PARENT_STYLE
    };


    public static final Map formattingValues = new HashMap();

    public String getName() {
        return I18n.getString("CopyFormatAction.name");
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

        Node node = activatedNodes[0];
        formattingValues.clear();

        for (int i=0; i<propertyNames.length; ++i)
        {
            Object val = getPropertyValue(node, propertyNames[i]);
            if (val != null)
            {
                formattingValues.put(propertyNames[i], val);
            }
        }

    }

    protected Object getPropertyValue(Node node, String prop)
    {
        PropertySet[] sets = node.getPropertySets();
        Property p = ModelUtils.findProperty(sets, prop);
        try {
            return p.getValue();
        } catch (Exception  ex) {
            return null;
        }
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length != 1) return false;
        if (!(activatedNodes[0] instanceof ElementNode)) return false;
        return true;
    }
}