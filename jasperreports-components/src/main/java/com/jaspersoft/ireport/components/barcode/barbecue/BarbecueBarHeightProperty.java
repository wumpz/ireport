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
package com.jaspersoft.ireport.components.barcode.barbecue;

import com.jaspersoft.ireport.designer.sheet.properties.IntegerProperty;
import net.sf.jasperreports.components.barbecue.StandardBarbecueComponent;

/**
 *
 * @author gtoffoli
 */
class BarbecueBarHeightProperty extends IntegerProperty {

    private StandardBarbecueComponent component = null;
    public BarbecueBarHeightProperty(StandardBarbecueComponent component) {
        super(component);
        this.component = component;
        setName(StandardBarbecueComponent.PROPERTY_BAR_HEIGTH);
        setDisplayName("Bar Height");
    }

    @Override
    public Integer getInteger() {
        return (component.getBarHeight() == null) ? new Integer(0) : component.getBarHeight();
    }

    @Override
    public Integer getOwnInteger() {
        return component.getBarHeight();
    }

    @Override
    public Integer getDefaultInteger() {
        return new Integer(0);
    }

    @Override
    public void validateInteger(Integer value) {
        if (value != null && value.intValue() < 0)
        {
            throw new IllegalArgumentException("Bar height must be a positive number");
        }
    }

    @Override
    public void setInteger(Integer value) {
        if (value != null && value.intValue() <= 0) value = new Integer(0);
        component.setBarHeight(value);
    }

    @Override
    public boolean supportsDefaultValue() {
        return true;
    }


}
