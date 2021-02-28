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

package com.jaspersoft.ireport.designer.editor;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.openide.util.ImageUtilities;

/**
 *
 * @version $Id: NamedIconTreeCellRenderer.java 0 2010-08-03 17:27:18 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class NamedIconTreeCellRenderer extends DefaultTreeCellRenderer {

    static ImageIcon defaultIcon = null;

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        if (defaultIcon == null)
        {
            defaultIcon = new ImageIcon( ImageUtilities.loadImage(NamedIconItem.ICON_FOLDER_VARIABLES));
        }

        this.setIcon(defaultIcon);
        Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        if (value instanceof DefaultMutableTreeNode)
        {
            value = ((DefaultMutableTreeNode)value).getUserObject();
        }

        if (c instanceof JLabel && value instanceof NamedIconItem && value != null)
        {
            NamedIconItem item = (NamedIconItem)value;
            ((JLabel)c).setText( item.getDisplayName() );
            ((JLabel)c).setIcon( item.getIcon() );
        }
        
        return c;

    }

}
