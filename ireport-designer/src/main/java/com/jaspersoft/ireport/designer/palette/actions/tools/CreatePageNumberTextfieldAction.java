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
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class CreatePageNumberTextfieldAction extends CreateTextFieldAction {

    @Override
    public JRDesignElement createReportElement(JasperDesign jd)
    {
        JRDesignTextField element = (JRDesignTextField)super.createReportElement( jd );

        ((JRDesignExpression)element.getExpression()).setText("$V{PAGE_NUMBER}");
        ((JRDesignExpression)element.getExpression()).setValueClassName("java.lang.Integer");

        setMatchingClassExpression(
            ((JRDesignExpression)element.getExpression()),
            "java.lang.Integer",
            true
            );

        return element;
    }

    /*
    public static PaletteItem createPaletteItem()
    {
        Properties props = new Properties();
        props.setProperty(PaletteItem.ACTION ,CreatePageNumberTextfieldAction.class.getName());
        props.setProperty(PaletteItem.PROP_ID , "PageNumber");
        props.setProperty(PaletteItem.PROP_NAME , "Page #");
        props.setProperty(PaletteItem.PROP_COMMENT , "Creates a textfield to display the page number");
        props.setProperty(PaletteItem.PROP_ICON16,"com/jaspersoft/ireport/designer/resources/textfield-16.png");
        props.setProperty(PaletteItem.PROP_ICON32,"com/jaspersoft/ireport/designer/resources/textfield-32.png");

        try {
            FileObject selectedPaletteFolder = Misc.createFolders("ireport/palette/tools");

            // We create the card if it does not exist:

            FileObject toolFile = selectedPaletteFolder.getFileObject(
                "PageNumber","irpitem");
            if (toolFile==null) {
                toolFile = selectedPaletteFolder.createData(
                     "PageNumber","irpitem");
            }
            FileLock lock = toolFile.lock();
            OutputStream out = toolFile.getOutputStream(lock);
    // Write the icon that the user selected
    // to the card file:
            props.store(out, "Tool PageNumber");
            out.close();
            lock.releaseLock();
        } catch (IOException ex) {
                ex.printStackTrace();
        }

        return null;
    }
     */
}
