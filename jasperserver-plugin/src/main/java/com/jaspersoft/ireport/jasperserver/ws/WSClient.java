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
package com.jaspersoft.ireport.jasperserver.ws;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.ws.util.ResourceConfigurationProvider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.attachments.AttachmentPart;
import org.apache.axis.client.Call;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.Argument;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ListItem;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.OperationResult;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.Request;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.ws.xml.Marshaller;
import com.jaspersoft.jasperserver.ws.xml.Unmarshaller;
import java.io.FileInputStream;
import org.apache.xerces.parsers.DOMParser;



/**
 *
 * @author gtoffoli
 */
public class WSClient {
	
	public static final String AXIS_CONFIGURATION_RESOURCE = "/com/jaspersoft/ireport/jasperserver/ws/client-config.wsdd";
    
    private JServer server = null;
    
    private String webservicesUri = null; // "http://127.0.0.1:8080/axis2/services/repository-ws-1.0";
    
    private ManagementService managementService = null;
    
    private Unmarshaller unmarshaller = new Unmarshaller();
    private Marshaller marshaller = new Marshaller();
    
    private String cachedServerVersion;
    
    public WSClient(JServer server) throws Exception {
    	this.server = server;
    	
    	URL url;
		try {
			url = new URL(server.getUrl());
		} catch (MalformedURLException e1) {
			throw new Exception(e1);
		}

		setWebservicesUri(url.toString());
    }
  

    /**
     * List all datasources. It returns a list of resourceDescriptors.
     */
    public java.util.List listDatasources() throws Exception {
        
        Request req = new Request();
                            
        req.setOperationName( Request.OPERATION_LIST);
        req.setResourceDescriptor( null );
        req.setLocale( getServer().getLocale() );
        
        req.getArguments().add(new Argument(Argument.LIST_DATASOURCES, Argument.VALUE_TRUE));

        StringWriter xmlStringWriter = new StringWriter();
        Marshaller.marshal(req, xmlStringWriter);

        return list( xmlStringWriter.toString());
    }
    
    /**
     * It returns a list of resourceDescriptors.
     */
    public java.util.List list(ResourceDescriptor descriptor) throws Exception
    {
        Request req = new Request();

        req.setOperationName( Request.OPERATION_LIST);
        req.setResourceDescriptor( descriptor);
        req.setLocale( getServer().getLocale() );

        StringWriter xmlStringWriter = new StringWriter();
        Marshaller.marshal(req, xmlStringWriter);
        
        return list(xmlStringWriter.toString());
    }
    
    /**
     * It returns a list of resourceDescriptors.
     */
    public String getVersion() throws Exception
    {
    	if (cachedServerVersion != null) {
    		return cachedServerVersion;
    	}
    	
        Request req = new Request();

        req.setOperationName( Request.OPERATION_LIST);
        req.setResourceDescriptor( null );
        req.setLocale( getServer().getLocale() );

        try {
            
            ManagementService ms = getManagementService();
            String reqXml = marshaller.marshal(req);
            //System.out.println("Executing list for version.." + new java.util.Date());
            //                System.out.flush();
            String result = ms.list(reqXml );
            //System.out.println("Finished list for version.." + new java.util.Date());
            //                System.out.flush();
            
            // In order to avoid problem with the classloading, forse a classloader for
            // the parting...
            OperationResult or = (OperationResult)unmarshal(result);
            
            if (or.getReturnCode() != 0) throw new Exception( or.getReturnCode() + " - " + or.getMessage() );
            
            cachedServerVersion = or.getVersion();
            return cachedServerVersion;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
        }  
    }
    
    
    /**
     * It returns a list of resourceDescriptors.
     */
    public java.util.List list(String xmlRequest) throws Exception {
    	
        try {
            
            String result = getManagementService().list( xmlRequest );
                        
            OperationResult or = (OperationResult)unmarshal(result);
            
            if (or.getReturnCode() != 0) throw new Exception( or.getReturnCode() + " - " + or.getMessage() );
            
            return or.getResourceDescriptors();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
        }    
    }
    
    
    public void delete(ResourceDescriptor descriptor) throws Exception
    {
        delete(descriptor, null);
    }
    /**
     * Delete a resource and its contents
     * Specify the reportUnitUri if you are deleting something inside this report unit.
     *
     */
    public void delete(ResourceDescriptor descriptor, String reportUnitUri) throws Exception {

        try {          
            Request req = new Request();
            req.setOperationName("delete");
            req.setResourceDescriptor( descriptor );
            req.setLocale( getServer().getLocale() );
            
            if (reportUnitUri != null && reportUnitUri.length() > 0)
            {
                req.getArguments().add(new Argument(Argument.MODIFY_REPORTUNIT, reportUnitUri ));
            }
            
            String result = getManagementService().delete( marshaller.marshal( req ) );
            
            OperationResult or = (OperationResult)unmarshal(result);
            
            if (or.getReturnCode() != 0) throw new Exception( or.getReturnCode() + " - " + or.getMessage() );
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
    
    /**
     * Export a resource using the "get" ws and save the resource in the file specified by
     * the user...
     * If the outputFile is null, the argument "NO_ATTACHMENT" is added to the request in order
     * of avoid the attachment trasmission.
     *
     */
    public ResourceDescriptor get(ResourceDescriptor descriptor, File outputFile) throws Exception
    {
        return get(descriptor, outputFile, null);
    }
    /**
     * Export a resource using the "get" ws and save the resource in the file specified by
     * the user...
     * If the outputFile is null, the argument "NO_ATTACHMENT" is added to the request in order
     * of avoid the attachment trasmission.
     *
     */
    public ResourceDescriptor get(ResourceDescriptor descriptor, File outputFile, java.util.List args) throws Exception
    {

        try {
            Request req = new Request();

            req.setOperationName("get");
            req.setResourceDescriptor( descriptor );
            req.setLocale( getServer().getLocale() );
            
            if (args != null)
            {
                for (int i=0; i<args.size(); ++i)
                {
                    Argument arg = (Argument)args.get(i);
                    req.getArguments().add(arg);
                }
            }
            
            if (outputFile == null)
            {
                req.getArguments().add( new Argument(Argument.NO_RESOURCE_DATA_ATTACHMENT, null) );
            }
                            
            String result = getManagementService().get( marshaller.marshal( req ) );

            OperationResult or = (OperationResult)unmarshal(result);
            
            if (or.getReturnCode() != 0) throw new Exception( or.getReturnCode() + " - " + or.getMessage() );
            
            Object[] resAtts = ((org.apache.axis.client.Stub)getManagementService()).getAttachments();
            if (resAtts != null && resAtts.length > 0 && outputFile != null)
            {
                java.io.InputStream is = ((org.apache.axis.attachments.AttachmentPart)resAtts[0]).getDataHandler().getInputStream();
                
                byte[] buffer = new byte[1024];
                OutputStream os = new FileOutputStream(outputFile);
                int bCount = 0;
                while ( (bCount = is.read(buffer)) > 0)
                {
                    os.write( buffer, 0, bCount);
                }
                is.close();
                os.close();
            }
            else if (outputFile != null)
            {
            	throw new Exception("Attachment not present!");
            }
            
            return (ResourceDescriptor)or.getResourceDescriptors().get(0);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
        
        }
        
    }
    
    public JasperPrint runReport(ResourceDescriptor descriptor, java.util.Map parameters) throws Exception {
    	List args = new ArrayList(1);
    	args.add(new Argument(Argument.RUN_OUTPUT_FORMAT, Argument.RUN_OUTPUT_FORMAT_JRPRINT));
    	Map attachments = runReport(descriptor, parameters, args);

    	FileContent content = null;
    	if (attachments != null && !attachments.isEmpty()) {
    	
    		content = (FileContent)(attachments.values().toArray()[0]);
    		//attachments.get("jasperPrint");
    	
    	}


    	if (content == null) {
    		throw new Exception("No JasperPrint");
    	}

    	InputStream is = new ByteArrayInputStream(content.getData());
    	
    	JasperPrint print = (JasperPrint) JRLoader.loadObject(is);
        return print;
    }
    
    /**
     * This method run a report. The return is an OperationResult.
     * If the result is succesfull, the message contains a set of strings
     * (one for each row) with the list of files attached complete of the
     * relative path. I.e.
     *
     * main_report.html
     * images/logo1.jpg
     * images/chartxyz.jpg 
     * 
     * Arguments:
     * 
     * 
     * 
     * The request must contains the descriptor of the report to execute
     * (only the URI is used).
     * Arguments can be attached to the descriptor as childs. Each argument
     * is a ListItem, with the parameter name as Name and the object 
     * rapresenting the value as Value.
     *
     * Operation result Codes:
     * 0 - Success
     * 1 - Generic error
     *  
     */
    public Map runReport(ResourceDescriptor descriptor, java.util.Map parameters, List args) throws Exception
    {

        try {
            Request req = new Request();
            req.setOperationName("runReport");
            req.setLocale( getServer().getLocale() );
            ResourceDescriptor newRUDescriptor = new ResourceDescriptor();
            newRUDescriptor.setUriString(descriptor.getUriString() );
            for (Iterator i= parameters.keySet().iterator(); i.hasNext() ;)
            {
                String key = ""+i.next();
                Object value = parameters.get( key );
                if (value instanceof java.util.Collection)
                {
                    Iterator cIter = ((Collection)value).iterator();
                    while (cIter.hasNext())
                    {
                        String item = ""+cIter.next();
                        ListItem l = new ListItem(key+"",item);
                        l.setIsListItem(true);
                        newRUDescriptor.getParameters().add( l );
                    }
                }
                else
                {
                    newRUDescriptor.getParameters().add( new ListItem(key+"",parameters.get( key )));
                }
            }
            
            req.setResourceDescriptor( newRUDescriptor );
            req.getArguments().addAll(args);
                            
            String result = getManagementService().runReport( marshaller.marshal( req ) );

            OperationResult or = (OperationResult)unmarshal(result);
            
            if (or.getReturnCode() != 0) throw new Exception( or.getReturnCode() + " - " + or.getMessage() );
            
            Map results = new HashMap();
            
            Object[] resAtts = ((org.apache.axis.client.Stub)getManagementService()).getAttachments();
            boolean attachFound = false;
            for (int i=0; resAtts != null &&  i< resAtts.length; ++i)
            {
                attachFound = true;
                DataHandler actualDH = (DataHandler)((org.apache.axis.attachments.AttachmentPart)resAtts[i]).getDataHandler();
                String name = actualDH.getName(); //  ((org.apache.axis.attachments.AttachmentPart)resAtts[i]).getAttachmentFile();
		String contentId = ((org.apache.axis.attachments.AttachmentPart)resAtts[i]).getContentId();
		if (name == null) name = "attachment-" + i;
		if (contentId == null) contentId = "attachment-" + i;
		

                InputStream is = actualDH.getInputStream();
        	ByteArrayOutputStream bos = new ByteArrayOutputStream();
        			
        	byte[] data = new byte[1000];
        	int bytesRead;
        		
        	while ((bytesRead = is.read(data)) != -1) {
        		bos.write(data, 0, bytesRead);
        	}

        	data = bos.toByteArray();
        			
	        String contentType = actualDH.getContentType();
	                
	    	FileContent content = new FileContent();
	    	content.setData(data);
	    	content.setMimeType(contentType);
	    	content.setName(name);

        	results.put(contentId, content);
	               
	   }
	   if (!attachFound)
           {
                throw new Exception("Attachment not present!");
           }

           return results;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
        	
        }  
        
    }
    
    
    /**
     * Add or Modify a resource. Return the updated ResourceDescriptor
     *
     */
    public ResourceDescriptor addOrModifyResource(ResourceDescriptor descriptor, File inputFile) throws Exception
    {
        return modifyReportUnitResource(null, descriptor, inputFile); 
    }
    
    public ResourceDescriptor putResource(ResourceDescriptor descriptor, RequestAttachment[] attachments) throws Exception
    {
    	return putReportUnitResource(null, descriptor, attachments);
    }
    
    public JServer getServer() {
        return server;
    }

    public void setServer(JServer server) {
        this.server = server;
    }

    public String getUsername() {
    	return getServer().getUsername();
    }

    public String getPassword() {
    	return getServer().getPassword();
    }

    public ResourceDescriptor modifyReportUnitResource(String reportUnitUri, ResourceDescriptor descriptor, File inputFile) throws Exception
    {
    	RequestAttachment[] attachments;
    	if (inputFile == null)
    	{
    		attachments = new RequestAttachment[0];
    	}
    	else 
    	{
                // patch jrxml files....
                //if (IReportManager.getPreferences().getBoolean("use_jrxml_DTD", false))
                //{
                //    if (inputFile.getName().toLowerCase().endsWith(".jrxml"))
                //    {
                //        inputFile = patchJRXML(inputFile);
                //    }
                //}

    		FileDataSource fileDataSource = new FileDataSource(inputFile);
    		RequestAttachment attachment = new RequestAttachment(fileDataSource);
			attachments = new RequestAttachment[]{attachment};
    	}
    	return putReportUnitResource(reportUnitUri, descriptor, attachments);
    }
    
    public ResourceDescriptor putReportUnitResource(String reportUnitUri, ResourceDescriptor descriptor, RequestAttachment[] attachments) throws Exception
    {

        try {
            Request req = new Request();
            req.setOperationName("put");
            req.setLocale( getServer().getLocale() );
            
            if (reportUnitUri != null && reportUnitUri.length() > 0)
            {
                req.getArguments().add(new Argument(Argument.MODIFY_REPORTUNIT, reportUnitUri ));
            }
            
            ManagementService ms = getManagementService();
            
            //ManagementServiceServiceLocator rsl = new ManagementServiceServiceLocator();
            //ManagementService ms = rsl.getrepository(new java.net.URL( getWebservicesUri() ) );
            //((org.apache.axis.client.Stub)ms).setUsername( getUsername() );
            //((org.apache.axis.client.Stub)ms).setPassword( getPassword() );
            //((org.apache.axis.client.Stub)ms).setMaintainSession( false ); 
            
            // attach the file...
            if (attachments != null && attachments.length > 0)
            {
            	descriptor.setHasData(true);

        		//Tell the stub that the message being formed also contains an attachment, and it is of type MIME encoding.
                
                if (IReportManager.getPreferences().getBoolean("jasperserver.useMIME", true))
                {
                    ((org.apache.axis.client.Stub)ms)._setProperty(Call.ATTACHMENT_ENCAPSULATION_FORMAT, Call.ATTACHMENT_ENCAPSULATION_FORMAT_MIME);
                }
                else
                {
                   ((org.apache.axis.client.Stub)ms)._setProperty(Call.ATTACHMENT_ENCAPSULATION_FORMAT, Call.ATTACHMENT_ENCAPSULATION_FORMAT_DIME); 
                }
                //

            	for (int i = 0; i < attachments.length; i++) {
			RequestAttachment attachment = attachments[i];
	                DataHandler attachmentHandler = new DataHandler(attachment.getDataSource());
            		AttachmentPart attachmentPart = new AttachmentPart(attachmentHandler);
	                if (attachment.getContentID() != null)
	                {
	                	attachmentPart.setContentId(attachment.getContentID());
	                }

	        		//Add the attachment to the message
	        		((org.apache.axis.client.Stub)ms).addAttachment(attachmentPart);
				}
            }
            
            req.setResourceDescriptor( descriptor );
            
            String result = ms.put( marshaller.marshal( req ) );

            OperationResult or = (OperationResult)unmarshal(result);
            
            if (or.getReturnCode() != 0) throw new Exception( or.getReturnCode() + " - " + or.getMessage() );
            
            return (ResourceDescriptor)or.getResourceDescriptors().get(0);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }  finally {
        	
        }
        
    }

    public void move(ResourceDescriptor resource, String destinationFolderURI) throws Exception {
        try {
            Request req = new Request();
            req.setOperationName("move");
            req.setResourceDescriptor(resource);
            req.setLocale(getServer().getLocale());
            req.getArguments().add(new Argument(Argument.DESTINATION_URI, destinationFolderURI));
            
            String result = getManagementService().move(marshaller.marshal(req));
            OperationResult or = (OperationResult)unmarshal(result);
            if (or.getReturnCode() != OperationResult.SUCCESS) {
            	throw new Exception(or.getReturnCode() + " - " + or.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public ResourceDescriptor copy(ResourceDescriptor resource, String destinationFolderURI) throws Exception {
        try {
            Request req = new Request();
            req.setOperationName("copy");
            req.setResourceDescriptor(resource);
            req.setLocale(getServer().getLocale());
            req.getArguments().add(new Argument(Argument.DESTINATION_URI, destinationFolderURI));
            
            String result = getManagementService().copy(marshaller.marshal(req));
            OperationResult or = (OperationResult)unmarshal(result);
            if (or.getReturnCode() != OperationResult.SUCCESS) {
            	throw new Exception(or.getReturnCode() + " - " + or.getMessage());
            }
            
            ResourceDescriptor copyDescriptor;
            List resultDescriptors = or.getResourceDescriptors();
            if (resultDescriptors == null || resultDescriptors.isEmpty()) {
            	copyDescriptor = null;
            } else {
            	copyDescriptor = (ResourceDescriptor) resultDescriptors.get(0);
            }
            return copyDescriptor;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
    
    public String getWebservicesUri() {
        return webservicesUri;
    }

    public void setWebservicesUri(String webservicesUri) {
        this.webservicesUri = webservicesUri;
    }

    public ManagementService getManagementService() throws Exception {
        
        if (managementService == null)
        {
            ManagementServiceServiceLocator rsl = new ManagementServiceServiceLocator(getEngineConfiguration());
            managementService = rsl.getrepository(new java.net.URL( getWebservicesUri() ) );
            ((org.apache.axis.client.Stub)managementService).setUsername( getUsername() );
            ((org.apache.axis.client.Stub)managementService).setPassword( getPassword() );
            ((org.apache.axis.client.Stub)managementService).setMaintainSession( true );
        }

        int timeout = IReportManager.getPreferences().getInt("client_timeout", 0) * 1000;
        if (timeout != ((org.apache.axis.client.Stub)managementService).getTimeout())
        {
            ((org.apache.axis.client.Stub)managementService).setTimeout(timeout);
        }
        return managementService;
    }
	
	protected EngineConfiguration getEngineConfiguration() { 
		try {
                    return new ResourceConfigurationProvider(AXIS_CONFIGURATION_RESOURCE);
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                return null;
	}

    public void setManagementService(ManagementService managementService) {
        this.managementService = managementService;
    }
    
    protected Object unmarshal(String xml) throws Exception
    {
        Object obj = null;
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(DOMParser.class.getClassLoader());
            obj = unmarshaller.unmarshal(xml);
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
        
        return obj;
            
    }

    private File patchJRXML(File inputFile) throws Exception {

        String content = "";
        FileInputStream is = new FileInputStream(inputFile);
        byte[] buffer = new byte[1024];
        int bcount = 0;
        while ( (bcount = is.read(buffer)) > 0)
        {
            content += new String(buffer,0,bcount);
        }
        is.close();

        if (content.indexOf("xmlns=\"http://jasperreports.sourceforge.net/jasperreports\"") > 0)
        {
            content = Misc.string_replace(
                        "<!DOCTYPE jasperReport PUBLIC \"-//JasperReports//DTD Report Design//EN\" \"http://jasperreports.sourceforge.net/dtds/jasperreport.dtd\">\n<jasperReport ",
                        "<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\"",
                        content);
            File newFile = new File( JasperServerManager.createTmpFileName(null, null));
            FileOutputStream os = new FileOutputStream(newFile);
            os.write( content.getBytes());
            os.close();
            return newFile;
        }

        return inputFile;
    }


    
}

