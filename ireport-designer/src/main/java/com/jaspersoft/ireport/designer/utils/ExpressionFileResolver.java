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
package com.jaspersoft.ireport.designer.utils;

import com.jaspersoft.ireport.designer.IRURLClassLoader;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import java.io.File;
import java.net.URL;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.FileResolver;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author gtoffoli
 */
public class ExpressionFileResolver implements FileResolver {

    private JRDesignExpression expression = null;
    private JasperDesign jasperDesign = null;
    private String reportFolder = null;
    private File file = null;
    private boolean resolveFile = true;
    private JRDesignDataset dataset = null;

    public ExpressionFileResolver(JRDesignExpression expression,
                                       JRDesignDataset dataset,
                                       JasperDesign jd)
    {
        this.expression = expression;
        this.jasperDesign = jd;
        this.dataset = dataset;
        if (dataset == null)
        {
            this.dataset = jd.getMainDesignDataset();
        }


        JrxmlVisualView visualView = IReportManager.getInstance().getActiveVisualView();
        if (visualView != null)
        {
            File reportFolderFile = FileUtil.toFile(visualView.getEditorSupport().getDataObject().getPrimaryFile());
            if (reportFolderFile.getParentFile() != null)
            {
                reportFolder = reportFolderFile.getParent();
            }
        }
    }

    public File resolveFile(String arg0) {
        if (resolveFile)
        {
            try {
                file = Misc.locateFileFromExpression(jasperDesign, null, expression, new File(reportFolder), null, null);
            } catch (Exception ex)
            {
                file = null;
            }

//            ClassLoader classLoader = IReportManager.getReportClassLoader();
//
//            try {
//
//                // Try to process the expression...
//                ExpressionInterpreter interpreter = new ExpressionInterpreter(dataset, classLoader);
//
//                Object ret = interpreter.interpretExpression( Misc.getExpressionText(expression) );
//
//                if (ret != null)
//                {
//                    String resourceName = ret + "";
//                    File f = new File(resourceName);
//                    if (!f.exists())
//                    {
//                        URL[] urls = new URL[]{};
//                        if (reportFolder != null)
//                        {
//                            urls = new URL[]{ (new File(reportFolder)).toURI().toURL()};
//                        }
//                        IRURLClassLoader urlClassLoader = new IRURLClassLoader(urls, classLoader);
//                        URL url = urlClassLoader.findResource(resourceName);
//                        if (url != null)
//                        {
//                            f = new File(url.getPath());
//                            if (f.exists())
//                            {
//                                file = f;
//                            }
//                        }
//                    }
//                    else
//                    {
//                        file = f;
//                    }
//
//                    resolveFile = false;
//                 }
//            } catch (Exception ex) {
//                resolveFile = false;
//                ex.printStackTrace();
//            }
        }
        return getFile();
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * @return the jasperDesign
     */
    public JasperDesign getJasperDesign() {
        return jasperDesign;
    }

    /**
     * @param jasperDesign the jasperDesign to set
     */
    public void setJasperDesign(JasperDesign jasperDesign) {
        if (this.jasperDesign != jasperDesign)
        {
            this.jasperDesign = jasperDesign;
            this.file = null;
            resolveFile = true;
        }
    }

    /**
     * @return the reportFolder
     */
    public String getReportFolder() {
        return reportFolder;
    }

    /**
     * @param reportFolder the reportFolder to set
     */
    public void setReportFolder(String reportFolder) {

        if (reportFolder == null && this.reportFolder == null) return;

        if ( (reportFolder != null && this.reportFolder == null) ||
             (reportFolder == null && this.reportFolder != null) ||
             !(reportFolder.equals(this.reportFolder)))
        {
            this.reportFolder = reportFolder;
            this.file = null;
            resolveFile = true;
        }
    }

}
