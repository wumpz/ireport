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
package com.jaspersoft.ireport.designer.compatibility;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.xml.JRXmlWriter;


/**
 *
 * @author gtoffoli
 */
public class JRXmlWriterHelper {

    private static final List<String> jrVersions = new ArrayList<String>();
    
    static {
        
        
        for (Field f : JRConstants.class.getFields())
        {
            if (f.getName().startsWith("VERSION_"))
                    try {
                            jrVersions.add((String) f.get(null));
                    } catch (Exception ex) {
                            ex.printStackTrace();
                    }
        }
    }
    
    public static List<String> getJRVersions()
    {
        return jrVersions;
        
    }
    
    
    private static VersionWarningDialog dialog = null;

    public static String writeReport(JRReport report, String encoding) throws Exception
    {
        
        // The user does not want to use compatibility...
        JRXmlWriter writer = new JRXmlWriter(new VersionJRContext(null));
        return writer.write(report, encoding);
    }
    
    
    public static String writeReport(JRReport report, String encoding, String version) throws Exception
    {
        JRXmlWriter writer = new JRXmlWriter(new VersionJRContext(version));
        if (IReportManager.getPreferences().getBoolean("show_compatibility_warning", true))
        {
            setDialog(null); // force the instance of a new dialog...

           getDialog().setVersion(version);

           if (SwingUtilities.isEventDispatchThread())
           {
               getDialog().setVisible(true);
           }
           else
           {
               SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        getDialog().setVisible(true);
                    }
                });
           }
           
           int res = getDialog().getDialogResult();

           if (!getDialog().askAgain())
           {
               IReportManager.getPreferences().putBoolean("show_compatibility_warning", false);
           }

           if (res == VersionWarningDialog.DIALOG_RESULT_USE_LAST_VERSION)
           {
               version = null;
           }

        }
        
        writer = new JRXmlWriter(new VersionJRContext(version));
        return writer.write(report, encoding);
        
    }

    /**
     * @return the dialog
     */
    public static VersionWarningDialog getDialog() {
        if (dialog == null)
        {
            // The operation Misc.getMainFrame() requires an AWT thread...
           Runnable run = new Runnable(){
                    public void run() {
                            setDialog(new VersionWarningDialog(Misc.getMainFrame(), true));
                        }
               };

               if (SwingUtilities.isEventDispatchThread())
               {
                   run.run();
               }
               else
               {
                try {
                    SwingUtilities.invokeAndWait(run);
                } catch (Exception ex) {
                }
               }
        }
        return dialog;
    }

    /**
     * @param dialog the dialog to set
     */
    public static void setDialog(VersionWarningDialog dlg) {
        dialog = dlg;
    }


}
