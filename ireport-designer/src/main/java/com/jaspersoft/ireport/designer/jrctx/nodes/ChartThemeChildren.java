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
package com.jaspersoft.ireport.designer.jrctx.nodes;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.chartthemes.simple.ChartThemeSettings;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
class ChartThemeChildren  extends Children.Keys {

    ChartThemeSettings template = null;
    private Lookup doLkp = null;

    @SuppressWarnings("unchecked")
    public ChartThemeChildren(ChartThemeSettings template, Lookup doLkp) {
        this.template = template;
        this.doLkp=doLkp;
        //this.template.getEventSupport().addPropertyChangeListener(this);
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        updateChildren();
    }


    protected Node[] createNodes(Object key) {

        Node node = null;
        
        if (key.equals("Chart"))
        {
            node = new ChartSettingsNode(template.getChartSettings(), doLkp);
        }
        else if (key.equals("Title"))
        {
            node = new TitleSettingsNode(template.getTitleSettings(), doLkp);
        }
        else if (key.equals("Subtitle"))
        {
            node = new TitleSettingsNode(template.getSubtitleSettings(), doLkp);
        }
        else if (key.equals("Legend"))
        {
            node = new LegendSettingsNode(template.getLegendSettings(), doLkp);
        }
        else if (key.equals("Plot"))
        {
            node = new PlotSettingsNode(template.getPlotSettings(), doLkp);
        }
        else if (key.equals("Domain Axis"))
        {
            node = new AxisSettingsNode(template.getDomainAxisSettings(), doLkp);
        }
        else if (key.equals("Range Axis"))
        {
            node = new AxisSettingsNode(template.getRangeAxisSettings(), doLkp);
        }

        node.setDisplayName(""+key);
        return new Node[]{node};
    }



    @SuppressWarnings("unchecked")
    public void updateChildren()
    {

        List l = new ArrayList();

        l.add( "Chart");
        l.add( "Title");
        l.add( "Subtitle");
        l.add( "Legend");
        l.add( "Plot");
        l.add( "Domain Axis");
        l.add( "Range Axis");

        setKeys(l);
    }

   
    

}
