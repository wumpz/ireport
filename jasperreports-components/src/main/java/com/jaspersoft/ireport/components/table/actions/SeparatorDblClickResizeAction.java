/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.table.actions;

import com.jaspersoft.ireport.components.table.TableCell;
import com.jaspersoft.ireport.components.table.TableMatrix;
import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.components.table.TableObjectScene;
import com.jaspersoft.ireport.components.table.undo.TableCellResizeUndoableEdit;
import com.jaspersoft.ireport.components.table.widgets.TableCellSeparatorWidget;
import com.jaspersoft.ireport.designer.IReportManager;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.List;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.SeparatorWidget.Orientation;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @version $Id: SeparatorDblClickResizeAction.java 0 2010-03-31 13:03:17 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class SeparatorDblClickResizeAction  extends WidgetAction.Adapter {

    @Override
    public WidgetAction.State mousePressed(Widget widget,   WidgetAction.WidgetMouseEvent event)
    {
        if (event.getButton() == MouseEvent.BUTTON1 &&
            event.getClickCount() == 2 &&
            widget instanceof TableCellSeparatorWidget)
        {

            TableCellSeparatorWidget separator = ((TableCellSeparatorWidget)widget);
            TableObjectScene scene = (TableObjectScene)separator.getScene();

            List<TableCell> cells = scene.getTableMatrix().getCells();
            TableMatrix matrix = scene.getTableMatrix();

            int delta = 0; // Difference between the current position the the high required to fit the elements...

            java.util.List<TableCellResizeUndoableEdit> undos = new java.util.ArrayList<TableCellResizeUndoableEdit>();

            if (separator.getOrientation() == Orientation.HORIZONTAL)
            {
                // set the new cell hight for all the cells in this row...

                delta = - matrix.getHorizontalSeparators().get(separator.getIndex() );

                // Find the biggest delta (delta >= -rowHeight)
                for (TableCell cell : cells)
                {
                    if (cell.getRow()+cell.getRowSpan() == separator.getIndex()) // this cell uses this separator as bottom
                    {
                        // Calculate the min separator position to fit all elements...
                        Rectangle bounds = matrix.getCellBounds(cell);

                        if (cell.getCell() != null)
                        {
                            int base = bounds.y; // top position
                            int currentBottom = bounds.y + bounds.height;

                            int bestCellHeight = 0;

                            List children = cell.getCell().getChildren();
                            for (int i=0; i<children.size(); ++i)
                            {
                                if (children.get(i) instanceof JRDesignElement)
                                {
                                    JRDesignElement ele = (JRDesignElement)children.get(i);
                                    if (ele.getY() + ele.getHeight() > bestCellHeight)
                                    {
                                        bestCellHeight = ele.getY() + ele.getHeight();
                                    }
                                }
                            }

                            if (bestCellHeight == 0 && bestCellHeight-delta <= 0)  bestCellHeight = bounds.height;

                            if (delta < bestCellHeight - bounds.height)
                            {
                                delta = bestCellHeight - bounds.height;
                            }
                        }
                        else
                        {
                            if (delta < -bounds.height)
                            {
                                delta = -bounds.height;
                            }
                        }
                    }
                }

                if (delta != 0)
                {

                    DesignCell firstNotNullCell = null;
                    for (TableCell cell : cells)
                    {
                        if (cell.getRow()+cell.getRowSpan() == separator.getIndex() && cell.getCell() != null) // this cell uses this separator as bottom
                        {
                            if (firstNotNullCell == null) firstNotNullCell = cell.getCell();
                            int oldHeight = matrix.getHorizontalSeparators().get(separator.getIndex()) - matrix.getHorizontalSeparators().get(cell.getRow());
                            int newHeight = oldHeight+delta;
                            oldHeight = cell.getCell().getHeight();
                            cell.getCell().setHeight(newHeight);
                            undos.add( new TableCellResizeUndoableEdit(scene.getTable(), scene.getJasperDesign(),  cell.getCell(),"Height",Integer.class, oldHeight, newHeight ) );
                        }
                    }
                    if (firstNotNullCell != null)
                        firstNotNullCell.getEventSupport().firePropertyChange("ROW_HEIGHT", null, 0);

                    return WidgetAction.State.CONSUMED;
                }
            }
            else
            {

                delta = - matrix.getVerticalSeparators().get(separator.getIndex() );

                // Find the biggest delta (delta >= -rowHeight)
                for (TableCell cell : cells)
                {
                    if (cell.getCol()+cell.getColSpan() == separator.getIndex()) // this cell uses this separator as bottom
                    {
                        // Calculate the min separator position to fit all elements...
                        Rectangle bounds = matrix.getCellBounds(cell);

                        if (cell.getCell() != null)
                        {
                            int base = bounds.x; // left position
                            int currentRight = bounds.x + bounds.width;

                            int bestCellWidth = 0;

                            List children = cell.getCell().getChildren();
                            for (int i=0; i<children.size(); ++i)
                            {
                                if (children.get(i) instanceof JRDesignElement)
                                {
                                    JRDesignElement ele = (JRDesignElement)children.get(i);
                                    if (ele.getX() + ele.getWidth() > bestCellWidth)
                                    {
                                        bestCellWidth = ele.getX() + ele.getWidth();
                                    }
                                }
                            }

                            if (bestCellWidth == 0 && bestCellWidth-delta <= 0)  bestCellWidth = bounds.width;

                            if (delta < bestCellWidth - bounds.width)
                            {
                                delta = bestCellWidth - bounds.width;
                            }
                        }
                        else
                        {
                            if (delta < -bounds.width)
                            {
                                delta = -bounds.width;
                            }
                        }
                    }
                }

                if (delta != 0)
                {


                    StandardBaseColumn firstNotNullColumn = null;
                    for (TableCell cell : cells)
                    {
                        if (cell.getType() == TableCell.TABLE_HEADER &&
                            cell.getCol()+cell.getColSpan() == separator.getIndex())
                        {
                            int oldWidth = matrix.getVerticalSeparators().get(separator.getIndex()) - matrix.getVerticalSeparators().get(cell.getCol());
                            int newWidth = oldWidth + delta;
                            oldWidth = cell.getColumn().getWidth();

                            ((StandardBaseColumn)cell.getColumn()).setWidth(newWidth);
                            firstNotNullColumn = (StandardBaseColumn)cell.getColumn();
                            undos.add( new TableCellResizeUndoableEdit(scene.getTable(), scene.getJasperDesign(),((StandardBaseColumn)cell.getColumn()),"Width",Integer.class, oldWidth, newWidth ) );
                        }
                    }
                    if (firstNotNullColumn != null)
                        firstNotNullColumn.getEventSupport().firePropertyChange("COLUMN_WIDTH", null, 0);
                }

            }
            
            if (delta != 0 && undos.size() > 0)
            {
                // Change the position of all the elements under the
                // modified line or on his right...
                TableCellResizeUndoableEdit mainUndo = undos.get(0);

                mainUndo.setMain(true);

                for (int i=1; i<undos.size(); ++i)
                {
                    TableCellResizeUndoableEdit undo = undos.get(i);
                    mainUndo.concatenate(undo);
                }

                IReportManager.getInstance().addUndoableEdit(mainUndo, false);
                TableModelUtils.fixTableLayout(scene.getTable(), scene.getJasperDesign());
                return WidgetAction.State.CONSUMED;
            }
        }

        return WidgetAction.State.CONSUMED; // let someone use it...
    }

}

