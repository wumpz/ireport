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
package com.jaspersoft.ireport.jasperserver;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.logpane.IRConsoleTopComponent;
import com.jaspersoft.ireport.designer.logpane.LogTextArea;
import com.jaspersoft.ireport.jasperserver.ui.JRViewerTopComponent;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperPrint;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Mutex;

/**
 *
 * @author gtoffoli
 */
public class ReportRunner implements Runnable {
    
	protected static final String VERSION_SUPPORT_REPORT_LOCALE = "2.0.1";
	
    private RepositoryReportUnit reportUnit;
    private java.util.Map map;
    private JServer server;
    private LogTextArea lta;
    
    
    
    /** Creates a new instance of ReportRunner */
    public ReportRunner() {
    }
        
    public void run()
    {
        final ProgressHandle handle = ProgressHandleFactory.createHandle("Executing " +  getReportUnit().getDescriptor().getLabel());
            
        try {
            
            handle.start();
            lta.logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#0000CC\"><b>" + JasperServerManager.getFormattedString("compiler.fillReportOnServer","Filling report on server: {0}", new Object[]{""+ getServer().getUrl()}) + "</b> </font><hr>", true);
            
            Map params = getReportParameters();
            
            final JasperPrint print = getServer().getWSClient().runReport(getReportUnit().getDescriptor() , params);
            
            // Show the print somewhere...
            if (print != null)
            {
                handle.setDisplayName( "Showing " +  getReportUnit().getDescriptor().getLabel());
                Mutex.EVENT.readAccess(new Runnable() {

                    public void run() {
                        JRViewerTopComponent win = JRViewerTopComponent.findInstance();
                        win.setJasperPrint(print);
                        win.open();
                        win.requestActive();
                    }
                });
                
            }
            
            
            
            //exportPrint( IReportManager.getInstance()..IREPORT_TMP_DIR, getReportUnit().getDescriptor().getName() ,print, getLta());
        } catch (Exception t)
        {
            StringWriter sw = new StringWriter();
            t.printStackTrace(new java.io.PrintWriter( sw ));
            sw.flush();
            lta.logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#CC0000\">"+ t.getMessage() + "</font><hr>", true);
            lta.logOnConsole( sw.toString(), false);
            
            Mutex.EVENT.readAccess(new Runnable() {

                    public void run() {
                        IRConsoleTopComponent win = IRConsoleTopComponent.findInstance();
                        win.open();
                        win.requestActive();
                    }
                });
        }
        finally
        {
            handle.finish();
        }
    }

	protected Map getReportParameters() throws Exception {
		Map params = new HashMap(getMap());
		String serverVersion = getServer().getWSClient().getVersion();
		if (VERSION_SUPPORT_REPORT_LOCALE.compareTo(serverVersion) <= 0
				&& !params.containsKey(JRParameter.REPORT_LOCALE)) {
			String locale = getReportLocale();
			if (locale != null && locale.length() > 0) {
				//setting the locale as a String so that it can be marshalled to XML
				params.put(JRParameter.REPORT_LOCALE, locale);
			}
		}
		return params;
	}

	protected String getReportLocale() {
		return IReportManager.getInstance().getProperty("reportLocale");
	}
    
    

    public RepositoryReportUnit getReportUnit() {
        return reportUnit;
    }

    public void setReportUnit(RepositoryReportUnit reportUnit) {
        this.reportUnit = reportUnit;
    }

    public java.util.Map getMap() {
        return map;
    }

    public void setMap(java.util.Map map) {
        this.map = map;
    }

    public JServer getServer() {
        return server;
    }

    public void setServer(JServer server) {
        this.server = server;
    }

    public LogTextArea getLta() {
        return lta;
    }

    public void setLta(LogTextArea lta) {
        this.lta = lta;
    }
    
    
    /*
    public void exportPrint(String tmpDir, String fileName, JasperPrint print, LogTextArea lta)
    {
        String status = "";
        if(!tmpDir.endsWith(File.separator))
            tmpDir = tmpDir + File.separator;

        int format = getMainFrame().getReportViewer();

        if(print != null)
        {
            JRExporter exporter = null;
            lta.logOnConsole(
                    "<font face=\"SansSerif\"  size=\"3\" color=\"#0000CC\"><b>" +
                    IRPlugin.getString("compiler.reportFilled","Report filled") +
                    "</b>&nbsp;" +
                    IRPlugin.getFormattedString("compiler.pagesGenerated","(pages generated: {0,number,integer} )", new Object[]{new Integer(print.getPages().size())}) +
                    "</font><hr>", true);
            
            status = "exporting report";
            long start = System.currentTimeMillis();
            String viewer_program = "";
            String exportingMessage = "";
            
            boolean errorExporting = false;
            
            try
            {
                if(format == MainFrame.IREPORT_PDF_VIEWER)
                {
                    exporter = new JRPdfExporter();
                    if(getMainFrame().getProperties().getProperty("PDF_IS_ENCRYPTED") != null)
                        exporter.setParameter(JRPdfExporterParameter.IS_ENCRYPTED, new Boolean(getMainFrame().getProperties().getProperty("PDF_IS_ENCRYPTED")));
                    if(getMainFrame().getProperties().getProperty("PDF_IS_128_BIT_KEY") != null)
                        exporter.setParameter(JRPdfExporterParameter.IS_128_BIT_KEY, new Boolean(getMainFrame().getProperties().getProperty("PDF_IS_128_BIT_KEY")));
                    if(getMainFrame().getProperties().getProperty("PDF_USER_PASSWORD") != null)
                        exporter.setParameter(JRPdfExporterParameter.USER_PASSWORD, getMainFrame().getProperties().getProperty("PDF_USER_PASSWORD"));
                    if(getMainFrame().getProperties().getProperty("PDF_OWNER_PASSWORD") != null)
                        exporter.setParameter(JRPdfExporterParameter.OWNER_PASSWORD, getMainFrame().getProperties().getProperty("PDF_OWNER_PASSWORD"));
                    if(getMainFrame().getProperties().getProperty("PDF_PERMISSIONS") != null)
                        exporter.setParameter(JRPdfExporterParameter.PERMISSIONS, new Integer(getMainFrame().getProperties().getProperty("PDF_PERMISSIONS")));
                    fileName = Misc.changeFileExtension(fileName, "pdf");
                    exportingMessage = IRPlugin.getFormattedString("compiler.exporting.pdf","Exporting pdf to file (using iText)... {0}", new Object[]{fileName});
           
                    viewer_program = getMainFrame().getProperties().getProperty("ExternalPDFViewer");
                } else
                if(format == MainFrame.IREPORT_CSV_VIEWER)
                {
                    exporter = new JRCsvExporter();
                    if(getMainFrame().getProperties().getProperty("CSV_FIELD_DELIMITER") != null)
                        exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, getMainFrame().getProperties().getProperty("CSV_FIELD_DELIMITER"));
                    fileName = Misc.changeFileExtension(fileName, "csv");
                    exportingMessage = IRPlugin.getFormattedString("compiler.exporting.cvs","Exporting CSV to file... {0}", new Object[]{fileName});
                    viewer_program = Misc.nvl(getMainFrame().getProperties().getProperty("ExternalCSVViewer"), "");
                } else
                if(format == MainFrame.IREPORT_HTML_VIEWER)
                {
                    exporter = new JRHtmlExporter();
                    if(getMainFrame().getProperties().getProperty("HTML_IMAGES_DIR_NAME") != null)
                        exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR_NAME, getMainFrame().getProperties().getProperty("HTML_IMAGES_DIR_NAME"));
                    if(getMainFrame().getProperties().getProperty("HTML_IS_OUTPUT_IMAGES_TO_DIR") != null)
                        exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, new Boolean(getMainFrame().getProperties().getProperty("HTML_IS_OUTPUT_IMAGES_TO_DIR")));
                    if(getMainFrame().getProperties().getProperty("HTML_IMAGES_URI") != null)
                        exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, getMainFrame().getProperties().getProperty("HTML_IMAGES_URI"));
                    if(getMainFrame().getProperties().getProperty("HTML_HTML_HEADER") != null)
                        exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, getMainFrame().getProperties().getProperty("HTML_HTML_HEADER"));
                    if(getMainFrame().getProperties().getProperty("HTML_BETWEEN_PAGES_HTML") != null)
                        exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, getMainFrame().getProperties().getProperty("HTML_BETWEEN_PAGES_HTML"));
                    if(getMainFrame().getProperties().getProperty("HTML_HTML_FOOTER") != null)
                        exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, getMainFrame().getProperties().getProperty("HTML_HTML_FOOTER"));
                    if(getMainFrame().getProperties().getProperty("HTML_IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS") != null)
                        exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, new Boolean(getMainFrame().getProperties().getProperty("HTML_IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS")));
                    if(getMainFrame().getProperties().getProperty("HTML_IS_WHITE_PAGE_BACKGROUND") != null)
                        exporter.setParameter(JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND, new Boolean(getMainFrame().getProperties().getProperty("HTML_IS_WHITE_PAGE_BACKGROUND")));
                    if(getMainFrame().getProperties().getProperty("HTML_IS_USING_IMAGES_TO_ALIGN") != null)
                        exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, new Boolean(getMainFrame().getProperties().getProperty("HTML_IS_USING_IMAGES_TO_ALIGN")));
                    fileName = Misc.changeFileExtension(fileName, "html");
                    exportingMessage = IRPlugin.getFormattedString("compiler.exporting.html","Exporting HTML to file... {0}", new Object[]{fileName});
                    viewer_program = Misc.nvl(getMainFrame().getProperties().getProperty("ExternalHTMLViewer"), "");
                } else
                if(format == MainFrame.IREPORT_XLS_VIEWER)
                {
                    exporter = new JRXlsExporter();
                    if(getMainFrame().getProperties().getProperty("XLS_IS_ONE_PAGE_PER_SHEET") != null)
                        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, new Boolean(getMainFrame().getProperties().getProperty("XLS_IS_ONE_PAGE_PER_SHEET")));
                    if(getMainFrame().getProperties().getProperty("XLS_IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS") != null)
                        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, new Boolean(getMainFrame().getProperties().getProperty("XLS_IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS")));
                    if(getMainFrame().getProperties().getProperty("XLS_IS_WHITE_PAGE_BACKGROUND") != null)
                        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, new Boolean(getMainFrame().getProperties().getProperty("XLS_IS_WHITE_PAGE_BACKGROUND")));
                    if(getMainFrame().getProperties().getProperty("XLS_IS_AUTO_DETECT_CELL_TYPE") != null)
                        exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, new Boolean(getMainFrame().getProperties().getProperty("XLS_IS_AUTO_DETECT_CELL_TYPE")));
                    fileName = Misc.changeFileExtension(fileName, "xls");
                    exportingMessage = IRPlugin.getFormattedString("compiler.exporting.xls","Exporting xls to file (using POI)...  {0}", new Object[]{fileName});
                    viewer_program = Misc.nvl(getMainFrame().getProperties().getProperty("ExternalXLSViewer"), "");
                } else
                if(format == MainFrame.IREPORT_XLS2_VIEWER)
                {
                    exporter = new JExcelApiExporter();
                    if(getMainFrame().getProperties().getProperty("XLS2_IS_FONT_SIZE_FIX_ENABLED") != null)
                        exporter.setParameter(JExcelApiExporterParameter.IS_FONT_SIZE_FIX_ENABLED, new Boolean(getMainFrame().getProperties().getProperty("XLS2_IS_FONT_SIZE_FIX_ENABLED")));
                    fileName = Misc.changeFileExtension(fileName, "xls");
                    exportingMessage = IRPlugin.getFormattedString("compiler.exporting.xls2","Exporting xls to file (using JExcelApi)... {0}", new Object[]{fileName});

                    viewer_program = Misc.nvl(getMainFrame().getProperties().getProperty("ExternalXLSViewer"), "");
                } else
                if(format == MainFrame.IREPORT_JAVA_VIEWER)
                {
                    exporter = new JRGraphics2DExporter();
                    exportingMessage = "Exporting to Java2D ";
                    viewer_program = null;
                } else
                if(format == MainFrame.IREPORT_JASPER_VIEWER)
                {
                    exportingMessage = IRPlugin.getString("compiler.exporting.jasper","Viewing with JasperReports Viewer");
                    exporter = null;
                    viewer_program = null;
                } else
                if(format == MainFrame.IREPORT_TXT_VIEWER)
                {
                    exporter = new JRTxtExporter();
                    if(getMainFrame().getProperties().getProperty("TXT_PAGE_ROWS") != null)
                        exporter.setParameter(JRTxtExporterParameter.PAGE_ROWS, getMainFrame().getProperties().getProperty("TXT_PAGE_ROWS"));
                    if(getMainFrame().getProperties().getProperty("TXT_PAGE_COLUMNS") != null)
                        exporter.setParameter(JRTxtExporterParameter.PAGE_COLUMNS, getMainFrame().getProperties().getProperty("TXT_PAGE_COLUMNS"));
                    if(getMainFrame().getProperties().getProperty("TXT_ADD_FORM_FEED") != null)
                        exporter.setParameter(JRTxtExporterParameter.ADD_FORM_FEED, new Boolean(getMainFrame().getProperties().getProperty("TXT_ADD_FORM_FEED")));
                    fileName = Misc.changeFileExtension(fileName, "txt");
                    exportingMessage = IRPlugin.getFormattedString("compiler.exporting.txt","Exporting txt (iReport) to file... {0}", new Object[]{fileName});
                    viewer_program = Misc.nvl(getMainFrame().getProperties().getProperty("ExternalTXTViewer"), "");
                } else
                if(format == MainFrame.IREPORT_TXT_JR_VIEWER)
                {
                    exporter = new JRTextExporter();
                    if(getMainFrame().getProperties().getProperty("JRTXT_PAGE_WIDTH") != null)
                        exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, new Integer(getMainFrame().getProperties().getProperty("JRTXT_PAGE_WIDTH")));
                    if(getMainFrame().getProperties().getProperty("JRTXT_PAGE_HEIGHT") != null)
                        exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, new Integer(getMainFrame().getProperties().getProperty("JRTXT_PAGE_HEIGHT")));
                    if(getMainFrame().getProperties().getProperty("JRTXT_CHARACTER_WIDTH") != null)
                        exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Integer(getMainFrame().getProperties().getProperty("JRTXT_CHARACTER_WIDTH")));
                    if(getMainFrame().getProperties().getProperty("JRTXT_CHARACTER_HEIGHT") != null)
                        exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Integer(getMainFrame().getProperties().getProperty("JRTXT_CHARACTER_HEIGHT")));
                    if(getMainFrame().getProperties().getProperty("JRTXT_BETWEEN_PAGES_TEXT") != null)
                        exporter.setParameter(JRTextExporterParameter.BETWEEN_PAGES_TEXT, getMainFrame().getProperties().getProperty("JRTXT_BETWEEN_PAGES_TEXT"));
                    fileName = Misc.changeFileExtension(fileName, "txt");
                    exportingMessage = IRPlugin.getFormattedString("compiler.exporting.txt2","Exporting txt (jasperReports) to file... {0}", new Object[]{fileName});
                    viewer_program = Misc.nvl(getMainFrame().getProperties().getProperty("ExternalTXTViewer"), "");
                } else
                if(format == MainFrame.IREPORT_RTF_VIEWER)
                {
                    exporter = new JRRtfExporter();
                    fileName = Misc.changeFileExtension(fileName, "rtf");
                    exportingMessage = IRPlugin.getFormattedString("compiler.exporting.rtf","Exporting RTF to file... {0}", new Object[]{fileName});
                    viewer_program = Misc.nvl(getMainFrame().getProperties().getProperty("ExternalRTFViewer"), "");
                }
                java.net.URL img_url = getClass().getResource("/it/businesslogic/ireport/icons/printer_mini.png");
                lta.logOnConsole(
                        "<font face=\"SansSerif\"  size=\"3\"><img align=\"right\" src=\"" + img_url + "\"> &nbsp;" +
                        exportingMessage + "</font>", true);
                if(exporter != null)
                {
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, (new StringBuilder()).append(tmpDir).append(fileName).toString());
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                    exporter.exportReport();
                } else
                if(format == MainFrame.IREPORT_JASPER_VIEWER)
                {
                    JasperViewer jasperViewer = new JasperViewer(print, false);
                    jasperViewer.setTitle("iReport JasperViewer");
                    jasperViewer.setVisible(true);
                }
            }
            catch(Throwable ex2)
            {
                errorExporting = true;
                lta.logOnConsole(IRPlugin.getString("compiler.exporting.error","Error exporting print...")+"\n");
                ex2.printStackTrace();
                lta.logOnConsole((new StringBuilder()).append("").append(ex2).toString());
            }
            
            
            if (!errorExporting)
            {
                lta.logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#0000CC\"><b>" +
                        IRPlugin.getFormattedString("compiler.exporting.time","Export running time: {0,number,long} millisecs", new Object[]{new Long(System.currentTimeMillis() - start)}) +
                        "</b></font><hr>", true);
                Runtime rt = Runtime.getRuntime();
                if(viewer_program == null || viewer_program.equals(""))
                {
                    if(format != MainFrame.IREPORT_JASPER_VIEWER)
                        lta.logOnConsole("<font face=\"SansSerif\"  size=\"3\">" +
                                IRPlugin.getString("compiler.exporting.noviewer","No external viewer specified for this type of print. Set it in the options frame!")+
                                 "</font>", true);
                } else
                {
                    try
                    {
                        File f = new File(fileName);

                        String execute_string = (new StringBuilder()).append(viewer_program).append(" ").append(f).append("").toString();
                        lta.logOnConsole("<font face=\"SansSerif\"  size=\"3\">" +
                                IRPlugin.getFormattedString("compiler.exporting.executing","Executing: {0}", new Object[]{execute_string}) +
                                "</font>", true);
                        rt.exec(execute_string);
                    }
                    catch(Exception ex)
                    {
                        lta.logOnConsole(IRPlugin.getString("compiler.exporting.viewing","Error viewing report...")+"\n");
                        ex.printStackTrace();
                    }
                }
            }
            else
            {
                lta.logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#CC0000\"><b>" + 
                        IRPlugin.getString("compiler.exporting.exporterror","Error exporting the report, try to run the report using the internal viewer.") +
                        "</b></font><hr>", true);
            }
        } else
        {
            
            lta.logOnConsole(IRPlugin.getString("compiler.printNotFilled","Print not filled.") + "\n");
        }
    }
    */

}
