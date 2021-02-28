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

package com.jaspersoft.ireport.designer.fonts;

import net.sf.jasperreports.engine.fonts.SimpleFontFamily;

/**
 * This is an extended version of the SimpleFontFamily calss of JasperReports
 * used to edit the font family properties for fonts defined in font/fonts.xml
 *
 *
 * @version $Id: SimpleFontFamilyEx.java 0 2009-10-26 11:12:37 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class SimpleFontFamilyEx extends SimpleFontFamily {
    private String normalFont = null;
    private String boldFont = null;
    private String italicFont = null;
    private String boldItalicFont = null;

    /**
     * @return the normalFont
     */
    public String getNormalFont() {
        return normalFont;
    }

    /**
     * @param normalFont the normalFont to set
     */
    public void setNormalFont(String normalFont) {
        this.normalFont = normalFont;
    }

    /**
     * @return the boldFont
     */
    public String getBoldFont() {
        return boldFont;
    }

    /**
     * @param boldFont the boldFont to set
     */
    public void setBoldFont(String boldFont) {
        this.boldFont = boldFont;
    }

    /**
     * @return the italicFont
     */
    public String getItalicFont() {
        return italicFont;
    }

    /**
     * @param italicFont the italicFont to set
     */
    public void setItalicFont(String italicFont) {
        this.italicFont = italicFont;
    }

    /**
     * @return the italicBoldFont
     */
    public String getBoldItalicFont() {
        return boldItalicFont;
    }

    /**
     * @param boldItalicFont the boldItalicFont to set
     */
    public void setBoldItalicFont(String boldItalicFont) {
        this.boldItalicFont = boldItalicFont;
    }



}
