/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.callouts;



import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.ImageUtilities;

/**
 *
 * @version $Id: PinWidget.java 0 2010-01-19 15:14:44 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class PinWidget extends ImageWidget {

    private ConnectionWidget connectionWidget = null;

    public PinWidget(Scene scene)
    {
        this(scene, null);
    }

    public PinWidget(Scene scene, ConnectionWidget connection)
    {
        super(scene, ImageUtilities.loadImage("com/jaspersoft/ireport/addons/callouts/pointer.png"));
        this.connectionWidget = connection;
        getActions().addAction(ActionFactory.createMoveAction(ActionFactory.createFreeMoveStrategy(), new MoveProvider() {

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

        getActions().addAction(ActionFactory.createPopupMenuAction(new PopupMenuProvider() {

                public JPopupMenu getPopupMenu(Widget widget, Point point) {
                    return getConnectionPopupMenu(widget, point);
                }
            }));
    }


    /**
     * @return the connectionWidget
     */
    public ConnectionWidget getConnectionWidget() {
        return connectionWidget;
    }

    /**
     * @param connectionWidget the connectionWidget to set
     */
    public void setConnectionWidget(ConnectionWidget connectionWidget) {
        this.connectionWidget = connectionWidget;
    }


    public JPopupMenu getConnectionPopupMenu (Widget widget, Point localLocation)
    {
        final Widget pinWidget = widget;

            JPopupMenu connectionPopupMenu = new JPopupMenu();
            JMenuItem removeConnection = new JMenuItem("Delete pin");
            removeConnection.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                        if (pinWidget instanceof PinWidget) // The pin...
                        {
                            if (((PinWidget)pinWidget).getConnectionWidget() != null)
                            {
                                ((PinWidget)pinWidget).getConnectionWidget().removeFromParent();
                            }
                            pinWidget.removeFromParent();
                        }
                    }
                });

            connectionPopupMenu.add(removeConnection);

        return connectionPopupMenu;
    }
}
