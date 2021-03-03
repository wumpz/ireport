/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.table.actions;

import com.jaspersoft.ireport.components.table.TableCell;
import com.jaspersoft.ireport.components.table.TableElementNode;
import com.jaspersoft.ireport.components.table.TableObjectScene;
import com.jaspersoft.ireport.components.table.nodes.TableCellNode;
import com.jaspersoft.ireport.components.table.nodes.TableNullCellNode;
import com.jaspersoft.ireport.components.table.widgets.IndicatorWidget;
import com.jaspersoft.ireport.designer.outline.OutlineTopComponent;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.Node;

/**
 *
 * @version $Id: SeparatorDblClickResizeAction.java 0 2010-03-31 13:03:17 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class TableSceneClickAction  extends WidgetAction.Adapter {

    @Override
    public WidgetAction.State mouseReleased(final Widget widget,  final  WidgetAction.WidgetMouseEvent event)
    {

        if (widget instanceof TableObjectScene)
        {
            final TableObjectScene scene = (TableObjectScene)widget.getScene();

            Node tableNode = findNode( scene.getTableElement(), OutlineTopComponent.getDefault().getExplorerManager().getRootContext());

            if (tableNode == null) return WidgetAction.State.REJECTED;

            TableCell cell = ((TableObjectScene)widget).getTableMatrix().getCellAt(event.getPoint());

            Node cellNode = null;
            // Look for the cell...
            if (cell != null)
            {
                cellNode = findNode(cell, tableNode);
            }


            Node  node = (cellNode == null) ? tableNode : cellNode;

            try {
                    // Select the nodes immediatly in case there is a next action looking at them (like popup menu...)

                ExplorerManager manager = ExplorerManager.find( ((TableObjectScene)widget).getJComponent() );
                manager.setSelectedNodes(new Node[]{node});
                } catch (PropertyVetoException ex) {
            }
            

//            // Then execute this code again, since another event may screw up the selection just made.
//            SwingUtilities.invokeLater(new Runnable() {
//
//                    public void run() {
//                            try {
//                               ExplorerManager manager = ExplorerManager.find( ((TableObjectScene)widget).getJComponent() );
//                               manager.setSelectedNodes(new Node[]{node});
//                            } catch (PropertyVetoException ex) {
//
//                            }
//                        }
//                });

            // If the cell is null, the table should be selected...

            if (cellNode == null)
            {
                Point p = event.getPoint();
                BaseColumn column = scene.getTableMatrix().getColumnAt(p);


                if (column != null)
                {
                    Rectangle r = scene.getTableMatrix().getColumnBounds(column);
                    if (r != null)
                    {
                        scene.getIndicatorsLayer().removeChildren();
                        IndicatorWidget cw =  new IndicatorWidget(scene, column, IndicatorWidget.COLUMN);
                        cw.setPreferredLocation(new Point(r.x, -10));
                        cw.setPreferredBounds(new Rectangle(0,0, r.width,8));
                        cw.revalidate(true);
                        scene.getIndicatorsLayer().addChild(cw);
                        cw.revalidate();
                        scene.validate();
                        
                        /*
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                    
                                    
                                    
                                }
                        });
                         */
                    }
                }
            }


            
            // set the table component as selected object in the main tree...
            
            // if we selected a column let's highlight it!!!
            
            
        }
        return WidgetAction.State.REJECTED; // let someone use it...
    }


    public Node findNode(JRDesignComponentElement element, Node root)
    {
        if (root instanceof TableElementNode && ((TableElementNode)root).getElement() == element) return root;
        // Look in childrens...

        Node[] nodes = root.getChildren().getNodes();
        for (Node node : nodes)
        {
            Node res = findNode(element, node);
            if (res != null) return res;
        }
        return null;
    }

    public Node findNode(TableCell tc, Node root)
    {
        DesignCell cell = tc.getCell();
        BaseColumn column = tc.getColumn();
        int section = tc.getType();
        String groupName = tc.getGroupName();

        if (root instanceof TableCellNode && ((TableCellNode)root).getCell() == cell) return root;
        if (root instanceof TableNullCellNode)
        {
            TableNullCellNode nullNode = (TableNullCellNode)root;
            if (nullNode.getSection() == section && nullNode.getColumn() == column)
            {
                if ( (groupName == null &&  nullNode.getGroup() == null) || (nullNode.getGroup() != null && nullNode.getGroup().getName().equals(groupName)))
                {
                    return root;
                }
            }
        }
        // Look in childrens...

        Node[] nodes = root.getChildren().getNodes();
        for (Node node : nodes)
        {
            Node res = findNode(tc, node);
            if (res != null) return res;
        }
        return null;
    }

}

