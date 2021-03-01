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
package com.jaspersoft.ireport.jasperserver;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.InputControlQueryDataRow;
import java.util.List;
import java.util.Map;

/**
 * This is a simple interface to get data from a generic server
 * query.
 * This can be extended in the future.
 * @author gtoffoli
 */
public interface JSDataProvider {

    /**
     *
     * @param server
     * @param dsName
     * @param query
     * @param parameters
     * @return a list of InputControlQueryDataRow
     */
    public List<InputControlQueryDataRow> getData(JServer server, String dsName, String query, Map parameters);

    /**
     * Returns true if the specified language is supported.
     * 
     * @param language
     * @return
     */
    public boolean supports(String language);


}
