/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2009 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.ireport.designer.sheet.properties;

import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.sheet.editors.GenericElementParametersPropertyEditor;
import com.jaspersoft.ireport.designer.sheet.editors.SubreportParametersPropertyEditor;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignGenericElement;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import org.openide.nodes.PropertySupport;

    
public final class GenericElementParametersProperty  extends PropertySupport {

    PropertyEditor editor = null;
    private final JRDesignDataset dataset;
    private final JRDesignGenericElement element;

    @SuppressWarnings("unchecked")
    public GenericElementParametersProperty(JRDesignGenericElement element, JRDesignDataset dataset)
    {
       super( JRDesignGenericElement.PROPERTY_PARAMETERS, List.class, I18n.getString("Global.Property.Parameters"),I18n.getString("Global.Property.GenericElementparameters"), true,true);

       setValue("canEditAsText", Boolean.FALSE);
       setValue("expressionContext", new ExpressionContext(dataset));
       //setValue("subreport", element);
       this.element = element;
       this.dataset = dataset;
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return element.getParamtersList();
    }

    @SuppressWarnings("unchecked")
    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (val == null || !(val instanceof List)) throw new IllegalArgumentException();

        // If val is the same as the old map, the user pressed cancel
        // in the editor.. so nothing to do...
        if (val == element.getParamtersList()) return;
        // Fill this map with the content of the map we got here...
        // TODO: manage UNDO for a map object...
        List parameters = (List)val;
        element.getParamtersList().clear();
        element.getParamtersList().addAll(parameters);
        element.getEventSupport().firePropertyChange( JRDesignGenericElement.PROPERTY_PARAMETERS , null, element.getParamtersList() );
    }

    @Override
    public PropertyEditor getPropertyEditor() {

        if (editor == null)
        {
            editor = new GenericElementParametersPropertyEditor();
        }
        return editor;
    }


}
