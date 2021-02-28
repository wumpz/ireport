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
package com.jaspersoft.ireport.designer.data.fieldsproviders.olap;


import  javax.swing.tree.*;
import  javax.swing.*;
import  java.awt.*;
import mondrian.olap.QueryAxis;
/**
 *
 * @author  Administrator
 */
public class OlapTreeCellRenderer extends DefaultTreeCellRenderer {

    
    static ImageIcon measureIcon;
    static ImageIcon dimensionIcon;
    static ImageIcon hierarchyIcon;
    
    public OlapTreeCellRenderer() {
        super();

        if (measureIcon == null) measureIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/olap/measure.png"));
        if (dimensionIcon == null) dimensionIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/olap/axis.png"));
        if (hierarchyIcon == null) hierarchyIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/olap/hierarchy.png"));
        
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
        
        if (!(value instanceof DefaultMutableTreeNode)) return null; // ?!!!
        
        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode)value;

        if (node.getParent() != null)
        {
            if (!(node.getUserObject() instanceof OlapElement)) return dimensionIcon;

            OlapElement olapElement = (OlapElement)node.getUserObject();
            if (olapElement.getType() == OlapElement.TYPE_AXIS) return dimensionIcon;
            if (olapElement.getType() == OlapElement.TYPE_HIERARCHY) return hierarchyIcon;
            if (olapElement.getType() == OlapElement.TYPE_MEASURE) return measureIcon;
            return hierarchyIcon;

        }
        return null;
    }
}

