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

/**
 *
 * @author gtoffoli
 */
public class CompilationStatusEvent {

    public static final int STATUS_UNDEFINED= -1;
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FAILED = 1;
    public static final int STATUS_COMPLETED = 2;

    private IReportCompiler compiler = null;

    public IReportCompiler getCompiler() {
        return compiler;
    }

    public void setCompiler(IReportCompiler compiler) {
        this.compiler = compiler;
    }
    private int status = STATUS_UNDEFINED;
    private String message = "";
    
    public CompilationStatusEvent(IReportCompiler compiler, int status, String message)
    {
        this.compiler = compiler;
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
}
