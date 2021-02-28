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
package com.jaspersoft.ireport.designer.errorhandler;

import com.jaspersoft.ireport.designer.compiler.xml.SourceLocation;
import net.sf.jasperreports.engine.design.JRValidationFault;

/**
 *
 * @author gtoffoli
 */
public class ProblemItem {
    
    public static final int INFORMATION = 0;
    public static final int WARNING = 1;
    public static final int ERROR = 2;
    
    
    private String description = null;
    private String where = "";
    private Object problemReference = null;
    private JRValidationFault fault = null;
    private SourceLocation sourceLocation = null;
    
    private int problemType = 1;
    
    /** Creates a new instance of ProblemItem */
    public ProblemItem() {
        this(0,"no description available", null);
    }
    
    /** Creates a new instance of ProblemItem */
    public ProblemItem(int problemType, JRValidationFault fault) {
        this.fault = fault;
        this.problemType = problemType;
        if (fault != null)
        {
            this.description = fault.getMessage();
        }
    }
    
    /** Creates a new instance of ProblemItem */
    public ProblemItem(int problemType, JRValidationFault fault, SourceLocation sl) {
        this.fault = fault;
        this.problemType = problemType;
        if (fault != null)
        {
            this.description = fault.getMessage();
        }
        this.sourceLocation = sl;
        if (sl != null)
        {
            this.where = sl.getXPath();
        }
    }

    
    /** Creates a new instance of ProblemItem */
    public ProblemItem(int problemType, String description, Object problemReference) {
        this(0,"no description available", null, "");
    }
    
    /** Creates a new instance of ProblemItem */
    public ProblemItem(int problemType, String description, Object problemReference, String where) {
        this.problemType = problemType;
        this.description = description;
        this.problemReference = problemReference;
        this.where = where;
    }
    

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getProblemReference() {
        if (fault != null) return fault.getSource();
        return problemReference;
    }

    public void setProblemReference(Object problemReference) {
        this.problemReference = problemReference;
    }

    public int getProblemType() {
        return problemType;
    }

    public void setProblemType(int problemType) {
        this.problemType = problemType;
    }
    
    public void resolve()
    {
        
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public JRValidationFault getFault() {
        return fault;
    }

    public void setFault(JRValidationFault fault) {
        this.fault = fault;
    }

    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(SourceLocation sourceLocation) {
        this.sourceLocation = sourceLocation;
    }
    
}
