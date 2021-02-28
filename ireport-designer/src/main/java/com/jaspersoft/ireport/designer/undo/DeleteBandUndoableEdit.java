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
package com.jaspersoft.ireport.designer.undo;

import com.jaspersoft.ireport.designer.ModelUtils;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class DeleteBandUndoableEdit extends AggregatedUndoableEdit {

    private JRDesignBand band = null;
    private JasperDesign jasperDesign = null;
    
    public DeleteBandUndoableEdit(JRDesignBand band, JasperDesign jasperDesign)
    {
        this.band = band;
        this.jasperDesign = jasperDesign;
    }
    
    @Override
    public void undo() throws CannotUndoException {
        
        super.undo();
        JROrigin origin = getBand().getOrigin();
        if (origin != null)
        {
            switch (origin.getBandTypeValue())
            {
                case BACKGROUND:
                    jasperDesign.setBackground(band); break;
                case TITLE:
                    jasperDesign.setTitle(band); break;
                case PAGE_HEADER:
                    jasperDesign.setPageHeader(band); break;
                case COLUMN_HEADER:
                    jasperDesign.setColumnHeader(band); break;
                case DETAIL:
                {
                    ((JRDesignSection)jasperDesign.getDetailSection()).addBand(band);
                    break;
                }
                case COLUMN_FOOTER:
                    jasperDesign.setColumnFooter(band); break;
                case PAGE_FOOTER:
                    jasperDesign.setPageFooter(band); break;
                case LAST_PAGE_FOOTER:
                    jasperDesign.setLastPageFooter(band); break;
                case SUMMARY:
                    jasperDesign.setSummary(band); break;
                case NO_DATA:
                    jasperDesign.setNoData(band); break;
                case GROUP_HEADER:
                {
                    JRDesignGroup group = (JRDesignGroup)jasperDesign.getGroupsMap().get(origin.getGroupName());
                    ((JRDesignSection)group.getGroupHeaderSection()).addBand(band);
                    break;
                }
                case GROUP_FOOTER:
                {
                    JRDesignGroup group = (JRDesignGroup)jasperDesign.getGroupsMap().get(origin.getGroupName());
                    ((JRDesignSection)group.getGroupFooterSection()).addBand(band);
                    break;
                }
            }
        }
        
    }

    @Override
    public void redo() throws CannotRedoException {
        
        super.redo();
        
        JROrigin origin = getBand().getOrigin();

        if (origin != null)
        {
            switch (origin.getBandTypeValue())
            {
                case BACKGROUND:
                    jasperDesign.setBackground(null); break;
                case TITLE:
                    jasperDesign.setTitle(null); break;
                case PAGE_HEADER:
                    jasperDesign.setPageHeader(null); break;
                case COLUMN_HEADER:
                    jasperDesign.setColumnHeader(null); break;
                case DETAIL:
                {
                    JRDesignSection section = (JRDesignSection)jasperDesign.getDetailSection();
                    section.removeBand(band);
                    break;
                    //jasperDesign.setDetail(null); break;
                }
                case COLUMN_FOOTER:
                    jasperDesign.setColumnFooter(null); break;
                case PAGE_FOOTER:
                    jasperDesign.setPageFooter(null); break;
                case LAST_PAGE_FOOTER:
                    jasperDesign.setLastPageFooter(null); break;
                case SUMMARY:
                    jasperDesign.setSummary(null); break;
                case NO_DATA:
                    jasperDesign.setNoData(null); break;
                case GROUP_HEADER:
                {
                    JRDesignGroup group = ((JRDesignGroup)jasperDesign.getGroupsMap().get(origin.getGroupName()));
                    JRDesignSection section = (JRDesignSection)group.getGroupHeaderSection();
                    section.removeBand(band);
                    break;
                    //((JRDesignGroup)jasperDesign.getGroupsMap().get(origin.getGroupName())).setGroupHeader(null); break;
                }
                case GROUP_FOOTER:
                {
                    JRDesignGroup group = ((JRDesignGroup)jasperDesign.getGroupsMap().get(origin.getGroupName()));
                    JRDesignSection section = (JRDesignSection)group.getGroupFooterSection();
                    section.removeBand(band);
                    break;
                    //((JRDesignGroup)jasperDesign.getGroupsMap().get(origin.getGroupName())).setGroupFooter(null); break;
                }
                    
            }
        }
    }
    
    @Override
    public String getPresentationName() {
        return "Delete Band " + ModelUtils.nameOf(getBand().getOrigin());
    }

    public JRDesignBand getBand() {
        return band;
    }

    public void setBand(JRDesignBand band) {
        this.band = band;
    }

    public JasperDesign getJasperDesign() {
        return jasperDesign;
    }

    public void setJasperDesign(JasperDesign jasperDesign) {
        this.jasperDesign = jasperDesign;
    }
}
