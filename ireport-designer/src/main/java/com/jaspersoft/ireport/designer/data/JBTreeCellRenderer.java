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
package com.jaspersoft.ireport.designer.data;

import  javax.swing.tree.*;
import  javax.swing.*;
import  java.awt.*;
/**
 *
 * @author  Administrator
 */
public class JBTreeCellRenderer extends DefaultTreeCellRenderer {

    static ImageIcon objectIcon;
    static ImageIcon typeIcon;

    public JBTreeCellRenderer() {
        super();
        if (objectIcon == null) objectIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/data/object.png"));
        if (typeIcon == null) typeIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/data/type.png"));
    }

    @Override
    public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
            this.setForeground( Color.BLACK);
            ImageIcon icon = getElementIcon(value);
            setIcon(icon);

            setToolTipText(null);

        return this;
    }

    protected ImageIcon getElementIcon(Object value) {
        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode)value;
        this.setForeground( Color.BLACK);
                
        if (node.getUserObject() != null &&
            node.getUserObject() instanceof TreeJRField ) 
        {
            TreeJRField tf = (TreeJRField)node.getUserObject();
            if (tf.getObj() == null || tf.getObj().getName().startsWith("java.lang") || tf.getObj().isPrimitive())
            {
                return typeIcon;
            }
        }
     
        return objectIcon;
    }
}

