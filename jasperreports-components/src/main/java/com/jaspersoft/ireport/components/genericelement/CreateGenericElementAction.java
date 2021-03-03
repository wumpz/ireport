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
package com.jaspersoft.ireport.components.genericelement;

import com.jaspersoft.ireport.designer.palette.actions.*;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignGenericElement;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class CreateGenericElementAction extends CreateReportElementAction {

    @Override
    public JRDesignElement createReportElement(JasperDesign jd) {

        JRDesignGenericElement element = new JRDesignGenericElement(jd);
        JRGenericElementType type = new JRGenericElementType("http://www.somenamespace.com", "someName");
        element.setGenericType(type);
        element.setWidth(300);
        element.setHeight(200);

        return element;
    }



    
}
