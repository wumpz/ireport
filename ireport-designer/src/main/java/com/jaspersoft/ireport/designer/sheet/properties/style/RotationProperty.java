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
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.type.RotationEnum;
import org.openide.nodes.PropertySupport;

/**
 * Class to manage the JRDesignElement.PROPERTY_POSITION_TYPE property
 */
final public class RotationProperty extends PropertySupport {

    private final JRBaseStyle style;
    private ComboBoxPropertyEditor editor;

    @SuppressWarnings(value = "unchecked")
    public RotationProperty(JRBaseStyle style) {
        super(JRBaseStyle.PROPERTY_ROTATION, RotationEnum.class, I18n.getString("AbstractStyleNode.Property.Rotation"), I18n.getString("AbstractStyleNode.Property.RotationDetail"), true, true);
        this.style = style;
        setValue("suppressCustomEditor", Boolean.TRUE);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public PropertyEditor getPropertyEditor() {
        if (editor == null) {
            ArrayList l = new ArrayList();
            l.add(new Tag(null, "<Default>"));
            l.add(new Tag(RotationEnum.NONE, I18n.getString("AbstractStyleNode.Property.None")));
            l.add(new Tag(RotationEnum.LEFT, I18n.getString("AbstractStyleNode.Property.Left")));
            l.add(new Tag(RotationEnum.RIGHT, I18n.getString("AbstractStyleNode.Property.Right")));
            l.add(new Tag(RotationEnum.UPSIDE_DOWN, I18n.getString("AbstractStyleNode.Property.Upside_Down")));
            editor = new ComboBoxPropertyEditor(false, l);
        }
        return editor;
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return style.getRotationValue();
    }

    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        setPropertyValue(val);
    }

    private void setPropertyValue(Object val) {
        if (val == null || val instanceof RotationEnum) {
            RotationEnum oldValue = style.getOwnRotationValue();
            RotationEnum newValue = (RotationEnum) val;
            style.setRotation(newValue);
            ObjectPropertyUndoableEdit urob = new ObjectPropertyUndoableEdit(style, "Rotation", RotationEnum.class, oldValue, newValue);
            IReportManager.getInstance().addUndoableEdit(urob);
        }
    }

    @Override
    public boolean isDefaultValue() {
        return style.getOwnRotationValue() == null;
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
