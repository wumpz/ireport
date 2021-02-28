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
import com.jaspersoft.ireport.designer.connection.gui.JDBCConnectionEditor;
import com.jaspersoft.ireport.designer.IReportConnectionEditor;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.connection.gui.PasswordDialog;
import com.jaspersoft.ireport.designer.data.WizardFieldsProvider;
import com.jaspersoft.ireport.designer.data.fieldsproviders.SQLFieldsProvider;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
/**
 *
 * @author  Administrator
 */
public class JDBCConnection extends com.jaspersoft.ireport.designer.IReportConnection implements WizardFieldsProvider {

    private static final ArrayList<Driver> drivers = new ArrayList<Driver>();
    public static void loadDriver(String driverClass) throws ClassNotFoundException {

            if (driverClass == null) return;

            try {
                    Driver theDriver = (Driver)Class.forName(driverClass, false, IReportManager.getInstance().getReportClassLoader()).newInstance();
                    if (!isDriverLoaded(theDriver)) {
                            synchronized (drivers) {
                                    drivers.add(theDriver);
                            }
                    }
            } catch (Throwable t) {
                    throw new ClassNotFoundException(driverClass);
            }
    }

    private static boolean isDriverLoaded(Driver theDriver) {
		int version = (theDriver.getMajorVersion()*10) + theDriver.getMinorVersion();

		for (Driver driver : drivers) {
			int thisVersion = (driver.getMajorVersion()*10) + driver.getMinorVersion();
			if (thisVersion == version && (driver.getClass().getName().equals(theDriver.getClass().getName())))
				return true;
		}
		return false;
	}

    public static Driver getSuitableDriver(String jdbcURL) throws SQLException {

		for (Driver driver : drivers)
                {
			try {
				if (driver.acceptsURL(jdbcURL)) return driver;
			} catch (Throwable t) {
                            // Do nothing....
			}
                }
		return null;
	}

    private String JDBCDriver;
    
    private String username;
    
    private String password = null;
    
    private String url;
    
    private String database;
    
    private boolean savePassword;
    
    private String name;
    
    /**
     * Holds value of property serverAddress.
     */
    private String serverAddress;
    
    /** Creates a new instance of JDBCConnection */
    
    
    public JDBCConnection() {
    }
    
    /**  This method return an instanced connection to the database.
     *  If isJDBCConnection() return false => getConnection() return null
     *
     */
    @Override
    public java.sql.Connection getConnection() {
        
            // Try the java connection...
            try {
                
                    loadDriver( this.getJDBCDriver());
                    
                    java.sql.Driver driver = getSuitableDriver( url );
                    
                    java.util.Properties connectProps = new java.util.Properties();
                    
                    if ((password == null || password.equals("") ) && !isSavePassword())
                    {
                        password = getPassword();
                    }
                    
                    connectProps.setProperty("user", username);
                    connectProps.setProperty("password", password);
                    
                    Connection conn = driver.connect( url, connectProps); 
                    
                    if ( (this.getJDBCDriver().toLowerCase().indexOf("oracle") >= 0) && (IReportManager.getInstance().getProperty("oracle_language","").trim().length() > 0 ||
                        IReportManager.getInstance().getProperty("oracle_territory","").trim().length() > 0) )
                    {
	                    Statement stmt = null;
	                    try {
		                    stmt = conn.createStatement();
		                    if (IReportManager.getInstance().getProperty("oracle_language","").trim().length() > 0)
				    stmt.execute("ALTER SESSION SET NLS_LANGUAGE = '" + IReportManager.getInstance().getProperty("oracle_language","").trim() + "'");
				    if (IReportManager.getInstance().getProperty("oracle_territory","").trim().length() > 0)
				    stmt.execute("ALTER SESSION SET NLS_TERRITORY='" + IReportManager.getInstance().getProperty("oracle_territory","").trim() + "'");
				    
	                    } catch (Exception ex)
	                    {
	                    	ex.printStackTrace();
	        	    }
	        	    finally {
		        	    if (stmt != null) stmt.close();
	        	    }
                    }
        
                    return conn;
			
            }catch (NoClassDefFoundError ex)
		{
                    showErrorMessage(Misc.formatString(  // "messages.connection.noClassDefFoundError",
                                "{0}\nNoClassDefFoundError!!\nCheck your classpath!\n{1}",
                                new Object[]{""+ this.getName(), ""+ex.getMessage()}),
                                "Exception", ex); //"message.title.exception"
                    
		    return null;					
		} 
		catch (ClassNotFoundException ex)
		{
		    showErrorMessage(Misc.formatString( // "messages.connection.classNotFoundError",
                                "{0}\nClassNotFoundError!\nMsg: {1}\nPossible not found class: {2}\nCheck your classpath!",
                                new Object[]{""+ this.getName(), ""+ex.getMessage(), "" + this.getJDBCDriver()}),
                                "Exception", ex);
			return null;				
		} 
		catch (java.sql.SQLException ex)
		{
			if (!savePassword) password = null;
			showErrorMessage(Misc.formatString( // "messages.connection.sqlError",
                                "{0}\nSQL problems: {1}\n{2}",
                                new Object[]{""+ this.getName(), ""+ex.getMessage(), "" + url}),
                                "Exception", ex);
			return null;					
		} 
		catch (Exception ex)
		{
			showErrorMessage(Misc.formatString( // "messages.connection.generalError",
                                "{0}\nGeneral problem: {1}\nPlease check your username and password. The DBMS is running?!",
                                new Object[]{""+ this.getName(), ""+ex.getMessage()}),
                                "Exception", ex);
                        
                        ex.printStackTrace();
			return null;					
		}
    }    
    
    private void  showErrorMessage(String errorMsg, String title, Throwable theException)
    {
        
        final JXErrorPane pane = new JXErrorPane();
        //pane.setLocale(I18n.getCurrentLocale());
       
        String[] lines = errorMsg.split("\r\n|\n|\r");

        String shortMessage = errorMsg;
        if (lines.length > 4)
        {
            shortMessage = "";
            for (int i=0; i<4; ++i)
            {
                shortMessage += lines[i]+"\n";
            }
            shortMessage = shortMessage.trim() + "\n...";
        }
      
        final ErrorInfo ei = new ErrorInfo(title,
                 shortMessage,
                 null, //"<html><pre>" + errorMsg + "</pre>"
                 null,
                 theException,
                 null,
                 null);
         
        
        /*
        
        
        final String fErrorMsg = errorMsg;
        */
        Runnable r = new Runnable() {
                public void run() {
                   // JOptionPane.showMessageDialog(MainFrame.getMainInstance(),fErrorMsg,title,JOptionPane.ERROR_MESSAGE);
                
                    pane.setErrorInfo(ei);
                   JXErrorPane.showDialog(Misc.getMainWindow(), pane);
                }
            };

        if (!SwingUtilities.isEventDispatchThread())
        {
            try {
                SwingUtilities.invokeAndWait( r );
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        else
        {
                r.run();
        }
    }
    
    
    /*  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     *
     */
    @Override
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource() {
        return  new net.sf.jasperreports.engine.JREmptyDataSource();
    }
    
    @Override
    public boolean isJDBCConnection() {
        return true;
    }
    
    /** Getter for property database.
     * @return Value of property database.
     *
     */
    public java.lang.String getDatabase() {
        return database;
    }
    
    /** Setter for property database.
     * @param database New value of property database.
     *
     */
    public void setDatabase(java.lang.String database) {
        this.database = database;
    }
    
    /** Getter for property JDBCDriver.
     * @return Value of property JDBCDriver.
     *
     */
    public java.lang.String getJDBCDriver() {
        return JDBCDriver;
    }
    
    /** Setter for property JDBCDriver.
     * @param JDBCDriver New value of property JDBCDriver.
     *
     */
    public void setJDBCDriver(java.lang.String JDBCDriver) {
        this.JDBCDriver = JDBCDriver;
    }
    
    /** Getter for property password.
     * @return Value of property password.
     *
     */
    public java.lang.String getPassword() {
        
        if (isSavePassword()) return password;
        else
        {
            // Ask for password...
            try {
                return PasswordDialog.askPassword();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return "";
    }
    
    /** Setter for property password.
     * @param password New value of property password.
     *
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }
    
    /** Getter for property savePassword.
     * @return Value of property savePassword.
     *
     */
    public boolean isSavePassword() {
        return savePassword;
    }
    
    /** Setter for property savePassword.
     * @param savePassword New value of property savePassword.
     *
     */
    public void setSavePassword(boolean savePassword) {
        this.savePassword = savePassword;
    }
    
    /** Getter for property url.
     * @return Value of property url.
     *
     */
    public java.lang.String getUrl() {
        return url;
    }
    
    /** Setter for property url.
     * @param url New value of property url.
     *
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }
    
    /** Getter for property username.
     * @return Value of property username.
     *
     */
    public java.lang.String getUsername() {
        return username;
    }
    
    /** Setter for property username.
     * @param username New value of property username.
     *
     */
    public void setUsername(java.lang.String username) {
        this.username = username;
    }
    
    /*
     *  This method return all properties used by this connection
     */
    @SuppressWarnings("unchecked")
    @Override
    public java.util.HashMap getProperties()
    {    
        java.util.HashMap map = new java.util.HashMap();
        map.put("JDBCDriver", Misc.nvl(this.getJDBCDriver(),"") );
        map.put("Url", Misc.nvl(this.getUrl(),""));
        map.put("Database", Misc.nvl(this.getDatabase(),""));
        map.put("Username", Misc.nvl(this.getUsername(),""));
        if (this.isSavePassword())
            map.put("Password", Misc.nvl(this.getPassword(),""));
        else map.put("Password","");
        map.put("SavePassword", ""+this.isSavePassword());
        map.put("ServerAddress", Misc.nvl(this.getServerAddress(),"") );
        
        return map;
    }
    
    @Override
    public void loadProperties(java.util.HashMap map)
    {
        this.setJDBCDriver( (String)map.get("JDBCDriver"));
        this.setUrl( (String)map.get("Url"));
        this.setDatabase( (String)map.get("Database"));
        this.setUsername( (String)map.get("Username"));
        this.setSavePassword(  (""+map.get("SavePassword")).equals("true") );
        if (this.isSavePassword())
            this.setPassword( Misc.nvl((String)map.get("Password"),""));
        this.setServerAddress( Misc.nvl((String)map.get("ServerAddress"),"") );
        
    }
    
    /**
     * Getter for property serverAddress.
     * @return Value of property serverAddress.
     */
    public String getServerAddress() {
        return this.serverAddress;
    }
    
    /**
     * Setter for property serverAddress.
     * @param serverAddress New value of property serverAddress.
     */
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
    
    
    public String getDescription(){ return "Database JDBC connection"; } //"connectionType.jdbc"
    
    @Override
    public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new JDBCConnectionEditor();
    }
    
    
    @Override
    public void test() throws Exception
    {
        // Try the java connection...
        Connection conn = null;
        try {
                conn = getConnection();
                if (conn == null) throw new Exception("");
        } finally {
            // Clean up
            if( conn!=null ) try{ conn.close(); } catch(Exception e) { /* anyone really care? */ }
        }
        JOptionPane.showMessageDialog(Misc.getMainWindow(),"Connection test successful!","",JOptionPane.INFORMATION_MESSAGE); //"messages.connectionDialog.connectionTestSuccessful"
        return;
    }

    public String getQueryLanguage() {
        return "SQL";
    }

    public List<JRDesignField> readFields(String query) throws Exception {
        
        SQLFieldsProvider provider = new SQLFieldsProvider();
        List<JRDesignField> result = new ArrayList<JRDesignField>();
        JRDesignDataset dataset = new JRDesignDataset(true);
        JRDesignQuery dquery = new JRDesignQuery();
        dquery.setLanguage("SQL");
        dquery.setText(query);
        dataset.setQuery(dquery);
        JRField[] fields = provider.getFields(this, dataset, new HashMap());
        for (int i=0; i<fields.length; ++i)
        {
            result.add((JRDesignField)fields[i]);
        }
        
        return result;
    }
    
    
    public boolean supportsDesign() {
        return true;
    }

    public String designQuery(String query) {
        
        try {
            SQLFieldsProvider provider = new SQLFieldsProvider();
            return provider.designQuery(this, query, null);
        } catch (Exception ex)
        {
            return query;
        }
    }
}
