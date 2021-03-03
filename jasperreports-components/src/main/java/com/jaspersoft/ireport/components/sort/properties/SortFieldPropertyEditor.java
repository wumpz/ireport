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
package com.jaspersoft.ireport.components.sort.properties;


import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyEditorSupport;

// bugfix# 9219 for attachEnv() method
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import java.beans.FeatureDescriptor;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import org.openide.nodes.Node;


/**
 * A property editor for String class.
 * @author   Ian Formanek
 * @version  1.00, 18 Sep, 1998
 */
public class SortFieldPropertyEditor extends PropertyEditorSupport implements ExPropertyEditor, PropertyChangeListener
{
    private static SortFieldCellRenderer renderer;


    public SortFieldPropertyEditor()
    {
        super();
        renderer = new SortFieldCellRenderer();
    }
    
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

        JComponent c = (JComponent) renderer.getListCellRendererComponent(null, getValue(), 0, false, false);
        c.setSize(box.width, box.height);
        c.paint(grx);
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
    public java.awt.Component getCustomEditor () {
        Object val = getValue();
        return new SortFieldPropertyCustomEditor((String)val, this, env); // NOI18N
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

    public void propertyChange(PropertyChangeEvent evt) {
        //System.out.println(evt.getPropertyName() + " " + evt.getPropertyName());
    }
}

