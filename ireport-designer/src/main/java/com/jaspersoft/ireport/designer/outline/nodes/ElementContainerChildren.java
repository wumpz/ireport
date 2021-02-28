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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRVisitable;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Mutex;

/**
 *
 * @author gtoffoli
 */
public class ElementContainerChildren  extends Index.KeysChildren implements PropertyChangeListener {

    private ElementNodeVisitor visitor = null;
    private JRDesignElementGroup container = null;
    
    private boolean init=false;
    private synchronized void setInit(boolean init)
    {
        this.init = init;
    }
    private synchronized boolean isInit()
    {
        return this.init;
    }
    
    @SuppressWarnings("unchecked")
    protected ElementContainerChildren(JasperDesign jasperDesign, Lookup doLkp) {
        super(new ArrayList());
        this.visitor = new ElementNodeVisitor(jasperDesign, doLkp);
    }
    
    
    @SuppressWarnings("unchecked")
    public ElementContainerChildren(JasperDesign jasperDesign, JRDesignElementGroup container, Lookup doLkp) {
        super(new ArrayList());
        this.visitor = new ElementNodeVisitor(jasperDesign, doLkp);
        this.container = container;
        this.container.getEventSupport().addPropertyChangeListener(this);
    }

    protected Node[] createNodes(Object key) 
    {
        IRIndexedNode node = visitor.getNode((JRVisitable)key);
        
        if (node != null)
            return new Node[]{node};
        
        return new Node[]{};
    }
    
    @Override
    protected void addNotify() {
        super.addNotify();
        recalculateKeys();
    }
    
    
    @SuppressWarnings("unchecked")
    public void recalculateKeys() {
        
        if (container == null) return;
        
        List l = (List)lock();
        l.clear();
        l.addAll(container.getChildren());
        boolean b = isInit();
        setInit(true);
        update();
        setInit(b);
    }
    
    @SuppressWarnings("unchecked")
    public void reorder() { 
            Mutex.Action action = new Mutex.Action(){ 
                public Object run(){ 
                    Index.Support.showIndexedCustomizer(ElementContainerChildren.this.getIndex()); 
                    return null; 
                } 
            }; 
            MUTEX.writeAccess(action); 
        }

    public void propertyChange(PropertyChangeEvent evt) {
        if (isInit()) return;
        
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignBand.PROPERTY_CHILDREN))
        {
            recalculateKeys();
        }
    }
}
