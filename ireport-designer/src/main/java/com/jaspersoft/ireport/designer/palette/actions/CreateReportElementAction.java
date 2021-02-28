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
package com.jaspersoft.ireport.designer.palette.actions;

import java.awt.Point;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.widget.Scene;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class CreateReportElementAction extends CreateReportElementsAction
{

    @Override
    public JRDesignElement[] createReportElements(JasperDesign jd) {
        JRDesignElement ele = createReportElement(jd);
        if (ele == null) return new JRDesignElement[0];
        return new JRDesignElement[]{ele};
    }

   
    public abstract JRDesignElement createReportElement(JasperDesign jd);

    public static void dropElementAt(Scene theScene, JasperDesign jasperDesign, JRDesignElement element, Point location)
    {
        if (element != null)
        {
            CreateReportElementAction rea = new CreateReportElementAction() {

                @Override
                public JRDesignElement createReportElement(JasperDesign jd) {
                    return null;
                }
            };
            rea.dropElementsAt(theScene, jasperDesign, new JRDesignElement[]{element},location);
        }
    }
    // The main idea is to optimize the space of each element...
    /*
    public static void dropElementAt(Scene theScene, JasperDesign jasperDesign, JRDesignElement element, Point location)
    {
        if (theScene instanceof ReportObjectScene)
        {
            Point p = theScene.convertViewToScene( location );
            //p.x -= 10; removing a location transformation
            //p.y -= 10;
            // find the band...
            JRDesignBand b = ModelUtils.getBandAt(jasperDesign, p);
            int yLocation = ModelUtils.getBandLocation(b, jasperDesign);
            Point pLocationInBand = new Point(p.x - jasperDesign.getLeftMargin(),
                                              p.y - yLocation);
            if (b != null)
            {
                JRDesignFrame frame = findTopMostFrameAt((ReportObjectScene)theScene, p);

                if (frame != null)
                {
                    frame.addElement(element);
                    Point parentLocation = ModelUtils.getParentLocation(jasperDesign, element);
                    element.setX( p.x - parentLocation.x);
                    element.setY( p.y - parentLocation.y);
                }
                else
                {
                    element.setX(pLocationInBand.x);
                    element.setY(pLocationInBand.y);
                    b.addElement(element);
                }
                AddElementUndoableEdit edit = new AddElementUndoableEdit(element,b);
                IReportManager.getInstance().addUndoableEdit(edit);
            }
        }
        else if (theScene instanceof CrosstabObjectScene)
        {
            Point p = theScene.convertViewToScene( location );
            //p.x -= 10; removing a location transformation
            //p.y -= 10;
            
            // This if should be always false since the check is done
            // in the specific implementations of createReportElement...
            if (element instanceof JRCrosstab ||
                element instanceof JRChart ||
                element instanceof JRBreak ||
                element instanceof JRSubreport)
            {
                Runnable r = new Runnable() {
                    public void run() {
                        JOptionPane.showMessageDialog(Misc.getMainFrame(), "You can use this kind of element inside a crosstab.","Error", JOptionPane.WARNING_MESSAGE);
                    }
                };

                Mutex.EVENT.readAccess(r); 
                return;
             }
            
             JRDesignCrosstab crosstab = ((CrosstabObjectScene)theScene).getDesignCrosstab();
             final JRDesignCellContents cell = ModelUtils.getCellAt(crosstab, p, true);
             if (cell != null)
             {
                 Point base = ModelUtils.getCellLocation(crosstab, cell);
                 element.setX( p.x - base.x );
                 element.setY( p.y - base.y );
                 
                 String styleName = "Crosstab Data Text";
                 if (jasperDesign.getStylesMap().containsKey(styleName))
                 {
                     element.setStyle((JRStyle)jasperDesign.getStylesMap().get(styleName));
                 }
                 cell.addElement(element);
                 
                 AddElementUndoableEdit edit = new AddElementUndoableEdit(element,cell);
                 IReportManager.getInstance().addUndoableEdit(edit);
                 
             }
        }
    }
     */
    
}
