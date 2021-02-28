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
package com.jaspersoft.ireport.designer.menu.preview;

import com.jaspersoft.ireport.designer.IReportManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;

/**
 *
 * @author gtoffoli
 */
public abstract class AbstractPreviewAction extends CallableSystemAction implements PreferenceChangeListener, ItemListener {

    public abstract String getPreviewType();

    private JRadioButtonMenuItem item;
    private boolean updating = false;
    
    public AbstractPreviewAction()
    {
        item = new JRadioButtonMenuItem(getName());
        IReportManager.getPreferences().addPreferenceChangeListener(this);
        preferenceChange(null);
        item.addItemListener(this);
    }
    
    public void performAction()
    {

    }
            

    @Override
    public JMenuItem getMenuPresenter() {
        return item;
    }
     
    @Override
    protected void initialize() {
        super.initialize();
        
        // see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
        
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    public void preferenceChange(PreferenceChangeEvent evt)
    {
        String fmt = IReportManager.getPreferences().get("output_format", "");
        if (getPreviewType().equals(fmt) != item.isSelected())
        {
            setUpdating(true);
            item.setSelected(!item.isSelected());
            setUpdating(false);
        }
    }
                      
    
    public void itemStateChanged(ItemEvent e)
    {
        if (isUpdating()) return;
        
        if (e.getStateChange() == ItemEvent.DESELECTED)
        {
            IReportManager.getPreferences().remove("output_format");
        }
        else
        {
            if (getPreviewType().length() > 0)
            {
                IReportManager.getPreferences().put("output_format", getPreviewType());
            }
            else
            {
                IReportManager.getPreferences().remove("output_format");
            }
        }
        performAction();
    }

    /**
     * @return the updating
     */
    public boolean isUpdating() {
        return updating;
    }

    /**
     * @param updating the updating to set
     */
    public void setUpdating(boolean updating) {
        this.updating = updating;
    }

}
