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

import com.jaspersoft.ireport.designer.IReportManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import net.sf.jasperreports.crosstabs.JRCrosstabParameter;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabParameter;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Mutex;

/**
 *
 * @author gtoffoli
 */
public class CrosstabParametersChildren extends Index.KeysChildren implements PropertyChangeListener, PreferenceChangeListener {

    JasperDesign jd = null;
    private JRDesignCrosstab crosstab = null;
    private Lookup doLkp = null;
    

    @SuppressWarnings("unchecked")
    public CrosstabParametersChildren(JasperDesign jd, JRDesignCrosstab crosstab, Lookup doLkp) {
        super(new ArrayList());
        this.jd = jd;
        this.doLkp = doLkp;
        this.crosstab = crosstab;
        this.crosstab.getEventSupport().addPropertyChangeListener(this);
        IReportManager.getPreferences().addPreferenceChangeListener(this);
    }

    public void preferenceChange(PreferenceChangeEvent evt) {
        if (evt.getKey().equals("filter_parameters"))
        {
            recalculateKeys();
        }
    }
    
    /*
    @Override
    protected List<Node> initCollection() {
        return recalculateKeys();
    }
    */
    
    
    protected Node[] createNodes(Object key) {
        
        return new Node[]{new CrosstabParameterNode(jd, crosstab, (JRDesignCrosstabParameter)key,doLkp)};
    }
    
    
    
    @Override
    protected void addNotify() {
        super.addNotify();
        recalculateKeys();
    }
    
    
    @SuppressWarnings("unchecked")
    public void recalculateKeys() {

        /*
        List l = (List)lock();
        l.clear();
        List params = null;
        l.addAll(crosstab.getParametersList());
        */
        
        List l = (List)lock();
        l.clear();
        if (IReportManager.getPreferences().getBoolean("filter_parameters",false))
        {
            List paramsAll = crosstab.getParametersList();
            for (int i=0; i<paramsAll.size(); ++i)
            {
                JRCrosstabParameter p = (JRCrosstabParameter)paramsAll.get(i);
                if (!p.isSystemDefined())
                {
                    l.add(p);
                }
            }
        }
        else
        {
            l.addAll(crosstab.getParametersList());
        }

        if (getNode() != null && getNode() instanceof CrosstabParametersNode)
        {
            if (((SortableParametersNode)getNode()).isSort())
            {
                // Order elements by name...
                Object[] parameters = l.toArray();
                Arrays.sort(parameters, new Comparator() {

                    public int compare(Object o1, Object o2) {
                        return ((JRDesignCrosstabParameter)o1).getName().compareToIgnoreCase(((JRDesignCrosstabParameter)o2).getName());
                    }
                });
                l.clear();
                l.addAll(Arrays.asList(parameters));
            }
        }

         update();
    }

    protected void forceReorder(int[] ints) {
        super.reorder(ints);
        update();
    }
    
    @SuppressWarnings("unchecked")
    public void reorder() { 
            Mutex.Action action = new Mutex.Action(){ 
                public Object run(){ 
                    Index.Support.showIndexedCustomizer(CrosstabParametersChildren.this.getIndex()); 
                    return null; 
                } 
            }; 
            MUTEX.writeAccess(action); 
        }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignCrosstab.PROPERTY_PARAMETERS))
        {
            recalculateKeys();
        }
    }
}
