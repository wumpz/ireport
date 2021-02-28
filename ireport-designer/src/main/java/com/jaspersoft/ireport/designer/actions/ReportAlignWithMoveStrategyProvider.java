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

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.undo.AddElementUndoableEdit;
import com.jaspersoft.ireport.designer.undo.AggregatedUndoableEdit;
import com.jaspersoft.ireport.designer.undo.BandChangeUndoableEdit;
import com.jaspersoft.ireport.designer.undo.DeleteElementUndoableEdit;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.designer.undo.UndoMoveChildrenUndoableEdit;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import com.jaspersoft.ireport.designer.widgets.SelectionWidget;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.AlignWithMoveDecorator;
import org.netbeans.api.visual.action.AlignWithWidgetCollector;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class ReportAlignWithMoveStrategyProvider extends AlignWithSupport implements MoveStrategy, MoveProvider {

    private ExMoveAction action = null;

    private boolean outerBounds;
    private boolean moveEnabled = false;
    private boolean snapToGrid = false;

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public boolean isOuterBounds() {
        return outerBounds;
    }

    public void setOuterBounds(boolean outerBounds) {
        this.outerBounds = outerBounds;
    }

    public boolean isSnapToGrid() {
        return snapToGrid;
    }

    public void setSnapToGrid(boolean snapToGrid) {
        this.snapToGrid = snapToGrid;
    }
    private int gridSize = 13;
    
    public ReportAlignWithMoveStrategyProvider (AlignWithWidgetCollector collector, LayerWidget interractionLayer, AlignWithMoveDecorator decorator, boolean outerBounds) {
        super (collector, interractionLayer, decorator);
        this.outerBounds = outerBounds;
    }

    public Point locationSuggested (Widget widget, Point originalLocation, Point suggestedLocation) {

        if (!moveEnabled)
        {
            if (Math.abs(suggestedLocation.x-originalLocation.x) > 5 || 
                Math.abs(suggestedLocation.y-originalLocation.y) > 5)
            {
                moveEnabled = true;
            }
            else
            {
                return originalLocation;
            }
        }

        if (IReportManager.getPreferences().getBoolean("noMagnetic", false)) return suggestedLocation;
        
        Point widgetLocation = widget.getLocation();
        Rectangle widgetBounds = outerBounds ? widget.getBounds () : widget.getClientArea();
        Rectangle bounds = widget.convertLocalToScene (widgetBounds);
        bounds.translate (suggestedLocation.x - widgetLocation.x, suggestedLocation.y - widgetLocation.y);
        Insets insets = widget.getBorder ().getInsets ();
        
        if (!outerBounds) {
            suggestedLocation.x += insets.left;
            suggestedLocation.y += insets.top;
        }
        
        Point point = new Point(suggestedLocation);
        if (isSnapToGrid())
        {
            
            point = snapToGrid(point, getGridSize());
            //System.out.println("Snapping....x:" + widget.getBounds().x + " " +suggestedLocation + " " + widgetLocation + "=> " + point + " " + bounds);
            
            point.x += insets.left;
            point.y += insets.top;
        }
        else
        {
            //System.out.println("Free movements");
            //System.out.flush();
            point = super.locationSuggested(widget, bounds, point, true, true, true, true);
        }
        
        if (! outerBounds) {
            point.x -= insets.left;
            point.y -= insets.top;
        }
        return widget.getParentWidget().convertSceneToLocal (point);
    }

    List<JRDesignElement> theSelectedElements = null;
    java.util.List<AggregatedUndoableEdit> undoEdits = null;
    public void movementStarted (Widget widget) {
        moveEnabled = false;

        undoEdits = new java.util.ArrayList<AggregatedUndoableEdit>();
        
        List<Widget> selectedElements = ((AbstractReportObjectScene)widget.getScene()).getSelectionLayer().getChildren();

        theSelectedElements = new ArrayList<JRDesignElement>();
        for (Widget w : selectedElements)
        {
            if (w.isVisible())
            {
                JRDesignElementWidget dew = ((SelectionWidget)w).getRealWidget();
                theSelectedElements.add(dew.getElement());
            }
        }

        // for each element, let's save x and y...
        for (Widget w : selectedElements)
        {
           if (w.isVisible())
           {
                JRDesignElementWidget dew = ((SelectionWidget)w).getRealWidget();
                JasperDesign jd = ((AbstractReportObjectScene)dew.getScene()).getJasperDesign();
                undoEdits.add(new ObjectPropertyUndoableEdit(dew.getElement(), "X", Integer.TYPE, new Integer(dew.getElement().getX()), new Integer(dew.getElement().getX())));
                undoEdits.add(new ObjectPropertyUndoableEdit(dew.getElement(), "Y", Integer.TYPE, new Integer(dew.getElement().getY()), new Integer(dew.getElement().getY())));
           }
        }
        show ();
    }

    public void movementFinished (Widget widget) {

        final AbstractReportObjectScene scene = (AbstractReportObjectScene)widget.getScene();
        List<Widget> selectedElements = scene.getSelectionLayer().getChildren();

        // This list is used to decide if look for a best parent.
        // A best parent should be found only for elements for which their element group
        // is not in the selection...

        List<JRDesignElement> elements = new ArrayList<JRDesignElement>();
        for (Widget w : selectedElements)
        {
            if (w.isVisible())
            {
                JRDesignElementWidget dew = ((SelectionWidget)w).getRealWidget();
                elements.add(dew.getElement());
            }
        }


        final List<JRDesignElement> elementsToAddToSelection = new ArrayList<JRDesignElement>();
        ArrayList<Widget> changedWidgets = new ArrayList<Widget>();

        for (Widget w : selectedElements)
        {
            if (w.isVisible())
            {
                JRDesignElementWidget dew = ((SelectionWidget)w).getRealWidget();
                elementsToAddToSelection.add(dew.getElement());

                Object obj = dew.getElement().getElementGroup();
                boolean changed = false;
                if (obj instanceof JRDesignElement && elements.contains((JRDesignElement)obj))
                {
                    // do nothing.
                }
                else
                {
                    AggregatedUndoableEdit edit = findBestNewParent(dew);
                    if (edit != null)
                    {
                        changed = true;
                        undoEdits.add(edit);
                    }
                }
                 
                /*
                if (dew.getElement().getElementGroup() instanceof JRDesignBand)
                {
                    JRDesignBand oldBand = (JRDesignBand)dew.getElement().getElementGroup();
                    JasperDesign jd = ((ReportObjectScene)dew.getScene()).getJasperDesign();
                    Point localLocation = dew.convertModelToLocalLocation(new Point( dew.getElement().getX(), dew.getElement().getY() ));
                    JRDesignBand newBand = ModelUtils.getBandAt(jd, localLocation);
                    if (newBand != null && newBand != oldBand)
                    {
                        int y1 = ModelUtils.getBandLocation(newBand, jd);
                        int y0 = ModelUtils.getBandLocation(oldBand, jd);

                        int deltaBand = y0 - y1;
                        // Update element band...
                        oldBand.getChildren().remove(dew.getElement());
                        //oldBand.removeElement(dew.getElement());

                        // Update the element coordinates...
                        dew.getElement().setElementGroup(newBand);
                        dew.getElement().setY(dew.getElement().getY() + deltaBand);
                        newBand.getChildren().add(dew.getElement());
                        newBand.getEventSupport().firePropertyChange(JRDesignBand.PROPERTY_CHILDREN, null, null);
                        oldBand.getEventSupport().firePropertyChange(JRDesignBand.PROPERTY_CHILDREN, null, null);
                        BandChangeUndoableEdit bcUndo = new BandChangeUndoableEdit(
                            jd,  oldBand, newBand, dew.getElement());
                        undoEdits.add(bcUndo);
                        
                    }
                }
                */

                if (dew.getChildrenElements() != null)
                {
                    if (changed)
                    {
                        updateChildren(dew, scene, changedWidgets);
                    }
                    undoEdits.add(0,new UndoMoveChildrenUndoableEdit(dew));
                }
            }
        }

        // add the undo operation...
        for (int i=0;i<undoEdits.size(); ++i)
        {
            AggregatedUndoableEdit theEdit = undoEdits.get(i);
            if (theEdit instanceof ObjectPropertyUndoableEdit)
            {
                ObjectPropertyUndoableEdit edit = (ObjectPropertyUndoableEdit)theEdit;
                if (edit.getNewValue() == null &&
                    edit.getOldValue() == null)
                {
                    undoEdits.remove(edit);
                    i--;
                    continue;
                }
                if (edit.getNewValue() != null &&
                    edit.getOldValue() != null &&
                    edit.getNewValue().equals(edit.getOldValue()))
                {
                    undoEdits.remove(edit);
                    i--;
                    continue;
                }
            }
            else if (theEdit instanceof BandChangeUndoableEdit)
            {
                BandChangeUndoableEdit edit = (BandChangeUndoableEdit)theEdit;
                if (edit.getOldBand() == null ||
                    edit.getNewBand() == null ||
                    edit.getNewBand() == edit.getOldBand())
                {
                    undoEdits.remove(edit);
                    i--;
                    continue;
                }
            }
        }
        
        if (undoEdits.size() > 0)
        {
            AggregatedUndoableEdit masterEdit = new AggregatedUndoableEdit("Move");
            
            for (int i=0;i<undoEdits.size(); ++i)
            {
                AggregatedUndoableEdit edit = undoEdits.get(i);
                masterEdit.concatenate(edit);
            }

            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    // 1. find elements to remove from the current selection...
                        List<Object> selectedElements = Arrays.asList(scene.getSelectedObjects().toArray());
                        for (Object obj : selectedElements)
                        {
                            if (!theSelectedElements.contains( obj ))
                            {
                                IReportManager.getInstance().removeSelectedObject(obj);
                            }
                        }

                        // 2. fimd elements to add to the current selection...
                        for (int sIndex=0; sIndex < theSelectedElements.size(); ++sIndex)
                        {
                            JRDesignElement selectedelement = theSelectedElements.get(sIndex);
                            if (!selectedElements.contains(selectedelement))
                            {
                                IReportManager.getInstance().addSelectedObject(selectedelement);
                            }
                        }
                }
            });
            

            /*
            if (elementsToAddToSelection.size() == 0)
            {
                IReportManager.getInstance().setSelectedObject(null);
            }
            boolean first = true;
            for (JRDesignElement ele : elementsToAddToSelection)
            {
                if (first)
                {
                    IReportManager.getInstance().setSelectedObject(ele);
                    first = false;
                }
                else
                {
                    IReportManager.getInstance().addSelectedObject(ele);
                }
            }
            */
            IReportManager.getInstance().addUndoableEdit(masterEdit);
        }
        
        // set the correct new selection...
        // TO DO.....
        /*
        if (undoEdits.size() > 0)
        {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {

                    for (int sIndex=0; sIndex < theSelectedElements.size(); ++sIndex)
                    {
                        JRDesignElement selectedelement = theSelectedElements.get(sIndex);
                        if (sIndex==0)
                        {
                            
                            IReportManager.getInstance().setSelectedObject(selectedelement);
                        }
                        IReportManager.getInstance().addSelectedObject(selectedelement);
                    }
                }
            });
        }
        */

        hide ();

    }

    public Point getOriginalLocation (Widget widget) {
        return ActionFactory.createDefaultMoveProvider ().getOriginalLocation (widget);
    }

    public void setNewLocation (Widget widget, Point location) 
    {
        // Calculating movement delta...
        Point p = widget.getPreferredLocation();
        Point delta = new Point(location);
        delta.translate(-p.x, -p.y);
        if (delta.x == 0 && delta.y == 0) return; //Nothing to do...
        //ActionFactory.createDefaultMoveProvider().setNewLocation(widget, location);
        // Update all the selected objects...
        
        List<Widget> selectedElements = ((AbstractReportObjectScene)widget.getScene()).getSelectionLayer().getChildren();
        ArrayList<Widget> changedWidgets = new ArrayList<Widget>();
        
        for (Widget w : selectedElements)
        {
           if (w.isVisible())
           {
                JRDesignElementWidget dew = ((SelectionWidget)w).getRealWidget();
                
                if (changedWidgets.contains(dew)) continue;

                Point loc = w.getPreferredLocation();
                loc.translate(delta.x, delta.y);
                w.setPreferredLocation(loc);
                
                Point dewloc = dew.getPreferredLocation();
                dewloc.translate(delta.x, delta.y);
                dewloc = dew.convertLocalToModelLocation(dewloc);
                boolean b = dew.setChanging(true);
                try {
                    dew.getElement().setX( dewloc.x);
                    dew.getElement().setY( dewloc.y);
                    findEdit(dew.getElement(),"X").setNewValue(dewloc.x);
                    findEdit(dew.getElement(),"Y").setNewValue(dewloc.y);
                } finally {
                    dew.setChanging(b);
                }
                dew.updateBounds();
                if (dew.getChildrenElements() != null)
                {
                    updateChildren(dew, (AbstractReportObjectScene)dew.getScene(), changedWidgets);
                }

                changedWidgets.add(dew);
           }
        }
    }

    private ObjectPropertyUndoableEdit findEdit(Object obj, String property)
    {
        for (AggregatedUndoableEdit theEdit : undoEdits)
        {

            if (theEdit instanceof ObjectPropertyUndoableEdit)
            {
                ObjectPropertyUndoableEdit edit = (ObjectPropertyUndoableEdit)theEdit;
                if (edit.getObject() == obj && edit.getProperty().equals(property))
                {
                    return edit;
                }
            }
        }
        return null;
    }

    private void updateChildren(JRDesignElementWidget dew, AbstractReportObjectScene scene, ArrayList<Widget> changedWidgets)
    {
        List listOfElements = dew.getChildrenElements();
        updateChildren(listOfElements, scene, changedWidgets);
    }

    private void updateChildren(List listOfElements, AbstractReportObjectScene scene, ArrayList<Widget> changedWidgets)
    {
          for (int i=0; i < listOfElements.size(); ++i)
          {
               if (listOfElements.get(i) instanceof JRDesignElement)
               {
                   JRDesignElement element = (JRDesignElement)listOfElements.get(i);
                   JRDesignElementWidget w = (JRDesignElementWidget)scene.findWidget(element);
                   if (w == null || changedWidgets.contains(w)) continue;
                   w.updateBounds();
                   w.getSelectionWidget().updateBounds();

                   if (w.getChildrenElements() != null)
                   {
                       updateChildren(w, scene, changedWidgets);
                   }

                   changedWidgets.add(w);
               }
               else if (listOfElements.get(i) instanceof JRDesignElementGroup)
               {
                   updateChildren(((JRDesignElementGroup)listOfElements.get(i)).getChildren(), scene, changedWidgets);
               }
          }
    }

    /**
     * This method tries to change the parent of an element if it's top left corner is no longer
     * inside its parent...
     * The new parent can be a band or even a frame.
     * @param dew
     */
    private AggregatedUndoableEdit findBestNewParent(JRDesignElementWidget dew) {

        if (!(dew.getScene() instanceof ReportObjectScene)) return null;
        
        boolean lookForBestNewParent = true;
        boolean forceLookForBestNewParent = false;
        
        if (getAction() != null)
        {
            lookForBestNewParent = !((getAction().getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK);
            forceLookForBestNewParent = (getAction().getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) == InputEvent.SHIFT_DOWN_MASK;
        }

        if (!lookForBestNewParent) return null;


        // if the element has not been moved... avoid to change anything...
        if (!moveEnabled) return null;
        

        JRDesignElement ele = dew.getElement();
        Point localLocation = new Point(dew.getLocation());

        JasperDesign jd = ((AbstractReportObjectScene)dew.getScene()).getJasperDesign();
        Rectangle parentBounds = ModelUtils.getParentBounds(jd, dew.getElement());

        if (!forceLookForBestNewParent && parentBounds.contains(new Point(ele.getX() + parentBounds.x, ele.getY() + parentBounds.y)))
        {
            return null;
        }
        ReportObjectScene ros = (ReportObjectScene)dew.getScene();

        

        JRElementGroup newParent = null;

        Point delta = new Point(0,0);

        JRDesignBand newBand = ModelUtils.getBandAt(jd, localLocation);
        if (newBand != null)
        {
            newParent = newBand;
            int y1 = ModelUtils.getBandLocation(newBand, jd);
            int x1 = jd.getLeftMargin();
            delta = new Point(localLocation.x - x1 ,localLocation.y - y1);
        }

        List<Widget> widgets = ros.getElementsLayer().getChildren();
        for (Widget w : widgets)
        {
            if (w instanceof JRDesignElementWidget && ((JRDesignElementWidget)w).getElement() instanceof JRDesignFrame
                && ((JRDesignElementWidget)w).getElement() != ele)
            {
                Rectangle r = new Rectangle(w.getLocation(), w.getBounds().getSize());
                if (r.contains(localLocation))
                {
                    newParent = (JRDesignFrame)((JRDesignElementWidget)w).getElement();
                    int y1 = w.getLocation().y;
                    int x1 = w.getLocation().x;
                    delta = new Point(localLocation.x - x1 ,localLocation.y - y1);

                }
            }
        }

        if (newParent != null)
        {
        
            // Change the parent...
            JRElementGroup oldElementGroup = (JRElementGroup)dew.getElement().getElementGroup();

            AggregatedUndoableEdit undoEdit = new AggregatedUndoableEdit();


            int indexInChildrens = 0;
            // remove the element from the old group....without firing any eveny...
            if (oldElementGroup instanceof JRDesignFrame)
            {
                indexInChildrens = ((JRDesignFrame)oldElementGroup).getChildren().indexOf(ele);
                ((JRDesignFrame)oldElementGroup).getChildren().remove(ele);
            }
            else if (oldElementGroup instanceof JRDesignBand)
            {
                indexInChildrens = ((JRDesignBand)oldElementGroup).getChildren().indexOf(ele);
                ((JRDesignBand)oldElementGroup).getChildren().remove(ele);
            }
            else
            {
                // too risky without knowing what type of container the parent,
                // and we don't want to mess with element groups.
                return null;
            }

            ele.setX(delta.x);
            ele.setY(delta.y);

            ele.setElementGroup(newParent);

            undoEdit.addEdit(new DeleteElementUndoableEdit(ele, oldElementGroup, indexInChildrens));

            // add element from the old group....without firing any eveny...
            
            if (newParent instanceof JRDesignFrame)
            {
                ((JRDesignFrame)newParent).getChildren().add(ele);
            }
            else if (newParent instanceof JRDesignBand)
            {
                ((JRDesignBand)newParent).getChildren().add(ele);
            }

            undoEdit.addEdit(new AddElementUndoableEdit(ele, newParent));



            // Now fire the proper events..
            if (oldElementGroup instanceof JRDesignFrame)
            {
                ((JRDesignFrame)oldElementGroup).getEventSupport().firePropertyChange(JRDesignBand.PROPERTY_CHILDREN, null, null);
            }
            else if (oldElementGroup instanceof JRDesignBand)
            {
                ((JRDesignBand)oldElementGroup).getEventSupport().firePropertyChange(JRDesignBand.PROPERTY_CHILDREN, null, null);
            }

            if (newParent instanceof JRDesignFrame)
            {
                ((JRDesignFrame)newParent).getEventSupport().firePropertyChange(JRDesignBand.PROPERTY_CHILDREN, null, null);
            }
            else if (newParent instanceof JRDesignBand)
            {
                ((JRDesignBand)newParent).getEventSupport().firePropertyChange(JRDesignBand.PROPERTY_CHILDREN, null, null);
            }

            

            return undoEdit;
            
        }

        return null;
    }

    /**
     * @return the action
     */
    public ExMoveAction getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(ExMoveAction action) {
        this.action = action;
    }
}

