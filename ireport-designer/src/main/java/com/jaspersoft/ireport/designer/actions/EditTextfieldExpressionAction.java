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
import net.sf.jasperreports.engine.design.JRDesignTextField;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.editor.ExpressionEditor;
import com.jaspersoft.ireport.designer.undo.PropertyUndoableEdit;
import com.jaspersoft.ireport.designer.utils.Misc;
import javax.swing.JOptionPane;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import com.jaspersoft.ireport.designer.sheet.properties.AbstractProperty;

public final class EditTextfieldExpressionAction extends NodeAction {

        
    public String getName() {
        return I18n.getString("EditTextfieldExpressionAction.name");
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
        ElementNode node = (ElementNode)activatedNodes[0];
        //ExpressionEditor2 editor = new ExpressionEditor2();
        ExpressionEditor editor = new ExpressionEditor();

        JRDesignTextField element = (JRDesignTextField)node.getElement();

        if (ModelUtils.getTopElementGroup(element) instanceof JRDesignCellContents)
        {
            JRDesignCellContents contents = (JRDesignCellContents) ModelUtils.getTopElementGroup(element);
            editor.setExpressionContext(new ExpressionContext(contents.getOrigin().getCrosstab()));
        }
        else
        {
            JRDesignDataset dataset = ModelUtils.getElementDataset(element, node.getJasperDesign());
            if (dataset != null)
            {
                editor.setExpressionContext(new ExpressionContext(dataset));
            }
        }

        if (element.getExpression() != null)
        {
            editor.setExpression( Misc.getExpressionText( element.getExpression() ));
        }

        if (editor.showDialog(Misc.getMainFrame()) == JOptionPane.OK_OPTION)
        {
            JRDesignExpression oldExp = (JRDesignExpression) element.getExpression();
            JRDesignExpression newExp = new JRDesignExpression();
            newExp.setValueClassName(  getExpressionClassName(element) );
            newExp.setText( editor.getExpression() );
            element.setExpression(newExp);

            Object obj = ModelUtils.findProperty(node.getPropertySets(), JRDesignTextField.PROPERTY_EXPRESSION);
            if (obj != null && obj instanceof AbstractProperty)
            {
                PropertyUndoableEdit edit = new PropertyUndoableEdit((AbstractProperty)obj, oldExp, newExp);
                IReportManager.getInstance().addUndoableEdit(edit);
                IReportManager.getInstance().notifyReportChange();
            }
        }


    }

    private String getExpressionClassName(JRDesignTextField element)
    {
        if (element.getExpression() == null) return "java.lang.String";
        if (element.getExpression().getValueClassName() == null) return "java.lang.String";
        return element.getExpression().getValueClassName();
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length != 1) return false;
        if (!(activatedNodes[0] instanceof ElementNode)) return false;
        ElementNode node = (ElementNode)activatedNodes[0];
        if (node.getElement() instanceof JRDesignTextField)
        {
            return true;
        }
        return false;
    }
}