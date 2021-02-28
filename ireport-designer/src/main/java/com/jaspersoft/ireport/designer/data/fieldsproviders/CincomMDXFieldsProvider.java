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

import com.jaspersoft.ireport.designer.FieldsProviderEditor;
import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.connection.JRXMLADataSourceConnection;
import com.jaspersoft.ireport.designer.data.ReportQueryDialog;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;

import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.design.JRDesignField;

/**
 *
 * @author gtoffoli
 */
public class CincomMDXFieldsProvider extends MDXFieldsProvider {
    
    private final String axis0 = "Axis0";  
    private final String axis1 = "Axis1";  
    private final String slicer = "SlicerAxis";
    private final char leftPar='['; 
    private final char rightPar=']';
    public static java.util.ArrayList dimensionsList = new java.util.ArrayList();
    private boolean foundMeasure;   // to handle queries with no explicit measure in them
    
    /** Creates a new instance of CincomMDXFieldsProvider */
    public CincomMDXFieldsProvider() {
        super();
    }
    
    public boolean supportsGetFieldsOperation() {
        return true;
    }
    
    public boolean supportsAutomaticQueryExecution() {
        return true;
    }
    
    @Override
    public boolean hasEditorComponent() {
        return false;
    }
    
    @Override
    public FieldsProviderEditor getEditorComponent(ReportQueryDialog reportQueryDialog) {
        
        return null;
    }
    
    @Override
    public JRField[] getFields(IReportConnection con, JRDataset reportDataset, Map parameters) throws JRException, UnsupportedOperationException {
        /**
             * Copyright (C) 2006 CINCOM SYSTEMS, INC.
             * All Rights Reserved
             * www.cincom.com
             */
            if (con instanceof JRXMLADataSourceConnection)
            {
                try {
                    String query = reportDataset.getQuery().getText();
                   
                     java.util.ArrayList v_fields = getFields(query, (JRXMLADataSourceConnection)con);
                   
                     // Translate 
                    JRField[] final_fields = new JRField[v_fields.size()];
                    for (int i=0; i<v_fields.size(); ++i)
                    {
                        XmlaFieldNode f = (XmlaFieldNode)v_fields.get(i);
                        JRDesignField field = new JRDesignField();
                        field.setName( f.getName() );
                        field.setValueClassName( f.getValueClassName() );
                        field.setDescription( f.getDescription() );
                        final_fields[i] = field;
                    }

                    return final_fields;
                
                     
                } catch (Exception ex)
                {
                    throw new JRException(ex);
                }
            }
            else
            {
                     throw new JRException("The active connection is not of type XMLA. Activate an XMLA connection first.");
            }
    }
    
    
    /**
     * Returns the Fields discovered for the MDX Query
     * @param : Query String
     * @param : status is added to return the appropriate field values
     * Reason : to handle the Wizard Dialog and ReportQuery Dialog issue
     * ReportQueryDialog looks for a jTable / ArrayList
     * WizardDialog looks for a jList / List
     * status == true implies that the call is from WizardDialog
     *           false imples that the call is from ReportQueryDialog.
     * 
     */
    @SuppressWarnings("unchecked")
    public java.util.ArrayList getFields(String query, JRXMLADataSourceConnection con) throws java.lang.Exception 
    {
        foundMeasure = false;      
        XmlaFieldNode fld2;
        java.util.ArrayList fields = new java.util.ArrayList();
        rex.metadata.ServerMetadata smd =
                new rex.metadata.ServerMetadata(con.getUrl(),null);
        if (smd.isValidUrl() == false) {
            return null;
        }
        rex.xmla.RexXMLAExecuteProperties rexProperties = new rex.xmla.RexXMLAExecuteProperties();
        rexProperties.setDataSourceInfo(con.getDatasource());
        rexProperties.setCatalog(con.getCatalog());
        CincomMDXFieldsProvider.dimensionsList.clear(); 
        try {
            rex.metadata.ExecuteResult eResult =
                    new rex.metadata.ExecuteResult(smd.execute(query,rexProperties),null);
            if (!addMDXAxisColumns(fields, eResult, axis0)){
                return null;
            }
            if (!addMDXAxisColumns(fields, eResult, axis1)){
                return null;
            }
            if (!addMDXAxisColumns(fields, eResult, slicer)){
                return null;
            }
            //if no measure was explicitly parsed, then add one to pick up the default (ALL) measure:
            if (!foundMeasure)   {
                fld2 = new XmlaFieldNode(
                        "DefaultMeasure", 2);
                fld2.setDescription("DefaultMeasure");
          
                fields.add(fld2);
            }
            eResult = null;
            rexProperties = null;
            smd = null;
        }

       catch(Exception e){
           throw e; 
       }
        return fields;
    }
    
    /**
     * Adds all the Dimensions and measures found on Rows, Columns and Slicer axis
     * 
     * @param fields : All the dimension / measures discovered are added and returned
     * @param : status is added to return the appropriate field values
     * Reason : to handle the Wizard Dialog and ReportQuery Dialog issue
     * ReportQueryDialog looks for a jTable / ArrayList
     * WizardDialog looks for a jList / List
     * status == true implies that the call is from WizardDialog
     *           false imples that the call is from ReportQueryDialog.
     *
     */
    @SuppressWarnings("unchecked")
    private boolean addMDXAxisColumns(java.util.ArrayList fields,
            rex.metadata.ExecuteResult eResult,
            String axisName)    
    {
        java.util.HashSet fSet = new java.util.HashSet(); 
        
        
        int axisNo=-1;
        if (eResult == null || fields == null || axisName == null ){
            return false; 
        }
        if ((axisName.compareTo(axis0) != 0) &&
                (axisName.compareTo(axis1) !=0) &&
                (axisName.compareTo(slicer) !=0)){
            return false;  
        }
        rex.metadata.resultelements.Axis axis =
                eResult.getAxis(axisName);
        if (axis == null){
            return false; 
        }
        rex.metadata.resultelements.HierarchyInfo hierInfo;
        rex.metadata.resultelements.Tuple tuple;
        
        
        if (axisName.compareTo(axis0) == 0) {
            axisNo = 0;
        } else {
            axisNo = 1;
        }
        tuple = null;
        
        XmlaFieldNode fld;
        String longName= "";
        String shortName = "";
       
        int hierarchyCount = axis.getHierarchyInfoCount();   
        for (int hierIndex=0;hierIndex<hierarchyCount;hierIndex++) {
            hierInfo = axis.getHierarchyInfoAt(hierIndex);
            if (hierInfo == null){
                return false; 
            }
           
            int tupleCount = axis.getTupleCount();  // ham 9/11/06
            for (int tupleIndex=0; tupleIndex< tupleCount; tupleIndex++){
                tuple = axis.getTupleAt(tupleIndex);
                // following loop  for multiple dimensions in one (slicer) tuple
                // references within this loop to getMemberAt(0) were changed to getMemberAt(tupleMemberIndex)
                int tupleMemberCount = tuple.getMemberCount();
                for (int tupleMemberIndex=0;tupleMemberIndex<tupleMemberCount;tupleMemberIndex++) {
                    if (tuple.getMemberAt(tupleMemberIndex).isMeasure()) {
                        foundMeasure = true;  
                        
                        longName= tuple.getMemberAt(tupleMemberIndex).getUniqueName();
                       
                        shortName= longName.substring(longName.lastIndexOf(leftPar)+1,
                                longName.lastIndexOf(rightPar));
                        
                        
                        // following IF and adding it to FSET added for duplicate fields in query problem
                       
                        
                        if (!fSet.contains(shortName))  {
                            
                            
                            fld = new XmlaFieldNode(
                                    shortName, axisNo);
                            fld.setDescription(longName);
                            fSet.add(shortName);  
                            
                            // Checking the status and add the appropirte values to fields
                            //if (status.booleanValue()){  //this incase of WizardDialog
                                fields.add(fld);
                            //} else{  //case of ReportQueryDialog
                            //    fields.add(new Object[]{fld,fld.getClassType(),fld.getDescription()});
                            //}
                        }
                    } else{
                        
                        longName= tuple.getMemberAt(tupleMemberIndex).getLname();
                      
                        shortName= longName.substring(longName.lastIndexOf(leftPar)+1,
                                longName.lastIndexOf(rightPar));
                           if (!fSet.contains(shortName)) {
                            fld = new XmlaFieldNode(
                                    shortName,axisNo);
                            fld.setDescription(longName);
                            fSet.add(shortName);
                            
                            // Checking the status and add the appropirte values to fields
                         
                            //if (status.booleanValue()){  //this incase of WizardDialog
                                fields.add(fld);
                              
                                if(!CincomMDXFieldsProvider.dimensionsList.contains(fld)){
                                    CincomMDXFieldsProvider.dimensionsList.add(fld);
                                }
                                
                            //} else{  //case of ReportQueryDialog
                            //    fields.add(new Object[]{fld,fld.getClassType(),fld.getDescription()});
                             
                            //    if(!this.dimensionsList.contains(fld)){
                            //          this.dimensionsList.add(new Object[]{fld,fld.getClassType(),fld.getDescription()});
                            //    }
                            //}
                      
                        }
                    }
                }
            }
            
        }
        return true;
    }
    /**
     * Helper method to return the dimensions discoverd in the MDX Query
     */
    public static java.util.ArrayList getDimensions(){
        return dimensionsList;
    }
}
