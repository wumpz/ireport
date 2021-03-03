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
package com.jaspersoft.ireport.components.sort.properties;

import com.jaspersoft.ireport.designer.sheet.properties.AbstractProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import net.sf.jasperreports.components.sort.SortComponent;

import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;

    
/**
 *  Class to manage the JRDesignImage.PROPERTY_EVALUATION_TIME property
 */
public final class ColumnProperty extends AbstractProperty
{
        private final JRDesignDataset dataset;
        private final SortComponent component;
        private SortFieldPropertyEditor editor = null;

        @SuppressWarnings("unchecked")
        public ColumnProperty(SortComponent component, JRDesignDataset dataset)
        {
            super(String.class, component);
            this.component = component;
            this.dataset = dataset;

            setValue("dataset", dataset);
            setValue("suppressCustomEditor", Boolean.FALSE);
            setValue("canEditAsText", false);


         }

        @Override
    public String getName()
    {
        return SortComponent.PROPERTY_COLUMN_NAME;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.Column");
    }


    @SuppressWarnings("unchecked")
    public PropertyEditor getPropertyEditor() {

        if (editor == null)
        {
            editor = new SortFieldPropertyEditor();
        }
        return editor;
    }

    @Override
    public Object getPropertyValue() {

        String field = component.getSortFieldName();

        if (field == null || field.length() == 0) return null;
        if (component.getSortFieldType() != null && component.getSortFieldType() == SortFieldTypeEnum.VARIABLE ) return "V"+field;
        return "F"+field;

    }

    @Override
    public Object getOwnPropertyValue() {
        return getPropertyValue();
    }

    @Override
    public Object getDefaultValue() {
        return null;
    }

    @Override
    public void validate(Object value) {
        
    }

    @Override
    public void setPropertyValue(Object value) {

        if (value == null)
        {
            component.setSortFieldName(null);
            component.setSortFieldType(null);
        }
        else
        {
            if (value instanceof String && ((String)value).startsWith("F"))
            {
                component.setSortFieldName(((String)value).substring(1));
                component.setSortFieldType(SortFieldTypeEnum.FIELD);
            }
            else if (value instanceof String && ((String)value).startsWith("V"))
            {
                component.setSortFieldName(((String)value).substring(1));
                component.setSortFieldType(SortFieldTypeEnum.VARIABLE);
            }
            else
            {
                component.setSortFieldName(null);
                component.setSortFieldType(null);
            }
        }
    }

   
}
