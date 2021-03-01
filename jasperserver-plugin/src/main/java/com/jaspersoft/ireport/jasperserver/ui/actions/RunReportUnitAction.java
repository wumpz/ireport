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
package com.jaspersoft.ireport.jasperserver.ui.actions;

import com.jaspersoft.ireport.designer.logpane.IRConsoleTopComponent;
import com.jaspersoft.ireport.designer.logpane.LogTextArea;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.ReportRunner;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.jasperserver.RepositoryReportUnit;
import com.jaspersoft.ireport.jasperserver.ui.ReportUnitRunDialog;
import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.ListItemWrapper;
import com.jaspersoft.ireport.jasperserver.ui.nodes.ResourceNode;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.Argument;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.InputControlQueryDataRow;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ListItem;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.actions.NodeAction;

public final class RunReportUnitAction extends NodeAction {

    public String getName() {
        return NbBundle.getMessage(RunReportUnitAction.class, "CTL_RunReportUnitAction");
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    protected void performAction(org.openide.nodes.Node[] activatedNodes)
    {
        runReportUnit(activatedNodes[0]);
    }    
    
    public void runReportUnit(org.openide.nodes.Node reportUnitNode)
    {
        if (!(reportUnitNode instanceof ResourceNode)) return;
        
        
        
        RepositoryFolder rf = ((ResourceNode)reportUnitNode).getRepositoryObject();
                
        RepositoryReportUnit reportUnit = null;
        ResourceDescriptor optionsRd = null;
        HashMap defaultValues = new HashMap();
        
        if (rf.getDescriptor().getWsType().equals("ReportOptionsResource"))
        {
            try {
                    optionsRd = rf.getDescriptor();
                    java.lang.String ruURI = optionsRd.getResourcePropertyValue("PROP_RU_URI");
                    com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor reportRd = new com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor();
                    reportRd.setUriString(ruURI);
                    reportRd = rf.getServer().getWSClient().get(reportRd, null);
                    reportUnit = new RepositoryReportUnit(rf.getServer(), reportRd);
                    reportUnit.setLoaded(false);
                    
                    // get the default values...
                    ResourceProperty rp = optionsRd.getResourceProperty("PROP_VALUES");
        
                    List list = rp.getProperties();
                    if (list != null)
                    {
                        for (int i=0; i<list.size(); ++i)
                        {
                           ResourceProperty li = (ResourceProperty)list.get(i);
                           String value = (li.getValue() == null) ? "" : li.getValue();
                           if (li.getProperties().size() > 0) // it is a list....
                           {
                                java.util.ArrayList listVal = new java.util.ArrayList();
                                for (int k=0; k<li.getProperties().size(); ++k)
                                {
                                    ResourceProperty sli = (ResourceProperty)li.getProperties().get(k);
                                    listVal.add(sli.getValue());
                                }
                                
                                defaultValues.put(li.getName(), (Object)listVal);
                           }
                           else
                           {
                                defaultValues.put(li.getName(), value);
                           }
                        }
                    }
                    
                    
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        }
        
            
        if (reportUnit == null)
        {
             reportUnit = getParentReportUnit(reportUnitNode);
        }
            
         if ( reportUnit != null)
         {
            JServer server = reportUnit.getServer();
            
            LogTextArea lta = IRConsoleTopComponent.getDefault().createNewLog();
            lta.setTitle( JasperServerManager.getFormattedString("repositoryExplorer.message.runningReport", "Running {0}", new Object[]{reportUnit.getDescriptor().getName()}) );
            IRConsoleTopComponent.getDefault().setActiveLog( lta );
             
             try {

                List list = server.getWSClient().list( reportUnit.getDescriptor() );

                String dsUri = null;
                
                List inputcontrols = new java.util.ArrayList();
                // get input controls...
                for (int i=0; i<list.size(); ++i)
                {
                    ResourceDescriptor sub_rd = (ResourceDescriptor)list.get(i);
                    if (sub_rd.getWsType().equals( ResourceDescriptor.TYPE_INPUT_CONTROL))
                    {
                        inputcontrols.add(sub_rd);
                    }
                    else if (sub_rd.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE))
                    {
                            dsUri = sub_rd.getReferenceUri();
                    }
                    else if ( RepositoryFolder.isDataSource( sub_rd))
                    {
                            dsUri = sub_rd.getUriString();
                    }
                }
                
               
                boolean atLeastOneVisible = false;
                for (int i=0; i<inputcontrols.size(); ++i)
                {
                        ResourceDescriptor ic = (ResourceDescriptor)inputcontrols.get(i);

                        if (ic.getResourcePropertyValue(  ResourceDescriptor.PROP_INPUTCONTROL_IS_VISIBLE ) == null ||
                            ic.getResourcePropertyValue(  ResourceDescriptor.PROP_INPUTCONTROL_IS_VISIBLE ).equals("true"))
                        {
                            atLeastOneVisible = true;
                        }

                        //System.out.println("Got:  " + ic.getName() + " " + ic.getControlType());
                        if (ic.getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY ||
                            ic.getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY_RADIO ||
                            ic.getControlType() == ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY ||
                            ic.getControlType() == ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY_CHECKBOX)
                        {
                            String dsUriQuery = null;
                            inputcontrols.remove(ic);

                            String language = "sql";
                            String query = null;
                            // Ask to add values to the control....
                            java.util.List args = new java.util.ArrayList();
                            // reset query data...
                            // Look if this query has a specific datasource...
                            for (int k=0; dsUriQuery == null && k<ic.getChildren().size(); ++k)
                            {
                                ResourceDescriptor sub_ic = (ResourceDescriptor)ic.getChildren().get(k);
                                if (sub_ic.getWsType().equals(ResourceDescriptor.TYPE_QUERY) )
                                {
                                    language = sub_ic.getResourceProperty(ResourceDescriptor.PROP_QUERY_LANGUAGE).getValue();
                                    query = sub_ic.getResourceProperty(ResourceDescriptor.PROP_QUERY).getValue();

                                    // Look in the query detail
                                    for (int k2=0; k2<sub_ic.getChildren().size(); ++k2)
                                    {
                                        ResourceDescriptor sub_sub_ic = (ResourceDescriptor)sub_ic.getChildren().get(k2);
                                        if (RepositoryFolder.isDataSource( sub_sub_ic) )
                                        {
                                            dsUriQuery = sub_sub_ic.getUriString();
                                            
                                            break;
                                        }
                                    }
                                }
                            }
                            if (dsUriQuery == null) dsUriQuery = dsUri;
                            ic.setResourceProperty(ic.PROP_QUERY_DATA, null);


//                            if (JasperServerManager.getMainInstance().getDataProvidersMap().containsKey(language))
//                            {
//                                JSDataProvider dataProvider = JasperServerManager.getMainInstance().getDataProvidersMap().get(language);
//                                try {
//                                    List data = dataProvider.getData(server, dsUri, query, new HashMap());
//                                    ic.setQueryData(data);
//                                } catch (Exception ex)
//                                {
//                                    ex.printStackTrace();
//                                }
//
//                            }
//                            else
//                            {
                                args.add(new Argument( Argument.IC_GET_QUERY_DATA, dsUriQuery));
                                ic = server.getWSClient().get(ic, null, args);

                                // Filter query data....
                                /*
                                Map<Object,List> valueMap = new HashMap<Object, List>();

                                for (int k=0; k<ic.getQueryData().size(); ++k)
                                {
                                    Object itemObject = ic.getQueryData().get(k);
                                    if (itemObject instanceof InputControlQueryDataRow)
                                    {
                                        InputControlQueryDataRow qd =  (InputControlQueryDataRow)itemObject;
                                        List itemColumnValues = qd.getColumnValues();

                                        if (valueMap.containsKey(qd.getValue()))
                                        {
                                            List<List> records = valueMap.get(itemObject);
                                            boolean duplicatedRecord = false;
                                            for (int li = 0; li<records.size(); ++i)
                                            {
                                                List currentRecord = records.get(li);
                                                // Check if this record is equal to the current one...
                                                boolean different = false;
                                                for (int ri = 0; ri<itemColumnValues.size(); ++ri)
                                                {
                                                    Object field1 = itemColumnValues.get(ri);
                                                    Object field2 = currentRecord.get(ri);

                                                    if (field1 == field2 ||
                                                        (null != field1 && null != field2 && field1.equals(field2)) )
                                                    {
                                                        continue;
                                                    }
                                                    different = true; break;
                                                }
                                                if (!different)
                                                {
                                                    ic.getQueryData().remove(itemObject);
                                                    k--;
                                                    duplicatedRecord = true;
                                                    break;
                                                }
                                            }

                                            if (!duplicatedRecord)
                                            {
                                                records.add(itemColumnValues);
                                            }
                                        }
                                        else
                                        {
                                            List<List> records = new ArrayList<List>();
                                            records.add(itemColumnValues);
                                            valueMap.put(itemObject, records);
                                        }
                                    }

                                }*/

//                            }
                            inputcontrols.add(i, ic);
                        }
                }
                java.util.Map map = null;
                if (atLeastOneVisible)
                {
                    ReportUnitRunDialog rurd = new ReportUnitRunDialog(Misc.getMainFrame(), true);
                    rurd.setServer(server);
                    rurd.setReportUnitUri( reportUnit.getDescriptor().getUriString() );
                    rurd.setInputControls( inputcontrols, defaultValues);

                    rurd.setVisible(true);
                    if (rurd.getDialogResult() == JOptionPane.OK_OPTION)
                    {
                        map = rurd.getParametersValues();
                    }
                    else
                    {
                        return;
                    }
                }
                else
                {
                    map = new java.util.HashMap();
                }
                
                
                ReportRunner rr = new ReportRunner();
                rr.setLta( lta );
                rr.setMap( map );
                rr.setReportUnit( reportUnit );
                rr.setServer( server );
                
                RequestProcessor.getDefault().post(rr);
                
             } catch (Exception ex)
             {
                //JOptionPane.showMessageDialog(getPlugin().getMainFrame(),JasperServerManager.getFormattedString("messages.error.3", "Error:\n {0}", new Object[] {ex.getMessage()}));
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new java.io.PrintWriter( sw ));
                sw.flush();
                lta.logOnConsole( sw.toString(), false);
                ex.printStackTrace();
             }
             finally
             {
                 lta.setRemovable(true);
             }
            
             /*
            Thread t = new Thread(new Runnable() {

                public void run() {
                    try {
                        rf.getServer().getWSClient().modifyReportUnitResource(ruUri, rf.getDescriptor(), new File(fileName));

                        Mutex.EVENT.readAccess(new Runnable() {

                            public void run() {
                                JOptionPane.showMessageDialog(Misc.getMainFrame(),
                                JasperServerManager.getString("repositoryExplorer.message.fileUpdated", "File successfully updated."),
                                JasperServerManager.getString("repositoryExplorer.message.operationResult", "Operation result"), JOptionPane.INFORMATION_MESSAGE);

                            }
                        }); 
                    } catch (Exception ex)
                    {
                        final String msg = ex.getMessage();
                        Mutex.EVENT.readAccess(new Runnable() {

                            public void run() {
                                 JOptionPane.showMessageDialog(Misc.getMainFrame(),JasperServerManager.getFormattedString("messages.error.3", "Error:\n {0}", new Object[] {msg}),"Operation result", JOptionPane.ERROR_MESSAGE);
                            }
                        });
                        ex.printStackTrace();
                    }

                }
            });

            t.start();
              **/
        }
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        
        if (activatedNodes == null || activatedNodes.length != 1) return false;
        if ( activatedNodes[0] instanceof ResourceNode && 
             (((ResourceNode)activatedNodes[0]).getResourceDescriptor().getWsType().equals("ReportOptionsResource"))
             || ( getParentReportUnit(activatedNodes[0]) != null))
        {
            return true;
        }
        return false;
    }
    
    public RepositoryReportUnit getParentReportUnit(Node node)
    {
        if (node == null) return null;
        
        if ( (node instanceof ResourceNode) &&
            ((ResourceNode)node).getRepositoryObject() instanceof RepositoryReportUnit)
        {
            return (RepositoryReportUnit)((ResourceNode)node).getRepositoryObject();
        }
        
        return getParentReportUnit(node.getParentNode());
    }
}