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

import com.jaspersoft.ireport.designer.sheet.*;
import java.beans.FeatureDescriptor;
import java.beans.PropertyEditorSupport;
import java.util.List;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.nodes.Node;


public class ComboBoxPropertyEditor extends PropertyEditorSupport
        implements ExPropertyEditor { //, InplaceEditor.Factory
    
    private java.util.List tagValues = null;
    private boolean editable = true;
            
    /** Creates a new instance of DataPropertyEditor 
     *  @param boolean editable
     *  @param java.util.List tagValues
     */
    public ComboBoxPropertyEditor(boolean editable, java.util.List tagValues) {
        
        super();
        this.tagValues = tagValues;
        this.editable = editable;
    }

    @Override
    public void setValue(Object value) {

        Tag t = Tag.findTagByValue(value, tagValues);
        super.setValue( t != null ? t : value);
    }

    @Override
    public Object getValue() {

        Object value = super.getValue();
        if (value instanceof Tag)
        {
            return ((Tag)value).getValue();
        }

        return value;
    }
    
    @java.lang.Override
    public String getAsText() {
        Object key = getValue();
        
        // Look for the right tag...
        Tag t = Tag.findTagByValue(key, tagValues);
        if (t == null) {
            return ""+key;
        }
        return t.toString();
    }
    
    @java.lang.Override
    public void setAsText(String s) {
        Tag t = Tag.findTagByName(s, tagValues);
        setValue( t != null ? t.getValue() : s);
    }
    
    private String instructions=null;
    private boolean oneline=false;
    private boolean customEd=true;
    private PropertyEnv env;
    
    
    public void attachEnv(PropertyEnv env) {
        this.env = env;       
        FeatureDescriptor desc = env.getFeatureDescriptor();
        if (desc instanceof Node.Property){
            Node.Property prop = (Node.Property)desc;
            instructions = (String) prop.getValue ("instructions"); //NOI18N
            oneline = Boolean.TRUE.equals (prop.getValue ("oneline")); //NOI18N
            customEd = !Boolean.TRUE.equals (prop.getValue("suppressCustomEditor")); //NOI18N
        }
    }

    @Override
    public String[] getTags() {
        
        String[] s = new String[tagValues.size()];
        for (int i=0; i<tagValues.size(); ++i)
        {
            s[i] = ""+tagValues.get(i);
        }
        return s;
    }
    
    public void setTagValues(List newValues)
    {
        this.tagValues = newValues;
        this.firePropertyChange();
    }


    @Override
    public boolean supportsCustomEditor () {
        return customEd;
    }

    
    public java.awt.Component getCustomEditor () {
        Object val = getValue();
        String s = ""; // NOI18N
        if (val != null) {
            s = val instanceof String ? (String) val : val.toString();
        }
        return new StringCustomEditor(s, editable, oneline, instructions, this, env); // NOI18N
    }
    
    
    
}

