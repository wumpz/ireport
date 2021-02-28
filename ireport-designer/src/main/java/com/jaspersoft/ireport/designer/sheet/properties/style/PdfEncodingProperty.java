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
 * Class to manage the JRDesignStyle.PROPERTY_PDF_ENCODING property
 */
final public  class PdfEncodingProperty extends PropertySupport.ReadWrite {

    private final JRBaseStyle style;
    PropertyEditor editor = null;

    @SuppressWarnings(value = "unchecked")
    public PdfEncodingProperty(JRBaseStyle style) {
        super(JRBaseStyle.PROPERTY_PDF_ENCODING, String.class, I18n.getString("AbstractStyleNode.Property.Pdf_Encoding"), I18n.getString("AbstractStyleNode.Property.Pdf_Encoding"));
        this.style = style;

        setValue("canEditAsText", true);
        setValue("oneline", true);
        setValue("suppressCustomEditor", false);
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return style.getPdfEncoding();
    }

    @Override
    public String getHtmlDisplayName() {
        return "<html><s>" + getDisplayName();
    }

    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (val == null || val instanceof String) {
            if ((val + "").trim().length() == 0) {
                val = null;
            }
            String oldValue = style.getOwnPdfEncoding();
            String newValue = (String) val;

            style.setPdfEncoding(newValue);
            ObjectPropertyUndoableEdit urob = new ObjectPropertyUndoableEdit(style, "PdfEncoding", String.class, oldValue, newValue);
            IReportManager.getInstance().addUndoableEdit(urob);
        }
    }

    @Override
    public boolean isDefaultValue() {
        return style.getOwnPdfEncoding() == null;
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
            List encodings = new ArrayList();
            encodings.add(new Tag("Cp1250", "CP1250 (Central European)"));
            encodings.add(new Tag("Cp1251", "CP1251 (Cyrillic)"));
            encodings.add(new Tag("Cp1252", "CP1252 (Western European ANSI aka WinAnsi)"));
            encodings.add(new Tag("Cp1253", "CP1253 (Greek)"));
            encodings.add(new Tag("Cp1254", "CP1254 (Turkish)"));
            encodings.add(new Tag("Cp1255", "CP1255 (Hebrew)"));
            encodings.add(new Tag("Cp1256", "CP1256 (Arabic)"));
            encodings.add(new Tag("Cp1257", "CP1257 (Baltic)"));
            encodings.add(new Tag("Cp1258", "CP1258 (Vietnamese)"));
            encodings.add(new Tag("UniGB-UCS2-H", "UniGB-UCS2-H (Chinese Simplified)"));
            encodings.add(new Tag("UniGB-UCS2-V", "UniGB-UCS2-V (Chinese Simplified)"));
            encodings.add(new Tag("UniCNS-UCS2-H", "UniCNS-UCS2-H (Chinese traditional)"));
            encodings.add(new Tag("UniCNS-UCS2-V", "UniCNS-UCS2-V (Chinese traditional)"));
            encodings.add(new Tag("UniJIS-UCS2-H", "UniJIS-UCS2-H (Japanese)"));
            encodings.add(new Tag("UniJIS-UCS2-V", "UniJIS-UCS2-V (Japanese)"));
            encodings.add(new Tag("UniJIS-UCS2-HW-H", "UniJIS-UCS2-HW-H (Japanese)"));
            encodings.add(new Tag("UniJIS-UCS2-HW-V", "UniJIS-UCS2-HW-V (Japanese)"));
            encodings.add(new Tag("UniKS-UCS2-H", "UniKS-UCS2-H (Korean)"));
            encodings.add(new Tag("UniKS-UCS2-V", "UniKS-UCS2-V (Korean)"));
            encodings.add(new Tag("Identity-H", "Identity-H (Unicode with horizontal writing)"));
            encodings.add(new Tag("Identity-V", "Identity-V (Unicode with vertical writing)"));
            editor = new ComboBoxPropertyEditor(true, encodings);
        }
        return editor;
    }
}
