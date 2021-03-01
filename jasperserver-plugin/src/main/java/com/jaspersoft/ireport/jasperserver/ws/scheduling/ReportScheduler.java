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

/**
 * ReportScheduler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */
public interface ReportScheduler extends java.rmi.Remote {
    public void deleteJob(long id) throws java.rmi.RemoteException;
    public void deleteJobs(long[] ids) throws java.rmi.RemoteException;
    public com.jaspersoft.jasperserver.ws.scheduling.Job getJob(long id) throws java.rmi.RemoteException;
    public com.jaspersoft.jasperserver.ws.scheduling.Job scheduleJob(com.jaspersoft.jasperserver.ws.scheduling.Job job) throws java.rmi.RemoteException;
    public com.jaspersoft.jasperserver.ws.scheduling.Job updateJob(com.jaspersoft.jasperserver.ws.scheduling.Job job) throws java.rmi.RemoteException;
    public com.jaspersoft.jasperserver.ws.scheduling.JobSummary[] getAllJobs() throws java.rmi.RemoteException;
    public com.jaspersoft.jasperserver.ws.scheduling.JobSummary[] getReportJobs(java.lang.String reportURI) throws java.rmi.RemoteException;
}
