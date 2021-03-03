/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.table;

import com.jaspersoft.ireport.designer.GenericDesignerPanel;
import com.jaspersoft.ireport.designer.GenericDesignerPanelFactory;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignRectangle;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @version $Id: TableComponentDesignerPanelFactory.java 0 2010-03-12 16:13:27 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class TableComponentDesignerPanelFactory implements GenericDesignerPanelFactory{

    public boolean accept(JRDesignElement element) {
        return element instanceof JRDesignComponentElement &&
               ((JRDesignComponentElement)element).getComponent() instanceof StandardTable;
    }

    public GenericDesignerPanel createDesigner(JRDesignElement element, JasperDesign jasperdesign) {
        return new TableDesignerPanel(element, jasperdesign );
    }

}
