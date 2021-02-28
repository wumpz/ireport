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

import com.jaspersoft.ireport.designer.IReportConnectionEditor;
import com.jaspersoft.ireport.designer.connection.gui.JREmptyDataSourceConnectionEditor;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.engine.JREmptyDataSource;

/**
 *
 * @author  Administrator
 */
public class JREmptyDatasourceConnection extends NullConnection {
    
    private int records = 1;
    
    /** Creates a new instance of JDBCConnection */
    public JREmptyDatasourceConnection() {
        this.setName(I18n.getString("JREmptyDatasourceConnection.Property.Title"));
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
        return true;
    }
    
   /*
     *  This method return all properties used by this connection
     */
    @SuppressWarnings("unchecked")
    @Override
    public java.util.HashMap getProperties()
    {    
        java.util.HashMap map = new java.util.HashMap();
        map.put("records", "" + this.getRecords() );
        return map;
    }
    
    @Override
    public void loadProperties(java.util.HashMap map)
    {
        this.setRecords( Integer.parseInt( Misc.nvl( (String)map.get("records"),"1") ) );
    }
    
    
    
    /**
     *  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     */
    @Override
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource()
    { 
        return new JREmptyDataSource(getRecords());
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }
    
    @Override
    public String getDescription(){ return "Empty data source"; } //I18n.getString("connectionType.emptyDataSource", 
    
    @Override
    public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new JREmptyDataSourceConnectionEditor();
    }
}
