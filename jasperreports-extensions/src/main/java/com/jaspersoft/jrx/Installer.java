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
package com.jaspersoft.jrx;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.data.queryexecuters.QueryExecuterDef;
import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.jrx.json.JsonDataSourceConnectionFactory;
import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.ResourceBundle;
import org.openide.modules.InstalledFileLocator;
import org.openide.modules.ModuleInstall;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {

        I18n.addBundleLocation(ResourceBundle.getBundle("/com/jaspersoft/jrx/Bundle"));

        // Add to the iReport classpath the jar with the
        // JRXPathQueryExecuter....

        List<String> classpath = IReportManager.getInstance().getClasspath();
        File libDir = InstalledFileLocator.getDefault().locate("modules/ext", null, false);

        // find a jar called jasperreports-extensions-*.jar
        if (libDir != null && libDir.isDirectory())
        {
            File[] jars = libDir.listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    if (name.toLowerCase().startsWith("jasperreports-extensions-") &&
                        name.toLowerCase().endsWith(".jar"))
                    {
                        return true;
                    }
//
//                    if (name.toLowerCase().startsWith("jasperreports-json") ||
//                        name.toLowerCase().endsWith(".jar"))
//                    {
//                        return true;
//                    }
                    return false;
                }
            });

            for (int i=0; i<jars.length; ++i)
            {
                if (classpath.contains(jars[i].getPath())) continue;
                classpath.add(jars[i].getPath());
            }
            IReportManager.getInstance().setClasspath(classpath);
        }

        // Plugging the new datasource implementation.
        IReportManager.getInstance().addConnectionImplementationFactory(new JRXMLDataSourceConnectionFactory());

        System.out.println("Adding JSON data source...");
        System.out.flush();
        IReportManager.getInstance().addConnectionImplementationFactory(new JsonDataSourceConnectionFactory());

        // adding the query executer for xpath2...
        QueryExecuterDef qed = new QueryExecuterDef("xpath2",
                "com.jaspersoft.jrx.query.JRXPathQueryExecuterFactory",
                "com.jaspersoft.ireport.designer.data.fieldsproviders.XMLFieldsProvider");
        
        IReportManager.getInstance().addQueryExecuterDef(qed, false);

        QueryExecuterDef qedPlSQL = new QueryExecuterDef("plsql",
                "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory",
                "com.jaspersoft.ireport.designer.data.fieldsproviders.SQLFieldsProvider");

        QueryExecuterDef qedjson = new QueryExecuterDef("json",
                "net.sf.jasperreports.engine.query.JsonQueryExecuterFactory",
                "com.jaspersoft.ireport.designer.data.fieldsproviders.SQLFieldsProvider");

        QueryExecuterDef qedJSON = new QueryExecuterDef("JSON",
                "net.sf.jasperreports.engine.query.JsonQueryExecuterFactory",
                "com.jaspersoft.ireport.designer.data.fieldsproviders.SQLFieldsProvider");

        IReportManager.getInstance().addQueryExecuterDef(qedjson, false);
        IReportManager.getInstance().addQueryExecuterDef(qedJSON, false);
        IReportManager.getInstance().addQueryExecuterDef(qedPlSQL, false);


        // Pluggin the new exporter...
        IReportManager.getInstance().getExporterFactories().add(new JRTxtExporterFactory());

    }
}
