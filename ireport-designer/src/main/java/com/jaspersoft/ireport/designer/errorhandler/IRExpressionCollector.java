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
package com.jaspersoft.ireport.designer.errorhandler;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class IRExpressionCollector extends JRExpressionCollector {

    JasperDesign jasperDesign = null;
    
    public List extraExpressions = new ArrayList();
    
    public IRExpressionCollector(JasperDesign jd)
    {
        super(null, jd);
        jasperDesign = jd;
    }

    @Override
    public List getExpressions() {
        List expressions = super.getExpressions();
        expressions.addAll(extraExpressions);
        return expressions;
    }
    
    
    
    
    @Override
    public void collect(JRChart element)
    {
            JRDesignChart chart = (JRDesignChart)element;
            super.collect(element);
            chart.getDataset().collectExpressions(this);
            chart.getPlot().collectExpressions(this);
            JRDatasetRun datasetRun = chart.getDataset().getDatasetRun();
            
            
            if (datasetRun != null &&
                datasetRun.getDatasetName() != null)
            {
                    JRExpressionCollector collector = getCollector( (JRDataset)jasperDesign.getDatasetMap().get(datasetRun.getDatasetName()));
                    extraExpressions.addAll(collector.getExpressions());
            }
            System.out.flush();
            
    }
    
}
