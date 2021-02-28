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
package com.jaspersoft.ireport.designer.outline.nodes;

import com.jaspersoft.ireport.designer.ModelUtils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRCrosstabOrigin;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class CrosstabChildren  extends Index.KeysChildren implements PropertyChangeListener {

    Lookup doLkp = null;
    
    
    private JRDesignCrosstab crosstab = null;
    private JasperDesign jd = null;
    
    @SuppressWarnings("unchecked")
    public CrosstabChildren(JasperDesign jd, JRDesignCrosstab crosstab, Lookup doLkp) {
        super(new ArrayList());
        this.doLkp = doLkp;
        this.jd=jd;
        this.crosstab = crosstab;
        this.crosstab.getEventSupport().addPropertyChangeListener(this);
    }
    
    @SuppressWarnings("unchecked")
    public void recalculateKeys() {
        
        List l = (List)lock();
        l.clear();
        
        //AbstractNode parameters = new AbstractNode(doLkp);
        //parameters.setName("Parameters");
        l.add(JRDesignCrosstab.PROPERTY_PARAMETERS);
        
        //AbstractNode rows = new AbstractNode(Children.LEAF,doLkp);
        //rows.setName("Row groups");
        l.add(JRDesignCrosstab.PROPERTY_ROW_GROUPS);
        
        //AbstractNode columns = new AbstractNode(Children.LEAF,doLkp);
        //columns.setName("Column groups");
        l.add(JRDesignCrosstab.PROPERTY_COLUMN_GROUPS);
        
        //AbstractNode measures = new AbstractNode(Children.LEAF, doLkp);
        //measures.setName("Measures");
        l.add(JRDesignCrosstab.PROPERTY_MEASURES);
        
        if (crosstab.getHeaderCell() == null)
        {
            // Add a null cell here
            NullCell cell = new NullCell(new JRCrosstabOrigin( crosstab, JRCrosstabOrigin.TYPE_HEADER_CELL ));
            l.add( cell );
        }
        else
        {
            l.add(crosstab.getHeaderCell());
        }
        
        // Add all the columns...
        JRCrosstabColumnGroup[] columns = crosstab.getColumnGroups();
        for (int i=0; i < columns.length; ++i)
        {
            if ( columns[i].getHeader() == null)
            {
                NullCell cell = new NullCell(new JRCrosstabOrigin( crosstab, JRCrosstabOrigin.TYPE_COLUMN_GROUP_HEADER, null, columns[i].getName() ));
                l.add( cell );
            }
            else
            {
                l.add( columns[i].getHeader() );
            }
            
            if ( columns[i].getHeader() == null)
            {
                NullCell cell = new NullCell(new JRCrosstabOrigin( crosstab, JRCrosstabOrigin.TYPE_COLUMN_GROUP_TOTAL_HEADER, null, columns[i].getName() ));
                l.add( cell );
            }
            else
            {
                l.add( columns[i].getTotalHeader());
            }
            
            if ( columns[i].getCrosstabHeader() == null)
            {
                NullCell cell = new NullCell(new JRCrosstabOrigin( crosstab, JRCrosstabOrigin.TYPE_COLUMN_GROUP_CROSSTAB_HEADER, null, columns[i].getName() ));
                l.add( cell );
            }
            else
            {
                l.add( columns[i].getCrosstabHeader());
            }
            
        }
        // Add all the rows...
        JRCrosstabRowGroup[] rows = crosstab.getRowGroups();
        for (int i=0; i < rows.length; ++i)
        {
            if ( rows[i].getHeader() == null)
            {
                NullCell cell = new NullCell(new JRCrosstabOrigin( crosstab, JRCrosstabOrigin.TYPE_ROW_GROUP_HEADER, rows[i].getName(), null));
                l.add( cell );
            }
            else
            {
                l.add( rows[i].getHeader() );
            }
            
            if ( rows[i].getHeader() == null)
            {
                NullCell cell = new NullCell(new JRCrosstabOrigin( crosstab, JRCrosstabOrigin.TYPE_ROW_GROUP_TOTAL_HEADER, rows[i].getName(), null ));
                l.add( cell );
            }
            else
            {
                l.add( rows[i].getTotalHeader());
            }
        }
        
        // We should use the crosstab.getCells() to have the cells ordered..
        // but the matrix could be not updated...
        
//        JRCrosstabCell[][] cells = crosstab.getCells();
//        for (int i = cells.length-1; i>=0; --i)
//        {
//            for (int j = cells[i].length-1; j>=0; --j)
//            {
//                if (cells[i][j].getContents() == null)
//                {
//                    String rowGrp = (i < rows.length) ? rows[i].getName() : null;
//                    String colGrp = (j < columns.length) ? columns[j].getName() : null;
//                    
//                    NullCell cell = new NullCell(new JRCrosstabOrigin( crosstab, JRCrosstabOrigin.TYPE_DATA_CELL, rowGrp, colGrp ));
//                    l.add( cell );
//                }
//                else
//                {
//                    l.add( cells[i][j].getContents() );
//                }
//            }
//        }
        
        List cells = crosstab.getCellsList();
        for (int i = 0; i<cells.size(); ++i)
        {
            JRCrosstabCell theCell = (JRCrosstabCell) cells.get(i);
            if (theCell.getContents() == null)
            {
                   NullCell cell = new NullCell(
                        new JRCrosstabOrigin( crosstab, JRCrosstabOrigin.TYPE_DATA_CELL, 
                                theCell.getRowTotalGroup(),
                                theCell.getColumnTotalGroup()));
                l.add( cell );
            }
            else
            {
                l.add( theCell.getContents() );
            }
        }
        
        update();
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        recalculateKeys();
    }
    
    @Override
    protected Node[] createNodes(Object key) {
        
        AbstractNode a = new AbstractNode(Children.LEAF,doLkp);
        a.setName(key+"");
        
        if (key instanceof String && key != null && key.equals( JRDesignCrosstab.PROPERTY_PARAMETERS))
        {
            return new Node[]{new CrosstabParametersNode(jd, crosstab, doLkp)};
        }
        else if (key instanceof String && key != null && key.equals( JRDesignCrosstab.PROPERTY_ROW_GROUPS))
        {
            return new Node[]{new CrosstabGroupsNode(jd, crosstab, doLkp, CrosstabGroupNode.ROW_GROUP)};
        }
        else if (key instanceof String && key != null && key.equals( JRDesignCrosstab.PROPERTY_COLUMN_GROUPS))
        {
            return new Node[]{new CrosstabGroupsNode(jd, crosstab, doLkp, CrosstabGroupNode.COLUMN_GROUP)};
        }
        else if (key instanceof String && key != null && key.equals( JRDesignCrosstab.PROPERTY_MEASURES))
        {
            return new Node[]{new CrosstabMeasuresNode(jd, crosstab, doLkp)};
        }
        if (key instanceof JRDesignCellContents)
        {
            CellNode node = new CellNode(jd,crosstab,(JRDesignCellContents)key, doLkp);
            return new Node[]{node};
        }
        else if (key instanceof NullCell)
        {
            NullCellNode node = new NullCellNode(jd, (NullCell)key, crosstab, doLkp);
            return new Node[]{node};
        }
        return new Node[]{a};
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
    }
    

}
