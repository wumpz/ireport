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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.util.prefs.Preferences;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.SimpleJasperReportsContext;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.PdfReportConfiguration;
import net.sf.jasperreports.export.TextReportConfiguration;
import net.sf.jasperreports.export.XlsExporterConfiguration;
import net.sf.jasperreports.export.XlsReportConfiguration;

/**
 * This is a generic exporter used internally by iReport for the default
 * export formats.
 * @author gtoffoli
 */
public class DefaultExporterFactory implements ExporterFactory {

    String format = "";
    public DefaultExporterFactory(String format)
    {
        this.format = format;
    }

    public String getExportFormat() {
        return format;
    }

    public String getExporterFileExtension() {
        if (format.equals("xhtml") || format.equals("layered_html")) return "html";
        if (format.equals("xml")) return "jrpxml";
        if (format.equals("xls")) return "xls";
        if (format.equals("xls2")) return "xls";
        if (format.equals("xls3")) return "xls";
        return format;
    }

    /**
     * 
     * @return
     * @deprecated
     */
    public JRExporter createExporter() {
        return createExporter(null);
    }
    
    
    public JRExporter createExporter(JasperReportsContext ctx) {

       SimpleJasperReportsContext context = null;
       
       if (ctx instanceof SimpleJasperReportsContext)
       {
           context = (SimpleJasperReportsContext)ctx;
       }
        
       JRExporter exporter = null;

       if (format.equalsIgnoreCase("pdf"))
       {
          exporter = new  net.sf.jasperreports.engine.export.JRPdfExporter(context);
          configurePdfExporter(exporter, context);
       }
       else if (format.equalsIgnoreCase("csv"))
       {
          exporter = new  net.sf.jasperreports.engine.export.JRCsvExporter(context);
          configureCsvExporter(exporter, context);
       }
       else if (format.equalsIgnoreCase("html"))
       {
          exporter = new  net.sf.jasperreports.engine.export.HtmlExporter(context);
          //exporter = new net.sf.jasperreports.engine.export.HtmlExporter(context);
          configureHtmlExporter(exporter, context);
       }
       else if (format.equalsIgnoreCase("layered_html"))
       {
          //exporter = new  net.sf.jasperreports.engine.export.JRHtmlExporter(context);
          exporter = new net.sf.jasperreports.engine.export.HtmlExporter(context);
          configureHtmlExporter(exporter, context);
       }
       else if (format.equalsIgnoreCase("xhtml"))
       {
          exporter = new  net.sf.jasperreports.engine.export.HtmlExporter(context);
          configureXHtmlExporter(exporter, context);
       }
       else if (format.equalsIgnoreCase("xls"))
       {
          exporter = new  net.sf.jasperreports.engine.export.JRXlsExporter(context);
          configureXlsExporter(exporter, context);
       }
       else if (format.equalsIgnoreCase("xls2"))
       {
          exporter = new  net.sf.jasperreports.engine.export.JRXlsExporter(context);
          configureXlsExporter(exporter, context);
       }
       else if (format.equalsIgnoreCase("xls3"))
       {
          exporter = new  net.sf.jasperreports.engine.export.JRXlsMetadataExporter(context);
          configureXlsExporter(exporter, context);
       }
       else if (format.equalsIgnoreCase("xlsx"))
       {
          exporter = new  net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter(context);
          configureXlsExporter(exporter, context);
       }
       else if (format.equalsIgnoreCase("java2D"))
       {
          // no exporter will be returned.
       }
       else if (format.equalsIgnoreCase("txt"))
       {
          exporter = new  net.sf.jasperreports.engine.export.JRTextExporter(context);
          configureTextExporter(exporter, context);
       }
       else if (format.equalsIgnoreCase("rtf"))
       {
          exporter = new  net.sf.jasperreports.engine.export.JRRtfExporter(context);
       }
       else if (format.equalsIgnoreCase("odt"))
       {
          exporter = new  net.sf.jasperreports.engine.export.oasis.JROdtExporter(context);
       }
       else if (format.equalsIgnoreCase("ods"))
       {
          exporter = new  net.sf.jasperreports.engine.export.oasis.JROdsExporter(context);
       }
       else if (format.equalsIgnoreCase("docx"))
       {
          exporter = new  net.sf.jasperreports.engine.export.ooxml.JRDocxExporter(context);
       }
       else if (format.equalsIgnoreCase("xml"))
       {
          exporter = new  net.sf.jasperreports.engine.export.JRXmlExporter(context);
          configureXmlExporter(exporter, context);
       }
       else if (format.equalsIgnoreCase("pptx"))
       {
          exporter = new  net.sf.jasperreports.engine.export.ooxml.JRPptxExporter(context);
          //configureXmlExporter(exporter);
       }
       return exporter;
    }

    public String getExportFormatDisplayName() {
       return I18n.getString("export.format.name." + format);
    }

    public String getViewer() {

       if (format.equalsIgnoreCase("pdf"))
       {
          return IReportManager.getInstance().getProperty("ExternalPDFViewer");
       }
       else if (format.equalsIgnoreCase("csv"))
       {
          return Misc.nvl( IReportManager.getInstance().getProperty("ExternalCSVViewer"), "");
       }
       else if (format.equalsIgnoreCase("html") || format.equalsIgnoreCase("xhtml") || format.equals("layered_html"))
       {
          return Misc.nvl( IReportManager.getInstance().getProperty("ExternalHTMLViewer"), "");
       }
       else if (format.equalsIgnoreCase("xls") ||
                format.equalsIgnoreCase("xls2") ||
                format.equalsIgnoreCase("xls3") ||
                format.equalsIgnoreCase("xlsx"))
       {
            return Misc.nvl( IReportManager.getInstance().getProperty("ExternalXLSViewer"), "");
       }
       else if (format.equalsIgnoreCase("java2D"))
       {
          return null;
       }
       else if (format.equalsIgnoreCase("txt") || format.equalsIgnoreCase("xml"))
       {
          return Misc.nvl( IReportManager.getInstance().getProperty("ExternalTXTViewer"), "");
       }
       else if (format.equalsIgnoreCase("rtf"))
       {
          return Misc.nvl( IReportManager.getInstance().getProperty("ExternalRTFViewer"), "");
       }
       else if (format.equalsIgnoreCase("odt"))
       {
          return Misc.nvl( IReportManager.getInstance().getProperty("ExternalODFViewer"), "");
       }
       else if (format.equalsIgnoreCase("ods"))
       {
          return Misc.nvl( IReportManager.getInstance().getProperty("ExternalODSViewer"), "");
       }
       else if (format.equalsIgnoreCase("docx"))
       {
          return Misc.nvl( IReportManager.getInstance().getProperty("ExternalDOCXViewer"), "");
       }
       else if (format.equalsIgnoreCase("pptx"))
       {
          return Misc.nvl( IReportManager.getInstance().getProperty("ExternalPPTXViewer"), "");
       }

       return null;
    }

    private void configureTextExporter(JRExporter exporter, SimpleJasperReportsContext context) {

        Preferences pref = IReportManager.getPreferences();
        JRPropertiesUtil jrPropUtils = JRPropertiesUtil.getInstance(context);

        float floatVal = pref.getFloat(TextReportConfiguration.PROPERTY_CHARACTER_HEIGHT, 0);
        if (floatVal > 0) context.setValue( TextReportConfiguration.PROPERTY_CHARACTER_HEIGHT, floatVal+"");

        floatVal = pref.getFloat(TextReportConfiguration.PROPERTY_CHARACTER_WIDTH, 0);
        if (floatVal > 0) context.setValue( TextReportConfiguration.PROPERTY_CHARACTER_WIDTH, floatVal+"");

        int val = pref.getInt(TextReportConfiguration.PROPERTY_PAGE_HEIGHT, 0);
        if (val > 0) context.setValue( TextReportConfiguration.PROPERTY_PAGE_HEIGHT, ""+val);

        val = pref.getInt(TextReportConfiguration.PROPERTY_PAGE_WIDTH, 0);
        if (val > 0) context.setValue( TextReportConfiguration.PROPERTY_PAGE_WIDTH, ""+val);

        String s = null;
        if (pref.getBoolean(JRPropertiesUtil.PROPERTY_PREFIX + "export.txt.nothingBetweenPages", false))
        {
            exporter.setParameter( JRTextExporterParameter.BETWEEN_PAGES_TEXT, "");
        }
        else
        {
            s = pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.txt.betweenPagesText", "");
            if (s.length() > 0) exporter.setParameter( JRTextExporterParameter.BETWEEN_PAGES_TEXT, s);
        }

        s = pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.txt.lineSeparator", "");
        if (s.length() > 0) exporter.setParameter( JRTextExporterParameter.LINE_SEPARATOR, s);

    }

    private void configureXlsExporter(JRExporter exporter, SimpleJasperReportsContext context) {

        Preferences pref = IReportManager.getPreferences();
        JRPropertiesUtil jrPropUtils = JRPropertiesUtil.getInstance(context);

        context.setValue( XlsExporterConfiguration.PROPERTY_CREATE_CUSTOM_PALETTE , pref.getBoolean(XlsExporterConfiguration.PROPERTY_CREATE_CUSTOM_PALETTE, jrPropUtils.getBooleanProperty(XlsExporterConfiguration.PROPERTY_CREATE_CUSTOM_PALETTE)));

        String password = pref.get(XlsReportConfiguration.PROPERTY_PASSWORD, jrPropUtils.getProperty(XlsReportConfiguration.PROPERTY_PASSWORD));
        if (password != null && password.length() > 0)
        {
            context.setValue( XlsReportConfiguration.PROPERTY_PASSWORD ,password);
        }

        context.setValue( XlsReportConfiguration.PROPERTY_COLLAPSE_ROW_SPAN , pref.getBoolean(XlsReportConfiguration.PROPERTY_COLLAPSE_ROW_SPAN, jrPropUtils.getBooleanProperty(XlsReportConfiguration.PROPERTY_COLLAPSE_ROW_SPAN)));
        
        System.out.println("Setting detect cell type to:" + pref.getBoolean(XlsReportConfiguration.PROPERTY_DETECT_CELL_TYPE, jrPropUtils.getBooleanProperty(XlsReportConfiguration.PROPERTY_DETECT_CELL_TYPE)));
        
        context.setValue( XlsReportConfiguration.PROPERTY_DETECT_CELL_TYPE , pref.getBoolean(XlsReportConfiguration.PROPERTY_DETECT_CELL_TYPE, jrPropUtils.getBooleanProperty(XlsReportConfiguration.PROPERTY_DETECT_CELL_TYPE)));
        
        System.out.println("Setting detect cell type to ("+XlsReportConfiguration.PROPERTY_DETECT_CELL_TYPE+"):" + jrPropUtils.getBooleanProperty(XlsReportConfiguration.PROPERTY_DETECT_CELL_TYPE));
        
        context.setValue( XlsReportConfiguration.PROPERTY_FONT_SIZE_FIX_ENABLED , pref.getBoolean(XlsReportConfiguration.PROPERTY_FONT_SIZE_FIX_ENABLED, jrPropUtils.getBooleanProperty(XlsReportConfiguration.PROPERTY_FONT_SIZE_FIX_ENABLED)));
        context.setValue( XlsReportConfiguration.PROPERTY_IGNORE_CELL_BORDER , pref.getBoolean(XlsReportConfiguration.PROPERTY_IGNORE_CELL_BORDER, jrPropUtils.getBooleanProperty(XlsReportConfiguration.PROPERTY_IGNORE_CELL_BORDER)));
        context.setValue( XlsReportConfiguration.PROPERTY_IGNORE_CELL_BACKGROUND , pref.getBoolean(XlsReportConfiguration.PROPERTY_IGNORE_CELL_BACKGROUND, jrPropUtils.getBooleanProperty(XlsReportConfiguration.PROPERTY_IGNORE_CELL_BACKGROUND)));
        context.setValue( XlsReportConfiguration.PROPERTY_IGNORE_GRAPHICS , pref.getBoolean(XlsReportConfiguration.PROPERTY_IGNORE_GRAPHICS, jrPropUtils.getBooleanProperty(XlsReportConfiguration.PROPERTY_IGNORE_GRAPHICS)));
        context.setValue( XlsReportConfiguration.PROPERTY_IMAGE_BORDER_FIX_ENABLED , pref.getBoolean(XlsReportConfiguration.PROPERTY_IMAGE_BORDER_FIX_ENABLED, jrPropUtils.getBooleanProperty(XlsReportConfiguration.PROPERTY_IMAGE_BORDER_FIX_ENABLED)));
        context.setValue( XlsReportConfiguration.PROPERTY_ONE_PAGE_PER_SHEET , pref.getBoolean(XlsReportConfiguration.PROPERTY_ONE_PAGE_PER_SHEET, jrPropUtils.getBooleanProperty(XlsReportConfiguration.PROPERTY_ONE_PAGE_PER_SHEET)));
        context.setValue( XlsReportConfiguration.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS , pref.getBoolean(XlsReportConfiguration.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, jrPropUtils.getBooleanProperty(XlsReportConfiguration.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS)));
        context.setValue( XlsReportConfiguration.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS , pref.getBoolean(XlsReportConfiguration.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, jrPropUtils.getBooleanProperty(XlsReportConfiguration.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS)));
        context.setValue( XlsReportConfiguration.PROPERTY_WHITE_PAGE_BACKGROUND , pref.getBoolean(XlsReportConfiguration.PROPERTY_WHITE_PAGE_BACKGROUND, jrPropUtils.getBooleanProperty(XlsReportConfiguration.PROPERTY_WHITE_PAGE_BACKGROUND)));

        int maxRowsPerSheet = pref.getInt(XlsReportConfiguration.PROPERTY_MAXIMUM_ROWS_PER_SHEET, jrPropUtils.getIntegerProperty(XlsReportConfiguration.PROPERTY_MAXIMUM_ROWS_PER_SHEET));
        if (maxRowsPerSheet > 0)
        {
            context.setValue( XlsReportConfiguration.PROPERTY_MAXIMUM_ROWS_PER_SHEET, ""+ maxRowsPerSheet);
        }
        if (pref.getBoolean(JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.useSheetNames", false))
        {
            String sheetNames = pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.sheetNames", "");
            exporter.setParameter( JRXlsAbstractExporterParameter.SHEET_NAMES,  sheetNames.split("\n"));
        }

        // Add freeze options...
        String  col = pref.get(XlsReportConfiguration.PROPERTY_FREEZE_COLUMN, jrPropUtils.getProperty(XlsReportConfiguration.PROPERTY_FREEZE_COLUMN));
        if (col != null && col.length() > 0)
        {
            context.setValue( XlsReportConfiguration.PROPERTY_FREEZE_COLUMN, ""+col);
        }

//        String columnEdge = pref.get(JRXlsExporter.PROPERTY_FREEZE_COLUMN_EDGE, null);
//        if (columnEdge != null && columnEdge.length() > 0)
//        {
//            context.setValue( JRXlsExporter.PROPERTY_FREEZE_COLUMN_EDGE, columnEdge);
//        }

        // Add freeze options...
        int row = pref.getInt(XlsReportConfiguration.PROPERTY_FREEZE_ROW, jrPropUtils.getIntegerProperty(XlsReportConfiguration.PROPERTY_FREEZE_ROW,0));
        if (row > 0)
        {
            context.setValue( XlsReportConfiguration.PROPERTY_FREEZE_ROW, ""+row);
        }

//        String rowEdge = pref.get( JRXlsExporter.PROPERTY_FREEZE_ROW_EDGE, null);
//        if (rowEdge != null && rowEdge.length() > 0)
//        {
//            context.setValue( JRXlsExporter.PROPERTY_FREEZE_ROW_EDGE, pref.get(JRXlsExporter.PROPERTY_FREEZE_ROW_EDGE,  rowEdge));
//        }

        


    }

    private void configurePdfExporter(JRExporter exporter, SimpleJasperReportsContext context) {

        Preferences pref = IReportManager.getPreferences();
        JRPropertiesUtil jrPropUtils = JRPropertiesUtil.getInstance(context);
        

        String pdfVersion = pref.get(PdfExporterConfiguration.PROPERTY_PDF_VERSION, null);
        if (pdfVersion != null && pdfVersion.length()==1) context.setValue( PdfExporterConfiguration.PROPERTY_PDF_VERSION  , ""+pdfVersion.charAt(0));

        boolean b = pref.getBoolean(PdfExporterConfiguration.PROPERTY_CREATE_BATCH_MODE_BOOKMARKS, jrPropUtils.getBooleanProperty(PdfExporterConfiguration.PROPERTY_CREATE_BATCH_MODE_BOOKMARKS));
        context.setValue( PdfExporterConfiguration.PROPERTY_CREATE_BATCH_MODE_BOOKMARKS , new Boolean(b));

        context.setValue( PdfExporterConfiguration.PROPERTY_COMPRESSED , new Boolean(pref.getBoolean(PdfExporterConfiguration.PROPERTY_COMPRESSED, jrPropUtils.getBooleanProperty(PdfExporterConfiguration.PROPERTY_COMPRESSED))));
        context.setValue( PdfReportConfiguration.PROPERTY_FORCE_LINEBREAK_POLICY , new Boolean(pref.getBoolean(PdfReportConfiguration.PROPERTY_FORCE_LINEBREAK_POLICY, jrPropUtils.getBooleanProperty(PdfReportConfiguration.PROPERTY_FORCE_LINEBREAK_POLICY))));
        context.setValue( PdfReportConfiguration.PROPERTY_FORCE_SVG_SHAPES , new Boolean(pref.getBoolean(PdfReportConfiguration.PROPERTY_FORCE_SVG_SHAPES, jrPropUtils.getBooleanProperty(PdfReportConfiguration.PROPERTY_FORCE_SVG_SHAPES))));
        context.setValue( PdfExporterConfiguration.PROPERTY_TAGGED , new Boolean(pref.getBoolean(PdfExporterConfiguration.PROPERTY_TAGGED, jrPropUtils.getBooleanProperty(PdfExporterConfiguration.PROPERTY_TAGGED))));
        context.setValue( PdfExporterConfiguration.PROPERTY_ENCRYPTED , new Boolean(pref.getBoolean(PdfExporterConfiguration.PROPERTY_ENCRYPTED, jrPropUtils.getBooleanProperty(PdfExporterConfiguration.PROPERTY_ENCRYPTED))));
        context.setValue( PdfExporterConfiguration.PROPERTY_128_BIT_KEY , new Boolean(pref.getBoolean(PdfExporterConfiguration.PROPERTY_128_BIT_KEY, jrPropUtils.getBooleanProperty(PdfExporterConfiguration.PROPERTY_128_BIT_KEY))));

        if (pref.get("export.pdf.METADATA_AUTHOR", "").length() > 0)
        {
            exporter.setParameter( PdfExporterConfiguration.PROPERTY_METADATA_AUTHOR , pref.get("export.pdf.METADATA_AUTHOR", ""));
        }
        if (pref.get("export.pdf.METADATA_CREATOR", "").length() > 0)
        {
            exporter.setParameter( PdfExporterConfiguration.METADATA_CREATOR , pref.get("export.pdf.METADATA_CREATOR", ""));
        }
        if (pref.get("export.pdf.METADATA_KEYWORDS", "").length() > 0)
        {
            exporter.setParameter( PdfExporterConfiguration.METADATA_KEYWORDS , pref.get("export.pdf.METADATA_KEYWORDS", ""));
        }
        if (pref.get("export.pdf.METADATA_SUBJECT", "").length() > 0)
        {
            exporter.setParameter( PdfExporterConfiguration.METADATA_SUBJECT , pref.get("export.pdf.METADATA_SUBJECT", ""));
        }
        if (pref.get("export.pdf.METADATA_TITLE", "").length() > 0)
        {
            exporter.setParameter( PdfExporterConfiguration.METADATA_TITLE , pref.get("export.pdf.METADATA_TITLE", ""));
        }
        if (pref.get("export.pdf.OWNER_PASSWORD", "").length() > 0)
        {
            context.setValue( PdfExporterConfiguration.PROPERTY_OWNER_PASSWORD , pref.get("export.pdf.OWNER_PASSWORD", ""));
        }
        if (pref.get("export.pdf.USER_PASSWORD", "").length() > 0)
        {
            context.setValue( PdfExporterConfiguration.PROPERTY_USER_PASSWORD , pref.get("export.pdf.USER_PASSWORD", ""));
        }
        if (pref.get("export.pdf.TAG_LANGUAGE", jrPropUtils.getProperty(PdfExporterConfiguration.PROPERTY_TAG_LANGUAGE)) != null)
        {
            context.setValue( PdfExporterConfiguration.PROPERTY_TAG_LANGUAGE ,pref.get("export.pdf.TAG_LANGUAGE", jrPropUtils.getProperty(PdfExporterConfiguration.PROPERTY_TAG_LANGUAGE)));
        }
        if (pref.get("export.pdf.PDF_JAVASCRIPT", jrPropUtils.getProperty(PdfExporterConfiguration.PROPERTY_PDF_JAVASCRIPT)) != null)
        {
            context.setValue( PdfExporterConfiguration.PROPERTY_PDF_JAVASCRIPT ,pref.get("export.pdf.PDF_JAVASCRIPT", jrPropUtils.getProperty(PdfExporterConfiguration.PROPERTY_PDF_JAVASCRIPT)));
        }
        if (pref.getInt("export.pdf.PERMISSIONS",0) != 0)
        {
            exporter.setParameter(PdfExporterConfiguration.PERMISSIONS ,pref.getInt("export.pdf.PERMISSIONS",0));
        }


        String pdfa = pref.get(PdfExporterConfiguration.PROPERTY_PDFA_CONFORMANCE,jrPropUtils.getProperty(PdfExporterConfiguration.PDFA_CONFORMANCE_NONE));
        if (pdfa != null)
        {
            context.setValue( PdfExporterConfiguration.PROPERTY_PDFA_CONFORMANCE ,pdfa);
        }

        String pdfaICC = pref.get(PdfExporterConfiguration.PROPERTY_PDFA_ICC_PROFILE_PATH,jrPropUtils.getProperty(PdfExporterConfiguration.PROPERTY_PDFA_ICC_PROFILE_PATH));
        if (pdfaICC != null && !pdfaICC.equals(""))
        {
            context.setValue( PdfExporterConfiguration.PROPERTY_PDFA_ICC_PROFILE_PATH ,pdfaICC);
        }
    }


    private void configureXHtmlExporter(JRExporter exporter, SimpleJasperReportsContext context) {

        Preferences pref = IReportManager.getPreferences();
        JRPropertiesUtil jrPropUtils = JRPropertiesUtil.getInstance(context);

        exporter.setParameter( JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, pref.getBoolean(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.saveImages", true));
        exporter.setParameter( JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND, pref.getBoolean(JRHtmlExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND, jrPropUtils.getBooleanProperty(JRHtmlExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND)));
        exporter.setParameter( JRHtmlExporterParameter.IS_WRAP_BREAK_WORD, pref.getBoolean(JRHtmlExporterParameter.PROPERTY_WRAP_BREAK_WORD, jrPropUtils.getBooleanProperty(JRHtmlExporterParameter.PROPERTY_WRAP_BREAK_WORD)));

        if (pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.imagesDirectory","").length() > 0)
        {
            context.setValue( JRPropertiesUtil.PROPERTY_PREFIX + "export.html.imagesDirectory" , pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.imagesDirectory",""));
        }
        if (pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.imagesUri","").length() > 0)
        {
            context.setValue( JRPropertiesUtil.PROPERTY_PREFIX + "export.html.imagesUri" , pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.imagesUri",""));
        }
        if (pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlHeader","").length() > 0)
        {
            context.setValue( JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlHeader" , pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlHeader",""));
        }
        if (pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlBetweenPages","").length() > 0)
        {
            context.setValue( JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlBetweenPages" , pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlBetweenPages",""));
        }
        if (pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlFooter","").length() > 0)
        {
            context.setValue( JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlFooter" , pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlFooter",""));
        }
        if (pref.get(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT, jrPropUtils.getProperty(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT)).length() > 0)
        {
            context.setValue( JRHtmlExporterParameter.PROPERTY_SIZE_UNIT , pref.get(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT, jrPropUtils.getProperty(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT)));
        }

    }


    private void configureHtmlExporter(JRExporter exporter, SimpleJasperReportsContext context) {

        Preferences pref = IReportManager.getPreferences();
        JRPropertiesUtil jrPropUtils = JRPropertiesUtil.getInstance(context);

        context.setValue( JRHtmlExporterParameter.PROPERTY_FRAMES_AS_NESTED_TABLES, pref.getBoolean(JRHtmlExporterParameter.PROPERTY_FRAMES_AS_NESTED_TABLES, jrPropUtils.getBooleanProperty(JRHtmlExporterParameter.PROPERTY_FRAMES_AS_NESTED_TABLES)));
        context.setValue( JRHtmlExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, pref.getBoolean(JRHtmlExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, jrPropUtils.getBooleanProperty(JRHtmlExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS)));
        exporter.setParameter( JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, pref.getBoolean(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.saveImages", true));
        context.setValue( JRHtmlExporterParameter.PROPERTY_USING_IMAGES_TO_ALIGN, pref.getBoolean(JRHtmlExporterParameter.PROPERTY_USING_IMAGES_TO_ALIGN, jrPropUtils.getBooleanProperty(JRHtmlExporterParameter.PROPERTY_USING_IMAGES_TO_ALIGN)));
        context.setValue( JRHtmlExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND, pref.getBoolean(JRHtmlExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND, jrPropUtils.getBooleanProperty(JRHtmlExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND)));
        context.setValue( JRHtmlExporterParameter.PROPERTY_WRAP_BREAK_WORD, pref.getBoolean(JRHtmlExporterParameter.PROPERTY_WRAP_BREAK_WORD, jrPropUtils.getBooleanProperty(JRHtmlExporterParameter.PROPERTY_WRAP_BREAK_WORD)));

        //FIXME these properties do not actually exist!!!!!!!..... check all properties

        if (pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.imagesDirectory","").length() > 0)
        {
            context.setValue( JRPropertiesUtil.PROPERTY_PREFIX + "export.html.imagesDirectory" , pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.imagesDirectory",""));
        }
        if (pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.imagesUri","").length() > 0)
        {
            context.setValue( JRPropertiesUtil.PROPERTY_PREFIX + "export.html.imagesUri" , pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.imagesUri",""));
        }
        if (pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlHeader","").length() > 0)
        {
            context.setValue(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlHeader" , pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlHeader",""));
        }
        if (pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlBetweenPages","").length() > 0)
        {
            context.setValue( JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlBetweenPages" , pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlBetweenPages",""));
        }
        if (pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlFooter","").length() > 0)
        {
            context.setValue( JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlFooter", pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlFooter",""));
        }
        if (pref.get(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT, jrPropUtils.getProperty(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT)).length() > 0)
        {
            context.setValue( JRHtmlExporterParameter.PROPERTY_SIZE_UNIT , pref.get(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT, jrPropUtils.getProperty(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT)));
        }

    }


    private void configureCsvExporter(JRExporter exporter, SimpleJasperReportsContext context) {

        Preferences pref = IReportManager.getPreferences();
        JRPropertiesUtil jrPropUtils = JRPropertiesUtil.getInstance(context);

        context.setValue( JRCsvExporterParameter.PROPERTY_FIELD_DELIMITER, pref.get(JRCsvExporterParameter.PROPERTY_FIELD_DELIMITER, jrPropUtils.getProperty(JRCsvExporterParameter.PROPERTY_FIELD_DELIMITER)));
        context.setValue( JRCsvExporterParameter.PROPERTY_RECORD_DELIMITER, pref.get(JRCsvExporterParameter.PROPERTY_RECORD_DELIMITER, jrPropUtils.getProperty(JRCsvExporterParameter.PROPERTY_RECORD_DELIMITER)));
    }

    private void configureXmlExporter(JRExporter exporter, SimpleJasperReportsContext context) {

        Preferences pref = IReportManager.getPreferences();

        //context.setValue( JRCsvExporterParameter.FIELD_DELIMITER, pref.get(JRCsvExporterParameter.PROPERTY_FIELD_DELIMITER, jrPropUtils.getProperty(JRCsvExporterParameter.PROPERTY_FIELD_DELIMITER)));
        //context.setValue( JRCsvExporterParameter.RECORD_DELIMITER, pref.get(JRCsvExporterParameter.PROPERTY_RECORD_DELIMITER, jrPropUtils.getProperty(JRCsvExporterParameter.PROPERTY_RECORD_DELIMITER)));
    }


}
