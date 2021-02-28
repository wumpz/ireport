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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;

/**
 *
 * @author gtoffoli
 */
abstract public class TemplateItemAction {

    public static final String PROP_SHOW_TEMPLATES = "showTemplates";
    public static final String PROP_SHOW_FINISH_BUTTON = "showFinishButton";
    public static final String PROP_SHOW_OPEN_TEMPLATE_BUTTON = "showOpenTemplateButton";
    public static final String PROP_SHOW_LAUNCH_REPORT_WIZARD_BUTTON = "showLaunchReportWizard";

    public static final int BUTTON_FINISH = 1;
    public static final int BUTTON_LAUNCH_REPORT_WIZARD = 2;
    public static final int BUTTON_OPEN_TEMPLATE = 3;

    private Map<String,Object> properties = new HashMap<String,Object>();

    public Object getProperty(String key)
    {
        return properties.get(key);
    }

    public void putProperty(String key, Object value)
    {
        if (value == null)  properties.remove(key);
        else properties.put(key, value);
    }
    
    private static List<TemplateItemAction> actions = new ArrayList<TemplateItemAction>();

    /**
     * Add an action to the templates window
     * @param action
     * @return
     */
    public static boolean addAction(TemplateItemAction action)
    {
        if (!actions.contains(action))
        {
            actions.add(action);
            return true;
        }
        return false;
    }

    public static boolean addAction(TemplateItemAction action, int index)
    {
        if (!actions.contains(action))
        {
            actions.add(index, action);
            return true;
        }
        return false;
    }

    public static boolean removeAction(TemplateItemAction action)
    {
        if (actions.contains(action))
        {
            actions.remove(action);
            return true;
        }
        return false;
    }

    public static List<TemplateItemAction> getActions()
    {
        return actions;
    }
    private String displayName = "";
    private ImageIcon icon = null;
    private String description = null;
    

    /**
     * Decide what to do when the user select to run this report
     * @param frame
     */
    abstract public void performAction(TemplatesFrame frame, int buttonId);


    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the icon
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
