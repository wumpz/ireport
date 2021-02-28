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
package com.jaspersoft.ireport.designer.compiler;

import com.jaspersoft.ireport.designer.IRLocalJasperReportsContext;
import com.jaspersoft.ireport.designer.IRURLClassLoader;
import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlEditorSupport;
import com.jaspersoft.ireport.designer.JrxmlPreviewView;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.ThreadUtils;
import com.jaspersoft.ireport.designer.compiler.prompt.Prompter;
import com.jaspersoft.ireport.designer.compiler.xml.SourceLocation;
import com.jaspersoft.ireport.designer.compiler.xml.SourceTraceDigester;
import com.jaspersoft.ireport.designer.connection.EJBQLConnection;
import com.jaspersoft.ireport.designer.connection.JDBCConnection;
import com.jaspersoft.ireport.designer.connection.JRDataSourceProviderConnection;
import com.jaspersoft.ireport.designer.connection.JRHibernateConnection;
import com.jaspersoft.ireport.designer.connection.MondrianConnection;
import com.jaspersoft.ireport.designer.data.queryexecuters.QueryExecuterDef;
import com.jaspersoft.ireport.designer.errorhandler.ErrorHandlerTopComponent;
import com.jaspersoft.ireport.designer.errorhandler.ProblemItem;
import com.jaspersoft.ireport.designer.export.ExporterFactory;
import com.jaspersoft.ireport.designer.logpane.IRConsoleTopComponent;
import com.jaspersoft.ireport.designer.logpane.LogTextArea;
import com.jaspersoft.ireport.designer.tools.TimeZoneWrapper;
import com.jaspersoft.ireport.designer.utils.ExpressionInterpreter;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.prefs.Preferences;
import javax.persistence.EntityManager;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import javax.xml.parsers.ParserConfigurationException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRCompiler;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JRJdtCompiler;
import net.sf.jasperreports.engine.design.JRValidationException;
import net.sf.jasperreports.engine.design.JRValidationFault;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.query.JRJpaQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.util.JRSwapFile;
import net.sf.jasperreports.olap.JRMondrianQueryExecuterFactory;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.query.JRHibernateQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlDigesterFactory;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.awt.HtmlBrowser;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Cancellable;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.xml.sax.SAXException;

/**
 * Please note that this class is totally different from the old IReportCompiler.class
 * shipped with iReport 1.
 * @author  Administrator
 */
public class IReportCompiler implements Runnable, JRExportProgressMonitor
{

   public static final int CMD_COMPILE  = 0x01;
   public static final int CMD_EXPORT   = 0x02;
   public static final int CMD_COMPILE_SCRIPTLET = 0x04;

   public static final String OUTPUT_DIRECTORY     = "OUTPUT_DIRECTORY";
   public static final String OUTPUT_FORMAT        = "OUTPUT_FORMAT";
   public static final String USE_EMPTY_DATASOURCE = "USE_EMPTY_DATASOURCE";
   public static final String USE_CONNECTION = "USE_CONNECTION";
   public static final String CONNECTION = "CONNECTION";
   public static final String SCRIPTLET_OUTPUT_DIRECTORY = "SCRIPTLET_OUTPUT_DIRECTORY";
   public static final String COMPILER = "COMPILER";
   public static final String EMPTY_DATASOURCE_RECORDS = "EMPTY_DATASOURCE_RECORDS";

   private static int executingReport = 0;
   private static String systemCpBackup = "";


   private String constTabTitle = "";

   static PrintStream myPrintStream = null;
   int filledpage=0;


   private String status="Starting";
   private IReportConnection iReportConnection;
   private int statusLevel = 0;

   private int maxBufferSize = 100000;
   private int command;
   private HashMap properties;
   private Thread thread;
   
   private FileObject file = null;

   private LogTextArea logTextArea = null;

   private String javaFile = "";
   static private StringBuffer outputBuffer = new StringBuffer();


   private Map additionalParameters = new HashMap();
   
   
   private SimpleJasperReportsContext context = null;


   
   /**
    * This is used to enable the preview tab when the report is ready...
    */
   private JrxmlEditorSupport support = null;
   
   ProgressHandle handle = null;


   /**
    * added by Felix Firgau
    */
   private static Vector compileListener = new Vector();

   /** Creates a new instance of IReportCompiler */
   public IReportCompiler()
   {
      properties = new HashMap();
      command = 0;

      try {
        maxBufferSize = Integer.parseInt( System.getProperty("ireport.maxbufsize", "100000"));
      } catch (Exception ex)
      {
          maxBufferSize = 1000000;
      }
      
      
      context = IRLocalJasperReportsContext.deriveContext();
   }

   /**
    * This method should be called in case of interruption...
    */
   public void stopThread()
   {
       command = 0;
//       if (thread != null && thread.isAlive())
//       {
//           try  {
//                thread.interrupt();
//           } catch (Exception ex)
//           {
//               ex.printStackTrace();
//           }
//       }
       cleanup();

       getLogTextArea().setTitle("Killed" + constTabTitle);
       getLogTextArea().setRemovable(true);
       System.gc();
       System.gc();
   }

   /** When an object implementing interface <code>Runnable</code> is used
    * to create a thread, starting the thread causes the object's
    * <code>run</code> method to be called in that separately executing
    * thread.
    * <p>
    * The general contract of the method <code>run</code> is that it may
    * take any action whatsoever.
    *
    * @see     java.lang.Thread#run()
    *
    */
    @SuppressWarnings({"deprecation", "unchecked"})
   public void run()
   {

      PrintStream out = System.out;
      PrintStream err = System.err;
      
      String JRXML_FILE_NAME = FileUtil.toFile(getFile()).getPath();
      
      init();
      
      try {
          
          SourceTraceDigester digester = null;
          ErrorsCollector errorsCollector = new ErrorsCollector();

          File f_report_title = FileUtil.toFile(getFile());
          constTabTitle = " [" + f_report_title.getName() + "]";

          logTextArea = IRConsoleTopComponent.getDefault().createNewLog();
          status  = "Starting";
          updateHandleStatus(status);
          logTextArea.setTitle(status + constTabTitle);

          boolean compilation_ok = true;
          long start = System.currentTimeMillis();

          // Redirect output stream....
          if (myPrintStream == null)
             myPrintStream  =new PrintStream(new FilteredStream(new ByteArrayOutputStream()));

          if (out != myPrintStream)
             System.setOut(myPrintStream);
          if (err != myPrintStream)
             System.setErr(myPrintStream);

          outputBuffer= new StringBuffer();


          String fileName = JRXML_FILE_NAME;
          String srcFileName = JRXML_FILE_NAME;
          fileName = Misc.changeFileExtension(fileName,"jasper");

          File f = new File(fileName);
          if (!IReportManager.getPreferences().getBoolean("useReportDirectoryToCompile", true))
          {
             fileName = IReportManager.getPreferences().get("reportDirectoryToCompile", ".");
             if (!fileName.endsWith(File.separator))
             {
                    fileName += File.separator;
             }
             fileName += f.getName();
          }
          
          
          JRPropertiesUtil jrPropUtils = JRPropertiesUtil.getInstance(context);
          

          /*
          String scriptletFileName = JRXML_FILE_NAME;
          String srcScriptletFileName = JRXML_FILE_NAME;
          scriptletFileName = srcScriptletFileName.substring(0,scriptletFileName.length()-1)+"Scriptlet.java";
          srcScriptletFileName = scriptletFileName;

          File f2 = new File(scriptletFileName);
          if (properties.get(IReportCompiler.SCRIPTLET_OUTPUT_DIRECTORY) != null)
          {
             scriptletFileName = (String)properties.get(IReportCompiler.SCRIPTLET_OUTPUT_DIRECTORY) + f2.separatorChar + f2.getName();
          }
          */

           String reportDirectory = new File(JRXML_FILE_NAME).getParent();

           reportDirectory = reportDirectory.replace('\\', '/');
           if(!reportDirectory.endsWith("/")){
                reportDirectory += "/";//the file path separator must be present
           }
           if(!reportDirectory.startsWith("/")){
                reportDirectory = "/" + reportDirectory;//it's important to JVM 1.4.2 especially if contains windows drive letter
           }

           if (Thread.interrupted()) throw new InterruptedException();
           
           /******************/
           /*
           if ((command & CMD_COMPILE_SCRIPTLET) != 0)
          {
             status  = "Compiling scriptlet";
             updateThreadList();
             start = System.currentTimeMillis();

             // Compile the scriptlet class...

             //String tempDirStr = System.getProperty("jasper.reports.compile.temp");
             String tempDirStr = net.sf.jasperreports.engine.util.JRProperties.getProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_TEMP_DIR);

             String oldCompileTemp  = tempDirStr;
             if (tempDirStr == null || tempDirStr.length() == 0)
             {
                tempDirStr = new File(JRXML_FILE_NAME).getParent();
             }
             File tempDirFile = new File(tempDirStr);
             javaFile = srcScriptletFileName;
             javaFile = (new File(tempDirFile,javaFile)).getPath();

             javaFile = jrf.getReport().getScriptletFileName();



             if (Misc.getLastWriteTime(javaFile) > Misc.getLastWriteTime(Misc.changeFileExtension(javaFile, "class" )))
             {
                     getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#000000\">" +
                             I18n.getFormattedString("iReportCompiler.compilingScriptlet", "Compiling scriptlet source file... {0}",
                                    new Object[]{javaFile }) + "</font>",true);
                     try
                     {
                        //JasperCompileManager.compileReportToFile(srcFileName, fileName);
                        net.sf.jasperreports.engine.design.JRJdk13Compiler compiler = new net.sf.jasperreports.engine.design.JRJdk13Compiler();
                        String errors = compiler.compileClass( new File(javaFile),   Misc.getClassPath() );
                        if (errors != null && errors.length() > 0)
                        {
                            getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#CC0000\"><b>" +
                                    I18n.getFormattedString("iReportCompiler.errorsCompilingScriptlet", "Errors compiling {0}!",
                                    new Object[]{javaFile }) +"</b></font>",true);
                            getLogTextArea().logOnConsole(errors);
                            compilation_ok = false;
                        }
                     }
                     catch (Exception ex)
                     {
                        getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#CC0000\"><b>" +
                                I18n.getString("iReportCompiler.errorsCompilingScriptletJavaSource", "Error compiling the Scriptlet java source!") +
                                "</b></font>",true);
                        StringWriter sw = new StringWriter(0);
                        ex.printStackTrace(new PrintWriter(sw));
                        myPrintStream.flush();
                        parseException( outputBuffer.toString()+sw.getBuffer()+"", null);
                        compilation_ok = false;
                     }
                     catch (Throwable ext)
                     {
                        getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#CC0000\"><b>" +
                                I18n.getString("iReportCompiler.errorsCompilingScriptletJavaSource", "Error compiling the Scriptlet java source!") +
                                "</b></font>",true);
                        StringWriter sw = new StringWriter(0);
                        ext.printStackTrace(new PrintWriter(sw));
                        myPrintStream.flush();
                        parseException( outputBuffer.toString()+sw.getBuffer()+"", null);
                        compilation_ok = false;
                     }
                     finally
                     {
                        if(mainFrame.isUsingCurrentFilesDirectoryForCompiles())
                        {

                        }//end if using current files directory for compiles
                     }//end finally
                     getLogTextArea().logOnConsole(outputBuffer.toString());
                     outputBuffer=new StringBuffer();
                     getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#0000CC\"><b>" +
                             I18n.getFormattedString("iReportCompiler.compilationRunningTime", "Compilation running time: {0,number}!",
                                    new Object[]{new Long(System.currentTimeMillis() - start)}) + "</b></font><hr>",true);
             }
          }

          if (!compilation_ok) {

              fireCompileListner(this, CL_COMPILE_FAIL, CLS_COMPILE_SCRIPTLET_FAIL);
              removeThread();
              return;
          }
          */


          
          if ((command & CMD_COMPILE) != 0)
          {
             status  = "Compiling report";
             updateHandleStatus(status);

             //System.setProperty("jasper.reports.compile.keep.java.file", "true");

//             if (IReportManager.getInstance().getProperty("KeepJavaFile","false").equals("false") )
//             {
//                    net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_KEEP_JAVA_FILE, false);
//             }
//             else
//             {
//                    net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_KEEP_JAVA_FILE, true);
//             }

             //String tempDirStr = System.getProperty("jasper.reports.compile.temp");
             String tempDirStr = jrPropUtils.getProperty(JRCompiler.COMPILER_TEMP_DIR);

             String oldCompileTemp  = tempDirStr;
             if (tempDirStr == null || tempDirStr.length() == 0)
             {
                tempDirStr = new File(JRXML_FILE_NAME).getParent();
             }
             File tempDirFile = new File(tempDirStr);
             javaFile = (new File(tempDirFile,javaFile)).getPath();

             URL img_url_comp = this.getClass().getResource("/com/jaspersoft/ireport/designer/compiler/comp1_mini.jpg");

             getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#000000\"><img align=\"right\" src=\""+  img_url_comp  +"\"> &nbsp;" +
                             Misc.formatString("Compiling to file... {0}",
                                    new Object[]{fileName}) + "</font>",true);

             
             
             
             
             try
             {
                context.setProperty(JRCompiler.COMPILER_TEMP_DIR, tempDirStr);
                context.setProperty(JRCompiler.COMPILER_CLASSPATH, Misc.nvl( new File(fileName).getParent(), ".")  + File.pathSeparator  + Misc.getClassPath());

                String compiler_name  = "JasperReports default compiler";
                String compiler_code = IReportManager.getInstance().getProperty("DefaultCompiler",null);

                JRJdtCompiler jdtCompiler = null;

                JasperDesign jd = null;

                String compatibility = IReportManager.getPreferences().get("compatibility", "");

                if (getJrxmlVisualView().getModel() != null &&
                    getJrxmlVisualView().getModel().getJasperDesign() != null &&
                    compatibility.length() == 0)
                {
                    jd = getJrxmlVisualView().getModel().getJasperDesign();
                }
                else
                {
                    jd = IReportCompiler.loadJasperDesign( new FileInputStream(srcFileName) , digester);
                }

                if (this.getProperties().get(COMPILER) != null)
                {
                    context.setProperty(JRCompiler.COMPILER_CLASS, ""+this.getProperties().get(COMPILER));
                    compiler_name = Misc.formatString("Special language compiler ({0})", new Object[]{this.getProperties().get(COMPILER)});
                }
                else if ( (jd.getLanguage() == null || jd.getLanguage().equals("java")) && jrPropUtils.getProperty("net.sf.jasperreports.compiler.java") != null
                        && jrPropUtils.getProperty("net.sf.jasperreports.compiler.java").length() > 0)
                {
                    // Use specified compiler...
                     setupClassPath(reportDirectory);
                     compiler_name = Misc.formatString("Using Java compiler: {0}", new Object[]{jrPropUtils.getProperty("net.sf.jasperreports.compiler.java")});
                     getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#000000\">"+ compiler_name +"</font>",true);
                }
                else if (compiler_code !=  null && !compiler_code.equals("0") && !compiler_code.equals(""))
                {
                    /*
                    if (compiler_code.equals("1"))
                    {
                        net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.design.JRCompiler.COMPILER_PREFIX + "java", "net.sf.jasperreports.engine.design.JRJdk13Compiler");
                        compiler_name = "Java Compiler";
                    }
                    else if (compiler_code.equals("2"))
                    {
                        net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.design.JRCompiler.COMPILER_PREFIX + "java", "it.businesslogic.ireport.compiler.ExtendedJRJdtCompiler" );
                        compiler_name = "JDT Compiler";
                        jdtCompiler = new ExtendedJRJdtCompiler();
                    }
                    else if (compiler_code.equals("3"))
                    {
                        net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.design.JRCompiler.COMPILER_PREFIX + "java", "net.sf.jasperreports.compilers.JRBshCompiler" );
                        compiler_name = "BeanShell Compiler";
                    }
                    else if (compiler_code.equals("4"))
                    {
                        net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.design.JRCompiler.COMPILER_PREFIX + "java", "net.sf.jasperreports.engine.design.JRJikesCompiler" );
                        compiler_name = "Jikes Compiler";
                    }
                    */
                }
                else
                {
                     //Force to use the jdtCompiler compiler....
                     //net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.design.JRCompiler.COMPILER_PREFIX + "java", "it.businesslogic.ireport.compiler.ExtendedJRJdtCompiler" );
                     jdtCompiler = new ExtendedJRJdtCompiler(context);
                }

                
                start = System.currentTimeMillis();

                digester = IReportCompiler.createDigester();
                
                
                if (jdtCompiler != null && (jd.getLanguage() == null || jd.getLanguage().equals("java")))
                {
                    ((ExtendedJRJdtCompiler)jdtCompiler).setDigester(digester);
                    ((ExtendedJRJdtCompiler)jdtCompiler).setErrorHandler(errorsCollector);

                    JasperReport finalJR = jdtCompiler.compileReport( jd  );

                    if (errorsCollector.getProblemItems().size() > 0 || finalJR == null)
                    {
                        throw new JRException("");
                    }
                    JRSaver.saveObject(finalJR,  fileName);

                    if (IReportManager.getPreferences().getBoolean("compile_subreports",true))
                    {
                        compileSubreports(jd, reportDirectory, true);
                    }
                    //System.out.println("Report saved..." + finalJR + " " + errorsCollector.getProblemItems().size());
                }
                else
                {
                     JasperCompileManager.compileReportToFile(jd, fileName);
                     if (IReportManager.getPreferences().getBoolean("compile_subreports",true))
                     {
                        compileSubreports(jd, reportDirectory, true);
                     }
                }

                if (errorsCollector != null && getJrxmlVisualView() != null)
                {
                    getJrxmlVisualView().setReportProblems(errorsCollector.getProblemItems() );
                    ErrorHandlerTopComponent.getDefault().refreshErrors();
                }

             }
             catch (JRValidationException e)
             {
                 compilation_ok = false;

                    for (Iterator it = e.getFaults().iterator(); it.hasNext();)
                    {
                            JRValidationFault fault = (JRValidationFault) it.next();
                            Object source = fault.getSource();
                            SourceLocation sl = digester.getLocation( source );
                            if (sl == null)
                            {
                                errorsCollector.getProblemItems().add(new ProblemItem(ProblemItem.WARNING, fault, sl) );
                            }   
                            else
                            {
                                errorsCollector.getProblemItems().add(new ProblemItem(ProblemItem.WARNING, fault, sl) );
                            }

                            //
                    }
                 
                    if (getJrxmlVisualView() != null)
                    {
                        getJrxmlVisualView().setReportProblems(errorsCollector.getProblemItems() );
                        ErrorHandlerTopComponent.getDefault().refreshErrors();
                    }
                    //getJrf().setReportProblems( errorsCollector.getProblemItems() );
                    //MainFrame.getMainInstance().getLogPane().getProblemsPanel().updateProblemsList();

                    StringWriter sw = new StringWriter(0);
                    e.printStackTrace(new PrintWriter(sw));
                    System.out.println("\n\n\n");
                    myPrintStream.flush();
                    parseException( outputBuffer.toString()+sw.getBuffer()+"", null);



             } catch (JRException jrex)
             {
                 System.out.println("Compilation exceptions: " + errorsCollector);
                 System.out.flush();
                 if (errorsCollector != null && errorsCollector.getProblemItems() != null)
                 {
                    if (getJrxmlVisualView() != null)
                    {
                        getJrxmlVisualView().setReportProblems(errorsCollector.getProblemItems() );
                        ErrorHandlerTopComponent.getDefault().refreshErrors();
                        
                    }
                    //getJrf().setReportProblems( errorsCollector.getProblemItems() );
                    //MainFrame.getMainInstance().getLogPane().getProblemsPanel().updateProblemsList();
                 }

                 compilation_ok = false;
                 getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#CC0000\"><b>" +
                             Misc.formatString( "Errors compiling {0}!",
                                    new Object[]{fileName}) + "</b></font>",true);


                StringWriter sw = new StringWriter(0);
                jrex.printStackTrace(new PrintWriter(sw));

                System.out.println("\n\n\n");
                myPrintStream.flush();
                parseException( outputBuffer.toString()+sw.getBuffer()+"", null);

             }
             catch (Exception ex)
             {
                getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#CC0000\"><b>" +
                             "Error compiling the report java source!" + "</b></font>",true);
                StringWriter sw = new StringWriter(0);
                ex.printStackTrace(new PrintWriter(sw));
                myPrintStream.flush();
                parseException( outputBuffer.toString()+sw.getBuffer()+"", null);
                compilation_ok = false;
             }
             catch (Throwable ext)
             {
                getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#CC0000\"><b>" +
                             "Error compiling the report java source!" + "</b></font>",true);
                StringWriter sw = new StringWriter(0);
                ext.printStackTrace(new PrintWriter(sw));
                myPrintStream.flush();
                parseException( outputBuffer.toString()+sw.getBuffer()+"", null);
                compilation_ok = false;
             }
             finally
             {
                
             }//end finally
             getLogTextArea().logOnConsole(outputBuffer.toString());
             outputBuffer=new StringBuffer();
             getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#0000CC\"><b>" +
                             Misc.formatString("Compilation running time: {0,number}!",
                                    new Object[]{new Long(System.currentTimeMillis() - start)}) + "</b></font><hr>",true);

             if (errorsCollector != null && errorsCollector.getProblemItems().size() > 0)
             {
                        try {
                            SwingUtilities.invokeAndWait(new Runnable() {
                                public void run() {
                                       //IRConsoleTopComponent.getDefault().setActiveLogComponent( MainFrame.getMainInstance().getLogPane().getProblemsPanel() );
                                }
                            });
                        } catch (InvocationTargetException ex) {
                            ex.printStackTrace();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
             }
          }


          if (!compilation_ok) {

              fireCompilationStatus(CompilationStatusEvent.STATUS_FAILED, CLS_COMPILE_SOURCE_FAIL);
              fireCompileListner(this, CL_COMPILE_FAIL, CLS_NONE);
              cleanup();
              handle.finish();
              showErrorConsole();
              return;
          }
          else
          {

               fireCompileListner(this, CL_COMPILE_OK, CLS_NONE);
          }

           if  ((command & CMD_EXPORT) == 0)
          {
               // Show the output console.
               showOutputConsole();
          }

          if  ((command & CMD_EXPORT) != 0)
          {

             status  = "Generating report";
             updateHandleStatus(status);
             
             String queryLanguage = "sql";
             JasperDesign jd = null;
             try {
                 jd = JRXmlLoader.load(JRXML_FILE_NAME);
                 if (jd.getQuery() != null &&
                  jd.getQuery().getText() != null)
                 {
                  queryLanguage = jd.getQuery().getText();
                 }
             } catch (Exception ex) { }

             // Try to look for a good QueryExecutor...
               List<QueryExecuterDef> configuredExecuters = IReportManager.getInstance().getQueryExecuters();
               for (QueryExecuterDef qe : configuredExecuters)
               {
                   if (qe != null && qe.getLanguage() != null && qe.getLanguage().equals( queryLanguage ))
                   {
                       context.setProperty("net.sf.jasperreports.query.executer.factory." + qe.getLanguage(), qe.getClassName());
                       getLogTextArea().logOnConsole(
                                        Misc.formatString("<font face=\"SansSerif\" size=\"3\" color=\"#000000\">Setting {0} as Query Executer Factory for language: {1}</font>\n",
                                        new Object[]{qe.getClassName(), ""+qe.getLanguage() }),true);

                       break;
                   }

               }

             // Compile report....
             JasperPrint print = null;
             URL img_url = this.getClass().getResource("/com/jaspersoft/ireport/designer/compiler/rundb1_mini.jpg");

             getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#000000\"><img align=\"right\" src=\""+  img_url  +"\"> &nbsp;" +
                     "Filling report..."+ "</font>",true);



             statusLevel = 5;
             Map hm = Prompter.promptForParameters( jd );

             hm.put("REPORT_LOCALE",  Misc.getLocaleFromString(IReportManager.getInstance().getProperty("reportLocale","")));

             img_url = this.getClass().getResource("/com/jaspersoft/ireport/designer/compiler/world.png");
             getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#000000\"><img align=\"right\" src=\""+  img_url  +"\"> &nbsp;"+
                     Misc.formatString("Locale: <b>{0}</b>",
                     new Object[]{Misc.getLocaleFromString(IReportManager.getInstance().getProperty("reportLocale", null)).getDisplayName()}) + "</font>",true);

             String reportTimeZoneId = IReportManager.getInstance().getProperty("reportTimeZone","");
             String timeZoneName = "Default";
             if (reportTimeZoneId != null && reportTimeZoneId.length() > 0 )
             {
                java.util.TimeZone tz = java.util.TimeZone.getTimeZone(reportTimeZoneId);
                hm.put("REPORT_TIME_ZONE", tz );
                timeZoneName = new TimeZoneWrapper( tz ) + "";
             }

             img_url = this.getClass().getResource("/com/jaspersoft/ireport/designer/compiler/timezone.png");
             getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#000000\"><img align=\"right\" src=\""+  img_url  +"\"> &nbsp;" +
                     Misc.formatString("Time zone: <b>{0}</b>",
                     new Object[]{timeZoneName}) + "</font>",true);



             int reportMaxCount = 0;
             try {
                 if ( IReportManager.getPreferences().getBoolean("limitRecordNumber", false) )
                 {
                     reportMaxCount = IReportManager.getPreferences().getInt("maxRecordNumber",0);
                 }
             } catch (Exception ex) {}

             if (reportMaxCount > 0)
             {
                 img_url = this.getClass().getResource("/com/jaspersoft/ireport/designer/compiler/file-info.png");
                 getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#000000\"><img align=\"right\" src=\""+  img_url  +"\"> &nbsp;" +
                     Misc.formatString("Max number of records: <b>{0,number}</b>",
                     new Object[]{new Long(reportMaxCount)}) + "</font>",true);

                 hm.put("REPORT_MAX_COUNT",  new Integer(reportMaxCount) );
             }


            // Thread.currentThread().setContextClassLoader( reportClassLoader );



             if (IReportManager.getPreferences().getBoolean("isIgnorePagination",false))
             {
                 hm.put("IS_IGNORE_PAGINATION",  Boolean.TRUE );
                 img_url = this.getClass().getResource("/com/jaspersoft/ireport/designer/compiler/file-info.png");
                 getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#000000\"><img align=\"right\" src=\""+  img_url  +"\"> &nbsp;" +
                         "Ignoring pagination" + "</font>",true);

             }
             if (IReportManager.getPreferences().getBoolean("isUseReportVirtualizer",false))
             {
                 try {


                     net.sf.jasperreports.engine.JRVirtualizer virtualizer = null;
                     String rvName = IReportManager.getInstance().getProperty("ReportVirtualizer", "JRFileVirtualizer");
                     String vrTmpDirectory = IReportManager.getInstance().getProperty("ReportVirtualizerDirectory", System.getProperty("java.io.tmpdir") );
                     int vrSize = Integer.parseInt( IReportManager.getInstance().getProperty("ReportVirtualizerSize","100"));

                     String msg = "";

                     if (rvName.equals("JRGzipVirtualizer"))
                     {
                         msg = Misc.formatString("JRGzipVirtualizer Size: {0,number}<br>",
                                                  new Object[]{new Integer(vrSize)});
                         virtualizer = new net.sf.jasperreports.engine.fill.JRGzipVirtualizer(vrSize);
                     }
                     else if (rvName.equals("JRSwapFileVirtualizer"))
                     {
                         msg = Misc.formatString("JRSwapFileVirtualizer Size: {0,number} Swap directory: {1};<br>" +
                                                 "  ReportVirtualizerBlockSize: {2}<br>ReportVirtualizerGrownCount: {3}<br>",
                                                       new Object[]{new Integer(vrSize), vrTmpDirectory,
                                                                    IReportManager.getInstance().getProperty("ReportVirtualizerBlockSize","100"),
                                                                    IReportManager.getInstance().getProperty("ReportVirtualizerGrownCount","100")});

                         JRSwapFile swapFile = new JRSwapFile(vrTmpDirectory,
                                 Integer.parseInt( IReportManager.getInstance().getProperty("ReportVirtualizerBlockSize","100")),
                                 Integer.parseInt( IReportManager.getInstance().getProperty("ReportVirtualizerGrownCount","100")));
                         virtualizer = new net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer(vrSize,swapFile);
                     }
                     else // default if (rvName.equals("JRFileVirtualizer"))
                     {
                         msg = Misc.formatString("JRFileVirtualizer Size: {0,number} Swap directory: {1};<br>",
                                                       new Object[]{new Integer(vrSize),vrTmpDirectory});
                        virtualizer = new net.sf.jasperreports.engine.fill.JRFileVirtualizer(vrSize, vrTmpDirectory );
                     }

                     img_url = this.getClass().getResource("/com/jaspersoft/ireport/designer/compiler/file-info.png");
                     getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#000000\"><img align=\"right\" src=\""+  img_url  +"\"> &nbsp;"
                             + "Using report virtualizer... " +  msg+ "</font>",true);

                     hm.put("REPORT_VIRTUALIZER", virtualizer );

                 } catch (Throwable ex)
                 {
                     getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#660000\">" +
                             "WARNING: Report virtualizer not available." +
                             "</font>",true);

                 }
             }

             // Now we add the paramaters contributed from the outside
             // by listeners of this compiler (i.e. the JasperServer plugin set a specific file resolver when the report
             // has the property "ireport.jasperserver.url"  set to some value...
             if (getAdditionalParameters().size() > 0)
             {
                 hm.putAll(getAdditionalParameters());
                 getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#000000\">Added additional parameters.</font>",true);
             }
             

             if (Thread.interrupted()) throw new InterruptedException();
             start = System.currentTimeMillis();

             if (properties.get(USE_EMPTY_DATASOURCE) != null && properties.get(USE_EMPTY_DATASOURCE).equals("true"))
             {
                try
                {
                   int records = 1;
                   try {

                       records = ((Integer)properties.get(EMPTY_DATASOURCE_RECORDS)).intValue();
                   } catch (Exception ex)
                   {
                       records = 1;
                   }

                   print = JasperFillManager.fillReport(fileName,hm,new JREmptyDataSource(records));


                }
                catch (OutOfMemoryError ex)
                {
                    getLogTextArea().logOnConsole(
                            "Out of memory exception!\n"
                            );
                }
                catch (Exception ex)
                {
                   getLogTextArea().logOnConsole(
                           Misc.formatString("Error filling print... {0}\n",
                                             new Object[]{ex.getMessage()}));

                   ex.printStackTrace();
                   getLogTextArea().logOnConsole(outputBuffer.toString());
                   outputBuffer = new StringBuffer();
                   
                   showErrorConsole();
                   
                }
             }
             else if (properties.get(USE_CONNECTION) != null && properties.get(USE_CONNECTION).equals("true"))
             {
                IReportConnection connection = (IReportConnection)properties.get(CONNECTION);

                try
                {
                   hm = connection.getSpecialParameters( hm );

                   if (connection.isJDBCConnection())
                   {
                       Connection con = connection.getConnection();
                       try {
                          print = JasperFillManager.fillReport(fileName,hm, con);
                       } catch (Exception ex)
                       {
                           throw ex;
                       } finally
                       {
                           // FIXMEGT This way of closing connection based on the connection class is not very clean...

                           if (connection instanceof JDBCConnection)
                           {
                                if (con != null) try {  con.close(); } catch (Exception ex) { }
                           }
                       }
                   }
                   else if (connection.isJRDataSource())
                   {

                       JRDataSource ds = null;
                       JasperReport jasper_report_obj =  (JasperReport)JRLoader.loadObject(new File(fileName));
                       if (connection instanceof JRDataSourceProviderConnection)
                       {
                            ds = ((JRDataSourceProviderConnection) connection).getJRDataSource(jasper_report_obj);

                            if (ds == null) return;
                            print = JasperFillManager.fillReport(jasper_report_obj,hm,ds);

                            try { ((JRDataSourceProviderConnection)connection).disposeDataSource(); } catch (Exception ex) {

                                getLogTextArea().logOnConsole(
                                      Misc.formatString("Error closing datasource: {0}\n",
                                                       new Object[]{ex.getMessage()}) );

                            }
                       }
                       else
                       {
                           ds = connection.getJRDataSource(jasper_report_obj);
                           print = JasperFillManager.fillReport(fileName,hm,ds);
                       }
                   }
                   else
                   {
                       if (connection instanceof JRHibernateConnection)
                       {
                           Session session = null;
                           Transaction transaction = null;
                           System.out.println();
                           getLogTextArea().logOnConsole(
                                      "Hibernate session opened");

                           try {
                                session = ((JRHibernateConnection)connection).createSession();
                                transaction = session.beginTransaction();
                                hm.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
                                print = JasperFillManager.fillReport(fileName,hm);

                           } catch (Exception ex)
                           {
                               throw ex;
                           } finally
                           {
                                if (transaction != null) try {  transaction.rollback(); } catch (Exception ex) { }
                                if (session != null) try {  session.close(); } catch (Exception ex) { }
                           }
                       }
                       else if (connection instanceof EJBQLConnection)
                       {
                           EntityManager em = null;
                           try {

                               getLogTextArea().logOnConsole(
                                      "Creating entity manager");

                                em = ((EJBQLConnection)connection).getEntityManager();
                                hm.put(JRJpaQueryExecuterFactory.PARAMETER_JPA_ENTITY_MANAGER, em);
                                //Thread.currentThread().setContextClassLoader( reportClassLoader );
                                print = JasperFillManager.fillReport(fileName,hm);

                           } catch (Exception ex)
                           {
                               throw ex;
                           } finally
                           {
                               getLogTextArea().logOnConsole(
                                     "Closing entity manager");
                                ((EJBQLConnection)connection).closeEntityManager();
                           }
                       }
                       else if (connection instanceof MondrianConnection)
                       {
                           mondrian.olap.Connection mCon = null;
                           try {
                               getLogTextArea().logOnConsole(
                                      "Opening Mondrian connection");
                                mCon = ((MondrianConnection)connection).getMondrianConnection();
                                hm.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION, mCon);
                                //Thread.currentThread().setContextClassLoader( reportClassLoader );
                                print = JasperFillManager.fillReport(fileName,hm);

                           } catch (Exception ex)
                           {
                               throw ex;
                           } finally
                           {
                               getLogTextArea().logOnConsole(
                                      "Closing Mondrian connection");
                                ((MondrianConnection)connection).closeMondrianConnection();
                           }
                       }
                       else // Query Executor mode...
                       {
                           //Thread.currentThread().setContextClassLoader( reportClassLoader );
                           print = JasperFillManager.fillReport(fileName,hm);
                       }
                   }

                   fireCompileListner(this, CL_FILL_OK, CLS_NONE);

                } catch (Exception ex)
                {

                   getLogTextArea().logOnConsole(
                           Misc.formatString("Error filling print... {0}\n",
                                                       new Object[]{ex.getMessage()}));
                   ex.printStackTrace();
                   getLogTextArea().logOnConsole(outputBuffer.toString());
                   outputBuffer = new StringBuffer();
                   
                   showErrorConsole();
                   fireCompileListner(this, CL_FILL_FAIL, CLS_NONE);
                }
                catch (Throwable ext)
                {
                    getLogTextArea().logOnConsole(
                           Misc.formatString("Error filling print... {0}\n",
                                                       new Object[]{ext + " " + ext.getCause()}));
                   ext.printStackTrace();
                   getLogTextArea().logOnConsole(outputBuffer.toString());
                   outputBuffer = new StringBuffer();
                   
                   getJrxmlPreviewView().setJasperPrint(null);
                   showErrorConsole();
                   fireCompileListner(this, CL_FILL_FAIL, CLS_NONE);
                }
                finally
                {
                    connection.disposeSpecialParameters(hm);
                    if (connection != null && connection instanceof JRDataSourceProviderConnection)
                    {
                            try { ((JRDataSourceProviderConnection)connection).disposeDataSource(); } catch (Exception ex) {
                                getLogTextArea().logOnConsole(
                                      Misc.formatString("Error closing datasource: {0}\n",
                                                       new Object[]{ex.getMessage()}) );
                            }

                    }

                }
             }
             net.sf.jasperreports.view.JRViewer jrv = null;
             net.sf.jasperreports.engine.JRExporter exporter=null;

             getLogTextArea().logOnConsole(outputBuffer.toString());
             outputBuffer = new StringBuffer();
             
             if (Thread.interrupted()) throw new InterruptedException();

             if (print != null)
             {
                getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#0000CC\">" +
                             Misc.formatString("<b>Report fill running time: {0,number}!</b> (pages generated: {1,number})",
                                    new Object[]{new Long(System.currentTimeMillis() - start), new Integer((print.getPages()).size())}) + "</font><hr>",true);

                status  = "Exporting report";
                updateHandleStatus(status);
                start = System.currentTimeMillis();
                String  format = IReportManager.getPreferences().get("output_format","");
                String  viewer_program = "";

                //getLogTextArea().logOnConsole(properties.get(OUTPUT_FORMAT) + "Exporting\n");
                getLogTextArea().logOnConsole(outputBuffer.toString());
                outputBuffer = new StringBuffer();

                String exportingMessage = "";

                try
                {
                   if (format != null && format.trim().length() > 0)
                   {
                       List<ExporterFactory> expFactories = IReportManager.getInstance().getExporterFactories();
                       for (ExporterFactory expFactory : expFactories)
                       {
                          if (format.equals(expFactory.getExportFormat()))
                          {
                              exporter = expFactory.createExporter(context);
                              fileName = Misc.changeFileExtension(fileName,expFactory.getExporterFileExtension());
                              if (exporter == null)
                              {
                                  exportingMessage = Misc.formatString("Exporting {0}...", new Object[]{expFactory.getExportFormatDisplayName()});
                              }
                              else
                              {
                                exportingMessage = Misc.formatString("Exporting {0} to file...  {1}!", new Object[]{expFactory.getExportFormatDisplayName(),fileName});
                                viewer_program = expFactory.getViewer();
                              }
                          }
                       }
                   }
                   else
                   {

                      exportingMessage = "Viewing with JasperReports Viewer";
                      exporter = null;
                      viewer_program = null;
                   }
    //               else if (format.equalsIgnoreCase("txt"))
    //               {
    //                  exporter = new  it.businesslogic.ireport.export.JRTxtExporter();
    //
    //                  if (IReportManager.getInstance().getProperty("TXT_PAGE_ROWS") != null)
    //                  { exporter.setParameter( it.businesslogic.ireport.export.JRTxtExporterParameter.PAGE_ROWS, IReportManager.getInstance().getProperty("TXT_PAGE_ROWS") ); }
    //                  if (IReportManager.getInstance().getProperty("TXT_PAGE_COLUMNS") != null)
    //                  { exporter.setParameter( it.businesslogic.ireport.export.JRTxtExporterParameter.PAGE_COLUMNS, IReportManager.getInstance().getProperty("TXT_PAGE_COLUMNS") ); }
    //                  if (IReportManager.getInstance().getProperty("TXT_ADD_FORM_FEED") != null)
    //                  { exporter.setParameter( it.businesslogic.ireport.export.JRTxtExporterParameter.ADD_FORM_FEED, new Boolean(IReportManager.getInstance().getProperty("TXT_ADD_FORM_FEED"))); }
    //
    //                  fileName = Misc.changeFileExtension(fileName,"txt");
    //                  exportingMessage = Misc.formatString("Exporting txt (iReport) to file... {0}!",  new Object[]{fileName});
    //                  viewer_program = Misc.nvl( IReportManager.getInstance().getProperty("ExternalTXTViewer"), "");
    //               }
   

                   img_url = this.getClass().getResource("/com/jaspersoft/ireport/designer/compiler/printer_mini.png");

                   getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\"><img align=\"right\" src=\""+  img_url  +"\"> &nbsp;" + exportingMessage + "</font>",true);

                   //((JrxmlPreviewView)getSupport().getDescriptions()[2]).setJasperPrint(null);
                          
                   if (print.getPages().size() == 0)
                   {
                      try {
                      SwingUtilities.invokeLater(new Runnable() {
                          public void run() {
                                  JOptionPane.showMessageDialog(getLogTextArea(), "The document has no pages");
                          }
                      });
                      } catch (Exception ex){}
                   }
                    else
                    {
                      final JasperPrint thePrint = print;
                      ThreadUtils.invokeInAWTThread(
                              new Runnable()
                              {
                                    public void run()
                                    {
                                        ((JrxmlPreviewView)getSupport().getDescriptions()[2]).setJasperPrint(thePrint);
                                        ((JrxmlPreviewView)getSupport().getDescriptions()[2]).requestVisible();
                                        ((JrxmlPreviewView)getSupport().getDescriptions()[2]).requestActive();
                                        ((JrxmlPreviewView)getSupport().getDescriptions()[2]).updateUI();
                                    }
                      }, true);
                      //JasperViewer jasperViewer = new JasperViewer(print,false);
                      //jasperViewer.setTitle("iReport JasperViewer");
                      //jasperViewer.setVisible(true);

                    }
                   
                   if (exporter != null)
                   {

                       // Save all properties...
                       Map<String,String> oldProperties = context.getProperties();

                       try {
                              //Adding final common properties...
                              configureExporter(exporter);

                              exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,fileName);
                              exporter.setParameter(JRExporterParameter.JASPER_PRINT,print);
                              exporter.setParameter(JRExporterParameter.PROGRESS_MONITOR, this);
                              //exporter.setParameter(JRExporterParameter.CLASS_LOADER, Thread.currentThread().getContextClassLoader() );

                              exporter.exportReport();
                       } finally
                       {
                            // Restore properties
                            for (Iterator<String> it = oldProperties.keySet().iterator(); it.hasNext();)
                            {
                                String key = it.next();
                                        
                                context.setProperty(key, oldProperties.get(key));
                            }
                            
                            //for (PropertySuffix p : oldProperties)
                            //{
                            //    JRProperties.setProperty(p.getKey(), p.getValue());
                            //}
                       }

                       getLogTextArea().logOnConsole(outputBuffer.toString());
                      outputBuffer = new StringBuffer();
                      fireCompileListner(this, CL_EXPORT_OK, CLS_NONE);

                   }
    //               else if (format.equalsIgnoreCase("java2D") )
    //               {
    //                   if (print.getPages().size() == 0)
    //                  {
    //                      try {
    //                      SwingUtilities.invokeLater(new Runnable() {
    //                          public void run() {
    //                                  JOptionPane.showMessageDialog( getLogTextArea()  , "The document has no pages");
    //                          }
    //                      });
    //                      } catch (Exception ex){}
    //                  }
    //                  else
    //                  {
    //                    PagesFrame pd = new PagesFrame(print);
    //                    pd.setVisible(true);
    //                  }
    //               }
                   
                } catch (Throwable ex2)
                {

                   getLogTextArea().logOnConsole(
                           Misc.formatString("Error exporting print... {0}\n",
                                                       new Object[]{ex2.getMessage()}));
                   ex2.printStackTrace();
                   getLogTextArea().logOnConsole(outputBuffer.toString());
                   outputBuffer = new StringBuffer();
                   fireCompileListner(this, CL_EXPORT_FAIL, CLS_NONE);
                   
                }

                getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#0000CC\"><b>" +
                             Misc.formatString("Export running time: {0,number}!",
                                    new Object[]{new Long(System.currentTimeMillis() - start), new Integer((print.getPages()).size())}) + "</b></font><hr>",true);

                // Export using the rigth program....

                if (Thread.interrupted()) throw new InterruptedException();
                
                Runtime rt = Runtime.getRuntime();
                if (viewer_program == null || viewer_program.equals(""))
                {

                   if (format.equalsIgnoreCase("") || format.equalsIgnoreCase("java2D"))
                   {
                        
                   }
                   else
                   {
                       // Try to use an internal viewer before give up
                        try {
                                   if (fileName.toLowerCase().endsWith(".html"))
                                   {
                                       HtmlBrowser.URLDisplayer.getDefault().showURL( new File(fileName).toURI().toURL() );
                                   }
                                   else if (fileName.toLowerCase().endsWith(".txt") ||
                                            fileName.toLowerCase().endsWith(".csv") ||
                                            fileName.toLowerCase().endsWith(".xml") ||
                                            fileName.toLowerCase().endsWith(".jrpxml"))
                                   {

                                       // Open default browser
                                       FileObject fo = FileUtil.toFileObject(new File(fileName));
                                       DataObject dobj = DataObject.find(fo);
                                       if (dobj != null)
                                       {
                                           dobj.getCookie(OpenCookie.class).open();
                                       }
                                   }
                                   else
                                   {
                                       // try using the default OS specific tricks...
                                      if (Utilities.isWindows())
                                      {
                                        viewer_program = "rundll32 SHELL32.DLL,ShellExec_RunDLL";
                                      }
                                      else if (Utilities.isMac())
                                      {
                                        viewer_program = "open";
                                      }
                                   }

                        }
                        catch (Exception ex) {

                            ex.printStackTrace();
                        }

                      
                   }
                }

                // Retry after the first pass....
                if (viewer_program != null && !viewer_program.equals(""))
                {
                   try
                   {
                       String execute_string = "";
                       
                       if (viewer_program.indexOf("{URL}") >= 0)
                       {
                           viewer_program = Misc.string_replace("\""+fileName+"\"","{URL}", viewer_program);
                       }
                       else
                       {
                        viewer_program += " \""+fileName+"\"";
                       }
                          getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\">" +
                             Misc.formatString("Executing: {0}",
                                    new Object[]{viewer_program}) + "</font>",true);

                       rt.exec(Misc.getTokens(viewer_program));

                       /*
                      if (Utilities.isWindows())
                      {
                        rt.exec( execute_string );
                      }
                      else if (Utilities.isUnix() || Utilities.isMac())
                      {
                          rt.exec(new String[]{viewer_program, fileName});
                      }

                        */
                   } catch (Exception ex)
                   {

                      getLogTextArea().logOnConsole("Error viewing report...\n");
                      ex.printStackTrace();
                      getLogTextArea().logOnConsole(outputBuffer.toString());
                      outputBuffer = new StringBuffer();
                   }
                   //getLogTextArea().logOnConsole("Finished...\n");
                }
                else if (format == null || (!format.equalsIgnoreCase("") && !format.equalsIgnoreCase("java2D")))
                {
                    getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\">" +
                              "No external viewer specified for this type of print. Set it in the options frame!" +
                              "</font>",true);
                }
             }
             else
             {
                 getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\">" +
                             "Print not filled. Try to use an EmptyDataSource..." + "</font>",true);
                getLogTextArea().logOnConsole("\n");
             }
          }

          fireCompilationStatus(CompilationStatusEvent.STATUS_COMPLETED, CLS_NONE);
          
          cleanup();
          handle.finish();

          
     } catch (InterruptedException ex)
     {
         stopThread();
     } finally
     {
          System.gc();
          System.setOut(out);
          System.setErr(err);   
          System.gc();
     }
      
   }


   
   /** Getter for property command.
    * @return Value of property command.
    *
    */
   public int getCommand()
   {
      return command;
   }

   /** Setter for property command.
    * @param command New value of property command.
    *
    */
   public void setCommand(int command)
   {
      this.command = command;
   }

   /** Getter for property iReportConnection.
    * @return Value of property iReportConnection.
    *
    */
   public IReportConnection getIReportConnection()
   {
      return iReportConnection;
   }

   /** Setter for property iReportConnection.
    * @param iReportConnection New value of property iReportConnection.
    *
    */
   public void setIReportConnection(IReportConnection iReportConnection)
   {
      this.iReportConnection = iReportConnection;
   }

   
   /** Getter for property properties.
    * @return Value of property properties.
    *
    */
   public HashMap getProperties()
   {
      return properties;
   }

   /** Setter for property properties.
    * @param properties New value of property properties.
    *
    */
   public void setProperties(HashMap properties)
   {
      this.properties = properties;
   }

    @Override
   public String toString()
   {
      return status;
   }

    private void compileSubreports(JasperDesign jd, String reportDir) throws JRException
    {
        compileSubreports(jd, reportDir, false);
    }

    private void compileSubreports(JasperDesign jd, String reportDir, boolean first) throws JRException {

        boolean foundSubreport = false;
        List<JRDesignElement> elements = ModelUtils.getAllElements(jd);
        for (JRDesignElement element : elements)
        {
            if (element instanceof JRDesignSubreport)
            {
                if (!foundSubreport && first)
                {
                   URL img_url_comp = this.getClass().getResource("/com/jaspersoft/ireport/designer/compiler/comp1_mini.jpg");
                   getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#000000\"><img align=\"right\" src=\""+  img_url_comp  +"\"> &nbsp;Compiling subreports....</font>",true);
                   foundSubreport = true;
                }
                JRDesignSubreport subreport = (JRDesignSubreport)element;
                File f = null;

                try {
                    f = Misc.locateFileFromExpression(jd, null, (JRDesignExpression) subreport.getExpression(), new File(reportDir), ".jrxml", null);
                } catch (Exception ex)
                {
                    URL img_url_warning = this.getClass().getResource("/com/jaspersoft/ireport/designer/resources/errorhandler/warning.png");
                    getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#000000\"><img align=\"right\" src=\""+  img_url_warning  +"\"> &nbsp;" +
                                "Unable to locate the subreport with expression: \"<code>" + Misc.getExpressionText(subreport.getExpression()) + "</code>\".</font>",true);

                    return;
                }
                        //locateSubreport(jd, subreport, new File(reportDir), null);

                long lastModified = f.lastModified();
                String jasper = Misc.changeFileExtension(f.getPath(), ".jasper");
                File jasperFile = new File(jasper);
                if (jasperFile.exists() && jasperFile.lastModified() > lastModified)
                {
                    URL img_url_info = this.getClass().getResource("/com/jaspersoft/ireport/designer/resources/errorhandler/information.png");
                    getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#000000\"><img align=\"right\" src=\""+  img_url_info  +"\"> &nbsp;" +
                            "Subreport " + f.getPath() + " already compiled.</font>",true);
                }
                else
                {
                    try {
                        JasperCompileManager.compileReportToFile(f.getPath(), jasper);
                        URL img_url_info = this.getClass().getResource("/com/jaspersoft/ireport/designer/resources/errorhandler/information.png");
                         getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#000000\"><img align=\"right\" src=\""+  img_url_info  +"\"> &nbsp;" +
                            "Subreport " + f.getPath() + " compiled.</font>",true);
                    } catch (JRException ex) {

                        URL img_url_error = this.getClass().getResource("/com/jaspersoft/ireport/designer/resources/errorhandler/error.png");
                        try {
                            getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#000000\"><img align=\"right\" src=\"" + img_url_error + "\"> &nbsp;" + "An error has accurred compiling the subreport: <a href=\"" + f.toURI().toURL() + "\">" + f.getPath() + "</a></font>", true);
                        } catch (MalformedURLException ex1) {
                        }

                        throw new JRException("An error has accurred compiling the subreport: " + f.getPath(), ex);
                    }
                    compileSubreports(JRXmlLoader.load(f), f.getParent());
                }
            }
        }


    }

    /**
     * Return an array of files: File[0] is the jrxml, File[1] is the optional jasper file.
     * Return null in case of error.
     * @param jasperDesign
     * @param subreport
     * @param reportDir
     * @param cl
     * @return
     */
    public File locateSubreport(JasperDesign jasperDesign, JRDesignSubreport subreport, File reportFolder, ClassLoader cl)
    {
        if (subreport.getExpression() == null ||
                subreport.getExpression().getValueClassName() == null ||
                !subreport.getExpression().getValueClassName().equals("java.lang.String"))
        {
           // Return default image...
           // Unable to resolve the subreoport jrxml file...
            return null;
        }
        if (cl == null) cl = Thread.currentThread().getContextClassLoader();

        JRDesignDataset dataset =  jasperDesign.getMainDesignDataset();

        try {

            // Try to process the expression...
            ExpressionInterpreter interpreter = new ExpressionInterpreter(dataset, cl);

            Object ret = interpreter.interpretExpression( subreport.getExpression().getText() );

            if (ret != null)
            {
                String resourceName = ret + "";
                if (resourceName.toLowerCase().endsWith(".jasper"))
                {
                    resourceName = resourceName.substring(0, resourceName.length() -  ".jasper".length());
                    resourceName += ".jrxml";
                }

                if (!resourceName.toLowerCase().endsWith(".jrxml"))
                {
                    return null;
                }

                File f = new File(resourceName);
                if (!f.exists())
                {
                    URL[] urls = new URL[]{};
                    if (reportFolder != null)
                    {
                        urls = new URL[]{ reportFolder.toURI().toURL()};
                    }
                    IRURLClassLoader urlClassLoader = new IRURLClassLoader(urls, cl);
                    URL url = urlClassLoader.getResource(resourceName);
                    if (url == null)
                    {
                        return null;
                    }

                    f = new File(url.toURI().getPath());
                    if (f.exists())
                    {
                        return f;
                    }
                    else
                    {
                        return null;
                    }
                }
                else
                {
                    return f;
                }

             }
        } catch (Throwable ex) {

        }
        return null;
    }


    private void configureExporter(JRExporter exporter) {

        Preferences pref = IReportManager.getPreferences();
        
        JRPropertiesUtil jrPropUtils = JRPropertiesUtil.getInstance(context);

        exporter.setParameter(JRExporterParameter.IGNORE_PAGE_MARGINS, pref.getBoolean(JRExporterParameter.PROPERTY_IGNORE_PAGE_MARGINS, jrPropUtils.getBooleanProperty(JRExporterParameter.PROPERTY_IGNORE_PAGE_MARGINS)));
        int pageMode = pref.getInt(JRPropertiesUtil.PROPERTY_PREFIX + "export.printrange", 0);

        if (pageMode == 1)
        {
            exporter.setParameter(JRExporterParameter.PAGE_INDEX,  pref.getInt(JRPropertiesUtil.PROPERTY_PREFIX + "export.printrange.index", 1));
        }
        else if (pageMode == 2)
        {
            exporter.setParameter(JRExporterParameter.START_PAGE_INDEX,  pref.getInt(JRPropertiesUtil.PROPERTY_PREFIX + "export.printrange.from", 1));
            exporter.setParameter(JRExporterParameter.END_PAGE_INDEX,  pref.getInt(JRPropertiesUtil.PROPERTY_PREFIX + "export.printrange.to", 1));
        }

        String encoding = pref.get(JRExporterParameter.PROPERTY_CHARACTER_ENCODING, jrPropUtils.getProperty(JRExporterParameter.PROPERTY_CHARACTER_ENCODING));
        if (encoding != null)
        {
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, encoding);
        }

        if (pref.getInt(JRPropertiesUtil.PROPERTY_PREFIX + "export.offset.x", 0) > 0)
        {
            exporter.setParameter(JRExporterParameter.OFFSET_X, pref.getInt(JRPropertiesUtil.PROPERTY_PREFIX + "export.offset.x", 0));
        }
        if (pref.getInt(JRPropertiesUtil.PROPERTY_PREFIX + "export.offset.y", 0) > 0)
        {
            exporter.setParameter(JRExporterParameter.OFFSET_Y, pref.getInt(JRPropertiesUtil.PROPERTY_PREFIX + "export.offset.y", 0));
        }
    }

    /**
     * Setting up the classpath in the JRProperties. This is useful only when using not standard java compiler.
     * The classpath is not set if the java compiler class is not set in the JR properties, since in this case
     * the context classloader is used instead...
     */
    private void setupClassPath(String reportDirectory) {
        
            JRPropertiesUtil jrPropUtils = JRPropertiesUtil.getInstance(context);
            
            if ( jrPropUtils.getProperty("net.sf.jasperreports.compiler.java") != null
                 && jrPropUtils.getProperty("net.sf.jasperreports.compiler.java").length() > 0)
            {
               //String classpath = System.getProperty("jasper.reports.compile.class.path");
               String oldClasspath = jrPropUtils.getProperty(JRCompiler.COMPILER_CLASSPATH);
               if (oldClasspath == null) oldClasspath="";
               String classpath = "";

               ArrayList<String> oldCp = new ArrayList<String>();
               String[] paths = oldClasspath.split(File.pathSeparator);
               for (int idx = 0; idx < paths.length; idx++) {
                   oldCp.add(paths[idx]);
               }

               //We should add to the classpath all the required jar in modules/ext too...
               File libDir = InstalledFileLocator.getDefault().locate("modules/ext", null, false);

                // find a jar called jasperreports-extensions-*.jar
                if (libDir != null && libDir.isDirectory())
                {
                    File[] jars = libDir.listFiles();

                    for (int i=0; i<jars.length; ++i)
                    {
                        if (oldCp.contains(jars[i].getPath())) continue;
                        oldCp.add(jars[i].getPath());
                    }
                }


               if (!oldCp.contains(reportDirectory))
               {
                    classpath += ((oldCp.size() > 0) ? File.pathSeparator : "" ) + reportDirectory + File.pathSeparator;
               }

               for (String cpItem : oldCp)
               {
                   classpath += cpItem + File.pathSeparator;
               }

               // Concatenate the iReport classpath...
               List<String> cp = IReportManager.getInstance().getClasspath();
               for (String cpItem : cp)
               {
                   if (!oldCp.contains(cpItem))
                   {
                    classpath += cpItem + File.pathSeparator;
                   }
               }

               context.setProperty(JRCompiler.COMPILER_CLASSPATH, classpath);
               System.setProperty("java.class.path", classpath);
           }
    }

    private void updateHandleStatus(String status) {
        handle.setDisplayName( file.getName() + " (" + status + ")");
        fireCompilationStatus(CompilationStatusEvent.STATUS_RUNNING, status);
    }

    /**
     * @return the additionalParameters
     */
    public Map getAdditionalParameters() {
        return additionalParameters;
    }

    /**
     * @param additionalParameters the additionalParameters to set
     */
    public void setAdditionalParameters(Map additionalParameters) {
        this.additionalParameters = additionalParameters;
    }

   class FilteredStream extends FilterOutputStream
   {
      public FilteredStream(OutputStream aStream)
      {
         super(aStream);
      }

        @Override
      public void write(byte b[]) throws IOException
      {
         String aString = new String(b);
         outputBuffer.append( aString );

         if (outputBuffer.length() > maxBufferSize) // 5000000
         {
             outputBuffer = outputBuffer.delete(0, outputBuffer.length()-maxBufferSize);
         }
      }

        @Override
      public void write(byte b[], int off, int len) throws IOException
      {
         String aString = new String(b , off , len);
         outputBuffer.append( aString );
         if (outputBuffer.length() > maxBufferSize)
         {
             outputBuffer = outputBuffer.delete(0, outputBuffer.length()-maxBufferSize);
         }
         //getLogTextArea().logOnConsole(aString);
      }
   }
   
   /**
    * This method should be called by run when the process is started.
    */
   protected void init()
   {
       thread = Thread.currentThread();

       synchronized(this) {
          executingReport++;
          if (executingReport == 1)
          {
              this.systemCpBackup = System.getProperty("java.class.path");
          }
       }

       // Print all the properties...
//       System.out.println("Prop3: net.sf.jasperreports.xpath.executer.factory: "  + JRProperties.getProperty("net.sf.jasperreports.xpath.executer.factory"));
//       System.out.flush();

       handle = ProgressHandleFactory.createHandle(status,
               new Cancellable() {
                public boolean cancel() {
                    thread.interrupt();
                    return true;
                }
        });
        
        updateHandleStatus(status);
        handle.start();
       
       //using the report directory to load classes and resources
	try{
		  String reportDirectory = FileUtil.toFile(getFile()).getParent();

		  //include report directory for resource search path
          URL reportDirectoryUrl = new File(reportDirectory).toURI().toURL();


          ClassLoader urlClassLoader = null;
          String reportCompileDir = IReportManager.getPreferences().get("reportDirectoryToCompile", ".");
          File reportCompileDirFile = new File(reportCompileDir);
          if ((reportCompileDirFile.exists()) && !IReportManager.getPreferences().getBoolean("useReportDirectoryToCompile", true))
          {
              urlClassLoader = new IRURLClassLoader(new URL[]{
                   reportDirectoryUrl,reportCompileDirFile.toURI().toURL()},
                   IReportManager.getInstance().getReportClassLoader());
          }
          else
          {
            urlClassLoader = new IRURLClassLoader(new URL[]{
		  	  reportDirectoryUrl
            }, IReportManager.getInstance().getReportClassLoader());
          }


		  Thread.currentThread().setContextClassLoader(urlClassLoader);

                 if (getJrxmlPreviewView() != null)
                 {
                     this.addCompilationStatusListener(getJrxmlPreviewView());
                 }
                  
                  
        } catch (MalformedURLException mue){
	  mue.printStackTrace();
	}
   }

   /**
    * This method is called by run when the process is going to end in order to
    * change the textArea status.
    */
   public void cleanup()
    {
        
        synchronized(this) {
            executingReport--;
            if (executingReport == 0)
            {
              System.setProperty("java.class.path", systemCpBackup);
              // Unuseful expensive operation... IReportManager.getInstance().reloadJasperReportsProperties();
            }
        }

        getLogTextArea().setTitle("Finished" + constTabTitle);
        getLogTextArea().setRemovable(true);
        
    }

   
   
   /**
    * Initially a way to parse output, today this method just print as html
    * the output generated by the execution
    */
   public void parseException(String exception, Vector sourceLines)
   {

      // Create a single outString...
      String outString = "";

      // For each row, looking for a file name followed by a row number...
      //javax.swing.JOptionPane.showMessageDialog(null,exception);
      StringTokenizer st = new StringTokenizer(exception, "\n");
      while (st.hasMoreElements())
      {
         String line = st.nextToken();
         outString += Misc.toHTML(line+"\n");
      }
      getLogTextArea().logOnConsole(outString,true);
      //getLogTextArea().logOnConsole( "<a href=\"http://problem\">*****<hr><font face=\"Courier New\" size=\"3\">"+ exception +"</a>", true);
      outputBuffer = new StringBuffer();
   }

   

   /**
    * This methods honors the JRExportProgressMonitor interface
    */
   public void afterPageExport() {

       filledpage++;
       if (command == 0)
       {

       }


   }

   /**
    *  The logTextArea tied to this process
    */
    public LogTextArea getLogTextArea() {
        return logTextArea;
    }

    public void setLogTextArea(LogTextArea logTextArea) {
        this.logTextArea = logTextArea;
    }

    /**
     * added by Felix Firgau
     */
    public static final int CL_COMPILE_OK = 1;
    public static final int CL_COMPILE_FAIL = 2;
    public static final int CL_FILL_OK = 3;
    public static final int CL_FILL_FAIL = 4;
    public static final int CL_EXPORT_OK = 5;
    public static final int CL_EXPORT_FAIL = 6;


    public static final String CLS_COMPILE_OK = "compileok";
    public static final String CLS_COMPILE_SCRIPTLET_FAIL = "scriptletfail";
    public static final String CLS_COMPILE_SOURCE_FAIL = "sourcefail";
    public static final String CLS_NONE = "";


    private java.util.List<CompilationStatusListener> compilationStatusListener = new java.util.ArrayList<CompilationStatusListener>();
    /**
     * (FF) addCompileListener to notify about compiling actions
     * @param listener ActionListener
     */
    @SuppressWarnings("unchecked")
    public void addCompilationStatusListener(CompilationStatusListener listener) {
      if(!compilationStatusListener.contains(listener))
        compilationStatusListener.add(listener);
    }
    
    public void removeCompilationStatusListener(CompilationStatusListener listener) {
      compilationStatusListener.remove(listener);
    }
    
    /**
     * (FF) fireCompileListner fires compiling action notifications
     * @param id int
     * @param status String
     */
    public void fireCompilationStatus(int status, String message) {
        @SuppressWarnings("unchecked")
        CompilationStatusEvent event = new CompilationStatusEvent(this, status, message );
        for (CompilationStatusListener listener : compilationStatusListener)
        {
            listener.compilationStatus(event);
        }
    }
    
    
    /**
     * (FF) addCompileListener to notify about compiling actions
     * @param listener ActionListener
     */
    @SuppressWarnings("unchecked")
    public static void addCompileListener(java.awt.event.ActionListener listener) {
      if(!compileListener.contains(listener))
        compileListener.add(listener);
    }

    /**
     * (FF) removeCompileListener removes notification
     * @param listener ActionListener
     */
    public static void removeCompileListener(java.awt.event.ActionListener listener) {
      compileListener.remove(listener);
    }

    /**
     * (FF) fireCompileListner fires compiling action notifications
     * @param id int
     * @param status String
     */
    public static void fireCompileListner(IReportCompiler ireportCompiler, int id, String status) {
        @SuppressWarnings("unchecked")
      java.awt.event.ActionListener[] list = (java.awt.event.ActionListener[])compileListener.toArray(
       new java.awt.event.ActionListener[compileListener.size()]);

      java.awt.event.ActionEvent e = new java.awt.event.ActionEvent(ireportCompiler, id, status);
      for (int i = 0; i < list.length; i++) {
        java.awt.event.ActionListener listener = list[i];
        listener.actionPerformed(e);
      }
    }
    //End FF
    
    
    
    
    
    
    /**
     * Code to load a JasperDesign. We should not load a jasperDesign, since we already have one in memory.
     * This code must be replaced at some point. Please note that this code use the SourceTraceDigester digester
     * (not used in the JrxmlLoader. We should merge this code with the one provided by JrxmlLoader.
     */
    public static JasperDesign loadJasperDesign(InputStream fileStream, SourceTraceDigester digester) throws JRException
    {
            try
            {
                    JasperDesign jd = JRXmlLoader.load(fileStream);
                    return jd;
            }
            finally
            {
                    try
                    {
                            fileStream.close();
                    }
                    catch (IOException e)
                    {
                            // ignore
                    }
            }
    }

    public static SourceTraceDigester createDigester() throws JRException
    {
            SourceTraceDigester digester = new SourceTraceDigester();
            try
            {
                    JRXmlDigesterFactory.configureDigester(digester);
            }
            catch (SAXException e)
            {
                    throw new JRException(e);
            }
            catch (ParserConfigurationException e)
            {
                    throw new JRException(e);
            }
            return digester;
    }

    
    /**
     * The file object tied to this file.
     */
    public FileObject getFile() {
        return file;
    }

    public void setFile(FileObject file) {
        this.file = file;
    }

    public JrxmlEditorSupport getSupport() {
        return support;
    }

    public void setSupport(JrxmlEditorSupport support) {
        this.support = support;
    }
    
    private void showErrorConsole()
    {
        ThreadUtils.invokeInAWTThread( new Runnable()
              {
                  public void run()
                  {
                      JrxmlVisualView view = (JrxmlVisualView)(getSupport().getDescriptions()[0]);
                      view.requestActive();
                      
                      
                      TopComponent tc =WindowManager.getDefault().findTopComponent("IRConsoleTopComponent");
                      if (tc != null) 
                      {
                          tc.requestVisible();
                          //tc.requestActive();
                      }
                      ErrorHandlerTopComponent ehtc = ErrorHandlerTopComponent.getDefault();
                      if (ehtc != null && view.getReportProblems().size() > 0) 
                      {
                          ehtc.refreshErrors();
                          ehtc.requestVisible();
                          //ehtc.requestActive();
                      }
                   }
              });
    }

    private void showOutputConsole()
    {
        ThreadUtils.invokeInAWTThread( new Runnable()
              {
                  public void run()
                  {
                      JrxmlVisualView view = (JrxmlVisualView)(getSupport().getDescriptions()[0]);
                      view.requestActive();

                      TopComponent tc =WindowManager.getDefault().findTopComponent("IRConsoleTopComponent");
                      if (tc != null)
                      {
                          tc.requestVisible();
                          //tc.requestActive();
                      }
                   }
              });
    }
    
    public JrxmlPreviewView getJrxmlPreviewView()
    {
        
        return   (JrxmlPreviewView)getSupport().getDescriptions()[2];
    }
    
    public JrxmlVisualView getJrxmlVisualView()
    {
        return   (JrxmlVisualView)getSupport().getDescriptions()[0];
    }
                   
}

