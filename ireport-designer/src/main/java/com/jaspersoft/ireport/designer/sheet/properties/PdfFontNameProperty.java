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

import com.jaspersoft.ireport.designer.IRFont;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ReportClassLoader;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.fonts.FontInfo;
import net.sf.jasperreports.engine.util.JRFontUtil;
import org.openide.nodes.PropertySupport;

/**
 * Class to manage the JRBaseStyle.PROPERTY_FONT_NAME property
 */
public class PdfFontNameProperty extends PropertySupport.ReadWrite 
{
    // FIXME: refactorize this
    private final JRFont font;
    PropertyEditor editor = null;


    @SuppressWarnings("unchecked")
    public PdfFontNameProperty(JRFont font)
    {
        super(JRBaseStyle.PROPERTY_PDF_FONT_NAME, String.class,
              I18n.getString("Global.Property.PdfFontname"),
              I18n.getString("Global.Property.PdfFontname"));
        this.font = font;

        
        setValue("canEditAsText",true);
        setValue("oneline",true);
        setValue("suppressCustomEditor",true);
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        if (this.isDefaultValue())
        {
            return null;
        }
        return font.getOwnPdfFontName();
    }

    @Override
    public String getHtmlDisplayName() {
        return "<html><s>" + getDisplayName();
    }


    @Override
    public boolean canWrite() {
        if (font.getFontName() == null) return true;
        String fontName = font.getFontName();

        // If the font name comes from a font extension, this
        // property should be disables...
        JRFontUtil.getFontFamilyNames();

        Collection extensionFonts = JRFontUtil.getFontFamilyNames();
        if (extensionFonts.contains(fontName))
        {
            FontInfo fontInfo = JRFontUtil.getFontInfo(fontName, null);
            if (fontInfo.getFontFamily() != null &&
                fontInfo.getFontFamily().getNormalPdfFont() != null)
            {
                System.out.println(fontInfo.getFontFamily().getNormalPdfFont());
                return false;
            }
        }
        return true;
    }


    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        if (val == null || val instanceof String)
        {
            String oldValue = font.getOwnPdfFontName();
            String newValue =   (String)val;

            font.setPdfFontName(newValue);

            ObjectPropertyUndoableEdit urob =
                    new ObjectPropertyUndoableEdit(
                        font,
                        "PdfFontName", 
                        String.class,
                        oldValue,newValue);
            // Find the undoRedo manager...
            IReportManager.getInstance().addUndoableEdit(urob);
        }
    }

    @Override
    public boolean isDefaultValue() {
        return font.getOwnPdfFontName() == null;
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
    @SuppressWarnings("unchecked")
    public PropertyEditor getPropertyEditor() {

        if (editor == null)
        {
            java.util.List classes = new ArrayList();

            // Add regular PDF fonts...
            classes.add(new Tag(null,"<Default>"));
            classes.add(new Tag("Helvetica"));
            classes.add(new Tag("Helvetica-Bold"));
            classes.add(new Tag("Helvetica-BoldOblique"));
            classes.add(new Tag("Helvetica-Oblique"));
            classes.add(new Tag("Courier"));
            classes.add(new Tag("Courier-Bold"));
            classes.add(new Tag("Courier-BoldOblique"));
            classes.add(new Tag("Courier-Oblique"));
            classes.add(new Tag("Symbol"));
            classes.add(new Tag("Times-Roman"));
            classes.add(new Tag("Times-Bold"));
            classes.add(new Tag("Times-BoldItalic"));
            classes.add(new Tag("Times-Italic"));
            classes.add(new Tag("ZapfDingbats"));
            classes.add(new Tag("STSong-Light"));
            classes.add(new Tag("MHei-Medium"));
            classes.add(new Tag("MSung-Light"));
            classes.add(new Tag("HeiseiKakuGo-W5"));
            classes.add(new Tag("HeiseiMin-W3"));
            classes.add(new Tag("HYGoThic-Medium"));
            classes.add(new Tag("HYSMyeongJo-Medium"));
//
//            // Add the fonts coming from the registry...
//            classes.add(new Tag(null, null));
//
//            ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
//            Thread.currentThread().setContextClassLoader(new ReportClassLoader(IReportManager.getReportClassLoader()));
//
//            Collection extensionFonts = JRFontUtil.getFontFamilyNames();
//
//
//            for(Iterator it = extensionFonts.iterator(); it.hasNext();)
//            {
//                String fname = (String)it.next();
//                classes.add(new Tag(fname));
//            }
//
//            classes.add(new Tag(null, null));

            List<IRFont> fonts = IReportManager.getInstance().getIRFonts();

            for (IRFont f : fonts)
            {
                classes.add(new Tag(f.getFile(), f.toString()));
            }



            editor = new ComboBoxPropertyEditor(true, classes);
        }
        return editor;
    }

}
