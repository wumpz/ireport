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
/**
 *
 * @author gtoffoli
 */
public interface IReportConnectionFactory {

    /**
     * Creates a new instance of IReportConnection
     * @return an instance of IReportConnection
     */
    public IReportConnection createConnection();

    /**
     * This method returns the class name of the IReportConnection implementation.
     * This is used from the code that must check if this connection factory
     * is the good one to instance the connection serialized with a specific class
     * name. Since due to the ClassLoading limitation iReport is not able to
     * instance the class by its self, it looks for the appropriate registered
     * IReportConnectionFactory
     * @return the class name of the IReportConnection implementation created by this factory class
     */
    public String getConnectionClassName();
}
