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
package com.jaspersoft.ireport.designer.errorhandler;


import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.ModelChangeListener;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JRValidationFault;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 * This class is tied to the JrxmlVisualView.
 * All the times there is a change in the model, the document is verified.
 * The verification starts 500 milliseconds after the change, so it can
 * collect other changed before to proceed...
 * The verification should be very fast since we are operating directly
 * on the model...
 * 
 * @author gtoffoli
 */
public class DesignVerifyerThread implements Runnable, ModelChangeListener {
    
    private boolean reportChanged = true;
    private Thread thisThread = null;
    private boolean stop = false;
    private JrxmlVisualView jrxmlVisualView = null;
    
    
    /** Creates a new instance of DesignVerifyerThread */
    public DesignVerifyerThread(JrxmlVisualView view) {
    
        this.jrxmlVisualView = view;
        // Listen for changes in the document...
        view.addModelChangeListener(this);
        thisThread = new Thread( this );
    }
    
    public void start()
    {
        thisThread.start();
    }
    
    public void stop()
    {
        setStop(true);
    }

    public void run() {
        
        while (!isStop())
        {
            try {
                Thread.sleep(2000);
            } catch (Exception ex)
            {
            }
        
            if (isReportChanged())    
            {
                setReportChanged(false);
                verifyDesign();
            }
        }
    }

    public boolean isReportChanged() {
        return reportChanged;
    }

    public void setReportChanged(boolean reportChanged) {
        this.reportChanged = reportChanged;
    }

    public void modelChanged(JrxmlVisualView view) {
        if (this.getJrxmlVisualView() == view)
        {
            setReportChanged(true);
        }
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
    
    
    public void verifyDesign()
    {
       // Remove all the WARNINGS...
        
        
       for (int i=0; i<getJrxmlVisualView().getReportProblems().size(); ++i)
       {
           ProblemItem pii = getJrxmlVisualView().getReportProblems().get(i);
           if (pii.getProblemType() == ProblemItem.WARNING ||
               pii.getProblemType() == ProblemItem.INFORMATION)
           {
               getJrxmlVisualView().getReportProblems().remove(i);
               i--;
           }
       }
       //getJReportFrame().getReportProblems().clear();
       
       try {
            //SourceTraceDigester digester = IReportCompiler.createDigester();
            //ReportWriter rw = new ReportWriter(getJReportFrame().getReport());
            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //rw.writeToOutputStream(baos);
            //JasperDesign jd = IReportCompiler.loadJasperDesign( new ByteArrayInputStream( baos.toByteArray() ), digester);

            // set the correct class loader
           Thread.currentThread().setContextClassLoader( IReportManager.getReportClassLoader());


            if (getJrxmlVisualView().getModel() != null &&
                getJrxmlVisualView().getModel().getJasperDesign() != null)
            {
                JasperDesign design = getJrxmlVisualView().getModel().getJasperDesign();
            
                Collection ls = JasperCompileManager.verifyDesign(design);
                Iterator iterator = ls.iterator();
                while (iterator.hasNext())
                {
                    JRValidationFault fault = (JRValidationFault)iterator.next();
                    String s = fault.getMessage();
                    //SourceLocation sl = digester.getLocation( fault.getSource() );
                    getJrxmlVisualView().getReportProblems().add( new ProblemItem(ProblemItem.WARNING, fault));
                }
            }
       } catch (Exception ex)
        {
            ex.printStackTrace();
            getJrxmlVisualView().getReportProblems().add(new ProblemItem(ProblemItem.WARNING, ex.getMessage(), null, null) );
        }
       
       Runnable runner = new Runnable(){
                public void run()
                {
                    //MainFrame.getMainInstance().getLogPane().getProblemsPanel().update();
                    ErrorHandlerTopComponent.getDefault().refreshErrors();
                }
        };

        SwingUtilities.invokeLater(runner);
        
    }

    public JrxmlVisualView getJrxmlVisualView() {
        return jrxmlVisualView;
    }

    public void setJrxmlVisualView(JrxmlVisualView jrxmlVisualView) {
        this.jrxmlVisualView = jrxmlVisualView;
    }
}
