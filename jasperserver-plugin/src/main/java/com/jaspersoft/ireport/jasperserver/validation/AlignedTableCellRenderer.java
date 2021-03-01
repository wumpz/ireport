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
package com.jaspersoft.ireport.jasperserver.validation;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author gtoffoli
 */
public class AlignedTableCellRenderer extends DefaultTableCellRenderer {
    
    private int alignment = JLabel.RIGHT;
    static ImageIcon imageIcon;
    static ImageIcon subreportIcon;
    static ImageIcon unknownIcon;
    static ImageIcon linkIcon;
    static ImageIcon templateIcon;

    /** Creates a new instance of AlignedTableCellRenderer */
    public AlignedTableCellRenderer() {
        this(JLabel.RIGHT);
    }
    
    /** Creates a new instance of AlignedTableCellRenderer */
    public AlignedTableCellRenderer(int alignment) {
        super();
        if (subreportIcon == null) subreportIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/ui/resources/subreport-16.png"));
        if (imageIcon == null) imageIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/ui/resources/image-16.png"));
        if (templateIcon == null) templateIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/res/style-16.png"));
        if (unknownIcon == null) unknownIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/res/unknow.png"));
        if (linkIcon == null) linkIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/res/link.png"));

        this.alignment = alignment;
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (c instanceof JLabel)
                {
                    ((JLabel)c).setHorizontalAlignment( getAlignment());

                    if (value instanceof ElementValidationItem && ((ElementValidationItem)value).isStoreAsLink())
                    {
                        ((JLabel)c).setIcon( linkIcon );
                        ((JLabel)c).setText( "Linked resource" );
                    }
                    else if (value instanceof ImageElementValidationItem)
                    {
                         ((JLabel)c).setIcon( imageIcon );
                         ((JLabel)c).setText( "Image" );
                    }
                    else if (value instanceof SubReportElementValidationItem)
                    {
                         ((JLabel)c).setIcon( subreportIcon );
                         ((JLabel)c).setText( "Subreport" );
                    }
                    else if (value instanceof TemplateElementValidationItem)
                    {
                         ((JLabel)c).setIcon( templateIcon );
                         ((JLabel)c).setText( "Template" );
                    }
                    else if (value instanceof ElementValidationItem)
                    {
                         ((JLabel)c).setIcon( unknownIcon );
                         ((JLabel)c).setText( "Other resource type" );
                    }

                }
                
                return c;
    }

    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }
    
}
