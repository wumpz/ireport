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
import com.jaspersoft.ireport.designer.utils.Misc;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;
import org.openide.util.Exceptions;

/**
 *
 * @author gtoffoli
 */
public class CreatePercentageTextfieldAction extends CreateTextFieldAction {

    @Override
    public JRDesignElement createReportElement(JasperDesign jd)
    {
        JRDesignTextField element = (JRDesignTextField)super.createReportElement( jd );

        FieldPercentageDialog dialog = new FieldPercentageDialog(Misc.getMainFrame());
        dialog.setJasperDesign(jd);

        dialog.setVisible(true);
        if (dialog.getDialogResult() == JOptionPane.OK_OPTION)
        {
            JRField f = dialog.getSelectedField();
            ResetTypeEnum resetType = dialog.getSelectedResetTypeValue();
            JRGroup group = null;
            if (resetType == ResetTypeEnum.GROUP)
            {
                group = dialog.getSelectedGroup();
            }

            // Let's create the variable...

            JRDesignVariable variable = new JRDesignVariable();
            for (int i = 0; ; ++i)
            {
                String vname = f.getName() + "_SUM";
                if (i > 0) vname += "_" + i;

                if (jd.getVariablesMap().containsKey(vname))
                {
                    continue;
                }

                variable.setName(vname);
                break;
            }

            variable.setExpression( Misc.createExpression( f.getValueClassName(), "$F{" + f.getName() + "}" ));
            variable.setValueClassName( f.getValueClassName() );
            variable.setCalculation( CalculationEnum.SUM);
            variable.setResetType(resetType);
            if (resetType == ResetTypeEnum.GROUP)
            {
                variable.setResetGroup(group);
            }
            try {
                jd.addVariable(variable);
            } catch (JRException ex) {
                ex.printStackTrace();
            }

            ((JRDesignExpression)element.getExpression()).setText("new Double( $F{" + f.getName() + "}.doubleValue() / $V{"+ variable.getName() + "}.doubleValue() )");
            ((JRDesignExpression)element.getExpression()).setValueClassName("java.lang.Double");

            element.setPattern( "#,##0.00%" );
            setMatchingClassExpression(
            ((JRDesignExpression)element.getExpression()),
            ((JRDesignExpression)element.getExpression()).getValueClassName(),true);

            element.setEvaluationTime(EvaluationTimeEnum.AUTO);

            return element;
        }

        return null;
    }
}
