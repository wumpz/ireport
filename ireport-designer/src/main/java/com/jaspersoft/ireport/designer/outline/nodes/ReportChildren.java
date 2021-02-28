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
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class ReportChildren extends Children.Keys {

    JasperDesign jd = null;
    Lookup doLkp = null;
    
    public ReportChildren(JasperDesign jd, Lookup doLkp) {
        this.jd = jd;
        this.doLkp = doLkp;
    }
    
    protected Node[] createNodes(Object key) {

        if (key.equals("styles"))
        {
            return new Node[]{new StylesNode(jd,doLkp)};
        }
        else if (key.equals("parameters"))
        {
            return new Node[]{new ParametersNode(jd,doLkp)};
        }
        else if (key.equals("fields"))
        {
            return new Node[]{new FieldsNode(jd, jd.getMainDesignDataset(),doLkp)};
        }
        else if (key.equals("variables"))
        {
            return new Node[]{new VariablesNode(jd, jd.getMainDesignDataset(),doLkp)};
        }
        else if (key.equals("scriptlets"))
        {
            return new Node[]{new ScriptletsNode(jd,doLkp)};
        }
        else if (key instanceof JRDesignDataset)
        {
            return new Node[]{new DatasetNode(jd, (JRDesignDataset)key,doLkp)};
        }
        else if (key instanceof JRDesignBand)
        {
            return new Node[]{new BandNode(jd, (JRDesignBand)key,doLkp)};
        }
        else if (key instanceof NullBand)
        {
            return new Node[]{new NullBandNode(jd, (NullBand)key,doLkp)};
        }
        
        AbstractNode node = new IRAbstractNode(LEAF, new ProxyLookup(doLkp, Lookups.singleton(key)));
        node.setName(key+"");
        return new Node[]{node};
    }
    
    @Override
    protected void addNotify() {
        super.addNotify();
        updateChildren();
    }
    
    @SuppressWarnings("unchecked")
    public void updateChildren()
    {
        ArrayList children = new ArrayList();
        children.add("styles");
        children.add("parameters");
        children.add("fields");
        children.add("variables");
        children.add("scriptlets");
        children.addAll( jd.getDatasetsList() );
        //children.addAll( ModelUtils.getBands(jd) );
        children.add( ( jd.getTitle() != null) ? jd.getTitle() : new NullBand(new JROrigin(jd.getName(), BandTypeEnum.TITLE )) );
        children.add( ( jd.getPageHeader() != null) ? jd.getPageHeader() : new NullBand(new JROrigin(jd.getName(), BandTypeEnum.PAGE_HEADER )) );
        children.add( ( jd.getColumnHeader() != null) ? jd.getColumnHeader() : new NullBand(new JROrigin(jd.getName(), BandTypeEnum.COLUMN_HEADER )) );
        // Group headers...
        JRGroup[] groups = jd.getGroups();
        for (int i=0 ;i<groups.length; ++i)
        {
            if (groups[i].getGroupHeaderSection() == null ||
                groups[i].getGroupHeaderSection().getBands().length == 0)
            {
                children.add( new NullBand(new JROrigin(jd.getName(),groups[i].getName(), BandTypeEnum.GROUP_HEADER )));
            }
            else
            {
                JRSection section = groups[i].getGroupHeaderSection();
                JRBand[] bands = section.getBands();
                for (int k=0; k<bands.length; ++k)
                {
                    children.add(bands[k]);
                }
            }
        }
        if (jd.getDetailSection() == null ||
            jd.getDetailSection().getBands().length == 0)
        {
            children.add(new NullBand(new JROrigin(jd.getName(),BandTypeEnum.DETAIL )));
        }
        else
        {
            JRSection section = jd.getDetailSection();
            JRBand[] bands = section.getBands();
            for (int k=0; k<bands.length; ++k)
            {
                children.add(bands[k]);
            }
        }
        //children.add( ( jd.getDetail() != null) ? jd.getDetail() : new NullBand(new JROrigin(jd.getName(), JROrigin.DETAIL )) );

        // Group footers...
        for (int i=groups.length-1; i>=0; --i)
        {
            if (groups[i].getGroupFooterSection() == null ||
                groups[i].getGroupFooterSection().getBands().length == 0)
            {
                children.add( new NullBand(new JROrigin(jd.getName(),groups[i].getName(), BandTypeEnum.GROUP_FOOTER )));
            }
            else
            {
                JRSection section = groups[i].getGroupFooterSection();
                JRBand[] bands = section.getBands();
                for (int k=0; k<bands.length; ++k)
                {
                    children.add(bands[k]);
                }
            }
        }
        children.add( ( jd.getColumnFooter() != null) ? jd.getColumnFooter() : new NullBand(new JROrigin(jd.getName(), BandTypeEnum.COLUMN_FOOTER )) );
        children.add( ( jd.getPageFooter() != null) ? jd.getPageFooter() : new NullBand(new JROrigin(jd.getName(), BandTypeEnum.PAGE_FOOTER )) );
        children.add( ( jd.getLastPageFooter() != null) ? jd.getLastPageFooter() : new NullBand(new JROrigin(jd.getName(), BandTypeEnum.LAST_PAGE_FOOTER )) );
        children.add( ( jd.getSummary() != null) ? jd.getSummary() : new NullBand(new JROrigin(jd.getName(), BandTypeEnum.SUMMARY )) );
        children.add( ( jd.getNoData() != null) ? jd.getNoData() : new NullBand(new JROrigin(jd.getName(), BandTypeEnum.NO_DATA )) );
        children.add( ( jd.getBackground() != null) ? jd.getBackground() : new NullBand(new JROrigin(jd.getName(), BandTypeEnum.BACKGROUND )) );
        
        setKeys(children);
    }
}
