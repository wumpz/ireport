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
import com.jaspersoft.ireport.designer.undo.AggregatedUndoableEdit;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import com.jaspersoft.ireport.designer.widgets.SelectionWidget;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import org.netbeans.api.visual.action.AlignWithMoveDecorator;
import org.netbeans.api.visual.action.AlignWithWidgetCollector;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.action.ResizeProvider.ControlPoint;
import org.netbeans.api.visual.action.ResizeStrategy;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class ReportAlignWithResizeStrategyProvider extends AlignWithSupport implements ResizeStrategy, ResizeProvider {

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

    public ReportAlignWithResizeStrategyProvider (AlignWithWidgetCollector collector, LayerWidget interractionLayer, AlignWithMoveDecorator decorator, boolean outerBounds) {
        super (collector, interractionLayer, decorator);
        this.outerBounds = outerBounds;
    }

    public Rectangle boundsSuggested (Widget widget, Rectangle originalBounds, Rectangle suggestedBounds, ControlPoint controlPoint) {
        Insets insets = widget.getBorder ().getInsets ();
        int minx = insets.left + insets.right;
        int miny = insets.top + insets.bottom;

        if (IReportManager.getPreferences().getBoolean("noMagnetic", false)) return suggestedBounds;

        

        // If we have a multiselection, the minimum size is given by the smallset object of
        // the selection...
        List<Widget> selectedElements = ((AbstractReportObjectScene)widget.getScene()).getSelectionLayer().getChildren();
        
        int minElementWidth = widget.getPreferredBounds().width;
        int minElementHeight = widget.getPreferredBounds().height;
        //System.out.println("Element size: " + minElementWidth + " x " + minElementHeight);
        for (Widget w : selectedElements)
        {
           if (w.isVisible())
           {
                Rectangle r = w.getBounds();
                minElementWidth = Math.min( r.width, minElementWidth);
                minElementHeight = Math.min( r.height, minElementHeight);
           }
        }
        //System.out.println("Element min size: " + minElementWidth + " x " + minElementHeight);
        //System.out.println("Max delta: " + minElementWidth + "  (" + ((widget.getBounds().width) - minElementWidth));
        
        minx += (widget.getBounds().width) - minElementWidth;
        miny += (widget.getBounds().height) - minElementHeight;
        
        suggestedBounds = widget.convertLocalToScene (suggestedBounds);

        Point suggestedLocation, point;
        int tempx, tempy;
        
        
        switch (controlPoint) {
            case BOTTOM_CENTER:
                suggestedLocation = new Point (suggestedBounds.x + suggestedBounds.width / 2, suggestedBounds.y + suggestedBounds.height);
                if (! outerBounds)
                    suggestedLocation.y -= insets.bottom;

                if (isSnapToGrid()) point = super.snapToGrid(suggestedLocation, getGridSize());
                else point = super.locationSuggested (widget, new Rectangle (suggestedLocation), suggestedLocation, false, true, false, false);

                if (! outerBounds)
                    point.y += insets.bottom;

                suggestedBounds.height = Math.max (miny, point.y - suggestedBounds.y);
                break;
            case BOTTOM_LEFT:
                suggestedLocation = new Point (suggestedBounds.x, suggestedBounds.y + suggestedBounds.height);
                if (! outerBounds) {
                    suggestedLocation.y -= insets.bottom;
                    suggestedLocation.x += insets.left;
                }

                if (isSnapToGrid()) point = super.snapToGrid(suggestedLocation, getGridSize());
                else point = super.locationSuggested (widget, new Rectangle (suggestedLocation), suggestedLocation, true, true, false, false);

                if (! outerBounds) {
                    point.y += insets.bottom;
                    point.x -= insets.left;
                }

                suggestedBounds.height = Math.max (miny, point.y - suggestedBounds.y);

                tempx = Math.min (point.x, suggestedBounds.x + suggestedBounds.width - minx);
                suggestedBounds.width = suggestedBounds.x + suggestedBounds.width - tempx;
                suggestedBounds.x = tempx;
                break;
            case BOTTOM_RIGHT:
                suggestedLocation = new Point (suggestedBounds.x + suggestedBounds.width, suggestedBounds.y + suggestedBounds.height);
                if (! outerBounds) {
                    suggestedLocation.y -= insets.bottom;
                    suggestedLocation.x -= insets.right;
                }

                if (isSnapToGrid()) point = super.snapToGrid(suggestedLocation, getGridSize());
                else point = super.locationSuggested (widget, new Rectangle (suggestedLocation), suggestedLocation, true, true, false, false);

                if (! outerBounds) {
                    point.y += insets.bottom;
                    point.x += insets.right;
                }

                suggestedBounds.height = Math.max (miny, point.y - suggestedBounds.y);

                suggestedBounds.width = Math.max (minx, point.x - suggestedBounds.x);
                break;
            case CENTER_LEFT:
                suggestedLocation = new Point (suggestedBounds.x, suggestedBounds.y + suggestedBounds.height / 2);
                if (! outerBounds)
                    suggestedLocation.x += insets.left;

                if (isSnapToGrid()) point = super.snapToGrid(suggestedLocation, getGridSize());
                else point = super.locationSuggested (widget, new Rectangle (suggestedLocation), suggestedLocation, true, false, false, false);

                if (! outerBounds)
                    point.x -= insets.left;

                tempx = Math.min (point.x, suggestedBounds.x + suggestedBounds.width - minx);
                suggestedBounds.width = suggestedBounds.x + suggestedBounds.width - tempx;
                suggestedBounds.x = tempx;
                break;
            case CENTER_RIGHT:
                suggestedLocation = new Point (suggestedBounds.x + suggestedBounds.width, suggestedBounds.y + suggestedBounds.height / 2);
                if (! outerBounds)
                    suggestedLocation.x -= insets.right;

                if (isSnapToGrid()) point = super.snapToGrid(suggestedLocation, getGridSize());
                else point = super.locationSuggested (widget, new Rectangle (suggestedLocation), suggestedLocation, true, false, false, false);

                if (! outerBounds)
                    point.x += insets.right;

                suggestedBounds.width = Math.max (minx, point.x - suggestedBounds.x);
                break;
            case TOP_CENTER:
                suggestedLocation = new Point (suggestedBounds.x + suggestedBounds.width / 2, suggestedBounds.y);
                if (! outerBounds)
                    suggestedLocation.y += insets.top;

                if (isSnapToGrid()) point = super.snapToGrid(suggestedLocation, getGridSize());
                else point = super.locationSuggested (widget, new Rectangle (suggestedLocation), suggestedLocation, false, true, false, false);

                if (! outerBounds)
                    point.y -= insets.top;

                tempy = Math.min (point.y, suggestedBounds.y + suggestedBounds.height - miny);
                suggestedBounds.height = suggestedBounds.y + suggestedBounds.height - tempy;
                suggestedBounds.y = tempy;
                break;
            case TOP_LEFT:
                suggestedLocation = new Point (suggestedBounds.x, suggestedBounds.y);
                if (! outerBounds) {
                    suggestedLocation.y += insets.top;
                    suggestedLocation.x += insets.left;
                }

                if (isSnapToGrid()) point = super.snapToGrid(suggestedLocation, getGridSize());
                else point = super.locationSuggested (widget, new Rectangle (suggestedLocation), suggestedLocation, true, true, false, false);

                if (! outerBounds) {
                    point.y -= insets.top;
                    point.x -= insets.left;
                }

                tempy = Math.min (point.y, suggestedBounds.y + suggestedBounds.height - miny);
                suggestedBounds.height = suggestedBounds.y + suggestedBounds.height - tempy;
                suggestedBounds.y = tempy;

                tempx = Math.min (point.x, suggestedBounds.x + suggestedBounds.width - minx);
                suggestedBounds.width = suggestedBounds.x + suggestedBounds.width - tempx;
                suggestedBounds.x = tempx;
                break;
            case TOP_RIGHT:
                suggestedLocation = new Point (suggestedBounds.x + suggestedBounds.width, suggestedBounds.y);
                if (! outerBounds) {
                    suggestedLocation.y += insets.top;
                    suggestedLocation.x -= insets.right;
                }

                if (isSnapToGrid()) 
                {
                    point = super.snapToGrid(suggestedLocation, getGridSize());
                    //point.x -= insets.top;
                    //point.y += insets.right;
                }
                else point = super.locationSuggested (widget, new Rectangle (suggestedLocation), suggestedLocation, true, true, false, false);

                if (! outerBounds) {
                    point.y -= insets.top;
                    point.x += insets.right;
                }

                tempy = Math.min (point.y, suggestedBounds.y + suggestedBounds.height - miny);
                suggestedBounds.height = suggestedBounds.y + suggestedBounds.height - tempy;
                suggestedBounds.y = tempy;

                suggestedBounds.width = Math.max (minx, point.x - suggestedBounds.x);
                break;
        }
        
        // Update all the elements except the one selected...
        // Calculating movement delta...
        
         
        suggestedBounds = widget.convertSceneToLocal (suggestedBounds);
        Rectangle wbounds = widget.getBounds();
        Rectangle delta = new Rectangle(suggestedBounds.x - wbounds.x,
                                        suggestedBounds.y - wbounds.y,
                                        suggestedBounds.width - wbounds.width,
                                        suggestedBounds.height - wbounds.height);
        
        ArrayList<Widget> changedWidgets = new ArrayList<Widget>();
        
        for (Widget w : selectedElements)
        {
           if (w.isVisible()) // && w != widget
           {
                JRDesignElementWidget dew = ((SelectionWidget)w).getRealWidget();
               
                if (changedWidgets.contains(dew)) continue;
                
                Rectangle r = w.getPreferredBounds();
                r.x += delta.x;
                r.y += delta.y;
                r.width += delta.width;
                r.height += delta.height;
                w.setPreferredBounds(r);
                
                r = dew.getPreferredBounds();
                r.x += delta.x;
                r.y += delta.y;
                r.width += delta.width;
                r.height += delta.height;
                dew.setPreferredBounds(r);
                
                // Update the model...
                
                Point loc = dew.getPreferredLocation();
                loc.x += r.x;
                loc.y += r.y;
                //System.out.println("Resized: " + loc + " " + dew.convertLocalToModelLocation(loc) );
                loc = dew.convertLocalToModelLocation(loc);
                boolean b = dew.setChanging(true);
                try {
                    dew.getElement().setX( loc.x);
                    dew.getElement().setY( loc.y);
                    dew.getElement().setWidth(r.width);
                    dew.getElement().setHeight(r.height);
                } finally {
                    dew.setChanging(b);
                }
                
                if (dew.getChildrenElements() != null)
                {
                    updateChildren(dew, (AbstractReportObjectScene)dew.getScene(), changedWidgets);
                }
                changedWidgets.add(dew);
           }
        }
        
        
        return suggestedBounds;
    }

    java.util.List<ObjectPropertyUndoableEdit> undoEdits = null; 
    
    public void resizingStarted (Widget widget) {
        
        undoEdits = new java.util.ArrayList<ObjectPropertyUndoableEdit>();
        
        List<Widget> selectedElements = ((AbstractReportObjectScene)widget.getScene()).getSelectionLayer().getChildren();
        
        // for each element, let's save x and y...
        for (Widget w : selectedElements)
        {
           if (w.isVisible())
           {
                JRDesignElementWidget dew = ((SelectionWidget)w).getRealWidget();
                undoEdits.add(new ObjectPropertyUndoableEdit(dew.getElement(), "X", Integer.TYPE, new Integer(dew.getElement().getX()), new Integer(dew.getElement().getX())));
                undoEdits.add(new ObjectPropertyUndoableEdit(dew.getElement(), "Y", Integer.TYPE, new Integer(dew.getElement().getY()), new Integer(dew.getElement().getY())));
                undoEdits.add(new ObjectPropertyUndoableEdit(dew.getElement(), "Width", Integer.TYPE, new Integer(dew.getElement().getWidth()), new Integer(dew.getElement().getWidth())));
                undoEdits.add(new ObjectPropertyUndoableEdit(dew.getElement(), "Height", Integer.TYPE, new Integer(dew.getElement().getHeight()), new Integer(dew.getElement().getHeight())));
           }
        }
        show ();
    }

    private ObjectPropertyUndoableEdit findEdit(Object obj, String property)
    {
        for (ObjectPropertyUndoableEdit edit : undoEdits)
        {
            if (edit.getObject() == obj &&
                edit.getProperty().equals(property))
            {
                return edit;
            }
        }
        return null;
    }
    
    public void resizingFinished (Widget widget) {
        hide ();
        
        List<Widget> selectedElements = ((AbstractReportObjectScene)widget.getScene()).getSelectionLayer().getChildren();
        ArrayList<Widget> changedWidgets = new ArrayList<Widget>();
        
        for (Widget w : selectedElements)
        {
           if (w.isVisible())
           {
                JRDesignElementWidget dew = ((SelectionWidget)w).getRealWidget();
                
                if (changedWidgets.contains(dew)) continue;
                
                Insets insets = w.getBorder().getInsets();
                Point loc = w.getPreferredLocation();
                Rectangle bounds = w.getPreferredBounds();
                loc.x += bounds.x + insets.left;
                loc.y += bounds.y + insets.top;
                
                bounds.x = -insets.left;
                bounds.y = -insets.right;
                w.setPreferredBounds(bounds);
                w.setPreferredLocation(loc);
                
                //System.out.println("Resized: " + loc + " " + dew.convertLocalToModelLocation(loc) );
                loc = dew.convertLocalToModelLocation(loc);
                boolean b = dew.setChanging(true);
                try {
                    dew.getElement().setX( loc.x);
                    dew.getElement().setY( loc.y);
                    dew.getElement().setWidth( bounds.width - insets.left - insets.right);
                    dew.getElement().setHeight( bounds.height - insets.top - insets.bottom);
                    
                    findEdit(dew.getElement(),"X").setNewValue(dew.getElement().getX());
                    findEdit(dew.getElement(),"Y").setNewValue(dew.getElement().getY());
                    findEdit(dew.getElement(),"Width").setNewValue(dew.getElement().getWidth());
                    findEdit(dew.getElement(),"Height").setNewValue(dew.getElement().getHeight());
                
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
        
        
        // add the undo operation...
        for (int i=0;i<undoEdits.size(); ++i)
        {
            ObjectPropertyUndoableEdit edit = undoEdits.get(i);
            if (edit.getNewValue() == null &&
                edit.getOldValue() == null) 
            {
                undoEdits.remove(edit);
                continue;
            }
            if (edit.getNewValue() != null &&
                edit.getOldValue() != null &&
                edit.getNewValue().equals(edit.getOldValue()))
            {
                undoEdits.remove(edit);
                continue;
            }
        }
        
        if (undoEdits.size() > 0)
        {
            AggregatedUndoableEdit masterEdit = new AggregatedUndoableEdit("Resize");
            for (int i=0;i<undoEdits.size(); ++i)
            {
                ObjectPropertyUndoableEdit edit = undoEdits.get(i);
                masterEdit.concatenate(edit);
            }
            
            IReportManager.getInstance().addUndoableEdit(masterEdit);
        }
    }



    private void updateChildren(JRDesignElementWidget dew, AbstractReportObjectScene scene, ArrayList<Widget> changedWidgets)
    {
          List listOfElements = dew.getChildrenElements();

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
          }
    }

}

