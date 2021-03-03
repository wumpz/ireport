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
package com.jaspersoft.ireport.components.list;

import com.jaspersoft.ireport.designer.sheet.properties.IntegerProperty;
import net.sf.jasperreports.components.list.DesignListContents;

/**
 *
 * @author gtoffoli
 */
class ListContentsHeightProperty extends IntegerProperty {

    private DesignListContents contents = null;
    public ListContentsHeightProperty(DesignListContents contents) {
        super(contents);
        this.contents = contents;
        setName("LC" + DesignListContents.PROPERTY_HEIGHT);
        setDisplayName("Item height");
    }

    @Override
    public Integer getInteger() {
        return new Integer(contents.getHeight());
    }

    @Override
    public Integer getOwnInteger() {
        return getInteger();
    }

    @Override
    public Integer getDefaultInteger() {
        return getInteger();
    }

    @Override
    public void validateInteger(Integer value) {
    }

    @Override
    public void setInteger(Integer value) {
        if (value != null)
        contents.setHeight(value.intValue());
    }

    @Override
    public boolean supportsDefaultValue() {
        return false;
    }


}
