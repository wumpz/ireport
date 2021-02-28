/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2013 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.ireport.designer;

import com.jaspersoft.ireport.locale.I18n;

/**
 *
 * @author gtoffoli
 */
public class IRFont {
    
    private java.awt.Font font;
    
    private java.lang.String file;
    
    /** Creates a new instance of IRFont */
    public IRFont() {
    }
    
     public IRFont(java.awt.Font font, java.lang.String file) {
         this.font = font;
         this.file = file;
    }
    
    /** Getter for property file.
     * @return Value of property file.
     *
     */
    public java.lang.String getFile() {
        return file;
    }
    
    /** Setter for property file.
     * @param file New value of property file.
     *
     */
    public void setFile(java.lang.String file) {
        this.file = file;
    }
    
    /** Getter for property font.
     * @return Value of property font.
     *
     */
    public java.awt.Font getFont() {
        return font;
    }
    
    /** Setter for property font.
     * @param font New value of property font.
     *
     */
    public void setFont(java.awt.Font font) {
        this.font = font;
    }
    
    
    @Override
    public String toString()
    {
        if (font == null || file == null) return I18n.getString("IRFont.Message.NotInitializedFont");
        return font.getFontName()+" ("+file+")";
    }    
}
