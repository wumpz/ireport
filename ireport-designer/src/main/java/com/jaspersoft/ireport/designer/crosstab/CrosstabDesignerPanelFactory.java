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

package com.jaspersoft.ireport.designer.crosstab;

import com.jaspersoft.ireport.designer.GenericDesignerPanel;
import com.jaspersoft.ireport.designer.GenericDesignerPanelFactory;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @version $Id: CrosstabDesignerPanelFactory.java 0 2010-03-12 15:12:58 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class CrosstabDesignerPanelFactory implements GenericDesignerPanelFactory{

    public GenericDesignerPanel createDesigner(JRDesignElement element, JasperDesign jasperdesign) {
            if (element instanceof JRDesignCrosstab)
            {
                return new CrosstabPanel((JRDesignCrosstab)element, jasperdesign);
            }
            return null;
    }

    public boolean accept(JRDesignElement element) {
        return element instanceof JRDesignCrosstab;
    }

}
