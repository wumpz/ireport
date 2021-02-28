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
package com.jaspersoft.ireport.designer.templates;

import com.jaspersoft.ireport.designer.utils.Misc;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;

/**
 *
 * @author gtoffoli
 */
public abstract class AbstractReportGenerator implements ReportGenerator {

    abstract public FileObject generateReport(WizardDescriptor descriptor);
    
    /**
     * Find in band a JRDesignStaticText element having exp as text.
     * @param band
     * @param exp
     * @return the first matching element or null.
     */
    public static  JRDesignStaticText findStaticTextElement(JRElementGroup parent, String exp)
    {
        JRElement[] elements = parent.getElements();
        for (int i=0; i<elements.length; ++i)
        {
            JRElement ele = elements[i];
            if (ele instanceof JRDesignStaticText)
            {
                JRDesignStaticText st = (JRDesignStaticText)ele;
                if (st.getText() != null &&
                    st.getText().equalsIgnoreCase(exp))
                {
                    return st;
                }
            }
            else if (ele instanceof JRElementGroup)
            {
                JRDesignStaticText ele2 = findStaticTextElement((JRElementGroup)ele, exp);
                if (ele2 != null) return ele2;
            }
        }
        return null;
    }
    
    /**
     * Find in band a JRDesignTextField element having exp as expression value.
     * @param band
     * @param exp
     * @return the first matching element or null.
     */
    public static JRDesignTextField findTextFieldElement(JRElementGroup band, String exp)
    {
        JRElement[] elements = band.getElements();
        for (int i=0; i<elements.length; ++i)
        {
            JRElement ele = elements[i];
            if (ele instanceof JRDesignTextField)
            {
                String s = Misc.getExpressionText(((JRDesignTextField)ele).getExpression());
                if (s.startsWith("\""))
                {
                    s = s.substring(1);
                }
                if (s.endsWith("\""))
                {
                    s = s.substring(0, s.length()-1);
                }
                if (s.equalsIgnoreCase(exp)) return (JRDesignTextField) ele;
            }
            else if (ele instanceof JRElementGroup)
            {
                JRDesignTextField ele2 = findTextFieldElement((JRElementGroup)ele, exp);
                if (ele2 != null) return ele2;
            }
        }
        return null;
    }
    

}
