/*
 * Copyright (C) 2005 - 2008 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * ImageCellRenderer.java
 * 
 * Created on 20 maggio 2004, 8.49
 *
 */

package com.jaspersoft.ireport.addons.transformer.tool;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import org.openide.util.ImageUtilities;

/**
 *
 * @author  Administrator
 */
public class ImageCellRenderer extends DefaultTableCellRenderer {
    
    javax.swing.Icon icon1 = null;
    javax.swing.Icon icon2 = null;
    javax.swing.Icon icon3 = null;
    

    /** Creates a new instance of ImageCellRenderer */
    public ImageCellRenderer() {
        
        icon1 = new ImageIcon( ImageUtilities.loadImage("com/jaspersoft/ireport/addons/transformer/tool/docDirty.gif") );
        icon2 = new ImageIcon( ImageUtilities.loadImage("com/jaspersoft/ireport/addons/transformer/tool/doc.gif") );
        icon3 = new ImageIcon( ImageUtilities.loadImage("com/jaspersoft/ireport/addons/transformer/tool/warning.gif") );
        
        //label.setIcon( icon1 );
        //label.setText("");
    }
    
    @Override
    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof FileEntry)
        {
            if (((FileEntry)value).getStatus() == FileEntry.STATUS_ERROR_TRANSFORMING)
            {
                label.setIcon( icon3 );
            } 
            else if (((FileEntry)value).getStatus() == FileEntry.STATUS_NOT_TRANSFORMED || ((FileEntry)value).getStatus() == FileEntry.STATUS_TRANSFORMING)
            {
                label.setIcon( icon1 );
            }
            else if (((FileEntry)value).getStatus() == FileEntry.STATUS_TRANSFORMED)
            {
                label.setIcon( icon2 );
            }
        }
        else
        {
             label.setIcon( icon1 );
        }
        return label;
    
    }
    
}
