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
 * ReportSchedulerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */
public class ReportSchedulerServiceLocator extends org.apache.axis.client.Service implements com.jaspersoft.ireport.jasperserver.ws.scheduling.ReportSchedulerService {

    public ReportSchedulerServiceLocator() {
    }


    public ReportSchedulerServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ReportSchedulerServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ReportScheduler
    private java.lang.String ReportScheduler_address = "http://localhost:8880/jasperserver/services/ReportScheduler";

    public java.lang.String getReportSchedulerAddress() {
        return ReportScheduler_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ReportSchedulerWSDDServiceName = "ReportScheduler";

    public java.lang.String getReportSchedulerWSDDServiceName() {
        return ReportSchedulerWSDDServiceName;
    }

    public void setReportSchedulerWSDDServiceName(java.lang.String name) {
        ReportSchedulerWSDDServiceName = name;
    }

    public com.jaspersoft.ireport.jasperserver.ws.scheduling.ReportScheduler getReportScheduler() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ReportScheduler_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getReportScheduler(endpoint);
    }

    public com.jaspersoft.ireport.jasperserver.ws.scheduling.ReportScheduler getReportScheduler(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.jaspersoft.ireport.jasperserver.ws.scheduling.ReportSchedulerSoapBindingStub _stub = new com.jaspersoft.ireport.jasperserver.ws.scheduling.ReportSchedulerSoapBindingStub(portAddress, this);
            _stub.setPortName(getReportSchedulerWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setReportSchedulerEndpointAddress(java.lang.String address) {
        ReportScheduler_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.jaspersoft.ireport.jasperserver.ws.scheduling.ReportScheduler.class.isAssignableFrom(serviceEndpointInterface)) {
                com.jaspersoft.ireport.jasperserver.ws.scheduling.ReportSchedulerSoapBindingStub _stub = new com.jaspersoft.ireport.jasperserver.ws.scheduling.ReportSchedulerSoapBindingStub(new java.net.URL(ReportScheduler_address), this);
                _stub.setPortName(getReportSchedulerWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ReportScheduler".equals(inputPortName)) {
            return getReportScheduler();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.jasperforge.org/jasperserver/ws", "ReportSchedulerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.jasperforge.org/jasperserver/ws", "ReportScheduler"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ReportScheduler".equals(portName)) {
            setReportSchedulerEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
