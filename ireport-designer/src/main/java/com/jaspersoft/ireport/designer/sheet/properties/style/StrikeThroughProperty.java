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
import org.openide.nodes.PropertySupport;

/**
 * Class to manage the JRDesignStyle.PROPERTY_STRIKE_THROUGH property
 */
public final class StrikeThroughProperty extends PropertySupport.ReadWrite {

    private final JRBaseStyle style;

    @SuppressWarnings(value = "unchecked")
    public StrikeThroughProperty(JRBaseStyle style) {
        super(JRBaseStyle.PROPERTY_STRIKE_THROUGH, Boolean.class, I18n.getString("AbstractStyleNode.Property.Strike_Through"), "");
        this.style = style;
    }

    @Override
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return style.isStrikeThrough();
    }

    @Override
    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (val == null || val instanceof Boolean) {
            Boolean oldValue = style.isOwnStrikeThrough();
            Boolean newValue = (Boolean) val;
            style.setStrikeThrough(newValue);
            ObjectPropertyUndoableEdit urob = new ObjectPropertyUndoableEdit(style, "StrikeThrough", Boolean.class, oldValue, newValue);
            IReportManager.getInstance().addUndoableEdit(urob);
        }
    }

    @Override
    public boolean isDefaultValue() {
        return style.isOwnStrikeThrough() == null;
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
