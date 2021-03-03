/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.table.nodes.properties;

import com.jaspersoft.ireport.designer.sheet.JRLineBoxProperty;
import net.sf.jasperreports.components.table.DesignCell;

/**
 *
 * @version $Id: CellPaddingAndBordersProperty.java 0 2010-04-07 14:22:12 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class CellPaddingAndBordersProperty extends JRLineBoxProperty {

    public CellPaddingAndBordersProperty(DesignCell cell)
    {
        super(cell.getLineBox().getBoxContainer());
    }
}
