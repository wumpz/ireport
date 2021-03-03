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
package com.jaspersoft.ireport.components.spiderchart;

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.palette.actions.*;
import java.awt.Point;
import java.awt.dnd.DropTargetDropEvent;
import net.sf.jasperreports.components.spiderchart.SpiderChartComponent;
import net.sf.jasperreports.components.spiderchart.StandardChartSettings;
import net.sf.jasperreports.components.spiderchart.StandardSpiderDataset;
import net.sf.jasperreports.components.spiderchart.StandardSpiderPlot;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import org.netbeans.api.visual.widget.Scene;


/**
 *
 * @author gtoffoli
 */
public class CreateSpiderChartAction extends CreateReportElementAction {

    @Override
    public JRDesignElement createReportElement(JasperDesign jd) {




        JRDesignComponentElement component = new JRDesignComponentElement();
        SpiderChartComponent componentImpl = new SpiderChartComponent();

        StandardSpiderDataset spiderDataset = new StandardSpiderDataset();
        StandardChartSettings chartSettings = new StandardChartSettings();
        StandardSpiderPlot spiderPlot = new StandardSpiderPlot();

        
        //chartSettings.setC

        /*
        JRDesignDataset newDataset = new JRDesignDataset(false);
        String name = "spiderChartDataset";
        for (int i = 1;; i++) {
            if (!jd.getDatasetMap().containsKey(name + i)) {
                newDataset.setName(name + i);
                break;
            }
        }
        try {
            jd.addDataset(newDataset);
        } catch (JRException ex) {
            //Exceptions.printStackTrace(ex);
        }

        JRDesignDatasetRun datasetRun = new JRDesignDatasetRun();
        datasetRun.setDatasetName(newDataset.getName());

        JRDesignExpression exp = new JRDesignExpression();
        exp.setValueClassName("net.sf.jasperreports.engine.JRDataSource");//NOI18N
        exp.setText("new net.sf.jasperreports.engine.JREmptyDataSource(0)");//NOI18N

        datasetRun.setDataSourceExpression(exp);

        spiderDataset.setDatasetRun(datasetRun);
        */
        componentImpl.setDataset(spiderDataset);
        componentImpl.setChartSettings(chartSettings);
        componentImpl.setPlot(spiderPlot);

        spiderPlot.setWebFilled(Boolean.TRUE);

        component.setComponent(componentImpl);
        component.setComponentKey(new ComponentKey(
                                    "http://jasperreports.sourceforge.net/jasperreports/components",
                                    "sc", "spiderChart"));

        component.setWidth(200);
        component.setHeight(200);

        return component;
    }


    @Override
    public void drop(DropTargetDropEvent dtde) {

        JRDesignComponentElement element = (JRDesignComponentElement)createReportElement(getJasperDesign());

        if (element == null) return;
        // Find location...
        dropFieldElementAt(getScene(), getJasperDesign(), element, dtde.getLocation());
    }


    public void dropFieldElementAt(Scene theScene, JasperDesign jasperDesign, JRDesignComponentElement element, Point location)
    {
        if (theScene instanceof ReportObjectScene)
        {
            Point p = theScene.convertViewToScene( location );

            // find the band...
            JRDesignBand b = ModelUtils.getBandAt(jasperDesign, p);
            int yLocation = ModelUtils.getBandLocation(b, jasperDesign);
            Point pLocationInBand = new Point(p.x - jasperDesign.getLeftMargin(),
                                              p.y - yLocation);
            if (b != null)
            {

                // if the band is not a detail, propose to aggregate the value...
                if (b.getOrigin().getBandTypeValue() == BandTypeEnum.TITLE)
                {
                    ((SpiderChartComponent)(element.getComponent())).setEvaluationTime(EvaluationTimeEnum.REPORT);
                }
            }
       }

        super.dropElementAt(theScene, jasperDesign, element, location);
    }

    
}
