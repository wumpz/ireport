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

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.crosstab.CrosstabObjectScene;
import com.jaspersoft.ireport.designer.palette.PaletteItemAction;
import com.jaspersoft.ireport.designer.undo.AddElementUndoableEdit;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.awt.Point;
import java.awt.dnd.DropTargetDropEvent;
import java.util.List;
import javax.swing.JOptionPane;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Mutex;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class CreateReportElementsAction extends PaletteItemAction 
{

    /**
     * Find the top most element that support children...
     * @param theScene
     * @param p
     * @return
     */
    private static JRDesignElementWidget findTopMostFrameAt(AbstractReportObjectScene theScene, Point p) {

         LayerWidget layer = theScene.getElementsLayer();
         List<Widget> widgets = layer.getChildren();

         // Use revers order to find the top most...
         for (int i= widgets.size()-1; i>=0; --i)
         {
             Widget w = widgets.get(i);
             Point p2 = w.convertSceneToLocal(p);
             if (w.isHitAt(p2))
             {
                 if (w instanceof JRDesignElementWidget)
                 {
                     JRDesignElement de =((JRDesignElementWidget)w).getElement();
                     if (((JRDesignElementWidget)w).getChildrenElements() != null)
                     {
                         return (JRDesignElementWidget)w;
                     }
                 }
             }
         }
         return null;
    }

    public void drop(DropTargetDropEvent dtde) {
        
        JRDesignElement[] elements = createReportElements(getJasperDesign());
        
        if (elements == null || elements.length == 0) return;
        // Find location...
        dropElementsAt(getScene(), getJasperDesign(), elements, dtde.getLocation());
    }
    
    public abstract JRDesignElement[] createReportElements(JasperDesign jd);

    /**
     * This method allows to set element size and new position for the created elements
     *
     * @param elements Array of elements created by createReportElements
     * @param index inside the array for the specific element
     * @param theScene the scene in which we are creating the element
     * @param jasperDesign the JasperDesign
     * @param psrent can be a band, a frame or a cell
     * @param originalLocation the location in View coordinated of the drag event.
     *
     */
    public void adjustElement(JRDesignElement[] elements,
                                      int index,
                                      Scene theScene,
                                      JasperDesign jasperDesign,
                                      Object parent,
                                      Point dropLocation)
    {

    }


    // The main idea is to optimize the space of each element...
    public void dropElementsAt(Scene theScene, JasperDesign jasperDesign, JRDesignElement[] elements, Point location)
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
                JRDesignElementWidget wContainer = findTopMostFrameAt((ReportObjectScene)theScene, p);

                for (int k=0; k<elements.length; ++k)
                {
                    JRDesignElement element = elements[k];
                    if (wContainer != null)
                    {
                            JRDesignElement frame = wContainer.getElement();
                            Point parentLocation = wContainer.convertModelToLocalLocation(new Point(frame.getX(), frame.getY()));
                            /*
                            Point parentLocation = ModelUtils.getParentLocation(jasperDesign, element);
                            if (parentLocation.x == 0 &&
                                parentLocation.y == 0)
                            {
                                parentLocation = ModelUtils.getParentLocation(jasperDesign, wContainer.getElement());
                                parentLocation.x = wContainer.getElement().getX();
                                parentLocation.y = wContainer.getElement().getY();
                            }
                            */
                            element.setX( p.x - parentLocation.x);
                            element.setY( p.y - parentLocation.y);
                            adjustElement(elements,k,theScene, jasperDesign, frame, location);
                            wContainer.addElement(element);
                    }
                    else
                    {
                            // In case the element is added to the band... let's do our best to keep the element inside the band itself...
                            int newYLocation = pLocationInBand.y;
                            if (newYLocation + element.getHeight() > b.getHeight())
                            {
                                newYLocation = b.getHeight() - element.getHeight();
                                if (newYLocation < 0) newYLocation=0;
                            }
                            element.setX(pLocationInBand.x);
                            element.setY(newYLocation);
                            adjustElement(elements,k,theScene, jasperDesign, b, location);
                            b.addElement(element);
                    }
                    AddElementUndoableEdit edit = new AddElementUndoableEdit(element,b);
                    IReportManager.getInstance().addUndoableEdit(edit);

                }
            }
        }
        else if (theScene instanceof CrosstabObjectScene)
        {
            Point p = theScene.convertViewToScene( location );
            //p.x -= 10; removing a location transformation
            //p.y -= 10;
            
            // This if should be always false since the check is done
            // in the specific implementations of createReportElement...

            for (int k=0; k<elements.length; ++k)
            {
                JRDesignElement element = elements[k];

                if (element instanceof JRCrosstab ||
                    element instanceof JRChart ||
                    element instanceof JRBreak ||
                    element instanceof JRSubreport)
                {
                    Runnable r = new Runnable() {
                        public void run() {
                            JOptionPane.showMessageDialog(Misc.getMainFrame(), "You cannot use this kind of element inside a crosstab.","Error", JOptionPane.WARNING_MESSAGE);
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
                     
                     adjustElement(elements,k,theScene, jasperDesign, cell, location);
                     cell.addElement(element);

                     AddElementUndoableEdit edit = new AddElementUndoableEdit(element,cell);
                     IReportManager.getInstance().addUndoableEdit(edit);

                 }
            }
        }
        else if (theScene instanceof AbstractReportObjectScene )
        {

            for (int k=0; k<elements.length; ++k)
            {
                try {

                    JRDesignElement element = elements[k];
                    Object parent = ((AbstractReportObjectScene)theScene).dropElementAt(element, location);
                    if (parent == null) continue;
                    
                    adjustElement(elements,k,theScene, jasperDesign, parent, location);

                    AddElementUndoableEdit edit = new AddElementUndoableEdit(element, parent);
                    IReportManager.getInstance().addUndoableEdit(edit);


                } catch (final RuntimeException ex)
                {
                    Runnable r = new Runnable() {
                        public void run() {
                            JOptionPane.showMessageDialog(Misc.getMainFrame(), ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                        }
                    };

                    Mutex.EVENT.readAccess(r);
                    return;
                 }
            }
        }
    }
    
}
