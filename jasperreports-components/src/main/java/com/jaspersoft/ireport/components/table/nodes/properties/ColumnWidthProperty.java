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

import com.jaspersoft.ireport.components.table.TableElementNode;
import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.designer.sheet.properties.IntegerProperty;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.NbBundle;

    
/**
 *  Class to manage the JRDesignElement.PROPERTY_HEIGHT property
 */
public final class ColumnWidthProperty extends IntegerProperty
{
    private final StandardBaseColumn column;
    private final StandardTable table;
    private final JasperDesign jd;

    @SuppressWarnings("unchecked")
    public ColumnWidthProperty(StandardBaseColumn column, StandardTable table, JasperDesign jd)
    {
        super(column);
        this.column = column;
        this.table = table;
        this.jd = jd;
    }

    @Override
    public String getName()
    {
        return StandardBaseColumn.PROPERTY_WIDTH;
    }

    @Override
    public String getDisplayName()
    {
        return NbBundle.getMessage(TableElementNode.class, "column.width.property");
    }

    @Override
    public String getShortDescription()
    {
        return NbBundle.getMessage(TableElementNode.class, "column.width.property.description");
    }

    @Override
    public Integer getInteger()
    {
        return getOwnInteger();
    }

    @Override
    public Integer getOwnInteger()
    {
        return column.getWidth();
    }

    @Override
    public Integer getDefaultInteger()
    {
        return 0;// FIXMEGT: we should get the max height for this row...
    }

    @Override
    public void setInteger(Integer height)
    {
        column.setWidth(height); // FIXMEGT: we should update the with of the involved columns...
        TableModelUtils.fixTableLayout(table, jd);
        column.getEventSupport().firePropertyChange("COLUMN_WIDTH", null, height);
    }

    @Override
    public void validateInteger(Integer height)
    {
        if (height < 0)
        {
            throw annotateException(NbBundle.getMessage(TableElementNode.class, "column.width.validationError"));
        }
    }

    /**
     * @return the column
     */
    public StandardBaseColumn getColumn() {
        return column;
    }

}
