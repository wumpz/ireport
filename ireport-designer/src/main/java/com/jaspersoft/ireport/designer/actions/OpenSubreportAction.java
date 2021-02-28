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
package com.jaspersoft.ireport.designer.actions;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlEditorSupport;
import com.jaspersoft.ireport.designer.SubreportOpenerProvider;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.actions.NodeAction;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author gtoffoli
 */
public final class OpenSubreportAction extends NodeAction {

    public String getName() {
        return "Open Subreport";
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    protected void subreportNotFound(String msg)
    {
        // Display a message here...
        JOptionPane.showMessageDialog(Misc.getMainFrame(), "Unable to open the subreport:\n"+msg);
    }

    protected void performAction(org.openide.nodes.Node[] activatedNodes) {
        
        JasperDesign jasperDesign = ((ElementNode)activatedNodes[0]).getJasperDesign();

        JRDesignSubreport subreport = (JRDesignSubreport)((ElementNode)activatedNodes[0]).getElement();

        // Find the jrxml pointed by this subreport expression...

//        if (subreport.getExpression() == null ||
//                subreport.getExpression().getValueClassName() == null ||
//                !subreport.getExpression().getValueClassName().equals("java.lang.String"))
//        {
//           // Return default image...
//           // Unable to resolve the subreoport jrxml file...
//            subreportNotFound("The subreport expression is empty or it is not of type String.");
//            return;
//        }
//
//        JRDesignDataset dataset =  jasperDesign.getMainDesignDataset();
//        ClassLoader classLoader = IReportManager.getReportClassLoader();
//
//        File fileToOpen = null;
//        JrxmlEditorSupport es = IReportManager.getInstance().getActiveVisualView().getEditorSupport();
//
//
//        String error = null;
//        try {
//
//            // Try to process the expression...
//            ExpressionInterpreter interpreter = new ExpressionInterpreter(dataset, classLoader);
//            interpreter.setConvertNullParams(true);
//
//            Object ret = interpreter.interpretExpression( subreport.getExpression().getText() );
//
//            if (ret != null)
//            {
//                String resourceName = ret + "";
//                if (resourceName.toLowerCase().endsWith(".jasper"))
//                {
//                    resourceName = resourceName.substring(0, resourceName.length() -  ".jasper".length());
//                    resourceName += ".jrxml";
//                }
//
//                if (!resourceName.toLowerCase().endsWith(".jrxml"))
//                {
//                    throw new Exception("Unable to resolve the jrxml file for this subreport expression");
//                }
//
//                File f = new File(resourceName);
//                if (!f.exists())
//                {
//                    String jrxmlFileName = f.getName();
//                    File reportFolder = null;
//                    JrxmlVisualView visualView = IReportManager.getInstance().getActiveVisualView();
//                    if (visualView != null)
//                    {
//                        File file = FileUtil.toFile(visualView.getEditorSupport().getDataObject().getPrimaryFile());
//                        if (file.getParentFile() != null)
//                        {
//                            reportFolder = file.getParentFile();
//                        }
//                    }
//
//                    URL[] urls = new URL[]{};
//                    if (reportFolder != null)
//                    {
//                        urls = new URL[]{ reportFolder.toURI().toURL()};
//                    }
//                    IRURLClassLoader urlClassLoader = new IRURLClassLoader(urls, classLoader);
//
//                    URL url = urlClassLoader.getResource(resourceName);
//                    if (url == null)
//                    {
//                        // try just the file name...
//                        url = urlClassLoader.getResource(jrxmlFileName);
//
//                        if (url == null)
//                        {
//                            throw new Exception(resourceName + " not found.");
//                        }
//                    }
//
//                    f = new File(url.toURI().getPath());
//                    if (f.exists())
//                    {
//                        fileToOpen = f;
//                    }
//                    else
//                    {
//                        throw new Exception(f + " not found.");
//                    }
//                }
//                else
//                {
//                    fileToOpen = f;
//                }
//
//             }
//            else
//            {
//                throw new Exception();
//            }
//        } catch (Throwable ex) {
//
//            fileToOpen = null;
//            error = ex.getMessage();
//            ex.printStackTrace();
//        }
//
//
//        fileToOpen = notifySubreportProviders(es, subreport, fileToOpen);
//
//        if (fileToOpen != null)
//        {
//            try {
//                openFile(fileToOpen);
//            } catch (Throwable ex) {
//                error = ex.getMessage();
//                subreportNotFound(error);
//                ex.printStackTrace();
//            }
//        }
//        else
//        {
//            if (error == null)
//            {
//                error = "The subreport expression returned null. I'm unable to locate the subreport jrxml :-(";
//            }
//            subreportNotFound(error);
//        }
        
        try {
            JrxmlEditorSupport es = IReportManager.getInstance().getActiveVisualView().getEditorSupport();
            File fileToOpen = Misc.locateFileFromExpression(jasperDesign, null, (JRDesignExpression) subreport.getExpression(), null, ".jrxml", null);
            fileToOpen = notifySubreportProviders(es, subreport, fileToOpen);
            openFile(fileToOpen);
        } catch (Exception ex) {
            subreportNotFound(ex.getMessage());
        }
    }

    protected File notifySubreportProviders(JrxmlEditorSupport ed, JRDesignSubreport subreportElement, File file)
    {

        Lookup lookup = Lookups.forPath("ireport/SubreportOpenerProviders"); // NOI18N
        Collection<? extends SubreportOpenerProvider> subreportProviders = lookup.lookupAll(SubreportOpenerProvider.class);

        Iterator<? extends SubreportOpenerProvider> it = subreportProviders.iterator();
        while (it.hasNext ()) {

            SubreportOpenerProvider subreportOpenerProvider = it.next();

            try {
                File f = subreportOpenerProvider.openingSubreport(ed, subreportElement, file);
                if (f!=null)
                {
                    file = f;
                }

            } catch (Throwable t)
            {
                t.printStackTrace();
            }
        }

        return file;

    }
    
     
     
    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length != 1) return false;
        
        // Check we have selected a subreport
        if (activatedNodes[0] instanceof ElementNode &&
            ((ElementNode)activatedNodes[0]).getElement() instanceof JRDesignSubreport)
        {
            return true;
        }
        return false;
    }

    private void openFile(File f) throws Exception {

        DataObject obj;

        f = FileUtil.normalizeFile(f);
        FileObject fl = FileUtil.toFileObject(f);
        if (fl == null) throw new Exception("Unable to open the file " + f);
        obj = DataObject.find(fl);

        OpenCookie ocookie = obj.getCookie(OpenCookie.class);

        if (ocookie != null)
        {
            ocookie.open();
        }

    }
}
