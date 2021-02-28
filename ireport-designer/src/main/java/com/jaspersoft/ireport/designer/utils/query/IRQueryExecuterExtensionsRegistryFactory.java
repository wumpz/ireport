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

import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactoryBundle;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;

/*
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class IRQueryExecuterExtensionsRegistryFactory implements
		ExtensionsRegistryFactory {
	private static final ExtensionsRegistry defaultExtensionsRegistry = new ExtensionsRegistry() {
		public List getExtensions(Class extensionType) {
			if (JRQueryExecuterFactoryBundle.class.equals(extensionType)) {
				return Collections
						.singletonList(IRQueryExecuterFactoryBundle
								.getInstance());
			}
			return null;
		}
	};

	public ExtensionsRegistry createRegistry(String registryId,
			JRPropertiesMap properties) {
		return defaultExtensionsRegistry;
	}
}
