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
package com.jaspersoft.ireport.designer.compiler;

import com.jaspersoft.ireport.designer.compiler.xml.SourceLocation;
import com.jaspersoft.ireport.designer.errorhandler.ProblemItem;
import net.sf.jasperreports.engine.JRExpression;
import org.eclipse.jdt.core.compiler.IProblem;

/**
 *
 * @author gtoffoli
 */
public class ErrorsCollector implements JasperReportErrorHandler {
    
    private java.util.List problemItems = null;
    /** Creates a new instance of ErrorsCollector */
    public ErrorsCollector() {
        setProblemItems(new java.util.ArrayList());
    }

    public void addMarker(Throwable e) {
        e.printStackTrace();
        addMarker( e.getMessage(), null);
    }

    @SuppressWarnings("unchecked")
    public void addMarker(String message, SourceLocation location) {
        if (location == null)
        {
            getProblemItems().add( new ProblemItem(ProblemItem.ERROR, message, location, null) );
        }
        else
        {
            getProblemItems().add( new ProblemItem(ProblemItem.ERROR, message, location, location.getXPath()) );
        }
    }

    public void addMarker(IProblem problem, SourceLocation location) {
        
        addMarker( problem.getMessage(), location);
    }
    
    public void addMarker(JRExpression expression, IProblem problem, SourceLocation location) {
        
        getProblemItems().add( new ProblemItem(ProblemItem.ERROR, problem.getMessage(), expression, location.getXPath()) );
    }

    public java.util.List getProblemItems() {
        return problemItems;
    }

    public void setProblemItems(java.util.List problemItems) {
        this.problemItems = problemItems;
    }

    public void addMarker(IProblem problem, JRExpression expression, SourceLocation location) {

        getProblemItems().add( new ProblemItem(ProblemItem.ERROR, problem.getMessage(), expression, (location==null) ? null : location.getXPath()) );
    }
    
}
