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
package com.jaspersoft.ireport.designer.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JEditorPane;
import net.sf.jasperreports.crosstabs.JRCrosstabParameter;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabMeasure;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignDataset;


/**
 * This class provides a way to define what objects are available in the context of
 * a particular expression.
 * @author gtoffoli
 */
public class ExpressionContext 
{
    public static final String ATTRIBUTE_EXPRESSION_CONTEXT = "EXPRESSION_CONTEXT";

    private static ExpressionContext globalContext = null;
    public static ExpressionContext getGlobalContext()
    {
        return globalContext;
    }

    synchronized public static void setGlobalContext(ExpressionContext g)
    {
        globalContext = g;
    }

    public static JEditorPane activeEditor = null;

    private List<JRDesignDataset> datasets = new ArrayList<JRDesignDataset>();
    private List<JRDesignCrosstab> crosstabs = new ArrayList<JRDesignCrosstab>();

    /**
     *  Return the datasources available for this context.
     *  Actually only one datasource should be visible at time, but in the future
     *  you never know.
     *
     */
    public List<JRDesignDataset> getDatasets() { return datasets; }

    /**
     *  Return the crosstabs available for this context.
     */
    public List<JRDesignCrosstab> getCrosstabs() { return crosstabs; }

    public ExpressionContext() {}

    /**
     * The most commond kind of Expressioncontext used for elements in
     * a document (not in a crosstab), for fields and variables, for
     * crosstab groups (bucket expressions) and measures.
     *
     * @param dataset
     */
    public ExpressionContext(JRDesignDataset dataset)
    {
        datasets.add(dataset);
    }

    /**
     * This kind of expression context should be set for element expressions
     * (like textfields and images) appearing in a crosstab
     *
     * @param crosstab
     */
    public ExpressionContext(JRDesignCrosstab crosstab)
    {
        crosstabs.add(crosstab);
    }


    /**
     * This pass something like name
     * Prefix is V, P, F
     * @return
     */
    public String findObjectClassName(String objName, char type)
    {

            if (type == 'P')
            {
                for (JRDesignDataset dataset : getDatasets())
                {
                    if (dataset.getParametersMap().containsKey(objName)) return ((JRParameter)dataset.getParametersMap().get(objName)).getValueClassName();
                }
                for (JRDesignCrosstab crosstab : getCrosstabs())
                {
                    String className = getCrosstabParameterClassName(crosstab, objName);
                    if (className != null) return className;
                }
            }
            else if (type == 'V')
            {
                for (JRDesignDataset dataset : getDatasets())
                {
                    if (dataset.getVariablesMap().containsKey(objName)) return ((JRVariable)dataset.getVariablesMap().get(objName)).getValueClassName();
                }
                for (JRDesignCrosstab crosstab : getCrosstabs())
                {
                    String className = getCrosstabVariableClassName(crosstab, objName);
                    if (className != null) return className;
                }
            }
            else if (type == 'F')
            {
                for (JRDesignDataset dataset : getDatasets())
                {
                    if (dataset.getFieldsMap().containsKey(objName)) return ((JRField)dataset.getFieldsMap().get(objName)).getValueClassName();
                }
            }

        return null;
    }

    private static final String getCrosstabParameterClassName(JRDesignCrosstab crosstab, String name)
    {
            if (crosstab.getParametersMap().containsKey(name))
            {
                return ((JRCrosstabParameter)crosstab.getParametersMap().get(name)).getValueClassName();
            }
            return null;
    }

    private static final String getCrosstabVariableClassName(JRDesignCrosstab crosstab, String name)
    {

            List rowGroups = crosstab.getRowGroupsList();
            List columnGroups = crosstab.getColumnGroupsList();

            Iterator measures = crosstab.getMesuresList().iterator();
            while (measures.hasNext())
            {
                JRDesignCrosstabMeasure measure = (JRDesignCrosstabMeasure)measures.next();
                if (name.equals(measure.getVariable().getName())) return measure.getVariable().getValueClassName();

                for (int i=0; i<rowGroups.size(); ++i)
                {
                    JRDesignCrosstabRowGroup rowGroup = (JRDesignCrosstabRowGroup)rowGroups.get(i);
                    CrosstabTotalVariable var = new CrosstabTotalVariable(measure, rowGroup, null);
                    if (("$V{"+ name + "}").equals(var.getExpression())) return var.getClassType();

                    for (int j=0; j<columnGroups.size(); ++j)
                    {
                        JRDesignCrosstabColumnGroup columnGroup = (JRDesignCrosstabColumnGroup)columnGroups.get(j);
                        if (j==0)
                        {
                            CrosstabTotalVariable var2 = new CrosstabTotalVariable(measure, null, columnGroup);
                            if (("$V{"+ name + "}").equals(var2.getExpression())) return var2.getClassType();
                        }

                        CrosstabTotalVariable var3 = new CrosstabTotalVariable(measure, rowGroup, columnGroup);
                        if (("$V{"+ name + "}").equals(var3.getExpression())) return var3.getClassType();
                    }
                }
            }

            for (int i=0; i<rowGroups.size(); ++i)
            {
                JRDesignCrosstabRowGroup rowGroup = (JRDesignCrosstabRowGroup)rowGroups.get(i);
                if (name.equals(rowGroup.getVariable().getName())) return rowGroup.getVariable().getValueClassName();
            }

            for (int i=0; i<columnGroups.size(); ++i)
            {
                JRDesignCrosstabColumnGroup columnGroup = (JRDesignCrosstabColumnGroup)columnGroups.get(i);
                if (name.equals(columnGroup.getVariable().getName())) return columnGroup.getVariable().getValueClassName();
            }

            return null;
    }

}
