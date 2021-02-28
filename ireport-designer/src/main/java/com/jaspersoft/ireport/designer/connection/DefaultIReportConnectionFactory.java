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
import com.jaspersoft.ireport.designer.IReportManager;

/**
 *
 * @author gtoffoli
 */
public class DefaultIReportConnectionFactory implements IReportConnectionFactory {

    private String connectionClassName = null;

    public DefaultIReportConnectionFactory(String connectionClassName)
    {
        this.connectionClassName = connectionClassName;
    }


    public IReportConnection createConnection() {

        try {
            IReportConnection c = (IReportConnection)Class.forName(connectionClassName,true, IReportManager.getReportClassLoader()).newInstance();
            return c;
        } catch (Throwable tw)
        {
            tw.printStackTrace();
        }
        return null;
    }

    /**
     * @return the connectionClassName
     */
    public String getConnectionClassName() {
        return connectionClassName;
    }

    /**
     * @param connectionClassName the connectionClassName to set
     */
    public void setConnectionClassName(String connectionClassName) {
        this.connectionClassName = connectionClassName;
    }



}
