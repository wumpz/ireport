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
package com.jaspersoft.ireport.designer.crosstab;

import com.jaspersoft.ireport.designer.*;
import com.jaspersoft.ireport.designer.actions.KeyboardElementMoveAction;
import com.jaspersoft.ireport.designer.actions.ReportAlignWithMoveStrategyProvider;
import com.jaspersoft.ireport.designer.actions.ReportAlignWithResizeStrategyProvider;
import com.jaspersoft.ireport.designer.actions.ReportAlignWithWidgetCollector;
import com.jaspersoft.ireport.designer.actions.TranslucentRectangularSelectDecorator;
import com.jaspersoft.ireport.designer.crosstab.actions.CellSelectionAction;
import com.jaspersoft.ireport.designer.crosstab.actions.CellSeparatorMoveAction;
import com.jaspersoft.ireport.designer.crosstab.widgets.CellSeparatorWidget;
import com.jaspersoft.ireport.designer.crosstab.widgets.CrosstabWidget;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import com.jaspersoft.ireport.designer.widgets.JRDesignImageWidget;
import com.jaspersoft.ireport.designer.widgets.visitor.ConfigurableDrawVisitor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.crosstabs.type.CrosstabTotalPositionEnum;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.draw.DrawVisitor;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.widget.EventProcessingType;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.SeparatorWidget.Orientation;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class CrosstabObjectScene extends AbstractReportObjectScene implements PropertyChangeListener {

    private CrosstabWidget crosstabWidget = null;
    private List<JRDesignElement> selectedElements = new ArrayList<JRDesignElement>();
    
    private ReportAlignWithWidgetCollector reportAlignWithWidgetCollector = null;
    private ReportAlignWithMoveStrategyProvider reportAlignWithMoveStrategyProvider = null;
    private ReportAlignWithResizeStrategyProvider reportAlignWithResizeStrategyProvider = null;
    private KeyboardElementMoveAction keyboardElementMoveAction = null;
    private CellSelectionAction cellSelectionAction = null;
    
    private List<Integer> verticalSeparators = new ArrayList<Integer>();
    private List<Integer> horizontalSeparators = new ArrayList<Integer>();
    
    public void setSnapToGrid(boolean snapToGrid) {
        if (this.snapToGrid != snapToGrid)
        {
            this.snapToGrid = snapToGrid;
            this.reportAlignWithMoveStrategyProvider.setSnapToGrid(snapToGrid);
            this.reportAlignWithResizeStrategyProvider.setSnapToGrid(snapToGrid);
        }
    }

    public void setGridVisible(boolean gridVisible) {
        if (this.gridVisible != gridVisible)
        {
            this.gridVisible = gridVisible;
            pageLayer.revalidate(true);
            validate();
        }
    }
    
    public ReportAlignWithWidgetCollector getReportAlignWithWidgetCollector() {
        return reportAlignWithWidgetCollector;
    }

    public void setReportAlignWithWidgetCollector(ReportAlignWithWidgetCollector reportAlignWithWidgetCollector) {
        this.reportAlignWithWidgetCollector = reportAlignWithWidgetCollector;
    }

    private JasperDesign jasperDesign = null;
    private JRDesignCrosstab designCrosstab = null;
    private DrawVisitor drawVisitor = null;

    public DrawVisitor getDrawVisitor() {
        return drawVisitor;
    }

    public void setDrawVisitor(DrawVisitor drawVisitor) {
        this.drawVisitor = drawVisitor;
    }
    

    public JasperDesign getJasperDesign() {
        return jasperDesign;
    }

    public void setJasperDesign(JasperDesign jasperDesign) {

            this.jasperDesign = jasperDesign;
            if (jasperDesign == null) {
                
                this.drawVisitor = null;
                this.rebuildDocument();
                return;
            }

            this.drawVisitor = new ConfigurableDrawVisitor( jasperDesign, null);
            jasperDesign.getEventSupport().addPropertyChangeListener(this);
            
            // Adding listeners for groups...
            for (int i=0; i<this.jasperDesign.getGroupsList().size(); ++i)
            {
                JRDesignGroup grp = (JRDesignGroup)this.jasperDesign.getGroupsList().get(i);
                grp.getEventSupport().addPropertyChangeListener(this);
            }
            
            
            ThreadUtils.invokeInAWTThread(new Runnable() {

                public void run() {
                    rebuildDocument();
                }
            });
        
        
    }
    
    /*
    public void addCellSeparatorWidget(JRDesignCrosstabCell cell, int xyLocation, boolean vertical)
    {
        if (b == null) return;
        BandSeparatorWidget bbw = new BandSeparatorWidget(this, b);
        bbw.getActions().addAction( new BandMoveAction(true, InputEvent.SHIFT_DOWN_MASK) );
        bbw.getActions().addAction( new BandMoveAction() );
        bandSeparatorsLayer.addChild(bbw);
    }*/
    
    public void addElementWidget(JRDesignElement de)
    {
        if (de == null) return;
        JRDesignElementWidget widget = null;
        
        if (de instanceof JRDesignImage)
        {
            widget = new JRDesignImageWidget(this, (JRDesignImage)de);
        }
        else
        {
            widget = new JRDesignElementWidget(this, de);
        }
        //widget.getSelectionWidget().setLayout(new ResizeHandleLayout());
        //widget.getSelectionWidget().getActions().addAction(createSelectAction());
        
        //widget.getActions().addAction (createSelectAction());
        widget.getActions().addAction (getReportSelectAction());
        
        widget.getSelectionWidget().getActions().addAction( keyboardElementMoveAction );

        widget.getSelectionWidget().getActions().addAction (getReportSelectAction());
        widget.getSelectionWidget().getActions().addAction(createObjectHoverAction());
        
        
        widget.getSelectionWidget().getActions().addAction( ActionFactory.createResizeAction(
                        reportAlignWithResizeStrategyProvider, 
                        reportAlignWithResizeStrategyProvider) );
        
        widget.getSelectionWidget().getActions().addAction( ActionFactory.createMoveAction(
                        reportAlignWithMoveStrategyProvider, 
                        reportAlignWithMoveStrategyProvider) );
        
        widget.getActions().addAction( ActionFactory.createResizeAction(
                        reportAlignWithResizeStrategyProvider, 
                        reportAlignWithResizeStrategyProvider) );
        
        widget.getActions().addAction( ActionFactory.createMoveAction(
                        reportAlignWithMoveStrategyProvider, 
                        reportAlignWithMoveStrategyProvider) );
        
        widget.getActions().addAction(inplaceEditorAction);
        widget.getSelectionWidget().getActions().addAction(inplaceEditorAction);
        
        widget.getActions ().addAction(elementPopupMenuAction);
        widget.getSelectionWidget().getActions().addAction(elementPopupMenuAction);

        widget.getActions().addAction( ActionFactory.createActionMapAction(elementInputMap, elementActionMap) );
        widget.getSelectionWidget().getActions().addAction( ActionFactory.createActionMapAction(elementInputMap, elementActionMap) );

        
        elementsLayer.addChild(widget);
        selectionLayer.addChild(widget.getSelectionWidget());
        
        addObject(de, widget);
    }
    
    public void rebuildDocument()
    {
        boolean oldUpdateingStatus = isUpdatingView();
        try {
            setUpdatingView(true);
            
            pageLayer.removeChildren();
            elementsLayer.removeChildren();
            cellSeparatorsLayer.removeChildren();
            selectionLayer.removeChildren();
            backgroundLayer.removeChildren();
            interractionLayer.removeChildren();

            // Remove all the objects...
            while (getObjects().size() > 0)
            {
                removeObject(getObjects().iterator().next());
            }

            if (jasperDesign == null) return;

            getDesignCrosstab().preprocess();

            if (crosstabWidget == null)
            {
                crosstabWidget = new CrosstabWidget(this, getDesignCrosstab());
            }
            else
            {
                crosstabWidget.updateBounds();
            }


            pageLayer.addChild(getCrosstabWidget());

            refreshCells();
        
        } finally {
        
            setUpdatingView(oldUpdateingStatus);
        }
    }
    
    /**
     * Returns a boolean array which says for each column if it exists or not.
     * The only case a column does not exists is when a column group total position
     * is set to none.
     * @return
     */
    public boolean[] columnExistsArray()
    {
        JRCrosstabRowGroup[] row_groups = getDesignCrosstab().getRowGroups();
        JRCrosstabColumnGroup[] col_groups = getDesignCrosstab().getColumnGroups();

        boolean[] cols = new boolean[row_groups.length + col_groups.length+1];
        Arrays.fill(cols, true);

        int w = col_groups.length+1;
        int currentX = row_groups.length;

        for (JRCrosstabColumnGroup colGroup : col_groups)
        {
            if (colGroup.getTotalPositionValue() == CrosstabTotalPositionEnum.START)
            {
                currentX++;
            }
            else if (colGroup.getTotalPositionValue() == CrosstabTotalPositionEnum.NONE)
            {
                // The current last column does not exists...
                cols[currentX+w-1]=false;
            }
            w--;
        }

//        for (boolean b : cols)
//        {
//            System.out.print( ((b) ? "-" : "X"));
//        }
//        System.out.println();

        return cols;


    }


    /**
     * Returns a boolean array which says for each column if it exists or not.
     * The only case a column does not exists is when a column group total position
     * is set to none.
     * @return
     */
    public boolean[] rowExistsArray()
    {
        JRCrosstabRowGroup[] row_groups = getDesignCrosstab().getRowGroups();
        JRCrosstabColumnGroup[] col_groups = getDesignCrosstab().getColumnGroups();

        boolean[] rows = new boolean[row_groups.length + col_groups.length+1];
        Arrays.fill(rows, true);

        int h = row_groups.length+1;
        int currentY = col_groups.length;

        for (JRCrosstabRowGroup rowGroup : row_groups)
        {
            if (rowGroup.getTotalPositionValue() == CrosstabTotalPositionEnum.START)
            {
                currentY++;
            }
            else if (rowGroup.getTotalPositionValue() == CrosstabTotalPositionEnum.NONE)
            {
                // The current last column does not exists...
                rows[currentY+h-1]=false;
            }
            h--;
        }

//        for (boolean b : cols)
//        {
//            System.out.print( ((b) ? "-" : "X"));
//        }
//        System.out.println();

        return rows;


    }


    /**
     * Refresh the bands, adding the missing elements if required.
     * Elements no longer referenced in the model are not removed by this method.
     * 
     */
    public void refreshCells()
    {
        // Remove all the cell separators...
        List<Widget> bWidgets = cellSeparatorsLayer.getChildren();
        cellSeparatorsLayer.removeChildren();
        
        getHorizontalSeparators().clear();
        getVerticalSeparators().clear();
        
        // Add a line for each crosstab intersection....
        List<JRDesignCellContents> cellContents = new ArrayList<JRDesignCellContents>();
        cellContents.add( (JRDesignCellContents)getDesignCrosstab().getHeaderCell() );

        JRCrosstabRowGroup[] row_groups = getDesignCrosstab().getRowGroups();
        JRCrosstabColumnGroup[] col_groups = getDesignCrosstab().getColumnGroups();

        JRCrosstabCell[][] cells = ModelUtils.normalizeCell(getDesignCrosstab().getCells(),row_groups,col_groups);
        
        int current_x = 0;
        for (int i=0; i<row_groups.length; ++i)
        {
            current_x += row_groups[i].getHeader().getWidth();
            getVerticalSeparators().add(current_x);
            
            cellContents.add( (JRDesignCellContents)row_groups[i].getHeader());
            if (row_groups[i].getTotalPositionValue() !=  CrosstabTotalPositionEnum.NONE )
            {
                cellContents.add( (JRDesignCellContents)row_groups[i].getTotalHeader());
            }
        }
        for (int i=cells[0].length-1; i>=0; --i)
        {
            current_x += ModelUtils.findColumnWidth(cells, i);
            getVerticalSeparators().add(current_x);
        }
        
        int current_y = 0;
        for (int i=0; i<col_groups.length; ++i)
        {
            current_y += col_groups[i].getHeader().getHeight();
            getHorizontalSeparators().add(current_y);
        
            cellContents.add( (JRDesignCellContents)col_groups[i].getHeader());
            if (col_groups[i].getTotalPositionValue() !=  CrosstabTotalPositionEnum.NONE )
            {
                cellContents.add( (JRDesignCellContents)col_groups[i].getTotalHeader());
            }
        }
        for (int i=cells.length-1; i>=0; --i)
        {
            current_y += ModelUtils.findRowHeight(cells, i);
            getHorizontalSeparators().add(current_y);
        }
        
        // Check for the crosstab widget....
        boolean[] rowsExists = rowExistsArray();
        for (int i=0; i<getHorizontalSeparators().size();++i)
        {
            if (rowsExists[i])
            {
                CellSeparatorWidget w = new CellSeparatorWidget(this, i, Orientation.HORIZONTAL);
                w.getActions().addAction( new CellSeparatorMoveAction(true, InputEvent.SHIFT_DOWN_MASK) );
                w.getActions().addAction( new CellSeparatorMoveAction() );
                cellSeparatorsLayer.addChild(w);
            }
        }

        boolean[] colsExists = columnExistsArray();
        for (int i=0; i<getVerticalSeparators().size();++i)
        {
            // If this separator is tied to a column which actually does not exists, just remove it.
            // This happen when this is the end column of a group, which has the total position set to NONE...
            // Which group? Let's find out...
            if (colsExists[i])
            {
                CellSeparatorWidget w = new CellSeparatorWidget(this, i, Orientation.VERTICAL);
                w.getActions().addAction( new CellSeparatorMoveAction(true, InputEvent.SHIFT_DOWN_MASK) );
                w.getActions().addAction( new CellSeparatorMoveAction() );
                cellSeparatorsLayer.addChild(w);
            }
        }
        
        
        for (int i=cells.length-1; i>=0; --i)
        {
            for (int j=cells[i].length-1; j>=0; --j)
            {
                if (cells[i][j] != null)
                {
                    cellContents.add( (JRDesignCellContents)cells[i][j].getContents());
                }
            }
        }
        
        List<Object> toBeRemovedObjects = new ArrayList<Object>();
        List<Widget> toBeRemoved = new ArrayList<Widget>();
        List<Widget> toBeRemovedSelection = new ArrayList<Widget>();
        
       
        for (Iterator iter = getObjects().iterator(); iter.hasNext(); )
        {
            // Remove just the orphan elements...
            Object obj = iter.next();
            if (obj instanceof JRDesignElement && ModelUtils.isOrphan((JRDesignElement)obj, getJasperDesign()))
            {
                // remove object and relative widgets...
                Widget w = findWidget(obj);
                if (w != null)
                {
                    toBeRemoved.add(w);
                    toBeRemovedSelection.add(((JRDesignElementWidget)w).getSelectionWidget());
                    //elementsLayer.removeChild(w);
                    //selectionLayer.removeChild(((JRDesignElementWidget)w).getSelectionWidget());
                }
                toBeRemovedObjects.add(obj);
            }
        }

        for (Object obj : toBeRemovedObjects)
        {
            removeObject(obj);
        }
        
        elementsLayer.removeChildren(toBeRemoved);
        selectionLayer.removeChildren(toBeRemovedSelection);
        
        // Add the elements to the cells...
        for (JRDesignCellContents content : cellContents)
        {
            if (content != null)
            {
                addElements( content.getChildren() );
            }
        }
        
        // Remove orphans widgets...
        
        List<Widget> widgets = elementsLayer.getChildren();
        
//        List<Widget> toBeRemoved = new ArrayList<Widget>();
//        List<Widget> toBeRemovedSelection = new ArrayList<Widget>();
//        for (Widget w : widgets)
//        {
//            if (w instanceof JRDesignElementWidget)
//            {
//                JRDesignElementWidget dew = (JRDesignElementWidget)w;
//                
//                if (ModelUtils.isOrphan(dew.getElement()))
//                {                    
//                    toBeRemoved.add(w);
//                    toBeRemovedSelection.add( ((JRDesignElementWidget)w).getSelectionWidget() );
//                }                
//            }
//            else
//            {
//               
//            }
//            /*
//            if (w instanceof JRDesignElementWidget)
//            {
//                JRDesignElement de = ((JRDesignElementWidget)w).getElement();
//                if (ModelUtils.isOrphan(de))
//                {
//                    if (getObjects().contains(de))
//                    {
//                        // we need to remove this widget..
//                        removeObject(de);
//                    }
//                    else
//                    {
//                        
//                    }
//                }
//            }
//            */
//        }
        
        
        
        
        validate();
        
        /*
        for (int i=cells.length-1; i>=0; --i)
        {
            for (int k=cells[i].length-1; k>=0; --k)
            {
                if (!horizontalSeparators.contains(current_y))
                {
                    horizontalSeparators.add(current_y);
                }
                current_y += cells[i][j].getContents().getHeight();
            }
            
            if (!verticalSeparators.contains(current_x))
            {
                verticalSeparators.add(current_x);
            }
            current_x += cells[i][j].getContents().getWidth();
                
        }
        */
        
        
        
        /*
        int yLocation = getJasperDesign().getTopMargin();
        
        // Reset selection (just in case...)
        if (getSelectedObjects().size() > 0)
        {
            setSelectedObjects( Collections.EMPTY_SET );
        }
        
        List<JRBand> bands = ModelUtils.getBands(getJasperDesign());
            
        for (JRBand b : bands)
        {
            yLocation += b.getHeight();
            addBandSeparatorWidget(b, yLocation);
            
            // Add the elements inside this band...
            //JRElement[] elements = b.getElements();
            addElements( b.getChildren());
        }
        
        // Remove the elements belonging to bands that are no longer
        // in the model...
        List<Widget> widgets = elementsLayer.getChildren();
        List<Widget> toBeRemoved = new ArrayList<Widget>();
        List<Widget> toBeRemovedSelection = new ArrayList<Widget>();
        List<Widget> toBeRemovedObject = new ArrayList<Widget>();
        for (Widget widget : widgets)
        {
            if (widget instanceof JRDesignElementWidget)
            {
                JRDesignElementWidget dew = (JRDesignElementWidget)widget;
                JRElementGroup grp = ModelUtils.getTopElementGroup(dew.getElement());
                if (!bands.contains(grp))
                {
                    // This band (top group) is not longer in the mode...
                    this.removeObject(dew.getElement());
                    toBeRemoved.add(widget);
                    toBeRemovedSelection.add(dew.getSelectionWidget());
                }
            }
        }
        if (toBeRemoved.size() > 0)
        {
            elementsLayer.removeChildren(toBeRemoved);
            selectionLayer.removeChildren(toBeRemovedSelection);
            
        }
        
         */
    }
    
    @SuppressWarnings("unchecked")
    private void addElements(List children)
    {
        for (int i=0; i<children.size(); ++i)
            {
                Object obj = children.get(i);
                if (obj instanceof JRDesignElementGroup)
                {
                    if (!elementGroupListeners.containsKey(obj))
                    {
                        GroupChangeListener gcl = new GroupChangeListener((JRDesignElementGroup)obj);
                        ((JRDesignElementGroup)obj).getEventSupport().addPropertyChangeListener(gcl);
                        elementGroupListeners.put(obj,gcl);
                    }
                    
                    addElements( ((JRDesignElementGroup)obj).getChildren() );
                }
            
                if (obj instanceof JRDesignElement)
                {
                    JRDesignElement de = (JRDesignElement)children.get(i);
                    JRDesignElementWidget w = (JRDesignElementWidget)findWidget(de);
                    
                    if (w != null)
                    {
                        w.updateBounds();
                        //if (getObjects().contains(de))
                        //{
                        //    addObject(de, w);
                        //}
                    }
                    else
                    {
                        addElementWidget(de);
                    }

                    if (de instanceof JRDesignFrame)
                    {
                        addElements( ((JRDesignFrame)de).getChildren() );
                    }
                }
            }
    }
    
    public JRDesignElementWidget findElementWidget(JRDesignElement de)
    {
        List<Widget> widgets = elementsLayer.getChildren();
        
        for (Widget w : widgets)
        {
            if (w instanceof JRDesignElementWidget)
            {
                JRDesignElementWidget dew = (JRDesignElementWidget)w;
                if (dew.getElement() == de) return dew;
            }
        }
        return null;
    }
    
    /**
     * This methods update the order and all the elements of a band/cell, removing
     * elements no longer in the band...
     * The selection is preserved.
     */
    @SuppressWarnings("unchecked")
    public void refreshElementGroup(JRElementGroup group)
    {
//         boolean oldUpdateingStatus = isUpdatingView();
//        try {
//            setUpdatingView(true);
//        
//            HashSet selectedObjects = new HashSet();
//            selectedObjects.addAll(  getSelectedObjects() );
//            if (selectedObjects.size() == 0) selectedObjects = null;
//
//            List<Widget> children = getElementsLayer().getChildren();
//
//            String groupName = "?";
//            if (group instanceof JRDesignCellContents)
//            {
//                groupName = ModelUtils.nameOf((JRDesignCellContents)group);
////                System.out.println("Children of this cell conents... ");
////                for (int x=0;x<group.getChildren().size(); ++x)
////                {
////                    System.out.println("   child["+x+"] " + group.getChildren().get(x) + " Parent..." + ((JRDesignElement)group.getChildren().get(x)).getElementGroup());
////                }
//            }
//
//            // 1. remove all the widget referencing this cell
//            List<JRDesignElementWidget> toRemove = new java.util.ArrayList<JRDesignElementWidget>();
//            for (Widget w : children)
//            {
//                if (w instanceof JRDesignElementWidget)
//                {
//                    JRDesignElementWidget dw = (JRDesignElementWidget)w;
//
//                    // Please note that the element can belong to a sub group ...
//                    if (ModelUtils.isOrphan(dw.getElement()) || 
//                        ModelUtils.isElementChildOf(dw.getElement(),group) )
//                    {
//
//
//                        toRemove.add(dw);
//                        JRDesignElement element = dw.getElement();
//                        // check if the element is still valid...
//                        if (selectedObjects != null && selectedObjects.contains(element))
//                        {
//                            // Check if the element is still there...
//                            boolean found = false;
//                            JRElement[] ele = group.getElements();
//                            for (int i=0; i<ele.length; ++i)
//                            {
//                                if (ele[i] == element)
//                                {
//                                    found = true;
//                                    break;
//                                }
//                            }
//                            // The element is no longer in the model, remove it.
//                            if (!found)
//                            {
//                                selectedObjects.remove(element);
//                            }
//                        }
//                    }
//                }
//            }
//
//            for (JRDesignElementWidget dw : toRemove)
//            {
//                dw.getSelectionWidget().removeFromParent();
//                dw.removeFromParent();
//                if (getObjects().contains(dw.getElement()))
//                {
//                    removeObject(dw.getElement());
//                }
//            }
//
//            // 2. add the new children...
//            //JRElement[] elements = group.getElements();
//            addElements( group.getChildren() );
//
//            // Update all the report elements children...
//            if (selectedObjects != null)
//                setSelectedObjects( selectedObjects);
//            validate();
//
//        } finally {
//        
//            setUpdatingView(oldUpdateingStatus);
//        }
    }
       
    
    /**
     *  Layer used for the background (in the future this layer can be used to place
     *  watermarks to help the user to design the report.
     */
    LayerWidget backgroundLayer = null;

    public LayerWidget getBackgroundLayer() {
        return backgroundLayer;
    }

    public LayerWidget getPageLayer() {
        return pageLayer;
    }

    

    public LayerWidget getInterractionLayer() {
        return interractionLayer;
    }
    
    /**
     * This layer is used for the main page widget and the bands.
     */
    LayerWidget pageLayer = null;
    
    /**
     * This layer is used for interactions
     */
    LayerWidget interractionLayer  = null;

    /**
     * This layer is used for bandBorders widgets
     */
    LayerWidget cellSeparatorsLayer  = null;
    
    public LayerWidget getCellSeparatorsLayer() {
        return cellSeparatorsLayer;
    }

    public void setCellSeparatorsLayer(LayerWidget cellSeparatorsLayer) {
        this.cellSeparatorsLayer = cellSeparatorsLayer;
    }
    
    
    public CrosstabObjectScene(JRDesignCrosstab ct, JasperDesign jd) {
        super();
        this.jasperDesign = jd;
        this.designCrosstab = ct;
        initScene();
        
    }
    
    private void initScene()
    {
        // I like to see a gray background. The default background for a JComponent is fine.
        this.setBackground(   UIManager.getColor("Panel.background")  );
        setKeyEventProcessingType(EventProcessingType.FOCUSED_WIDGET_AND_ITS_PARENTS);
        
        backgroundLayer = new LayerWidget(this);
        addChild(backgroundLayer);
        
        pageLayer = new LayerWidget(this);
        addChild(pageLayer);
        
        elementsLayer = new LayerWidget(this);
        addChild(elementsLayer);
        
        cellSeparatorsLayer = new LayerWidget(this);
        addChild(cellSeparatorsLayer);
        
        selectionLayer  = new LayerWidget(this);
        addChild(selectionLayer );
        
        guideLinesLayer  = new LayerWidget(this);
        addChild(guideLinesLayer );
        
        interractionLayer  = new LayerWidget(this);
        addChild(interractionLayer );
        
        reportAlignWithWidgetCollector = new ReportAlignWithWidgetCollector(this);
        reportAlignWithMoveStrategyProvider = new ReportAlignWithMoveStrategyProvider(reportAlignWithWidgetCollector, interractionLayer, ALIGN_WITH_MOVE_DECORATOR_DEFAULT, false);
        reportAlignWithResizeStrategyProvider  = new ReportAlignWithResizeStrategyProvider(reportAlignWithWidgetCollector, interractionLayer, ALIGN_WITH_MOVE_DECORATOR_DEFAULT, false);
                
        cellSelectionAction = new CellSelectionAction();
        
        getActions().addAction(reportPopupMenuAction);
        getActions().addAction(cellSelectionAction);
        
        getActions().addAction(ActionFactory.createRectangularSelectAction(
                new TranslucentRectangularSelectDecorator(this), interractionLayer,
                ActionFactory.createObjectSceneRectangularSelectProvider(this)));
        
        
        
        getActions().addAction (ActionFactory.createMouseCenteredZoomAction (1.1));
        getActions().addAction (ActionFactory.createPanAction ());
        
        
        keyboardElementMoveAction = new KeyboardElementMoveAction();
        getActions().addAction( keyboardElementMoveAction );
        
        this.setMaximumBounds(new Rectangle(-10,-10,Integer.MAX_VALUE, Integer.MAX_VALUE) );
        this.drawVisitor = new ConfigurableDrawVisitor(getJasperDesign(), null);
        
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                rebuildDocument();
            }
        });
        
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        
        //System.out.println("Model changed: " + evt.getPropertyName() + " " + evt.getSource());
           /* 
        Runnable r = null;
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals(JasperDesign.PROPERTY_BACKGROUND) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_TITLE) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_PAGE_HEADER) ||    
            evt.getPropertyName().equals(JasperDesign.PROPERTY_COLUMN_HEADER) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_DETAIL) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_COLUMN_FOOTER) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_PAGE_FOOTER) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_LAST_PAGE_FOOTER) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_SUMMARY) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_NO_DATA) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_PAGE_WIDTH) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_PAGE_HEIGHT) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_TOP_MARGIN) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_BOTTOM_MARGIN) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_LEFT_MARGIN) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_RIGHT_MARGIN) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_COLUMN_COUNT) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_COLUMN_SPACING) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_COLUMN_WIDTH) ||
            evt.getPropertyName().equals(JRDesignGroup.PROPERTY_GROUP_HEADER) ||
            evt.getPropertyName().equals(JRDesignGroup.PROPERTY_GROUP_FOOTER) ||
            evt.getPropertyName().equals(JRDesignDataset.PROPERTY_GROUPS))
        {
             r = new Runnable(){  
                 public void run()  {
                    rebuildDocument();
                }};
        }
        
        
        if (r != null)
        {
           ThreadUtils.invokeInAWTThread(r);
        }
        
        // Update group listeners...
        if (evt.getPropertyName().equals(JRDesignDataset.PROPERTY_GROUPS))
        {
            // refresh group listening...
            for (int i=0; i<this.jasperDesign.getGroupsList().size(); ++i)
            {
                JRDesignGroup grp = (JRDesignGroup)this.jasperDesign.getGroupsList().get(i);
                grp.getEventSupport().removePropertyChangeListener(this);
                grp.getEventSupport().addPropertyChangeListener(this);
            }
        }
            */
    }

    public JRDesignCrosstab getDesignCrosstab() {
        return designCrosstab;
    }

    public void setDesignCrosstab(JRDesignCrosstab designCrosstab) {
        this.designCrosstab = designCrosstab;
    }

    public CrosstabWidget getCrosstabWidget() {
        return crosstabWidget;
    }

    public

    List<Integer> getVerticalSeparators() {
        return verticalSeparators;
    }

    public List<Integer> getHorizontalSeparators() {
        return horizontalSeparators;
    }
    
    
    
    @Override
    public boolean acceptDropAt(Point location)
    {
        Point p = convertViewToScene(location);

        JRDesignCrosstab crosstab = getDesignCrosstab();
        List<CellInfo> cells = ModelUtils.getCellInfos(crosstab);
        int crosstabWidth = 0;
        int crosstabHeight = 0;

        for (int i=0; i<cells.size(); ++i)
        {
            CellInfo ci = cells.get(i);

            int thisW = ci.getLeft() + ci.getCellContents().getWidth();
            if (thisW > crosstabWidth) crosstabWidth = thisW;

            int thisH = ci.getTop() + ci.getCellContents().getHeight();
            if (thisH > crosstabHeight) crosstabHeight = thisH;
        }
        return (new Rectangle(0,0,crosstabWidth, crosstabHeight).contains(p));
    }
    
}

