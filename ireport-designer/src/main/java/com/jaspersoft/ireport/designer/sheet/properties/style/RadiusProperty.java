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
package com.jaspersoft.ireport.designer.sheet.properties.style;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;

/**
 * Class to manage the JRDesignElement.PROPERTY_X property
 */
public final class RadiusProperty extends PropertySupport {

    private final JRBaseStyle style;

    @SuppressWarnings(value = "unchecked")
    public RadiusProperty(JRBaseStyle style) {
        super(JRBaseStyle.PROPERTY_RADIUS, Integer.class, I18n.getString("RadiusPropertyRadius.Property.Radius"), I18n.getString("RadiusPropertyRadius.Property.Radiusdetail"), true, true);
        this.style = style;
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return style.getRadius();
    }

    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (val == null || val instanceof Integer) {
            Integer oldValue = style.getOwnRadius();
            Integer newValue = (Integer) val;
            if (newValue != null && newValue < 0) {
                IllegalArgumentException iae = annotateException(I18n.getString("RadiusPropertyRadius.Property.Message"));
                throw iae;
            }

            style.setRadius(newValue);
            ObjectPropertyUndoableEdit urob = new ObjectPropertyUndoableEdit(style, "Radius", Integer.TYPE, oldValue, newValue);
            IReportManager.getInstance().addUndoableEdit(urob);
        }
    }

    public IllegalArgumentException annotateException(String msg) {
        IllegalArgumentException iae = new IllegalArgumentException(msg);
        ErrorManager.getDefault().annotate(iae, ErrorManager.EXCEPTION, msg, msg, null, null);
        return iae;
    }

    @Override
    public boolean isDefaultValue() {
        return style.getOwnRadius() == null;
    }

    @Override
    public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
        setValue(null);
    }

    @Override
    public boolean supportsDefaultValue() {
        return true;
    }
}
