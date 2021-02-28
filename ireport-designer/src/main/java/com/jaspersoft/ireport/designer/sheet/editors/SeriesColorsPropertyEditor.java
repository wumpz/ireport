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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import java.beans.PropertyEditorSupport;

// bugfix# 9219 for attachEnv() method
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.JRChartPlot;
import org.openide.nodes.Node;


/**
 * A property editor for String class.
 * @author   Ian Formanek
 * @version  1.00, 18 Sep, 1998
 */
public class SeriesColorsPropertyEditor extends PropertyEditorSupport implements ExPropertyEditor
{
    public boolean isEditable(){
        return false;
    }

    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public void paintValue(Graphics gfx, Rectangle box) {
        //super.paintValue(gfx, box);
        List colors = (List)getValue();
        if (colors == null) super.paintValue(gfx, box);
        else
        {
            //gfx.clearRect(box.x, box.y, box.width, box.height);
            int cols = colors.size();
            int bw = 10;
            
            while (cols > 0 && ((bw+2)*cols) > box.width)
            {
                if (bw > 4) bw--;
                else
                {
                    cols--;
                }
            }
            
            int x = box.x+1;
            Iterator it = colors.iterator();
            for (int i=0; i< cols && it.hasNext(); ++i)
            {
                JRChartPlot.JRSeriesColor color = (JRChartPlot.JRSeriesColor)it.next();
                gfx.setColor(color.getColor());
                gfx.fillRect(x, box.y + 3, bw, Math.min(box.height-6, 10));
                gfx.setColor(Color.BLACK);
                gfx.drawRect(x, box.y + 3, bw, Math.min(box.height-6, 10));
                
                x += bw+2;
            }
        }
        
        
        
    }
    
    /** sets new value */
    @Override
    public String getAsText() {
        return "test";
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
    public java.awt.Component getCustomEditor () {
        List val = (List)getValue();
        return new SeriesColorsPropertyCustomEditor(val, false, null, this, env); // NOI18N
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

