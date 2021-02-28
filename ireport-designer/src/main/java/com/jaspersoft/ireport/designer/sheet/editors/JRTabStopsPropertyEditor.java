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

import com.jaspersoft.ireport.locale.I18n;

import java.beans.PropertyEditorSupport;

// bugfix# 9219 for attachEnv() method
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import java.beans.FeatureDescriptor;
import java.util.List;
import javax.xml.soap.Text;
import org.openide.nodes.Node;


/**
 * A property editor for String class.
 * @author   Ian Formanek
 * @version  1.00, 18 Sep, 1998
 */
public class JRTabStopsPropertyEditor extends PropertyEditorSupport implements ExPropertyEditor
{
    public boolean isEditable(){
        return false;
    }
                
    @Override
    public String getAsText() {
        Object val = getValue();
        if (val == null) {
            return I18n.getString("Global.Property.Notabstopsset");
        }
        if (val instanceof List)
        {
            int len = ((List)val).size();
            switch (len)
            {
                case 0: return I18n.getString("Global.Property.Notabstopsset");
                case 1: return I18n.getString("Global.Property.Oneabstopset");
                default: return  I18n.getString("Global.Property.tabstopsset", len);
            }   
        }
        return "";
    }
    
    @Override
    public boolean supportsCustomEditor () {
        return true;
    }
    
    @Override
    public java.awt.Component getCustomEditor () {
        Object val = getValue();
        return new JRTabStopsPropertyCustomEditor(val, this, env); // NOI18N
    }

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

