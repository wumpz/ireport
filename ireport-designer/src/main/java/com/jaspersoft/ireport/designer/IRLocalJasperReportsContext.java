/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2012 Jaspersoft Corporation. All rights reserved.
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

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.SimpleJasperReportsContext;
import net.sf.jasperreports.engine.util.LocalJasperReportsContext;

/**
 *
 * @author gtoffoli
 */


public class IRLocalJasperReportsContext extends LocalJasperReportsContext {
    
    static private Map<String, String> defaultProperties;
    static private IRLocalJasperReportsContext instance;
    static private JRPropertiesUtil utilities;
    
    static public IRLocalJasperReportsContext getInstance()
    {
        if (instance == null)
        {
            // The first time this class is loaded, we save a list of all the default properties...
            defaultProperties = new HashMap<String, String>();
            
            defaultProperties.putAll(DefaultJasperReportsContext.getInstance().getProperties());
            
            instance = new IRLocalJasperReportsContext();
        }
        
        return instance;
    }
    
    protected IRLocalJasperReportsContext()
    {
        super( DefaultJasperReportsContext.getInstance());
    }

    
    /**
     * Look inside the iReport preference before looking in the JR defaults...
     * 
     * @param key
     * @return 
     */
    @Override
    public String getProperty(String key) {
        String val = IReportManager.getPreferences().get(key, null);
        
        if (val == null)
        {
            val = IReportManager.getPreferences().get(IReportManager.PROPERTY_JRPROPERTY_PREFIX + key, null);
        }
        
        if (val == null)
        {
            return super.getProperty(key);
        }
        
        return val;
    }
    
    
    static public SimpleJasperReportsContext deriveContext()
    {
        SimpleJasperReportsContext co = new LocalJasperReportsContext(getInstance());
        return co;
    }
    
    
    static public JRPropertiesUtil getUtilities()
    {
        if (utilities == null)
        {
            utilities = JRPropertiesUtil.getInstance(getInstance());
        }
        
        return utilities;
    }

    @Override
    public Map<String, String> getProperties() {
        Map<String,String> map = super.getProperties();
    
        
        if (map == null)  map  = new HashMap<String, String>();
        
        Preferences pref = IReportManager.getPreferences();
        
        String[] keys;
        try {
            keys = pref.keys();
        
            for (String key : keys)
            {
                if (key.startsWith(  IReportManager.PROPERTY_JRPROPERTY_PREFIX))
                {
                    String kk = key.substring( IReportManager.PROPERTY_JRPROPERTY_PREFIX.length());
                    String val = pref.get(key, null);

                    if (val != null)
                    {
                        map.put(kk, pref.get(key, null));
                    }
                }
            }
        
        } catch (BackingStoreException ex) {
           // Exceptions.printStackTrace(ex);
        }
        
        return map;
    
    }
    
    
    
    /**
     * 
     * Returns true if the key is loaded by JasperReports default configuration
     * with the value specified...
     * 
     * @param key
     * @param value
     * @param keyOnly  - if true, the method checks only if the key is defined by JasperReports 
     * @return 
     */
    public static boolean isJasperReportsDefaultProperty(String key, String value, boolean keyOnly)
    {
        if  (defaultProperties == null) getInstance();
        
        if (defaultProperties.containsKey(key))
        {
            if (keyOnly) return true;
            
            String val =  defaultProperties.get(key);
            return val != null && val.equals(value);
        }
        return false;
    }

    
   
    
}
