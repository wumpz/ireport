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
import com.jaspersoft.ireport.designer.outline.nodes.BandNode;
import com.jaspersoft.ireport.designer.undo.DeleteBandUndoableEdit;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

public final class DeleteBandAction extends NodeAction {

    private static DeleteBandAction instance = null;
    
    public static synchronized DeleteBandAction getInstance()
    {
        if (instance == null)
        {
            instance = new DeleteBandAction();
        }
        
        return instance;
    }
    
    private DeleteBandAction()
    {
        super();
    }
    
    
    public String getName() {
        return I18n.getString("DeleteBandAction.Name.CTL_DeleteBandAction");
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
            if (activatedNodes[i] instanceof BandNode)
            {
                BandNode nbn = (BandNode)activatedNodes[i];
                JasperDesign jd = nbn.getJasperDesign();
                JRDesignBand band = nbn.getBand();
                
                if (jd != null && band !=null)
                {
                    
                    if (band.getOrigin().getBandTypeValue() == BandTypeEnum.BACKGROUND) jd.setBackground(null);
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.TITLE) jd.setTitle(null);
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.PAGE_HEADER) jd.setPageHeader(null);
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.COLUMN_HEADER) jd.setColumnHeader(null);
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.DETAIL)
                    {
                        JRDesignSection section = (JRDesignSection)jd.getDetailSection();
                        section.removeBand(band);
                        //jasperDesign.setDetail(null);
                    }
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.COLUMN_FOOTER) jd.setColumnFooter(null);
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.PAGE_FOOTER) jd.setPageFooter(null);
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.LAST_PAGE_FOOTER) jd.setLastPageFooter(null);
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.SUMMARY) jd.setSummary(null);
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.NO_DATA) jd.setNoData(null);
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.GROUP_HEADER)
                    {
                        JRDesignGroup group = ((JRDesignGroup)jd.getGroupsMap().get(band.getOrigin().getGroupName()));
                        JRDesignSection section = (JRDesignSection)group.getGroupHeaderSection();
                        section.removeBand(band);
                        //  JRDesignGroup g = (JRDesignGroup)jd.getGroupsMap().get( band.getOrigin().getGroupName());
                        //  g.setGroupHeader(null);

                    }
                    else if (band.getOrigin().getBandTypeValue() == BandTypeEnum.GROUP_FOOTER)
                    {
                        JRDesignGroup group = ((JRDesignGroup)jd.getGroupsMap().get(band.getOrigin().getGroupName()));
                        JRDesignSection section = (JRDesignSection)group.getGroupFooterSection();
                        section.removeBand(band);
                        //  JRDesignGroup g = (JRDesignGroup)jd.getGroupsMap().get( band.getOrigin().getGroupName());
                        //  g.setGroupFooter(null);
                    }
                    
                    DeleteBandUndoableEdit edit = new DeleteBandUndoableEdit(band,jd);
                    IReportManager.getInstance().addUndoableEdit(edit);
                }
            }
            
        }
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length == 0) return false;
        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (!(activatedNodes[i] instanceof BandNode)) return false;
        }
        return true;
    }
}