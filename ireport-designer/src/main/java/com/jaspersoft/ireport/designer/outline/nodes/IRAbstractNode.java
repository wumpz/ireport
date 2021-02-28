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

import com.jaspersoft.ireport.designer.pdf508.Pdf508TagDecorator;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;


/**
 *
 * @author gtoffoli
 */
public class IRAbstractNode extends AbstractNode  {

    /**
     * This lookup should contain only cookies like SaveCookie, PrintCookie, etc... 
     * 
     */
    private Lookup specialDataObjectLookup = null;

    /**
     * 
     * @param children
     * @param lkp  Lookup to be used with the node
     */
    public IRAbstractNode(Children children, Lookup lkp)
    {
        super(children, lkp);
        this.specialDataObjectLookup = lkp;
    }
       
    public Lookup getSpecialDataObjectLookup() {
        return specialDataObjectLookup;
    }

    public void setSpecialDataObjectLookup(Lookup specialDataObjectLookup) {
        this.specialDataObjectLookup = specialDataObjectLookup;
    }

}
