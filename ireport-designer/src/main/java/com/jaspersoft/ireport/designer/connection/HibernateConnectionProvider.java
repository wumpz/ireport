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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;

/**
 *
 * @author gtoffoli
 */
public class HibernateConnectionProvider implements org.hibernate.connection.ConnectionProvider {

    Connection conn = null;
    int useCounter = 0;
    JDBCConnection irConnection = null;


    public void configure(Properties props) throws HibernateException {
        irConnection = new JDBCConnection();
        irConnection.setJDBCDriver( props.getProperty(Environment.DRIVER));
        irConnection.setUsername( props.getProperty(Environment.USER));
        irConnection.setPassword( props.getProperty(Environment.PASS));
        irConnection.setUrl( props.getProperty(Environment.URL));
    }

    public Connection getConnection() throws SQLException {
        try {

            if (conn == null || conn.isClosed())
            {
                conn = irConnection.getConnection();
                useCounter=1;
            }
        } catch (SQLException sqlEx)
        {
            throw sqlEx;
        } catch (Exception ex)
        {
            throw new SQLException(ex.getMessage());
        }
        return conn;
    }

    public void closeConnection(Connection c) throws SQLException {
        if (conn != null && useCounter==1)
        {
            conn.close();
        }
        useCounter--;

    }

    public void close() throws HibernateException {

    }

    public boolean supportsAggressiveRelease() {
        return false;
    }

}
