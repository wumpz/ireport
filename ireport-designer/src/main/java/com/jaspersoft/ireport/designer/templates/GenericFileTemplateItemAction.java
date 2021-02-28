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

import javax.swing.ImageIcon;

/**
 *
 * @author gtoffoli
 */
public class GenericFileTemplateItemAction extends TemplateItemAction {

    private String targetName = null;
    private String templateName = null;


    public GenericFileTemplateItemAction(String displayName, String description, String targetName, String templateName, ImageIcon icon)
    {
        setDisplayName(displayName);
        setDescription(description);
        this.targetName = targetName;
        this.templateName = templateName;
        setIcon(icon);
        putProperty(PROP_SHOW_TEMPLATES, Boolean.FALSE);
        putProperty(PROP_SHOW_FINISH_BUTTON, Boolean.TRUE);
        putProperty(PROP_SHOW_LAUNCH_REPORT_WIZARD_BUTTON, Boolean.FALSE);
        putProperty(PROP_SHOW_OPEN_TEMPLATE_BUTTON, Boolean.FALSE);
    }

    @Override
    public void performAction(TemplatesFrame frame, int buttonId) {

        if (buttonId == BUTTON_FINISH)
        {
            frame.runTemplateWizard(getTargetName(), getTemplateName());
        }


    }

    /**
     * @return the targetName
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * @param targetName the targetName to set
     */
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    /**
     * @return the templateName
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @param templateName the templateName to set
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

}
