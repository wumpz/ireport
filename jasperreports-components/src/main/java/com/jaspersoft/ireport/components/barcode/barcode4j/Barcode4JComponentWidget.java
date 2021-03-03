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

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.beans.PropertyChangeEvent;
import net.sf.jasperreports.components.barcode4j.BarcodeComponent;
import net.sf.jasperreports.components.barcode4j.CodabarComponent;
import net.sf.jasperreports.components.barcode4j.Code39Component;
import net.sf.jasperreports.components.barcode4j.DataMatrixComponent;
import net.sf.jasperreports.components.barcode4j.FourStateBarcodeComponent;
import net.sf.jasperreports.components.barcode4j.PDF417Component;
import net.sf.jasperreports.components.barcode4j.POSTNETComponent;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sourceforge.barbecue.twod.pdf417.PDF417Barcode;

/**
 *
 * @author gtoffoli
 */
public class Barcode4JComponentWidget extends JRDesignElementWidget {

    public Barcode4JComponentWidget(AbstractReportObjectScene scene, JRDesignElement element) {
        super(scene, element);

        if (((JRDesignComponentElement)element).getComponent() instanceof BarcodeComponent)
        {
            BarcodeComponent c = (BarcodeComponent) ((JRDesignComponentElement)element).getComponent();
            c.getEventSupport().addPropertyChangeListener(this);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        
        if (evt.getPropertyName().equals(BarcodeComponent.PROPERTY_CODE_EXPRESSION) ||
            evt.getPropertyName().equals(BarcodeComponent.PROPERTY_MODULE_WIDTH) ||
            evt.getPropertyName().equals(BarcodeComponent.PROPERTY_ORIENTATION) ||
            evt.getPropertyName().equals(BarcodeComponent.PROPERTY_PATTERN_EXPRESSION) ||
            evt.getPropertyName().equals(BarcodeComponent.PROPERTY_TEXT_POSITION) ||
            evt.getPropertyName().equals(Code39Component.PROPERTY_CHECKSUM_MODE) ||
            evt.getPropertyName().equals(CodabarComponent.PROPERTY_WIDE_FACTOR) ||
            evt.getPropertyName().equals(DataMatrixComponent.PROPERTY_SHAPE) ||
            evt.getPropertyName().equals(FourStateBarcodeComponent.PROPERTY_ASCENDER_HEIGHT) ||
            evt.getPropertyName().equals(FourStateBarcodeComponent.PROPERTY_INTERCHAR_GAP_WIDTH) ||
            evt.getPropertyName().equals(FourStateBarcodeComponent.PROPERTY_TRACK_HEIGHT) ||
            evt.getPropertyName().equals(Code39Component.PROPERTY_EXTENDED_CHARSET_ENABLED) ||
            evt.getPropertyName().equals(Code39Component.PROPERTY_DISPLAY_CHECKSUM) ||
            evt.getPropertyName().equals(Code39Component.PROPERTY_DISPLAY_START_STOP) ||
            evt.getPropertyName().equals(POSTNETComponent.PROPERTY_BASELINE_POSITION) ||
            evt.getPropertyName().equals(POSTNETComponent.PROPERTY_SHORT_BAR_HEIGHT) ||
            evt.getPropertyName().equals(PDF417Component.PROPERTY_WIDTH_TO_HEIGHT_RATIO) ||
            evt.getPropertyName().equals(PDF417Component.PROPERTY_ERROR_CORRECTION_LEVEL) ||
            evt.getPropertyName().equals(PDF417Component.PROPERTY_MAX_COLUMNS) ||
            evt.getPropertyName().equals(PDF417Component.PROPERTY_MIN_COLUMNS) ||
            evt.getPropertyName().equals(PDF417Component.PROPERTY_QUIET_ZONE) ||
            evt.getPropertyName().equals(PDF417Component.PROPERTY_VERTICAL_QUIET_ZONE) ||
            evt.getPropertyName().equals(PDF417Component.PROPERTY_MAX_ROWS) ||
            evt.getPropertyName().equals(PDF417Component.PROPERTY_MIN_ROWS)
            )
        {
            updateBounds();
            this.repaint();
            this.revalidate(true);
            this.getSelectionWidget().updateBounds();
            this.getSelectionWidget().revalidate(true);
            getScene().validate();
        }
        
        super.propertyChange(evt);
    }

}
