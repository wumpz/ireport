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
package com.jaspersoft.ireport.jasperserver.ws.scheduling;

import com.jaspersoft.jasperserver.ws.scheduling.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Locale;

import javax.xml.rpc.ServiceException;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.message.SOAPHeaderElement;

import com.jaspersoft.ireport.jasperserver.ws.util.ResourceConfigurationProvider;

/**
 * Facade for the report scheduling service stub.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: ReportSchedulerFacade.java 9930 2007-09-06 16:22:48Z lucian $
 */
public class ReportSchedulerFacade implements ReportScheduler {
	
	private static final String NAMESPACE = "http://www.jasperforge.org/jasperserver/ws";
	private static final String HEADER_LOCALE = "locale";
	
	private static final String AXIS_CONFIGURATION_RESOURCE = "/com/jaspersoft/jasperserver/ws/scheduling/client-config.wsdd";
	
	private final ReportSchedulerSoapBindingStub service;

	/**
	 * Creates and wraps a client stub for a given set of service connection attributes.
	 * 
	 * @param address the service endpoint URL
	 * @param user the username to be used to authenticate the service caller
	 * @param password the password to be used to authenticate the service caller
	 * @throws ServiceException if the client stub cannot be created
	 */
	public ReportSchedulerFacade(URL address, String user, String password) throws ServiceException {
		this.service = createServiceStub(address, user, password);
	}
	
	/**
	 * Wraps a client stub.
	 * 
	 * @param serviceStub the service stub
	 */
	public ReportSchedulerFacade(ReportSchedulerSoapBindingStub serviceStub) {
        this.service = serviceStub;
	}
	
	protected ReportSchedulerSoapBindingStub createServiceStub(URL address, String user, String password) throws ServiceException {
		ReportSchedulerServiceLocator locator = new ReportSchedulerServiceLocator(getEngineConfiguration());
		ReportSchedulerSoapBindingStub stub = (ReportSchedulerSoapBindingStub) locator.getReportScheduler(address);

		stub.setUsername(user);
		stub.setPassword(password);
		stub.setMaintainSession(true);

		return stub;
	}

	/**
	 * Sets the locale to be used when issuing request to the service.
	 * <p/>
	 * The locale is used to localize messages returned by the service.
	 * 
	 * @param locale
	 */
	public void setLocale(Locale locale) {
		setLocale(locale.toString());
	}
	
	protected void setLocale(String locale) {
        SOAPHeaderElement localeHeader = new SOAPHeaderElement(NAMESPACE, HEADER_LOCALE, locale);
        localeHeader.setActor(null);
        service.setHeader(localeHeader);
	}
	
	protected EngineConfiguration getEngineConfiguration() {
		return new ResourceConfigurationProvider(AXIS_CONFIGURATION_RESOURCE);
	}

	/**
	 * Deletes a report job.
	 * 
	 * @param id the ID of the job to delete
	 */
	public void deleteJob(long id) throws RemoteException {
		service.deleteJob(id);
	}

	/**
	 * Deletes several report jobs.
	 * 
	 * @param ids the IDs of the jobs to delete
	 */
	public void deleteJobs(long[] ids) throws RemoteException {
		service.deleteJobs(ids);
	}

	/**
	 * Retrieves a list containing all the report jobs.
	 * <p/>
	 * The list is filtered by the service to exclude jobs to which
	 * the current user does not have access.
	 * 
	 * @return the list of all accessible report jobs
	 */
	public JobSummary[] getAllJobs() throws RemoteException {
		return service.getAllJobs();
	}

	/**
	 * Retrieves the full details of a report job.
	 * 
	 * @param id the job ID
	 * @return on object containing all the attributes of a report job
	 */
	public Job getJob(long id) throws RemoteException {
		return processJob(service.getJob(id));
	}

	/**
	 * Retrieves a list of jobs of a specific report.
	 * <p/>
	 * The list is filtered by the service to exclude jobs to which
	 * the current user does not have access.
	 * 
	 * @param reportURI the URI of the report
	 * @return the list of accessible report jobs
	 */
	public JobSummary[] getReportJobs(String reportURI)
			throws RemoteException {
		return service.getReportJobs(reportURI);
	}

	/**
	 * Schedules a new report job.
	 * 
	 * @param job an object containing the attributes of
	 * the new job
	 * @return the saved job details
	 */
	public Job scheduleJob(Job job) throws RemoteException {
		return processJob(service.scheduleJob(job));
	}

	/**
	 * Updates an existing report job.
	 * 
	 * @param job an object containing all the attributes
	 * of the job to be updated
	 * @return the updated job details
	 */
	public Job updateJob(Job job) throws RemoteException {
		return processJob(service.updateJob(job));
	}
	
	protected Job processJob(Job job) {
		if (job != null) {
			processJobParameters(job);
		}
		return job;
	}

	protected void processJobParameters(Job job) {
		JobParameter[] parameters = job.getParameters();
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				JobParameter parameter = parameters[i];
				processJobParameter(parameter);
			}
		}
	}

	protected void processJobParameter(JobParameter parameter) {
		Object value = parameter.getValue();
		if (value instanceof Calendar) {
			parameter.setValue(((Calendar) value).getTime());
		}
	}
	
}
