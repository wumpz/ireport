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
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Mutex;

/**
 *
 * @author gtoffoli
 */
public class ParametersChildren extends Index.KeysChildren implements PropertyChangeListener, PreferenceChangeListener {

    JasperDesign jd = null;
    private JRDesignDataset dataset = null;
    private Lookup doLkp = null;
    
    public ParametersChildren(JasperDesign jd, Lookup doLkp) {
        this(jd, jd.getMainDesignDataset(),doLkp);
    }

    @SuppressWarnings("unchecked")
    public ParametersChildren(JasperDesign jd, JRDesignDataset dataset, Lookup doLkp) {
        super(new ArrayList());
        this.jd = jd;
        this.doLkp = doLkp;
        if (dataset == null) dataset = jd.getMainDesignDataset();
        this.dataset = dataset;
        this.dataset.getEventSupport().addPropertyChangeListener(this);
        IReportManager.getPreferences().addPreferenceChangeListener(this);

    }

    /*
    @Override
    protected List<Node> initCollection() {
        return recalculateKeys();
    }
    */
    
    
    protected Node[] createNodes(Object key) {
        
        return new Node[]{new ParameterNode(jd, (JRDesignParameter)key,doLkp)};
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
        if (IReportManager.getPreferences().getBoolean("filter_parameters",false))
        {
            List paramsAll = dataset.getParametersList();
            for (int i=0; i<paramsAll.size(); ++i)
            {
                JRParameter p = (JRParameter)paramsAll.get(i);
                if (!p.isSystemDefined())
                {
                    l.add(p);
                }
            }
        }
        else
        {
            l.addAll(dataset.getParametersList());
        }

        if (getNode() != null && getNode() instanceof ParametersNode)
        {
            if (((ParametersNode)getNode()).isSort())
            {
                // Order elements by name...
                Object[] parameters = l.toArray();
                Arrays.sort(parameters, new Comparator() {

                    public int compare(Object o1, Object o2) {
                        return ((JRDesignParameter)o1).getName().compareToIgnoreCase(((JRDesignParameter)o2).getName());
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
                    Index.Support.showIndexedCustomizer(ParametersChildren.this.getIndex()); 
                    return null; 
                } 
            }; 
            MUTEX.writeAccess(action); 
        }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignDataset.PROPERTY_PARAMETERS))
        {
            recalculateKeys();
        }
    }

    public void preferenceChange(PreferenceChangeEvent evt) {
        if (evt.getKey().equals("filter_parameters"))
        {
            recalculateKeys();
        }
    }

    @Override
    protected void reorder(int[] ints) {

        if (getNode() != null && getNode() instanceof ParametersNode && ((ParametersNode)getNode()).isSort())
        {
            return;
        }
        super.reorder(ints);
    }
}
