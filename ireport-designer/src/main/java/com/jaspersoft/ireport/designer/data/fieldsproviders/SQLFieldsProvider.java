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
package com.jaspersoft.ireport.designer.data.fieldsproviders;

import com.jaspersoft.ireport.designer.FieldsProvider;
import com.jaspersoft.ireport.designer.FieldsProviderEditor;
import com.jaspersoft.ireport.designer.IRLocalJasperReportsContext;
import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.connection.JDBCNBConnection;
import com.jaspersoft.ireport.designer.data.ReportQueryDialog;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.query.JRJdbcQueryExecuter;

/**
 *
 * @author gtoffoli
 */
public class SQLFieldsProvider implements FieldsProvider {
    
    public static boolean useVisualDesigner = true;
    
//    static {
//        java.util.Properties p = new java.util.Properties();
//        try {
//            //java.io.InputStream is = SQLFieldsProvider.class.getClass().getResourceAsStream("/it/businesslogic/ireport/data/fieldsprovider.properties");
//            //java.io.InputStream is = SQLFieldsProvider.class.getResourceAsStream("/it/businesslogic/ireport/data/fieldsprovider.properties");
//            //p.load(  is );
//            //if (p.getProperty("sql").equals("0"))
//            //{
//            //    useVisualDesigner = false;
//            //}
//        } catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }
    
    
    /** Creates a new instance of SQLFieldsProvider */
    public SQLFieldsProvider() {
        
        
    }
    
    /**
     * Returns true if the provider supports the {@link #getFields(IReportConnection,JRDataset,Map) getFields} 
     * operation. By returning true in this method the data source provider indicates
     * that it is able to introspect the data source and discover the available fields.
     * 
     * @return true if the getFields() operation is supported.
     */
    public boolean supportsGetFieldsOperation() {
        return true;
    }

    @SuppressWarnings("unchecked")
    public JRField[] getFields(IReportConnection irConn, JRDataset reportDataset, Map parameters) throws JRException, UnsupportedOperationException {
        
        String error_msg = "";
        Connection con = null;
        JRJdbcQueryExecuter qe = null;
        
        if (irConn == null || !irConn.isJDBCConnection()) {
             throw new JRException("The active connection is not of type JDBC. Activate a JDBC connection first.");
        }
        
        
        try {
        
            // JasperReports query executer instances requires
            // REPORT_PARAMETERS_MAP parameter to be defined and not null
            Map<String, JRValueParameter> tmpMap = convertMap(reportDataset, parameters);
            
            
            con = irConn.getConnection();
            
            tmpMap.put(JRParameter.REPORT_CONNECTION, new SimpleValueParameter(con));

            tmpMap.put(JRParameter.REPORT_PARAMETERS_MAP,
                            new SimpleValueParameter(
                                            new HashMap<String, JRValueParameter>()));
            
            tmpMap.put(JRParameter.REPORT_MAX_COUNT,
                            new SimpleValueParameter(null));

            qe = new JRJdbcQueryExecuter(IRLocalJasperReportsContext.getInstance(), reportDataset,tmpMap);
            qe.createDatasource();
            ResultSet rs = qe.getResultSet();
            if (rs != null) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int cc = metaData.getColumnCount();
                    Set<String> colset = new HashSet<String>();
                    List<JRDesignField> columns = new ArrayList<JRDesignField>(cc);
                    for (int i = 1; i <= cc; i++) {
                            String name = metaData.getColumnLabel(i);
                            if (colset.contains(name))
                                    continue;
                            colset.add(name);
                            JRDesignField field = new JRDesignField();
                            field.setName(name);

                            field.setValueClassName(getJdbcTypeClass(metaData, i));
                            try {
                                    String catalog = metaData.getCatalogName(i);
                                    String schema = metaData.getSchemaName(i);
                                    String table = metaData.getTableName(i);
                                    ResultSet rsmc = con.getMetaData().getColumns(catalog,
                                                    schema, table, name);
                                    while (rsmc.next()) {
                                            field.setDescription(rsmc.getString("REMARKS"));
                                            break;
                                    }
                            } catch (SQLException se) {
                                    se.printStackTrace();
                            }
                            columns.add(field);
                    }
                    return columns.toArray(new JRDesignField[columns.size()]);
            }
        } catch( IllegalArgumentException ie ) {
            throw new JRException( ie.getMessage() );
        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
            error_msg = "NoClassDefFoundError!!\nCheck your classpath!";
            throw new JRException( error_msg );
        } catch (java.sql.SQLException ex) {
            error_msg = "SQL problems:\n"+ex.getMessage();
            throw new JRException( error_msg );
        } catch (Exception ex) {
            
            
            if (ex.getCause() != null && ex.getCause() instanceof SQLException)
            {
                ex.getCause().printStackTrace();
                throw new JRException( ex.getCause()  );
            }
            
            ex.printStackTrace();
            error_msg = "General problem:\n"+ex.getMessage()+
                "\n\nCheck username and password; is the DBMS active ?!";
            throw new JRException( error_msg );
        } catch (Throwable t)
        {
            t.printStackTrace();
          throw new JRException( t.getMessage() );
        } finally {
            if(qe!=null) try { qe.close(); } catch(Exception e ) {}
            if(con !=null && !(irConn instanceof JDBCNBConnection)) try { con.close(); } catch(Exception e ) {}
        }
                        
        return new JRField[0];
    }
    
    	public static String getJdbcTypeClass(java.sql.ResultSetMetaData rsmd, int t) {
		try {
			return getJRFieldType(rsmd.getColumnClassName(t));
		} catch (SQLException ex) {
			// if getColumnClassName is not supported...
			try {
				int type = rsmd.getColumnType(t);
				switch (type) {
				case Types.CHAR:
				case Types.VARCHAR:
				case Types.LONGVARCHAR:
					return "java.lang.String";
				case Types.NUMERIC:
				case Types.DECIMAL:
					return "java.math.BigDecimal";
				case Types.BIT:
					return "java.lang.Boolean";
				case Types.TINYINT:
					return "java.lang.Byte";
				case Types.SMALLINT:
					return "java.lang.Short";
				case Types.INTEGER:
					return "java.lang.Integer";
				case Types.BIGINT:
					return "java.lang.Long";
				case Types.REAL:
					return "java.lang.Real";
				case Types.FLOAT:
				case Types.DOUBLE:
					return "java.lang.Double";
				case Types.BINARY:
				case Types.VARBINARY:
				case Types.LONGVARBINARY:
					return "java.lang.Byte[]";
				case Types.DATE:
					return "java.sql.Date";
				case Types.TIME:
					return "java.sql.Time";
				case Types.TIMESTAMP:
					return "java.sql.Timestamp";
				}
			} catch (SQLException ex2) {
				ex2.printStackTrace();
			}
		}
		return Object.class.getName();
	}

	public static String getJRFieldType(String type) {
		if (type == null)
			return Object.class.getName();
		if (type.equals(boolean.class.getName()))
			return Boolean.class.getName();
		if (type.equals(byte.class.getName()))
			return Byte.class.getName();
		if (type.equals(int.class.getName()))
			return Integer.class.getName();
		if (type.equals(long.class.getName()))
			return Long.class.getName();
		if (type.equals(double.class.getName()))
			return Double.class.getName();
		if (type.equals(float.class.getName()))
			return Float.class.getName();
		if (type.equals(short.class.getName()))
			return Short.class.getName();
		if (type.startsWith("["))
			return Object.class.getName();
		return type;
	}
        

    private Class findParameterClass(String paramName, JRDataset reportDataset)
    {
        JRParameter[] params = reportDataset.getParameters();
        for (JRParameter p : params)
        {
            if (p.getName().equals(paramName))
            {
                try {
                    return p.getValueClass();
                } catch (Throwable t) {}
            }
        }
        return null;
    }

    public boolean supportsAutomaticQueryExecution() {
        return true;
    }

    public boolean hasQueryDesigner() {
        return useVisualDesigner;
    }

    public boolean hasEditorComponent() {
        return true;
    }

    public String designQuery(IReportConnection con,  String query, ReportQueryDialog reportQueryDialog) throws JRException, UnsupportedOperationException {
        // Start FREE QUERY BUILDER....
            QueryBuilderDialog qbd = new QueryBuilderDialog( (reportQueryDialog != null) ? reportQueryDialog : new JDialog(), true);

            if (con.isJDBCConnection())
            {
                qbd.setConnection(  con.getConnection() );
            }
        
        try {
            
            if (query != null && query.length() > 0)
            {
                qbd.setQuery(query);
            }
        } catch (Throwable ex)
        {
            if (reportQueryDialog != null)
            {
                reportQueryDialog.getJLabelStatusSQL().setText("I'm sorry, I'm unable to parse the query...");
                ex.printStackTrace();
            }
            ex.printStackTrace();
            return null;
        }
        qbd.setVisible(true);
        
        if (qbd.getDialogResult() == JOptionPane.OK_OPTION)
        {
            return qbd.getQuery();
        }
        return null;
    }

    public FieldsProviderEditor getEditorComponent(ReportQueryDialog reportQueryDialog) {

        SQLFieldsProviderEditor dpe = new SQLFieldsProviderEditor();
        dpe.setReportQueryDialog( reportQueryDialog );
        return dpe;
    }
    
    
    public static Map<String, JRValueParameter> convertMap(JRDataset dataset, Map<String, ?> inmap) {
            Map<String, JRValueParameter> outmap = new HashMap<String, JRValueParameter>();
            for (String key : inmap.keySet())
            {
                    SimpleValueParameter svp = new SimpleValueParameter(inmap.get(key));
                    
                    // Let's set the correct class name for this parameter...
                    for (JRParameter p : dataset.getParameters() )
                    {
                        if (p.getName() != null && p.getName().equals(key))
                        {
                            svp.setValueClassName( p.getValueClassName());
                            break;
                        }
                    }
                    outmap.put(key, svp);
            }
            return outmap;
    }
    
}
