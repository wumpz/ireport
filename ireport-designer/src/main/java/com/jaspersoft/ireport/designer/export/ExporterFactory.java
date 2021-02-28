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
package com.jaspersoft.ireport.designer.export;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 *
 * @author gtoffoli
 */
public interface ExporterFactory {

    /**
     * Return a code that will be set as preview type
     * in order to use this exporter (i.e. myTxtFormat)
     * @return
     */
    public String getExportFormat();

    /**
     * Return the name that should be appear in the Preview
     * menu when specifying this format. I.e. (John Smith's custom text)
     * @return
     */
    public String getExportFormatDisplayName();

    /**
     * The extension of the file that will be used to replace the .jasper
     * extension in the original file (i.e. txt)
     * @return
     */
    public String getExporterFileExtension();
    
    /**
     * Return the name of an application to be executed to view this file
     * The command should be executed with the file name as first parameter.
     * If the return is null or an empty string, the internal preview will be used.
     * @return
     */
    public String getViewer();

    /**
     * 
     *
     * @return
     * @deprecated Use createExporter(JasperReportsContext context);
     */
    public JRExporter createExporter();
    
    
    /**
     * 
     * This function creates and configures the exporter. Extra parameters
     * can be set or replaced by the IReportCompiler class. In particular this
     * class will set these parameters:
     * JRExporterParameter.OUTPUT_FILE_NAME
     * JRExporterParameter.JASPER_PRINT
     * JRExporterParameter.PROGRESS_MONITOR
     * JRExporterParameter.IGNORE_PAGE_MARGINS
     * JRExporterParameter.PAGE_INDEX
     * JRExporterParameter.START_PAGE_INDEX
     * JRExporterParameter.END_PAGE_INDEX
     * JRExporterParameter.PROPERTY_CHARACTER_ENCODING
     * JRExporterParameter.CHARACTER_ENCODING
     * JRExporterParameter.OFFSET_X
     * JRExporterParameter.OFFSET_Y
     *
     * if propertly requested by the user with the general export options panel.
     * 
     * @param context
     * @return 
     */
    public JRExporter createExporter(JasperReportsContext context);

}
