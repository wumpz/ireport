/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.table.nodes.properties;

import com.jaspersoft.ireport.components.table.TableElementNode;
import com.jaspersoft.ireport.designer.sheet.properties.ExpressionProperty;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import org.openide.util.NbBundle;

/**
 *
 * @version $Id: ColumnPrintWhenExpression.java 0 2010-05-27 10:55:42 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class ColumnPrintWhenExpressionProperty extends ExpressionProperty
{
    private final StandardBaseColumn column;

    public ColumnPrintWhenExpressionProperty(StandardBaseColumn column, JRDesignDataset dataset)
    {
        super(column, dataset);
        this.column = column;
    }

    @Override
    public String getName()
    {
        return StandardBaseColumn.PROPERTY_PRINT_WHEN_EXPRESSION;
    }

    @Override
    public String getDisplayName()
    {
        return NbBundle.getMessage(TableElementNode.class, "column.printWhenExpression");
    }

    @Override
    public String getShortDescription()
    {
        return NbBundle.getMessage(TableElementNode.class, "column.printWhenExpression.description");
    }

    @Override
    public String getDefaultExpressionClassName()
    {
        return Boolean.class.getName();
    }

    @Override
    public JRDesignExpression getExpression()
    {
        return (JRDesignExpression)column.getPrintWhenExpression();
    }

    @Override
    public void setExpression(JRDesignExpression expression)
    {
        column.setPrintWhenExpression(expression);
    }

}

