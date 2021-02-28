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
package com.jaspersoft.ireport.designer.charts.datasets;

import com.jaspersoft.ireport.designer.editor.ExpressionContext;

/**
 *
 * @author gtoffoli
 */
public interface ChartDatasetPanel {
    public void setExpressionContext(ExpressionContext ec);

    /**
     * This method is used to higlight and focus a component that is generating an error.
     * Since the expression to show can be nested in some remote window, the array hold
     * the complete "path" required to open the right expression.
     *
     * The simplest case is a single Integer value (like for the PieChart)
     * A complicated case can be an error in an hyperlink parameter in some category series
     * that would make the array to be something like:
     * [0] CATEGORY_LIST
     * [1] index of the category
     * [2] (used for the category window) COMPONENT_HYPERLINK
     * [3] (for the hyper link component) COMPONENT_PARAMETERS
     * [4] Index of the parameter
     * [5] Expression to hilight in the parameter link window
     *
     */
    public void setFocusedExpression(Object[] expressionInfo);
    
    /**
     * this method is called when the container window is opened...
     */
    public void containerWindowOpened();
    
}
