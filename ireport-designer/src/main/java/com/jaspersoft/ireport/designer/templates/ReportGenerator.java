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

import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;

/**
 * Interface to plug new ReportGenerator types. IReport has a default
 * report generator that works with his own templates.
 *  
 * @author gtoffoli
 */
public interface ReportGenerator {

    /**
     * You can use properties coming from the WizardDescriptor.
     * The WizardDescriptor should normally be a TemplateWizard,
     * so you can assume to use getTargetDirectory and getTargetName.
     * You need to return the generated jrxml file.
     * 
     * @param descriptor
     * @return
     */
    public FileObject generateReport(WizardDescriptor descriptor);
}
