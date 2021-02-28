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
package com.jaspersoft.ireport.designer.sheet;

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.sheet.editors.box.JRLineBoxPropertyEditor;
import com.jaspersoft.ireport.designer.sheet.properties.AbstractProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;

/**
 *
 * @author gtoffoli
 */
public class JRLineBoxProperty  extends AbstractProperty {

    PropertyEditor editor = null;
    JRBoxContainer container = null;
    
    @SuppressWarnings("unchecked")
    public JRLineBoxProperty(JRBoxContainer container)
    {
       super(JRLineBox.class, container.getLineBox());
       setName("linebox");
       setDisplayName(I18n.getString("JRLineBoxProperty.Paddingandborders"));
       setShortDescription(I18n.getString("JRLineBoxProperty.Paddingandborders"));
       setValue("canEditAsText", Boolean.FALSE);
       this.container = container;
    }

    
    @Override
    public PropertyEditor getPropertyEditor() {
        
        if (editor == null)
        {
            editor = new JRLineBoxPropertyEditor();
        }
        return editor;
    }

    @Override
    public Object getPropertyValue() {
        return container.getLineBox().clone(container);
    }

    @Override
    public Object getOwnPropertyValue() {
        return container.getLineBox().clone(container);
    }

    @Override
    public Object getDefaultValue() {
        return new JRBaseLineBox(container);
    }

    @Override
    public void validate(Object value) {
        
    }

    @Override
    public void setPropertyValue(Object value) {
        if (value != null && value instanceof JRLineBox)
        {
            ModelUtils.applyBoxProperties(container.getLineBox(), (JRLineBox)value);
            // Check if we are able to fire an event....

            try {
                Method m = container.getClass().getMethod("getEventSupport", new Class[]{});
                if (m != null)
                {
                    JRPropertyChangeSupport support = (JRPropertyChangeSupport) m.invoke(container, new Object[]{});
                    support.firePropertyChange("linebox", null, value);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
    
    
}

