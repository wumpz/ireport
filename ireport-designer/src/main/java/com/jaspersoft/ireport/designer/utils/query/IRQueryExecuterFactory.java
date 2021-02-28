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
package com.jaspersoft.ireport.designer.utils.query;

import java.util.Map;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.query.JREmptyQueryExecuter;
import net.sf.jasperreports.engine.query.JRQueryExecuter;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;



/*
 * Empty query executer factory.
 * <p/>
 * The factory creates {@link net.sf.jasperreports.engine.query.JREmptyQueryExecuter JREmptyQueryExecuter}
 * query executers.
 * 
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: EmptyQueryExecuterFactory.java 382 2012-04-11 12:29:56Z chicuslavic $
 */
public class IRQueryExecuterFactory implements QueryExecuterFactory {

	public Object[] getBuiltinParameters() {
		return null;
	}

	public JRQueryExecuter createQueryExecuter(JRDataset dataset,
			Map<String, ? extends JRValueParameter> parameters)
			throws JRException {
		return new JREmptyQueryExecuter();
	}

	public boolean supportsQueryParameterType(String className) {
		return true;
	}

	public JRQueryExecuter createQueryExecuter(
			JasperReportsContext jasperReportsContext, JRDataset dataset,
			Map<String, ? extends JRValueParameter> parameters)
			throws JRException {
		return new JREmptyQueryExecuter();
	}
}
