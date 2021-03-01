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

package com.jaspersoft.ireport.jasperserver.ui.actions;

import com.jaspersoft.ireport.jasperserver.ui.ReportUnitRunDialog;
import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.BasicInputControl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @version $Id: ICActionListener.java 0 2009-11-04 17:13:06 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class ICActionListener implements ActionListener {

    ReportUnitRunDialog dialog = null;
    BasicInputControl ic = null;

    public ICActionListener(BasicInputControl ic, ReportUnitRunDialog dialog)
    {
        this.ic = ic;
        this.dialog = dialog;
    }

    public void actionPerformed(ActionEvent e) {
        if (dialog != null)
        {
            dialog.actionPerformed(ic);
        }
    }


}
