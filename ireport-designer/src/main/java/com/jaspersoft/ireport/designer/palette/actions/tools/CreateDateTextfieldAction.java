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

import com.jaspersoft.ireport.designer.palette.actions.*;
import com.jaspersoft.ireport.designer.tools.FieldPatternDialog;
import com.jaspersoft.ireport.designer.utils.Misc;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class CreateDateTextfieldAction extends CreateTextFieldAction {



    @Override
    public JRDesignElement createReportElement(JasperDesign jd)
    {
        JRDesignTextField element = (JRDesignTextField)super.createReportElement( jd );

        // get field and reset type...

        



        ((JRDesignExpression)element.getExpression()).setText("new java.util.Date()");
        ((JRDesignExpression)element.getExpression()).setValueClassName("java.util.Date");

        FieldPatternDialog dialog = new FieldPatternDialog(Misc.getMainFrame(),true);
        dialog.setOnlyDate(true);

        dialog.setVisible(true);
        if (dialog.getDialogResult() == JOptionPane.OK_OPTION)
        {
            element.setPattern( dialog.getPattern() );
            setMatchingClassExpression(
            ((JRDesignExpression)element.getExpression()),
            ((JRDesignExpression)element.getExpression()).getValueClassName(),true);

            return element;
        }

        return null;
    }
}
