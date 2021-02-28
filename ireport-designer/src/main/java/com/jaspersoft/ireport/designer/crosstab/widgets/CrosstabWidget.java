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
package com.jaspersoft.ireport.designer.crosstab.widgets;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.borders.ReportBorder;
import com.jaspersoft.ireport.designer.crosstab.CrosstabObjectScene;
import com.jaspersoft.ireport.designer.palette.actions.DefaultCellElementsLayout;
import com.jaspersoft.ireport.designer.utils.Java2DUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabGroup;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.convert.ReportConverter;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import org.netbeans.api.visual.widget.Widget;


/**
 *
 * @author gtoffoli
 */
public class CrosstabWidget extends Widget implements PropertyChangeListener {
    
    public static Color CELL_LABEL_COLOR = new Color(210,210,210, 100);
    
    private int gridSize = 13;
    private JRDesignCrosstab crosstab = null;
    private TexturePaint gridTexture = null;
    private static final BasicStroke GRID_STROKE = new BasicStroke(0, BasicStroke.CAP_SQUARE,
            BasicStroke.JOIN_BEVEL, 1.0f, new float[]{2f,2f}, 0.0f);
    
    public CrosstabWidget(CrosstabObjectScene scene, JRDesignCrosstab crosstab) {
        super(scene);
        this.crosstab = crosstab;

        setBorder(new ReportBorder());
        setBackground(Color.WHITE);
        setOpaque(true);
        setCheckClipping(true);
        updateBounds();
        
        crosstab.getEventSupport().addPropertyChangeListener(this);
        updateCellListeners();
    }
    
    public void updateBounds()
    {
        int width = getCrosstabDesignWidth();
        width = Math.max(width, getCrosstab().getWidth());
        
        // Design height calculation...
        
        int height = getCrosstabDesignHeight();
        height = Math.max(height, getCrosstab().getHeight());
        
        this.setPreferredSize(new Dimension( width+20, height+20 ));
    }
    
    /*
    public Rectangle  calculateClientArea()
    {
        return new Rectangle(getJasperDesign().getPageWidth() + 20, getDesignHeight()+20 );
    }
    */
    
    public JasperDesign getJasperDesign()
    {
        return ((AbstractReportObjectScene)this.getScene()).getJasperDesign();
    }
    
    

    @Override
    protected void paintWidget() {
        super.paintWidget();
        
        Graphics2D g = this.getGraphics();
        
        if (((AbstractReportObjectScene)getScene()).isGridVisible())
        {
            paintGrid(g);
        }
    
        Stroke oldStroke = g.getStroke();
        //g.setStroke(new BasicStroke(0));

        double zoom = getScene().getZoomFactor();
        Stroke bs = Java2DUtils.getInvertedZoomedStroke(oldStroke, getScene().getZoomFactor());
        g.setStroke(bs);
        
        
        g.setFont( new Font( "Arial", Font.PLAIN, 8));
        
        // Paint the cell margins....
        
        //1. The crosstab header cell...
        int header_width = ModelUtils.getHeaderCellWidth(getCrosstab());
        int header_height = ModelUtils.getHeaderCellHeight(getCrosstab());
        
        JRCrosstabRowGroup[] row_groups = getCrosstab().getRowGroups();
        JRCrosstabColumnGroup[] col_groups = getCrosstab().getColumnGroups();
        
        // Draw this cell...
        paintCell(g, "Crosstab header", 0, 0, getCrosstab().getHeaderCell());
        
        // Paint the data cells...
        
        
        int x = header_width;
        int y = header_height;
        
        //We have to calculate the right order...
        JRCrosstabCell[][] cells = ModelUtils.normalizeCell(getCrosstab().getCells(), row_groups, col_groups);
        
        for (int i=cells.length-1; i>=0; --i)
        {
            x = header_width;
            for (int k=cells[i].length-1; k>=0; --k)
            {
                JRCrosstabCell cell = cells[i][k];
                if (cell != null)
                {
                    String title = (cell.getRowTotalGroup() == null ? "Detail" : cell.getRowTotalGroup()) + " / " +
                                   (cell.getColumnTotalGroup() == null ? "Detail" : cell.getColumnTotalGroup());
                    paintCell(g, title, x, y, cell.getContents());
                }
                x += ModelUtils.findColumnWidth(cells, k);
            }
            
            y += ModelUtils.findRowHeight(cells, i);
        }
        
        int data_width = x - header_width;
        int data_height = y - header_height;
        
        x = 0;
        y = header_height;
        
        
        // paint row cells...
        for (int i=0; i<row_groups.length; ++i)
        {
            switch (row_groups[i].getTotalPositionValue())
            {
                case START:
                    paintCell(g, row_groups[i].getName() + " total header", x, y, row_groups[i].getTotalHeader());
                    data_height -= row_groups[i].getTotalHeader().getHeight();
                    y += row_groups[i].getTotalHeader().getHeight();
                    break;
                case END:
                    int y_loc = y + data_height - row_groups[i].getTotalHeader().getHeight();
                    paintCell(g, row_groups[i].getName() + " total header", x, y_loc, row_groups[i].getTotalHeader());
                    data_height -= row_groups[i].getTotalHeader().getHeight();
                    break;
                case NONE:
                    break;
            }
            
            paintCell(g, row_groups[i].getName() + " header", x, y, row_groups[i].getHeader());
            x += row_groups[i].getHeader().getWidth();
        }
        
        
        x = header_width;
        y = 0;
        
        // paint col cells...
        for (int i=0; i<col_groups.length; ++i)
        {
            switch (col_groups[i].getTotalPositionValue())
            {
                case START:
                    paintCell(g, col_groups[i].getName() + " total header", x, y, col_groups[i].getTotalHeader());
                    data_width -= col_groups[i].getTotalHeader().getWidth();
                    x += col_groups[i].getTotalHeader().getWidth();
                    break;
                case END:
                    int x_loc = x + data_width - col_groups[i].getTotalHeader().getWidth();
                    paintCell(g, col_groups[i].getName() + " total header", x_loc, y, col_groups[i].getTotalHeader());
                    data_width -= col_groups[i].getTotalHeader().getWidth();
                    break;
                case NONE:
                    break;
            }
            
            paintCell(g, col_groups[i].getName() + " header", x, y, col_groups[i].getHeader());
            y += col_groups[i].getHeader().getHeight();
        }
        
        g.setStroke(oldStroke);
    
    }

    
    
    /**
     * Draw a cell painting just the border and the cell name.
     **/
    private void paintCell(Graphics2D g, String title, int x, int y, JRCellContents contents)
    {
        if (contents == null) return;
        int width = contents.getWidth();
        int height = contents.getHeight();

        int txt_height = g.getFontMetrics().getHeight()/2;
        
        Paint oldPaint = g.getPaint();
        Shape oldClip = g.getClip();
        //Java2DUtils.resetClip(g);
        
        g.setPaint(CELL_LABEL_COLOR);
        
        g.drawRect(x, y, width, height);
        g.setPaint(oldPaint);
        
        g.setPaint(AbstractReportObjectScene.DESIGN_LINE_COLOR);
        
        
        
        AffineTransform af = g.getTransform();
        AffineTransform new_af = (AffineTransform) af.clone();
        AffineTransform translate = AffineTransform.getTranslateInstance(
                x,
                y);
        new_af.concatenate(translate);
        g.setTransform(new_af);
        
        
        JRFrame frame = getCrosstabCellFrame(new ReportConverter(getJasperDesign(),true),
                        contents,
                        x,y,
                        x==0 && crosstab.getRunDirectionValue() == RunDirectionEnum.LTR,
			x==0 && crosstab.getRunDirectionValue() == RunDirectionEnum.RTL,
                        false);
        if (frame != null && ((CrosstabObjectScene)this.getScene()).getDrawVisitor() != null)
        {
            ((CrosstabObjectScene)this.getScene()).getDrawVisitor().setGraphics2D(g);
            ((CrosstabObjectScene)this.getScene()).getDrawVisitor().visitFrame(frame);
        }
       
        g.setTransform(af);
        
        g.clipRect(x,
                   y,
                   width-2,
                   height-2);
        
        // TODO: add to the visitore the code to draw a void cell....
        Color cc = contents.getBackcolor() != null ? contents.getBackcolor() : Color.WHITE;
        cc = cc.darker(); //new Color( cc.getRed(), cc.getGreen(), cc.getBlue(), 200);
        g.setPaint(cc);
        
        if (IReportManager.getPreferences().getBoolean( IReportManager.PROPERTY_SHOW_CELL_NAMES, false))
        {
            g.drawString( title, x+3, y + txt_height + 3);  
        }
        
        g.setClip(oldClip);
        g.setPaint(oldPaint);
    }
   
    private JRFrame getCrosstabCellFrame(
		ReportConverter reportConverter,
		JRCellContents cell, 
		int x, 
		int y, 
		boolean left, 
		boolean right, 
		boolean top
		)
    {
            JRDesignFrame frame = new JRDesignFrame(cell.getDefaultStyleProvider());
            frame.setX(x);
            frame.setY(y);
            frame.setWidth(cell.getWidth());
            frame.setHeight(cell.getHeight());

            frame.setMode(cell.getModeValue());
            frame.setBackcolor(cell.getBackcolor());
            //frame.setStyle(reportConverter.resolveStyle(cell));

            JRLineBox box = cell.getLineBox();
            if (box != null)
            {
                    frame.copyBox(box);

                    boolean copyLeft = left && box.getLeftPen().getLineWidth().floatValue() <= 0f && box.getRightPen().getLineWidth().floatValue() > 0f;
                    boolean copyRight = right && box.getRightPen().getLineWidth().floatValue() <= 0f && box.getLeftPen().getLineWidth().floatValue() > 0f;
                    boolean copyTop = top && box.getTopPen().getLineWidth().floatValue() <= 0f && box.getBottomPen().getLineWidth().floatValue() > 0f;

                    if (copyLeft)
                    {
                            ((JRBaseLineBox)frame.getLineBox()).copyLeftPen(box.getRightPen());
                    }

                    if (copyRight)
                    {
                            ((JRBaseLineBox)frame.getLineBox()).copyRightPen(box.getLeftPen());
                    }

                    if (copyTop)
                    {
                            ((JRBaseLineBox)frame.getLineBox()).copyTopPen(box.getBottomPen());
                    }
            }
            return frame;
    }
    
    protected void paintGrid(Graphics2D g) {
        Paint oldPaint = g.getPaint();
        g.setPaint( getGridTexture() );
        
        g.fill( getClientArea() );
        g.setPaint(oldPaint);
    }

    private TexturePaint getGridTexture()
    {
        if ( gridTexture == null || gridTexture.getImage().getWidth() != getGridSize() )
        {
                BufferedImage img = new BufferedImage( getGridSize(), getGridSize(), BufferedImage.TYPE_INT_RGB );
                Graphics2D g2 = img.createGraphics();
                g2.setColor(new Color(255,255,255,255));
                g2.fill( getClientArea() );
                g2.setColor( ReportObjectScene.GRID_LINE_COLOR );
                g2.setStroke( GRID_STROKE );
                g2.drawLine( getGridSize()-1, 0, getGridSize()-1, getGridSize()-1 );
                g2.drawLine( 0, getGridSize()-1, getGridSize()-1, getGridSize()-1 );
                gridTexture = new TexturePaint( img, new Rectangle(0,0, getGridSize(), getGridSize() ) );
        }
        return gridTexture;
    }


    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public JRDesignCrosstab getCrosstab() {
        return crosstab;
    }
   
    
    public int getCrosstabDesignWidth()
    {
        JRCrosstabCell[][] cells = getCrosstab().getCells();
        
        // Calculate the crosstab width...
        int width = 0;
        
        // 1. Add all the rows groups widths...
        JRCrosstabRowGroup[] row_groups = getCrosstab().getRowGroups();
        
        for (int i=0; i<row_groups.length; ++i)
        {
            width += row_groups[i].getWidth();
        }
        // 2. Sum the width of all the not null data cells ...
        for (int i=0; i<cells[0].length; ++i)
        {
            width += ModelUtils.findColumnWidth(cells, i);
        }
        
        return width;
    }
    
    public int getCrosstabDesignHeight()
    {
        JRCrosstabCell[][] cells = getCrosstab().getCells();
        
        // Calculate the crosstab width...
        int height = 0;
        
        JRCrosstabColumnGroup[] column_groups = getCrosstab().getColumnGroups();
        
        for (int i=0; i<column_groups.length; ++i)
        {
            height += column_groups[i].getHeight();
        }
        
        // 2. Sum the width of all the data cells on the first row...
        for (int i=0; i<cells.length; ++i)
        {
            height += ModelUtils.findRowHeight(cells, i);
        }
        
        return height;
    }

    private void updateCellListeners()
    {
        List<JRDesignCellContents> cells = ModelUtils.getAllCells(crosstab);
        for (JRDesignCellContents content : cells)
        {
            if (content != null)
            {
                content.getEventSupport().removePropertyChangeListener(this);
                content.getEventSupport().addPropertyChangeListener(this);
            }
        }
        
        // update groups listeners too...
        JRCrosstabGroup[] groups = crosstab.getRowGroups();
        for (int i=0; i<groups.length; ++i)
        {
            ((JRDesignCrosstabGroup)groups[i]).getEventSupport().removePropertyChangeListener(this);
            ((JRDesignCrosstabGroup)groups[i]).getEventSupport().addPropertyChangeListener(this);
        }
        
        groups = crosstab.getColumnGroups();
        for (int i=0; i<groups.length; ++i)
        {
            ((JRDesignCrosstabGroup)groups[i]).getEventSupport().removePropertyChangeListener(this);
            ((JRDesignCrosstabGroup)groups[i]).getEventSupport().addPropertyChangeListener(this);
        }
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        
        if (evt.getPropertyName().equals( JRDesignCrosstab.PROPERTY_CELLS) ||
            evt.getPropertyName().equals( JRDesignCrosstab.PROPERTY_HEADER_CELL) ||
            evt.getPropertyName().equals( JRDesignCrosstab.PROPERTY_ROW_GROUPS) ||
            evt.getPropertyName().equals( JRDesignCrosstab.PROPERTY_COLUMN_GROUPS) ||
            evt.getPropertyName().equals( JRDesignCrosstab.PROPERTY_WHEN_NO_DATA_CELL) ||
            evt.getPropertyName().equals( JRDesignCrosstabGroup.PROPERTY_TOTAL_POSITION) ||
            evt.getPropertyName().equals( JRDesignCrosstabGroup.PROPERTY_HEADER) ||
            evt.getPropertyName().equals( JRDesignCrosstabGroup.PROPERTY_TOTAL_HEADER))
       {
            updateCellListeners();
            SwingUtilities.invokeLater( new Runnable() {

               public void run() {
                    ((CrosstabObjectScene)getScene()).rebuildDocument();
               }
           });
           return;
       }
        
       boolean refreshCell = false;
       final JRDesignCellContents cellContent;
       if (evt.getPropertyName().equals( JRDesignCellContents.PROPERTY_CHILDREN))
       {
           refreshCell = true;
           if (evt.getSource() instanceof JRDesignCellContents)
           {
               cellContent = (JRDesignCellContents)evt.getSource();
           }
           else
           {
               cellContent = null;
           }
       }
       else if (evt.getPropertyName().equals( JRDesignCellContents.PROPERTY_STYLE) ||
           evt.getPropertyName().equals( JRDesignCellContents.PROPERTY_BOX) ||
           evt.getPropertyName().equals( JRBaseStyle.PROPERTY_MODE) ||
           evt.getPropertyName().equals( JRBaseStyle.PROPERTY_BACKCOLOR))
       {
           refreshCell = true;
           cellContent = null;
       }
       else
       {
           cellContent = null;
       }
        
       if (refreshCell)
       {
            SwingUtilities.invokeLater( new Runnable() {

               public void run() {
                   // TODO: refresh groups....
                    ((CrosstabObjectScene)getScene()).refreshCells();
                    // Recalculate spaces...
                    if (cellContent != null && !IReportManager.getPreferences().getBoolean("disableCrosstabAutoLayout", false))
                    {
                        DefaultCellElementsLayout.doLayout(cellContent, (CrosstabObjectScene)getScene());
                    }
               }
           });
       }
    }
    
}
