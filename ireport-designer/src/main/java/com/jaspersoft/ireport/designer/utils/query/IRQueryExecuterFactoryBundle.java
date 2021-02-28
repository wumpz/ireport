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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.data.queryexecuters.QueryExecuterDef;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactoryBundle;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRSingletonCache;
import net.sf.jasperreports.extensions.ExtensionsEnvironment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: EmptyQueryExecuterFactoryBundle.java 382 2012-04-11 12:29:56Z chicuslavic $
 */
public final class IRQueryExecuterFactoryBundle implements
		JRQueryExecuterFactoryBundle {
	private final Log log = LogFactory.getLog(IRQueryExecuterFactory.class);

	private static final JRSingletonCache cache = new JRSingletonCache(
			QueryExecuterFactory.class);

	private static final IRQueryExecuterFactoryBundle INSTANCE = new IRQueryExecuterFactoryBundle();

	private IRQueryExecuterFactoryBundle() {
	}

	/**
	 * 
	 */
	public static IRQueryExecuterFactoryBundle getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 */
	public String[] getLanguages() {
		Set<String> langs = new HashSet<String>();
		List<?> bundles = ExtensionsEnvironment.getExtensionsRegistry()
				.getExtensions(JRQueryExecuterFactoryBundle.class);
		for (Iterator<?> it = bundles.iterator(); it.hasNext();) {
			JRQueryExecuterFactoryBundle bundle = (JRQueryExecuterFactoryBundle) it
					.next();
			if (!bundle.getClass().equals(this.getClass())) {
				String[] l = bundle.getLanguages();
				for (int i = 0; i < l.length; i++) {
					langs.add(l[i]);
				}
			}
		}
		List<String> languages = new ArrayList<String>();
		languages.addAll(langs);
		return languages.toArray(new String[languages.size()]);
	}

	/**
	 * 
	 */
	public QueryExecuterFactory getQueryExecuterFactory(String language)
			throws JRException {
		List<?> bundles = ExtensionsEnvironment.getExtensionsRegistry()
				.getExtensions(JRQueryExecuterFactoryBundle.class);
		for (Iterator<?> it = bundles.iterator(); it.hasNext();) {
			JRQueryExecuterFactoryBundle bundle = (JRQueryExecuterFactoryBundle) it
					.next();
			if (!bundle.getClass().equals(this.getClass())) {
				QueryExecuterFactory factory = bundle
						.getQueryExecuterFactory(language);
				if (factory != null) {
					return factory;
				}
			}
		}

                // we have not found anything... let's see if iReport knows about it...
                //QueryExecuterDef
                List<QueryExecuterDef> qes = IReportManager.getInstance().getQueryExecuters();
                
                for (QueryExecuterDef qe : qes)
                {
                    if (qe.getLanguage().equals(language))
                    {
                        try {
                            return (QueryExecuterFactory)cache.getCachedInstance(qe.getClassName()); //QueryExecuterFactory)IReportManager.getJRExtensionsClassLoader().loadClass(qe.getClassName()).newInstance();
                        } catch (JRException ex) {
                            if (log.isWarnEnabled())
                                log.warn("Unable to instance the create a QueryExecuterFactory for the language  " + language + ": " + ex.getMessage());
                            
                            System.out.println("Unable to instance the create a QueryExecuterFactory for the language  " + language + ": " + ex.getMessage());
                            System.out.flush();
                        }
                    }
                }
                
		if (log.isWarnEnabled())
			log.warn("No query executer factory class registered for "
					+ language + " queries.");

		return (QueryExecuterFactory) cache
				.getCachedInstance(IRQueryExecuterFactory.class.getName());
	}

}
