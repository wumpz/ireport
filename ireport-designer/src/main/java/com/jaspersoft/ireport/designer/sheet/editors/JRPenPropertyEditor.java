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
package com.jaspersoft.ireport.designer.sheet.editors;

import com.jaspersoft.ireport.designer.sheet.editors.box.BoxBorderSelectionPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import java.beans.PropertyEditorSupport;

// bugfix# 9219 for attachEnv() method
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import java.beans.FeatureDescriptor;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.base.JRBasePrintLine;
import net.sf.jasperreports.engine.export.draw.LineDrawer;
import net.sf.jasperreports.engine.util.JRPenUtil;
import org.openide.nodes.Node;


/**
 * A property editor for String class.
 * @author   Ian Formanek
 * @version  1.00, 18 Sep, 1998
 */
public class JRPenPropertyEditor extends PropertyEditorSupport implements ExPropertyEditor
{
    public boolean isEditable(){
        return false;
    }

    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public void paintValue(Graphics grx, Rectangle box) 
    {
        JRPen pen = getValue() instanceof JRPen ? (JRPen)getValue() : null;
        if (pen == null)
        {
            super.paintValue(grx, box);
        }
        else
        {
//            //grx.clearRect(box.x, box.y, box.width, box.height);
//            grx.setColor(pen.getLineColor() == null ? Color.BLACK : pen.getLineColor());
//            //Stroke s = BoxBorderSelectionPanel.createStroke(pen);
//            Stroke stroke = JRPenUtil.getStroke(pen, BasicStroke.CAP_SQUARE);
//            if (stroke != null)
//            {
//                ((Graphics2D)grx).setStroke(stroke);
//                grx.drawLine(box.x + 4, box.y + box.height / 2, box.x + box.width - 4, box.y + box.height / 2);
//            }
            JRPrintLine line = new JRBasePrintLine(null);
            line.setX(box.x + 4);
            line.setY(box.y + box.height / 2);
            line.setWidth(box.width - 8);
            line.setHeight(1);
            line.getLinePen().setLineColor(pen.getLineColor());
            line.getLinePen().setLineStyle(pen.getLineStyleValue());
            line.getLinePen().setLineWidth(pen.getLineWidth());
            new LineDrawer().draw((Graphics2D)grx, line, 0, 0);
        }
        
    }
    
    /** sets new value */
    @Override
    public String getAsText() {
        return "";
    }
    
    /** sets new value */
    @Override
    public void setAsText(String s) {
        return;
    }

    @Override
    public boolean supportsCustomEditor () {
        return customEd;
    }

    @Override
    public java.awt.Component getCustomEditor () 
    {
        JRPen pen = getValue() instanceof JRPen ? (JRPen)getValue() : null;
        return new JRPenPropertyCustomEditor(pen, false, null, this, env); // NOI18N
    }

    //private String instructions=null;
    //private boolean oneline=false;
    private boolean customEd=true;
    private PropertyEnv env;

    // bugfix# 9219 added attachEnv() method checking if the user canWrite in text box 
    public void attachEnv(PropertyEnv env) {

        FeatureDescriptor desc = env.getFeatureDescriptor();
        if (desc instanceof Node.Property){
            Node.Property prop = (Node.Property)desc;
            //enh 29294 - support one-line editor & suppression of custom
            //editor
            //instructions = (String) prop.getValue ("instructions"); //NOI18N
            //oneline = Boolean.TRUE.equals (prop.getValue ("oneline")); //NOI18N
            customEd = !Boolean.TRUE.equals (prop.getValue ("suppressCustomEditor")); //NOI18N
        }
        this.env = env;
        
        

    }
}

