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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;

/**
 *
 * @author gtoffoli
 */
public class NamedIconItem {
    
    public static final String ICON_FOLDER_FIELDS = "com/jaspersoft/ireport/designer/resources/fields-16.png";
    public static final String ICON_FOLDER_PARAMETERS = "com/jaspersoft/ireport/designer/resources/parameters-16.png";
    public static final String ICON_FOLDER_VARIABLES = "com/jaspersoft/ireport/designer/resources/variables-16.png";
    public static final String ICON_FOLDER = "com/jaspersoft/ireport/designer/resources/folder.png";
    public static final String ICON_FOLDER_WIZARDS = ICON_FOLDER;
    public static final String ICON_FOLDER_RECENT_EXPRESSIONS = ICON_FOLDER;
    public static final String ICON_FOLDER_FORMULAS = ICON_FOLDER;
    public static final String ICON_CROSSTAB = "com/jaspersoft/ireport/designer/resources/crosstab-16.png";
    
    private Object item = null;
    private String displayName = null;
    private Icon icon = null;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }
    
    public NamedIconItem(Object item)
    {
        this(item, null);
    }
    
    public NamedIconItem(Object item, String displayName)
    {
        this(item, displayName, (Icon)null);
    }
    
    public NamedIconItem(Object item, String displayName, Icon icon)
    {
        this.item = item;
        this.displayName = displayName;
        this.icon = icon;
    }
    
    public NamedIconItem(Object item, String displayName, String iconName)
    {
        this(item, displayName);
        try {
            this.icon = new ImageIcon( ImageUtilities.loadImage(iconName) );
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    @Override
    public String toString()
    {
        return (displayName == null) ? item+"" : displayName;
    }
}
