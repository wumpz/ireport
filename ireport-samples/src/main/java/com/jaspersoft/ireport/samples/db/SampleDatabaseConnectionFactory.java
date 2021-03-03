/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.samples.db;

import com.jaspersoft.ireport.designer.IReportConnection;

/**
 *
 * @version $Id: SampleJDBCConnection.java 0 2010-01-11 16:14:45 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class SampleDatabaseConnectionFactory implements com.jaspersoft.ireport.designer.connection.IReportConnectionFactory {

    public IReportConnection createConnection() {

        return new SampleDatabaseConnection();

    }

    public String getConnectionClassName() {
        return SampleDatabaseConnection.class.getName();
    }

}
