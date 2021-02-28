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
package com.jaspersoft.ireport.designer.options.export;

import javax.swing.JPanel;
import com.jaspersoft.ireport.designer.options.OptionsPanel;
import com.jaspersoft.ireport.designer.options.IReportOptionsPanelController;

/**
 *
 * @author gtoffoli
 */
public abstract class AbstractExportParametersPanel extends JPanel implements OptionsPanel {

    private IReportOptionsPanelController controller = null;

    private boolean init = false;

    public boolean setInit(boolean b)
    {
        boolean old = init;
        init =b;
        return old;
    }

    public boolean isInit()
    {
        return init;
    }

    public AbstractExportParametersPanel()
    {
    }

    /**
     * Notify a change in the UI.
     */
    public void notifyChange()
    {
        if (this.getController() != null && !isInit())
        {
            getController().changed();
        }
    }

    /**
     * @return the controller
     */
    public IReportOptionsPanelController getController() {
        return controller;
    }
    
    /**
     * The contorller is always set by iReport before to use the panel...
     * @return the controller
     */
    public void setController(IReportOptionsPanelController ctrl) {
        this.controller = ctrl;
    }




    /**
     * return the name that should appear in the exporters list
     * @return
     */
    abstract public String getDisplayName();
}
