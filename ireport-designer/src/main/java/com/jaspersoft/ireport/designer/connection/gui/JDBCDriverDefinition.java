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
package com.jaspersoft.ireport.designer.connection.gui;

import com.jaspersoft.ireport.designer.IReportManager;
import java.text.MessageFormat;
import java.util.Formatter;

/**
 *
 * @author gtoffoli
 */
public class JDBCDriverDefinition implements Comparable {
    private String urlPattern = "";
    private String dbName = "";
    private String driverName = "";
    private String defaultDBName = "MYDATABASE";

    public JDBCDriverDefinition(String dbName, String driverName, String urlPattern)
    {
        this(dbName, driverName, urlPattern, "MYDATABASE");
    }

    public JDBCDriverDefinition(String dbName, String driverName, String urlPattern, String defaultDBName)
    {
        this.dbName = dbName;
        this.driverName = driverName;
        this.urlPattern = urlPattern;
        this.defaultDBName = defaultDBName;
    }

    public String getUrl(String server, String database)
    {
        if (database == null || database.trim().length() == 0)
        {
            database = getDefaultDBName();
        }
        database = database.trim();
        return MessageFormat.format(getUrlPattern(), new Object[]{server,database});
    }

    public boolean isAvailable()
    {
        try {
            Class.forName(getDriverName(), false, IReportManager.getReportClassLoader());
        } catch (ClassNotFoundException ex)
        {
            return false;
        } catch (Throwable ex)
        {
            return false;
        }
        return true;
    }

    /**
     * @return the urlPattern
     */
    public String getUrlPattern() {
        return urlPattern;
    }

    /**
     * @param urlPattern the urlPattern to set
     */
    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    /**
     * @return the dbName
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * @param dbName the dbName to set
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * @return the driverName
     */
    public String getDriverName() {
        return driverName;
    }

    /**
     * @param driverName the driverName to set
     */
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    /**
     * @return the defaultDBName
     */
    public String getDefaultDBName() {
        return defaultDBName;
    }

    /**
     * @param defaultDBName the defaultDBName to set
     */
    public void setDefaultDBName(String defaultDBName) {
        this.defaultDBName = defaultDBName;
    }

    public String toString()
    {
        return dbName + " (" + driverName + ")";
    }

    public int compareTo(Object o) {
        return (o+"").compareTo(toString());
    }


}
