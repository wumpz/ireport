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

import com.jaspersoft.ireport.designer.crosstab.CellInfo;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.palette.actions.CreateTextFieldAction;
import com.jaspersoft.ireport.designer.sheet.GenericProperty;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Point;
import java.awt.Rectangle;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRCrosstabOrigin;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabCell;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabDataset;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabParameter;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.crosstabs.type.CrosstabTotalPositionEnum;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertyExpression;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignChartDataset;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignPropertyExpression;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.type.PrintOrderEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author gtoffoli
 */
public class ModelUtils {

    public static JRDesignCrosstabParameter cloneCrosstabParameter(JRDesignCrosstabParameter param) {
        JRDesignCrosstabParameter newParam = new JRDesignCrosstabParameter();
        newParam.setName( param.getName() );
        newParam.setForPrompting( param.isForPrompting() );
        newParam.setSystemDefined( param.isSystemDefined() );
        //newParam.setValueClass( param.getValueClass() );
        newParam.setValueClassName( param.getValueClassName() );
        newParam.setDescription( param.getDescription());
        if (param.getDefaultValueExpression() != null)
        {
            newParam.setDefaultValueExpression( cloneExpression( (JRDesignExpression)param.getDefaultValueExpression()) );
        }
        
        // Duplicate properties...
        replacePropertiesMap(param.getPropertiesMap(), newParam.getPropertiesMap());
        
        return newParam;
    }

    public static JRDesignStyle cloneStyle(JRDesignStyle style) {
        JRDesignStyle newStyle = (JRDesignStyle)style.clone();
        return newStyle;
    }

   /**
    * Get all the elements in the report
    * @param jd
    * @param includeCrosstabs
    * @return
    */
   public static List<JRDesignElement> getAllElements(JasperDesign jd, boolean includeCrosstabs) {

        List<JRDesignElement> list = new ArrayList<JRDesignElement>();

        List<JRBand> bands = getBands(jd);
        for (JRBand band : bands)
        {
            list.addAll(getAllElements(band));
        }

        if (includeCrosstabs)
        {
            List<JRDesignElement> list2 = new ArrayList<JRDesignElement>();
            for (int i=0; i<list.size(); ++i)
            {
                JRDesignElement ele = list.get(i);
                if (ele instanceof JRDesignCrosstab)
                {
                    list2.addAll(getAllElements((JRDesignCrosstab)ele));
                }
            }
            list.addAll(list2);
        }
        
        return list;
    }

    /**
     * Return all the elements contained in the cells of the specified crosstab.
     * @param crosstab
     * @return
     */
    public static List<JRDesignElement> getAllElements(JRDesignCrosstab crosstab) {

        List list = new ArrayList();
        List<JRDesignCellContents> cells = getAllCells(crosstab);

        for (JRDesignCellContents content : cells)
        {
            list.addAll(  getAllElements(content) );
        }
        return  list;
    }



    /**
     * Get all the elements in the report
     * Same as: getAllElements(jd, true);
     * @param jd
     * @return
     */
    public static List<JRDesignElement> getAllElements(JasperDesign jd) {
        
        return getAllElements(jd, true);
    }

    /**
     * This method does not include children of components...
     * @param group
     * @return
     */
    public static List<JRDesignElement> getAllElements(JRElementGroup group) {
        List list = new ArrayList();

        if (group == null) return list;
        List elements = group.getChildren();
        
        for (Object ele : elements)
        {
            if (ele instanceof JRElementGroup)
            {
                list.addAll(getAllElements((JRElementGroup)ele));

            }

            if (ele instanceof JRDesignElement)
            {
                list.add((JRDesignElement)ele);
                
                if(ele instanceof JRDesignCrosstab)
                {
                    JRDesignCrosstab crosstab = (JRDesignCrosstab)ele;
                    List cells = crosstab.getCellsList();
                    for (int i=0; i<cells.size(); ++i)
                    {
                        JRCrosstabCell cell = (JRCrosstabCell)cells.get(i);
                        list.addAll(getAllElements(cell.getContents()));
                    }
                }
            }
        }
        
        return list;
    }

    /**
     * Return the ordered list of bands available in the current report
     * @param jd the JasperDesign
     * @return a list of bands
     */
    public static List<JRBand> getBands(JasperDesign jd)
    {
        List<JRBand> list = new ArrayList<JRBand>();
        if (jd == null) return list;
        
        JRGroup[] groups = jd.getGroups();
        
        
        if (null != jd.getTitle()) list.add(jd.getTitle());
        if (null != jd.getPageHeader()) list.add(jd.getPageHeader());
        if (null != jd.getColumnHeader()) list.add(jd.getColumnHeader());
        for (int i=0; i<groups.length; ++i)
        {
            //if (null != groups[i].getGroupHeader()) list.add(groups[i].getGroupHeader());
            if (groups[i].getGroupHeaderSection() != null)
            {
                JRBand[] bandsList = groups[i].getGroupHeaderSection().getBands();
                for (int k=0; bandsList != null && k<bandsList.length; ++k)
                {
                    if (bandsList[k] != null)
                    {
                        list.add(bandsList[k]);
                    }
                }
            }
            
        }
        //if (null != jd.getDetail()) list.add(jd.getDetail());
        if (jd.getDetailSection() != null)
        {
            JRBand[] bandsList = jd.getDetailSection().getBands();
            for (int k=0; bandsList != null && k<bandsList.length; ++k)
            {
                if (bandsList[k] != null)
                {
                    list.add(bandsList[k]);
                }
            }
        }
 
        for (int i=groups.length-1; i>=0; --i)
        {
            //if (null != groups[i].getGroupFooter()) list.add(groups[i].getGroupFooter());
            if (groups[i].getGroupFooterSection() != null)
            {
                JRBand[] bandsList = groups[i].getGroupFooterSection().getBands();
                for (int k=0; bandsList != null && k<bandsList.length; ++k)
                {
                    if (bandsList[k] != null)
                    {
                        list.add(bandsList[k]);
                    }
                }
            }
        }
        if (null != jd.getColumnFooter()) list.add(jd.getColumnFooter());
        if (null != jd.getPageFooter()) list.add(jd.getPageFooter());
        if (null != jd.getLastPageFooter()) list.add(jd.getLastPageFooter());
        if (null != jd.getSummary()) list.add(jd.getSummary());
        if (null != jd.getNoData()) list.add(jd.getNoData());
        if (null != jd.getBackground()) list.add(jd.getBackground());
        
        return list;
    }

    public static int getHeaderCellWidth(JRDesignCrosstab crosstab)
    {
        if (crosstab == null) return 0;
        //if (crosstab.getHeaderCell() != null) return crosstab.getHeaderCell().getWidth();
        JRCrosstabRowGroup[] row_groups = crosstab.getRowGroups();
        int tot = 0;
        for (int i=0; i<row_groups.length; ++i)
        {
            tot += row_groups[i].getWidth();
        }
        
        return tot;
    }
    
    public static int getHeaderCellHeight(JRDesignCrosstab crosstab)
    {
        if (crosstab == null) return 0;
        //if (crosstab.getHeaderCell() != null) return crosstab.getHeaderCell().getHeight();
        JRCrosstabColumnGroup[] col_groups = crosstab.getColumnGroups();
        int tot = 0;
        for (int i=0; i<col_groups.length; ++i)
        {
            tot += col_groups[i].getHeight();
        }
        
        return tot;
    }
    
    public static int findRowHeight(JRCrosstabCell[][] cells, int rowIndex) {
        for (int i=0; i<cells[rowIndex].length; ++i)
        {
            if (cells[rowIndex][i] != null && cells[rowIndex][i].getContents() != null)
            {
                return cells[rowIndex][i].getContents().getHeight();
            }
        }
        return 0;
    }
    
    public static int findColumnWidth(JRCrosstabCell[][] cells, int colIndex) {
        for (int i=0; i<cells.length; ++i)
        {
            if (cells[i][colIndex] != null && cells[i][colIndex].getContents() != null)
            {
                return cells[i][colIndex].getContents().getWidth();
            }
        }
        return 0;
    }
    
    public static JRDesignCellContents getCellAt(JRDesignCrosstab crosstab, Point point)
    {
        return getCellAt(crosstab,point, false);
        
    }
    public static JRDesignCellContents getCellAt(JRDesignCrosstab crosstab, Point point, boolean createIt) {

            List<CellInfo> cellInfos = getCellInfos(crosstab);

            int header_width = 0; // header width
            int header_height = 0; // header height

            for (int i=0; i<cellInfos.size(); ++i)
            {
                CellInfo ci = cellInfos.get(i);
                Rectangle rect = new Rectangle(ci.getLeft(), ci.getTop(), ci.getCellContents().getWidth(), ci.getCellContents().getHeight());
                if (rect.contains(point)) return ci.getCellContents();

                if (ci.getY() == crosstab.getColumnGroups().length)
                {
                    header_height = ci.getTop();
                }

                if (ci.getX() == crosstab.getRowGroups().length)
                {
                    header_width = ci.getLeft();
                }
            }

            // cell not found...
            if (!createIt) return null;

            // 1. is the point a header?
            Rectangle rectHeaderCell = new Rectangle(0, 0,header_width,header_height);
            if (rectHeaderCell.contains(point))
            {
                crosstab.setHeaderCell(new JRDesignCellContents());
                return (JRDesignCellContents) crosstab.getHeaderCell();
            }
            
            JRCrosstabRowGroup[] row_groups = crosstab.getRowGroups();
            JRCrosstabColumnGroup[] col_groups = crosstab.getColumnGroups();
            
            JRCrosstabCell[][] cells = normalizeCell(crosstab.getCells(),row_groups,col_groups);
        
            int x = header_width;
            int y = header_height;
            
            Rectangle r1 = new Rectangle(0,0,header_width,header_height);
            if (r1.contains(point)) return (JRDesignCellContents)crosstab.getHeaderCell();

            for (int i=cells.length-1; i>=0; --i)
            {
                x = header_width;
                for (int k=cells[i].length-1; k>=0; --k)
                {
                    JRCrosstabCell cell = cells[i][k];
                    if (cell == null) continue;
                    Rectangle rect = new Rectangle(x,y,cell.getContents().getWidth(),cell.getContents().getHeight());

                    if (rect.contains(point)) 
                    {
                        if (cell.getContents() == null && createIt)
                        {
                            ((JRDesignCrosstabCell)cell).setContents(new JRDesignCellContents());
                        }
                        return (JRDesignCellContents)cell.getContents();
                    }
                    
                    x += ModelUtils.findColumnWidth(cells, k);
                }
                
                y += findRowHeight(cells, i);
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
                        Rectangle rect1 = new Rectangle(x,y,row_groups[i].getTotalHeader().getWidth(),row_groups[i].getTotalHeader().getHeight());
                        if (rect1.contains(point)) 
                        {
                            if ((JRDesignCellContents)row_groups[i].getTotalHeader() == null && createIt)
                            {
                                 ((JRDesignCrosstabGroup)row_groups[i]).setTotalHeader(new JRDesignCellContents());   
                            }
                            return (JRDesignCellContents)row_groups[i].getTotalHeader();
                        }
                        data_height -= row_groups[i].getTotalHeader().getHeight();
                        y += row_groups[i].getTotalHeader().getHeight();
                        break;
                    case END:
                        int y_loc = y + data_height - row_groups[i].getTotalHeader().getHeight();
                        Rectangle rect2 = new Rectangle(x,y_loc,row_groups[i].getTotalHeader().getWidth(),row_groups[i].getTotalHeader().getHeight());
                        if (rect2.contains(point)) 
                        {
                            if ((JRDesignCellContents)row_groups[i].getTotalHeader() == null && createIt)
                            {
                                 ((JRDesignCrosstabGroup)row_groups[i]).setTotalHeader(new JRDesignCellContents());   
                            }
                            return (JRDesignCellContents)row_groups[i].getTotalHeader();
                        }

                        data_height -= row_groups[i].getTotalHeader().getHeight();
                        break;
                }
                
                Rectangle rect3 = new Rectangle(x,y,row_groups[i].getHeader().getWidth(),row_groups[i].getHeader().getHeight());
                if (rect3.contains(point)) 
                {
                        if ((JRDesignCellContents)row_groups[i].getHeader() == null && createIt)
                        {
                             ((JRDesignCrosstabGroup)row_groups[i]).setHeader(new JRDesignCellContents());   
                        }
                        return (JRDesignCellContents)row_groups[i].getHeader();
                }
                
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
                        Rectangle rect1 = new Rectangle(x,y,col_groups[i].getTotalHeader().getWidth(),col_groups[i].getTotalHeader().getHeight());
                        if (rect1.contains(point))
                        {
                            if ((JRDesignCellContents)col_groups[i].getTotalHeader() == null && createIt)
                            {
                              ((JRDesignCrosstabGroup)col_groups[i]).setTotalHeader(new JRDesignCellContents());   
                            }
                            return (JRDesignCellContents)col_groups[i].getTotalHeader();
                        }
                        data_width -= col_groups[i].getTotalHeader().getWidth();
                        x += col_groups[i].getTotalHeader().getWidth();
                        break;
                    case END:
                        int x_loc = x + data_width - col_groups[i].getTotalHeader().getWidth();
                        Rectangle rect2 = new Rectangle(x_loc,y,col_groups[i].getTotalHeader().getWidth(),col_groups[i].getTotalHeader().getHeight());
                        if (rect2.contains(point))
                        {
                            if ((JRDesignCellContents)col_groups[i].getTotalHeader() == null && createIt)
                            {
                              ((JRDesignCrosstabGroup)col_groups[i]).setTotalHeader(new JRDesignCellContents());   
                            }
                            return (JRDesignCellContents)col_groups[i].getTotalHeader();
                        }
                            
                        data_width -= col_groups[i].getTotalHeader().getWidth();
                        break;
                    case NONE:
                        break;
                }

                Rectangle rect3 = new Rectangle(x,y,col_groups[i].getHeader().getWidth(),col_groups[i].getHeader().getHeight());
                if (rect3.contains(point))
                {
                    if ((JRDesignCellContents)col_groups[i].getHeader() == null && createIt)
                    {
                      ((JRDesignCrosstabGroup)col_groups[i]).setHeader(new JRDesignCellContents());   
                    }
                    return (JRDesignCellContents)col_groups[i].getHeader();
                }    

                y += col_groups[i].getHeader().getHeight();
            }
        
            return null;
    }


    /**
     * This method returns all the crosstab cell (not null cells) with all the info of each cell.
     * @param crosstab
     * @return
     */
    public static List<CellInfo> getCellInfos(JRDesignCrosstab crosstab)
    {
        List<CellInfo> cellInfos = new ArrayList<CellInfo>();
            // Add columns...

            JRCrosstabRowGroup[] row_groups = crosstab.getRowGroups();
            JRCrosstabColumnGroup[] col_groups = crosstab.getColumnGroups();

            /*
            // The size of the crosstab matrix...
            Point size = new Point(0,1);

            for (int i=0; i<row_groups.length; ++i)
            {
                size.x++;
            }

            for (int i=0; i<col_groups.length; ++i)
            {
                JRCrosstabColumnGroup group = col_groups[i];
                size.x++;
                if (group.getTotalPosition() != BucketDefinition.TOTAL_POSITION_NONE)
                {
                    size.x++;
                }
            }
            */

            int x =0;
            int y =0;


            // Add header cell...
            if (crosstab.getHeaderCell() != null)
            {
                CellInfo ci = new CellInfo(x, y, row_groups.length, col_groups.length, crosstab.getHeaderCell());
                cellInfos.add(ci);
            }

            x = row_groups.length;
            y = 0;

            // Add column headers...
            int w = 1; // holds the total number of columns.
            for (int i=0; i<col_groups.length; ++i)
            {
                JRCrosstabColumnGroup group = col_groups[i];
                if (group.getTotalPositionValue() != CrosstabTotalPositionEnum.NONE)
                {
                    w++;
                }
            }

            int h = col_groups.length;


            for (int i=0; i<col_groups.length; ++i)
            {
                JRCrosstabColumnGroup group = col_groups[i];

                if (group.getTotalPositionValue() == CrosstabTotalPositionEnum.START)
                {
                    CellInfo ci = new CellInfo(x, y, 1, h, group.getTotalHeader());
                    cellInfos.add(ci);
                    x++;
                    w--;
                }

                CellInfo ci2 = new CellInfo(x, y, w, 1, group.getHeader());
                cellInfos.add(ci2);

                if (group.getTotalPositionValue() == CrosstabTotalPositionEnum.END)
                {
                     CellInfo ci = new CellInfo(x+w-1, y, 1, h, group.getTotalHeader());
                     cellInfos.add(ci);
                     w--;
                }
                h--;
                y++;
            }


            x = 0;
            y = col_groups.length;

            // Add column headers...
            w = row_groups.length;

            h = 1; //row_groups.length;

            for (int i=0; i<row_groups.length; ++i)
            {
                JRCrosstabRowGroup group = row_groups[i];
                if (group.getTotalPositionValue() != CrosstabTotalPositionEnum.NONE)
                {
                    h++;
                }
            }

            for (int i=0; i<row_groups.length; ++i)
            {
                JRCrosstabRowGroup group = row_groups[i];

                if (group.getTotalPositionValue() == CrosstabTotalPositionEnum.START)
                {
                    CellInfo ci = new CellInfo(x, y, w, 1, group.getTotalHeader());
                    cellInfos.add(ci);
                    y++;
                    h--;
                }

                CellInfo ci2 = new CellInfo(x, y, 1, h, group.getHeader());
                cellInfos.add(ci2);

                if (group.getTotalPositionValue() == CrosstabTotalPositionEnum.END)
                {
                    CellInfo ci = new CellInfo(x, y+h-1, w, 1, group.getTotalHeader());
                    cellInfos.add(ci);
                    h--;
                }

                w--;
                x++;
            }


            x = row_groups.length;
            y = col_groups.length;

            JRCrosstabCell[][] cells = crosstab.getCells();
            cells = ModelUtils.normalizeCell(cells, row_groups, col_groups);
            for (int i=0; i<cells.length; ++i)
            {
                for (int k=0; k<cells[i].length; ++k)
                {
                    int cx = x + cells[i].length-k-1;
                    int cy = y + cells.length-i-1;
                    if (cells[i][k] == null)
                    {
                        continue;
                    }
                    CellInfo ci = new CellInfo(cx, cy, 1, 1, cells[i][k].getContents());
                    cellInfos.add(ci);
                }
            }

            // Find x lines and y lines (create the position of the grid).
            Integer[][] separators = getColumnWidths(crosstab,cells);

            for (int i=0; i < cellInfos.size(); ++i)
            {
                CellInfo ci = cellInfos.get(i);
                // calculate the width...
                // find the max with of the CI with x = ci.getX();
                ci.setTop(separators[0][ci.getY()]);
                ci.setLeft(separators[1][ci.getX()]);
//
//                int posX = 0;
//                int posY = 0;
//
//                for (int index = 0; index < ci.getX(); ++index)
//                {
//
//                    for (int k=0; k < cellInfos.size(); ++k)
//                    {
//                        CellInfo ci2 = cellInfos.get(k);
//                        if (ci2.getX() == index)
//                        {
//                            posX += ci2.getCellContents().getWidth();
//                            break;
//                        }
//                    }
//                }
//
//                ci.setLeft(posX);
//                for (int index = 0; index < ci.getY(); ++index)
//                {
//                    for (int k=0; k < cellInfos.size(); ++k)
//                    {
//                        CellInfo ci2 = cellInfos.get(k);
//                        if (ci2.getY() == index)
//                        {
//                            posY += ci2.getCellContents().getHeight();
//                            break;
//                        }
//                    }
//                }
//                ci.setTop(posY);
            }
            
            return cellInfos;
    }


    /*
     * Returns an array of 2 lists of integers.
     * The first list is for horizontal lines (rows)
     * the second for vertical lines (columns)
     */
    private static Integer[][] getColumnWidths(JRDesignCrosstab designCrosstab, JRCrosstabCell[][] cells)
    {
        // Add a line for each crosstab intersection....
        List<JRDesignCellContents> cellContents = new ArrayList<JRDesignCellContents>();
        cellContents.add( (JRDesignCellContents)designCrosstab.getHeaderCell() );

        List<Integer> verticalSeparator = new ArrayList<Integer>();
        List<Integer> horizontalSeparator = new ArrayList<Integer>();

        verticalSeparator.add(0);
        horizontalSeparator.add(0);


        JRCrosstabColumnGroup[] col_groups = designCrosstab.getColumnGroups();
        JRCrosstabRowGroup[] row_groups = designCrosstab.getRowGroups();

        int current_x = 0;
        for (int i=0; i<row_groups.length; ++i)
        {
            current_x += row_groups[i].getHeader().getWidth();
            verticalSeparator.add(current_x);

            cellContents.add( (JRDesignCellContents)row_groups[i].getHeader());
            if (row_groups[i].getTotalPositionValue() != CrosstabTotalPositionEnum.NONE )
            {
                cellContents.add( (JRDesignCellContents)row_groups[i].getTotalHeader());
            }
        }
        for (int i=cells[0].length-1; i>=0; --i)
        {
            current_x += ModelUtils.findColumnWidth(cells, i);
            verticalSeparator.add(current_x);
        }

        int current_y = 0;
        for (int i=0; i<col_groups.length; ++i)
        {
            current_y += col_groups[i].getHeader().getHeight();
            horizontalSeparator.add(current_y);

            cellContents.add( (JRDesignCellContents)col_groups[i].getHeader());
            if (col_groups[i].getTotalPositionValue() != CrosstabTotalPositionEnum.NONE )
            {
                cellContents.add( (JRDesignCellContents)col_groups[i].getTotalHeader());
            }
        }
        for (int i=cells.length-1; i>=0; --i)
        {
            current_y += ModelUtils.findRowHeight(cells, i);
            horizontalSeparator.add(current_y);
        }

        Integer[] hs = horizontalSeparator.toArray(new Integer[horizontalSeparator.size()]);
        Integer[] vs = verticalSeparator.toArray(new Integer[verticalSeparator.size()]);

        Integer[][] result = new Integer[2][];
        result[0]=hs;
        result[1]=vs;

        return result;


    }


    /**
     * It could be a very expensive operation, don't use it if you have to explore the whole crosstab
     * 
     * @param crosstab
     * @param content
     * @return
     */
    public static Point getCellLocation(JRDesignCrosstab crosstab, JRDesignCellContents content) {
 
            if (content == null || content == crosstab.getHeaderCell()) return new Point(0,0);

            /*
            int header_width = getHeaderCellWidth(crosstab);
            int header_height = getHeaderCellHeight(crosstab);

            JRCrosstabRowGroup[] row_groups = crosstab.getRowGroups();
            JRCrosstabColumnGroup[] col_groups = crosstab.getColumnGroups();
            
            JRCrosstabCell[][] jr_cells = crosstab.getCells();
            JRCrosstabCell baseCell = jr_cells[jr_cells.length-1][jr_cells[0].length-1];
            
            JRCrosstabCell[][] cells = normalizeCell(jr_cells,row_groups,col_groups);


            int x = header_width;
            int y = header_height;

            print++;

            if (print <= 1)
            {

                for (int i=cells.length-1; i>=0; --i)
                {
                    x = header_width;
                    int height = 0;
                    for (int k=cells[i].length-1; k>=0; --k)
                    {
                        JRCrosstabCell cell = cells[i][k];
                        if (cell == null)
                        {
                            //x += baseCell.getWidth();
                            System.out.println("Cell " + i + "/" + k +" NULL");
                            System.out.flush();
                        }
                        else
                        {
                           System.out.println("Cell " + i + "/" + k +" " + (cell.getContents() == content) + " "+ ModelUtils.nameOf((JRDesignCellContents)cell.getContents()) + " (" + x + "," + y + ", "+ cell.getContents().getWidth() + "," + cell.getContents().getHeight() + ")");
                           System.out.flush();

                            x += cell.getContents().getWidth();
                            height = cell.getContents().getHeight();
                        }
                    }
                    y += height;
                }

                x = header_width;
                y = header_height;
            }


            int[] ct_cell_width = new int[col_groups.length+1];
            int[] ct_cell_height = new int[row_groups.length+1];

            ct_cell_width[0] = header_width;
            ct_cell_height[0] = header_height;

            // fill the two arrays...
            List<JRCrosstabColumnGroup> exploredColGroups = new ArrayList<JRCrosstabColumnGroup>();

            for (int i=1; i<ct_cell_width.length; ++i)
            {
                // Look for a column group with
                for (int k=0; k<col_groups.length; ++k)
                {
                    JRCrosstabColumnGroup group = col_groups[k];
                    if (exploredColGroups.contains(group)) continue;
                    if (group.getTotalPosition() == BucketDefinition.TOTAL_POSITION_START)
                    {
                        exploredColGroups.add(group);
                        // Look for the position of the cell gr1/?
                    }
                }

            }


            for (int i=cells.length-1; i>=0; --i)
            {
                x = header_width;
                int height = 0;
                for (int k=cells[i].length-1; k>=0; --k)
                {
                    JRCrosstabCell cell = cells[i][k];
                    if (cell == null)
                    {
                        //x += baseCell.getWidth();
                        //System.out.println("Cell " + i + "/" + k +" NULL");
                        //System.out.flush();
                    }
                    else
                    {
                       //System.out.println("Cell " + i + "/" + k +" " + (cell.getContents() == content) + " "+ ModelUtils.nameOf((JRDesignCellContents)cell.getContents()) + " (" + x + "," + y + ") " + cell.getContents());
                       //System.out.flush();

                        if (cell.getContents() == content) return new Point(x,y);
                        x += cell.getContents().getWidth();
                        height = cell.getContents().getHeight();
                    }
                }
                y += height;
            }
            
            int data_width = x - header_width;
            int data_height = y - header_height;


            // paint row cells...
            if (content.getOrigin().getType() == JRCrosstabOrigin.TYPE_ROW_GROUP_HEADER ||
                content.getOrigin().getType() == JRCrosstabOrigin.TYPE_ROW_GROUP_TOTAL_HEADER)
            {
                x = 0;
                y = header_height;
            
                for (int i=0; i<row_groups.length; ++i)
                {
                    switch (row_groups[i].getTotalPosition())
                    {
                        case BucketDefinition.TOTAL_POSITION_START:
                            if (row_groups[i].getTotalHeader() == content) return new Point(x,y);
                            data_height -= row_groups[i].getTotalHeader().getHeight();
                            y += row_groups[i].getTotalHeader().getHeight();
                            break;
                        case BucketDefinition.TOTAL_POSITION_END:
                            int y_loc = y + data_height - row_groups[i].getTotalHeader().getHeight();
                            if (row_groups[i].getTotalHeader() == content) return new Point(x,y_loc);
                            data_height -= row_groups[i].getTotalHeader().getHeight();
                            break;
                    }

                    if (row_groups[i].getHeader() == content) return new Point(x,y);
                    x += row_groups[i].getHeader().getWidth();
                }
            }

            x = header_width;
            y = 0;

            if (content.getOrigin().getType() == JRCrosstabOrigin.TYPE_COLUMN_GROUP_HEADER ||
                content.getOrigin().getType() == JRCrosstabOrigin.TYPE_COLUMN_GROUP_TOTAL_HEADER)
            {
                // paint col cells...
                for (int i=0; i<col_groups.length; ++i)
                {
                    switch (col_groups[i].getTotalPosition())
                    {
                        case BucketDefinition.TOTAL_POSITION_START:
                            if (col_groups[i].getTotalHeader() == content) return new Point(x,y);
                            data_width -= col_groups[i].getTotalHeader().getWidth();
                            x += col_groups[i].getTotalHeader().getWidth();
                            break;
                        case BucketDefinition.TOTAL_POSITION_END:
                            int x_loc = x + data_width - col_groups[i].getTotalHeader().getWidth();
                            if (col_groups[i].getTotalHeader() == content) return new Point(x_loc,y);
                            data_width -= col_groups[i].getTotalHeader().getWidth();
                            break;
                        case BucketDefinition.TOTAL_POSITION_NONE:
                            break;
                    }

                    if (col_groups[i].getHeader() == content) return new Point(x,y);
                    y += col_groups[i].getHeader().getHeight();
                }
            }

            return new Point(0,0);
            */
            

            List<CellInfo> cellInfos = getCellInfos(crosstab);
            for (int i=0; i<cellInfos.size(); ++i)
            {
                CellInfo ci = cellInfos.get(i);
                if (ci.getCellContents() == content)
                {
                    return new Point(ci.getLeft(), ci.getTop());
                }
            }

            return new Point(0,0);
    }



    

    /**
     * Look in the crosstab if the provided new name is already a name of a group or a measure...
     * 
     * @param crosstab
     * @param new_name
     * @return
     */
    public static boolean isValidNewCrosstabObjectName(JRDesignCrosstab crosstab, String new_name) {
        
        if (crosstab.getRowGroupIndicesMap().containsKey(new_name)) return false;
        if (crosstab.getColumnGroupIndicesMap().containsKey(new_name)) return false;
        if (crosstab.getMeasureIndicesMap().containsKey(new_name)) return false;
        
        return true;
    }
    
    public static String nameOf(JRCrosstabOrigin origin) {
        
        String title =I18n.getString("ModelUtils.Title.unknow");
        if (origin != null)
        {
            
            switch (origin.getType())
            {
                case JRCrosstabOrigin.TYPE_HEADER_CELL:
                    title = I18n.getString("ModelUtils.Title.Header");
                    break;
                case JRCrosstabOrigin.TYPE_DATA_CELL:
                    title = (origin.getRowGroupName() == null ? I18n.getString("ModelUtils.Title.Detail") : origin.getRowGroupName()) + " / " +
                            (origin.getColumnGroupName() == null ? I18n.getString("ModelUtils.Title.Detail") : origin.getColumnGroupName());
                    break;
                case JRCrosstabOrigin.TYPE_COLUMN_GROUP_HEADER:
                    title = origin.getColumnGroupName() + I18n.getString("ModelUtils.Column.header");
                    break;
                case JRCrosstabOrigin.TYPE_COLUMN_GROUP_TOTAL_HEADER:
                    title = origin.getColumnGroupName() + I18n.getString("ModelUtils.Title.Totalheader");
                    break;
                case JRCrosstabOrigin.TYPE_COLUMN_GROUP_CROSSTAB_HEADER:
                    title = origin.getColumnGroupName() + " " + I18n.getString("ModelUtils.Title.Header");
                    break;
                case JRCrosstabOrigin.TYPE_ROW_GROUP_HEADER:
                    title = origin.getRowGroupName() + I18n.getString("ModelUtils.Title.HeaderH");
                    break;
                case JRCrosstabOrigin.TYPE_ROW_GROUP_TOTAL_HEADER:
                    title = origin.getRowGroupName() + I18n.getString("ModelUtils.Title.Totalheader");
                    break;
                case JRCrosstabOrigin.TYPE_WHEN_NO_DATA_CELL:
                    title = I18n.getString("ModelUtils.Title.WhenNoData");
                    break;
            }
        }

        return title;
        
    }
    
    /**
     * Look the contents origin to guess the cell name
     * 
     * @param contents
     * @return
     */
    public static String nameOf(JRDesignCellContents contents)
    {
        JRCrosstabOrigin origin = contents.getOrigin();
        
        return nameOf(origin);
        
        /*
        
        // Draw this cell...
        if (contents == crosstab.getHeaderCell()) return "Crosstab header";
        
        // Paint the data cells...
        JRCrosstabCell[][] cells = crosstab.getCells();
        for (int i=cells.length-1; i>=0; --i)
        {
            for (int k=cells[i].length-1; k>=0; --k)
            {
                JRCrosstabCell cell = cells[i][k];
                String title = (cell.getRowTotalGroup() == null ? "Detail" : cell.getRowTotalGroup()) + " / " +
                               (cell.getColumnTotalGroup() == null ? "Detail" : cell.getColumnTotalGroup());
                if (contents == cell.getContents()) return title;
            }
        }
        
        JRCrosstabRowGroup[] row_groups = crosstab.getRowGroups();
        
        // paint row cells...
        for (int i=0; i<row_groups.length; ++i)
        {
            if (row_groups[i].getHeader() == contents) return row_groups[i].getName() + " header";
            if (row_groups[i].getTotalHeader() == contents) return row_groups[i].getName() + " total header";
        }
        
        JRCrosstabColumnGroup[] col_groups = crosstab.getColumnGroups();
        
        for (int i=0; i<col_groups.length; ++i)
        {
            if (col_groups[i].getHeader() == contents) return col_groups[i].getName() + " header";
            if (col_groups[i].getTotalHeader() == contents) return col_groups[i].getName() + " total header";
        }
        
        return "?";
        */
    }
    
    public static List<JRDesignCellContents> getAllCells(JRDesignCrosstab designCrosstab) {
        
        List<JRDesignCellContents> list = new ArrayList<JRDesignCellContents>();
        
        list.add( (JRDesignCellContents)designCrosstab.getHeaderCell() );
        
        // Row cells
        List cells = designCrosstab.getCellsList();
        for (int i=0; i<cells.size(); ++i)
        {
            JRCrosstabCell cell = (JRCrosstabCell)cells.get(i);
            if (cell != null && (JRDesignCellContents)cell.getContents() != null)
            {
                list.add( (JRDesignCellContents)cell.getContents() );
            }
        }
        
        JRCrosstabRowGroup[] row_groups = designCrosstab.getRowGroups();
        for (int i=0; i<row_groups.length; ++i)
        {
            switch (row_groups[i].getTotalPositionValue())
            {
                case START:
                case END:
                    list.add( (JRDesignCellContents)row_groups[i].getTotalHeader());
                    break;
                default: break;
            }
            list.add( (JRDesignCellContents)row_groups[i].getHeader());
        }
        
        JRCrosstabColumnGroup[] col_groups = designCrosstab.getColumnGroups();
        for (int i=0; i<col_groups.length; ++i)
        {
            switch (col_groups[i].getTotalPositionValue())
            {
                case START:
                case END:
                    list.add( (JRDesignCellContents)col_groups[i].getTotalHeader());
                    break;
                default: break;
            }
            list.add( (JRDesignCellContents)col_groups[i].getHeader());
        }
        
        return list;
    }

    /**
     * Returns the dataset of an element. If the element is contained in a crosstab referencing a specific
     * dataset, it is found.
     * If not, the main report dataset is returned.
     */
    public static JRDesignDataset getElementDataset(JRDesignElement element, JasperDesign jd) {
        
        JRDesignDataset dataset = null;

        JRElementGroup group = getTopElementGroup(element);
        
        if (group != null && group instanceof JRDesignCellContents) {  // Main datasource
            // Check if this crosstab is using a different dataset...
            JRDesignCellContents cellContent = (JRDesignCellContents)group;
            if (cellContent.getOrigin().getCrosstab().getDataset() != null)
            {
                JRElementDataset elementDataset = cellContent.getOrigin().getCrosstab().getDataset();
                if (elementDataset.getDatasetRun() != null)
                {
                    // Get the dataset name...
                    String datasetName = elementDataset.getDatasetRun().getDatasetName();
                    
                    return (JRDesignDataset)jd.getDatasetMap().get(datasetName);
                }
            }
        }
        else if (!(group instanceof JRBand))
        {
            // This element belongs to a custom component....
           // Go trought the scenes...
            JrxmlVisualView view = IReportManager.getInstance().getActiveVisualView();
            if (view != null)
            {
                List<GenericDesignerPanel> panels = view.getReportDesignerPanel().getDeisgnPanels();
                for (GenericDesignerPanel panel : panels)
                {
                    dataset = panel.getElementDataset(element);
                    if (dataset != null) return dataset;
                }
            }
            
            // Not found yet... try to check if a component can be found in the main designer...
            // Here iReport uses some tricks. It doesn't know anything about the components, but
            // it assumes that if a component uses a special sub dataset, it must be a method called
            // getDatasetRun. Of course this is a big assumption, but it still good enough for what
            // we can do. The following code looks a parent of the current element which is a component
            // (it can be the component itself which can be inside another component, this last assumption
            //  may be not optimal)...
            // If the method getDatasetRun exists, and it points to a valid dataset, this is the dataset
            // returned by the method. The method stops at the first component (this should be a good
            // enough assumption.
            Node enode = findElementNode(view.getExplorerManager().getRootContext(), element);
            while (enode != null && enode instanceof ElementNode)
            {
                if (((ElementNode)enode).getElement() instanceof JRDesignComponentElement)
                {
                    JRDesignComponentElement component = (JRDesignComponentElement) ((ElementNode)enode).getElement();
                    Component componentImpl = component.getComponent();
                    if (componentImpl != null)
                    {
                        try {
                            Method m = componentImpl.getClass().getMethod("getDatasetRun");
                            if (m != null)
                            {
                                JRDatasetRun dr = (JRDatasetRun) m.invoke(componentImpl, new Object[]{});

                                if (dr != null && jd.getDatasetMap().containsKey(dr.getDatasetName()) )
                                {
                                    return (JRDesignDataset)jd.getDatasetMap().get( dr.getDatasetName()  );
                                }
                            }

                        } catch (Throwable t){
                            break;
                        }
                    }
                    break;
                }

                enode = enode.getParentNode();
            }

        }
        
            
        return jd.getMainDesignDataset();

    }

    
    /**
     * Returns the dataset tied to a dataset run of a crosstab, or the main jd dataset
     * if the crosstab dataset and/or the dataset run are null.
     * 
     * @param element
     * @param jd
     * @return crosstab's JRDesignDataset
     */
    public static JRDesignDataset getCrosstabDataset(JRDesignCrosstab element, JasperDesign jd)
    {
                JRElementDataset elementDataset = element.getDataset();
                if (elementDataset != null && elementDataset.getDatasetRun() != null)
                {
                    // Get the dataset name...
                    String datasetName = elementDataset.getDatasetRun().getDatasetName();
                    
                    return (JRDesignDataset)jd.getDatasetMap().get(datasetName);
                }    
                
                return (JRDesignDataset)jd.getMainDataset();
    }
    
    
    /**
     * Return a JRDesignGroup from a band if this band is the header or the foorter of a group.
     */
    public static JRDesignGroup getGroupFromBand(JasperDesign jd, JRDesignBand band) {
        
        if (band == null || jd == null || band.getOrigin().getGroupName() == null) return null;
        String s = band.getOrigin().getGroupName();

        JRGroup[] groups = jd.getGroups();
        for (int i=0; i<groups.length; ++i)
        {
                if (groups[i].getName().equals(s) ) return (JRDesignGroup)groups[i];
        }
        
        return null;
    }

    /**
     * Check if  parent is Ancestor of g
     */
    public static boolean isAncestorElemenetGroup(JRElementGroup parent, JRElementGroup g) {
        
        while (g != null)
        {
            if (g instanceof JRDesignBand || g instanceof JRDesignCellContents) return false;
            if (g == parent) return true;
            g = g.getElementGroup();
        }
        
        return false;
    }

    /**
     * Find element in elements. If an element is a frame or can have children, look inside the frame
     * recursively
     **/
    public static boolean isChildOf(JRDesignElement element,  JRElement[] elements)
    {
        return isChildOf(element, Arrays.asList(elements));
//        for (int i=0; i<elements.length; ++i)
//        {
//            if (element == elements[i]) return true;
//            if (elements[i] instanceof JRDesignFrame)
//            {
//                if (isChildOf(element, ((JRDesignFrame)elements[i]).getElements() ))
//                    return true;
//            }
//        }
//        return false;
    }

    public static boolean isChildOf(JRDesignElement element, List childrenElements) {

        if (childrenElements == null) return false;
        for (int i=0; i<childrenElements.size(); ++i)
        {
            if (childrenElements.get(i) instanceof JRDesignElement)
            {
                if (isChildOf(element, (JRDesignElement)childrenElements.get(i)))
                    return true;
            }
        }
        return false;


    }

    /**
     * The method returns true if element is equals to elementParent or
     * if elementParent is of type JRDesignFrame and it belongs to it or
     * if elementParent is of type JRDesignComponentElement and it belongs to it.
     * @param element
     * @param elementParent
     * @return
     */
    public static boolean isChildOf(JRDesignElement element,  JRDesignElement elementParent)
    {
        if (element == elementParent)
        {
            return true;
        }
        if (elementParent instanceof JRDesignFrame)
        {
            if (isChildOf(element, ((JRDesignFrame)elementParent).getElements() ))
            {
                return true;
            }
        }
        else if (elementParent instanceof JRDesignComponentElement)
        {
            // In this case we need to find if the component can have children.
            // The easiest way to do it is looking for its widget...
            try {
                JRDesignElementWidget w = (JRDesignElementWidget)(IReportManager.getInstance().getActiveVisualView().getReportDesignerPanel().getActiveScene().findWidget(elementParent));
                if (w != null && w.getChildrenElements() != null)
                {
                    if (isChildOf(element, w.getChildrenElements()))
                    {
                        return true;
                    }
                }
            } catch (Throwable t) {}
        }
        return false;
    }

    public static boolean isGroupHeader(JRBand b, JasperDesign jd) {
        
        if (b == null) return false;
        return ((JRDesignBand)b).getOrigin().getBandTypeValue() == BandTypeEnum.GROUP_HEADER;
//        JRGroup[] groups = jd.getGroups();
//        for (int i=0; i<groups.length; ++i)
//        {
//            if (b == groups[i].getGroupHeader()) return true;
//        }
//        return false;
    }
    
    public static boolean isGroupFooter(JRBand b, JasperDesign jd) {
        
        if (b == null) return false;
        return ((JRDesignBand)b).getOrigin().getBandTypeValue() == BandTypeEnum.GROUP_FOOTER;
//        JRGroup[] groups = jd.getGroups();
//        for (int i=0; i<groups.length; ++i)
//        {
//            if (b == groups[i].getGroupFooter()) return true;
//        }
//        return false;
    }

    public static String nameOf(JROrigin origin) {
        if (origin != null)
        {
            switch (origin.getBandTypeValue())
            {
                case BACKGROUND: return I18n.getString("band.name.background");
                case TITLE: return I18n.getString("band.name.title");
                case PAGE_HEADER: return I18n.getString("band.name.pageHeader");
                case COLUMN_HEADER: return I18n.getString("band.name.columnHeader");
                case DETAIL: return I18n.getString("band.name.detail");
                case COLUMN_FOOTER: return I18n.getString("band.name.columnFooter");
                case PAGE_FOOTER: return I18n.getString("band.name.pageFooter");
                case LAST_PAGE_FOOTER: return I18n.getString("band.name.lastPageFooter");
                case SUMMARY: return I18n.getString("band.name.summary");
                case NO_DATA: return I18n.getString("band.name.noData");
                case GROUP_HEADER:
                {
                    // Find the section number
                    return  I18n.getString("band.name.GroupHeader", origin.getGroupName());
                }
                case GROUP_FOOTER:
                {
                    return  I18n.getString("band.name.GroupFooter", origin.getGroupName());
                }
            }
        }
        
        return I18n.getString("ModelUtils.Title.unknow");
        
    }
    
    public static String nameOf(JRBand b, JasperDesign jd)
    {
        /*
        if (b == null) return null;
        if (b == jd.getTitle()) return "title";
        if (b == jd.getPageHeader()) return "pageHeader";
        if (b == jd.getColumnHeader()) return "columnHeader";
        if (b == jd.getDetail()) return "detail";
        if (b == jd.getColumnFooter()) return "columnFooter";
        if (b == jd.getPageFooter()) return "pageFooter";
        if (b == jd.getSummary()) return "summary";
        if (b == jd.getNoData()) return "noData";
        
        JRGroup[] groups = jd.getGroups();
        for (int i=0; i<groups.length; ++i)
        {
            if (b == groups[i].getGroupHeader()) return groups[i].getName() + "GroupHeader";
            if (b == groups[i].getGroupFooter()) return groups[i].getName() + "GroupFooter";
        }
        */
        JROrigin origin = ((JRDesignBand)b).getOrigin();
        if (origin.getBandTypeValue() == BandTypeEnum.GROUP_HEADER)
        {

                JRGroup group = (JRGroup)jd.getGroupsMap().get(origin.getGroupName());
                int index = getBandIndex(group.getGroupHeaderSection(), b);
                return  I18n.getString("band.name.GroupHeaderSection", origin.getGroupName(),index+1);
        }
        else if (origin.getBandTypeValue() == BandTypeEnum.DETAIL)
        {
                int index = getBandIndex(jd.getDetailSection(), b);
                return  I18n.getString("band.name.detailSection", index+1);
        }
        else if (origin.getBandTypeValue() == BandTypeEnum.GROUP_FOOTER)
        {
                JRGroup group = (JRGroup)jd.getGroupsMap().get(origin.getGroupName());
                int index = getBandIndex(group.getGroupFooterSection(), b);
                return  I18n.getString("band.name.GroupFooterSection", origin.getGroupName(),index+1);
        }

        return nameOf( ((JRDesignBand)b).getOrigin());
    }

    /**
     * Return the index of band in the section.
     * It return -1 if the band is not found in this section
     *
     * @param section
     * @param band
     * @return
     */
    public static int getBandIndex(JRSection section, JRBand band)
    {
        JRBand[] bands = section.getBands();
        for (int i=0;bands != null && i<bands.length; ++i)
        {
            if (bands[i] == band) return i;
        }
        return -1;

    }
    
    /**
     * Returns null if b is the first available band.
     * 
     * @param b 
     * @param jd 
     * @return 
     */
    public static JRBand getPreviousBand(JRBand b, JasperDesign jd)
    {
        if (b == null || jd == null) return null;
        List<JRBand> bands = getBands(jd);
        JRBand previous = null;
        for (JRBand tmpBand : bands)
        {
            if (tmpBand == b) return previous;
            previous = tmpBand;
        }
        return previous;
    }
    
    public static int getBandLocation(JRBand b, JasperDesign jd)
    {
        int yLocation = jd.getTopMargin();
        List<JRBand> bands = ModelUtils.getBands(jd);
            
        for (JRBand tmpBand : bands)
        {
            // Detached background...
            if (tmpBand instanceof JRDesignBand)
            {
                if (((JRDesignBand)tmpBand).getOrigin().getBandTypeValue() == BandTypeEnum.BACKGROUND)
                {
                    if (IReportManager.getInstance().isBackgroundSeparated())
                    {
                        yLocation += jd.getTopMargin();
                        yLocation += jd.getBottomMargin();
                        yLocation += 40;
                    }
                }
            }
            if (tmpBand == b) return yLocation;
            yLocation += tmpBand.getHeight();
        }

        return yLocation;
    }
    
    public static int getBandHeight(JRBand b)
    {
        return (b != null) ? b.getHeight() : 0;
    }
    
    /**
     * This method summarize the JasperReports rules for bands height.
     * The real check should be done by the JRVerifier class, probably
     * we should move that code there providing a similar static method.
     * 
     * @param b
     * @param jd
     * @return
     */
    public static int getMaxBandHeight(JRDesignBand b, JasperDesign jd)
    {
        if (b == null || jd == null) return 0;
        
        JROrigin origin = b.getOrigin();
        
        int topBottomMargins = jd.getTopMargin() + jd.getBottomMargin();
        
        if ( (origin.getBandTypeValue() == BandTypeEnum.TITLE && jd.isTitleNewPage()) ||
             (origin.getBandTypeValue() == BandTypeEnum.SUMMARY) ||  // && jd.isSummaryNewPage()
             origin.getBandTypeValue() == BandTypeEnum.BACKGROUND ||
             origin.getBandTypeValue() == BandTypeEnum.NO_DATA)
        {
            return jd.getPageHeight() - topBottomMargins;
        }
        
        int basicBandsHeight = 0;

        basicBandsHeight += topBottomMargins;
        basicBandsHeight += jd.getPageHeader() != null ? jd.getPageHeader().getHeight() : 0;
        basicBandsHeight += jd.getColumnHeader() != null ? jd.getColumnHeader().getHeight() : 0;
        basicBandsHeight += jd.getColumnFooter() != null ? jd.getColumnFooter().getHeight() : 0;

        if (b.getOrigin().getBandTypeValue() == BandTypeEnum.LAST_PAGE_FOOTER)
        {
            return  jd.getPageHeight() - basicBandsHeight;
        }

        basicBandsHeight += jd.getPageFooter() != null ? jd.getPageFooter().getHeight() : 0;

        int heighestGroupHeader = 0;
        int heighestGroupFooter = 0;

        for (int i=0; i<jd.getGroupsList().size(); ++i)
        {
            JRDesignGroup grp = (JRDesignGroup)jd.getGroupsList().get(i);
            JRBand[] bands = grp.getGroupHeaderSection().getBands();
            for (int k=0; bands != null && k<bands.length; ++k)
            {
                heighestGroupHeader = Math.max(heighestGroupHeader, bands[k].getHeight());
            }
            bands = grp.getGroupFooterSection().getBands();
            for (int k=0; bands != null && k<bands.length; ++k)
            {
                heighestGroupFooter = Math.max(heighestGroupFooter, bands[k].getHeight());
            }
        }

        if (b.getOrigin().getBandTypeValue() == BandTypeEnum.TITLE)
        {
            return  jd.getPageHeight() - basicBandsHeight - Math.max(heighestGroupFooter, heighestGroupHeader);
        }

        if (b.getOrigin().getBandTypeValue() == BandTypeEnum.DETAIL)
        {
            return jd.getPageHeight() - basicBandsHeight;
        }

        int titleHeight = jd.getTitle() != null ? jd.getTitle().getHeight() : 0;
        if (jd.isTitleNewPage()) titleHeight = 0;

        if (origin.getBandTypeValue() == BandTypeEnum.GROUP_FOOTER ||
            origin.getBandTypeValue() == BandTypeEnum.GROUP_HEADER)
        {
            return jd.getPageHeight() - basicBandsHeight - titleHeight;
        }

        //int summaryHeight = jd.getSummary() != null ? jd.getSummary().getHeight() : 0;
        //if (!jd.isSummaryNewPage()) basicBandsHeight += summaryHeight;

        int detailHeight = 0;

        if (jd.getDetailSection() != null)
        {
            JRBand[] bandsList = jd.getDetailSection().getBands();
            for (int k=0; bandsList != null && k<bandsList.length; ++k)
            {
                detailHeight = Math.max(detailHeight,bandsList[k].getHeight());
            }
        }

        int maxAlternativeSection = Math.max( detailHeight,  Math.max(heighestGroupFooter, heighestGroupHeader) + titleHeight);

        basicBandsHeight += maxAlternativeSection;

        int res = jd.getPageHeight() - basicBandsHeight + b.getHeight();
        res = Math.min(res, jd.getPageHeight()-topBottomMargins);
        res = Math.max(res, 0);

        // Calcolate the design page without extra bands and the current band...
        return res;
    }
    
    public static JRBand bandOfElement(JRElement element, JasperDesign jd)
    {
        if (element == null || jd == null) return null;
        
        List<JRBand> bands = ModelUtils.getBands(jd);
        
        for (JRBand tmpBand : bands)
        {
            JRElement[] elements = tmpBand.getElements();
            for (int i=0; i<elements.length; ++i)
            {
                if (element == elements[i]) 
                {
                    return tmpBand;
                }
            }
        }
        
        return null;
    }
    
    
    public static int getDesignHeight(JasperDesign jd )
    {
        int designHeight = 0;
        if (jd != null)
        {
            designHeight += jd.getTopMargin();
            List<JRBand> bands = ModelUtils.getBands(jd);
            
            for (JRBand b : bands)
            {
                designHeight += b.getHeight();
            }
            
            designHeight += jd.getBottomMargin();
        }

        // Detached background...
        if (IReportManager.getInstance().isBackgroundSeparated() &&
            jd.getBackground() != null &&
            jd.getBackground().getHeight() > 0)
        {
            designHeight += jd.getTopMargin();
            designHeight += jd.getBottomMargin();
            designHeight += 40;
        }

        return designHeight;
    }
    
    
    
    public static int getMaximumDesignHeight(JasperDesign jd )
    {
        int maxDesignHeight = 3*jd.getPageHeight(); // BG + page + noData
        if (jd.isTitleNewPage()) maxDesignHeight += jd.getPageHeight();
        if (jd.isSummaryNewPage()) maxDesignHeight += jd.getPageHeight();
        return maxDesignHeight;
    }
    
    public static Node findElementNode(Node rootNode, JRElement element)
    {
        if (rootNode instanceof ElementNode)
        {
            if ( ((ElementNode)rootNode).getElement().equals(element))
            {
                return rootNode;
            }
        }
        
        if (rootNode == null) return null;
        Children children = rootNode.getChildren();
        Node[] nodes = children.getNodes();
        for (int i=0; i<nodes.length; ++i)
        {
            Node node = findElementNode(nodes[i], element);
            if (node != null) return node;
        }
        return null;
    }
    
    /**
     *  Utility function to duplicate a parameter. All the parameter properties
     *  and parameter default value expression are duplicated as well.
     */
    public static JRDesignParameter cloneParameter(JRDesignParameter param)
    {
        JRDesignParameter newParam = new JRDesignParameter();
        newParam.setName( param.getName() );
        newParam.setForPrompting( param.isForPrompting() );
        newParam.setSystemDefined( param.isSystemDefined() );
        //newParam.setValueClass( param.getValueClass() );
        newParam.setValueClassName( param.getValueClassName() );
        newParam.setDescription( param.getDescription());
        if (param.getDefaultValueExpression() != null)
        {
            newParam.setDefaultValueExpression( cloneExpression( (JRDesignExpression)param.getDefaultValueExpression()) );
        }
        
        // Duplicate properties...
        replacePropertiesMap(param.getPropertiesMap(), newParam.getPropertiesMap());
        
        return newParam;
    }

    /**
     * This method creates a new cell matrix ordering the cells based on total position....
     * The matrix is displayed like that:
     *  <pre>
     *       2         1         0
     *   +--------+---------+---------+
     * 3 |  3/2   |   3/1   |  3/0    |
     *   +--------+---------+---------+
     * 2 |  2/2   |   2/1   |  2/0    |
     *   +--------+---------+---------+
     * 1 |  1/2   |   1/1   |  1/0    |
     *   +--------+---------+---------+
     * 0 |  0/2   |   0/1   |  0/0    |
     *   +--------+---------+---------+
     * </pre>
     * @param cells
     * @param row_groups
     * @param col_groups
     * @return
     */
    public static JRCrosstabCell[][] normalizeCell(JRCrosstabCell[][] cells, JRCrosstabRowGroup[] row_groups, JRCrosstabColumnGroup[] col_groups) {
        
        JRCrosstabCell[][] newCells = new JRCrosstabCell[row_groups.length+1][col_groups.length+1];
        
//        System.out.println("cell     Rows " + cells.length + " x columns " + cells[0].length);
//        System.out.flush();
//        System.out.println("newCells Rows " + newCells.length + " x columns " + newCells[0].length);
//        System.out.flush();
        
        int minRow = 0;
        int maxRow = row_groups.length;
        int minCol = 0;
        int maxCol = col_groups.length;
        
        int[] rowsConversion = new int[row_groups.length+1];
        int[] colsConversion = new int[col_groups.length+1];
        
        for (int i=0; i<row_groups.length; ++i)
        {
            if (row_groups[i].getTotalPositionValue() == CrosstabTotalPositionEnum.START)
            {
                rowsConversion[i] = maxRow;
                maxRow--;
            }
            else
            {
                rowsConversion[i] = minRow;
                minRow++;
            }
        }
        rowsConversion[row_groups.length] = minRow;
        
        
        for (int i=0; i<col_groups.length; ++i)
        {
            if (col_groups[i].getTotalPositionValue() == CrosstabTotalPositionEnum.START)
            {
                colsConversion[i] = maxCol;
                maxCol--;
            }
            else
            {
                colsConversion[i] = minCol;
                minCol++;
            }
        }
        colsConversion[col_groups.length] = minCol;
        
        for (int i=0; i<rowsConversion.length; ++i)
        {
            for (int j=0; j<colsConversion.length; ++j)
            {
                int x = rowsConversion[i];
                int y = colsConversion[j];
                try {
                    JRCrosstabCell cell = cells[i][j];
                    newCells[x][y] = cell;
                } catch (Exception ex)
                {
                }
            }
        }
        
        
//        // Print all:
//        for (int i=cells.length-1; i>=0; --i)
//        {
//            System.out.print(" >> ");
//            for (int j=cells[i].length-1; j>=0; --j)
//            { 
//                
//                 JRCrosstabCell cell = cells[i][j];
//                 if (cell == null)   System.out.print("NULL\t|");
//                 else System.out.print(ModelUtils.nameOf((JRDesignCellContents)cell.getContents()) + "\t|");
//            }
//            System.out.println("");
//        }
//        System.out.println("---------------");
//        for (int i=newCells.length-1; i>=0; --i)
//        {
//            for (int j=newCells[i].length-1; j>=0; --j)
//            { 
//                 JRCrosstabCell cell = newCells[i][j];
//                 if (cell == null)   System.out.print("NULL\t|");
//                 else System.out.print(ModelUtils.nameOf((JRDesignCellContents)cell.getContents()) + "\t|");
//            }
//            System.out.println("");
//        }
//        System.out.println("----------------------------------");
//        System.out.flush();
        return newCells;
        
    }

    
    public static void fixElementsExpressions(JasperDesign jd, String oldName, String newName, byte chunckType, String newClassName) {
        List<JRBand> bands = ModelUtils.getBands(jd);
        for (JRBand b : bands)
        {
            if (b != null && b instanceof JRDesignBand)
            {
                fixElementsExpressions((JRDesignBand)b,oldName,newName,chunckType,newClassName);
            }
        }
    }
    
    public static void fixElementsExpressions(JRDesignCrosstab crosstab, String oldName, String newName, byte chunckType, String newClassName) {
        
        List<JRDesignCellContents> cells = ModelUtils.getAllCells(crosstab);
        for (JRDesignCellContents cell : cells)
        {
            if (cell != null && cell instanceof JRDesignCellContents)
            {
                fixElementsExpressions(cell,oldName,newName,chunckType,newClassName);
            }
        }
    }
    
    public static void fixElementsExpressions(JRDesignElementGroup group, String oldName, String newName, byte chunckType, String newClassName) {
        
        List list = group.getChildren();
        for (int i=0; i<list.size(); ++i)
        {
            Object obj = list.get(i);
            if (obj == null) continue;
            if (obj instanceof JRDesignElementGroup)
            {
                fixElementsExpressions((JRDesignElementGroup)obj,oldName,newName,chunckType,newClassName);
            }
            else if (obj instanceof JRDesignTextField)
            {
                fixElementExpressionText((JRDesignTextField)obj,oldName,newName,chunckType,newClassName);
            }
            else if (obj instanceof JRDesignImage)
            {
                fixElementExpressionImage((JRDesignImage)obj,oldName,newName,chunckType,newClassName);
            }
            
            
        }
        
    }
    
    public static void fixElementExpressionText(JRDesignTextField textfield, String oldName, String newName, byte chunckType, String newClassName) {
        
        JRDesignExpression exp = (JRDesignExpression)textfield.getExpression();
        replaceChunkText( exp, oldName, newName, chunckType, newClassName);
        if (exp != null && exp.getValueClassName() != null && newClassName != null)
        {
            CreateTextFieldAction.setMatchingClassExpression(exp, exp.getValueClassName(), true);
            textfield.getEventSupport().firePropertyChange( JRDesignTextField.PROPERTY_EXPRESSION, null, exp);
        }
        
    }
    
    public static void fixElementExpressionImage(JRDesignImage imageelement, String oldName, String newName, byte chunckType, String newClassName) {
        
        JRDesignExpression exp = (JRDesignExpression)imageelement.getExpression();
        replaceChunkText( exp, oldName, newName, chunckType, newClassName);
        if (exp != null)
        {
            imageelement.getEventSupport().firePropertyChange( JRDesignTextField.PROPERTY_EXPRESSION, null, exp);
        }
        
    }
    
    /**
     * Replace a particular name with another name.
     * If newClassName is null, it is ignored, otherwise the expression will take the new class value.
     * if oldName is null or newName is null, nothing is done
     * if oldName and newName are equals,  nothing is done
     * if exp is null, nothing is done
     * 
     * @param exp
     * @param oldName
     * @param newName
     * @param chunckType
     * @param newClassName (can be null)
     */
    public static void replaceChunkText(JRDesignExpression exp, String oldName, String newName, byte chunckType, String newClassName) {
        if (exp == null) return;
        if (oldName == null || newName == null) return;
        if (oldName.equals(newName)) return;
        
        // replace the correct chunk. TODO: better implementation?
        String oldString = "";
        String pre = "";
        String post = "";
        
        switch (chunckType)
        {
            case JRExpressionChunk.TYPE_FIELD:
                pre = "$F{";  post = "}"; break;
            case JRExpressionChunk.TYPE_VARIABLE:
                pre = "$V{";  post = "}"; break;
            case JRExpressionChunk.TYPE_PARAMETER:
                pre = "$P{";  post = "}";  break;
            case JRExpressionChunk.TYPE_RESOURCE:
                pre = "$R{";  post = "}";  break;
            default:
               break;
        }
        
        oldName = pre + oldName + post;
        newName = pre + newName + post;
        
        if (exp.getText() == null || exp.getText().indexOf(oldName) < 0) return;
          
        exp.setText( Misc.string_replace(newName, oldName, exp.getText()));
        if (exp.getValueClassName() != null &&
            newClassName != null &&
            !exp.getValueClassName().equals(newClassName))
        {
            exp.setValueClassName( newClassName );
        }
    }
    
    /**
     * replace the properties in dest with the ones found in source.
     */
    public static void replacePropertiesMap(JRPropertiesMap source, JRPropertiesMap dest)
    {
        // Update names.
        String[] propertyNames = source.getPropertyNames();
        
        if (propertyNames != null && propertyNames.length > 0)
        {
                for(int i = 0; i < propertyNames.length; i++)
                {
                        dest.setProperty(propertyNames[i], source.getProperty(propertyNames[i]));
                }
        }
        
        // Remove unset names...
        propertyNames = dest.getPropertyNames();
        if (propertyNames != null && propertyNames.length > 0)
        {
                for(int i = 0; i < propertyNames.length; i++)
                {
                    if (!source.containsProperty(propertyNames[i]))
                        dest.removeProperty(propertyNames[i]);
                }
        }
    }
    
    /**
     * replace the expression properties in element with the ones found in newExpressionProperties.
     */
    public static void replaceExpressionProperties(JRDesignElement element, List<GenericProperty> newExpressionProperties)
    {
        // Update names.
        
        List usedProps = new ArrayList();
//        List propertyExpressions = element.getPropertyExpressionsList();
        
        while(element.getPropertyExpressionsList().size() > 0)
        {
            element.removePropertyExpression((JRPropertyExpression)element.getPropertyExpressionsList().get(0));
        }
        
        if (newExpressionProperties == null) return;
        for(GenericProperty prop : newExpressionProperties)
        {
            if (!prop.isUseExpression()) continue;
            JRDesignPropertyExpression newProp = new JRDesignPropertyExpression();
            newProp.setName(prop.getKey());
            newProp.setValueExpression(prop.getExpression());
            element.addPropertyExpression(newProp);
        }
        
    }
    
    /**
     *  Utility function to duplicate an expression.
     */
    public static JRDesignExpression cloneExpression(JRExpression exp)
    {
        if (exp == null) return null;
        JRDesignExpression newExp = new JRDesignExpression();
        //exp.setValueClass( exp.getValueClass() );
        newExp.setValueClassName( exp.getValueClassName() );
        newExp.setText( exp.getText() );
        return newExp;
    }

//    /**
//     * This utility looks for the phisical parent of an element and returns his position.
//     * This position is refers to the plain document preview, where 0,0 are the coordinates
//     * of the upperleft corner of the document.
//     * If no parent is found, the method returns 0,0
//     *
//     * @deprecated Use getParentLocationVisual instead
//     */
//    public static Point getParentLocation(JasperDesign jd, JRDesignElement element )
//    {
//        return getParentLocationVisual(jd, element, null);
//    }
    /**
     * This utility looks for the phisical parent of an element and returns his position.
     * This position is refers to the plain document preview, where 0,0 are the coordinates
     * of the upperleft corner of the document.
     * If no parent is found, the method returns 0,0
     */ 
    public static Point getParentLocation(JasperDesign jd, JRDesignElement element, JRDesignElementWidget widget)
    {
        // Check if we are able to find the element widget somewhere before giving up...
        if (widget == null &&
            IReportManager.getInstance().getActiveVisualView() != null &&
            IReportManager.getInstance().getActiveVisualView().getReportDesignerPanel() != null &&
            IReportManager.getInstance().getActiveVisualView().getReportDesignerPanel().getActiveScene() != null)
        {
            Widget wx = IReportManager.getInstance().getActiveVisualView().getReportDesignerPanel().getActiveScene().findWidget(element);
            if (wx != null && wx instanceof JRDesignElementWidget)
            {
                widget = (JRDesignElementWidget)wx;
            }
        }
        
        if (widget != null && widget.getScene() instanceof AbstractReportObjectScene)
        {
            return ((AbstractReportObjectScene)widget.getScene()).getParentLocation(jd, element, widget);
        }

        return getParentLocationImpl(jd, element, widget);
    }

    /**
     * This utility looks for the phisical parent of an element and returns his position.
     * This position is refers to the plain document preview, where 0,0 are the coordinates
     * of the upperleft corner of the document.
     * If no parent is found, the method returns 0,0
     */
    public static Point getParentLocationImpl(JasperDesign jd, JRDesignElement element, JRDesignElementWidget widget)
    {

        Point base = null;
        if (element == null) return new Point(0,0);

        JRElementGroup grp = element.getElementGroup();

        if (grp == element)
        {
            System.out.println("ERROR ---------- Element group same as element!!!");
            System.out.flush();
            return null;

        }
        // I need to discover the first logical parent of this element
        while (grp != null)    // Element placed in a frame
        {
            if (grp instanceof JRDesignBand)    // Element placed in a band
            {
                JRDesignBand band = (JRDesignBand)grp;
                base = new Point(
                        jd.getLeftMargin(),  // X
                        ModelUtils.getBandLocation(band, jd) // Y
                );

                if ((band.getOrigin().getBandTypeValue() == BandTypeEnum.DETAIL ||
                     band.getOrigin().getBandTypeValue() == BandTypeEnum.COLUMN_FOOTER ||
                     band.getOrigin().getBandTypeValue() == BandTypeEnum.COLUMN_HEADER ||
                     (band.getOrigin().getBandTypeValue() == BandTypeEnum.GROUP_HEADER && jd.getPrintOrderValue() == PrintOrderEnum.VERTICAL) ||
                     (band.getOrigin().getBandTypeValue() == BandTypeEnum.GROUP_FOOTER && jd.getPrintOrderValue() == PrintOrderEnum.VERTICAL)
                     ) &&
                     //jd.getPrintOrderValue() == PrintOrderEnum.HORIZONTAL &&
                     jd.getColumnDirection() == RunDirectionEnum.RTL &&
                     jd.getColumnCount() > 1)
                {
                    // the band origin is different... let's calculate it...
                    int x = jd.getLeftMargin() + (jd.getColumnCount()-1)*jd.getColumnWidth() + (jd.getColumnCount()-1)*jd.getColumnSpacing();
                    base.x = x;
                }

                break;
            }
            else if (grp instanceof JRDesignCellContents)    // Element placed in a cell
            {
                // TODO: calculate cell position....
                JRDesignCellContents cell = (JRDesignCellContents)grp;
                base = getCellLocation(cell.getOrigin().getCrosstab(),cell);
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
                if (widget.getScene() instanceof ReportObjectScene)
                {
                    JRDesignElementWidget owner = ((ReportObjectScene)widget.getScene()).findCustomComponentOwner(element);
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
     * This utility looks for the phisical parent of an element and returns his position.
     * This position is refers to the plain document preview, where 0,0 are the coordinates
     * of the upperleft corner of the document.
     * If no parent is found, the method returns 0,0
     */
    public static Rectangle getParentBounds(JasperDesign jd, JRDesignElement element )
    {
        return getParentBounds(jd, element, null);
    }


      /**
     * This utility looks for the phisical parent of an element and returns his position.
     * This position is refers to the plain document preview, where 0,0 are the coordinates
     * of the upperleft corner of the document.
     * If no parent is found, the method returns 0,0
     */
    public static Rectangle getParentBounds(JasperDesign jd, JRDesignElement element, JRDesignElementWidget widget)
    {
        // Check if we are able to find the element widget somewhere before giving up...
        if (widget == null &&
            IReportManager.getInstance().getActiveVisualView() != null &&
            IReportManager.getInstance().getActiveVisualView().getReportDesignerPanel() != null &&
            IReportManager.getInstance().getActiveVisualView().getReportDesignerPanel().getActiveScene() != null)
        {
            Widget wx = IReportManager.getInstance().getActiveVisualView().getReportDesignerPanel().getActiveScene().findWidget(element);
            if (wx != null && wx instanceof JRDesignElementWidget)
            {
                widget = (JRDesignElementWidget)wx;
            }
        }

        if (widget != null && widget.getScene() instanceof AbstractReportObjectScene)
        {
            return ((AbstractReportObjectScene)widget.getScene()).getParentBounds(jd, element, widget);
        }

        return getParentBoundsImpl(jd, element, widget);
    }

    public static Rectangle getParentBoundsImpl(JasperDesign jd, JRDesignElement element, JRDesignElementWidget widget)
    {
        Rectangle base = null;
        if (element == null) return new Rectangle(0,0,0,0);

        JRElementGroup grp = element.getElementGroup();

        // I need to discover the first logical parent of this element
        while (grp != null)    // Element placed in a frame
        {
            if (grp instanceof JRDesignBand)    // Element placed in a band
            {
                JRDesignBand band = (JRDesignBand)grp;
                base = new Rectangle(
                        jd.getLeftMargin(),  // X
                        ModelUtils.getBandLocation(band, jd), // Y
                        jd.getPageWidth() - jd.getLeftMargin() - jd.getRightMargin(), //width
                        band.getHeight()); //height

                break;
            }
            else if (grp instanceof JRDesignCellContents)    // Element placed in a cell
            {
                // TODO: calculate cell position....
                JRDesignCellContents cell = (JRDesignCellContents)grp;
                Point p = getCellLocation(cell.getOrigin().getCrosstab(),cell);
                base = new Rectangle(p.x, p.y, cell.getWidth(), cell.getHeight());
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
                if (widget.getScene() instanceof ReportObjectScene)
                {
                    JRDesignElementWidget owner = ((ReportObjectScene)widget.getScene()).findCustomComponentOwner(element);
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
     *  Utility function to duplicate a parameter. All the parameter properties
     *  and parameter default value expression are duplicated as well.
     */
    public static JRDesignField cloneField(JRDesignField field) throws CloneNotSupportedException
    {
        /*
        JRDesignField newField = new JRDesignField();
        newField.setName( field.getName() );
        newField.setValueClassName( field.getValueClassName() );
        newField.setDescription(field.getDescription());
        // Duplicate properties...
        replacePropertiesMap(field.getPropertiesMap(), newField.getPropertiesMap());
        
        return newField;
         */
        return (JRDesignField)field.clone();
    }
    
    
    /**
     *  Utility function to duplicate a parameter. All the parameter properties
     *  and parameter default value expression are duplicated as well.
     */
    public static JRDesignVariable cloneVariable(JRDesignVariable variable)
    {
        return cloneVariable(variable, null);
    }

    /**
     *  Utility function to duplicate a parameter. All the variable properties
     *  and variable default value expression are duplicated as well.
     *
     *  The design dataset can be null, and it is used to check the existens of the group
     *  (if not null) in the desitination dataset.
     *
     */
    public static JRDesignVariable cloneVariable(JRDesignVariable variable, JRDesignDataset ds)
    {
        JRDesignVariable newVariable = new JRDesignVariable();
        newVariable.setName( variable.getName() );
        newVariable.setValueClassName( variable.getValueClassName() );
        newVariable.setCalculation( variable.getCalculationValue() );
        newVariable.setExpression( cloneExpression( variable.getExpression()) );
        newVariable.setIncrementGroup( variable.getIncrementGroup() );
        newVariable.setIncrementType( variable.getIncrementTypeValue() );
        newVariable.setIncrementerFactoryClassName( variable.getIncrementerFactoryClassName() );
        newVariable.setInitialValueExpression( cloneExpression( variable.getInitialValueExpression() ));
        newVariable.setResetType( variable.getResetTypeValue());
        
        JRGroup group = variable.getIncrementGroup();
        if (group != null && ds != null)
        {
            if (ds.getGroupsMap().containsKey( group.getName()))
            {
                newVariable.setIncrementGroup( (JRGroup)ds.getGroupsMap().get(group.getName()));
            }
            else
            {
                if (newVariable.getIncrementTypeValue() == IncrementTypeEnum.GROUP)
                {
                    newVariable.setIncrementType(IncrementTypeEnum.REPORT);
                    newVariable.setIncrementGroup(null);
                }
            }
        }
        else
        {
            newVariable.setIncrementGroup( group );
        }


        group = variable.getResetGroup();
        if (group != null && ds != null)
        {
            if (ds.getGroupsMap().containsKey( group.getName()))
            {
                newVariable.setResetGroup( (JRGroup)ds.getGroupsMap().get(group.getName()));
            }
            else
            {
                if (newVariable.getResetTypeValue() == ResetTypeEnum.GROUP)
                {
                    newVariable.setResetType(ResetTypeEnum.REPORT);
                    newVariable.setResetGroup(null);
                }
            }
        }
        else
        {
            newVariable.setResetGroup( group );
        }

        
        newVariable.setSystemDefined( variable.isSystemDefined() );
        return newVariable;
    }

    /**
     *  Utility function to duplicate a group.
     *  No elements are considered, just the group for subdataset
     */
    public static JRDesignGroup cloneGroup(JRDesignGroup group)
    {
        JRDesignGroup newGroup = new JRDesignGroup();
        newGroup.setName( group.getName() );
        newGroup.setExpression( cloneExpression( group.getExpression()) );
        return newGroup;
    }
    
    /**
     * Looks for propertyName in the sets of properties.
     * @param Node.PropertySet[] sets The sets of properties (see Node.getPropertySets()
     * @param String propertyName The name of the property to look for
     * @return true if propertyName is in one of the sets.
     * 
     */
    public static boolean containsProperty(Node.PropertySet[] sets, String propertyName)
    {
        for (int i=0; i<sets.length; ++i)
        {
            Node.Property[] pp = sets[i].getProperties();
            for (int j=0; j<pp.length; ++j)
            {
                String name = pp[j].getName();
                if (name != null && name.equals(propertyName)) return true;
            }
        }
        
        return false;
    }

    /**
     * Looks for propertyName in the sets of properties.
     * @param Node.PropertySet[] sets The sets of properties (see Node.getPropertySets()
     * @param String propertyName The name of the property to look for
     * @return true if propertyName is in one of the sets.
     *
     */
    public static Node.Property findProperty(Node.PropertySet[] sets, String propertyName)
    {
        for (int i=0; i<sets.length; ++i)
        {
            Node.Property[] pp = sets[i].getProperties();
            for (int j=0; j<pp.length; ++j)
            {
                String name = pp[j].getName();
//                if (pp[j] instanceof PatternProperty)
//                {
//                    System.out.println("Property: " + pp[j].getName());
//                }

                if (name != null && name.equals(propertyName)) return pp[j];
            }
        }

        return null;
    }

    /**
     * Look if an element is referencing as parent the group or some group child of this group.
     * Please note that we are not looking iside the group.getElements since the element can
     * no longer be part of the model, but it can still referencing a parent.
     */
    public static boolean isElementChildOf(JRDesignElement element, JRElementGroup group) {
        
        JRElementGroup g1 = element.getElementGroup();
        while (g1 != null)
        {
            if (g1 == group) return true;
            g1 = g1.getElementGroup();
        }
        return false;
    }

    /**
     * Check if an element is orphan or not.
     * An element is orphan when his parent is null, or if is null one of his ancestor parents
     */
    public static boolean isOrphan(JRDesignElement element, JasperDesign jd) {
        
        JRElementGroup group = getTopElementGroup(element);
        if (group == null) return true;
        // Check if it belongs to this jasperdesign...
        if (group instanceof JRBand && !ModelUtils.getBands(jd).contains(group)) return true;

        // We should check if it the parent belongs for real to this report...
        if (group instanceof JRDesignCellContents)
        {
            // TODO check if this cell contents belongs to a crosstab in this report...
            JRDesignCellContents cell = (JRDesignCellContents)group;
        }

        
        return  false;
    }
    
    /**
     * Check if an element is orphan or not.
     * An element is orphan when his parent is null, or if it is null one of his ancestors
     */
    public static JRElementGroup getTopElementGroup(JRDesignElement element) {
        
        JRElementGroup g1 = element.getElementGroup();
        while (g1 != null)
        {
            //if (!g1.getChildren().contains(element)) return null; // The element points to its parent, but its parent has not it as child
            if (g1 instanceof JRDesignBand || g1 instanceof JRDesignCellContents) return g1;
            g1 = g1.getElementGroup();

        }
        return null;
    }

    
    /**
     * Return the band at the specified point.
     * In the point is not inside a band, it returns null;
     **/
    public static JRDesignBand getBandAt(JasperDesign jd, Point p)
    {
        if (p.x < jd.getLeftMargin()) return null;
        if (p.x > jd.getPageWidth() - jd.getRightMargin()) return null;
        if (p.y < jd.getTopMargin()) return null;
         
        List<JRBand> bands = ModelUtils.getBands(jd);
            
        int currentHeight = jd.getTopMargin();
        for (JRBand tmpBand : bands)
        {
            if (tmpBand instanceof JRDesignBand)
            {
                if (((JRDesignBand)tmpBand).getOrigin().getBandTypeValue() == BandTypeEnum.BACKGROUND &&
                    IReportManager.getInstance().isBackgroundSeparated())
                {
                    continue;
                }

            }

            currentHeight += tmpBand.getHeight();
            if (p.y < currentHeight) return (JRDesignBand)tmpBand;
        }

        if (IReportManager.getInstance().isBackgroundSeparated() &&
            jd.getBackground() != null &&
            jd.getBackground().getHeight() > 0 )
        {
            currentHeight += 40 + jd.getTopMargin() + jd.getBottomMargin();
            if (p.y >= currentHeight &&
                p.y < jd.getBackground().getHeight() + currentHeight)
            {
                return (JRDesignBand)jd.getBackground();
            }
        }
        
        return null;
    }
    
    public static JRDesignDataset getDatasetFromChartDataset(JRDesignChartDataset dataset, JasperDesign jd)
    {
        JRDesignDataset ds = (JRDesignDataset)jd.getMainDataset();
        if (dataset.getDatasetRun() != null &&
            dataset.getDatasetRun().getDatasetName() != null)
        {
            ds = (JRDesignDataset)jd.getDatasetMap().get( dataset.getDatasetRun().getDatasetName() );
        }     
        
        return ds;
    }
    
    public static JRDesignDataset getDatasetFromCrosstabDataset(JRDesignCrosstabDataset dataset, JasperDesign jd)
    {
        JRDesignDataset ds = (JRDesignDataset)jd.getMainDataset();
        if (dataset.getDatasetRun() != null &&
            dataset.getDatasetRun().getDatasetName() != null)
        {
            ds = (JRDesignDataset)jd.getDatasetMap().get( dataset.getDatasetRun().getDatasetName() );
        }     
        
        return ds;
    }

    public static void copyHyperlink(JRHyperlink from, JRHyperlink to)
    {
        if (from == null || to == null) return;
        try {
            
            setHyperlinkAttribute(to, "HyperlinkAnchorExpression", JRExpression.class, (from.getHyperlinkAnchorExpression() == null) ? null : from.getHyperlinkAnchorExpression().clone() );
            setHyperlinkAttribute(to, "HyperlinkPageExpression", JRExpression.class, (from.getHyperlinkPageExpression() == null) ? null : from.getHyperlinkPageExpression().clone() );
            setHyperlinkAttribute(to, "HyperlinkReferenceExpression", JRExpression.class, (from.getHyperlinkReferenceExpression() == null) ? null : from.getHyperlinkReferenceExpression().clone() );
            setHyperlinkAttribute(to, "LinkTarget", String.class, from.getLinkTarget() );
            setHyperlinkAttribute(to, "HyperlinkTarget", Byte.TYPE, from.getHyperlinkTarget() );
            setHyperlinkAttribute(to, "HyperlinkTooltipExpression", JRExpression.class, (from.getHyperlinkTooltipExpression() == null) ? null : from.getHyperlinkTooltipExpression().clone() );
            setHyperlinkAttribute(to, "LinkType", String.class, (from.getLinkType() == null) ? null : from.getLinkType() );
            setHyperlinkAttribute(to, "HyperlinkWhenExpression", JRExpression.class, (from.getHyperlinkWhenExpression()== null) ? null : from.getHyperlinkWhenExpression().clone() );

            
            
            // remove all the old params...
            JRHyperlinkParameter[] params = from.getHyperlinkParameters();
            List parameters = getHyperlinkParametersList(to);
            parameters.clear();

            if (params != null)
            {
                for (int i=0; i<params.length; ++i)
                {
                    parameters.add( params[i].clone() );
                }
            }
            
        } catch (Throwable t)
        {
            t.printStackTrace();
        }
    }

    /**
     * This methos looks if this node is in some way a Nephew or a Child or the same Crosstab node.
     * It does it first looking in the node lookup, then going trough the parent chain
     * 
     * @param node
     * @param designCrosstab
     * @return
     */
    static boolean isNephewOf(Node node, JRDesignCrosstab designCrosstab)
    {
        if (node == null || designCrosstab == null) return false;
        if (node.getLookup().lookup(JRDesignCrosstab.class) == designCrosstab) return true;
        
        if (node.getParentNode() != null)
        {
            return isNephewOf(node.getParentNode(),designCrosstab);
        }
        
        return false;
    }
    
    /**
     * We assume the  JRHyperlink has always a way to get the parameters a list...
     * @param hl
     * @return the list of parameters
     */
    private static List getHyperlinkParametersList(JRHyperlink hyperlink)
    {
        if (hyperlink == null) return null;
        try {
            Method m = hyperlink.getClass().getMethod("getHyperlinkParametersList");
            return (List)m.invoke(hyperlink);
        }
        catch (Throwable t) {
            t.printStackTrace(); 
        }
        return null;
    }
    
    /**
     * We assume the  JRHyperlink has always a way to get the parameters a list...
     * @param hl
     * @return the list of parameters
     */
    private static void setHyperlinkAttribute(JRHyperlink hyperlink, String attribute, Class clazz, Object value)
    {
        if (hyperlink == null) return;
        try {
            Method m = hyperlink.getClass().getMethod("set" + attribute, clazz);
            m.invoke(hyperlink, value);
            IReportManager.getInstance().notifyReportChange();
        }
        catch (Throwable t) { t.printStackTrace(); }
    }
    
    
    public static void applyBoxProperties(JRLineBox to, JRLineBox from)
    {
        // Paddings...
        to.setPadding(from.getOwnPadding());
        to.setLeftPadding( from.getOwnLeftPadding() );
        to.setRightPadding( from.getOwnRightPadding() );
        to.setBottomPadding( from.getOwnBottomPadding() );
        to.setTopPadding( from.getOwnTopPadding() );
        
        // Pens...
        applyPenProperties( to.getPen(), from.getPen());
        applyPenProperties( to.getTopPen(), from.getTopPen());
        applyPenProperties( to.getLeftPen(), from.getLeftPen());
        applyPenProperties( to.getBottomPen(), from.getBottomPen());
        applyPenProperties( to.getRightPen(), from.getRightPen());
        
    }
    
    public static void applyPenProperties(JRPen to, JRPen from)
    {
        // Paddings...
        to.setLineColor( from.getOwnLineColor() );
        to.setLineWidth( from.getOwnLineWidth() );
        to.setLineStyle( from.getOwnLineStyleValue() );
     }

     public static void applyDiff(JRLineBox main, JRLineBox box) {
        
         if (main.getOwnPadding() != box.getOwnPadding()) main.setPadding(null);
         if (main.getOwnLeftPadding() != box.getOwnLeftPadding()) main.setLeftPadding(null);
         if (main.getOwnRightPadding() != box.getOwnRightPadding()) main.setRightPadding(null);
         if (main.getOwnBottomPadding() != box.getOwnBottomPadding()) main.setBottomPadding(null);
         if (main.getOwnTopPadding() != box.getOwnTopPadding()) main.setTopPadding(null);
         
         applyDiff( main.getPen(), box.getPen());
         applyDiff( main.getTopPen(), box.getTopPen());
         applyDiff( main.getLeftPen(), box.getLeftPen());
         applyDiff( main.getBottomPen(), box.getBottomPen());
         applyDiff( main.getRightPen(), box.getRightPen());
         
     }
     
     public static void applyDiff(JRPen main, JRPen from)
     {
        // Paddings...
        if (main.getOwnLineColor() != null && !main.getOwnLineColor().equals(from.getOwnLineColor())) main.setLineColor(null);
        if (main.getOwnLineWidth() != null && !main.getOwnLineWidth().equals(from.getOwnLineWidth())) main.setLineWidth(null);
        if (main.getOwnLineStyleValue() != null && main.getOwnLineStyleValue() != from.getOwnLineStyleValue()) main.setLineStyle((LineStyleEnum)null);
     }

     /**
      * This method is a workaround for the getGroupFooter implemented in JR 3.5.2. It should be not used after the release
      * of JR 3.5.3
      * @param gruop
      * @return
      */
     public static JRBand getGroupFooter(JRGroup group)
     {
             if (group.getGroupFooterSection() != null)
             {
                 JRBand[] footers = group.getGroupFooterSection().getBands();
                 if (footers != null && footers.length > 0)
                 {
                     return footers[0];
                 }
             }
         return null;
     }
}
