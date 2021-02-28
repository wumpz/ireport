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
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Mutex;

/**
 *
 * @author gtoffoli
 */
public class StyleChildren extends Index.KeysChildren implements PropertyChangeListener {

    JasperDesign jd = null;
    private JRDesignStyle style = null;
    private Lookup doLkp = null;
    
    @SuppressWarnings("unchecked")
    public StyleChildren(JasperDesign jd, JRDesignStyle style, Lookup doLkp) {
        super(new ArrayList());
        this.jd = jd;
        this.doLkp=doLkp;
        this.style=style;
        this.style.getEventSupport().addPropertyChangeListener(this);
    }

    /**
     * This method assumes that all the keys are JRDesignConditionalStyle.
     * 
     * @param key
     * @return
     */
    protected Node[] createNodes(Object key) {
        
        return new Node[]{new ConditionalStyleNode(jd, (JRDesignConditionalStyle)key, doLkp, getStyle())};
    }
    
    @Override
    protected void addNotify() {
        super.addNotify();
        recalculateKeys();
    }
    
    
    @SuppressWarnings("unchecked")
    public void recalculateKeys() {
        
        List l = (List)lock();
        l.clear();
        List styles = null;
        l.addAll(getStyle().getConditionalStyleList());
        update();
    }
    
    @SuppressWarnings("unchecked")
    public void reorder() { 
            Mutex.Action action = new Mutex.Action(){ 
                public Object run(){ 
                    Index.Support.showIndexedCustomizer(StyleChildren.this.getIndex()); 
                    return null; 
                } 
            }; 
            MUTEX.writeAccess(action); 
        }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName() == null) return;
        
        if (evt.getPropertyName().equals( JRDesignStyle.PROPERTY_CONDITIONAL_STYLES))
        {
            recalculateKeys();
        }
    }

    public JRDesignStyle getStyle() {
        return style;
    }

    public void setStyle(JRDesignStyle style) {
        this.style = style;
    }
}
