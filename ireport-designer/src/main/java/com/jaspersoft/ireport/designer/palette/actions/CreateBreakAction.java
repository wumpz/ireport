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

import com.jaspersoft.ireport.designer.crosstab.CrosstabObjectScene;
import com.jaspersoft.ireport.designer.utils.Misc;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.design.JRDesignBreak;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.Mutex;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class CreateBreakAction extends CreateReportElementAction 
{

    public JRDesignElement createReportElement(JasperDesign jd)
    {
        if (getScene() instanceof CrosstabObjectScene)
        {
            Runnable r = new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(Misc.getMainFrame(), "You can not use a break element inside a crosstab","Error", JOptionPane.WARNING_MESSAGE);
                }
            };
            
            Mutex.EVENT.readAccess(r); 
            return null;
        }
        
        JRDesignBreak element = new JRDesignBreak();

        BreakTypeDialog dialog = new BreakTypeDialog(Misc.getMainFrame(), true);
        dialog.setVisible(true);

        element.setWidth(100);
        element.setType(  dialog.getBreakTypeValue()  );
        return element;
    }
    
}
