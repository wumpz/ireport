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
package com.jaspersoft.ireport.designer.outline.nodes;

import javax.swing.JPanel;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class IRIndexedNode extends IRAbstractNode {

    /** Index implementation */
    private Index indexImpl;

   
    /** Allows subclasses to provide their own children and
    * index handling.
    * @param children Lookup...
    * @param children the children implementation
    * @param indexImpl the index implementation
    */
    protected IRIndexedNode(Children children, Index indexImpl, Lookup lookup) {
        super(children, lookup);
        this.indexImpl = indexImpl;
    }

    /*
    * @return false to signal that the customizer should not be used.
    *  Subclasses can override this method to enable customize action
    *  and use customizer provided by this class.
    */
    @Override
    public boolean hasCustomizer() {
        return indexImpl != null;
    }

    /* Returns the customizer component.
    * @return the component
    */
    @Override
    @SuppressWarnings("deprecation")
    public java.awt.Component getCustomizer() {
        
        java.awt.Container c = new JPanel();
        if (indexImpl != null)
        {
            org.openide.nodes.IndexedCustomizer customizer = new org.openide.nodes.IndexedCustomizer();
            customizer.setObject(indexImpl);
        }
        return c;
    }

    /** Get a cookie.
    * @param clazz representation class
    * @return the index implementation or children if these match the cookie class,
    * else using the superclass cookie lookup
    */
    @Override
    public <T extends Node.Cookie> T getCookie(Class<T> clazz) {
        if (indexImpl != null && clazz.isInstance(indexImpl)) {
            // ok, Index implementor is enough
            return clazz.cast(indexImpl);
        }

        Children ch = getChildren();

        if (clazz.isInstance(ch)) {
            // ok, children are enough
            return clazz.cast(ch);
        }

        return super.getCookie(clazz);
    }
    
}
