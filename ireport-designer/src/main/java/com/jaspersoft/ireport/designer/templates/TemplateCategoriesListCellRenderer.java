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
package com.jaspersoft.ireport.designer.templates;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author gtoffoli
 */
public class TemplateCategoriesListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        setOpaque(isSelected);
        setBorder(null);



        if (value instanceof TemplateCategory)
        {
            setText("   " + ((TemplateCategory)value).getSubCategory());
        }
        else
        {
            if (value.equals(TemplateCategory.CATEGORY_ALL_REPORTS))
            {
                setText("<html><b>All");
            }
            else if (value.equals(TemplateCategory.CATEGORY_OTHER_REPORTS))
            {
                setText("<html><b>Other");
            }
            else
            {
                setText("<html><b>" + getText());
            }
        }
        //setIcon(null);

        return this;
    }

    


}
