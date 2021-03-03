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
package com.jaspersoft.ireport.components.table.widgets;

import com.jaspersoft.ireport.components.table.DefaultTableCellElementsLayout;
import com.jaspersoft.ireport.components.table.TableCell;
import com.jaspersoft.ireport.components.table.TableMatrix;
import com.jaspersoft.ireport.components.table.TableObjectScene;
import com.jaspersoft.ireport.components.table.nodes.TableChildren;
import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.borders.ReportBorder;
import com.jaspersoft.ireport.designer.utils.Java2DUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
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
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.draw.DrawVisitor;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.ImageUtilities;


/**
 *
 * @author gtoffoli
 */
public class TableWidget extends Widget implements PropertyChangeListener {
    
    public static Color CELL_LABEL_COLOR = AbstractReportObjectScene.DESIGN_LINE_COLOR;

    int cellListenersCount = 0;
    // Main model
    private StandardTable table = null;
    private JRDesignComponentElement element = null;
    JRDesignDataset currentDataset = null;
    private PropertyChangeListener layoutListener = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {

            if (evt.getPropertyName().equals(StandardBaseColumn.PROPERTY_WIDTH))
            {
                return; // Ignore this...
            }

            layoutChanged(evt);
        }
    };

    PropertyChangeListener datasetRunChangeListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                 datasetPropertyChanged(evt);
            }
    };

    private PropertyChangeListener cellLayoutListener = new PropertyChangeListener() {

        public void propertyChange(final PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(DesignCell.PROPERTY_CHILDREN))
            {
                removeCellListeners();
                addCellListeners();

                SwingUtilities.invokeLater( new Runnable() {

                   public void run() {
                       ((TableObjectScene)getScene()).refreshCells();
                       if (evt.getSource() != null && evt.getSource() instanceof DesignCell && !IReportManager.getPreferences().getBoolean("disableCrosstabAutoLayout", false))
                        {
                            DefaultTableCellElementsLayout.doLayout((DesignCell)evt.getSource(), (TableObjectScene)getScene());
                        }
                   }
               });

            }
            else if (evt.getPropertyName().equals("ROW_HEIGHT"))
            {
                layoutChanged(evt);
            }
            else
            {
                // just refresh the widget...
                TableWidget.this.revalidate(true);
                TableWidget.this.getScene().validate();
            }
        }
    };




    //--------- GRID STUFF ------------
    private  TexturePaint gridTexture = null;
    private  static  TexturePaint nullAreaTexture = null;
    private  static  TexturePaint nullTableAreaTexture = null;
    private final BasicStroke GRID_STROKE = new BasicStroke(0, BasicStroke.CAP_SQUARE,
            BasicStroke.JOIN_BEVEL, 1.0f, new float[]{2f,2f}, 0.0f);
    private  int gridSize = 13;


    
    public TableWidget(TableObjectScene scene, JRDesignComponentElement element) {
        super(scene);
        this.element = element;
        this.table = (StandardTable) element.getComponent();

        setBorder(new ReportBorder());
        setBackground(Color.WHITE);
        setOpaque(true);
        setCheckClipping(true);
        updateBounds();

        //table.getEventSupport().addPropertyChangeListener(this);
        table.getEventSupport().addPropertyChangeListener(layoutListener);
        element.getEventSupport().addPropertyChangeListener(this);
        addColumnsListeners(table.getColumns());
        addCellListeners();
        updateDatasetRunListeners();
    }

    public void addColumnsListeners(List<BaseColumn> columns)
    {
        for (BaseColumn column : columns)
        {
            ((StandardBaseColumn)column).getEventSupport().addPropertyChangeListener(layoutListener);
            if (column instanceof StandardColumnGroup)
            {
                addColumnsListeners(((StandardColumnGroup)column).getColumns());
            }
        }
    }

    public void removeColumnsListeners(List<BaseColumn> columns)
    {
        for (BaseColumn column : columns)
        {
            ((StandardBaseColumn)column).getEventSupport().removePropertyChangeListener(layoutListener);
            if (column instanceof StandardColumnGroup)
            {
                removeColumnsListeners(((StandardColumnGroup)column).getColumns());
            }
        }
    }

    public void addCellListeners()
    {
        
        List<TableCell> cells = ((TableObjectScene)getScene()).getTableMatrix().getCells();
        for (TableCell cell : cells)
        {
            if (cell.getCell() != null)
            {
                cell.getCell().getEventSupport().addPropertyChangeListener(cellLayoutListener);
            }
        }
    }

    public void removeCellListeners()
    {
        List<TableCell> cells = ((TableObjectScene)getScene()).getTableMatrix().getCells();
        for (TableCell cell : cells)
        {
            if (cell.getCell() != null)
            {
                cell.getCell().getEventSupport().removePropertyChangeListener(cellLayoutListener);
            }
        }
    }
    
    public void updateBounds()
    {
        int width = ((TableObjectScene)getScene()).getTableMatrix().getTableDesignWidth();
        width = Math.max(width, getElement().getWidth());
        
        // Design height calculation...
        
        int height = ((TableObjectScene)getScene()).getTableMatrix().getTableDesignHeight();
        height = Math.max(height, getElement().getHeight());
        
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

        Rectangle tableArea = getBounds(); // new Rectangle(new Dimension(getElement().getWidth(), getElement().getHeight()));
        // Fill the background with the null grid...
         g.setPaint(new Color(232,232,234,64));
        g.fill(tableArea);
        g.setPaint(Color.WHITE);

        Rectangle designArea = new Rectangle(0, 0,
                ((TableObjectScene)getScene()).getTableMatrix().getTableDesignWidth(),
                ((TableObjectScene)getScene()).getTableMatrix().getTableDesignHeight());

        g.fill(designArea);
        


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

        TableMatrix matrix = ((TableObjectScene)getScene()).getTableMatrix();
        List<TableCell> cells = matrix.getCells();
        
        for (TableCell tc : cells)
        {
            Rectangle bounds = matrix.getCellBounds(tc);
            paintCell(this, g,tc.getCell(), bounds.x,bounds.y,bounds.width,bounds.height);
        }


        int width = ((TableObjectScene)getScene()).getTableMatrix().getTableDesignWidth();
        int height = ((TableObjectScene)getScene()).getTableMatrix().getTableDesignHeight();
        paintFrame(this, g, null , element.getStyle() ,0,0, width, height);

        g.setStroke(oldStroke);
    
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


    private  static TexturePaint getNullAreaTexture()
    {
        if ( nullAreaTexture == null )
        {
                Image img2 = ImageUtilities.loadImage("com/jaspersoft/ireport/components/table/null_area.png");
                BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                img.getGraphics().drawImage(img2, 0, 0, null);
                nullAreaTexture = new TexturePaint( img, new Rectangle(0,0, 16, 16) );
        }
        return nullAreaTexture;
    }

    private  static TexturePaint getNullTableAreaTexture()
    {
        if ( nullTableAreaTexture == null )
        {
                Image img2 = ImageUtilities.loadImage("com/jaspersoft/ireport/components/table/null_table_area.png");
                BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                img.getGraphics().drawImage(img2, 0, 0, null);
                nullTableAreaTexture = new TexturePaint( img, new Rectangle(0,0, 16, 16) );
        }
        return nullTableAreaTexture;
    }


    public  int getGridSize() {
        return gridSize;
    }

    public  void setGridSize(int size) {
        gridSize = size;
    }


    public void propertyChange(PropertyChangeEvent evt) {
        
       if (evt.getPropertyName().equals( JRDesignComponentElement.PROPERTY_HEIGHT) ||
           evt.getPropertyName().equals( JRDesignComponentElement.PROPERTY_WIDTH))
       {
            updateBounds();
            revalidate();
            getScene().validate();
       }
    }

    public void layoutChanged(PropertyChangeEvent evt)
    {
        ((TableObjectScene)getScene()).rebuildDocument();
        
        removeColumnsListeners(table.getColumns());
        removeCellListeners();
        addColumnsListeners(table.getColumns());
        addCellListeners();


        /*
        SwingUtilities.invokeLater( new Runnable() {

           public void run() {
               System.out.println("Rebuilding document...");
               System.out.flush();
                
           }
       });
         */
       return;

    }

    /**
     * @return the element
     */
    public JRDesignComponentElement getElement() {
        return element;
    }

    /**
     * @param element the element to set
     */
    public void setElement(JRDesignComponentElement element) {
        this.element = element;
    }

    /**
     * @return the table
     */
    public StandardTable getTable() {
        return table;
    }

    /**
     * @param table the table to set
     */
    public void setTable(StandardTable table) {
        this.table = table;
    }


    public static void paintCell(Widget widget, Graphics2D g, Cell cell, int x, int y, int width, int height) {
        
        Paint oldPaint = g.getPaint();
        g.setColor(CELL_LABEL_COLOR);
        
        if (cell == null)
        {
            g.setPaint(getNullAreaTexture());
            g.fillRect(x, y, width, height);
            g.setPaint(CELL_LABEL_COLOR);
            g.drawRect(x, y, width, height);
            return;
        }

        g.drawRect(x, y, width, height);

        // paint frame...
        g.setPaint(AbstractReportObjectScene.DESIGN_LINE_COLOR);
        paintFrame(widget, g, cell.getLineBox(),cell.getStyle(),x,y,width,height);
        g.setPaint(oldPaint);
}



    private static void  paintFrame(Widget widget, Graphics2D g, JRLineBox box, JRStyle style ,int x, int y, int w, int h)
    {
            JRDesignFrame frame = new JRDesignFrame(((TableObjectScene)widget.getScene()).getJasperDesign());
            frame.setX(x);
            frame.setY(y);
            frame.setWidth(w);
            frame.setHeight(h);
            frame.setStyle(style);

            if (box != null)
            {
                frame.copyBox(box);
            }
            
            Paint oldPaint = g.getPaint();
            Shape oldClip = g.getClip();

            // get the clip of the undeformed gfx...
            AffineTransform af = g.getTransform();

            /*
            Rectangle bounds = g.getClipBounds();
            bounds.x = x-2;
            bounds.x = x-2;
            bounds.width = w+4;
            bounds.height = h+42;

            g.setClip(bounds);
            */
            g.setTransform(af);
            
            AffineTransform new_af = (AffineTransform) af.clone();
            AffineTransform translate = AffineTransform.getTranslateInstance(x,y);
            new_af.concatenate(translate);
            g.setTransform(new_af);


            if (frame != null)
            {
                DrawVisitor dv = ((TableObjectScene)widget.getScene()).getDrawVisitor();
                dv.setGraphics2D(g);
                dv.visitFrame(frame);
            }

            g.setTransform(af);
            g.setClip(oldClip);
            g.setPaint(oldPaint);

    }


    private void updateDatasetRunListeners() {

        table.getEventSupport().removePropertyChangeListener(StandardTable.PROPERTY_DATASET_RUN,datasetRunChangeListener);
        table.getEventSupport().addPropertyChangeListener(StandardTable.PROPERTY_DATASET_RUN, datasetRunChangeListener);

        // Add listeners to the dataset poitned by this dataset run...
        JRDesignDataset dataset = null;
        if (table.getDatasetRun() != null && table.getDatasetRun().getDatasetName() != null)
        {
            dataset = (JRDesignDataset) getJasperDesign().getDatasetMap().get(table.getDatasetRun().getDatasetName());
        }

        if (dataset == currentDataset) return;
        if (dataset != currentDataset)
        {
            if (currentDataset != null)
            {
                currentDataset.getEventSupport().removePropertyChangeListener(datasetRunChangeListener);

                // remove all the old group listeners...
                List groups = currentDataset.getGroupsList();
                for (int i=0; i<groups.size(); ++i)
                {
                    JRDesignGroup group = new JRDesignGroup();
                    group.getEventSupport().removePropertyChangeListener(JRDesignGroup.PROPERTY_NAME, datasetRunChangeListener);
                }
            }

            currentDataset = dataset;
            dataset.getEventSupport().addPropertyChangeListener(datasetRunChangeListener);

            // remove all the old group listeners...
            List groups = currentDataset.getGroupsList();
            for (int i=0; i<groups.size(); ++i)
            {
                JRDesignGroup group = new JRDesignGroup();
                group.getEventSupport().removePropertyChangeListener(JRDesignGroup.PROPERTY_NAME, datasetRunChangeListener);
                group.getEventSupport().addPropertyChangeListener(JRDesignGroup.PROPERTY_NAME, datasetRunChangeListener);
            }
        }
    }

    protected void datasetPropertyChanged(PropertyChangeEvent evt) {

        boolean refreshTableStructure = false;

        if (evt.getPropertyName().equals(JRDesignDataset.PROPERTY_NAME))
        {
            if (evt.getSource() instanceof JRDesignDataset)
            {
                ((JRDesignDatasetRun)table.getDatasetRun()).setDatasetName(currentDataset.getName() );
            }
            else if (evt.getSource() instanceof JRDesignGroup)
            {
                refreshTableStructure = true;
            }
        }
        else if (evt.getPropertyName().equals(StandardTable.PROPERTY_DATASET_RUN) ||
                 evt.getPropertyName().equals(JRDesignDataset.PROPERTY_GROUPS))
        {
            refreshTableStructure = true;
        }

        if (refreshTableStructure)
        {
            updateDatasetRunListeners();
            layoutChanged(evt);
        }
    }
    
}
