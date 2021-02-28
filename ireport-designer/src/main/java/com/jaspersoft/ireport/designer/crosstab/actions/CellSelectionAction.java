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
package com.jaspersoft.ireport.designer.crosstab.actions;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.crosstab.CrosstabObjectScene;
import com.jaspersoft.ireport.designer.outline.OutlineTopComponent;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.Node;

/**
 *
 * @author gtoffoli
 */
public class CellSelectionAction extends WidgetAction.Adapter {

    public WidgetAction.State mousePressed(Widget widget,   WidgetAction.WidgetMouseEvent event)
    {
        
        if (event.getButton() == MouseEvent.BUTTON1 && widget instanceof CrosstabObjectScene)
        {
            // find the correct band...
            JasperDesign jd = ((CrosstabObjectScene)widget).getJasperDesign();
            JRDesignCrosstab crosstab = ((CrosstabObjectScene)widget).getDesignCrosstab();
            if (jd != null && crosstab != null)
            {
                    JRDesignCellContents cellContent = ModelUtils.getCellAt(crosstab, event.getPoint());
                    // If the cell is null, the document root is selected.
                    final Node node = IReportManager.getInstance().findNodeOf(cellContent == null ? crosstab : cellContent, OutlineTopComponent.getDefault().getExplorerManager().getRootContext());

                    SwingUtilities.invokeLater( new Runnable() {

                    public void run() {
                         try {
                            IReportManager.getInstance().getActiveVisualView().getExplorerManager().setSelectedNodes(new Node[]{node});
                            //IReportManager.getInstance().setSelectedObject(cellContent == null ? crosstab : cellContent);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                    //IReportManager.getInstance().setSelectedObject(cellContent == null ? crosstab : cellContent);
            }
        }
        
        return WidgetAction.State.REJECTED; // let someone use it...
    }
    
}
