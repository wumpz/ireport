/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaspersoft.ireport.jasperserver;

import com.jaspersoft.ireport.designer.IRLocalJasperReportsContext;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.jasperserver.ws.IReportSSLSocketFactory;
import com.jaspersoft.ireport.jasperserver.ws.IReportTrustManager;
import org.apache.commons.httpclient.protocol.Protocol;
import org.openide.modules.ModuleInstall;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {

        IReportManager.getInstance().getFileResolvers().add(RepoImageCache.getInstance());
        IReportManager.getInstance().addCustomLinkType("ReportExecution", "ReportExecution");

        // Set a fake query executer for sl languages...
        if (IRLocalJasperReportsContext.getUtilities().getProperty("net.sf.jasperreports.query.executer.factory.sl") == null)
        {
            IReportManager.getInstance().setJRProperty("net.sf.jasperreports.query.executer.factory.sl", "net.sf.jasperreports.engine.query.JRJdbcQueryExecuterFactory");
        }


        if (IRLocalJasperReportsContext.getUtilities().getProperty("net.sf.jasperreports.query.executer.factory.domain") == null)
        {
            IReportManager.getInstance().setJRProperty("net.sf.jasperreports.query.executer.factory.domain", "net.sf.jasperreports.engine.query.JRJdbcQueryExecuterFactory");
        }


        try {
            IReportTrustManager tm = new IReportTrustManager();

            Protocol protocol = new Protocol("https", new IReportSSLSocketFactory(), 443);
            Protocol.registerProtocol("https", protocol);
        } catch (Exception e) {
            e.printStackTrace();
        }


        ClassLoader cl = IReportManager.getReportClassLoader();
        while (cl != null)
        {
            System.out.println(cl.getClass().getName());
            cl = cl.getParent();
        }
    }
}
