/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaspersoft.ireport.samples.db;

import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.hsqldb.Server;
import org.openide.modules.InstalledFileLocator;

public final class RunSampleDatabaseAction implements ActionListener {

    public static final int SERVER_STATE_SHUTDOWN = 16;

    private static Server server = null;
    private String s="";
    public void setValue(String s)
    {
        this.s = s;
    }

    public void actionPerformed(ActionEvent e) {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                   getServer().checkRunning(true);

                   JOptionPane.showMessageDialog(Misc.getMainFrame(), "Sample database already running", "Running sample database", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex)
                {
                    new Thread(new Runnable() {

                        public void run() 
                        {
                                    getServer().start();
                                    SwingUtilities.invokeLater(new Runnable() {

                                        public void run() {
                                            int state = getServer().getState();
                                            if (state == 1)
                                            {
                                                JOptionPane.showMessageDialog(Misc.getMainFrame(), "Sample database successfully started" + s, "Running sample database", JOptionPane.INFORMATION_MESSAGE);
                                            }
                                            else if (state == 16 || state == 8)
                                            {
                                                JOptionPane.showMessageDialog(Misc.getMainFrame(), "An error occurred while starting up the sample database. Database not running.\nCheck the log from the menu View -> IDE Log for mode details", "Running sample database", JOptionPane.INFORMATION_MESSAGE);
                                            }
                                        }
                                    });
                        }
                    }).start();
                }
            }
        });
    }

    public static Server getServer()
    {
        if (server == null)
        {
            server = new org.hsqldb.Server();
            server.setNoSystemExit(true);
            server.setRestartOnShutdown(false);
            server.setTls(false);

            // Locate the database scripts...
            File libDir = InstalledFileLocator.getDefault().locate("database", null, false);
            server.setDatabasePath(0, libDir.getPath() + File.separator + "test");
            server.setDatabaseName(0, "");
            server.setTls(false);

        }
        
        return server;
    }
}
