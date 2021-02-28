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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import java.awt.event.MouseEvent;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class BandSelectionAction extends WidgetAction.Adapter {

    public WidgetAction.State mousePressed(Widget widget,   WidgetAction.WidgetMouseEvent event)
    {
        if ((event.getModifiersEx () & MouseEvent.CTRL_DOWN_MASK) != 0 &&
             ((ReportObjectScene)widget.getScene()).getSelectedObjects().size() > 0)
        {
            return WidgetAction.State.CONSUMED;
        }

        if (event.getButton() == MouseEvent.BUTTON1 && widget instanceof ReportObjectScene)
        {
            // find the correct band...
            JasperDesign jd = ((ReportObjectScene)widget).getJasperDesign();
            if (jd != null)
            {
                    JRBand band = ModelUtils.getBandAt( jd , event.getPoint());
                    // If the band is null, the document root is selected.
                    IReportManager.getInstance().setSelectedObject(band);
            }
        }
        
        return WidgetAction.State.REJECTED; // let someone use it...
    }
    
}
