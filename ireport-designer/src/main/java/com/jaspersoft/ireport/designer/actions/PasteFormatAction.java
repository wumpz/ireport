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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.sheet.properties.AbstractProperty;
import com.jaspersoft.ireport.designer.undo.PropertyUndoableEdit;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.nodes.Node.PropertySet;

public final class PasteFormatAction extends NodeAction {


    public String getName() {
        return I18n.getString("PasteFormatAction.name");
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

         PropertyUndoableEdit undo = null;
        for (int k=0; k<activatedNodes.length; ++k)
        {
            Node node = activatedNodes[k];
            PropertySet[] sets = node.getPropertySets();
           
            for (int i=0; i<CopyFormatAction.propertyNames.length; ++i)
            {
                if (CopyFormatAction.formattingValues.containsKey(CopyFormatAction.propertyNames[i]))
                {
                    Property p = ModelUtils.findProperty(sets, CopyFormatAction.propertyNames[i]);
                    if (p != null)
                    {
                        Object oldValue;
                        try {
                            oldValue = p.getValue();

                            Object newValue = CopyFormatAction.formattingValues.get(CopyFormatAction.propertyNames[i]);
                            if (newValue != null)
                            {
                                p.setValue(newValue);
                                if (p instanceof AbstractProperty)
                                {
                                     if (undo == null)
                                     {
                                        undo = new PropertyUndoableEdit((AbstractProperty)p,oldValue,newValue);
                                     }
                                     else
                                     {
                                         undo.concatenate(new PropertyUndoableEdit((AbstractProperty)p,oldValue,newValue));
                                     }
                                }
                            }
                        } catch (Exception ex) {
                        }
                    }
                }
            }
        }
        if (undo != null)
        {
            IReportManager.getInstance().addUndoableEdit(undo);
        }
    }


    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (CopyFormatAction.formattingValues.isEmpty()) return false;
        if (activatedNodes == null || activatedNodes.length < 1) return false;
        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (!(activatedNodes[i] instanceof ElementNode)) return false;
        }
        return true;
    }
}