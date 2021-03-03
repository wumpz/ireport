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
package com.jaspersoft.ireport.components.table.nodes;

import java.util.List;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class TableColumnGroupChildren extends AbstractTableChildren {

    private StandardColumnGroup columnGroup = null;
        
    @SuppressWarnings("unchecked")
    public TableColumnGroupChildren(JasperDesign jd, JRDesignComponentElement componentElement, StandardColumnGroup columnGroup, byte section, JRDesignGroup group, Lookup doLkp) {
        super(jd,componentElement,section,group,doLkp);
        this.columnGroup = columnGroup;
        columnGroup.getEventSupport().addPropertyChangeListener(this);
    }


    
    @SuppressWarnings("unchecked")
    public void recalculateKeysImpl() {
        
        List l = (List)lock();

        for (int i=0; i<l.size(); ++i)
        {
            StandardBaseColumn col = (StandardBaseColumn)l.get(i);
            col.getEventSupport().removePropertyChangeListener(getCellPropertyName(), this);
        }

        l.clear();

        List<BaseColumn> columns = columnGroup.getColumns();
        l.addAll(columns);

        for (int i=0; i<l.size(); ++i)
        {
            StandardBaseColumn col = (StandardBaseColumn)l.get(i);
            col.getEventSupport().addPropertyChangeListener(getCellPropertyName(), this);
        }
        
        // update(); Performed by the abstract superclass
    }
    
}
