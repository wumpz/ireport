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
package com.jaspersoft.ireport.designer;

/**
 * A IReportConnectionEditor class provides a complete custom GUI for customizing a target IReportConnection.<br>
 * Each IReportConnectionEditor should inherit from the java.awt.Component class so it can be instantiated inside an AWT dialog or panel.<br>
 * Each IReportConnectionEditor should have a null constructor.<br>
 * 
 * @author gtoffoli
 */
public interface IReportConnectionEditor {
    
    /**
     * Set the IReportConnection to edit. Actually it is a copy of the original IReportConnection.
     * It can be modifed by the user interface.<br><br>
     * 
     * The copy of an IReportConnection is done instancing a new class of the same type and loading
     * the properties stored by the first object
     * @param c IReportConnection to edit
     */
    public void setIReportConnection(IReportConnection c);
    
    /**
     * This method is called when the user completes to edit the datasource or when a datasource test is required.
     * @return IReportConnection modified. IT can be the same instance get in input with setIReportConnection or a new one.
     */
    public IReportConnection getIReportConnection();
    
}
