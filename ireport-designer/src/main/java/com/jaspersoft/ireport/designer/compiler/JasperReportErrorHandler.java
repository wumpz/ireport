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
import net.sf.jasperreports.engine.JRExpression;
import org.eclipse.jdt.core.compiler.IProblem;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JasperReportErrorHandler.java 23 2007-03-09 14:36:40Z lucianc $
 */
public interface JasperReportErrorHandler
{

	void addMarker(Throwable e);

	void addMarker(String message, SourceLocation location);

	void addMarker(IProblem problem, SourceLocation location);

       	void addMarker(IProblem problem, JRExpression expression, SourceLocation location);

}

