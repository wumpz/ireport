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
package com.jaspersoft.ireport.designer.subreport;

import javax.swing.JComponent;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import org.openide.loaders.TemplateWizard;

/**
 *
 * @author gtoffoli
 */
public class SubreportTemplateWizard extends TemplateWizard {

    SubreportWizardIterator iterator = null;
    
    public SubreportTemplateWizard()
    {
        super();
        iterator = new SubreportWizardIterator();
        iterator.initialize(this);
        setPanelsAndSettings(iterator, this);
    }
    
    @Override
    protected void updateState() {
        super.updateState();
        
        // Set the setings...
        // compoun steps pane info
        putProperty("WizardPanel_contentSelectedIndex", ((JComponent)iterator.current().getComponent()).getClientProperty("WizardPanel_contentSelectedIndex")   );
        putProperty("WizardPanel_contentData", ((JComponent)iterator.current().getComponent()).getClientProperty("WizardPanel_contentData")  );
    }
    
    public JRDesignSubreport getElement()
    {
        return iterator.getElement();
    }
    
    
    
}
