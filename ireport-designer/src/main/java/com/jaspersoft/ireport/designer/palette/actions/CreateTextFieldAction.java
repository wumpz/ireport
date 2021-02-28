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
package com.jaspersoft.ireport.designer.palette.actions;

import com.jaspersoft.ireport.designer.IReportManager;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class CreateTextFieldAction extends CreateReportElementsAction
{

    static private java.util.List acceptedTextfieldClasses = null;
    static {
        acceptedTextfieldClasses = new java.util.ArrayList();

        acceptedTextfieldClasses.add("java.lang.Boolean");
        acceptedTextfieldClasses.add("java.lang.Byte");
        acceptedTextfieldClasses.add("java.util.Date");
        acceptedTextfieldClasses.add("java.sql.Timestamp");
        acceptedTextfieldClasses.add("java.sql.Time");
        acceptedTextfieldClasses.add("java.lang.Double");
        acceptedTextfieldClasses.add("java.lang.Float");
        acceptedTextfieldClasses.add("java.lang.Integer");
        acceptedTextfieldClasses.add("java.lang.Long");
        acceptedTextfieldClasses.add("java.lang.Short");
        acceptedTextfieldClasses.add("java.math.BigDecimal");
        acceptedTextfieldClasses.add("java.lang.Number");
        acceptedTextfieldClasses.add("java.lang.String");
    }
    
    public JRDesignElement createReportElement(JasperDesign jd)
    {
        JRDesignTextField element = new JRDesignTextField( jd );
        JRDesignExpression exp = new JRDesignExpression();
        exp.setValueClassName("java.lang.String");
        exp.setText("$F{field}");
        element.setExpression(exp);
        element.setWidth(100);
        element.setHeight(20);
        return element;
    }
    
    /** Setter for property classExpression.
     * If newClass is not in 
     * java.lang.Boolean
     * java.lang.Byte
     * java.util.Date
     * java.sql.Timestamp
     * java.sql.Time
     * java.lang.Double
     * java.lang.Float
     * java.lang.Integer
     * java.lang.Long
     * java.lang.Short
     * java.math.BigDecimal
     * java.lang.Number
     * java.lang.String
     * the method trys to find something of similar using getSuperClass
     * it is used.
     *
     * In no solution is find, the type is set to java.lang.String an optionally is possible
     * change the expression with something like ""+<exp>. This should be very rare...
     * @param classExpression New value of property classExpression.
     * 
     */
    public static void setMatchingClassExpression(JRDesignExpression expression, java.lang.String newClassName, boolean adjustExpression) {
        
        if (!acceptedTextfieldClasses.contains(newClassName))
        {
            try {
                Class newClass = IReportManager.getInstance().getReportClassLoader().loadClass( newClassName );
                setMatchingClassExpression(expression, newClass, adjustExpression);
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            expression.setValueClassName(newClassName);
        }
    }
    
    private static void setMatchingClassExpression(JRDesignExpression expression, Class newClass, boolean adjustExpression) {
        
        // try with the superclasss..
        Class superClass = newClass.getSuperclass();

        if (superClass == null)
        {
            expression.setValueClassName("java.lang.String");
            if (adjustExpression)
            {
                expression.setText("\"\"+" + expression.getText());
            }
        }
        else
        {
            setMatchingClassExpression(expression, superClass.getName(), adjustExpression);
        }
    }

    @Override
    public JRDesignElement[] createReportElements(JasperDesign jd) {
        return new JRDesignElement[]{createReportElement(jd)};
    }

}
