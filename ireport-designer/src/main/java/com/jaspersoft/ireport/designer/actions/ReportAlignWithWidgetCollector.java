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
package com.jaspersoft.ireport.designer.actions;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.crosstab.CrosstabObjectScene;
import com.jaspersoft.ireport.designer.crosstab.widgets.CrosstabWidget;
import com.jaspersoft.ireport.designer.widgets.GuideLineWidget;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import com.jaspersoft.ireport.designer.widgets.SelectionWidget;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import org.netbeans.api.visual.action.AlignWithWidgetCollector;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class ReportAlignWithWidgetCollector implements AlignWithWidgetCollector {

    private List<Integer> verticalGuideLines = new ArrayList<Integer>();

    public List<Integer> getHorizontalGuideLines() {
        return horizontalGuideLines;
    }

    public void setHorizontalGuideLines(List<Integer> horizontalGuideLines) {
        this.horizontalGuideLines = horizontalGuideLines;
    }

    public List<Integer> getVerticalGuideLines() {
        return verticalGuideLines;
    }

    public void setVerticalGuideLines(List<Integer> verticalGuideLines) {
        this.verticalGuideLines = verticalGuideLines;
    }
    private List<Integer> horizontalGuideLines = new ArrayList<Integer>();
    private AbstractReportObjectScene scene = null;
    
    public ReportAlignWithWidgetCollector(AbstractReportObjectScene scene)
    {
        this.scene = scene;
    }
    
    public boolean isSelectedObject(Widget widget)
    {
        
        Object obj = null;
        if (widget instanceof JRDesignElementWidget)
        {
            obj = ((JRDesignElementWidget)widget).getElement();
        }
        else if (widget instanceof SelectionWidget)
        {
            obj = ((SelectionWidget)widget).getRealWidget().getElement();
        }
        
        if ( ((AbstractReportObjectScene)widget.getScene()).getSelectedObjects().contains(obj) )
        {
            return true;
        }
        
        return false;
    }
    
    public boolean isChildOf(Widget widget, Widget parent)
    {
        if (parent == widget) return true;
        if (widget instanceof JRDesignElementWidget)
        {
            if (parent instanceof SelectionWidget)
                parent = ((SelectionWidget)parent).getRealWidget();
            
            if (parent instanceof JRDesignElementWidget)
            {
                JRDesignElement frame = ((JRDesignElementWidget)parent).getElement();
                JRDesignElement element = ((JRDesignElementWidget)widget).getElement();

                //if (frame instanceof JRDesignFrame)
                //{
                //    // Look if a child element has this widget....
                //    return ModelUtils.isChildOf(element, ((JRDesignFrame)frame).getElements());
                //}
                if ( ((JRDesignElementWidget)parent).getChildrenElements() != null)
                {
                    return ModelUtils.isChildOf(element, ((JRDesignElementWidget)parent).getChildrenElements());
                }
            }
        }
        return false;
    }
    
    
    
    
    public Collection<Rectangle> getRegions(Widget movingWidget) {
        
        java.util.List<Widget> children = scene.getElementsLayer().getChildren();
        ArrayList<Rectangle> regions = new ArrayList<Rectangle> (children.size());
        for (Widget widget : children)
        {

            if (widget != movingWidget && !isSelectedObject(widget) && !isChildOf(widget, movingWidget))
            {
                regions.add(widget.convertLocalToScene (widget.getClientArea()));
            }
        }
        //System.out.println("Elements in regions: " + regions.size() + " " + movingWidget);
        
        children = scene.getGuideLinesLayer().getChildren();
        for (Widget widget : children)
        {
            if (widget instanceof GuideLineWidget)
            {
                Rectangle r = widget.getClientArea();
                r.width = 0;
                regions.add(widget.convertLocalToScene(r));
            }
        }
        
        /*
        for (Integer line : getVerticalGuideLines())
        {
            regions.add( new Rectangle(10 + line.intValue(), 0, 1, Integer.MAX_VALUE) );
        }
        
        for (Integer line : getHorizontalGuideLines())
        {
            regions.add( new Rectangle(0, 10 + line.intValue(), Integer.MAX_VALUE, 1 ) );
        }
        */
        
        if (scene instanceof ReportObjectScene)
        {
            JasperDesign jd = scene.getJasperDesign();
            // Add margins and bands position...
            int bandLocation = jd.getTopMargin(); // Scene location...
            regions.add( new Rectangle(jd.getLeftMargin(), bandLocation, jd.getPageWidth() - jd.getRightMargin() - jd.getLeftMargin(), 0 ) );
            for (JRBand band : ModelUtils.getBands(jd))
            {
                if (band.getHeight() > 0)
                {
                    if (band instanceof JRDesignBand &&
                        ((JRDesignBand)band).getOrigin().getBandTypeValue() == BandTypeEnum.BACKGROUND &&
                        IReportManager.getInstance().isBackgroundSeparated())
                    {
                        bandLocation += 40 + jd.getTopMargin() + jd.getBottomMargin();
                        regions.add( new Rectangle(jd.getLeftMargin(), bandLocation, jd.getPageWidth() - jd.getRightMargin() - jd.getLeftMargin(), 0 ) );
                    }
                    bandLocation += band.getHeight();
                    regions.add( new Rectangle(jd.getLeftMargin(), bandLocation, jd.getPageWidth() - jd.getRightMargin() - jd.getLeftMargin(), 0 ) );
                }
            }
        }
        else if (scene instanceof CrosstabObjectScene)
        {
            
            List<Widget> separators = ((CrosstabObjectScene)scene).getCellSeparatorsLayer().getChildren();
            CrosstabWidget cw = ((CrosstabObjectScene)scene).getCrosstabWidget();
            //Rectangle r1 = new Rectangle(0,0, cw.getCrosstabDesignWidth(), cw.getCrosstabDesignHeight());
            //regions.add(r1);
            
            // Paint the data cells...
            //JRCrosstabCell[][] cells = cw.getCrosstab().getCells();
            int header_width = ModelUtils.getHeaderCellWidth(cw.getCrosstab());
            int header_height = ModelUtils.getHeaderCellHeight(cw.getCrosstab());

            JRCrosstabRowGroup[] row_groups = cw.getCrosstab().getRowGroups();
            JRCrosstabColumnGroup[] col_groups = cw.getCrosstab().getColumnGroups();
            
            JRCrosstabCell[][] cells = ModelUtils.normalizeCell(cw.getCrosstab().getCells(),row_groups,col_groups);
        
            int x = header_width;
            int y = header_height;

            for (int i=cells.length-1; i>=0; --i)
            {
                x = header_width;
                for (int k=cells[i].length-1; k>=0; --k)
                {
                    JRCrosstabCell cell = cells[i][k];
                    if (cell == null) continue;
                    regions.add(new Rectangle(x,y,cell.getContents().getWidth(),cell.getContents().getHeight()));
                    x += cell.getContents().getWidth();
                }
                if (cells[i][0] != null && cells[i][0].getContents() != null)
                {
                    y += cells[i][0].getContents().getHeight();
                }
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
                        regions.add(new Rectangle(x,y,row_groups[i].getTotalHeader().getWidth(),row_groups[i].getTotalHeader().getHeight()));
                        data_height -= row_groups[i].getTotalHeader().getHeight();
                        y += row_groups[i].getTotalHeader().getHeight();
                        break;
                    case END:
                        int y_loc = y + data_height - row_groups[i].getTotalHeader().getHeight();
                        regions.add(new Rectangle(x,y_loc,row_groups[i].getTotalHeader().getWidth(),row_groups[i].getTotalHeader().getHeight()));
                        data_height -= row_groups[i].getTotalHeader().getHeight();
                        break;
                }
                
                regions.add(new Rectangle(x,y,row_groups[i].getHeader().getWidth(),row_groups[i].getHeader().getHeight()));
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
                        regions.add(new Rectangle(x,y,col_groups[i].getTotalHeader().getWidth(),col_groups[i].getTotalHeader().getHeight()));
                        data_width -= col_groups[i].getTotalHeader().getWidth();
                        x += col_groups[i].getTotalHeader().getWidth();
                        break;
                    case END:
                        int x_loc = x + data_width - col_groups[i].getTotalHeader().getWidth();
                        regions.add(new Rectangle(x_loc,y,col_groups[i].getTotalHeader().getWidth(),col_groups[i].getTotalHeader().getHeight()));
                        data_width -= col_groups[i].getTotalHeader().getWidth();
                        break;
                    case NONE:
                        break;
                }

                regions.add(new Rectangle(x,y,col_groups[i].getHeader().getWidth(),col_groups[i].getHeader().getHeight()));
                y += col_groups[i].getHeader().getHeight();
            }
        }
       
        
        return regions;
    }
}
