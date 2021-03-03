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
package com.jaspersoft.ireport.components.barcode.barcode4j;

import com.jaspersoft.ireport.designer.sheet.properties.BooleanProperty;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.components.barcode4j.BarcodeComponent;
import net.sf.jasperreports.components.barcode4j.Code39Component;
import net.sf.jasperreports.components.barcode4j.Interleaved2Of5Component;
import net.sf.jasperreports.components.barcode4j.POSTNETComponent;


public final class Barcode4JDisplayChecksumProperty  extends BooleanProperty
{
        private BarcodeComponent component;

        @SuppressWarnings("unchecked")
        public Barcode4JDisplayChecksumProperty(BarcodeComponent component)
        {
           super(component);
           this.component = component;
           setName(Code39Component.PROPERTY_DISPLAY_CHECKSUM);
           setDisplayName(I18n.getString("barcode4j.property.displayChecksum.name"));
           setShortDescription(I18n.getString("barcode4j.property.displayChecksum.description"));
        }



    @Override
    public Boolean getBoolean() {
        return getComponentValue();
    }

    @Override
    public Boolean getOwnBoolean() {
        return getBoolean();
    }

    @Override
    public Boolean getDefaultBoolean() {
        return null;
    }

    @Override
    public void setBoolean(Boolean value) {
        setComponentValue(value);
    }

    private Boolean getComponentValue() {
        if (component instanceof Code39Component)
        {
            return ((Code39Component)component).isDisplayChecksum();
        }
        if (component instanceof Interleaved2Of5Component)
        {
            return ((Interleaved2Of5Component)component).isDisplayChecksum();
        }
        if (component instanceof POSTNETComponent)
        {
            return ((POSTNETComponent)component).getDisplayChecksum();
        }
        return null;
    }

    private void setComponentValue(Boolean value) {
        if (component instanceof Code39Component)
        {
            ((Code39Component)component).setDisplayChecksum(value);
        }
        if (component instanceof Interleaved2Of5Component)
        {
            ((Interleaved2Of5Component)component).setDisplayChecksum(value);
        }
        if (component instanceof POSTNETComponent)
        {
            ((POSTNETComponent)component).setDisplayChecksum(value);
        }
    }
}

