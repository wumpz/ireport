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
package com.jaspersoft.ireport.designer;

import com.jaspersoft.ireport.designer.actions.BandDblClickResizeAction;
import com.jaspersoft.ireport.designer.actions.BandMoveAction;
import com.jaspersoft.ireport.designer.actions.BandSelectionAction;
import com.jaspersoft.ireport.designer.actions.ExMoveAction;
import com.jaspersoft.ireport.designer.actions.KeyboardElementMoveAction;
import com.jaspersoft.ireport.designer.actions.ReportAlignWithMoveStrategyProvider;
import com.jaspersoft.ireport.designer.actions.ReportAlignWithResizeStrategyProvider;
import com.jaspersoft.ireport.designer.actions.ReportAlignWithWidgetCollector;
import com.jaspersoft.ireport.designer.actions.TranslucentRectangularSelectDecorator;
import com.jaspersoft.ireport.designer.widgets.BandSeparatorWidget;
import com.jaspersoft.ireport.designer.widgets.BandWidget;
import com.jaspersoft.ireport.designer.widgets.JRDesignChartWidget;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import com.jaspersoft.ireport.designer.widgets.JRDesignImageWidget;
import com.jaspersoft.ireport.designer.widgets.PageWidget;
import com.jaspersoft.ireport.designer.widgets.visitor.ConfigurableDrawVisitor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javax.swing.UIManager;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.convert.ReportConverter;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.draw.DrawVisitor;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.widget.EventProcessingType;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class ReportObjectScene extends AbstractReportObjectScene implements PropertyChangeListener {

    private List<JRDesignElement> selectedElements = new ArrayList<JRDesignElement>();
    
    private ReportAlignWithWidgetCollector reportAlignWithWidgetCollector = null;
    private ReportAlignWithMoveStrategyProvider reportAlignWithMoveStrategyProvider = null;
    private ReportAlignWithResizeStrategyProvider reportAlignWithResizeStrategyProvider = null;
    private KeyboardElementMoveAction keyboardElementMoveAction = null;
    private BandSelectionAction bandSelectionAction = null;
    

    
    
    @Override
    public void setSnapToGrid(boolean snapToGrid) {
        if (this.snapToGrid != snapToGrid)
        {
            this.snapToGrid = snapToGrid;
            this.reportAlignWithMoveStrategyProvider.setSnapToGrid(snapToGrid);
            this.reportAlignWithResizeStrategyProvider.setSnapToGrid(snapToGrid);
        }
    }

    @Override
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
            
            jasperDesign.getEventSupport().addPropertyChangeListener(this);
            updateSectionListeners();

            this.drawVisitor = 
                    new ConfigurableDrawVisitor(jasperDesign, null);
            ThreadUtils.invokeInAWTThread(new Runnable() {

                public void run() {
                    rebuildDocument();
                }
            });
        
        
    }

    public void updateSectionListeners()
    {
        ((JRDesignSection)getJasperDesign().getDetailSection()).getEventSupport().removePropertyChangeListener(this);
        ((JRDesignSection)getJasperDesign().getDetailSection()).getEventSupport().addPropertyChangeListener(this);


        for (int i=0; i<getJasperDesign().getGroupsList().size(); ++i)
        {
            JRDesignGroup grp = (JRDesignGroup)getJasperDesign().getGroupsList().get(i);
            grp.getEventSupport().removePropertyChangeListener(this);
            grp.getEventSupport().addPropertyChangeListener(this);
            if (((JRDesignSection)grp.getGroupHeaderSection() != null))
            {
                ((JRDesignSection)grp.getGroupHeaderSection()).getEventSupport().removePropertyChangeListener(this);
                ((JRDesignSection)grp.getGroupHeaderSection()).getEventSupport().addPropertyChangeListener(this);
            }
            if (((JRDesignSection)grp.getGroupFooterSection() != null))
            {
                ((JRDesignSection)grp.getGroupFooterSection()).getEventSupport().removePropertyChangeListener(this);
                ((JRDesignSection)grp.getGroupFooterSection()).getEventSupport().addPropertyChangeListener(this);
            }
        }
    }
    
    public void addBandSeparatorWidget(JRBand b, int yLocation)
    {
        if (b == null) return;

        if (b instanceof JRDesignBand &&
            ((JRDesignBand)b).getOrigin().getBandTypeValue() == BandTypeEnum.BACKGROUND)
        {
            ((JRDesignBand)b).getEventSupport().removePropertyChangeListener(JRDesignBand.PROPERTY_HEIGHT, this);
            if (IReportManager.getInstance().isBackgroundSeparated() && b.getHeight() == 0)
            {
                ((JRDesignBand)b).getEventSupport().addPropertyChangeListener(JRDesignBand.PROPERTY_HEIGHT, this);
                return;
            }
        }
        
        BandSeparatorWidget bbw = new BandSeparatorWidget(this, b);
        bbw.getActions().addAction( new BandMoveAction(true, InputEvent.SHIFT_DOWN_MASK) );
        bbw.getActions().addAction( new BandMoveAction() );
        bbw.getActions().addAction( new BandDblClickResizeAction());
        bandSeparatorsLayer.addChild(bbw);
        bandLayer.addChild(new BandWidget(this, b));
    }
    
    public JRDesignElementWidget addElementWidget(JRDesignElement de)
    {
        if (de == null) return null;
        JRDesignElementWidget widget = null;

        if (de instanceof JRDesignComponentElement)
        {
            widget = IReportManager.getComponentWidget(this, (JRDesignComponentElement)de);
        }
        else if (de instanceof JRDesignImage)
        {
            widget = new JRDesignImageWidget(this, (JRDesignImage)de);
        }
        else if (de instanceof JRDesignChart)
        {
            widget = new JRDesignChartWidget(this, (JRDesignChart)de);
        }

        if (widget == null) // Default...
        {
            widget = new JRDesignElementWidget(this, de);
        }
        //widget.getSelectionWidget().setLayout(new ResizeHandleLayout());
        //widget.getSelectionWidget().getActions().addAction(createSelectAction());
        
        widget.getActions().addAction (getReportSelectAction());
        
        widget.getSelectionWidget().getActions().addAction( keyboardElementMoveAction );

        widget.getSelectionWidget().getActions().addAction (getReportSelectAction());
        widget.getSelectionWidget().getActions().addAction(createObjectHoverAction());
        
        
        widget.getSelectionWidget().getActions().addAction( ActionFactory.createResizeAction(
                        reportAlignWithResizeStrategyProvider, 
                        reportAlignWithResizeStrategyProvider) );
        
        widget.getSelectionWidget().getActions().addAction(
                    new ExMoveAction(reportAlignWithMoveStrategyProvider, reportAlignWithMoveStrategyProvider));

//          widget.getSelectionWidget().getActions().addAction(
//                  ActionFactory.createMoveAction(
//                        reportAlignWithMoveStrategyProvider,
//                        reportAlignWithMoveStrategyProvider));
        
        widget.getActions().addAction( ActionFactory.createResizeAction(
                        reportAlignWithResizeStrategyProvider, 
                        reportAlignWithResizeStrategyProvider) );

        widget.getActions().addAction(
                    new ExMoveAction(reportAlignWithMoveStrategyProvider, reportAlignWithMoveStrategyProvider));

//        widget.getActions().addAction( ActionFactory.createMoveAction(
//                        reportAlignWithMoveStrategyProvider,
//                        reportAlignWithMoveStrategyProvider) );
        
        widget.getActions().addAction(inplaceEditorAction);
        widget.getSelectionWidget().getActions().addAction(inplaceEditorAction);
        
        widget.getActions ().addAction(elementPopupMenuAction);
        widget.getSelectionWidget().getActions().addAction(elementPopupMenuAction);

        widget.getActions().addAction( ActionFactory.createActionMapAction(elementInputMap, elementActionMap) );
        widget.getSelectionWidget().getActions().addAction( ActionFactory.createActionMapAction(elementInputMap, elementActionMap) );


        elementsLayer.addChild(widget);
        selectionLayer.addChild(widget.getSelectionWidget());
        
        addObject(de, widget);

        return widget;
    }
    
    public void rebuildDocument()
    {
        
        boolean oldUpdateingStatus = isUpdatingView();
        try {
            setUpdatingView(true);

            pageLayer.removeChildren();
            elementsLayer.removeChildren();
            bandLayer.removeChildren();
            bandSeparatorsLayer.removeChildren();
            selectionLayer.removeChildren();
            backgroundLayer.removeChildren();
            interractionLayer.removeChildren();

            // Remove all the objects...
            while (getObjects().size() > 0)
            {
                removeObject(getObjects().iterator().next());
            }

            if (jasperDesign == null) return;

            PageWidget pageWidget = new PageWidget(this);


            pageLayer.addChild(pageWidget);

            refreshBands();
            
        } finally {
        
            setUpdatingView(oldUpdateingStatus);
        }
    }
    
    
    public void  refreshDocument()
    {
        if (pageLayer.getChildren().size() != 0)
        {
            PageWidget pageWidget = (PageWidget)pageLayer.getChildren().get(0);
            pageWidget.updateBounds();
            pageWidget.repaint();
            refreshBands();
        }
    }
    /**
     * Refresh the bands, adding the missing elements if required.
     * Elements no longer referenced in the model are not removed by this method.
     * 
     */
    public void refreshBands()
    {
        boolean oldUpdateingStatus = isUpdatingView();
        try {
            setUpdatingView(true);
            
            int yLocation = getJasperDesign().getTopMargin();

            // Remove all the band separators, and remove the listening too...
            List<Widget> bWidgets = bandSeparatorsLayer.getChildren();
            for (Widget w : bWidgets)
            {
                ((JRDesignBand)((BandSeparatorWidget)w).getBand()).getEventSupport().removePropertyChangeListener((BandSeparatorWidget)w);
            }

            // Remove all the band BandWidget, and remove the listening too...
            bWidgets = bandLayer.getChildren();
            for (Widget w : bWidgets)
            {
                ((JRDesignBand)((BandWidget)w).getBand()).getEventSupport().removePropertyChangeListener((BandWidget)w);
            }

            bandSeparatorsLayer.removeChildren();
            bandLayer.removeChildren();

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
                        // This element could still be in the model holded by a special
                        // component... we should check first in all the custom components...
                        boolean remove = true;

                        JRDesignElementWidget owner = findCustomComponentOwner(dew.getElement());
                        if (owner != null)
                        {
                            JRElementGroup grp2 = ModelUtils.getTopElementGroup(owner.getElement());
                            if (bands.contains(grp2)) remove = false;
                        }

                        if (remove)
                        {
                            // This band (top group) is not longer in the mode...
                            this.removeObject(dew.getElement());
                            toBeRemoved.add(widget);
                            toBeRemovedSelection.add(dew.getSelectionWidget());
                        }
                    }
                }
            }
            if (toBeRemoved.size() > 0)
            {
                elementsLayer.removeChildren(toBeRemoved);
                selectionLayer.removeChildren(toBeRemovedSelection);

            }
            validate();
        
        } finally {
            setUpdatingView(oldUpdateingStatus);
        }
    }

    public JRDesignElementWidget findCustomComponentOwner(JRDesignElement element)
    {
        while (element.getElementGroup() != null &&
               element.getElementGroup() instanceof JRDesignFrame)
        {
            element = (JRDesignFrame)element.getElementGroup();
        }

        List<Widget> widgets = elementsLayer.getChildren();
        for (Widget widget : widgets)
        {
            if (widget instanceof JRDesignElementWidget)
            {
                    JRDesignElementWidget dew = (JRDesignElementWidget)widget;
                    if (dew.getElement() instanceof JRDesignComponentElement &&
                        dew.getChildrenElements() != null && dew.getChildrenElements().contains(element))
                    {
                        return dew;
                    }
            }
        }
        return null;
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
                    JRDesignElement de = (JRDesignElement)obj;
                    JRDesignElementWidget w = findElementWidget(de);
                    if (w != null)
                    {
                        w.updateBounds();
                        // put it at the end of the list of childrens...
                        w.bringToFront();
                    }
                    else
                    {
                        w  = addElementWidget(de);
                    }

                    // If the widget has childrens, add them here automatically....
                    if (w != null && w.getChildrenElements() != null)
                    {
                        addElements(w.getChildrenElements());
                    }

                }
            }
    }
    
    /**
     * This method replace the findWidget method. It does not any assumption
     * on the content of the objects and their relations in the object scene.
     * 
     * @param de
     * @return
     */
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
     * This methods update the order and all the elements of a band, removing
     * elements no longer in the band...
     * The selection is preserved.
     */
    @SuppressWarnings("unchecked")
    public void refreshElementGroup(JRElementGroup group)
    {
        boolean oldUpdateingStatus = isUpdatingView();
        try {
            setUpdatingView(true);
            
            HashSet selectedObjects = new HashSet();
            selectedObjects.addAll(  getSelectedObjects() );
            if (selectedObjects.size() == 0) selectedObjects = null;

            List<Widget> children = getElementsLayer().getChildren();

            // 1. remove all the widget referencing this band
            List<JRDesignElementWidget> toRemove = new java.util.ArrayList<JRDesignElementWidget>();
            for (Widget w : children)
            {
                if (w instanceof JRDesignElementWidget)
                {
                    JRDesignElementWidget dw = (JRDesignElementWidget)w;

                    // Please note that the element can belong to a sub group ...
                    if (ModelUtils.isOrphan(dw.getElement(), getJasperDesign()) )
                    {
                        // Fix Giulio (it does not make sense to remove all the group elements
                        // all the time. Add elements will take care to correctly discover
                        // if an element is already present in the band and will update it...
                        //ModelUtils.isElementChildOf(dw.getElement(),group) )
                        // Re Giulio: this would not take care of an order change...
                        boolean remove = true;
                        // Check if the element belong to a custom component...
                        JRDesignElementWidget owner = findCustomComponentOwner(dw.getElement());
                        if (owner != null)
                        {
                            remove = ModelUtils.isOrphan(owner.getElement(), getJasperDesign());
                        }

                        if (remove)
                        {
                            toRemove.add(dw);
                            JRDesignElement element = dw.getElement();
                            // check if the element is still valid...
                            if (selectedObjects != null && selectedObjects.contains(element))
                            {
                                // Check if the element is still there...
                                boolean found = false;
                                JRElement[] ele = group.getElements();
                                for (int i=0; i<ele.length; ++i)
                                {
                                    if (ele[i] == element)
                                    {
                                        found = true;
                                        break;
                                    }
                                }
                                // The element is no longer in the model, remove it.
                                if (!found)
                                {
                                    selectedObjects.remove(element);
                                }
                            }
                        }
                    }
                }
            }

            for (JRDesignElementWidget dw : toRemove)
            {
                dw.getSelectionWidget().removeFromParent();
                dw.removeFromParent();
                if (getObjects().contains(dw.getElement()))
                {
                    //System.out.println("Removing object2: " + dw.getElement());
                   // System.out.flush();
                    removeObject(dw.getElement());
                }
            }

            // 2. add the new children...
            //JRElement[] elements = group.getElements();
            addElements( group.getChildren() );
            
            // We should reorder the children in case the update is just about
            // the z-order...




            // Update all the report elements children...
            if (selectedObjects != null)
                setSelectedObjects( selectedObjects);
            validate();
        
        
        } finally {
            setUpdatingView(oldUpdateingStatus);
        }

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
    LayerWidget bandSeparatorsLayer  = null;
    
    /**
     * This layer is used for band widgets (tooltips)
     */
    LayerWidget bandLayer  = null;
    
    public LayerWidget getBandSeparatorsLayer() {
        return bandSeparatorsLayer;
    }

    public void setBandSeparatorsLayer(LayerWidget bandSeparatorsLayer) {
        this.bandSeparatorsLayer = bandSeparatorsLayer;
    }
    
    public LayerWidget getBandLayer() {
        return bandLayer;
    }

    public void setBandLayer(LayerWidget bandLayer) {
        this.bandLayer = bandLayer;
    }
    
    public ReportObjectScene() {
        super();
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
        
        bandLayer = new LayerWidget(this);
        addChild(bandLayer);
        
        bandSeparatorsLayer = new LayerWidget(this);
        addChild(bandSeparatorsLayer);
        
        selectionLayer  = new LayerWidget(this);
        addChild(selectionLayer );
        
        guideLinesLayer  = new LayerWidget(this);
        addChild(guideLinesLayer );
        
        interractionLayer  = new LayerWidget(this);
        addChild(interractionLayer );
        
        reportAlignWithWidgetCollector = new ReportAlignWithWidgetCollector(this);
        reportAlignWithMoveStrategyProvider = new ReportAlignWithMoveStrategyProvider(reportAlignWithWidgetCollector, interractionLayer, ALIGN_WITH_MOVE_DECORATOR_DEFAULT, false);
        reportAlignWithResizeStrategyProvider  = new ReportAlignWithResizeStrategyProvider(reportAlignWithWidgetCollector, interractionLayer, ALIGN_WITH_MOVE_DECORATOR_DEFAULT, false);
                
        bandSelectionAction = new BandSelectionAction();
        
        getActions().addAction(reportPopupMenuAction);
        getActions().addAction(bandSelectionAction);
        
        getActions().addAction(ActionFactory.createRectangularSelectAction(
                new TranslucentRectangularSelectDecorator(this), interractionLayer,
                ActionFactory.createObjectSceneRectangularSelectProvider(this)));
        
        
        
        getActions().addAction (ActionFactory.createMouseCenteredZoomAction (1.1));
        getActions().addAction (ActionFactory.createPanAction ());
        
        
        keyboardElementMoveAction = new KeyboardElementMoveAction();
        getActions().addAction( keyboardElementMoveAction );
        
        this.setMaximumBounds(new Rectangle(-10,-10,Integer.MAX_VALUE, Integer.MAX_VALUE) );
    }
    
    
    public void propertyChange(PropertyChangeEvent evt) {
        
        //System.out.println("Model changed: " + evt.getPropertyName() + " " + evt.getSource());
            
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
            evt.getPropertyName().equals(JRDesignDataset.PROPERTY_GROUPS) ||
            evt.getPropertyName().equals(JRDesignSection.PROPERTY_BANDS) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_PRINT_ORDER) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_COLUMN_DIRECTION) ||
            // PROPERTY_HEIGHT is used only for the background band when it is detached...
            evt.getPropertyName().equals(JRDesignBand.PROPERTY_HEIGHT))
        {
            updateSectionListeners();
             r = new Runnable(){  
                 public void run()  {
                    refreshDocument();
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
    }
    
    

    @Override
    public boolean acceptDropAt(Point location)
    {
        Point p = convertViewToScene(location);
        return ModelUtils.getBandAt(IReportManager.getInstance().getActiveReport(), p) != null;
    }
    
}

