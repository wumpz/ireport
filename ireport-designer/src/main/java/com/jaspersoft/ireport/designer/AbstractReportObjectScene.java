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

import com.jaspersoft.ireport.designer.actions.ElementF2Action;
import com.jaspersoft.ireport.designer.actions.ReportElementPopupMenuProvider;
import com.jaspersoft.ireport.designer.actions.ReportPopupMenuProvider;
import com.jaspersoft.ireport.designer.actions.ReportTextElementInplaceEditorProvider;
import com.jaspersoft.ireport.designer.actions.SelectAction;
import com.jaspersoft.ireport.designer.ruler.GuideLine;
import com.jaspersoft.ireport.designer.ruler.GuideLineChangedListener;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.widgets.GuideLineWidget;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import com.jaspersoft.ireport.designer.widgets.SelectionWidget;
import com.jaspersoft.ireport.designer.widgets.UnscaledDecoratorWidget;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.draw.DrawVisitor;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.AlignWithMoveDecorator;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public abstract class AbstractReportObjectScene extends ObjectScene implements GuideLineChangedListener {

    public static Color DESIGN_LINE_COLOR = new Color(170,170,255);
    public static Color EDITING_DESIGN_LINE_COLOR = Color.DARK_GRAY;
    public static Color GRID_LINE_COLOR = new Color(230,230,230);

    private ObjectSceneSelectionManager selectionManager = null;
    private WidgetAction reportSelectAction = null;
    

    public AbstractReportObjectScene()
    {
        super();
        selectionManager = new ObjectSceneSelectionManager(this);
        reportSelectAction = createReportSelectAction();
        elementInputMap.put(KeyStroke.getKeyStroke("F2"), "ElementF2Action");
        elementActionMap.put("ElementF2Action", new ElementF2Action());
    }

    public void assureVisible(Object object) {
    
        Widget w = findWidget(object);
        if (w != null)
        {
            
            JComponent view = getView();
            Rectangle visibleRect = view.getVisibleRect();
            
            // be sure the element (0,0) hit the rect...
            Rectangle bounds = w.getBounds();
            bounds.x = w.getLocation().x;
            bounds.y = w.getLocation().y;
            
            Rectangle newRect = convertLocalToScene(bounds);
            
            newRect = convertSceneToView(newRect);
            
            if (visibleRect.width < newRect.width)
            {
                newRect.width = visibleRect.width;
            }
            
            if (visibleRect.height < newRect.height)
            {
                newRect.height = visibleRect.height;
            }
            
            view.scrollRectToVisible(newRect);
        }
    
    }
    
    /**
     * The most importand method. Returns the JasperDesign to which the
     * scene refers to.
     * 
     * @return
     */
    abstract public JasperDesign getJasperDesign();
    
    /**
     * Generic call to refresh an entire element group.
     * It could be a band, a cell, a frame or an element group.
     * 
     * @param group
     */
    abstract public void refreshElementGroup(JRElementGroup group);
    
    /**
     * Return the DrawVisitor used by JR to render elements on screen.
     * The visitor can be instanciated simply with this code:
     * 
     * new DrawVisitor(jasperDesign, null);
     * 
     * @return DrawVisitor
     */
    abstract public DrawVisitor getDrawVisitor();
    

    // Implementation of common designers stuff
    
    /**
     * This layer is used to place elements
     */
    protected LayerWidget elementsLayer = null;
    
    public LayerWidget getElementsLayer() {
        return elementsLayer;
    }
    
    /**
     * This layer is used for selectionWidget widgets
     */
    protected LayerWidget selectionLayer  = null;

    public LayerWidget getSelectionLayer() {
        return selectionLayer;
    }
    
    /**
     * This layer is used for selectionWidget widgets
     */
    protected  LayerWidget guideLinesLayer  = null;

    public LayerWidget getGuideLinesLayer() {
        return guideLinesLayer;
    }
    
    protected boolean gridVisible = false;
    protected boolean snapToGrid = false;

    public boolean isSnapToGrid() {
        return snapToGrid;
    }

    /**
     * This method should be overridden to reflect change in the 
     * actions (AlignWithXXX)
     * 
     * @param snapToGrid
     */
    public void setSnapToGrid(boolean snapToGrid) {
        if (this.snapToGrid != snapToGrid)
        {
            this.snapToGrid = snapToGrid;
        }
    }

    public boolean isGridVisible() {
        return gridVisible;
    }

    /**
     * This method should be overridden to reflect changes
     * revalidating the layer that implements the grid
     * 
     * @param snapToGrid
     */
    public void setGridVisible(boolean gridVisible) {
        if (this.gridVisible != gridVisible)
        {
            this.gridVisible = gridVisible;
        }
    }
    
    protected static final BasicStroke STROKE = new BasicStroke (1.0f, BasicStroke.JOIN_BEVEL, BasicStroke.CAP_BUTT, 5.0f, new float[] { 6.0f, 3.0f }, 0.0f);

    protected static final AlignWithMoveDecorator ALIGN_WITH_MOVE_DECORATOR_DEFAULT = new AlignWithMoveDecorator() {
        public ConnectionWidget createLineWidget (Scene scene) {
            UnscaledDecoratorWidget widget = new UnscaledDecoratorWidget(scene);
            widget.setStroke(STROKE);
            widget.setForeground (Color.BLUE);
            return widget;
        }
    };
    
    protected static final WidgetAction elementPopupMenuAction = ActionFactory.createPopupMenuAction(new ReportElementPopupMenuProvider());
    protected static final WidgetAction reportPopupMenuAction = ActionFactory.createPopupMenuAction(new ReportPopupMenuProvider());

    protected static final InputMap elementInputMap = new InputMap();
    protected static final ActionMap elementActionMap = new ActionMap();

    final protected WidgetAction inplaceEditorAction = ActionFactory.createInplaceEditorAction(new ReportTextElementInplaceEditorProvider(new TextFieldInplaceEditor() {
            public boolean isEnabled (Widget widget) {
                JRDesignElement element = getElement(widget);
                return element != null && element instanceof JRDesignTextElement;
            }
            public String getText (Widget widget) {
                 JRDesignElement element = getElement(widget);
                 if (element != null && element instanceof JRDesignStaticText)
                 {
                     return ((JRDesignStaticText)element).getText();
                 }
                 else if (element != null && element instanceof JRDesignTextField)
                 {
                     return Misc.getExpressionText( ((JRDesignTextField)element).getExpression() );
                 }
                 
                 return "";
            }

            public void setText (Widget widget, String text) {
                
                JRDesignElement element = getElement(widget);
                 if (element != null && element instanceof JRDesignStaticText)
                 {
                     setValue((JRDesignStaticText)element, text);
                 }
                 else if (element != null && element instanceof JRDesignTextField)
                 {
                     setValue((JRDesignTextField)element, text);
                 }
            }
            
            public JRDesignElement getElement(Widget widget)
            {
                if (widget instanceof JRDesignElementWidget)
                {
                    return ((JRDesignElementWidget)widget).getElement();
                }
                if (widget instanceof SelectionWidget)
                {
                    
                    return getElement( ((SelectionWidget)widget).getRealWidget()  );
                }
                return null;
            }
            
            public void setValue(JRDesignTextField element, Object val)  {

                JRDesignExpression oldExp =  (JRDesignExpression)element.getExpression();
                JRDesignExpression newExp = null;
                
                if ( (val == null || val.equals("")) && 
                     (oldExp == null || oldExp.getValueClassName() == null || oldExp.getValueClassName().equals("java.lang.String")) )
                {
                    element.setExpression(null);
                }
                else
                {
                    String s = (val != null) ? val+"" : "";

                    newExp = new JRDesignExpression();
                    newExp.setText(s);
                    newExp.setValueClassName( oldExp != null ? oldExp.getValueClassName() : null );
                    element.setExpression(newExp);
                }

                ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                element,
                                "Expression", 
                                JRExpression.class,
                                oldExp,newExp);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);

            }
            
            public void setValue(JRDesignStaticText element,Object val)
            {
                String oldValue = element.getText();

                String newValue = val+"";
                if (val == null)
                {
                    newValue = "";
                }

                element.setText(newValue);

                ObjectPropertyUndoableEdit opue = new ObjectPropertyUndoableEdit(
                        element, "Text", String.class, oldValue, newValue);

                IReportManager.getInstance().addUndoableEdit(opue);

            }
        }));
        
    
        /**
     * Associates guides to widgets....
     */ 
    private HashMap guides_widgets = new HashMap();
    
    @SuppressWarnings("unchecked")
    public void guideLineAdded(GuideLine guideLine) {
        
        JComponent view = getView();
        GuideLineWidget guideLineWidget = new GuideLineWidget(this, guideLine);
        
        if (guideLine.isVertical())
        {
            int y0 = 0;
            if (getJasperDesign() != null)
            {
                y0 = getJasperDesign().getTopMargin();
            }
            guideLineWidget.setPreferredLocation(new Point(-10,guideLine.getPosition()+ y0));
            guideLineWidget.setPreferredBounds(new Rectangle(0,0, getJasperDesign().getPageWidth() + 10, 1) );
        }
        else
        {
            int x0 = 0;
            if (getJasperDesign() != null)
            {
                x0 = getJasperDesign().getLeftMargin();
            }
            guideLineWidget.setPreferredLocation(new Point(guideLine.getPosition()+ x0,-10));
            guideLineWidget.setPreferredBounds(new Rectangle(0,0,1, ModelUtils.getDesignHeight(getJasperDesign())+10));
        }
        guideLinesLayer.addChild(guideLineWidget);
        guides_widgets.put(guideLine,guideLineWidget);
        guideLineWidget.repaint();
        validate();
        
    }

    public void guideLineRemoved(GuideLine guideLine) {
        // Find the right guideline...
       GuideLineWidget w = (GuideLineWidget)guides_widgets.get(guideLine);
       if (w != null) {
           w.removeFromParent();
           validate();
       }
    }

    public void guideLineMoved(GuideLine guideLine) {
        
        GuideLineWidget w = (GuideLineWidget)guides_widgets.get(guideLine);
        if (w != null)
        {
            if (guideLine.isVertical())
            {
                int y0 = 0;
                if (getJasperDesign() != null)
                {
                    y0 = getJasperDesign().getTopMargin();
                }
                w.setPreferredLocation(new Point(-10, guideLine.getPosition()+ y0));
            }
            else
            {
                int x0 = 0;
                if (getJasperDesign() != null)
                {
                    x0 = getJasperDesign().getLeftMargin();
                }
                w.setPreferredLocation(new Point(guideLine.getPosition()+ x0,-10));
            }
            validate();
        }
    }
    
    
    
    protected JComponent jComponent = null;
    
    /**
     * Returns the view of the scene as JComponent.
     * This is used i.e. by the DesignerDropTarget
     * 
     * @return JComponent
     */
    public JComponent getJComponent() {
        
        if (jComponent == null)
        {
            jComponent = createView();

        }

        return jComponent;
    }
    



    protected java.util.HashMap elementGroupListeners = new HashMap();

    /**
     * @return the selectionManager
     */
    public ObjectSceneSelectionManager getSelectionManager() {
        return selectionManager;
    }

    /**
     * @return the reportSelectAction
     */
    public WidgetAction getReportSelectAction() {
        return reportSelectAction;
    }

    /**
     * @param reportSelectAction the reportSelectAction to set
     */
    public void setReportSelectAction(WidgetAction reportSelectAction) {
        this.reportSelectAction = reportSelectAction;
    }
    
    protected final class GroupChangeListener implements PropertyChangeListener
    {
        WeakReference<JRDesignElementGroup> group = null;

        @SuppressWarnings("unchecked")
        public GroupChangeListener( JRDesignElementGroup group )
        {
            this.group = new WeakReference( group );
        }

        public void propertyChange(PropertyChangeEvent evt) {
            if (group.get() != null)
            {
                refreshElementGroup(group.get());
            }
        }
    }
    
    
    private boolean updatingView = false;

    public boolean isUpdatingView() {
        return updatingView;
    }

    public void setUpdatingView(boolean updatingView) {
        this.updatingView = updatingView;
    }

    
    public WidgetAction createReportSelectAction()
    {
        return new SelectAction(new ReportObjectSelectProvider(this));
    }


    public Point getParentLocation(JasperDesign jd, JRDesignElement element, JRDesignElementWidget widget)
    {

        Point p = ModelUtils.getParentLocationImpl(jd, element, widget);
        
        return p;
    }

    public Rectangle getParentBounds(JasperDesign jd, JRDesignElement element, JRDesignElementWidget widget)
    {
        return ModelUtils.getParentBoundsImpl(jd, element, widget);
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

/**
     * Returns the container in which the element has been added
     * p is where the element has been pasted (not in scene coordinates).
     *
     * @param element
     * @return the container, or null if no drop action has been taken.
     */
    public Object dropElementAt(JRDesignElement element, Point location)
    {
        return null;
    }


    /**
     * If the current location is a valid drop location, return true.
     * @param location
     * @return
     */
    public abstract boolean acceptDropAt(Point location);

    public boolean isValidPosition(JRDesignElementWidget elementWidget)
    {

        Rectangle bounds = getParentBounds(getJasperDesign(), elementWidget.getElement(), elementWidget);
        if (elementWidget.getElement().getElementGroup() instanceof JRBand)
        {
            return bounds.height >= elementWidget.getElement().getY() + elementWidget.getElement().getHeight();
        }
        else
        {
            // Check that the element is fully contained inside its parent...
            return bounds.height >= elementWidget.getElement().getY() + elementWidget.getElement().getHeight() &&
                   elementWidget.getElement().getX() >= 0 &&
                   elementWidget.getElement().getY() >= 0 &&
                   bounds.width >= elementWidget.getElement().getX() + elementWidget.getElement().getWidth();
        }
    }
}
