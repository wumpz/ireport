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

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.beans.PropertyChangeEvent;
import net.sf.jasperreports.components.barbecue.StandardBarbecueComponent;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;

/**
 *
 * @author gtoffoli
 */
public class JRBarbecueComponentWidget extends JRDesignElementWidget {

    public JRBarbecueComponentWidget(AbstractReportObjectScene scene, JRDesignElement element) {
        super(scene, element);

        if (((JRDesignComponentElement)element).getComponent() instanceof StandardBarbecueComponent)
        {
            StandardBarbecueComponent c = (StandardBarbecueComponent) ((JRDesignComponentElement)element).getComponent();
            c.getEventSupport().addPropertyChangeListener(this);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals(StandardBarbecueComponent.PROPERTY_DRAW_TEXT) ||
            evt.getPropertyName().equals(StandardBarbecueComponent.PROPERTY_BAR_HEIGTH) ||
            evt.getPropertyName().equals(StandardBarbecueComponent.PROPERTY_BAR_WIDTH) ||
            evt.getPropertyName().equals(StandardBarbecueComponent.PROPERTY_CHECKSUM_REQUIRED) ||
            evt.getPropertyName().equals(StandardBarbecueComponent.PROPERTY_CODE_EXPRESSION) ||
            evt.getPropertyName().equals(StandardBarbecueComponent.PROPERTY_APPLICATION_IDENTIFIER_EXPRESSION) ||
            evt.getPropertyName().equals(StandardBarbecueComponent.PROPERTY_TYPE))
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
