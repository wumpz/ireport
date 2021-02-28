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
package com.jaspersoft.ireport.designer.menu;

import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlEditorSupport;
import com.jaspersoft.ireport.designer.compiler.IReportCompiler;
import java.util.HashMap;
import org.openide.cookies.SaveCookie;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.actions.CallableSystemAction;

public final class RunReportAction extends CallableSystemAction {

    @SuppressWarnings("unchecked")
    public void performAction() {
            
            if (IReportManager.getInstance().getActiveReport() == null) return;
            
            runReport(IReportManager.getInstance().getActiveVisualView().getEditorSupport());

    }

    @SuppressWarnings("unchecked")
    public static void runReport(JrxmlEditorSupport support)
    {
        runReport(support, false);
    }
    
    @SuppressWarnings("unchecked")
    public static void runReport(JrxmlEditorSupport support, boolean compileOnly)
    {
            try {
                SaveCookie save = support.getDataObject().getCookie(SaveCookie.class);
                if (save != null) save.save();
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        
            IReportCompiler ic = new IReportCompiler();

            int options = IReportCompiler.CMD_COMPILE;
            if (!compileOnly) options |= IReportCompiler.CMD_EXPORT;
            ic.setCommand( options );

//            if (jrf.getReport().getScriptletHandling() == jrf.getReport().SCRIPTLET_IREPORT_INTERNAL_HANDLING) {
//                ic.setCommand( ic.getCommand()  | IReportCompiler.CMD_COMPILE_SCRIPTLET);
//            }


            HashMap hm = new HashMap();
            hm.put( IReportCompiler.USE_EMPTY_DATASOURCE, "false");

            if ( IReportManager.getInstance().getDefaultConnection() == null ) {
                    javax.swing.JOptionPane.showMessageDialog( null,
                            "You must configure a connection first.\n\nGo to\nDatasources->Connection/Datasources to create one.",
                            "",javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
            }

            IReportConnection connection = IReportManager.getInstance().getDefaultConnection();


            //if (connection instanceof  it.businesslogic.ireport.connection.JDBCConnection)
            //{
            //    javax.swing.JOptionPane.showMessageDialog(null, ((it.businesslogic.ireport.connection.JDBCConnection)connection).getJDBCDriver());
            //}

            hm.put( IReportCompiler.USE_CONNECTION, "true");
            hm.put( IReportCompiler.CONNECTION, connection);
            //hm.put( ic.OUTPUT_DIRECTORY, getTranslatedCompileDirectory());

            /*
            JasperDesign jasperDesign = IReportManager.getInstance().getActiveReport();
            
            if (jasperDesign.getQuery() != null &&
                jasperDesign.getQuery().getLanguage() != null &&
                jasperDesign.getQuery().getLanguage().equals("groovy"))
            {
                hm.put( ic.COMPILER, "net.sf.jasperreports.compilers.JRGroovyCompiler");
            }
            */

            ic.setProperties(hm);

//            if (jRadioButtonMenuItemPreviewCSV.isSelected()) hm.put( ic.OUTPUT_FORMAT, "csv");
//            else if (jRadioButtonMenuItemPreviewHTML.isSelected()) hm.put( ic.OUTPUT_FORMAT, "html");
//            else if (jRadioButtonMenuItemPreviewXLS.isSelected()) hm.put( ic.OUTPUT_FORMAT, "xls");
//            else if (jRadioButtonMenuItemPreviewXLS2.isSelected()) hm.put( ic.OUTPUT_FORMAT, "xls2");
//            else if (jRadioButtonMenuItemPreviewJAVA.isSelected()) hm.put( ic.OUTPUT_FORMAT, "java2D");
//            else if (jRadioButtonMenuItemPreviewInternalViewer.isSelected()) hm.put( ic.OUTPUT_FORMAT, "jrviewer");
//            else if (jRadioButtonMenuItemPreviewTXT.isSelected()) hm.put( ic.OUTPUT_FORMAT, "txt");
//            else if (jRadioButtonMenuItemPreviewTXTJR.isSelected()) hm.put( ic.OUTPUT_FORMAT, "txtjr");
//            else if (jRadioButtonMenuItemPreviewRTF.isSelected()) hm.put( ic.OUTPUT_FORMAT, "rtf");
//            else if (jRadioButtonMenuItemPreviewODF.isSelected()) hm.put( ic.OUTPUT_FORMAT, "odf");
//            else
//                hm.put( ic.OUTPUT_FORMAT, "pdf");
            
            hm.put( ic.OUTPUT_FORMAT, "jrviewer");

            ic.setProperties(hm);
            ic.setFile( support.getDataObject().getPrimaryFile() );
            ic.setIReportConnection(connection);
            ic.setSupport(support);
            
            
            RequestProcessor.getDefault().post(ic);
    }
    
    
    public String getName() {
        return NbBundle.getMessage(RunReportAction.class, "CTL_RunReportAction");
    }

    @Override
    protected String iconResource() {
        return "com/jaspersoft/ireport/designer/menu/runReport.png";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}