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
package com.jaspersoft.ireport.designer.undo;

import com.jaspersoft.ireport.locale.I18n;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class AddStyleUndoableEdit extends AggregatedUndoableEdit {

    private JRDesignStyle style = null;
    private JasperDesign jasperDesign = null;
    
    private int index = -1;
    
    public AddStyleUndoableEdit(JRDesignStyle style, JasperDesign jasperDesign)
    {
        this.style = style;
        this.jasperDesign = jasperDesign;
    }
    
    @Override
    public void undo() throws CannotUndoException {
        
        super.undo();
        index = jasperDesign.getStylesList().indexOf(getStyle());
        jasperDesign.removeStyle(getStyle());
        
    }

    @Override
    public void redo() throws CannotRedoException {
        
        super.redo();
        
        // add the dataset again at the correct index...
        if (index > -1)
        {
            jasperDesign.getStylesList().add(index,getStyle());
        }
        else
        {
            jasperDesign.getStylesList().add(getStyle());
        }
        jasperDesign.getEventSupport().firePropertyChange( JasperDesign.PROPERTY_STYLES, null, null);
    }
    
    @Override
    public String getPresentationName() {
        return I18n.getString("AddStyleUndoableEdit.Name.AddStyle") + getStyle().getName();
    }

    public JasperDesign getJasperDesign() {
        return jasperDesign;
    }

    public void setJasperDesign(JasperDesign jasperDesign) {
        this.jasperDesign = jasperDesign;
    }

    public JRDesignStyle getStyle() {
        return style;
    }

    public void setStyle(JRDesignStyle style) {
        this.style = style;
    }
}
