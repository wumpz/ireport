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
package com.jaspersoft.ireport.designer.jrctx.nodes.properties;

import com.jaspersoft.ireport.designer.sheet.editors.JRPenPropertyEditor;
import com.jaspersoft.ireport.designer.sheet.properties.AbstractProperty;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.chartthemes.simple.ColorProvider;
import net.sf.jasperreports.chartthemes.simple.PaintProvider;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPenContainer;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.util.JRPenUtil;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class AbstractPenProperty extends AbstractProperty implements JRPenContainer
{
    private JRPenPropertyEditor editor = null;
    
    @SuppressWarnings("unchecked")
    public AbstractPenProperty(Object object)
    {
       super(JRPen.class, object);

       setValue( "canEditAsText", Boolean.FALSE );
    }

    @Override
    public Object getPropertyValue() 
    {
        return getPen();
    }

    @Override
    public Object getOwnPropertyValue() 
    {
        return getOwnPen();
    }

    @Override
    public Object getDefaultValue() 
    {
        return getDefaultPen();
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
    public void validate(Object value)
    {
    }

    @Override
    public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException 
    {
        setPropertyValue(getDefaultPen());
    }

    @Override
    public void setPropertyValue(Object value)
    {
        setPen((JRPen)value);
    }

    public JRPen getPen() 
    {
        JRPen pen = new JRBasePen(this);
        
        if (
            getPaintProvider() != null
            && getStroke() != null
            )
        {
            ColorProvider colorProvider = 
                getPaintProvider() instanceof ColorProvider
                ? (ColorProvider)getPaintProvider()
                : null;
            if (colorProvider != null)
            {
                pen.setLineColor(colorProvider.getColor());
            }
            BasicStroke basicStroke = 
                getStroke() instanceof BasicStroke
                ? (BasicStroke)getStroke()
                : null;
            if (basicStroke != null)
            {
                setToPen(basicStroke, pen);
            }
        }

        return pen;
    }

    public JRPen getOwnPen()
    {
        return getPen();
    }

    public JRPen getDefaultPen()
    {
        return null;
    }

    public void setPen(JRPen pen)
    {
        if (pen == null)
        {
            setPaintProvider(null);
            setStroke(null);
        }
        else
        {
            setPaintProvider(new ColorProvider(pen.getLineColor()));
            setStroke(JRPenUtil.getStroke(pen, BasicStroke.CAP_SQUARE));
        }
    }

    /**
     *
     */
    public void setToPen(BasicStroke stroke, JRPen pen)
    {
        if (stroke != null && pen != null)
        {
            float lineWidth = stroke.getLineWidth();
            float[] dashArray = stroke.getDashArray();

            pen.setLineWidth(lineWidth);
            
            int lineCap = stroke.getEndCap();
            switch (lineCap)
            {
                case BasicStroke.CAP_SQUARE :
                {
                    if (
                        dashArray != null && dashArray.length == 2 
                        && dashArray[0] == 0 && dashArray[1] == 2 * lineWidth
                        )
                    {
                        pen.setLineStyle(LineStyleEnum.DOTTED);
                    }
                    else if (
                        dashArray != null && dashArray.length == 2 
                        && dashArray[0] == 4 * lineWidth && dashArray[1] == 4 * lineWidth
                        )
                    {
                        pen.setLineStyle(LineStyleEnum.DASHED);
                    }
                    else
                    {
                        pen.setLineStyle(LineStyleEnum.SOLID);
                    }
                    break;
                }
                case BasicStroke.CAP_BUTT :
                {
                    if (
                        dashArray != null && dashArray.length == 2 
                        && dashArray[0] == lineWidth && dashArray[1] == lineWidth
                        )
                    {
                        pen.setLineStyle(LineStyleEnum.DOTTED);
                    }
                    else if (
                        dashArray != null && dashArray.length == 2 
                        && dashArray[0] == 5 * lineWidth && dashArray[1] == 3 * lineWidth
                        )
                    {
                        pen.setLineStyle(LineStyleEnum.DASHED);
                    }
                    else
                    {
                        pen.setLineStyle(LineStyleEnum.SOLID);
                    }
                    break;
                }
            }
        }
    }

    public Float getDefaultLineWidth() {
        return new Float(0);
    }

    public Color getDefaultLineColor() {
        return null;
    }

    public JRDefaultStyleProvider getDefaultStyleProvider() {
        return null;
    }

    public JRStyle getStyle() {
        return null;
    }

    public String getStyleNameReference() {
        return null;
    }

    public abstract PaintProvider getPaintProvider();

    public abstract void setPaintProvider(PaintProvider paintProvider);

    public abstract Stroke getStroke();

    public abstract void setStroke(Stroke stroke);

}
