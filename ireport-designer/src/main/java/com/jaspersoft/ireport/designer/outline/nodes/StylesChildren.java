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
import java.util.Arrays;
import java.util.List;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignReportTemplate;
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
public class StylesChildren extends Index.KeysChildren implements PropertyChangeListener {

    JasperDesign jd = null;
    private Lookup doLkp = null;
    
    public StylesChildren(JasperDesign jd, Lookup doLkp) {
        this(jd, jd.getMainDesignDataset(),doLkp);
    }
    @SuppressWarnings("unchecked")
    public StylesChildren(JasperDesign jd, JRDesignDataset dataset, Lookup doLkp) {
        super(new ArrayList());
        this.jd = jd;
        this.doLkp=doLkp;
        this.jd.getEventSupport().addPropertyChangeListener(this);
    }

    /*
    @Override
    protected List<Node> initCollection() {
        return recalculateKeys();
    }
    */
    
    
    protected Node[] createNodes(Object key) {

        if (key instanceof JRDesignStyle)
        {
            return new Node[]{new StyleNode(jd, (JRDesignStyle)key, doLkp)};
        }
        else //if (key instanceof JRReportTemplate)
        {
            return new Node[]{new TemplateReferenceNode(jd, (JRDesignReportTemplate)key, doLkp)};
        }
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
        l.addAll( Arrays.asList(jd.getTemplates()));
        l.addAll(jd.getStylesList());
        update();
    }
    
    @SuppressWarnings("unchecked")
    public void reorder() { 
            Mutex.Action action = new Mutex.Action(){ 
                public Object run(){ 
                    Index.Support.showIndexedCustomizer(StylesChildren.this.getIndex()); 
                    return null; 
                } 
            }; 
            MUTEX.writeAccess(action); 
        }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JasperDesign.PROPERTY_STYLES ) ||
            evt.getPropertyName().equals( JasperDesign.PROPERTY_TEMPLATES ))
        {
            recalculateKeys();
        }
    }
}
