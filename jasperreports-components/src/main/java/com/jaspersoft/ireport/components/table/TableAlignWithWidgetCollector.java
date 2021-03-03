/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.table;

import com.jaspersoft.ireport.designer.actions.ReportAlignWithWidgetCollector;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.List;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @version $Id: TableAlignWithWidgetCollector.java 0 2010-03-31 11:08:27 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class TableAlignWithWidgetCollector extends ReportAlignWithWidgetCollector {

    TableObjectScene scene = null;

    public TableAlignWithWidgetCollector(TableObjectScene scene)
    {
        super(scene);
        this.scene = scene;
    }

    public Collection<Rectangle> getRegions(Widget movingWidget) {

        Collection<Rectangle> regions = super.getRegions(movingWidget);

        // Add all the cells as regions...

        List<TableCell> cells = scene.getTableMatrix().getCells();
       for (TableCell cell : cells)
       {
           regions.add(scene.getTableMatrix().getCellBounds(cell));
       }


        return regions;
    }

    
}
