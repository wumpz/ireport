/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.designer.standalone.actions;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.prefs.Preferences;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import org.openide.util.NbPreferences;

/**
 *
 * @version $Id: ImportSettingsJob.java 0 2009-12-09 15:22:35 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class ImportSettingsJob implements Runnable {
    private File dir = null;
    private ImportSettingsStatusJDialog dialog = null;
    private Thread thread = null;

    private Exception result = null;

    public ImportSettingsJob(File dir, boolean showDialog)
    {
        this.dir = dir;
        thread = new Thread(this);
        if (showDialog)
        {
            dialog = new ImportSettingsStatusJDialog(Misc.getMainFrame(), true);
            dialog.setImportSettingsJob(this);
        }
    }

    public void start()
    {
        if (dialog != null)
        {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    dialog.setVisible(true);
                }
            });
        }

        getThread().start();
    }

    public void cleanUp()
    {
        if (dialog != null)
        {
            try {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            });
            } catch (Exception ex) {}
        }
    }

    /**
     * Import the settings. If an error occurs, an exception is thrown....
     * @param dir
     * @param showDialog
     * @throws Exception
     */
    public static void importSettings(File dir, boolean showDialog) throws Exception
    {
        ImportSettingsJob job = new ImportSettingsJob(dir, showDialog);
        job.start();
        if (job.getResult() != null)
        {
            throw new Exception( job.getResult());
        }

    }

    public void run() {

        try {
            final File iReportSettingsFile = new File(getDir(), "config/Preferences/com/jaspersoft/ireport.properties");


            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    if (dialog != null) dialog.setTextFile(iReportSettingsFile.getPath());
                }
            });

            int count=0;
            // remove UUID...
            Preferences prefs = NbPreferences.forModule(IReportManager.class);
            if (iReportSettingsFile.exists())
            {
                Properties props = new java.util.Properties();
                props.load(new FileInputStream(iReportSettingsFile));

                final int total = props.size();

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (dialog != null)
                        {
                            JProgressBar bar = dialog.getProgressBar();
                            bar.setMinimum(0);
                            bar.setMaximum(total);
                            bar.setValue(0);
                        }
                    }
                });

                Enumeration enumer = props.keys();
                while (enumer.hasMoreElements())
                {
                    count++;
                    String key = (String)enumer.nextElement();
                    if (key.equals("UUID")) continue;

                    prefs.put(key, props.getProperty(key));
                    final int value = count;
                    SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                            if (dialog != null)
                            {
                                JProgressBar bar = dialog.getProgressBar();
                                bar.setValue(value);
                            }
                        }
                    });

                }
            }
            else
            {
                System.out.println(iReportSettingsFile + "  does not exist");
                    System.out.flush();
            }

            final File jasperPluginSettingsFile = new File(getDir(), "config/Preferences/com/jaspersoft/ireport/jasperserver.properties");

            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    if (dialog != null) dialog.setTextFile(iReportSettingsFile.getPath());
                }
            });

            // remove UUID...
            prefs = NbPreferences.forModule(JasperServerManager.class);
            if (jasperPluginSettingsFile.exists())
            {
                Properties props = new java.util.Properties();
                props.load(new FileInputStream(jasperPluginSettingsFile));
                final int total = props.size();

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (dialog != null)
                        {
                            JProgressBar bar = dialog.getProgressBar();
                            bar.setMinimum(0);
                            bar.setMaximum(total);
                            bar.setValue(0);
                        }
                    }
                });

                Enumeration enumer = props.keys();
                while (enumer.hasMoreElements())
                {
                    count++;
                    String key = (String)enumer.nextElement();
                    prefs.put(key, props.getProperty(key));
                    final int value = count;
                    SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                            if (dialog != null) 
                            {
                                JProgressBar bar = dialog.getProgressBar();
                                bar.setValue(value);
                            }
                        }
                    });
                }
            }
            else
            {
                System.out.println(jasperPluginSettingsFile + "  does not exist");
                    System.out.flush();
            }
        } catch (Exception ex)
        {
            this.setResult(ex);
        }

        cleanUp();

    }

    /**
     * @return the result
     */
    public Exception getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(Exception result) {
        this.result = result;
    }

    /**
     * @return the dir
     */
    public File getDir() {
        return dir;
    }

    /**
     * @param dir the dir to set
     */
    public void setDir(File dir) {
        this.dir = dir;
    }

    /**
     * @return the dialog
     */
    public ImportSettingsStatusJDialog getDialog() {
        return dialog;
    }

    /**
     * @param dialog the dialog to set
     */
    public void setDialog(ImportSettingsStatusJDialog dialog) {
        this.dialog = dialog;
    }

    /**
     * @return the thread
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * @param thread the thread to set
     */
    public void setThread(Thread thread) {
        this.thread = thread;
    }




}
