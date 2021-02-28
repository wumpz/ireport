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

import com.jaspersoft.ireport.designer.connection.gui.BasicIReportConnectionEditor;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author  Administrator
 */
public abstract class IReportConnection {
    
  private String name="";
  /*
   * Return true if this connection is a "Connection" to a database
   * I.E. you can see JDBCConnection
   */
  public boolean isJDBCConnection(){ return false; }
  
  
  /*
   * Return true if this ireport connection can be used using getJRDataSource
   * I.E. you can see JDBCConnection
   */
  public boolean isJRDataSource() { return true; }
   
  /**
   * This method provides the name of the connection type. I.e.: JDBC connection
   */
  public abstract String getDescription();
  
  /**
    *  This method return an instanced connection to the database.
    *  If isJDBCConnection() return false => getConnection() return null
    */
  public java.sql.Connection getConnection(){ return null; }

  /**
     *  This method return an instanced JRDataDource to the database.
     *  It just call getJRDataSource(), but can be redefined in special
     *  connections.
     */
   public net.sf.jasperreports.engine.JRDataSource getJRDataSource(JasperReport jasper) {
         return getJRDataSource();
   }

    /**
     *  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     */
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource() { return new net.sf.jasperreports.engine.JREmptyDataSource(); }
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public java.lang.String getName() {
        return name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    
    /** All properties of an IReportConnection are stored in a XML file as Pair key/value
     *  This HashMap must contain all the properties that the IReportConnection must save in the
     *  XML.
     *  IReport will store the content of this HashMap in the XML. Please note that all the values
     *  and keys will be casted to String!
     */
    public java.util.HashMap getProperties()
    {
        return new java.util.HashMap();
    }
    
    /** All properties of a IReportConnection are stored in a XML file as Pair key/value
     *  This HashMap contains all the properties found for this IReportConnection in the
     *  XML. You must use this hashMap to initialize all attributes of your IReprotConnection
     */
    public void loadProperties(java.util.HashMap map)
    {
    }
    
    /** Redefine this method is not useful (and not raccomended)
     *  It just write a portion of XML for save properties a IReportConnection name
     */
    public void save(java.io.PrintWriter pw)
    {
        java.util.HashMap hm = this.getProperties();
        pw.println("\t<iReportConnection name=\""+ this.getName() +"\" connectionClass=\"" + this.getClass().getName() +"\">");
        java.util.Iterator iterator = hm.keySet().iterator();
        
        while (iterator.hasNext())
        {
            String key = (String)iterator.next();
            pw.println("\t\t<connectionParameter name=\""  +  key + "\"><![CDATA[" + hm.get(key) + "]]></connectionParameter>");
        }
        pw.println("\t</iReportConnection>");
    }    
    
    public String toString()
    {
        return getName();
    }
    
    /**
     * This method is call before the datasource is used and permit to add special parameters to the map
     *
     */
    public Map getSpecialParameters(Map map) throws net.sf.jasperreports.engine.JRException
    {
        return map;
    }
    
    /**
     * This method is call after the datasource is used to dispose special parameters
     * (i.e. closing an Hibernate session create as parameter with a getSpecialParameters...
     *
     */
    public Map disposeSpecialParameters(Map map)
    {
        return map;
    }
    
    
    /**
     * This method is used to test the configuration. To throw an exception if the test fails is not mandatory
     * and anyway the exception will be ignored.
     * The method is responsible to show error messages it the test has success or fails!!
     *
     */
    public void test() throws Exception  {
        //JOptionPane.showMessageDialog( Utilities. ,I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);
	return;
    }
    
    
    /**
     * This method is used to provide to the datasources window the GUI to configure this kind of component.
     * 
     *
     */
    public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new BasicIReportConnectionEditor();
    }


    public void  showErrorMessage(final String errorMsg, final String title)
    {
        Runnable r = new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),errorMsg,title,JOptionPane.ERROR_MESSAGE);
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
}

