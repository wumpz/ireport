/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaspersoft.ireport.samples;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.samples.db.SampleDatabaseConnection;
import com.jaspersoft.ireport.samples.db.SampleDatabaseConnectionFactory;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.List;
import org.hsqldb.lib.FileUtil;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.Repository;
import org.openide.modules.InstalledFileLocator;
import org.openide.modules.ModuleInstall;
import org.openide.util.NbPreferences;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {

        // Adding HSQL DB drivers to iReport
        List<String> classpath = IReportManager.getInstance().getClasspath();
        File libDir = InstalledFileLocator.getDefault().locate("modules/ext", null, false);

        // find a jar called jasperreports-extensions-*.jar
        if (libDir != null && libDir.isDirectory())
        {
            File[] jars = libDir.listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    if (name.toLowerCase().startsWith("hsqldb-") &&
                        name.toLowerCase().endsWith(".jar"))
                    {
                        return true;
                    }
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

        IReportManager.getInstance().addConnectionImplementationFactory(new SampleDatabaseConnectionFactory());

        // Add the database connection (only once)
        if (!NbPreferences.forModule(Installer.class).getBoolean("samples.testConnectionCreated", false))
        {
            SampleDatabaseConnection connection = new SampleDatabaseConnection();
            IReportManager.getInstance().addConnection(connection);

            NbPreferences.forModule(Installer.class).putBoolean("samples.testConnectionCreated", true);
        }

        
        
    }
}
