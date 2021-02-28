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
package com.jaspersoft.ireport.designer.data.queryexecuters;

/**
 *
 * @author  Administrator
 */
public class QueryExecuterDef {
    
    private String language="";
    private String className="";
    private String fieldsProvider="";

    private boolean builtin = false;

    public QueryExecuterDef(String language, String className, String fieldsProvider) {

            this(language, className, fieldsProvider, false);
    }
    /** Creates a new instance of JRProperty */
    public QueryExecuterDef(String language, String className, String fieldsProvider, boolean isBuiltin) {
        this.language = language;
        this.className = className;
        this.fieldsProvider = fieldsProvider;
        this.builtin = isBuiltin;
    }
    
    /** Creates a new instance of JRProperty */
    public QueryExecuterDef(String language, String className) {
        this(language , className, "");
    }
    
    /** Creates a new instance of JRProperty */
    public QueryExecuterDef(){
    }
   
    
    @Override
    public String toString()
    {
        return (getLanguage() == null) ? "" : getLanguage();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFieldsProvider() {
        return fieldsProvider;
    }

    public void setFieldsProvider(String fieldsProvider) {
        this.fieldsProvider = fieldsProvider;
    }

    /**
     * @return the builtin
     */
    public boolean isBuiltin() {
        return builtin;
    }

    /**
     * @param builtin the builtin to set
     */
    public void setBuiltin(boolean builtin) {
        this.builtin = builtin;
    }

    public QueryExecuterDef cloneMe()
    {
        QueryExecuterDef copy = new QueryExecuterDef();
        copy.setLanguage(this.language);
        copy.setBuiltin(builtin);
        copy.setClassName(className);
        copy.setFieldsProvider(fieldsProvider);

        return copy;
    }
}
