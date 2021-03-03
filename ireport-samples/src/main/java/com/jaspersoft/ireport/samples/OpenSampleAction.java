/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.samples;

import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.openide.filesystems.FileObject;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.NbBundle;

/**
 *
 * @version $Id: OpenSampleAction.java 0 2010-02-01 21:15:39 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class OpenSampleAction extends AbstractAction {

    private String key = null;

    public static Action createAction(FileObject fo)
    {
        OpenSampleAction action = new OpenSampleAction();
        final String s = (String) fo.getAttribute("key");
        action.putValue(Action.NAME, NbBundle.getMessage(OpenSampleAction.class, s));
        action.setKey(s);
        System.out.println("Action " + s + " created!");
        return action;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    public void actionPerformed(ActionEvent e) {

        File sampleDir = InstalledFileLocator.getDefault().locate("samples", null, false);

        if (sampleDir != null && sampleDir.exists())
        {
            sampleDir = new File(sampleDir, key );
            
            if (sampleDir.exists())
            {

                final File[] jrxmls = sampleDir.listFiles(new FilenameFilter() {

                    public boolean accept(File dir, String name) {
                       return name.toLowerCase().endsWith(".jrxml");
                    }
                });

                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        for (File f : jrxmls)
                        {
                            Misc.openFile(f);
                        }
                    }
                });
            }
            else
            {
                JOptionPane.showMessageDialog(Misc.getMainFrame(), "Unable to locate the directory "+ sampleDir.getPath());
            }

        }
        else
        {
            JOptionPane.showMessageDialog(Misc.getMainFrame(), "Unable to locate the directory " + "samples/"+key);
        }
    }

}
