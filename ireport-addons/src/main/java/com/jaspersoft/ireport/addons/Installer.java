/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaspersoft.ireport.addons;

import com.jaspersoft.ireport.addons.layers.LayersSupport;
import com.jaspersoft.ireport.locale.I18n;
import java.util.ResourceBundle;
import org.openide.modules.ModuleInstall;
import org.openide.windows.WindowManager;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {

        ReportOpenedListener.startListening();
        I18n.addBundleLocation(ResourceBundle.getBundle("/com/jaspersoft/ireport/addons/callouts/Bundle"));

         WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

            public void run() {
                LayersSupport.getInstance();
            }
        });
        
    }
}
