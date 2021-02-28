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
import com.jaspersoft.ireport.designer.actions.AddCellAction;
import javax.swing.Action;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class NullCellNode extends IRAbstractNode {

    JasperDesign jd = null;
    private NullCell cell = null;
    private JRDesignCrosstab crosstab = null;
    public NullCellNode(JasperDesign jd, NullCell cell, JRDesignCrosstab crosstab, Lookup doLkp)
    {
        super (Children.LEAF, new ProxyLookup( doLkp, Lookups.fixed(jd, cell, crosstab)));
        this.jd = jd;
        this.cell = cell;
        this.crosstab = crosstab;
        
        setDisplayName ( ModelUtils.nameOf(cell.getOrigin()));
        
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/cell-16.png");
    }
    
    @Override
    public String getHtmlDisplayName()
    {
        return "<font color=#999999>" + getDisplayName();
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        return sheet;
    }
    
    @Override
    public Action[] getActions(boolean popup) {
        return new Action[] { SystemAction.get(AddCellAction.class)};
    }

    public JRDesignCrosstab getCrosstab() {
        return crosstab;
    }

    public NullCell getCell() {
        return cell;
    }
}
