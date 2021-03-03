/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.samples.db;

import com.jaspersoft.ireport.designer.IReportConnectionEditor;
import com.jaspersoft.ireport.designer.connection.JDBCConnection;
import java.sql.Connection;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;

/**
 *
 * @version $Id: SampleDatabaseConnection.java 0 2010-01-11 16:20:39 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 * This class does not override the get
 *
 */
public class SampleDatabaseConnection extends JDBCConnection {

    public SampleDatabaseConnection()
    {
        super();
        setName(NbBundle.getMessage(SampleDatabaseConnection.class , "connectionName"));
        setJDBCDriver("org.hsqldb.jdbcDriver");
        setUsername("sa");
        setSavePassword(true);
        setPassword("");
        setUrl("jdbc:hsqldb:hsql://127.0.0.1/");
    }

    @Override
    public String getDescription() {
        return NbBundle.getMessage(SampleDatabaseConnection.class , "connectionType");
    }

    @Override
    public IReportConnectionEditor getIReportConnectionEditor() {
        return new SampleDabataseConnectionEditor();
    }


    @Override
    public Connection getConnection() {

        System.out.println("Getting connection: server state " + RunSampleDatabaseAction.getServer().getState());
        System.out.flush();
        //if (RunSampleDatabaseAction.getServer().getState() != 1)
        //{
            RunSampleDatabaseAction.getServer().start();
        //}
        
        return super.getConnection();
    }
}
