/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2013 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.ireport.designer.actions;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.outline.nodes.NullBand;
import com.jaspersoft.ireport.designer.outline.nodes.NullBandNode;
import com.jaspersoft.ireport.designer.undo.AddBandUndoableEdit;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

public final class AddBandAction extends NodeAction {

    private static AddBandAction instance = null;
    
    public static synchronized AddBandAction getInstance()
    {
        if (instance == null)
        {
            instance = new AddBandAction();
        }
        
        return instance;
    }
    
    private AddBandAction()
    {
        super();
    }
    
    
    public String getName() {
        return I18n.getString("BandAction.Name.CTL_AddBandAction");
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    protected void performAction(org.openide.nodes.Node[] activatedNodes) {
        
        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (activatedNodes[i] instanceof NullBandNode)
            {
                NullBandNode nbn = (NullBandNode)activatedNodes[i];
                JasperDesign jd = nbn.getLookup().lookup(JasperDesign.class);
                NullBand band = nbn.getLookup().lookup(NullBand.class);
                
                if (jd != null && band !=null)
                {
                    JRDesignBand b = new JRDesignBand();
                    b.setHeight(50);
                    if (band.getOrigin().getBandTypeValue() == BandTypeEnum.BACKGROUND) jd.setBackground(b);
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.TITLE) jd.setTitle(b);
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.PAGE_HEADER) jd.setPageHeader(b);
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.COLUMN_HEADER) jd.setColumnHeader(b);
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.DETAIL)
                    {
                        ((JRDesignSection)jd.getDetailSection()).addBand(b);
                        //jd.setDetail(b);
                    }
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.COLUMN_FOOTER) jd.setColumnFooter(b);
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.PAGE_FOOTER) jd.setPageFooter(b);
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.LAST_PAGE_FOOTER) jd.setLastPageFooter(b);
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.SUMMARY) jd.setSummary(b);
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.NO_DATA) jd.setNoData(b);
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.GROUP_HEADER)
                    {
                        JRDesignGroup g = (JRDesignGroup)jd.getGroupsMap().get( band.getOrigin().getGroupName());
                        ((JRDesignSection)g.getGroupHeaderSection()).addBand(b);
                        //g.setGroupHeader(b);
                    }
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.GROUP_FOOTER)
                    {
                        JRDesignGroup g = (JRDesignGroup)jd.getGroupsMap().get( band.getOrigin().getGroupName());
                        ((JRDesignSection)g.getGroupFooterSection()).addBand(b);
                        //g.setGroupFooter(b);
                    }
                    
                    AddBandUndoableEdit edit = new AddBandUndoableEdit(b,jd);
                    IReportManager.getInstance().addUndoableEdit(edit);
                }
            }
            
        }
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length == 0) return false;
        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (!(activatedNodes[i] instanceof NullBandNode)) return false;
        }
        return true;
    }
}