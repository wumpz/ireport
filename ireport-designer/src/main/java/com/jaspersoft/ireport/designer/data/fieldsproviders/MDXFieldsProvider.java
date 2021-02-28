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
package com.jaspersoft.ireport.designer.data.fieldsproviders;

import com.jaspersoft.ireport.designer.FieldsProvider;
import com.jaspersoft.ireport.designer.FieldsProviderEditor;
import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.connection.CustomHTTPAuthenticator;
import com.jaspersoft.ireport.designer.connection.JRXMLADataSourceConnection;
import com.jaspersoft.ireport.designer.data.ReportQueryDialog;
import com.jaspersoft.ireport.designer.data.fieldsproviders.olap.OlapBrowser;
import com.jaspersoft.ireport.designer.utils.Misc;

import java.net.Authenticator;
import java.util.Locale;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import rex.event.RexWizardEvent;
import rex.event.RexWizardListener;
import rex.graphics.mdxeditor.RexWizard;

/**
 *
 * @author gtoffoli
 */
public class MDXFieldsProvider implements FieldsProvider, RexWizardListener {
    
    private OlapBrowser olapBrowser = null;
    protected String getQueryFromRex = "";
    
    public static boolean useVisualDesigner = true;
    
    /*
    static {
        
        java.util.Properties p = new java.util.Properties();
        try {
            p.load( MDXFieldsProvider.class.getClass().getResourceAsStream("/it/businesslogic/ireport/data/fieldsprovider.properties")  );

            if (p.getProperty("mdx").equals("0"))
            {
                useVisualDesigner = false;
                System.out.println("ReX designer disabled");
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
    }
    */
    
    
    
    /** Creates a new instance of SQLFieldsProvider */
    public MDXFieldsProvider() {
    }

    /**
     * Returns true if the provider supports the {@link #getFields(IReportConnection,JRDataset,Map) getFields} 
     * operation. By returning true in this method the data source provider indicates
     * that it is able to introspect the data source and discover the available fields.
     * 
     * @return true if the getFields() operation is supported.
     */
    public boolean supportsGetFieldsOperation() {
        return false;
    }
    
    public JRField[] getFields(IReportConnection con, JRDataset reportDataset, Map parameters) throws JRException, UnsupportedOperationException {
        return null;
    }

    public boolean supportsAutomaticQueryExecution() {
        return true;
    }

    public boolean hasQueryDesigner() {
        return useVisualDesigner;
    }

    public boolean hasEditorComponent() {
        return true;
    }

    /**
     * Copyright (C) 2006 CINCOM SYSTEMS, INC.
     * All Rights Reserved
     * www.cincom.com
     */
    
   /*
     * Opens the Rex MDX Query Editor
     * @param MdxQuery
     * @retrun mdx Query from REX
     * sending the Query to Rex and bringing back the modified MDX query
     */
    boolean gotMdxResult = false;
    public String designQuery(IReportConnection con,  String query, ReportQueryDialog reportQueryDialog) throws JRException, UnsupportedOperationException {
        
        String newMDXQuery = query; 
        if (query == null) newMDXQuery = "";
        
        if (con instanceof JRXMLADataSourceConnection)
        {
            String strURL = ((JRXMLADataSourceConnection)con).getUrl();
        
            String strDataSource = ((JRXMLADataSourceConnection)con).getDatasource();

            String strCatalog = ((JRXMLADataSourceConnection)con).getCatalog();

            String strCubeName=((JRXMLADataSourceConnection)con).getCube(); 
            
            String username = ((JRXMLADataSourceConnection)con).getUsername();
            if (username != null && username.length() > 0)
            {
            
                Authenticator.setDefault(new CustomHTTPAuthenticator( username, ((JRXMLADataSourceConnection)con).getPassword() ));
            }
            
            //invoke MDXEditor
            RexWizard mdxWizard= new RexWizard(strURL, strDataSource, strCatalog,
                    strCubeName,newMDXQuery, Locale.getDefault() );
            // Passing the MDX Query to Rex Wizard 
            mdxWizard.addRexWizardListener(this);
            

            mdxWizard.showDialog();
            
            //Wait for the return value....
            //System.out.println("Waiting for MDX query");
            //System.out.flush();
            //while (!gotMdxResult)
            //{
            //    Thread.yield();
            //}
            
            //System.out.println("My MDX query " + getQueryFromRex);
            //System.out.flush();
            //condition added to handle Cancel click's in RexWizard
            if (getQueryFromRex.length() > 0){
                return getQueryFromRex;
            }
        }
        else
        {
            javax.swing.JOptionPane.showMessageDialog(Misc.getMainWindow(),
                        //I18n.getString("messages.reportQueryDialog.connectionNotSupported",
                        "In order to use the MDX query designer, you need an XMLA datasource active.",
                        "",javax.swing.JOptionPane.WARNING_MESSAGE );
                return null;
            
        }
        
        return query;
    }
    
    /**
     * Rex Passes the MDX query to Report Query Dialog
     */
     public void getMdx(RexWizardEvent evt){

          getQueryFromRex = evt.getQuery();
          gotMdxResult = true;

     }

/* end of modifications */

    public FieldsProviderEditor getEditorComponent(ReportQueryDialog reportQueryDialog) {
        
        if (olapBrowser == null)
        {
            olapBrowser = new OlapBrowser();
            olapBrowser.setReportQueryDialog(reportQueryDialog);
            if (reportQueryDialog != null)
            {
                olapBrowser.setJTableFields( reportQueryDialog.getFieldsTable() );
            }
        }
        return olapBrowser;
    }
    
}
