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

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.actions.AddBandAction;
import com.jaspersoft.ireport.designer.actions.DeleteGroupAction;
import com.jaspersoft.ireport.designer.actions.MaximizeBackgroundAction;
import com.jaspersoft.ireport.designer.actions.MoveGroupDownAction;
import com.jaspersoft.ireport.designer.actions.MoveGroupUpAction;
import com.jaspersoft.ireport.locale.I18n;
import java.util.ArrayList;
import javax.swing.Action;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class NullBandNode extends IRAbstractNode implements GroupNode {

    JasperDesign jd = null;
    NullBand band = null;
    public NullBandNode(JasperDesign jd, NullBand band,Lookup doLkp)
    {
        super (Children.LEAF, new ProxyLookup( doLkp, Lookups.fixed(jd, band)));
        this.jd = jd;
        this.band = band;
        setDisplayName ( ModelUtils.nameOf(band.getOrigin()));
        
        if (band.getOrigin().getBandTypeValue() == BandTypeEnum.GROUP_FOOTER)
        {
            setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/groupfooter-16.png");
        }
        else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.GROUP_HEADER)
        {
            setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/groupheader-16.png");
        }
        else
        {
            setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/band-16.png");
        }
    }
    
    @Override
    public String getHtmlDisplayName()
    {
        return "<font color=#999999>" + getDisplayName();
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();

        JRDesignGroup group = getGroup();
        if (group != null)
        {
            Sheet.Set groupPropertiesSet = Sheet.createPropertiesSet();
            groupPropertiesSet.setName("GROUP_PROPERTIES");
            groupPropertiesSet.setDisplayName(I18n.getString("BandNode.Property.Groupproperties"));
            groupPropertiesSet = BandNode.fillGroupPropertySet(groupPropertiesSet, getDataset(), group);

            sheet.put(groupPropertiesSet);
        }
        return sheet;
    }
    
    @Override
    public Action[] getActions(boolean popup) {

        java.util.List<Action> list = new ArrayList<Action>();
        list.add( SystemAction.get(AddBandAction.class));

        if (band.getOrigin().getBandTypeValue() == BandTypeEnum.BACKGROUND)
        {
            list.add(SystemAction.get(MaximizeBackgroundAction.class));
        }

        JRDesignGroup group = getGroup();

        if (group != null)
        {
            list.add( null );
            list.add( SystemAction.get(MoveGroupUpAction.class));
            list.add( SystemAction.get(MoveGroupDownAction.class));
            list.add( DeleteGroupAction.getInstance() );
        }

        return list.toArray(new Action[list.size()]);
    }

    public JRDesignDataset getDataset() {
        return jd.getMainDesignDataset();
    }

    public JRDesignGroup getGroup() {
        if (band.getOrigin() != null &&
            band.getOrigin().getGroupName() != null)
        {
            return (JRDesignGroup) getDataset().getGroupsMap().get(band.getOrigin().getGroupName());
        }
        return null;
    }
}
