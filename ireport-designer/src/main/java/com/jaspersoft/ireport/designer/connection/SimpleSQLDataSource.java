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
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author gtoffoli
 */
public class SimpleSQLDataSource implements javax.sql.DataSource {

    private IReportConnection iRConnection = null;
    private PrintWriter pw = new PrintWriter(System.out);
    private int loginTimeout = 0;

    public SimpleSQLDataSource(IReportConnection c)
    {
        iRConnection = c;
    }
    
    public Connection getConnection() throws SQLException {
        return iRConnection.getConnection();
    }

    public Connection getConnection(String username, String password) throws SQLException {
        // Ignoring username and password
        return getConnection();
    }

    public PrintWriter getLogWriter() throws SQLException {
        return pw;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        pw = out;
    }

    public void setLoginTimeout(int seconds) throws SQLException {
        loginTimeout = seconds;
    }

    public int getLoginTimeout() throws SQLException {
        return loginTimeout;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    /**
     * @return the iRConnection
     */
    public IReportConnection getIRConnection() {
        return iRConnection;
    }

    /**
     * @param iRConnection the iRConnection to set
     */
    public void setIRConnection(IReportConnection iRConnection) {
        this.iRConnection = iRConnection;
    }

}
