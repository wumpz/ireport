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
package com.jaspersoft.ireport.designer.sheet.properties.charts;

import com.jaspersoft.ireport.designer.sheet.editors.*;
import java.beans.FeatureDescriptor;
import java.beans.PropertyEditorSupport;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.nodes.Node;

import java.beans.PropertyEditorSupport;

// bugfix# 9219 for attachEnv() method
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import java.beans.FeatureDescriptor;
import net.sf.jasperreports.charts.JRItemLabel;
import net.sf.jasperreports.charts.design.JRDesignItemLabel;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.design.JRDesignFont;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Node;


/**
 * A property editor for String class.
 * @author   Ian Formanek
 * @version  1.00, 18 Sep, 1998
 */
public class JRItemLabelPropertyEditor extends PropertyEditorSupport implements ExPropertyEditor
{
    private static boolean useRaw = Boolean.getBoolean("netbeans.stringEditor.useRawCharacters");
    // bugfix# 9219 added editable field and isEditable() "getter" to be used in StringCustomEditor
    private boolean editable = true;   
    /** gets information if the text in editor should be editable or not */

    private JRChart chart = null;
    
    public JRItemLabelPropertyEditor(JRChart chart)
    {
        this.chart = chart;
    }
    
    public boolean isEditable(){
        return false;
    }
                
    /** sets new value */
    public void setAsText(String s) {
            return;
    }

    @Override
    public String getAsText() {
        JRItemLabel itemLabel = (JRItemLabel)getValue();
        if (itemLabel == null) itemLabel = new JRDesignItemLabel(null, chart);

        if (itemLabel.getFont() != null)
        {
            return itemLabel.getFont().getFontName() + " " + itemLabel.getFont().getFontSize();
        }
        else
        {
            return "";
        }
    }

    public boolean supportsCustomEditor () {
        return customEd;
    }

    public java.awt.Component getCustomEditor () {
        Object val = getValue();
        return 
            new JRItemLabelPropertyCustomEditor(
                chart,
                (JRItemLabel)val,
                isEditable(), 
                false, 
                null, 
                this, 
                env
                ); // NOI18N
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
            editable = prop.canWrite();
            //enh 29294 - support one-line editor & suppression of custom
            //editor
            //instructions = (String) prop.getValue ("instructions"); //NOI18N
            customEd = !Boolean.TRUE.equals (prop.getValue ("suppressCustomEditor")); //NOI18N
        }
        this.env = env;
    }
}

