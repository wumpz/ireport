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

import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.designer.sheet.properties.IntegerProperty;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JasperDesign;

    
/**
 *  Class to manage the JRDesignElement.PROPERTY_HEIGHT property
 */
public final class CellHeightProperty extends IntegerProperty
{
    private final DesignCell cell;
    private final StandardTable table;
    private final JasperDesign jd;


    @SuppressWarnings("unchecked")
    public CellHeightProperty(DesignCell cell, StandardTable table, JasperDesign jd)
    {
        super(cell);
        this.cell = cell;
        this.table = table;
        this.jd = jd;
    }

    @Override
    public String getName()
    {
        return DesignCell.PROPERTY_HEIGHT;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.Height");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.Hightdetail");
    }

    @Override
    public Integer getInteger()
    {
        return cell.getHeight();
    }

    @Override
    public Integer getOwnInteger()
    {
        return cell.getHeight();
    }

    @Override
    public Integer getDefaultInteger()
    {
        return 0;// FIXMEGT: we should get the max height for this row...
    }

    @Override
    public void setInteger(Integer height)
    {
        cell.setHeight(height);
        TableModelUtils.fixTableLayout(table, jd);
        cell.getEventSupport().firePropertyChange("ROW_HEIGHT", null, height);
    }

    @Override
    public void validateInteger(Integer height)
    {
        if (height < 0)
        {
            throw annotateException(I18n.getString("Global.Property.Heightexception"));
        }
    }

}
