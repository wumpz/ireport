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
package com.jaspersoft.ireport.components.list;

import com.jaspersoft.ireport.designer.palette.actions.*;
import net.sf.jasperreports.components.list.DesignListContents;
import net.sf.jasperreports.components.list.StandardListComponent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.Exceptions;

/**
 *
 * @author gtoffoli
 */
public class CreateListAction extends CreateReportElementAction {

    @Override
    public JRDesignElement createReportElement(JasperDesign jd) {




        JRDesignComponentElement component = new JRDesignComponentElement();
        StandardListComponent componentImpl = new StandardListComponent();
        DesignListContents contents = new DesignListContents();
        contents.setHeight(50);
        contents.setWidth(0);
        componentImpl.setContents(contents);

        JRDesignDataset newDataset = new JRDesignDataset(false);
        String name = "dataset";
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
        exp.setText("new net.sf.jasperreports.engine.JREmptyDataSource(1)");//NOI18N

        datasetRun.setDataSourceExpression(exp);
        
        
        componentImpl.setDatasetRun(datasetRun);
        component.setComponent(componentImpl);
        component.setComponentKey(new ComponentKey(
                                    "http://jasperreports.sourceforge.net/jasperreports/components",
                                    "jr", "list"));

        component.setWidth(400);
        component.setHeight(50);

        return component;
    }



    
}
