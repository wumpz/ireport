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
package com.jaspersoft.ireport.components.table.nodes.properties;

import com.jaspersoft.ireport.components.table.TableCell;
import com.jaspersoft.ireport.components.table.TableElementNode;
import com.jaspersoft.ireport.components.table.TableMatrix;
import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.sheet.properties.IntegerProperty;
import com.jaspersoft.ireport.designer.undo.PropertyUndoableEdit;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.NbBundle;

    
/**
 *  Class to manage the JRDesignElement.PROPERTY_HEIGHT property
 */
public final class RowSpanProperty extends IntegerProperty
{
    private final DesignCell cell;
    private final StandardTable table;
    private final JasperDesign jd;

    @SuppressWarnings("unchecked")
    public RowSpanProperty(DesignCell cell, StandardTable table, JasperDesign jd)
    {
        super(cell);
        this.cell = cell;
        this.table = table;
        this.jd = jd;
    }

    @Override
    public String getName()
    {
        return DesignCell.PROPERTY_ROW_SPAN;
    }

    @Override
    public String getDisplayName()
    {
        return NbBundle.getMessage(TableElementNode.class, "cell.rowSpan.property");
    }

    @Override
    public String getShortDescription()
    {
        return NbBundle.getMessage(TableElementNode.class, "cell.rowSpan.property.description");
    }

    @Override
    public Integer getInteger()
    {
        return getOwnInteger();
    }

    @Override
    public Integer getOwnInteger()
    {
        return (cell.getRowSpan() == null) ? getDefaultInteger() : cell.getRowSpan();
    }

    @Override
    public Integer getDefaultInteger()
    {
        return 1;
    }

    @Override
    public void setInteger(Integer span)
    {
        // The row span can be set ONLY for headers of StandardColumnGroups...
        TableMatrix matrix = TableModelUtils.createTableMatrix(table, jd);

        TableCell tableCell = matrix.getTableCell(cell);

        int oldSpan = tableCell.getRowSpan(); // get the real row span..

        int changedHLine = -1;
        List<Integer> hlines = new ArrayList<Integer>( matrix.getHorizontalSeparators() );

        if (span != oldSpan)
        {
            // Force the correct height of this cell to be then correctly fixed..
            // Adjust the height of children....
            cell.setHeight(matrix.getHorizontalSeparators().get( tableCell.getRow()+span) - matrix.getHorizontalSeparators().get( tableCell.getRow()));

        }
        // We need to add the undo operation checking the changed rows...
        cell.setRowSpan(span);

        TableModelUtils.fixTableLayout(table, jd);

        matrix.processMatrix();

        for (int i=0; i<hlines.size(); ++i)
        {
            if (hlines.get(i).intValue() != matrix.getHorizontalSeparators().get(i).intValue())
            {
                
                // Restore the cell height having this bottom line...
                int delta = matrix.getHorizontalSeparators().get(i).intValue() - hlines.get(i).intValue();

                System.out.println("Fixing cells on the changed line: " + i + "Delta: " + delta);
                System.out.flush();
                
                // set the new cell hight for all the cells in this row...
                List<TableCell> cells = matrix.getCells();

                for (TableCell cell : cells)
                {
                    if (cell.getRow()+cell.getRowSpan() == i &&  cell.getCell() != null)
                    {
                        cell.getCell().setHeight(cell.getCell().getHeight() - delta);
                    }
                }
                break;
            }
        }

        // Restore cell heights where changed...
        cell.getEventSupport().firePropertyChange("ROW_HEIGHT", null, span);
    }

    /**
     * Overridden because the undo operation is create
     * @param newValue
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Override
    public void setValue(Object newValue) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        Object oldValue = getOwnPropertyValue();

        validate(newValue);

        // Save the height of cells below this

        setPropertyValue(newValue);

        
        PropertyUndoableEdit undo =
            new PropertyUndoableEdit(
                this,
                oldValue,
                newValue
                );
        IReportManager.getInstance().addUndoableEdit(undo);
        
    }

    @Override
    public void validateInteger(Integer span)
    {
        if (span < 1)
        {
            throw annotateException(NbBundle.getMessage(TableElementNode.class, "cell.rowSpan.validationError"));
        }

        TableMatrix matrix = TableModelUtils.createTableMatrix(table, jd);

        TableCell tableCell = matrix.getTableCell(cell);

        int maxSpan = matrix.getMaxRowSpan(cell);
        if (span > maxSpan)
        {
            throw annotateException(NbBundle.getMessage(TableElementNode.class, "cell.rowSpan.maxSpan", maxSpan));
        }
    }

}
