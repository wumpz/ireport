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
package com.jaspersoft.ireport.designer.sheet;

import com.jaspersoft.ireport.designer.sheet.editors.JRPenPropertyEditor;
import com.jaspersoft.ireport.designer.sheet.properties.AbstractProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPenContainer;
import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.type.LineStyleEnum;

/**
 *
 * @author gtoffoli
 */
public class JRPenProperty extends AbstractProperty {

    JRPen pen = null;
    JRPenPropertyEditor editor = null;
    JRPenContainer container = null;
    
    @SuppressWarnings("unchecked")
    public JRPenProperty(JRPen pen, JRPenContainer container)
    {
       super(JRPen.class, pen);
       setName("pen");
       setDisplayName(I18n.getString("PenProperty.Property.Pen"));        
       setShortDescription(I18n.getString("JRPenProperty.Property.detail"));
       setValue( "canEditAsText", Boolean.FALSE );
       this.pen = pen;
       this.container = container;
    }

    public void setPen(JRPen mpen)
    {
        if (mpen != null)
        {
            pen.setLineColor( mpen.getOwnLineColor());
            pen.setLineWidth( mpen.getOwnLineWidth());
            pen.setLineStyle( mpen.getOwnLineStyleValue());
        }
        else
        {
            pen.setLineColor( null );
            pen.setLineWidth( null );
            pen.setLineStyle( (LineStyleEnum)null );
        }
    }
    
    @Override
    public boolean isDefaultValue() {
        
        if (pen == null) return true;
        
        if (pen.getOwnLineColor() != null) return false;
        if (pen.getOwnLineWidth() != null) return false;
        if (pen.getOwnLineStyleValue() != null) return false;
        
        return true;
    }

    @Override
    public boolean supportsDefaultValue() {
        return true;
    }
    
    @Override
    public PropertyEditor getPropertyEditor() {
        
        if (editor == null)
        {
            editor = new JRPenPropertyEditor();
        }
        return editor;
    }

    @Override
    public Object getPropertyValue() {
        return pen.clone(container);
    }

    @Override
    public Object getOwnPropertyValue() {
        return pen.clone(container);
    }

    @Override
    public Object getDefaultValue() {
        return new JRBasePen(null);//FIXME this is dangerous. check it
    }

    @Override
    public void validate(Object value) {
        
    }

    @Override
    public void setPropertyValue(Object value) {
        
        if (value instanceof JRPen)
        {
            setPen((JRPen)value);
        }
        else
        {
           setPen(null);
        }
    }
    
}
