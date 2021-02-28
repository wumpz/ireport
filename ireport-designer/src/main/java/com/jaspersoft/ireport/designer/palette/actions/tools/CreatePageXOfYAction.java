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
package com.jaspersoft.ireport.designer.palette.actions.tools;

import com.jaspersoft.ireport.designer.palette.actions.CreateTextFieldAction;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Point;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import org.netbeans.api.visual.widget.Scene;

/**
 *
 * @author gtoffoli
 */
public class CreatePageXOfYAction extends CreateTextFieldAction {

    @Override
    public JRDesignElement[] createReportElements(JasperDesign jd) {
        JRDesignTextField[] elements = new JRDesignTextField[2];

        elements[0] = (JRDesignTextField)super.createReportElement( jd );

        ((JRDesignExpression)elements[0].getExpression()).setText("\"" + I18n.getString("Page_X_Of_Y.page", "\"+$V{PAGE_NUMBER}+\"") + "\"");
        ((JRDesignExpression)elements[0].getExpression()).setValueClassName("java.lang.String");

        elements[0].setHorizontalAlignment( HorizontalAlignEnum.RIGHT);
        setMatchingClassExpression(
            ((JRDesignExpression)elements[0].getExpression()),
            "java.lang.String",
            true
            );


        elements[1] = (JRDesignTextField)super.createReportElement( jd );

        ((JRDesignExpression)elements[1].getExpression()).setText("\" \" + $V{PAGE_NUMBER}");
        ((JRDesignExpression)elements[1].getExpression()).setValueClassName("java.lang.String");

        setMatchingClassExpression(
            ((JRDesignExpression)elements[1].getExpression()),
            "java.lang.String",
            true
            );

        elements[1].setEvaluationTime( EvaluationTimeEnum.REPORT);

        return elements;
    }

    @Override
    public void adjustElement(JRDesignElement[] elements, int index, Scene theScene, JasperDesign jasperDesign, Object parent, Point dropLocation) {

        if (index == 0)
        {
            elements[0].setWidth(80);
        }
        if (index == 1)
        {
            elements[1].setWidth(40);
            elements[1].setX( elements[0].getX() + elements[0].getWidth());
        }


    }





}
