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
import java.util.Comparator;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Mutex;

/**
 *
 * @author gtoffoli
 */
public class FieldsChildren extends Index.KeysChildren implements PropertyChangeListener {

    JasperDesign jd = null;
    private JRDesignDataset dataset = null;
    private Lookup doLkp;
    
    public FieldsChildren(JasperDesign jd, Lookup doLkp) {
        this(jd, jd.getMainDesignDataset(),doLkp);
    }
    @SuppressWarnings("unchecked")
    public FieldsChildren(JasperDesign jd, JRDesignDataset dataset, Lookup doLkp) {
        super(new ArrayList());
        this.jd = jd;
        this.doLkp = doLkp;
        if (dataset == null) dataset = jd.getMainDesignDataset();
        this.dataset = dataset;
        this.dataset.getEventSupport().addPropertyChangeListener(this);
    }

    /*
    @Override
    protected List<Node> initCollection() {
        return recalculateKeys();
    }
    */
    
    
    protected Node[] createNodes(Object key) {
        
        return new Node[]{new FieldNode(jd, (JRDesignField)key, doLkp)};
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
        l.addAll(dataset.getFieldsList());
        if (getNode() != null && getNode() instanceof FieldsNode)
        {
            if (((FieldsNode)getNode()).isSort())
            {
                // Order elements by name...
                Object[] fields = dataset.getFieldsList().toArray();
                Arrays.sort(fields, new Comparator() {

                    public int compare(Object o1, Object o2) {
                        return ((JRDesignField)o1).getName().compareToIgnoreCase(((JRDesignField)o2).getName());
                    }
                });
                l.clear();
                l.addAll(Arrays.asList(fields));
            }
        }

        update();
    }

    @Override
    protected void reorder(int[] ints) {

        if (getNode() != null && getNode() instanceof FieldsNode && ((FieldsNode)getNode()).isSort())
        {
            return;
        }
        super.reorder(ints);
    }
    
    protected void forceReorder(int[] ints) {
        super.reorder(ints);
        update();
    }
    
    @SuppressWarnings("unchecked")
    public void reorder() { 
            Mutex.Action action = new Mutex.Action(){ 
                public Object run(){ 
                    Index.Support.showIndexedCustomizer(FieldsChildren.this.getIndex()); 
                    return null; 
                } 
            }; 
            MUTEX.writeAccess(action); 
        }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignDataset.PROPERTY_FIELDS))
        {
            recalculateKeys();
        }
    }
}
