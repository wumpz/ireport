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
package com.jaspersoft.ireport.designer.errorhandler;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.editor.ExpressionEditor;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.outline.nodes.ExpressionHolder;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.beans.PropertyVetoException;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author gtoffoli
 * Please not that this class is not thread safe.
 */
public class ErrorLocator {
    
    public void locateError(JrxmlVisualView view, ProblemItem item) 
    {
        // 1. try to select the source of the problem...
        Node node = IReportManager.getInstance().findNodeOf(item.getProblemReference(), view.getModel());
        if (node != null)
        {
            focusNode(view, node);
        }
        
        // 2. try to check if the reference is an expression (very common case...)
        if (item.getProblemReference() instanceof JRDesignExpression)
        {
            focusExpression(view,  (JRDesignExpression)item.getProblemReference());
        }
    }
    
    
    public void focusNode(JrxmlVisualView view, Node node)
    {
        try {
                view.getExplorerManager().setSelectedNodes(new Node[]{node});
                // if the node is an ElementNode, be sure to make the object visible...
                if (node instanceof ElementNode)
                {
                    JRDesignElement element = ((ElementNode)node).getElement();
                    AbstractReportObjectScene scene = view.getReportDesignerPanel().getSceneOf(element);
                    if (scene != null)
                    {
                        scene.assureVisible(element);
                    }
                }
                
            } catch (PropertyVetoException ex){}
    }
    
    
    public void focusExpression(JrxmlVisualView view, JRDesignExpression expression)
    {
            // 1 look for the node holding the expression...
            Node root = view.getModel();
            Node node = findExpressionNode( root, expression);
        
            if (node != null) focusNode(view, node);
            
            ExpressionEditor editor = new ExpressionEditor();
            
            if (node instanceof ExpressionHolder)
            {
                ExpressionContext context = ((ExpressionHolder)node).getExpressionContext(expression);
                editor.setExpressionContext(context);
            }
            else
            {
                // By default let's use the master dataset as expression context...
                
            }
            // here we should find the ExpressionContext....
            
            editor.setExpression(Misc.getExpressionText(expression));
            if (editor.showDialog(Misc.getMainFrame()) == JOptionPane.OK_OPTION)
            {
                expression.setText( editor.getExpression() );
                IReportManager.getInstance().notifyReportChange();
            }
    }
    
    public Node findExpressionNode( Node node, JRDesignExpression expression)
    {
        if (node instanceof ExpressionHolder)
        {
            if (((ExpressionHolder)node).hasExpression(expression)) return node;
        }
        
        Node[] nodes = node.getChildren().getNodes();
        for (int i=0; i<nodes.length; ++i)
        {
            Node newNode = findExpressionNode( nodes[i], expression);
            if (newNode != null) return newNode;
        }
        
        return null;
    }
}
