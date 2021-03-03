/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.table.widgets;

import com.jaspersoft.ireport.components.table.TableCell;
import com.jaspersoft.ireport.components.table.TableMatrix;
import com.jaspersoft.ireport.components.table.TableObjectScene;

import com.jaspersoft.ireport.components.table.actions.TableColumnMoveAction;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.List;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.ImageUtilities;

/**
 *
 * @version $Id: ColumnWidget.java 0 2010-03-31 19:41:22 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class IndicatorWidget extends Widget {

    public static final int COLUMN = 0;
    public static final int ROW  = 1;
    public static final int SECTION  = 2;
    public static final int COLUMN_GROUP = 3;

    public static TableColumnMoveAction moveAction = null;
    public static Cursor openHandCursor = null;
    public static Cursor closedHandCursor = null;

    private BufferedImage draggedColumnImage = null;
    private Image arrowImage = null;
    private int lastIndicatedIndex = -1;



    private boolean movingColumnMode = false;
    private int type = COLUMN;
    private Object data = null;

    public IndicatorWidget(TableObjectScene scene, Object data, int type)
    {
        super(scene);
        this.type = type;
        this.data = data;
        if (type == COLUMN)
        {
            this.setCursor( getOpenHandCursor() );
            getActions().addAction(getMoveAction());
        }

    }

    public static Cursor getOpenHandCursor()
    {
        if (openHandCursor == null)
        {
            openHandCursor = createOptimizedCursor("com/jaspersoft/ireport/components/table/hand_open.png","handOpen");
        }
        return openHandCursor;
    }

    public static Cursor getClosedHandCursor()
    {
        if (closedHandCursor == null)
        {
            closedHandCursor = createOptimizedCursor("com/jaspersoft/ireport/components/table/hand_closed.png","handClosed");
        }
        return closedHandCursor;
    }

    private static Cursor createOptimizedCursor(String imageName, String cursorName)
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image image = ImageUtilities.loadImage(imageName);

        Dimension size = tk.getBestCursorSize(image.getWidth(null), image.getWidth(null));
        BufferedImage cursorImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        cursorImage.getGraphics().drawImage(image, 0, 0, null);

        return tk.createCustomCursor(cursorImage, new Point(8,6),cursorName);
    }

    public static TableColumnMoveAction getMoveAction()
    {
        if (moveAction == null) moveAction = new TableColumnMoveAction();
        return moveAction; 
    }

    @Override
    protected void paintWidget() {
        super.paintWidget();

        Rectangle rect = getBounds();
        Paint oldPaint = this.getGraphics().getPaint();

        //this.getGraphics().setPaint(new Color(255,255,255,196));
        //this.getGraphics().fillRect(rect.x, rect.y, rect.width, rect.height);

        if (getType() == COLUMN)
        {
            this.getGraphics().setPaint(new Color(136,104,176));
            this.getGraphics().fillRect(rect.x, rect.y+1, rect.width, 4);
            this.getGraphics().fillRect(rect.x, rect.y, 2, 8);
            this.getGraphics().fillRect(rect.x+rect.width-2, rect.y, 2, 8);

            //Color c = this.getGraphics().getColor();
            //this.getGraphics().setColor(Color.BLACK);
            //TableMatrix matrix = ((TableObjectScene)getScene()).getTableMatrix();
            //this.getGraphics().drawString(""+matrix.getColumnIndex( (BaseColumn)getData() ), getBounds().width/2, 8);
            //this.getGraphics().setColor(c);

            if (isMovingColumnMode())
            {
                // find the closest vertical line to mid...
                TableMatrix matrix = ((TableObjectScene)getScene()).getTableMatrix();

                // Paint all the cells
                if (draggedColumnImage != null)
                {
                    this.getGraphics().drawImage(draggedColumnImage, 0, 10, null);
                }

                List<Integer> vertLines = matrix.getVerticalSeparators();
                List<Integer> horizLines = matrix.getHorizontalSeparators();

                int sepIndex = getLastIndicatedIndex();

                if (sepIndex != -1)
                {
                    //this.getGraphics().setColor(Color.red);
                    // Draw only on the border of cells having a border on this line...

                    int minXforArrow = -1;
                    for (TableCell cell : matrix.getCells())
                    {
                        if (cell.getCol() == sepIndex || cell.getCol()+cell.getColSpan() == sepIndex)
                        {
                            int height = horizLines.get(cell.getRow()+cell.getRowSpan()) - horizLines.get(cell.getRow());
                            if (height > 0)
                            {
                                this.getGraphics().fillRect(vertLines.get(sepIndex)-getLocation().x, 10 + horizLines.get(cell.getRow()), 2, height);
                                minXforArrow = (minXforArrow == -1 || (horizLines.get(cell.getRow())+2) < minXforArrow) ? (horizLines.get(cell.getRow())+2) : minXforArrow;
                            }
                        }
                    }
                    
                    if (arrowImage != null && minXforArrow >= 0)
                    {
                        this.getGraphics().drawImage(arrowImage, vertLines.get(sepIndex)-getLocation().x-6, minXforArrow, null);
                    }

                    
                    
                    
                }
            }

        }
        else if (getType() == ROW)
        {
            this.getGraphics().setPaint(new Color(136,104,176));
            this.getGraphics().fillRect(rect.x+1, rect.y, 4, rect.height);
            this.getGraphics().fillRect(rect.x, rect.y, rect.width, 2);
            this.getGraphics().fillRect(rect.x, rect.y+rect.height-2, rect.width, 2);
        }
        else if (getType() == SECTION)
        {
            this.getGraphics().setPaint(new Color(176,104,122));
            this.getGraphics().fillRect(rect.x+1, rect.y, 4, rect.height);
            this.getGraphics().fillRect(rect.x, rect.y, rect.width, 2);
            this.getGraphics().fillRect(rect.x, rect.y+rect.height-2, rect.width, 2);
        }
        else if (getType() == COLUMN_GROUP)
        {
            this.getGraphics().setPaint(new Color(176,158,104));
            this.getGraphics().fillRect(rect.x+1, rect.y, 4, rect.height);
            this.getGraphics().fillRect(rect.x, rect.y, rect.width, 2);
            this.getGraphics().fillRect(rect.x, rect.y+rect.height-2, rect.width, 2);
        }

        this.getGraphics().setPaint(oldPaint);
    }

    /**
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @return the movingColumnMode
     */
    public boolean isMovingColumnMode() {
        return movingColumnMode;
    }

    /**
     * @param movingColumnMode the movingColumnMode to set
     */
    public void setMovingColumnMode(boolean movingColumnMode) {

        if (type != COLUMN) return;

        if (this.movingColumnMode  == movingColumnMode) return;
        
        if (movingColumnMode)
        {
            TableMatrix matrix = ((TableObjectScene)getScene()).getTableMatrix();
            List<Integer> horisLines = matrix.getHorizontalSeparators();
            int height = horisLines.get(horisLines.size()-1) - getPreferredLocation().y;
            Rectangle newBounds = getPreferredBounds();
            newBounds.height = height;
            setLastIndicatedIndex(-1);

            draggedColumnImage = new BufferedImage(newBounds.width, horisLines.get(horisLines.size()-1), BufferedImage.TYPE_INT_ARGB);
            // Paint the current column here, and remove unwanted cells...
            // Look at all the cells of this column and childrens...
            if (getData() instanceof BaseColumn)
            {
                BaseColumn column = (BaseColumn)getData();
                Graphics2D gb = (Graphics2D) draggedColumnImage.getGraphics();
                //gb.setColor(Color.RED);
                //gb.drawRect(0,0,newBounds.width-1,horisLines.get(horisLines.size()-1)-1);
                //getScene().paint(gb);
                gb.translate(getScene().getBounds().x-getLocation().x, getScene().getBounds().y);
                gb.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
                for (TableCell cell : matrix.getCells())
                {
                    if (cell.getColumn() == column || TableMatrix.isAncestorColumnOf(cell.getColumn(), column) )
                    {
                        //Paing the scene at the cell position in the buffered Image...
                        Rectangle cellBonds = matrix.getCellBounds(cell);
                        gb.setClip(cellBonds.x-getScene().getBounds().x, cellBonds.y-getScene().getBounds().y, cellBonds.width, cellBonds.height);
                        getScene().paint(gb);
                    }
                }
            }

            if (arrowImage == null)
            {
                arrowImage = ImageUtilities.loadImage("com/jaspersoft/ireport/components/table/arrow.png");
            }

            setPreferredBounds(newBounds);
            this.revalidate();
            getScene().revalidate();
        }
        else
        {
            draggedColumnImage = null;
            this.setPreferredBounds(new Rectangle(0,0,getPreferredBounds().width,8));
        }

        this.movingColumnMode = movingColumnMode;
    }

    /**
     * @return the lastIndicatedIndex
     */
    public int getLastIndicatedIndex() {
        return lastIndicatedIndex;
    }

    /**
     * @param lastIndicatedIndex the lastIndicatedIndex to set
     */
    public void setLastIndicatedIndex(int lastIndicatedIndex) {
        this.lastIndicatedIndex = lastIndicatedIndex;
    }

  
}
