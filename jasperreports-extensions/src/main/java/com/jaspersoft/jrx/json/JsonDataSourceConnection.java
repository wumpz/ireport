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
package com.jaspersoft.jrx.json;
import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.IReportConnectionEditor;
import com.jaspersoft.ireport.designer.utils.*;
import com.jaspersoft.ireport.locale.I18n;
import java.io.File;
import java.io.FileInputStream;
import javax.swing.*;

import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.TimeZone;
import net.sf.jasperreports.engine.query.JsonQueryExecuterFactory;
/**
 *
 * @author  Administrator
 */
public class JsonDataSourceConnection extends IReportConnection {
    
    private String name;
    
    private String filename;
    
    private String selectExpression;
    
    private boolean useConnection = false;
    
    private Locale locale = null;
    private String datePattern = null;
    private String numberPattern = null;
    private TimeZone timeZone = null;
    
    /** Creates a new instance of JDBCConnection */   
    public JsonDataSourceConnection() {
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
        return !isUseConnection();
    }
    
    /*
     *  This method return all properties used by this connection
     */
    @Override
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

    @Override
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
    @Override
    public java.lang.String getName() {
        return name;
    }
    
    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    @Override
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    /**
     *  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     */
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource() { 
        try  {
            
           net.sf.jasperreports.engine.data.JsonDataSource ds = new net.sf.jasperreports.engine.data.JsonDataSource(filename, getSelectExpression() );
           
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
    @Override
    public java.util.Map getSpecialParameters(java.util.Map map) throws net.sf.jasperreports.engine.JRException
    {
        if (isUseConnection())
        {
            map.put(JsonQueryExecuterFactory.JSON_SOURCE, this.getFilename());
            
            
            if (getLocale()!=null)
           {
               map.put(JsonQueryExecuterFactory.JSON_LOCALE, getLocale());
           }
           
           if (getTimeZone() != null)
           {
               map.put(JsonQueryExecuterFactory.JSON_TIME_ZONE, getTimeZone());
           }
           
           if (getDatePattern() != null && getDatePattern().trim().length()>0)
           {
               map.put(JsonQueryExecuterFactory.JSON_DATE_PATTERN, getDatePattern());
           }
           
           if (getNumberPattern() != null && getNumberPattern().trim().length()>0)
           {
               map.put(JsonQueryExecuterFactory.JSON_NUMBER_PATTERN, getNumberPattern());
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
    
     public String getDescription(){ return "JSON datasource"; }
     
    @Override
     public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new JsonDataSourceEditor();
    }
     
    @Override
    public void test() throws Exception
    {
            
            try {


                File f = new File(getFilename());
                
                if (!f.exists()) throw new Exception("File not found");
                JOptionPane.showMessageDialog(Misc.getMainFrame(),I18n.getString("messages.connectionDialog.connectionTestSuccessful"),"",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            catch (Exception ex)
            {
                //JOptionPane.showMessageDialog(Misc.getMainFrame(),ex.getMessage(),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
		//ex.printStackTrace();

                JOptionPane.showMessageDialog(Misc.getMainWindow(),ex.getMessage(),
                        "Error",JOptionPane.ERROR_MESSAGE);		

                throw ex;
            } finally {

            }
    }
}

