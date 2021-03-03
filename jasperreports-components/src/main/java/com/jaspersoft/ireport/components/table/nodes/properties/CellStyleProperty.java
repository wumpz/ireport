/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.table.nodes.properties;

import com.jaspersoft.ireport.designer.sheet.properties.AbstractStyleProperty;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @version $Id: CellStyleProperty.java 0 2010-03-31 14:35:17 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class CellStyleProperty  extends AbstractStyleProperty
{
    private final DesignCell cell;

    @SuppressWarnings("unchecked")
    public CellStyleProperty(DesignCell cell, JasperDesign jasperDesign)
    {
        super(cell, jasperDesign);
        this.cell = cell;
    }

    @Override
    public String getStyleNameReference()
    {
        return cell.getStyleNameReference();
    }

    @Override
    public void setStyleNameReference(String s)
    {
        cell.setStyleNameReference(s);
    }

    @Override
    public JRStyle getStyle()
    {
        return cell.getStyle();
    }

    @Override
    public void setStyle(JRStyle s)
    {
        cell.setStyle(s);
    }
}
