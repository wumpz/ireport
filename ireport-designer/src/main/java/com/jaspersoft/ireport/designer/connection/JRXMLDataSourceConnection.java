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
import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.connection.gui.JRXMLDataSourceConnectionEditor;
import com.jaspersoft.ireport.designer.utils.Misc;
import javax.swing.*;

import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import java.io.File;
import org.w3c.dom.Document;
import net.sf.jasperreports.engine.util.JRXmlUtils;
import java.util.Locale;
import java.util.TimeZone;
/**
 *
 * @author  Administrator
 */
public class JRXMLDataSourceConnection extends IReportConnection {
    
    private String name;
    
    private String filename;
    
    private String selectExpression;
    
    private boolean useConnection = false;
    
    private Locale locale = null;
    private String datePattern = null;
    private String numberPattern = null;
    private TimeZone timeZone = null;
    
    /** Creates a new instance of JDBCConnection */   
    public JRXMLDataSourceConnection() {
    }
    
    /**  This method return an instanced connection to the database.
     *  If isJDBCConnection() return false => getConnection() return null
     *
     */
    public java.sql.Connection getConnection() {       
            return null;
    }
    
    public boolean isJDBCConnection() {
        return false;
    }
    
    public boolean isJRDataSource() {
        return !isUseConnection();
    }
    
    /*
     *  This method return all properties used by this connection
     */
    @Override
    @SuppressWarnings("unchecked")
    public java.util.HashMap getProperties()
    {    
        java.util.HashMap map = new java.util.HashMap();
        map.put("Filename", Misc.nvl(this.getFilename() ,"") );  
        map.put("SelectExpression", Misc.nvl(this.getSelectExpression() ,"") ); 
        map.put("UseConnection",  Misc.nvl(""+this.isUseConnection() ,"false") ); 
        
        if (getLocale() != null )
        {
            map.put("Locale_language", Misc.nvl(getLocale().getLanguage(),"") );
            map.put("Locale_country", Misc.nvl(getLocale().getCountry(),"") );
            map.put("Locale_variant", Misc.nvl(getLocale().getVariant(),"") );
        }
        
        map.put("DatePattern",  Misc.nvl(this.getDatePattern(),"") ); 
        map.put("NumberPattern",   Misc.nvl(this.getNumberPattern(),"") ); 
        
        if (getTimeZone() != null)
        {
            map.put("timeZone",  Misc.nvl(this.getTimeZone().getID(),"") ); 
        }
        
        return map;
    }
    
    public void loadProperties(java.util.HashMap map)
    {
        this.setFilename( (String)map.get("Filename"));
        this.setSelectExpression( (String)map.get("SelectExpression"));
        this.setUseConnection( Boolean.valueOf( Misc.nvl(map.get("UseConnection"),"false") ).booleanValue() );
        
        String language = (String)map.get("Locale_language");
        String country = (String)map.get("Locale_country");
        String variant = (String)map.get("Locale_variant");
        
        if (language != null && language.trim().length() > 0)
        {
            if (country != null && country.trim().length() > 0)
            {
                if (variant != null && variant.trim().length() > 0)
                {
                    setLocale( new Locale(language, country, variant));
                }
                else
                {
                    setLocale( new Locale(language, country));
                }
            }
            else
            {
                setLocale( new Locale(language));
            }
        }
        
        String datePatternValue = (String)map.get("DatePattern");
        if (datePatternValue != null && datePatternValue.trim().length() > 0)
        {
            this.setDatePattern(datePatternValue);
        }
        
        String numberPatternValue = (String)map.get("NumberPattern");
        if (numberPatternValue != null && numberPatternValue.trim().length() > 0)
        {
            this.setNumberPattern(numberPatternValue);
        }
        
        String timezoneId = (String)map.get("timeZone");
        if (timezoneId != null && timezoneId.trim().length() > 0)
        {
            this.setTimeZone( TimeZone.getTimeZone(timezoneId) );
        }
        
    }
    
    
    /**
     * Getter for property filename.
     * @return Value of property filename.
     */
    public java.lang.String getFilename() {
        return filename;
    }    
   
    /**
     * Setter for property filename.
     * @param filename New value of property filename.
     */
    public void setFilename(java.lang.String filename) {
        this.filename = filename;
    }    
    
    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public java.lang.String getName() {
        return name;
    }
    
    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    /**
     *  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     */
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource() { 
        try  {
            
           net.sf.jasperreports.engine.data.JRXmlDataSource ds = new net.sf.jasperreports.engine.data.JRXmlDataSource(filename, getSelectExpression() ); 
           
           if (getLocale()!=null)
           {
               ds.setLocale( getLocale());
           }
           
           if (getTimeZone() != null)
           {
               ds.setTimeZone( getTimeZone());
           }
           
           if (getDatePattern() != null && getDatePattern().trim().length()>0)
           {
               ds.setDatePattern( getDatePattern());
           }
           
           if (getNumberPattern() != null && getNumberPattern().trim().length()>0)
           {
               ds.setNumberPattern( getNumberPattern());
           }
           
           return ds; 
        } catch (Exception ex){}
        return null;
    }

    public String getSelectExpression() {
        return selectExpression;
    }

    public void setSelectExpression(String selectExpression) {
        this.selectExpression = selectExpression;
    }

    public boolean isUseConnection() {
        return useConnection;
    }

    public void setUseConnection(boolean useConnection) {
        this.useConnection = useConnection;
    }

    
    /**
     * This method is call before the datasource is used and permit to add special parameters to the map
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public java.util.Map getSpecialParameters(java.util.Map map) throws net.sf.jasperreports.engine.JRException
    {
        if (isUseConnection())
        {
            
            /*
            if (this.getFilename().toLowerCase().startsWith("https://") ||
                this.getFilename().toLowerCase().startsWith("http://") ||
                this.getFilename().toLowerCase().startsWith("file:"))
            {
                map.put(JRXPathQueryExecuterFactory.XML_URL, this.getFilename());
            }
            else
            {
            */
                Document document = JRXmlUtils.parse(new File( this.getFilename()));
                map.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);
            //}
            
            
            if (getLocale()!=null)
           {
               map.put(JRXPathQueryExecuterFactory.XML_LOCALE, getLocale());
           }
           
           if (getTimeZone() != null)
           {
               map.put(JRXPathQueryExecuterFactory.XML_TIME_ZONE, getTimeZone());
           }
           
           if (getDatePattern() != null && getDatePattern().trim().length()>0)
           {
               map.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, getDatePattern());
           }
           
           if (getNumberPattern() != null && getNumberPattern().trim().length()>0)
           {
               map.put(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, getNumberPattern());
           }
            
        }
        return map;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    public String getNumberPattern() {
        return numberPattern;
    }

    public void setNumberPattern(String numberPattern) {
        this.numberPattern = numberPattern;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
    
     public String getDescription(){ return "XML file datasource"; } //I18n.getString("connectionType.xml",
     
     public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new JRXMLDataSourceConnectionEditor();
    }
     
    @Override
    public void test() throws Exception
    {
        
            
            try {
                
                java.io.File f = new java.io.File(this.getFilename());
                if (!f.exists())
                {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),
                            Misc.formatString( //"messages.connectionDialog.fileNotFound",
                            "File {0} not found", new Object[]{this.getFilename()}),
                            "Error",JOptionPane.ERROR_MESSAGE);
                    throw new Exception();	
                }
                
                JOptionPane.showMessageDialog(Misc.getMainWindow(),
                        //I18n.getString("messages.connectionDialog.connectionTestSuccessful",
                        "Connection test successful!","",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            catch (Exception ex)
            {
                //JOptionPane.showMessageDialog(MainFrame.getMainInstance(),ex.getMessage(),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
		//ex.printStackTrace();
                throw ex;
            }
    }
}

