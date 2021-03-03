/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.table;

import com.jaspersoft.ireport.components.table.actions.SeparatorDblClickResizeAction;
import com.jaspersoft.ireport.components.table.actions.TableCellSeparatorMoveAction;
import com.jaspersoft.ireport.components.table.actions.TableElementPopupMenuProvider;
import com.jaspersoft.ireport.components.table.actions.TableSceneClickAction;
import com.jaspersoft.ireport.components.table.nodes.TableCellNode;
import com.jaspersoft.ireport.components.table.nodes.TableColumnGroupNode;
import com.jaspersoft.ireport.components.table.nodes.TableNullCellNode;
import com.jaspersoft.ireport.components.table.nodes.TableSectionNode;
import com.jaspersoft.ireport.components.table.undo.AddTableCellUndoableEdit;
import com.jaspersoft.ireport.components.table.widgets.IndicatorWidget;
import com.jaspersoft.ireport.components.table.widgets.TableCellSeparatorWidget;
import com.jaspersoft.ireport.components.table.widgets.TableWidget;
import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.actions.KeyboardElementMoveAction;
import com.jaspersoft.ireport.designer.actions.ReportAlignWithMoveStrategyProvider;
import com.jaspersoft.ireport.designer.actions.ReportAlignWithResizeStrategyProvider;
import com.jaspersoft.ireport.designer.actions.TranslucentRectangularSelectDecorator;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.widgets.JRDesignChartWidget;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import com.jaspersoft.ireport.designer.widgets.JRDesignImageWidget;
import com.jaspersoft.ireport.designer.widgets.visitor.ConfigurableDrawVisitor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.draw.DrawVisitor;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.EventProcessingType;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.SeparatorWidget.Orientation;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.Node;

/**
 *
 * @version $Id: TableReportObjectScene.java 0 2010-03-12 16:28:32 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class TableObjectScene extends AbstractReportObjectScene implements PropertyChangeListener {

    private TableWidget tableWidget = null;
    private List<JRDesignElement> selectedElements = new ArrayList<JRDesignElement>();
    private StandardTable table = null;
    private JasperDesign jasperDesign = null;
    private DrawVisitor drawVisitor = null;
    private JRDesignComponentElement tableElement = null;

    private TableAlignWithWidgetCollector reportAlignWithWidgetCollector = null;
    private ReportAlignWithMoveStrategyProvider reportAlignWithMoveStrategyProvider = null;
    private ReportAlignWithResizeStrategyProvider reportAlignWithResizeStrategyProvider = null;
    private KeyboardElementMoveAction keyboardElementMoveAction = null;
    private TableSceneClickAction tableSceneClickAction = null;
    protected static final WidgetAction tableElementPopupMenuAction = ActionFactory.createPopupMenuAction(new TableElementPopupMenuProvider());

    private PropertyChangeListener stylesChangeListener =  new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                setDrawVisitor(null);
                tableWidget.revalidate(true);
                getScene().validate();
                if (evt.getPropertyName().equals(JasperDesign.PROPERTY_STYLES))
                {
                    updateStyleListeners();
                }
            }
        };

    private TableMatrix tableMatrix = null;

    public TableObjectScene(JasperDesign jasperdesign, JRDesignComponentElement tableElement)
    {
        this.tableElement = tableElement;
        this.jasperDesign = jasperdesign;
        this.table = (StandardTable)tableElement.getComponent();
        initScene();
        jasperdesign.getEventSupport().addPropertyChangeListener(JasperDesign.PROPERTY_STYLES,stylesChangeListener);
        updateStyleListeners();
    }

    private void updateStyleListeners()
    {
        if (getJasperDesign() != null)
        {
            List styles = getJasperDesign().getStylesList();
            for (int i=0; i<styles.size(); ++i)
            {
                if (styles.get(i) instanceof JRDesignStyle)
                {
                    ((JRDesignStyle)styles.get(i)).getEventSupport().removePropertyChangeListener(stylesChangeListener);
                    ((JRDesignStyle)styles.get(i)).getEventSupport().addPropertyChangeListener(stylesChangeListener);
                }
            }
        }


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

        indicatorsLayer  = new LayerWidget(this);
        addChild(indicatorsLayer );


        reportAlignWithWidgetCollector = new TableAlignWithWidgetCollector(this);
        reportAlignWithMoveStrategyProvider = new ReportAlignWithMoveStrategyProvider(reportAlignWithWidgetCollector, interractionLayer, ALIGN_WITH_MOVE_DECORATOR_DEFAULT, false);
        reportAlignWithResizeStrategyProvider  = new ReportAlignWithResizeStrategyProvider(reportAlignWithWidgetCollector, interractionLayer, ALIGN_WITH_MOVE_DECORATOR_DEFAULT, false);

        

        tableSceneClickAction = new TableSceneClickAction();
        getActions().addAction(tableSceneClickAction);

        getActions().addAction(ActionFactory.createRectangularSelectAction(
                new TranslucentRectangularSelectDecorator(this), interractionLayer,
                ActionFactory.createObjectSceneRectangularSelectProvider(this)));

       getActions().addAction(reportPopupMenuAction);

        getActions().addAction (ActionFactory.createMouseCenteredZoomAction (1.1));
        getActions().addAction (ActionFactory.createPanAction ());


        keyboardElementMoveAction = new KeyboardElementMoveAction();
        getActions().addAction( keyboardElementMoveAction );

        this.setMaximumBounds(new Rectangle(-10,-10,Integer.MAX_VALUE, Integer.MAX_VALUE) );
        
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                rebuildDocument();
            }
        });

    }


    public void rebuildDocument()
    {
        if (tableMatrix == null)
        {
          tableMatrix  = new TableMatrix(getJasperDesign(), getTable());
        }
        else
        {
            // Reprocess the matrix...
            tableMatrix.processMatrix();
        }

        boolean oldUpdateingStatus = isUpdatingView();
        try {
            setUpdatingView(true);

            pageLayer.removeChildren();
            
            cellSeparatorsLayer.removeChildren();
            
            backgroundLayer.removeChildren();
            interractionLayer.removeChildren();
            indicatorsLayer.removeChildren();

            // Remove all the objects...
            if (jasperDesign == null)
            {
                while (getObjects().size() > 0)
                {
                    removeObject(getObjects().iterator().next());
                }
                elementsLayer.removeChildren();
                selectionLayer.removeChildren();
                return;
            }
            
            if (getTableWidget() == null)
            {
                setTableWidget(new TableWidget(this, getTableElement()));
                getTableWidget().getActions().addAction(inplaceEditorAction);
            }
            else
            {
                getTableWidget().updateBounds();
            }


            pageLayer.addChild(getTableWidget());

            refreshCells();

        } finally {

            setUpdatingView(oldUpdateingStatus);
        }
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

        // Add a line for each crosstab intersection....
        int currentX = 0;
        for (int i=1; i<getVerticalSeparators().size(); ++i)
        {
            TableCellSeparatorWidget w = new TableCellSeparatorWidget(this, i, Orientation.VERTICAL);
            w.getActions().addAction( new TableCellSeparatorMoveAction(true, InputEvent.SHIFT_DOWN_MASK) );
            w.getActions().addAction( new TableCellSeparatorMoveAction() );
            w.getActions().addAction(new SeparatorDblClickResizeAction());
            cellSeparatorsLayer.addChild(w);
        }

        updateIndicators();

        for (int i=1; i<getHorizontalSeparators().size(); ++i)
        {
            if ( tableMatrix.isValidBase(i))
            {
                TableCellSeparatorWidget w = new TableCellSeparatorWidget(this, i, Orientation.HORIZONTAL);
                w.getActions().addAction( new TableCellSeparatorMoveAction(true, InputEvent.SHIFT_DOWN_MASK) );
                w.getActions().addAction( new TableCellSeparatorMoveAction() );
                w.getActions().addAction(new SeparatorDblClickResizeAction());
                cellSeparatorsLayer.addChild(w);
            }
        }
       

        // Collect all the cells...
        List<TableCell> tableCells = getTableMatrix().getCells();
        List<DesignCell> cells = new ArrayList<DesignCell>();
        for (TableCell cell : tableCells)
        {
            if (cell.getCell() != null) cells.add(cell.getCell());
        }

        List<Object> toBeRemovedObjects = new ArrayList<Object>();
        List<Widget> toBeRemoved = new ArrayList<Widget>();
        List<Widget> toBeRemovedSelection = new ArrayList<Widget>();


        for (Iterator iter = getObjects().iterator(); iter.hasNext(); )
        {
            // Remove just the orphan elements...
            Object obj = iter.next();
            if (obj instanceof JRDesignElement && !cells.contains( TableModelUtils.getCell( (JRDesignElement)obj)))
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
        for (DesignCell cell : cells)
        {
            if (cell != null)
            {
                addElements( cell.getChildren() );
            }
        }

        validate();
    }

    @Override
    public JasperDesign getJasperDesign() {
        return jasperDesign;
    }

    @Override
    public void refreshElementGroup(JRElementGroup group) {
    }

    public DrawVisitor getDrawVisitor() {
        if (drawVisitor == null)
        {
            this.drawVisitor = new ConfigurableDrawVisitor(getJasperDesign(), null);
        }
        return drawVisitor;
    }

    public void setDrawVisitor(DrawVisitor drawVisitor) {
        this.drawVisitor = drawVisitor;
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
    
    public LayerWidget getIndicatorsLayer() {
        return indicatorsLayer;
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
     * This layer is used for row/col indicators
     */
    LayerWidget indicatorsLayer  = null;

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

    /**
     * @return the tableElement
     */
    public JRDesignComponentElement getTableElement() {
        return tableElement;
    }

    /**
     * @return the tableElement
     */
    public StandardTable getTable() {
        return (StandardTable)tableElement.getComponent();
    }

    /**
     * @param tableElement the tableElement to set
     */
    public void setTableElement(JRDesignComponentElement tableElement) {
        this.tableElement = tableElement;
    }

    /**
     * @return the tableWidget
     */
    public TableWidget getTableWidget() {
        return tableWidget;
    }

    /**
     * @param tableWidget the tableWidget to set
     */
    public void setTableWidget(TableWidget tableWidget) {
        this.tableWidget = tableWidget;
    }

    /**
     * @return the verticalSeparators
     */
    public List<Integer> getVerticalSeparators() {
        return getTableMatrix().getVerticalSeparators();
    }

    /**
     * @return the horizontalSeparators
     */
    public List<Integer> getHorizontalSeparators() {
        return getTableMatrix().getHorizontalSeparators();
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

                        w.getSelectionWidget().updateBounds();
                        // put it at the end of the list of childrens...
                        w.getSelectionWidget().bringToFront();
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

    /*
    public void addCellSeparatorWidget(JRDesignCrosstabCell cell, int xyLocation, boolean vertical)
    {
        if (b == null) return;
        BandSeparatorWidget bbw = new BandSeparatorWidget(this, b);
        bbw.getActions().addAction( new BandMoveAction(true, InputEvent.SHIFT_DOWN_MASK) );
        bbw.getActions().addAction( new BandMoveAction() );
        bandSeparatorsLayer.addChild(bbw);
    }*/

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

        widget.getActions ().addAction(tableElementPopupMenuAction);
        widget.getSelectionWidget().getActions().addAction(tableElementPopupMenuAction);

        widget.getActions().addAction( ActionFactory.createActionMapAction(elementInputMap, elementActionMap) );
        widget.getSelectionWidget().getActions().addAction( ActionFactory.createActionMapAction(elementInputMap, elementActionMap) );


        elementsLayer.addChild(widget);
        selectionLayer.addChild(widget.getSelectionWidget());

        addObject(de, widget);

        return widget;
    }


    /**
     * This utility looks for the phisical parent of an element and returns his position.
     * This position is refers to the plain document preview, where 0,0 are the coordinates
     * of the upperleft corner of the document.
     * If no parent is found, the method returns 0,0
     */
    @Override
    public Point getParentLocation(JasperDesign jd, JRDesignElement element, JRDesignElementWidget widget)
    {

        Point base = null;
        if (element == null) return new Point(0,0);

        JRElementGroup grp = element.getElementGroup();

        // I need to discover the first logical parent of this element
        while (grp != null)    // Element placed in a frame
        {
            if (grp instanceof DesignCell)    // Element placed in a cell
            {
                base =  getTableMatrix().getCellLocation((DesignCell)grp);
                break;
            }
            else if (grp instanceof JRDesignFrame)
            {
                JRDesignFrame frame = (JRDesignFrame)grp;
                base = getParentLocation(jd, frame, widget);
                base.x += frame.getX();
                base.y += frame.getY();
                // In this case we return immediatly
                break;
            }
            else
            {
                grp = grp.getElementGroup();
            }
        }

        if (base == null)
        {
            base = new Point(0,0);
            if (widget != null)
            {

                // this is a very strange case... check if this element belongs to
                // a custom component...
                if (widget.getScene() instanceof AbstractReportObjectScene)
                {
                    JRDesignElementWidget owner = ((AbstractReportObjectScene)widget.getScene()).findCustomComponentOwner(element);
                    if (owner != null)
                    {
                        base = getParentLocation(jd, owner.getElement(), owner);
                        base.x += owner.getElement().getX();
                        base.y += owner.getElement().getY();
                    }
                }
            }

        }

        return base;

    }

    /**
     * @return the tableMatrix
     */
    public TableMatrix getTableMatrix() {
        return tableMatrix;
    }

    /**
     * @param tableMatrix the tableMatrix to set
     */
    public void setTableMatrix(TableMatrix tableMatrix) {
        this.tableMatrix = tableMatrix;
    }


    @Override
    public Rectangle getParentBounds(JasperDesign jd, JRDesignElement element, JRDesignElementWidget widget)
    {
        Rectangle base = null;
        if (element == null) return new Rectangle(0,0,0,0);

        JRElementGroup grp = element.getElementGroup();

        // I need to discover the first logical parent of this element
        while (grp != null)    // Element placed in a frame
        {
            if (grp instanceof DesignCell)    // Element placed in a band
            {
                DesignCell cell = (DesignCell)grp;
                base = getTableMatrix().getCellBounds(cell);

                break;
            }
            else if (grp instanceof JRDesignFrame)
            {
                JRDesignFrame frame = (JRDesignFrame)grp;

                Point p = getParentLocation(jd, frame, widget);
                base = new Rectangle();
                base.x = p.x + frame.getX();
                base.y = p.y + frame.getY();
                base.width = frame.getWidth();
                base.height = frame.getHeight();

                break;
            }
            else
            {
                grp = grp.getElementGroup();
            }
        }

        if (base == null)
        {
            base = new Rectangle(0,0,0,0);
            if (widget != null)
            {

                // this is a very strange case... check if this element belongs to
                // a custom component...
                if (widget.getScene() instanceof AbstractReportObjectScene)
                {
                    JRDesignElementWidget owner = ((AbstractReportObjectScene)widget.getScene()).findCustomComponentOwner(element);
                    if (owner != null)
                    {
                        Point p = getParentLocation(jd, owner.getElement(), owner);

                        base.x += owner.getElement().getX();
                        base.y += owner.getElement().getY();
                        base.width += owner.getElement().getWidth();
                        base.height += owner.getElement().getHeight();
                    }
                }
            }
        }

        return base;

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
        Point p = convertViewToScene( location );

        TableCell cell = getTableMatrix().getCellAt(p);
        if (cell == null) return null;

        DesignCell designCell =  cell.getCell();
        JRDesignElementWidget wContainer = findTopMostFrameAt(p);

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
                    wContainer.addElement(element);

                    return frame;
            }
            else
            {
                    Point parentLocation = getTableMatrix().getCellLocation(cell);
                    element.setX( p.x - parentLocation.x);
                    element.setY( p.y - parentLocation.y);
                    if (designCell == null)
                    {
                        // Create a new cell just for that...
                        designCell = new DesignCell();
                        // check the current max row high of this row...
                        int rowHight = getHorizontalSeparators().get(cell.getRow()+cell.getRowSpan()) - getHorizontalSeparators().get(cell.getRow());
                        designCell.setHeight(rowHight);
                        designCell.addElement(element);
                        TableModelUtils.addCell(cell.getColumn(), designCell, (byte) cell.getType(), cell.getGroupName());

                        AddTableCellUndoableEdit edit = new AddTableCellUndoableEdit(getTable(), getJasperDesign(), designCell,cell.getColumn(),(byte) cell.getType(), (cell.getGroupName() != null) ? getGroup(cell.getGroupName()) : null);
                        IReportManager.getInstance().addUndoableEdit(edit);
                    }
                    else
                    {
                        designCell.addElement(element);
                    }
                    // TODO: Layout cell...
                    //DefaultTableCellElementsLayout.doLayout(designCell, this);
                    return designCell;
            }

    }

    private JRGroup getGroup(String name)
    {
        JRDesignDataset dataset = getJasperDesign().getMainDesignDataset();
        if (table.getDatasetRun() != null && table.getDatasetRun().getDatasetName() != null)
        {
            dataset = (JRDesignDataset) getJasperDesign().getDatasetMap().get(table.getDatasetRun().getDatasetName());
        }
        if (dataset != null)
        {
            return (JRGroup)dataset.getGroupsMap().get(name);
        }
        return null;
    }


    private JRDesignElementWidget findTopMostFrameAt(Point p) {

         LayerWidget layer = getElementsLayer();
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

    @Override
    public boolean acceptDropAt(Point location) {
        Point p = convertViewToScene(location);
        return getTableMatrix().getCellAt(p) != null;
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


    public synchronized void reprocessMatrix()
    {
        if (getJasperDesign() == null) return;
        if (table == null) return;
        if (getTableMatrix() == null) return;

        JRDesignDataset dataset = getJasperDesign().getMainDesignDataset();
        if (table.getDatasetRun() != null && table.getDatasetRun().getDatasetName() != null)
        {
            dataset = (JRDesignDataset) getJasperDesign().getDatasetMap().get(table.getDatasetRun().getDatasetName());
        }

        getTableMatrix().processMatrix(dataset);
    }

    Node[] lastSelectedNodes = null;


    private void updateIndicators()
    {

        BaseColumn column = null;

        getIndicatorsLayer().removeChildren();

        if (lastSelectedNodes == null || lastSelectedNodes.length == 0)
        {
            validate();
            return;
        }

        for (int i=0; i<lastSelectedNodes.length; ++i)
        {
            if (isNodeChildOf(lastSelectedNodes[i], getTableElement()))
            {
                column = lastSelectedNodes[i].getLookup().lookup(BaseColumn.class);
                
                if (lastSelectedNodes[i] instanceof TableNullCellNode)
                {
                    TableNullCellNode cellNode = (TableNullCellNode)lastSelectedNodes[i];
                    TableCell tc = getTableMatrix().findTableCell(cellNode.getColumn(), cellNode.getSection(), cellNode.getGroup() == null ? null : cellNode.getGroup().getName());
                    if (tc != null)
                    {
                        Rectangle r = getTableMatrix().getCellBounds(tc);
                        if (r != null)
                        {
                            IndicatorWidget cw = new IndicatorWidget(this, tc.getRow(), IndicatorWidget.ROW);
                            cw.setPreferredLocation(new Point(-10, r.y));
                            cw.setPreferredBounds(new Rectangle(0,0, 8, r.height));
                            cw.revalidate(true);
                            getIndicatorsLayer().addChild(cw);
                        }
                    }
                }
                else if (lastSelectedNodes[i] instanceof TableCellNode)
                {
                    TableCellNode cellNode = (TableCellNode)lastSelectedNodes[i];
                    TableCell tc = getTableMatrix().findTableCell(cellNode.getColumn(), cellNode.getSection(), cellNode.getGroup() == null ? null : cellNode.getGroup().getName());
                    if (tc != null)
                    {
                        Rectangle r = getTableMatrix().getCellBounds(tc);
                        if (r != null)
                        {
                            IndicatorWidget cw = new IndicatorWidget(this, tc.getRow(), IndicatorWidget.ROW);
                            cw.setPreferredLocation(new Point(-10, r.y));
                            cw.setPreferredBounds(new Rectangle(0,0, 8, r.height));
                            cw.revalidate(true);
                            getIndicatorsLayer().addChild(cw);
                        }
                    }
                }
                else if (lastSelectedNodes[i] instanceof TableSectionNode)
                {
                    TableSectionNode sectionNode = (TableSectionNode)lastSelectedNodes[i];
                    Rectangle r = getTableMatrix().getSectionBounds(sectionNode.getSectionType(), sectionNode.getGroup() == null ? null : sectionNode.getGroup().getName());
                    if (r != null)
                    {
                        IndicatorWidget cw = new IndicatorWidget(this, new Object[]{sectionNode.getSectionType(), sectionNode.getGroup()}, IndicatorWidget.SECTION);
                        cw.setPreferredLocation(new Point(-10, r.y));
                        cw.setPreferredBounds(new Rectangle(0,0, 8, r.height));
                        cw.revalidate(true);
                        getIndicatorsLayer().addChild(cw);
                    }
                }
                else if (lastSelectedNodes[i] instanceof TableColumnGroupNode)
                {
                    TableColumnGroupNode columnGroupNode = (TableColumnGroupNode)lastSelectedNodes[i];
                    Rectangle r = getTableMatrix().getColumnBounds(columnGroupNode.getColumnGroup());
                    if (r != null)
                    {
                        IndicatorWidget cw = new IndicatorWidget(this, columnGroupNode.getColumnGroup(), IndicatorWidget.COLUMN_GROUP);
                        cw.setPreferredLocation(new Point(-10, r.y));
                        cw.setPreferredBounds(new Rectangle(0,0, 8, r.height));
                        cw.revalidate(true);
                        getIndicatorsLayer().addChild(cw);
                    }
                }
                else if (lastSelectedNodes[i] instanceof ElementNode)
                {
                    // Check if the parent is a DesignCell element...
                    Node parent = lastSelectedNodes[i].getParentNode();
                    while (parent != null)
                    {
                        if (parent instanceof TableCellNode)
                        {
                            TableCellNode cellNode = (TableCellNode)parent;
                            TableCell tc = getTableMatrix().findTableCell(cellNode.getColumn(), cellNode.getSection(), cellNode.getGroup() == null ? null : cellNode.getGroup().getName());
                            if (tc != null)
                            {
                                column = tc.getColumn();
                                Rectangle r = getTableMatrix().getCellBounds(tc);
                                if (r != null)
                                {
                                    IndicatorWidget cw = new IndicatorWidget(this, tc.getRow(), IndicatorWidget.ROW);
                                    cw.setPreferredLocation(new Point(-10, r.y));
                                    cw.setPreferredBounds(new Rectangle(0,0, 8, r.height));
                                    cw.revalidate(true);
                                    getIndicatorsLayer().addChild(cw);
                                }
                            }
                            break;
                        }
                        parent = parent.getParentNode();
                    }
                }

                


            }
        }


        if (column != null)
        {
            Rectangle r = getTableMatrix().getColumnBounds(column);
            if (r != null)
            {
                IndicatorWidget cw = new IndicatorWidget(this, column, IndicatorWidget.COLUMN);
                cw.setPreferredLocation(new Point(r.x, -10));
                cw.setPreferredBounds(new Rectangle(0,0, r.width,8));
                cw.revalidate(true);
                getIndicatorsLayer().addChild(cw);
            }
        }

        validate();

    }

    public void propertyChange(PropertyChangeEvent evt) {

        if ( evt.getPropertyName().equals("selectedNodes"))
        {
            Node[] selectedNodes = (Node[])evt.getNewValue();

            lastSelectedNodes = selectedNodes;
            updateIndicators();

        }
    }

    private boolean isNodeChildOf(Node node, JRDesignComponentElement tableElement) {
        if (node == null) return false;
        if (node.getLookup().lookup(JRDesignComponentElement.class) == tableElement) return true;
        return isNodeChildOf(node.getParentNode(), tableElement);
    }
}
