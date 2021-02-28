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
package com.jaspersoft.ireport.designer.connection;

import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.IReportConnectionEditor;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.connection.gui.MondrianConnectionEditor;
import java.sql.Connection;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import mondrian.olap.DriverManager;
import mondrian.olap.Util;

/**
 *
 * @author  Administrator
 */
public class MondrianConnection extends IReportConnection {
    
    public static final String CATALOG_URI = "CatalogUri";
    public static final String CONNECTION_NAME = "ConnectionName";
    
    private java.util.HashMap map = null;
    
    private mondrian.olap.Connection mondrianConnection = null;
    
    private int usedby = 0;
    
    /** Creates a new instance of JDBCConnection */
    
    
    public MondrianConnection() {
        map = new java.util.HashMap();
    }
    
    /**  This method return an instanced connection to the database.
     *  If isJDBCConnection() return false => getConnection() return null
     *
     */
    @Override
    public java.sql.Connection getConnection() {       
            return null;
    }
    
    @Override
    public boolean isJDBCConnection() {
        return false;
    }
    
    @Override
    public boolean isJRDataSource() {
        return false;
    }
    
    /*
     *  This method return all properties used by this connection
     */
    @Override
    public java.util.HashMap getProperties()
    {    
        return map;
    }
    
    @Override
    public void loadProperties(java.util.HashMap map)
    {
        this.map = map;
    }
    
    
    
    /**
     *  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     */
    @Override
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource()
    { 
        return null;
    }
    
    
    public void closeMondrianConnection()
    {
        try {
            if (getMondrianConnection() != null)
            {
                usedby--;
                if (usedby == 0)
                {
                    mondrianConnection.close();
                    mondrianConnection = null;
                }
            }
        } catch (Exception ex)
        {
        }
    }   

    public mondrian.olap.Connection getMondrianConnection() throws Exception {
        
        if (mondrianConnection == null)
        {
            JDBCConnection con = getJDBCConnection();

            // Force opening connection...
            ClassLoader originalCL = Thread.currentThread().getContextClassLoader();

            
            try {

               Connection conn = null;
            try {
                    conn = con.getConnection();
                    if (conn == null) throw new Exception("No DB connection");
            } finally {
                // Clean up
                if( conn!=null ) try{ conn.close(); } catch(Exception e) { /* anyone really care? */ }
            }


            Thread.currentThread().setContextClassLoader( IReportManager.getReportClassLoader());
            Util.PropertyList props = new Util.PropertyList();
            props.put("Catalog", getCatalogUri());
            props.put("Provider", "mondrian");
            props.put("Locale", Locale.getDefault().getLanguage());

            SimpleSQLDataSource ds = new SimpleSQLDataSource(con);

            mondrianConnection = DriverManager.getConnection(props, null, ds);
            /*
            mondrianConnection  = DriverManager.getConnection(
					"Provider=mondrian;" + 
					"JdbcDrivers=" + escapeProperty( con.getJDBCDriver() )  + ";" +
					"Jdbc=" + escapeProperty( con.getUrl() ) + ";" +
					"JdbcUser=" + escapeProperty( con.getUsername() ) + ";" +
					"JdbcPassword=" + escapeProperty( con.getPassword() ) + ";" +
					"Catalog=" + escapeProperty( getCatalogUri() ) + ";",
					null, false);
            */
            
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }

            Thread.currentThread().setContextClassLoader( originalCL);

        }
        usedby++;
        return mondrianConnection;
    }

    public void setMondrianConnection(mondrian.olap.Connection mondrianConnection) {
        this.mondrianConnection = mondrianConnection;
    }

    public String getCatalogUri() {
        return (String)getProperties().get( CATALOG_URI );
    }

    @SuppressWarnings("unchecked")
    public void setCatalogUri(String catalogUri) {
        getProperties().put( CATALOG_URI, catalogUri);
    }

    public String getConnectionName() {
        return (String)getProperties().get( CONNECTION_NAME );
    }

    @SuppressWarnings("unchecked")
    public void setConnectionName(String connectionName) {
        getProperties().put( CONNECTION_NAME, connectionName);
    }
    
    
    private JDBCConnection getJDBCConnection()
    {
            String name = getConnectionName();
            List<IReportConnection> conns = IReportManager.getInstance().getConnections();
            for (IReportConnection con : conns)
            {
                if (con instanceof JDBCConnection &&
                    con.getName().equals(name))
                {
                    return (JDBCConnection)con;
                }
            }
            
            return null;
    }
    
    public String escapeProperty( String s)
    {
        if (s == null) s = "";
        s = Misc.string_replace("\"\"","\"",s);
    
        return s;
    }
    
    public String getDescription(){ return "Mondrian OLAP connection"; } //"connectionType.olap"
    
    @Override
    public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new MondrianConnectionEditor();
    }
    
    @Override
    public void test() throws Exception
    {
        try {
                SwingUtilities.invokeLater( new Runnable()
                {
                    public void run()
                    {
                        
                        
                        Thread.currentThread().setContextClassLoader( IReportManager.getInstance().getReportClassLoader() );
                        try {

                              getMondrianConnection();
                              closeMondrianConnection();
                              JOptionPane.showMessageDialog(Misc.getMainWindow(),
                                      //I18n.getString("messages.connectionDialog.connectionTestSuccessful",
                                      "Connection test successful!","",JOptionPane.INFORMATION_MESSAGE);

                        } catch (Exception ex)
                        {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(Misc.getMainWindow(),ex.getMessage(),
                                    "Error",JOptionPane.ERROR_MESSAGE);
                            return;					
                        } 
                        finally
                        {

                        }
                    }
                });
            } catch (Exception ex)
            {}
    }
}
