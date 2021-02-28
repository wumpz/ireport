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
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.type.ModeEnum;
import org.openide.nodes.PropertySupport;

/**
 * SHEET PROPERTIES DEFINITIONS
 */
public final class ModeProperty extends PropertySupport.ReadWrite {

    JRBaseStyle style = null;

    @Override
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return new Boolean(style.getModeValue() != null && style.getModeValue() == ModeEnum.OPAQUE);
    }

    @Override
    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (val == null || val instanceof Boolean) {
            ModeEnum oldValue = style.getOwnModeValue();
            ModeEnum newValue = val == null ? null : (((Boolean) val).booleanValue() ? ModeEnum.OPAQUE : ModeEnum.TRANSPARENT);
            style.setMode(newValue);
            ObjectPropertyUndoableEdit urob = new ObjectPropertyUndoableEdit(style, "Mode", ModeEnum.class, oldValue, newValue);
            IReportManager.getInstance().addUndoableEdit(urob);
        }
    }

    @Override
    public boolean isDefaultValue() {
        return style.getOwnModeValue() == null;
    }

    @Override
    public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
        setValue(null);
    }

    @Override
    public boolean supportsDefaultValue() {
        return true;
    }

    @SuppressWarnings(value = "unchecked")
    public ModeProperty(JRBaseStyle style) {
        super(JRBaseStyle.PROPERTY_MODE, Boolean.class, I18n.getString("AbstractStyleNode.Property.Opaque"), I18n.getString("AbstractStyleNode.Property.Set"));
        this.style = style;
    }
}
