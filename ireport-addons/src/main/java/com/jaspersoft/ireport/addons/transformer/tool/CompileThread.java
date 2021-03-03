/*
 * Copyright (C) 2005 - 2008 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * CompileThread.java
 * 
 * Created on 19 maggio 2004, 19.52
 *
 */

package com.jaspersoft.ireport.addons.transformer.tool;

import com.jaspersoft.ireport.addons.transformer.BarcodeTransformer;
import com.jaspersoft.ireport.addons.transformer.Transformer;
import com.jaspersoft.ireport.designer.IRURLClassLoader;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ReportClassLoader;
import com.jaspersoft.ireport.designer.compatibility.JRXmlWriterHelper;
import com.jaspersoft.ireport.designer.utils.Misc;
import javax.swing.table.*;
import java.io.*;
import java.net.URL;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
/**
 *
 * @author  Administrator
 */
public class CompileThread implements Runnable {
    
    private TransformationFrame massiveCompilerFrame = null;
    private boolean stop = false;
    private Thread thread = null;
    ReportClassLoader reportClassLoader = null;
    private Transformer transformer = null;
    
    
    private boolean compileSelectedOnly = false;
    
    public CompileThread(TransformationFrame mcf)
    {
        this.massiveCompilerFrame = mcf;
        thread = new Thread(this);
    }
    
    public void stop()
    {
        stop = true;
    }
    
    public void start()
    {
        thread.start();
    }
    
    public void run() {
        if (massiveCompilerFrame == null)
        {
            return;
        }
        

        final DefaultTableModel dtm = (DefaultTableModel)massiveCompilerFrame.getFileTable().getModel();

        for (int i=0; i<dtm.getRowCount(); ++i)
        {
            final int index = i;
            final FileEntry fe = (FileEntry)dtm.getValueAt(i, 0);
            if ( isCompileSelectedOnly() &&  !massiveCompilerFrame.getFileTable().isRowSelected(i))
            {
                continue;
            }
            
            // Start to transform this report...
            String srcFileName = "";
               
            try
            {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {

                        public void run() {
                            try  {
                                fe.setStatus( FileEntry.STATUS_TRANSFORMING );
                                //DefaultTableModel dtm = (DefaultTableModel)massiveCompilerFrame.getFileTable().getModel();
                                dtm.setValueAt( fe, index, 0);
                            
                                dtm.setValueAt( fe.getFile().getCanonicalPath(), index, 1);
                                dtm.setValueAt( fe.decodeStatus( fe.getStatus()), index, 2);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
               } catch (Exception ex) {}
                
                srcFileName = fe.getFile().getCanonicalPath();
                
                try {
                        IRURLClassLoader cl = new IRURLClassLoader(new URL[]{ fe.getFile().toURI().toURL() } , IReportManager.getReportClassLoader());
                        Thread.currentThread().setContextClassLoader( cl );
                } catch (Exception ex) {

                    ex.printStackTrace();
                }
                

                JasperDesign jasperDesign = getTransformer().transform(srcFileName);


                if (jasperDesign != null)
                {
                    writeReport(jasperDesign, new File(srcFileName));
                }
                fe.setStatus( FileEntry.STATUS_TRANSFORMED );

            } catch (Exception ex)
            {
                fe.setStatus( FileEntry.STATUS_ERROR_TRANSFORMING );
                StringWriter sw = new StringWriter();
                ex.printStackTrace( new PrintWriter( sw ));
                fe.setMessage(  sw.getBuffer().toString() );
                fe.setShortErrorMessage(ex.getMessage() );

            }

            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        try {
                            dtm.setValueAt(fe, index, 0);
                            dtm.setValueAt(fe.getFile().getCanonicalPath(), index, 1);
                            dtm.setValueAt(fe.decodeStatus(fe.getStatus()), index, 2);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            } catch (Exception ex) {

            }
        }


        try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {

                            massiveCompilerFrame.getFileTable().updateUI();
                            massiveCompilerFrame.finishedCompiling();

                    }
                });
            } catch (Exception ex) {}
        
    }
    
    /** Getter for property compileSelectedOnly.
     * @return Value of property compileSelectedOnly.
     *
     */
    public boolean isCompileSelectedOnly() {
        return compileSelectedOnly;
    }    
    
    /** Setter for property compileSelectedOnly.
     * @param compileSelectedOnly New value of property compileSelectedOnly.
     *
     */
    public void setCompileSelectedOnly(boolean compileSelectedOnly) {
        this.compileSelectedOnly = compileSelectedOnly;
    }

    /**
     * @return the transformer
     */
    public Transformer getTransformer() {
        return transformer;
    }

    /**
     * @param transformer the transformer to set
     */
    public void setTransformer(Transformer transformer) {
        this.transformer = transformer;
    }

    public static void writeReport(JasperDesign jd, File outputFile) throws java.lang.Exception
    {
        final String compatibility = IReportManager.getPreferences().get("compatibility", "");

        String content = "";
        if (compatibility.length() == 0)
        {
            content = JRXmlWriter.writeReport(jd, "UTF-8"); // IReportManager.getInstance().getProperty("jrxmlEncoding", System.getProperty("file.encoding") ));
        }
        else
        {
            content = JRXmlWriterHelper.writeReport(jd, "UTF-8", compatibility);
        }

        Writer out = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8");
        out.write(content);
        out.close();
    }

}
