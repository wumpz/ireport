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

import com.jaspersoft.ireport.components.table.TableCell;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class TableSectionChildren extends AbstractTableChildren {

   
    private StandardTable table = null;

    
    @SuppressWarnings("unchecked")
    public TableSectionChildren(JasperDesign jd, JRDesignComponentElement componentElement, byte section, JRDesignGroup group, Lookup doLkp) {
        super(jd,componentElement,section,group,doLkp);
        this.table = (StandardTable)componentElement.getComponent();
        table.getEventSupport().addPropertyChangeListener(this);
    }



    @SuppressWarnings("unchecked")
    public void recalculateKeysImpl() {
        
        List l = (List)lock();

        if (getSection() == TableCell.DETAIL)
        {
            List<StandardBaseColumn> columns = getAllColumns();
            for (StandardBaseColumn col : columns)
            {
                col.getEventSupport().removePropertyChangeListener(getCellPropertyName(), this);
                col.getEventSupport().addPropertyChangeListener(getCellPropertyName(), this);
                col.getEventSupport().removePropertyChangeListener(StandardColumnGroup.PROPERTY_COLUMNS, this);
                col.getEventSupport().addPropertyChangeListener(StandardColumnGroup.PROPERTY_COLUMNS, this);
            }
            l.clear();
            l.addAll(getStandardColumns());
        }
        else
        {

            for (int i=0; i<l.size(); ++i)
            {
                StandardBaseColumn col = (StandardBaseColumn)l.get(i);
                col.getEventSupport().removePropertyChangeListener(getCellPropertyName(), this);
            }

            l.clear();
            l.addAll(table.getColumns());

            for (int i=0; i<l.size(); ++i)
            {
                StandardBaseColumn col = (StandardBaseColumn)l.get(i);
                col.getEventSupport().addPropertyChangeListener(getCellPropertyName(), this);
            }
        }

        // update(); performed by the super class
    }


    public List<StandardColumn> getStandardColumns() {

        List<StandardColumn> standardColumns = new ArrayList<StandardColumn>();
        List<BaseColumn> columns = table.getColumns();
        for (BaseColumn c : columns)
        {
            standardColumns.addAll(getStandardColumns(c));
        }
        return standardColumns;
    }

    /**
     * REturns the position of each line.
     * startingWidth is the starting point from left.
     * @param col
     * @return
     */
    private List<StandardColumn> getStandardColumns(BaseColumn col) {

        List<StandardColumn> standardColumns = new ArrayList<StandardColumn>();

        if (col instanceof StandardColumn)
        {
            standardColumns.add((StandardColumn) col);
        }
        else if (col instanceof StandardColumnGroup)
        {
            StandardColumnGroup columnGroup = (StandardColumnGroup)col;
            List<BaseColumn> columns = columnGroup.getColumns();
            for (BaseColumn c : columns)
            {
                standardColumns.addAll(getStandardColumns(c));
            }
        }
        return standardColumns;
    }


    public List<StandardBaseColumn> getAllColumns() {

        List<StandardBaseColumn> allColumns = new ArrayList<StandardBaseColumn>();
        List<BaseColumn> columns = table.getColumns();
        for (BaseColumn c : columns)
        {
            allColumns.addAll(getAllColumns((StandardBaseColumn) c));
        }
        return allColumns;
    }

    /**
     * REturns the position of each line.
     * startingWidth is the starting point from left.
     * @param col
     * @return
     */
    private List<StandardBaseColumn> getAllColumns(StandardBaseColumn col) {

        List<StandardBaseColumn> allColumns = new ArrayList<StandardBaseColumn>();

        allColumns.add(col);
        if (col instanceof StandardColumnGroup)
        {
            StandardColumnGroup columnGroup = (StandardColumnGroup)col;
            List<BaseColumn> columns = columnGroup.getColumns();
            for (BaseColumn c : columns)
            {
                allColumns.addAll(getAllColumns((StandardBaseColumn)c));
            }
        }
        return allColumns;
    }


    

}
