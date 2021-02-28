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
package com.jaspersoft.ireport.designer.charts.multiaxis;

import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import java.beans.PropertyChangeEvent;
import java.util.List;
import net.sf.jasperreports.charts.design.JRDesignMultiAxisPlot;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class MultiAxisChartElementNode  extends ElementNode {


    public MultiAxisChartElementNode(JasperDesign jd, JRDesignChart element, Lookup doLkp)
    {
        this(jd, new AxisChartChildren(jd, element, doLkp), element, doLkp);
    }


    public MultiAxisChartElementNode(JasperDesign jd, AxisChartChildren children, JRDesignChart element, Lookup doLkp)
    {
           super(jd, element, children, children.getIndex(), doLkp);

           this.addNodeListener(new NodeListener() {

            public void childrenAdded(NodeMemberEvent ev) {}
            public void childrenRemoved(NodeMemberEvent ev) {}
            public void nodeDestroyed(NodeEvent ev) {}
            public void propertyChange(PropertyChangeEvent evt) {}

            @SuppressWarnings("unchecked")
            public void childrenReordered(NodeReorderEvent ev) {
                // Fire an event now...

                JRDesignMultiAxisPlot plot = (JRDesignMultiAxisPlot) ((JRDesignChart)getElement()).getPlot();

                List elements = plot.getAxes();
                int[] permutations = ev.getPermutation();

                Object[] elementsArray = new Object[elements.size()];
                for (int i=0; i<elementsArray.length; ++i)
                {
                    elementsArray[permutations[i]] = elements.get(i);
                }
                elements.clear();
                for (int i=0; i<elementsArray.length; ++i)
                {
                    elements.add(elementsArray[i]);
                }

                plot.getEventSupport().firePropertyChange( JRDesignMultiAxisPlot.PROPERTY_AXES, null, plot.getAxes());
            }
        });
    }

}
