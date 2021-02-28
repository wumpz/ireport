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

package com.jaspersoft.ireport.designer;


import java.io.File;
import net.sf.jasperreports.engine.design.JRDesignSubreport;

/**
 *
 * @version $Id: SubreportOpenerProvider.java 0 2009-10-16 17:30:56 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public interface SubreportOpenerProvider {

    /**
     * This method is called by the OpenSubreportAction when the subreport is opened.
     * If subreportFile is null, this means that iReport or previous providers were not
     * able to find the file to open.
     * If a provider finds a file, it should return it. A provider can even return a different
     * file or just null (in this case nothing changes for iReport, meaning that the file
     * previously found is not replaced with null).
     * 
     * When all the providers have been queried, iReport will take care to open the file
     * if not null.
     *
     * SubreportOpenerProviders must be installed inside ireport/SubreportOpenerProviders in the jrxml
     * as class instance.
     *
     * 
     * @param ed
     * @param subreportElement
     * @param subreportFile
     * @return File the file to open, or null.
     */
    public File openingSubreport(JrxmlEditorSupport ed, JRDesignSubreport subreportElement, File subreportFile);

}
