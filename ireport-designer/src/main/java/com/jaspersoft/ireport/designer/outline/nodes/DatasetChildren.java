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

import java.util.ArrayList;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author gtoffoli
 */
class DatasetChildren extends Children.Keys {

    JasperDesign jd = null;
    JRDesignDataset dataset = null;
    Lookup doLkp = null;
    
    public DatasetChildren(JasperDesign jd, JRDesignDataset dataset, Lookup doLkp) {
        this.jd = jd;
        this.doLkp = doLkp;
        this.dataset = dataset;
    }
        
    protected Node[] createNodes(Object key) {
        
        if (key.equals("parameters"))
        {
            return new Node[]{new ParametersNode(jd, dataset, doLkp)};
        }
        else if (key.equals("fields"))
        {
            return new Node[]{new FieldsNode(jd, dataset, doLkp)};
        }
        else if (key.equals("variables"))
        {
            return new Node[]{new VariablesNode(jd, dataset, doLkp)};
        }
        else if (key.equals("groups"))
        {
            return new Node[]{new GroupsNode(jd, dataset, doLkp)};
        }
        
        AbstractNode node = new AbstractNode(LEAF, Lookups.singleton(key));
        node.setName(key+"");
        return new Node[]{node};
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected void addNotify() {
        super.addNotify();
        ArrayList children = new ArrayList();
        children.add("parameters");
        children.add("fields");
        children.add("variables");
        children.add("groups");
        setKeys(children);
    }

}
