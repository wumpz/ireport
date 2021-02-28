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
package com.jaspersoft.ireport.designer.outline.nodes;

import com.jaspersoft.ireport.designer.ModelUtils;
import net.sf.jasperreports.crosstabs.design.JRCrosstabOrigin;

/**
 *
 * @author gtoffoli
 */
public class NullCell {
    
    private JRCrosstabOrigin origin = null;
    private String name = null;
    
    public NullCell(JRCrosstabOrigin origin)
    {
        this.origin = origin;
        this.name = ModelUtils.nameOf(origin);
    }
    
    @Override
    public String toString()
    {
        return ""+name;
    }

    public JRCrosstabOrigin getOrigin() {
        return origin;
    }

    public void setOrigin(JRCrosstabOrigin origin) {
        this.origin = origin;
        this.name = ModelUtils.nameOf(origin);
    }
}
