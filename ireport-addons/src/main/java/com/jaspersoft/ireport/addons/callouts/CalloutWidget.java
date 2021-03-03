/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.callouts;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;

import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.ResizeControlPointResolver;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.action.ResizeProvider.ControlPoint;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.ImageUtilities;

/**
 *
 * @version $Id: CalloutWidget.java 0 2010-01-14 14:40:52 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class CalloutWidget extends Widget implements PopupMenuProvider {

    private JTextArea field = null;
    private String text = "";

    private RenderingHints hints = null;
    private JPopupMenu popupMenu = null;


    final WidgetAction inplaceEditorAction = ActionFactory.createInplaceEditorAction(new CalloutInplaceEditorProvider( new TextFieldInplaceEditor() {
            public boolean isEnabled (Widget widget) {
                return true;
            }
            public String getText (Widget widget) {
                return ((CalloutWidget) widget).getText();
            }

            public void setText (Widget widget, String text) {
                CalloutsUtility.saveCallouts((AbstractReportObjectScene)getScene());
            }
        }));

    public CalloutWidget(AbstractReportObjectScene scene)
    {
        super(scene);

        field = new JTextArea(getText());
        field.setWrapStyleWord(true);
        field.setLineWrap(true);
        field.setBorder( new EmptyBorder(new Insets(0,2,0,2))); //new javax.swing.border.LineBorder(new Color(255,216,0), 1));  //
        //field.setFont(getFont());
        field.setBackground(new Color(252, 255,0 , 128)); //253, 255,127, ));
        field.setOpaque(false);

        setBackground(new Color(252, 255,0 , 128));
        setOpaque(true);

        setBorder(  BorderFactory.createImageBorder(new Insets(6, 7, 12, 12), new Insets(16, 16, 22, 21), ImageUtilities.loadImage("/com/jaspersoft/ireport/addons/callouts/borders.png")));


        addDependency(new Widget.Dependency() {

            boolean validating = false;

            public void revalidateDependency() {

                if (validating) return;

                if (getField() == null) return;
                if (getBounds() == null && getPreferredBounds() == null) return;
                Rectangle bounds = getBounds();
                if (bounds == null)
                {
                    bounds = getPreferredBounds();
                }
                if (bounds == null) return;
                getField().setSize( bounds.width - getBorder().getInsets().left - getBorder().getInsets().right, field.getHeight());
                recalculateFieldSize(false);
                validating = true;
                CalloutWidget.this.revalidate();
                getScene().validate();
                validating = false;
            }
        });


        setText(System.getProperty("user.name") + " " + (new SimpleDateFormat()).format(new java.util.Date()));


        
        getActions ().addAction (ActionFactory.createExtendedConnectAction (CalloutsUtility.getCalloutsLayer(scene, true), new ConnectProvider() { //interractionLayer
            public boolean isSourceWidget (Widget sourceWidget) {
                return sourceWidget == CalloutWidget.this;
            }
            public ConnectorState isTargetWidget (Widget sourceWidget, Widget targetWidget) {
                if (targetWidget instanceof JRDesignElementWidget) return ConnectorState.ACCEPT;
                return ConnectorState.REJECT;
            }
            public boolean hasCustomTargetWidgetResolver (Scene scene) {
                return false;
            }
            public Widget resolveTargetWidget (Scene scene, Point sceneLocation) {
                return null;
            }
            public void createConnection (Widget sourceWidget, Widget targetWidget) {

                createPinConnection(new Point( targetWidget.getLocation().x + (targetWidget.getBounds().width + targetWidget.getBounds().x)/2,
                                               targetWidget.getLocation().y + (targetWidget.getBounds().height + targetWidget.getBounds().y)/2));
                
            }
        }));

        getActions().addAction( ActionFactory.createResizeAction(ActionFactory.createFreeResizeStategy(),new ResizeControlPointResolver() {

            public ControlPoint resolveControlPoint(Widget widget, Point point) {
                Rectangle bounds = widget.getBounds ();
                Insets insets = widget.getBorder ().getInsets ();
                Rectangle spotArea = new Rectangle( bounds.x + bounds.width - insets.right - 10, bounds.y + bounds.height - insets.bottom - 10, 10, 10);

                if (spotArea.contains(point)) return ResizeProvider.ControlPoint.BOTTOM_RIGHT;
                return null;
            }
        } ,new ResizeProvider() {

            Rectangle bounds = null;
            public void resizingStarted(Widget widget) {
                bounds = widget.getBounds();
            }

            public void resizingFinished(Widget widget) {
                if (!bounds.equals(widget.getBounds()))
                {
                    CalloutsUtility.saveCallouts((AbstractReportObjectScene)getScene());
                }
            }
        }));
        getActions().addAction( ActionFactory.createMoveAction(ActionFactory.createFreeMoveStrategy(), new MoveProvider() {

            boolean changed = false;
            public void movementStarted(Widget widget) {
                changed = false;
            }

            public void movementFinished(Widget widget) {
                if (changed)
                {
                    CalloutsUtility.saveCallouts((AbstractReportObjectScene)getScene());
                }
            }

            public Point getOriginalLocation (Widget widget) {
                return widget.getPreferredLocation ();
            }

            public void setNewLocation (Widget widget, Point location) {
                if (!location.equals(widget.getPreferredLocation()))
                {
                    changed = true;
                }
                widget.setPreferredLocation (location);
            }
        }));

        getActions().addAction(inplaceEditorAction);
        getActions().addAction(  ActionFactory.createPopupMenuAction (this));
        


        setPreferredBounds(new Rectangle(0,0,150,75));
        field.setSize(150 - getBorder().getInsets().left - - getBorder().getInsets().right, field.getHeight());
        field.setSize( field.getPreferredSize() );
        recalculateFieldSize(false);
    }

    public JPopupMenu getPopupMenu (Widget widget, Point localLocation)
    {
        if (popupMenu == null)
        {
            popupMenu = new JPopupMenu();
            JMenuItem showAllTextCallout = new JMenuItem("Show all text");
            showAllTextCallout.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    recalculateFieldSize(true);
                    revalidate();
                    getScene ().validate ();
                }
            });

            popupMenu.add(showAllTextCallout);

            JMenuItem addPin = new JMenuItem("Add pin");
            addPin.setIcon( ImageUtilities.image2Icon(  ImageUtilities.loadImage("com/jaspersoft/ireport/addons/callouts/pin-16.png") ));
            addPin.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    // Create a connection with a widget just on the bottom of the
                    // note...
                    createPinConnection(null);
                }
            });

            
            popupMenu.add(addPin);

            popupMenu.add(new JSeparator());

            JMenuItem deleteCalloutChanges = new JMenuItem("Delete");
            deleteCalloutChanges.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    removeFromParent();

                    // Look for all its connections...
                    LayerWidget layer = CalloutsUtility.getCalloutsLayer((AbstractReportObjectScene)getScene(), true);
                    List<Widget> connections = layer.getChildren();
                    List<ConnectionWidget> toBeRemoved = new ArrayList<ConnectionWidget>();
                    for (Widget w : connections)
                    {
                        if (w instanceof ConnectionWidget)
                        {
                            ConnectionWidget cw = (ConnectionWidget)w;
                            if (cw.getSourceAnchor().getRelatedWidget() == CalloutWidget.this)
                            {
                                toBeRemoved.add(cw);
                            }
                        }
                    }
                    for (ConnectionWidget w : toBeRemoved)
                    {
                        w.getTargetAnchor().getRelatedWidget().removeFromParent();
                        w.removeFromParent();
                    }
                    
                    CalloutsUtility.saveCallouts((AbstractReportObjectScene)getScene());
                }
            });

            popupMenu.add(deleteCalloutChanges);
        }

        return popupMenu;
    }

    public JPopupMenu getConnectionPopupMenu (Widget widget, Point localLocation)
    {
        final Widget connectionWidget = widget;

            JPopupMenu connectionPopupMenu = new JPopupMenu();
            JMenuItem removeConnection = new JMenuItem("Delete connection");
            removeConnection.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    
                        if (connectionWidget instanceof ConnectionWidget)
                        {

                            ((ConnectionWidget)connectionWidget).getTargetAnchor().getRelatedWidget().removeFromParent();
                            connectionWidget.removeFromParent();
                        }
                    }
                });

            connectionPopupMenu.add(removeConnection);

        return connectionPopupMenu;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        
        if (text == null) text = "";
        if (text.equals(this.text)) return;

        this.text = text;

        recalculateFieldSize(false);
        revalidate();
    }

    protected void recalculateFieldSize(boolean force)
    {
        Dimension oldPreferredSize = getField().getPreferredSize();
        getField().setText(getText());

        Dimension newPreferredSize = getField().getPreferredSize();
        if (!newPreferredSize.equals(oldPreferredSize) || force)
        {
            getField().setSize(getField().getPreferredSize());
            Rectangle bounds = getBounds();
            if (bounds == null) bounds = getPreferredBounds();
            
            int w = getField().getPreferredSize().width + getBorder().getInsets().left + getBorder().getInsets().left;
            int h = getField().getPreferredSize().height + getBorder().getInsets().top + getBorder().getInsets().bottom;
            if (bounds != null)
            {
                if (w < bounds.width)
                {
                    w = bounds.width;
                }
                if (h < bounds.height)
                {
                    h = bounds.height;
                }
            }
            setPreferredBounds(new Rectangle(w, h));

        }
    }

    /**
     * Create a pin with a connection to this note.
     *
     * @param p - In null, the defaul pin location is used.
     */
    public void createPinConnection(Point p)
    {
        LayerWidget layer = CalloutsUtility.getCalloutsLayer((AbstractReportObjectScene)getScene(), true);

        if (p == null)
        {
            p = new Point(getBounds().width/2 + getPreferredLocation().x, getPreferredLocation().y + getBounds().height+30);
        }
        getScene().validate();

        PinWidget pinWidget = new PinWidget(getScene());
        getScene().validate();
        pinWidget.setPreferredLocation(p);
        getScene().validate();
        layer.addChild(pinWidget);
        getScene().validate();

        ConnectionWidget connection = new ConnectionWidget(getScene());
        getScene().validate();
        pinWidget.setConnectionWidget(connection);
        connection.setTargetAnchorShape (AnchorShape.NONE);
        connection.setEndPointShape (PointShape.NONE); //SQUARE_FILLED_BIG
        connection.setSourceAnchor (AnchorFactory.createRectangularAnchor (CalloutWidget.this));
        connection.setTargetAnchor (AnchorFactory.createRectangularAnchor(pinWidget));
        connection.getActions().addAction(ActionFactory.createPopupMenuAction(new PopupMenuProvider() {

            public JPopupMenu getPopupMenu(Widget widget, Point point) {
                return getConnectionPopupMenu(widget, point);
            }
        }));
        layer.addChild (connection);
        getScene().validate();
        getScene().repaint();
    }



    @Override
    public void paintWidget()
    {
        if (getField() == null) return;

        AffineTransform t = getGraphics().getTransform();
        getGraphics().translate(getBorder().getInsets().left, getBorder().getInsets().top);

        Shape clip = getGraphics().getClip();

        Area shape = new Area(clip);
        shape.intersect(new Area(new Rectangle2D.Float(0f,0f,getBounds().width - getBorder().getInsets().left - getBorder().getInsets().right,
                                            getBounds().height - getBorder().getInsets().top - getBorder().getInsets().bottom)));
        
        getGraphics().setClip(shape);

        getField().paint(getGraphics());

        getGraphics().setClip(clip);
        getGraphics().setTransform(t);
     }

    /**
     * @return the field
     */
    public JTextArea getField() {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(JTextArea field) {
        this.field = field;
    }


}
