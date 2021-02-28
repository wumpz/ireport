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
import com.jaspersoft.ireport.designer.outline.nodes.CellNode;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.sheet.editors.box.BoxPanel;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

/**
 *
 * @author gtoffoli
 */
public final class PaddingAndBordersAction extends NodeAction {

    public String getName() {
        return I18n.getString("PaddingAndBordersAction.Property.PaddingAndBorders");
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
        
        JasperDesign design = null;
        if (activatedNodes[0] instanceof ElementNode)
        {
            design = ((ElementNode)activatedNodes[0]).getJasperDesign();
        }
        else if (activatedNodes[0] instanceof CellNode)
        {
            design = ((CellNode)activatedNodes[0]).getJasperDesign();
        }

        List<JRLineBox> boxes = new ArrayList<JRLineBox>();
        
        JRBoxContainer firstContainer = null;
        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (activatedNodes[i] instanceof ElementNode &&
                ((ElementNode)activatedNodes[i]).getElement() instanceof JRBoxContainer)
            {
                boxes.add(((JRBoxContainer) ((ElementNode)activatedNodes[i]).getElement()).getLineBox());
                if (firstContainer == null)
                {
                    firstContainer = (JRBoxContainer) ((ElementNode)activatedNodes[i]).getElement();
                }
            }
            else if (activatedNodes[i] instanceof CellNode)
            {
                boxes.add(((JRBoxContainer) ((CellNode)activatedNodes[i]).getCellContents()).getLineBox());
                if (firstContainer == null)
                {
                    firstContainer = (JRBoxContainer)((CellNode)activatedNodes[i]).getCellContents();
                }
            }
            
        }
        
        
        JDialog dialog = new JDialog(Misc.getMainFrame(), true);
        BoxPanel panel = new BoxPanel();
        
        
        JRLineBox box = createCommonBox(boxes, firstContainer);
        
        panel.setLineBox(box);
        
        box = panel.showDialog(box);
       
        
        if (box != null)
        {
            for (JRLineBox bb : boxes)
            {
                  ModelUtils.applyBoxProperties(bb, box);
            }
            if (boxes.size() > 0) IReportManager.getInstance().notifyReportChange();
        }
        
//        for (int i=0; i<activatedNodes.length; ++i)
//        {
//            if (activatedNodes[i] instanceof ElementNode)
//            {
//                (((ElementNode)activatedNodes[i]).getElement()).getEventSupport().firePropertyChange( JRDesignElement.PROPERTY_PARENT_STYLE, null, null);
//            }
//        }
        
    }

//    private void printBox(JRLineBox box)
//    {
//        if (box == null)
//        {
//            System.out.println("NULL");
//        }
//        else
//        {
//            System.out.println("Padding: " + box.getPadding() + " " + box.getTopPadding() + " " + box.getLeftPadding() + " " + box.getRightPadding() + " " + box.getBottomPadding());
//            System.out.println("Pen:        " + box.getPen().getLineWidth() + " " + box.getPen().getLineStyle() + " " + box.getPen().getLineColor());
//            System.out.println("Pen bottom: " + box.getBottomPen().getLineWidth() + " " + box.getBottomPen().getLineStyle() + " " + box.getBottomPen().getLineColor());
//            System.out.println("Pen top:    " + box.getTopPen().getLineWidth() + " " + box.getTopPen().getLineStyle() + " " + box.getTopPen().getLineColor());
//            
//                    
//        }
//    }
    
    private JRLineBox createCommonBox(List<JRLineBox> boxes, JRBoxContainer container)
    {
        if (boxes == null || boxes.size() == 0) return new JRBaseLineBox(container);
        if (boxes.size() == 1) return (boxes.get(0)).clone(container);
        
        JRBaseLineBox finalbox = new JRBaseLineBox(null);
        
        boolean isFirst = true;
        
        for (JRLineBox box : boxes)
        {
            
            if (isFirst)
            {
                ModelUtils.applyBoxProperties(finalbox, box);
                isFirst = false;
                continue;
            }
            
            ModelUtils.applyDiff(finalbox, box);
        
        }
    
        return finalbox;
    }
    
    
     
     
    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length == 0) return false;
        
        // Check if all the elements are a JRBoxContainer
        for (int i=0; i<activatedNodes.length; ++i)
        {
                if (activatedNodes[i] instanceof ElementNode && ((ElementNode)activatedNodes[i]).getElement() instanceof JRBoxContainer)
                {
                    continue;
                }

                if (activatedNodes[i] instanceof CellNode)
                {
                    continue;
                }
                return false;
        }
        return true;
    }
}
