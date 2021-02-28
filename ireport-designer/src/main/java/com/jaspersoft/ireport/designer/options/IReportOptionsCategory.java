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
package com.jaspersoft.ireport.designer.options;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.spi.options.OptionsCategory;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

public final class IReportOptionsCategory extends OptionsCategory {

    public Icon getIcon() {
        return new ImageIcon(ImageUtilities.loadImage("com/jaspersoft/ireport/designer/options/ireport_icon.png"));
    }

    public String getCategoryName() {
        return NbBundle.getMessage(IReportOptionsCategory.class, "OptionsCategory_Name_IReport");
    }

    public String getTitle() {
        return NbBundle.getMessage(IReportOptionsCategory.class, "OptionsCategory_Title_IReport");
    }

    public OptionsPanelController create() {
        return new IReportOptionsPanelController();
    }
}
