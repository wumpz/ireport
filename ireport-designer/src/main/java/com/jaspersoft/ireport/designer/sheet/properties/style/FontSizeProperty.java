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
import java.util.List;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import org.openide.nodes.PropertySupport;

/**
 * Class to manage the JRDesignStyle.PROPERTY_FONT_SIZE property
 */
final public class FontSizeProperty extends PropertySupport.ReadWrite {

    private final JRBaseStyle style;
    PropertyEditor editor = null;

    @SuppressWarnings(value = "unchecked")
    public FontSizeProperty(JRBaseStyle style) {
        super(JRBaseStyle.PROPERTY_FONT_SIZE, Integer.class, I18n.getString("AbstractStyleNode.Property.Size"), I18n.getString("AbstractStyleNode.Property.Size"));
        this.style = style;

        setValue("canEditAsText", true);
        setValue("oneline", true);
        setValue("suppressCustomEditor", false);
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return style.getFontsize();
    }

    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (val != null && !(val instanceof Integer)) {
            try {
                val = new Integer(val + "");
            } catch (Exception ex) {
                // no way...
                return;
            }
        }
        if (val == null || val instanceof Integer) {
            Integer oldValue = style.getOwnFontsize().intValue();
            Integer newValue = (Integer) val;
            style.setFontSize(newValue.floatValue());
            ObjectPropertyUndoableEdit urob = new ObjectPropertyUndoableEdit(style, "FontSize", Integer.class, oldValue, newValue);
            IReportManager.getInstance().addUndoableEdit(urob);
        }
    }

    @Override
    public boolean isDefaultValue() {
        return style.getOwnFontsize() == null;
    }

    @Override
    public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
        setValue(null);
    }

    @Override
    public boolean supportsDefaultValue() {
        return true;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public PropertyEditor getPropertyEditor() {
        if (editor == null) {
            List classes = new ArrayList();
            for (int i = 6; i < 100;) {
                classes.add(new Tag(new Integer(i), "" + i));

                if (i < 16) {
                    i++;
                } else if (i < 32) {
                    i += 2;
                } else if (i < 48) {
                    i += 4;
                } else if (i < 72) {
                    i += 6;
                } else {
                    i += 8;
                }
            }
            editor = new ComboBoxPropertyEditor(true, classes);
        }
        return editor;
    }
}
