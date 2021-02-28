/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2005 - 2009 CINCOM SYSTEMS, INC. All rights reserved.
 * http://www.cincom.com
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

/*
 * Licensed to Jaspersoft Corporation under a Contributer Agreement
 */
package com.jaspersoft.ireport.designer.data.fieldsproviders;

import net.sf.jasperreports.engine.design.JRDesignField;

/**
 * @author MPenningroth
 */
public class XmlaFieldNode extends JRDesignField 
{
    private int axisNumber;
    
    /**
     * Creates a new instance of XmlaFieldNode 
     * assings the values for Node name and the Axis number.
     */
    public XmlaFieldNode(String name, int axisNumber) {
        super();
        setName(name);
        setValueClassName("java.lang.String");
        this.axisNumber = axisNumber;
    }
    /**
     * Returns axisNumber.
     */
    public int getAxisNumber() {
        return axisNumber;
    }
    
}
