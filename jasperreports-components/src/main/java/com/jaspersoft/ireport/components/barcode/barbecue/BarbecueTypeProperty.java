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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import net.sf.jasperreports.components.barbecue.BarcodeProviders;
import net.sf.jasperreports.components.barbecue.StandardBarbecueComponent;
import org.openide.nodes.PropertySupport;


public final class BarbecueTypeProperty  extends PropertySupport
{
        private StandardBarbecueComponent component;
        private ComboBoxPropertyEditor editor;

        private static String[] providerNames = new String[]{
                                "2of7",
                                "3of9",
                                "Bookland",
                                "Codabar",
                                "Code128",
                                "Code128A",
                                "Code128B",
                                "Code128C",
                                "Code39",
                                "Code39 (Extended)",
                                "EAN128",
                                "EAN13",
                                "GlobalTradeItemNumber",
                                "Int2of5",
                                "Monarch",
                                "NW7",
                                "PDF417",
                                "PostNet",
                                "RandomWeightUPCA",
                                "SCC14ShippingCode",
                                "ShipmentIdentificationNumber",
                                "SSCC18",
                                "Std2of5",
                                "UCC128",
                                "UPCA",
                                "USD3",
                                "USD4",
                                "USPS"};

        @SuppressWarnings("unchecked")
        public BarbecueTypeProperty(StandardBarbecueComponent component)
        {
            // TODO: Replace WhenNoDataType with the right constant
            super( StandardBarbecueComponent.PROPERTY_TYPE,String.class,
                    I18n.getString("barbecue.property.type.name"),
                    I18n.getString("barbecue.property.type.description"), true, true);
            this.component = component;
            setValue("suppressCustomEditor", Boolean.TRUE);
        }

        @Override
        @SuppressWarnings("unchecked")
        public PropertyEditor getPropertyEditor() {

            if (editor == null)
            {

                editor = new ComboBoxPropertyEditor(false, getListOfTags());
            }
            return editor;
        }

        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return component.getType();
        }

        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (val != null && val instanceof String)
            {
                String oldValue = component.getType();
                String newValue = (String)val;
                component.setType(newValue);

                ObjectPropertyUndoableEdit urob =
                        new ObjectPropertyUndoableEdit(
                            component,
                            "Type",
                            String.class,
                            oldValue,newValue);
                // Find the undoRedo manager...
                IReportManager.getInstance().addUndoableEdit(urob);
            }
        }

    private java.util.ArrayList getListOfTags()
    {
//        BarcodeProviders bp = BarcodeProviders.
//        Map providers = bp.getProviders();
//
//        Set keys = providers.keySet();
//        String[] keys_a = new String[keys.size()];
//        keys_a = (String[])keys.toArray(keys_a);
        Arrays.sort(providerNames);

        ArrayList tags = new java.util.ArrayList();

        for (int i=0; i<providerNames.length; ++i)
        {
            tags.add(new Tag(providerNames[i], providerNames[i]));
        }
        return tags;
    }
}

