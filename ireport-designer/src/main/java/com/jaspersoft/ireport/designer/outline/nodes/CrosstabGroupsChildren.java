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
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Mutex;

/**
 *
 * @author gtoffoli
 */
public class CrosstabGroupsChildren extends Index.KeysChildren implements PropertyChangeListener {

    JasperDesign jd = null;
    private JRDesignCrosstab crosstab = null;
    private Lookup doLkp = null;
    private int type = CrosstabGroupNode.ROW_GROUP;
    

    @SuppressWarnings("unchecked")
    public CrosstabGroupsChildren(JasperDesign jd, JRDesignCrosstab crosstab, Lookup doLkp, int type) {
        super(new ArrayList());
        this.jd = jd;
        this.doLkp = doLkp;
        this.crosstab = crosstab;
        this.type = type;
        this.crosstab.getEventSupport().addPropertyChangeListener(this);
    }

    /*
    @Override
    protected List<Node> initCollection() {
        return recalculateKeys();
    }
    */
    
    
    protected Node[] createNodes(Object key) {
        
        if (getType() == CrosstabGroupNode.ROW_GROUP)
        {
            return new Node[]{new CrosstabRowGroupNode(jd, crosstab, (JRDesignCrosstabRowGroup)key,doLkp)};
        }
        else
        {
            return new Node[]{new CrosstabColumnGroupNode(jd, crosstab, (JRDesignCrosstabColumnGroup)key,doLkp)};
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
        if (getType() == CrosstabGroupNode.ROW_GROUP)
        {
            l.addAll( Arrays.asList(crosstab.getRowGroups()));
        }
        else
        {
            l.addAll( Arrays.asList(crosstab.getColumnGroups()));
        }
        
        update();
    }
    
    @SuppressWarnings("unchecked")
    public void reorder() { 
            Mutex.Action action = new Mutex.Action(){ 
                public Object run(){ 
                    Index.Support.showIndexedCustomizer(CrosstabGroupsChildren.this.getIndex()); 
                    return null; 
                } 
            }; 
            MUTEX.writeAccess(action); 
        }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignCrosstab.PROPERTY_ROW_GROUPS) && getType()==CrosstabGroupNode.ROW_GROUP)
        {
            recalculateKeys();
        }
        if (evt.getPropertyName().equals( JRDesignCrosstab.PROPERTY_COLUMN_GROUPS) && getType()==CrosstabGroupNode.COLUMN_GROUP)
        {
            recalculateKeys();
        }
    }

    public int getType() {
        return type;
    }
}
