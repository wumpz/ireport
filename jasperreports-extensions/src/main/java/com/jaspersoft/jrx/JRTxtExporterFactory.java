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
package com.jaspersoft.jrx;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.export.ExporterFactory;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.engine.JRExporter;
import com.jaspersoft.jrx.export.JRTxtExporter;
import com.jaspersoft.jrx.export.JRTxtExporterParameter;
import java.util.prefs.Preferences;
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 *
 * @author gtoffoli
 */
public class JRTxtExporterFactory implements ExporterFactory {

    public String getExportFormat() {
        return "irtxt";
    }

    public String getExportFormatDisplayName() {
        return I18n.getString("format.irtxt");
    }

    public String getExporterFileExtension() {
        return "txt";
    }

    public String getViewer() {
        return Misc.nvl( IReportManager.getInstance().getProperty("ExternalTXTViewer"), "");
    }

    public JRExporter createExporter() {
        return createExporter(null);
    }
    public JRExporter createExporter(JasperReportsContext context) {
        JRTxtExporter exporter = new JRTxtExporter();

        // configuring the exporter...
        Preferences pref = IReportManager.getPreferences();


        int pageHeight = pref.getInt( "irtext.pageHeight", 0);
        if (pageHeight > 0)
        {
            exporter.setParameter( JRTxtExporterParameter.PAGE_ROWS, "" + pageHeight);
        }

        int pageWidth = pref.getInt( "irtext.pageWidth", 0);
        if (pageWidth > 0)
        {
            exporter.setParameter( JRTxtExporterParameter.PAGE_COLUMNS, "" + pageWidth);
        }

        boolean addFormFeed = pref.getBoolean("irtext.addFormFeed", true);
        exporter.setParameter( JRTxtExporterParameter.ADD_FORM_FEED, "" + addFormFeed);

        String bidi = pref.get("irtext.bidi", "");
        if (bidi.length() > 0)
        {
           exporter.setParameter( JRTxtExporterParameter.BIDI_PREFIX, bidi);

        }

        String displaywidthProviderFactory = pref.get("irtext.displaywidthProviderFactory", "");
        if (displaywidthProviderFactory.length() > 0)
        {
           exporter.setParameter( JRTxtExporterParameter.DISPLAY_WIDTH_PROVIDER_FACTORY, displaywidthProviderFactory);
        }

        return exporter;

    }

}
